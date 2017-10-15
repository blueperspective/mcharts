package com.redorb.mcharts.data.criteria.filter;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.data.criteria.structure.AbstractBinaryCriteriaDecorator;
import com.redorb.mcharts.data.criteria.structure.AbstractListCriteriaDecorator;
import com.redorb.mcharts.data.criteria.structure.AbstractUnaryCriteriaDecorator;
import com.redorb.mcharts.data.criteria.structure.ICriteria;
import com.redorb.mcharts.data.restriction.IRestriction;
import com.redorb.mcharts.utils.Utils;

/**
 * Creates criteria from
 * - a type
 * - list of parameter names and values
 */
public class CriteriaFactory {
	
	/*
	 * Attributes
	 */
	
	private static final Logger log = LoggerFactory.getLogger(CriteriaFactory.class);
	
	private static final String PACKAGE_PREFIX = "com.redorb.mcharts.data.criteria.structure.";
	private static final String PACKAGE_CORE_PREFIX = "com.redorb.mcharts.core.";
	
	public static final String PARAM_START_DATE = "startDate";
	public static final String PARAM_END_DATE = "endDate";
	public static final String PARAM_SET_OBJECT = "setObject";
	public static final String PARAM_SET_ELEMENT = "setElement";
	public static final String PARAM_NAME = "name";
	public static final String PARAM_REG_EXP = "regexp";
	public static final String VAR_DATE = "$date";
	public static final String VAR_DATE_SEP = ":";
	
	/*
	 * Ctor
	 */
	
	private CriteriaFactory() {}
	
	/*
	 * Operations
	 */
	
	/**
	 * Creates a criteria
	 * @param type the type of criteria, which must match the criteria class
	 * @param an array of parameter names
	 * @param an array of corresponding parameters values
	 */
	public static ICriteria newInstance(
			String type,
			String[] parameterNames,
			String[] parameterValues) {

		// the returned criteria
		ICriteria criteria = null;

		try {

			// checks the sizes of input array

			if (parameterNames != null && parameterValues != null
					&& parameterNames.length != parameterValues.length) {
				throw new Exception(I18n.getMessage("factory.parametersLength"));
			}
			
			if (parameterNames == null) {
				throw new Exception("parameter names is null");
			}

			// gets the criteria class

			Class<? extends ICriteria> criteriaClass = 
					Class.forName(PACKAGE_PREFIX + type).asSubclass(ICriteria.class);

			// array of final values after conversion from string			

			Object[] constructorParamsObjects = new Object[parameterNames.length];

			// converts each parameter

			for (int p = 0; p < parameterNames.length; p++) {

				// current parameter name and associated value
				String parameter = parameterNames[p];
				String value = parameterValues[p];

				// create the real object from a string parameter

				if (PARAM_START_DATE.equals(parameter)
				|| PARAM_END_DATE.equals(parameter)) {
					
					log.debug("current param " + parameter + " is start/end date");
					
					// the value contains the variable $date
					
					if (value.contains(VAR_DATE)) {
						
						String[] values = value.split(VAR_DATE_SEP);
						
						if (values.length == 2) {
							int amount = Integer.parseInt(values[1]);
							constructorParamsObjects[p] = Utils.getNMonthDate(new Date(), amount);
							log.debug("current parameter : " + constructorParamsObjects[p] + " / amount : " + amount);
						}
					}
					else {
						constructorParamsObjects[p] = Core.getInstance().getDateFormat().parse(value);	
					}					
				}
				else if (PARAM_SET_OBJECT.equals(parameter)) {
					constructorParamsObjects[p] = Class.forName(PACKAGE_CORE_PREFIX + value);
				}
				else if (PARAM_SET_ELEMENT.equals(parameter)) {
					// TODO
				}
				else if (PARAM_NAME.equals(parameter)) {
					constructorParamsObjects[p] = value;
				}
				else if (PARAM_REG_EXP.equals(parameter)) {
					constructorParamsObjects[p] = Pattern.compile(value);
				}
				else {
					throw new Exception(I18n.getMessage(
							"factory.unknownParameter", 
							parameter, parameterValues[p], IRestriction.class.getName()));
				}
			}

			// gets an array of Class from the array of Object

			Class<?>[] constructorParamsClasses = 
					Utils.toClassArray(constructorParamsObjects);

			// gets the constructor method

			Constructor<? extends ICriteria> c = 
					criteriaClass.getConstructor(constructorParamsClasses);

			// calls the constructor

			criteria = (ICriteria)c.newInstance(constructorParamsObjects);
			
			log.debug("Criteria created : " + criteria.getClass().getName());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return criteria;
	}
	
	/**
	 * Creates a compound criteria.
	 * @param type
	 * @param criteria
	 * @return
	 */
	public static ICriteria newInstance(String type, ICriteria[] criterias) {
		
		ICriteria criteria = null;
		
		try {
			
			// gets the criteria class
			
			Class<? extends ICriteria> criteriaClass = 
					Class.forName(PACKAGE_PREFIX + type).asSubclass(ICriteria.class);
			
			// criteria are not constructed the same way depending of their
			// type : unary, binary or list
			
			if (criteriaClass.getSuperclass().equals(AbstractUnaryCriteriaDecorator.class)
			|| criteriaClass.getSuperclass().equals(AbstractBinaryCriteriaDecorator.class)) {
				// for unary and binary criteria decorator, the sub criterias are passed
				// to the constructor
				
				// gets an array of Class from the array of Object

				Class<?>[] constructorParamsClasses = Utils.toClassArray(criterias);

				// gets the constructor method

				Constructor<? extends ICriteria> c = criteriaClass.getConstructor(constructorParamsClasses);
				
				// calls the constructor

				criteria = (ICriteria) c.newInstance((Object[])criterias);
			}
			else {
				// for list criteria decorator, as the number of sub criteria is unknown
				// we call n times a add method
				
				// gets the constructor method

				Constructor<? extends ICriteria> c = criteriaClass.getConstructor();
				
				// calls the constructor

				criteria = (ICriteria) c.newInstance();
				
				// adds all criterias
				
				for (int i = 0; i < criterias.length; i++) {
					((AbstractListCriteriaDecorator)criteria).addCriteria(criterias[i]);
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return criteria;
	}
}
