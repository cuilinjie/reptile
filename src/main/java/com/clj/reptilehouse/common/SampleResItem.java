package com.clj.reptilehouse.common;

import com.clj.reptilehouse.common.SampleResItem;
import com.clj.reptilehouse.common.tree.TreeUtils;


public class SampleResItem
{
	private String name;
	private String value;
	private String treeCode;
	private Integer type;

	public SampleResItem()
	{
		this.name = "";
		this.value = "";
		this.treeCode = "";
	}

	public SampleResItem(String name, String value)
	{
		this.name = name;
		this.value = value;
		this.treeCode = "";
	}

	public SampleResItem(String name, Integer value)
	{
		this.name = name;
		if(value != null){
			this.value = value.toString();
		}
		else{
			this.value = "";
		}
		this.treeCode = "";
	}
	
	public SampleResItem(String name, Long value)
	{
		this.name = name;
		if(value != null){
			this.value = value.toString();
		}
		else{
			this.value = "";
		}
		this.treeCode = "";
	}

	public SampleResItem(String name, String value, String treeCode)
	{
		this.name = name;
		this.value = value;
		this.treeCode = treeCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName()
	{
		if(treeCode != null && treeCode.length() > TreeUtils.TREE_DEFAULT_LEVEL_LENGTH)
		{
			int nLevel = treeCode.length() / TreeUtils.TREE_DEFAULT_LEVEL_LENGTH - 1;
			if(nLevel > 0)
			{
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i < nLevel; i++)
				{
					sb.append("　");
				}
				return sb.toString() + name;
			}
		}

		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getTreeCode()
	{
		return treeCode;
	}

	public void setTreeCode(String treeCode)
	{
		this.treeCode = treeCode;
	}

	public boolean equals(Object resObj)
	{
		SampleResItem res = (SampleResItem)resObj;
		boolean isSameName = this.getName().equals(res.getName());
		boolean isSameValue = this.getValue().equals(res.getValue());
		boolean isSameTreeCode = this.getTreeCode().equals(res.getTreeCode());
		if(isSameName && isSameValue && isSameTreeCode)
		{
			return true;
		}
		return false;
	}

	public String toString()
	{
		return this.value;
	}

}