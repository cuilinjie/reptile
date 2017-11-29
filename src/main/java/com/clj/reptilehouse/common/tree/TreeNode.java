package com.clj.reptilehouse.common.tree;

public class TreeNode {

	private String Id;
	private String ParentId;
	private boolean isRoot;
	private boolean isLeaf;
	private boolean isFirstSibling;
	private boolean isLastSibling;

	// 树节点附带的用户对象
	private Object nodeObj1;
	private Object nodeObj2;
	private Object nodeObj3;
	
	public TreeNode(String id, String parentId) {
		super();
		Id = id;
		ParentId = parentId;
	}

	public TreeNode() {
		Id = "";
		ParentId = "";
		isRoot = false;
		isLeaf = false;
		isFirstSibling = false;
		isLastSibling = false;
		nodeObj1 = null;
		nodeObj2 = null;
		nodeObj3 = null;
	}
	
	public boolean isRoot(){
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public boolean isFirstSibling() {
		return isFirstSibling;
	}

	public void setFirstSibling(boolean isFirstSibling) {
		this.isFirstSibling = isFirstSibling;
	}

	public boolean isLastSibling() {
		return isLastSibling;
	}

	public void setLastSibling(boolean isLastSibling) {
		this.isLastSibling = isLastSibling;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getParentId() {
		return ParentId;
	}

	public void setParentId(String parentId) {
		ParentId = parentId;
	}
	public Object getNodeObj1() {
		return nodeObj1;
	}

	public void setNodeObj1(Object nodeObj) {
		this.nodeObj1 = nodeObj;
	}

	public Object getNodeObj2() {
		return nodeObj2;
	}

	public void setNodeObj2(Object nodeObj) {
		this.nodeObj2 = nodeObj;
	}

	public Object getNodeObj3() {
		return nodeObj3;
	}

	public void setNodeObj3(Object nodeObj) {
		this.nodeObj3 = nodeObj;
	}


}
