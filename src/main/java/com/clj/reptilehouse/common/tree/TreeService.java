package com.clj.reptilehouse.common.tree;


import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springside.modules.utils.Reflections;

import com.clj.reptilehouse.common.query.QueryBuilder;
import com.clj.reptilehouse.common.query.SortDirection;
import com.clj.reptilehouse.common.tree.TreeUtils;


public class TreeService<T> extends SqlSessionDaoSupport{
	
	@Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){  
        super.setSqlSessionFactory(sqlSessionFactory);  
    }  
	
	private final String mapper="com.eidlink.eidboss.system.dao.";
	private final String suffix="Dao";
	private final String TREE_DEFAULT_SQL_NAME = "tree_code"; // 树编码默认字段名称
	private final String TREE_PARENTID_SQL_NAME = "parent_id"; // 父ID默认字段名称
	private final String TREE_GROUPID_SQL_NAME = "grp_id"; // 父ID默认字段名称
	
	@SuppressWarnings("unchecked")
	public String getMapper(String id) {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class<T> ts = (Class<T>) pt.getActualTypeArguments()[0];
		return mapper+ts.getSimpleName()+suffix+"."+id;
	}
	
	// 树对象专用，根据树编码获取该对象
	public T getObjByCode(final String treeCode){
		return null;//findUniqueBy(TREE_DEFAULT_SQL_NAME, treeCode);
	}
	
	//树对象专用，根据树编码获取该对象
	public T get(final Long id){
		return this.getSqlSession().selectOne(getMapper("selectByPrimaryKey"), id);
	}
		
	// 树对象专用，获取树对象树编码值
	public String getTreeCode(final T obj){
		return Reflections.getFieldValue(obj,TreeUtils.TREE_DEFAULT_COL_NAME).toString();
	}
	
	// tree对象专用,根据树编码获取该节点及所有子节点
	public List<T> findSubTree(final String treeCode){
		String query =QueryBuilder.custom()
				.andLike(TREE_DEFAULT_SQL_NAME, treeCode+"%")
				.withSortName(TREE_DEFAULT_SQL_NAME, SortDirection.ASC)
				.build();
		return this.getSqlSession().selectList(getMapper("list"), query);
	}
	
	// tree对象专用,根据树编码获取该节点及所有子节点
	public List<T> findSubTree(final Long grpId,final String treeCode){
		String query =QueryBuilder.custom()
				.andEquivalent(TREE_GROUPID_SQL_NAME, grpId)
				.andLike(TREE_DEFAULT_SQL_NAME, treeCode+"%")
				.withSortName(TREE_DEFAULT_SQL_NAME, SortDirection.ASC)
				.build();
		return this.getSqlSession().selectList(getMapper("list"), query);
	}
	
	// tree对象专用,根据树编码获取该节点下的所有树节点
	public List<T> findSubTrees(final String treeCode){
		String query =QueryBuilder.custom()
				.andLike(TREE_DEFAULT_SQL_NAME, treeCode,true)
				.andLike(TREE_DEFAULT_SQL_NAME, treeCode+"%")
				.withSortName(TREE_DEFAULT_SQL_NAME, SortDirection.ASC)
				.build();
		return this.getSqlSession().selectList(getMapper("list"), query);
	}
	
	// tree对象专用,获取指定父节点id的所有节点
	public List<T> findChildById(final Long parentId){
		String query ="";
		if(parentId==null||parentId<0){
			QueryBuilder qb =QueryBuilder.custom();
				qb.orLessThan(TREE_PARENTID_SQL_NAME, 0);
				qb.withSortName(TREE_DEFAULT_SQL_NAME, SortDirection.ASC);
			query=qb.build();
		}else{
		 query =QueryBuilder.custom()
				.andEquivalent(TREE_PARENTID_SQL_NAME, parentId)
				.withSortName(TREE_DEFAULT_SQL_NAME, SortDirection.ASC)
				.build();
		}
		return this.getSqlSession().selectList(getMapper("list"), query);
	}
	
	// tree对象专用,获取指定树编码下的所有直接子节点
	public List<T> findChildByCode(final String parentCode, final int treeLevelLength){
		String codeLike = parentCode;
		for( int i=0; i<treeLevelLength; i++ ){
			codeLike += "_";
		}
		String query =QueryBuilder.custom()
				.andLike(TREE_PARENTID_SQL_NAME, codeLike)
				.withSortName(TREE_DEFAULT_SQL_NAME, SortDirection.ASC)
				.build();
		return this.getSqlSession().selectList(getMapper("list"), query);
	}
	
	// tree对象专用,获取指定树编码树节点以外的所有节点
	public List<T> findOtherTrees(final String treeCode){
		String query =QueryBuilder.custom()
				.andLike(TREE_DEFAULT_SQL_NAME, treeCode+"%",true)
				.withSortName(TREE_DEFAULT_SQL_NAME, SortDirection.ASC)
				.build();
		//System.out.println(query);
		return this.getSqlSession().selectList(getMapper("list"), query);
	}
	
	// tree对象专用,获取指定树编码树节点以外的所有节点
		public List<T> findOtherTrees(final Long grpId,final String treeCode){
			String query =QueryBuilder.custom()
					.andEquivalent(TREE_GROUPID_SQL_NAME, grpId)
					.andLike(TREE_DEFAULT_SQL_NAME, treeCode+"%",true)
					.withSortName(TREE_DEFAULT_SQL_NAME, SortDirection.ASC)
					.build();
			//System.out.println(query);
			return this.getSqlSession().selectList(getMapper("list"), query);
		}
	
	// tree对象专用,获取指定节点的树编码
	public String getTreeCodeById(final Long id){
		if(id==null||id<0){
			return null;
		}
		T t= this.getSqlSession().selectOne(getMapper("selectByPrimaryKey"), id);
		return getTreeCode(t);
	}
	
	// tree对象专用,获取指定父节点的所有节点的最大树编码
	public String getMaxCodeById(final Long parentId){
		String query ="";
		if(parentId==null||parentId<0){
			QueryBuilder qb =QueryBuilder.custom();
			qb.orLessThan(TREE_PARENTID_SQL_NAME, 0);
			query= qb.build();
		}else{
			query =QueryBuilder.custom()
					.andEquivalent(TREE_PARENTID_SQL_NAME, parentId)
					.build();
		}
		return this.getSqlSession().selectOne(getMapper("selectMaxCode"), query);
	}

	// tree对象专用,获取指定父节点的所有节点的最大树编码
	public String getMaxCodeByCode(final String parentCode, final int treeLevelLength){
		String codeLike = parentCode;
		for( int i=0; i<treeLevelLength; i++ ){
			codeLike += "_";
		}
		String query =QueryBuilder.custom()
				.andLike(TREE_PARENTID_SQL_NAME, codeLike)
				.build();
		return this.getSqlSession().selectOne(getMapper("selectMaxCode"), query);
	}
	
	// tree对象专用,获取指定编码节点以后的所有兄弟树
	public List<T> findNextTrees(final String treeCode, final int treeLevelLength){
		String nextCode = TreeUtils.getNextTreeCode(treeCode, treeLevelLength);
		String parentCode = treeCode.substring(0, treeCode.length() - treeLevelLength);
		String query =QueryBuilder.custom()
				.andGreaterThan(TREE_DEFAULT_SQL_NAME, nextCode,true)
				.andLike(TREE_DEFAULT_SQL_NAME, parentCode+"%")
				.withSortName(TREE_DEFAULT_SQL_NAME, SortDirection.ASC)
				.build();
		//System.out.println(query);
		return this.getSqlSession().selectList(getMapper("list"), query);
	}
	
	// tree对象专用,获取指定编码节点以后的所有兄弟树
	public List<T> findNextTrees(final Long grpId,final String treeCode, final int treeLevelLength){
		String nextCode = TreeUtils.getNextTreeCode(treeCode, treeLevelLength);
		String parentCode = treeCode.substring(0, treeCode.length() - treeLevelLength);
		String query =QueryBuilder.custom()
				.andEquivalent(TREE_GROUPID_SQL_NAME,grpId)
				.andGreaterThan(TREE_DEFAULT_SQL_NAME, nextCode,true)
				.andLike(TREE_DEFAULT_SQL_NAME, parentCode+"%")
				.withSortName(TREE_DEFAULT_SQL_NAME, SortDirection.ASC)
				.build();
		return this.getSqlSession().selectList(getMapper("list"), query);
	}
	
	// tree对象专用,获取指定编码节点树及以后的所有兄弟树
	public List<T> findMeNextTrees(final String treeCode, final int treeLevelLength){
		String parentCode = treeCode.substring(0, treeCode.length() - treeLevelLength);
		String query =QueryBuilder.custom()
				.andGreaterThan(TREE_DEFAULT_SQL_NAME, treeCode,true)
				.andLike(TREE_DEFAULT_SQL_NAME, parentCode+"%")
				.withSortName(TREE_DEFAULT_SQL_NAME, SortDirection.ASC)
				.build();
		return this.getSqlSession().selectList(getMapper("list"), query);
	}
	
	// tree对象专用,删除指定编码的树（包括所有子节点）
	public void deleteTree(final String treeCode){
		String query =QueryBuilder.custom()
				.andLike(TREE_DEFAULT_SQL_NAME, treeCode+"")
				.build();
		this.getSqlSession().delete(getMapper("deleteBy"), query);
	}
	
	// tree对象专用,删除指定编码的树（包括所有子节点）
		public void deleteTree(final Long grpId,final String treeCode){
			String query =QueryBuilder.custom()
					.andEquivalent(TREE_GROUPID_SQL_NAME,grpId)
					.andLike(TREE_PARENTID_SQL_NAME, treeCode+"")
					.build();
			this.getSqlSession().delete(getMapper("deleteBy"), query);
		}
	
	// tree对象专用,计算指定id节点的新增加子节点的树编码
	public String getNewChildCode(final Long id, final int treeLevelLength){
		String originalCode = TreeUtils.GetLevelKey(TreeUtils.TREE_KEY_ORIGINAL_NUMBER, treeLevelLength);
		String maxTreeCode = getMaxCodeById(id);
		if (id==null||id<0) {
			if (StringUtils.isBlank(maxTreeCode)) {
				maxTreeCode = originalCode;
			}
		} 
		else {
			String parentCode = getTreeCodeById(id);
			if (StringUtils.isBlank(maxTreeCode)) {
				maxTreeCode = parentCode + originalCode;
			}
		}
		return TreeUtils.getNextTreeCode(maxTreeCode, treeLevelLength);
	}
	
	// tree对象专用,移动树节点
	public boolean moveNode(final Long moveId, final Long targetId, int moveType, int treeLevelLength){
		T moveNode = get(moveId);
		if( moveNode == null ){
			return false;
		}
		T targetNode = get(targetId);
		if( targetNode == null ){
			return false;
		}
		
		String moveNodeCode = getTreeCode(moveNode);
		// 获取所有要移动的节点
		List<T> listMove = findSubTree(moveNodeCode);
		// 获取所有假删除影响的节点
		List<T> listChange1 = findNextTrees(moveNodeCode, treeLevelLength);

		// 把要移动的节点的树编码清空(假删除)
		String query =QueryBuilder.custom()
				.andLike(TREE_DEFAULT_SQL_NAME, moveNodeCode+"%")
				.build();
		this.getSqlSession().update(getMapper("updateCodeBy"), query);
		// 更改假删除后影响的节点
		String treeCode;
		String adjustCode;
		for (T obj : listChange1)
		{
			treeCode = getTreeCode(obj);
			adjustCode = TreeUtils.getLevelLastTreeCode(treeCode, moveNodeCode.length());
			Reflections.setFieldValue(obj, TreeUtils.TREE_DEFAULT_COL_NAME, adjustCode);
			this.getSqlSession().update(getMapper("updateByPrimaryKeySelective"), obj);
		}

		Long parentId = null;
		String moveCode = null;
		int levelLength = 0;
		List<T> listChange2 = null;
		switch (moveType)
		{
			case TreeUtils.MOVE_TYPE_FIRST_SUB: // 移动到目标节点的第一子节点
				parentId = targetId;
				treeCode = getTreeCode(targetNode);
				levelLength = treeCode.length() + treeLevelLength;
				listChange2 = findSubTrees(treeCode);
				moveCode = treeCode + TreeUtils.GetLevelKey(TreeUtils.TREE_KEY_ORIGINAL_NUMBER + 1);
				break;
			case TreeUtils.MOVE_TYPE_LAST_SUB: // 移动到目标节点的最后子节点
				parentId = targetId;
				treeCode = getTreeCode(targetNode);
				levelLength = treeCode.length() + treeLevelLength;
				moveCode = getNewChildCode(targetId, treeLevelLength);
				break;
			case TreeUtils.MOVE_TYPE_UP_OBJ: // 移动到目标节点的上方
				parentId = (Long)Reflections.getFieldValue(targetNode, TreeUtils.TREE_PARENTID_COL_NAME);
				treeCode = getTreeCode(targetNode);
				levelLength = treeCode.length();
				listChange2 = findMeNextTrees(treeCode, treeLevelLength);
				moveCode = treeCode;
				break;
			case TreeUtils.MOVE_TYPE_DOWN_OBJ: // 移动到目标节点的下方
				parentId = (Long)Reflections.getFieldValue(targetNode, TreeUtils.TREE_PARENTID_COL_NAME);
				treeCode = getTreeCode(targetNode);
				levelLength = treeCode.length();
				listChange2 = findNextTrees(treeCode, treeLevelLength);
				moveCode = TreeUtils.getNextTreeCode(treeCode);
				break;
		}

		// 受影响的节点向下移
		if (listChange2 != null && listChange2.size() > 0)
		{
			for (T obj : listChange2)
			{
				treeCode = getTreeCode(obj);
				adjustCode = TreeUtils.getLevelNextTreeCode(treeCode, levelLength);
				Reflections.setFieldValue(obj, TreeUtils.TREE_DEFAULT_COL_NAME, adjustCode);
				this.getSqlSession().update(getMapper("updateByPrimaryKeySelective"), obj);
			}
		}	

		// 最后插入要移动的节点
		Reflections.setFieldValue(listMove.get(0), TreeUtils.TREE_PARENTID_COL_NAME, parentId);
		this.getSqlSession().update(getMapper("updateByPrimaryKeySelective"), listMove.get(0));
		
		for (T obj : listMove)
		{
			treeCode = getTreeCode(obj);
			adjustCode = TreeUtils.getMoveTreeCode(treeCode, moveCode, moveNodeCode.length());
			Reflections.setFieldValue(obj, TreeUtils.TREE_DEFAULT_COL_NAME, adjustCode);
			this.getSqlSession().update(getMapper("updateByPrimaryKeySelective"), obj);
		}
		return true;
	}
	
	// 检查树列表是否完整,不完整则补齐以保持树列表
	public void keepTreeList(List<T> list, int minLevel, int treeLevelLength)
	{
		if (list == null){
			return;
		}

		String lastLevelCode[] = { "?", "?", "?", "?", "?", "?", "?", "?", "?", "?", "?", "?", "?" };
		for (int i = 0; i < list.size(); i++)
		{
			T item = list.get(i);
			String nowCode = getTreeCode(item);
			int nowLevel = nowCode.length() / treeLevelLength;
			lastLevelCode[nowLevel] = nowCode;
			if (nowLevel > minLevel){
				int addCount = 0;
				for (int j = nowLevel - 1; j >= minLevel; j--){
					if (getTreeCode(item).startsWith(lastLevelCode[j])){
						break;
					}
					else{
						String levelCode = nowCode.substring(0, j * treeLevelLength);
						T addItem = getObjByCode(levelCode);
						if (addItem != null){
							addCount++;
							list.add(i, addItem);
							lastLevelCode[j] = levelCode;
						}
					}
				}
				i += addCount;
			}
		}
	}
	
	// tree对象专用,获取指定父节点的所有节点的最大树编码
	public String getMaxCodeById(final Long grpId,final Long parentId){
		String query ="";
		if(parentId==null||parentId<0){
			QueryBuilder qb =QueryBuilder.custom();
			qb.andEquivalent(TREE_GROUPID_SQL_NAME,grpId);
			qb.orLessThan(TREE_PARENTID_SQL_NAME, 0,true);
			query= qb.build();
		}else{
			query =QueryBuilder.custom()
					.andEquivalent(TREE_PARENTID_SQL_NAME, parentId)
					.build();
		}
		return this.getSqlSession().selectOne(getMapper("selectMaxCode"), query);
	}
		
	// tree对象专用,计算指定id节点的新增加子节点的树编码
	public String getNewChildCode(final Long grpId, final Long pid, final int treeLevelLength){
		String originalCode = TreeUtils.GetLevelKey(TreeUtils.TREE_KEY_ORIGINAL_NUMBER, treeLevelLength);
		String maxTreeCode = getMaxCodeById(grpId, pid);
		if (pid!=null&&pid>0) {
			if (StringUtils.isBlank(maxTreeCode)) {
				maxTreeCode = originalCode;
			}
		}else {
			String parentCode = getTreeCodeById(pid);
			if (StringUtils.isBlank(maxTreeCode)) {
				maxTreeCode = parentCode + originalCode;
			}
		}
		return TreeUtils.getNextTreeCode(maxTreeCode, treeLevelLength);
	}
	
	
}