package com.clj.reptilehouse.common.query;

/**
 * EquivalentComparator
 * @author jiangxs
 * @since 1.0.1(Jul 4, 2015)
 */
public class LikeComparator extends SingleComparator {

	private static final long serialVersionUID = -8168515102185840251L;
	private boolean reverse;

	LikeComparator() {
		super(null, null, null);
	}

	/**
	 * Construct an EquivalentComparator
	 * @param name field name, in fact is bean property name
	 * @param value property value
	 */
	public LikeComparator(String name, Object value) {
		this(name, value, false, null);
	}

	/**
	 * Construct an EquivalentComparator
	 * @param name
	 * @param value property value
	 * @param reverse represent non-equivalent if true
	 */
	public LikeComparator(String name, Object value, boolean reverse) {
		this(name, value, reverse, null);
	}

	public LikeComparator(String name, Object value, LogicalOperator lo) {
		this(name, value, false, lo);
	}

	public LikeComparator(String name, Object value, boolean reverse, LogicalOperator lo) {
		super(name, value, lo);
		this.reverse = reverse;
		if(name == null) throw new IllegalArgumentException("name can not be null");
		if(value == null) throw new IllegalArgumentException("value can not be null");
	}

	@Override
	public String getExpression() {
		if(reverse) return "#{name} NOT LIKE #{value}";
		return "#{name} LIKE #{value}";
	}
}
