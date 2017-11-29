package com.clj.reptilehouse.common.query;

/**
 * GroupComparator
 *
 * @author jiangxs(11/14/15)
 */
public class GroupComparator extends LogicalComparator {

	private static final long serialVersionUID = -5716211991847434044L;

	public static final String START = "(";

    public static final String END = ")";

    private boolean start;

    public boolean isStart() {
        return start;
    }

    public GroupComparator(boolean start, LogicalOperator lo) {
        super("", lo);
        this.start = start;

    }

    @Override
    public String getExpression() {
        return this.start ? START : END;
    }

}
