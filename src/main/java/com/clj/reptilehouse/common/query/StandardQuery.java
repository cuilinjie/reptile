package com.clj.reptilehouse.common.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a query
 * @author jiangxs
 * @since 1.0.1(Jul 4, 2015)
 */
public class StandardQuery implements Query {

	private static final long serialVersionUID = -656396755847643907L;
	private List<Comparator> comparators;
	private int limit;
	private Sort sort;
	
	public StandardQuery() {
		this.comparators = new ArrayList<Comparator>();
	}
	
	/* (non-Javadoc)
	 * @see com.champor.persistence.query.Query#addComparator(com.champor.persistence.query.Comparator)
	 */
	@Override
	public Query addComparator(Comparator comparator) {
		this.comparators.add(comparator);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.champor.persistence.query.Query#addAllComparator(java.util.List)
	 */
	@Override
	public Query addAllComparator(List<Comparator> comparators) {
		this.comparators.addAll(comparators);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.champor.persistence.query.Query#removeComparator(com.champor.persistence.query.Comparator)
	 */
	@Override
	public Query removeComparator(Comparator comparator) {
		this.comparators.remove(comparator);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.champor.persistence.query.Query#getComparators()
	 */
	@Override
	public List<Comparator> getComparators() {
		return this.comparators;
	}
	
	/* (non-Javadoc)
	 * @see com.champor.persistence.query.Query#setLimit(int)
	 */
	@Override
	public Query setLimit(int limit) {
		this.limit = limit;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.champor.persistence.query.Query#getLimit()
	 */
	@Override
	public int getLimit() {
		return this.limit;
	}

	/* (non-Javadoc)
	 * @see com.champor.persistence.query.Query#getSortName()
	 */
	@Override
	public Sort getSort() {
		return this.sort;
	}

	/* (non-Javadoc)
	 * @see com.champor.persistence.query.Query#setSortName(java.lang.String)
	 */
	@Override
	public void setSort(String sortName, SortDirection direction) {
		if(this.sort == null) sort = new Sort();
		sort.setName(sortName);
		sort.setDirection(direction);
	}
}
