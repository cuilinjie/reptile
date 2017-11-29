package com.clj.reptilehouse.common.tree;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springside.modules.utils.Reflections;

import com.clj.reptilehouse.common.tree.ITreeNodeObj;
import com.clj.reptilehouse.common.tree.TreeNode;



public class TreeUtils
{
	public final static String TREE_ROOT_PARENT_ID = ""; // 树的根节点父ID
	public final static String TREE_ROOT_ID = "0"; // 树的根节点ID
	public final static String TREE_ERROR_KEY = "99999"; // 树级别错误编码

	public final static String TREE_DEFAULT_COL_NAME = "treeCode"; // 树编码默认字段名称
	public final static String TREE_PARENTID_COL_NAME = "parentId"; // 父ID默认字段名称
	
	public final static int TREE_DEFAULT_LEVEL_LENGTH = 3; // 树编码每级默认长度
	public final static int TREE_KEY_ORIGINAL_NUMBER = 0; // 树编码每级原始数字

	public final static int MOVE_TYPE_FIRST_SUB = 0; // 移动到目标节点的第一子节点
	public final static int MOVE_TYPE_LAST_SUB = 1; // 移动到目标节点的最后子节点
	public final static int MOVE_TYPE_UP_OBJ = 2; // 移动到目标节点的上方
	public final static int MOVE_TYPE_DOWN_OBJ = 3; // 移动到目标节点的下方


	public static String GetLevelKey(int keyNumber)
	{
		return GetLevelKey(keyNumber, TREE_DEFAULT_LEVEL_LENGTH);
	}

	public static String GetLevelKey(int keyNumber, int LevelLength)
	{
		if (keyNumber < 0)
		{
			keyNumber = 0;
		}
		String key = String.valueOf(keyNumber);

		if (key.length() > LevelLength)
		{
			return TREE_ERROR_KEY;
		}

		while (key.length() < LevelLength)
		{
			key = "0" + key;
		}
		return key;
	}

	// 把树接口对象列表转换为树节点列表
	@SuppressWarnings("rawtypes")
	public static List<TreeNode> getNodeList(List listObj)
	{
		if (listObj == null || listObj.size() == 0)
		{
			return null;
		}
		return getNodeList(listObj, TREE_DEFAULT_LEVEL_LENGTH);
	}

	// 把树接口对象列表转换为树节点列表
	@SuppressWarnings("rawtypes")
	public static List<TreeNode> getNodeList(List listObj, int LevelLength)
	{
		if (listObj == null || listObj.size() == 0)
		{
			return null;
		}
		int waitNodeNum[] =
		{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
		List<TreeNode> list = new ArrayList<TreeNode>();
		int rootCodeLength = LevelLength;
		int currCodeLength = LevelLength;
		int lastCodeLength = LevelLength;
		int nodeSize = listObj.size();
		if (nodeSize > 0)
		{
			ITreeNodeObj item = (ITreeNodeObj) listObj.get(0);
			currCodeLength = item.getTreeCode().length();
			rootCodeLength = currCodeLength;
			TreeNode node = new TreeNode();
			node.setId(item.getId());
			node.setRoot(true);
			node.setParentId(TREE_ROOT_ID);
			node.setFirstSibling(true);
			node.setNodeObj1(item);
			list.add(node);
		}
		for (int i = 1; i < nodeSize; i++)
		{
			lastCodeLength = currCodeLength;
			ITreeNodeObj item = (ITreeNodeObj) listObj.get(i);
			currCodeLength = item.getTreeCode().length();
			TreeNode node = new TreeNode();
			node.setId(item.getId());
			node.setRoot(currCodeLength == rootCodeLength);
			node.setParentId(currCodeLength == rootCodeLength ? TREE_ROOT_ID : item.getParentId());
			if (currCodeLength > lastCodeLength)
			{
				node.setFirstSibling(true);
				TreeNode nodeLast = (TreeNode) list.get(i - 1);
				nodeLast.setLeaf(false);
				waitNodeNum[lastCodeLength / LevelLength] = i - 1;
			} else if (currCodeLength == lastCodeLength)
			{
				node.setFirstSibling(false);
				TreeNode nodeLast = (TreeNode) list.get(i - 1);
				nodeLast.setLeaf(true);
				nodeLast.setLastSibling(false);
			} else
			{
				node.setFirstSibling(false);
				TreeNode nodeLast = (TreeNode) list.get(i - 1);
				nodeLast.setLeaf(true);
				nodeLast.setLastSibling(true);
				int currLevel = currCodeLength / LevelLength;
				int lastLevel = lastCodeLength / LevelLength;
				for (int j = currLevel; j < lastLevel; j++)
				{
					if (waitNodeNum[j] > 0)
					{
						TreeNode waitNode = (TreeNode) list.get(waitNodeNum[j]);
						waitNode.setLastSibling(true);
						waitNodeNum[j] = -1;
					}
				}
			}
			if (i == nodeSize - 1)
			{
				node.setLeaf(true);
				node.setLastSibling(true);
			}
			node.setNodeObj1(item);
			list.add(node);
		}
		return list;
	}

	// 计算同一级别的下一个树编码
	public static String getNextTreeCode(String treeCode)
	{
		return getNextTreeCode(treeCode, TREE_DEFAULT_LEVEL_LENGTH);
	}

	// 计算同一级别的下一个树编码
	public static String getNextTreeCode(String treeCode, int LevelLength)
	{
		String returnCode = "";
		if (treeCode.length() >= LevelLength)
		{
			String firstCode = treeCode.substring(0, treeCode.length() - LevelLength);
			String lastCode = treeCode.substring(treeCode.length() - LevelLength);
			int iCode = Integer.parseInt(lastCode);
			iCode++;
			returnCode = firstCode + GetLevelKey(iCode, LevelLength);
		}

		return returnCode;
	}

	// 计算同一级别的上一个树编码
	public static String getLastTreeCode(String treeCode)
	{
		return getLastTreeCode(treeCode, TREE_DEFAULT_LEVEL_LENGTH);
	}

	// 计算同一级别的上一个树编码
	public static String getLastTreeCode(String treeCode, int LevelLength)
	{
		String returnCode = "";
		if (treeCode.length() >= LevelLength)
		{
			String firstCode = treeCode.substring(0, treeCode.length() - LevelLength);
			String lastCode = treeCode.substring(treeCode.length() - LevelLength);
			int iCode = Integer.parseInt(lastCode);
			iCode--;
			returnCode = firstCode + GetLevelKey(iCode, LevelLength);
		}

		return returnCode;
	}

	// 计算指定级别的下一个树编码
	public static String getLevelNextTreeCode(String treeCode, int levelLenth)
	{
		return getLevelNextTreeCode(treeCode, levelLenth, TREE_DEFAULT_LEVEL_LENGTH);
	}

	// 计算指定级别的下一个树编码
	public static String getLevelNextTreeCode(String treeCode, int levelLenth, int LevelLength)
	{
		String returnCode = "";
		if (treeCode.length() >= levelLenth)
		{
			String firstCode = treeCode.substring(0, levelLenth - LevelLength);
			String changeCode = treeCode.substring(levelLenth - LevelLength, levelLenth);
			String lastCode = treeCode.substring(levelLenth);
			int iCode = Integer.parseInt(changeCode);
			iCode++;
			returnCode = firstCode + GetLevelKey(iCode, LevelLength) + lastCode;
		}

		return returnCode;
	}

	// 计算指定级别的上一个树编码
	public static String getLevelLastTreeCode(String treeCode, int levelLenth)
	{
		return getLevelLastTreeCode(treeCode, levelLenth, TREE_DEFAULT_LEVEL_LENGTH);
	}

	// 计算指定级别的上一个树编码
	public static String getLevelLastTreeCode(String treeCode, int levelLenth, int LevelLength)
	{
		String returnCode = "";
		if (treeCode.length() >= levelLenth)
		{
			String firstCode = treeCode.substring(0, levelLenth - LevelLength);
			String changeCode = treeCode.substring(levelLenth - LevelLength, levelLenth);
			String lastCode = treeCode.substring(levelLenth);
			int iCode = Integer.parseInt(changeCode);
			iCode--;
			returnCode = firstCode + GetLevelKey(iCode, LevelLength) + lastCode;
		}

		return returnCode;
	}

	// 计算移动树编码
	public static String getMoveTreeCode(String treeCode, String titleCode, int moveLenth)
	{
		String lastCode = treeCode.substring(moveLenth);
		String returnCode = titleCode + lastCode;

		return returnCode;
	}
	
	@SuppressWarnings("rawtypes")
	public static String getHtmlTreeStr(final List list, String textAtt, String urlAtt, String iconAtt) {
		return getHtmlTreeStr(list, TREE_DEFAULT_COL_NAME, TREE_DEFAULT_LEVEL_LENGTH, textAtt, urlAtt, iconAtt);
	}

	@SuppressWarnings("rawtypes")
	public static String getHtmlTreeStr(final List list, String treeAtt, int levelLength, String textAtt, String urlAtt, String iconAtt) {
		if( list==null ){
			return "";
		}
		
		String lastTreeCode = "";
		String curTreeCode = "";
		int nChild = 0;
		int nodeNum = list.size();
		StringBuilder sb = new StringBuilder();
		sb.append("<ul class=\"sf-menu\">");
		for (int i = 0; i < nodeNum; i++)
		{
			curTreeCode = Reflections.getFieldValue(list.get(i), treeAtt).toString();
			if( i>0 ){
				if( curTreeCode.length()>lastTreeCode.length()){
					sb.append("<ul>");
					nChild++;
				}
				else if( curTreeCode.length()<lastTreeCode.length() ){
					int nLevel = (lastTreeCode.length()-curTreeCode.length())/levelLength;
					for( int k=0; k<nLevel; k++ ){
						sb.append("</li> </ul>");
						nChild--;
					}
					sb.append("</li>");
				}
				else{
					sb.append("</li>");
				}
			}
			
			String textValue = Reflections.getFieldValue(list.get(i), textAtt).toString();
			Object urlObj = Reflections.getFieldValue(list.get(i), urlAtt);
			String urlValue = urlObj==null ? "javascript:void(0)":urlObj.toString();
			Object iconObj = Reflections.getFieldValue(list.get(i), iconAtt);
			String iconValue = iconObj==null ? "":iconObj.toString();
			if( StringUtils.isBlank(urlValue) ){
				urlValue = "javascript:void(0)";
			}
			sb.append("<li> <a href=\"").append(urlValue).append("\">");
			sb.append("<span class=\"menu-icon pos1 ").append(iconValue).append("\"></span>");
			sb.append(textValue).append("</a>");

			lastTreeCode = curTreeCode;			
		}
		while (nChild > 0) {
			sb.append("</li> </ul>");
			nChild--;
		}
		if( nodeNum>0 ){
			sb.append("</li>");			
		}	
		sb.append("</ul>");
		
		return sb.toString();
	}
}
