package com.redorb.mcharts.data.restriction;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.utils.Utils;

/**
 * Factory for building restrictions.
 */
public class RestrictionFactory {

	/*
	 * Attributes
	 */

	private static final Logger log = LoggerFactory.getLogger(RestrictionFactory.class);
	
	private static final String PACKAGE_PREFIX = "com.redorb.mcharts.data.restriction.";

	public static final String PARAM_RESTRICTION_N = "n";

	/*
	 * Core
	 */
	
	private RestrictionFactory() {}
	
	/*
	 * Operations
	 */
	
	public static IRestriction newInstance(
			String type,
			String[] parameterNames,
			String[] parameterValues) {
		
		// gets the restriction class
		
		Class restrictionClass = null;
		
		try {
			restrictionClass = Class.forName(PACKAGE_PREFIX + type);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		}
		
		return newInstance(restrictionClass, parameterNames, parameterValues);
	}

	public static IRestriction newInstance(
			Class restrictionClass,
			String[] parameterNames,
			String[] parameterValues) {

		IRestriction restriction = null;

		try {

			// checks the sizes of input array

			if (parameterNames.length != parameterValues.length) {
				throw new Exception(I18n.getMessage("factory.parametersLength"));
			}

			// array of final values after conversion from string			

			Object[] constructorParamsObjects = new Object[parameterNames.length];

			// converts each parameter

			for (int p = 0; p < parameterNames.length; p++) {

				String parameter = parameterNames[p];
				String value = parameterValues[p];

				// treats the parameter (converts)

				if (PARAM_RESTRICTION_N.equals(parameter)) {
					constructorParamsObjects[p] = Integer.parseInt(value);
				}
				else {
					throw new Exception(I18n.getMessage(
							"factory.unknownParameter", 
							new Object[] {parameter, parameterValues[p], IRestriction.class.getName()}));
				}
			}

			// gets an array of Class from the array of Object

			Class[] constructorParamsClasses = Utils.toClassArray(constructorParamsObjects);

			// gets the constructor method

			Constructor c = restrictionClass.getConstructor(constructorParamsClasses);

			// calls the constructor

			restriction = (IRestriction)c.newInstance(constructorParamsObjects);
			
			log.debug("Restriction created : " + restriction.getClass().getName());

		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (InstantiationException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return restriction;
	}
}
