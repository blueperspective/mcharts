package com.redorb.mcharts.data.criteria.structure;

import java.util.List;


public interface ICriteriaDecorator extends ICriteria {

	List<ICriteria> getCriterias();
}
