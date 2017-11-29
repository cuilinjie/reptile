package com.clj.reptilehouse.common.query;

import java.io.Serializable;
import java.util.List;

public interface Query extends Serializable {

	public abstract Query addComparator(Comparator comparator);

	public abstract Query addAllComparator(List<Comparator> comparators);

	public abstract Query removeComparator(Comparator comparator);

	public abstract List<Comparator> getComparators();

	public abstract Query setLimit(int limit);

	public abstract int getLimit();

	public abstract Sort getSort();

	public abstract void setSort(String sortName, SortDirection direction);

}