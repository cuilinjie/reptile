package com.clj.reptilehouse.common.query;

/**
 * 
 * @author jiangxs
 * @since 1.0.1(Jul 5, 2015)
 */
public class QueryBuilder {

	protected Query query;

	private QueryBuilder() {
		this.query = new StandardQuery();
	}
	
	public static QueryBuilder custom() {
		return new QueryBuilder();
	}

	public QueryBuilder andLike( String name, Object value) {
		this.query.addComparator(new LikeComparator(name, value, false, LogicalOperator.AND));
		return this;
	}

	public QueryBuilder andLike( String name, Object value, boolean reserve) {
		this.query.addComparator(new LikeComparator(name, value, reserve, LogicalOperator.AND));
		return this;
	}

	public QueryBuilder andIn( String name, Object value) {
		this.query.addComparator(new InComparator(name, value, false, LogicalOperator.AND));
		return this;
	}

	public QueryBuilder andIn( String name, Object value, boolean reserve) {
		this.query.addComparator(new InComparator(name, value, reserve, LogicalOperator.AND));
		return this;
	}
	
	public QueryBuilder andEquivalent( String name, Object value) {
		this.query.addComparator(new EquivalentComparator(name, value, false, LogicalOperator.AND));
		return this;
	}
	
	public QueryBuilder andEquivalent( String name, Object value, boolean reserve) {
		this.query.addComparator(new EquivalentComparator(name, value, reserve, LogicalOperator.AND));
		return this;
	}
	
	public QueryBuilder andGreaterThan(String name, Object value) {
		this.query.addComparator(new GreaterThanComparator(name, value, false, LogicalOperator.AND));
		return this;
	}
	
	public QueryBuilder andGreaterThan(String name, Object value, boolean include) {
		this.query.addComparator(new GreaterThanComparator(name, value, include, LogicalOperator.AND));
		return this;
	}
	
	public QueryBuilder andLessThan(String name, Object value) {
		this.query.addComparator(new LessThanComparator(name, value, false, LogicalOperator.AND));
		return this;
	}
	
	public QueryBuilder andLessThan(String name, Object value, boolean include) {
		this.query.addComparator(new LessThanComparator(name, value, include, LogicalOperator.AND));
		return this;
	}
	
	public QueryBuilder andBetween(String name, Object value1, Object value2) {
		this.query.addComparator(new BetweenComparator(name, value1, value2, LogicalOperator.AND));
		return this;
	}


	/////// OR

	public QueryBuilder orLike( String name, Object value) {
		this.query.addComparator(new LikeComparator(name, value, false, LogicalOperator.OR));
		return this;
	}

	public QueryBuilder orLike( String name, Object value, boolean reserve) {
		this.query.addComparator(new LikeComparator(name, value, reserve, LogicalOperator.OR));
		return this;
	}

	public QueryBuilder orIn( String name, Object value) {
		this.query.addComparator(new InComparator(name, value, false, LogicalOperator.OR));
		return this;
	}

	public QueryBuilder orIn( String name, Object value, boolean reserve) {
		this.query.addComparator(new InComparator(name, value, reserve, LogicalOperator.OR));
		return this;
	}

	public QueryBuilder orEquivalent( String name, Object value) {
		this.query.addComparator(new EquivalentComparator(name, value, false, LogicalOperator.OR));
		return this;
	}
	
	public QueryBuilder orEquivalent( String name, Object value, boolean reserve) {
		this.query.addComparator(new EquivalentComparator(name, value, reserve, LogicalOperator.OR));
		return this;
	}
	
	public QueryBuilder orGreaterThan(String name, Object value) {
		this.query.addComparator(new GreaterThanComparator(name, value, false, LogicalOperator.OR));
		return this;
	}
	
	public QueryBuilder orGreaterThan(String name, Object value, boolean include) {
		this.query.addComparator(new GreaterThanComparator(name, value, include, LogicalOperator.OR));
		return this;
	}
	
	public QueryBuilder orLessThan(String name, Object value) {
		this.query.addComparator(new LessThanComparator(name, value, false, LogicalOperator.OR));
		return this;
	}
	
	public QueryBuilder orLessThan(String name, Object value, boolean include) {
		this.query.addComparator(new LessThanComparator(name, value, include, LogicalOperator.OR));
		return this;
	}
	
	public QueryBuilder orBetween(String name, Object value1, Object value2) {
		this.query.addComparator(new BetweenComparator(name, value1, value2, LogicalOperator.OR));
		return this;
	}
	
	public QueryBuilder withSortName(String sortName, SortDirection direction) {
		this.query.setSort(sortName, direction);
		return this;
	}
	
	public QueryBuilder withLimit(int limit) {
		this.query.setLimit(limit);
		return this;
	}


	public QueryBuilder orGroup() {
		this.query.addComparator(new GroupComparator(true, LogicalOperator.OR));
		return this;
	}

	public QueryBuilder andGroup() {
		this.query.addComparator(new GroupComparator(true, LogicalOperator.AND));
		return this;
	}

	public QueryBuilder endGroup() {
		this.query.addComparator(new GroupComparator(false, LogicalOperator.AND));
		return this;
	}
	
	public String build() {
		String where = "";
		if(this.query!=null) {
			MysqlQueryParser parser=new MysqlQueryParser();
			StringBuffer result = parser.parse(this.query);
			if(result != null) where = result.toString();
		}else{
			 where = " 1=1 LIMIT 20";
		}
		return where;
	}
}
