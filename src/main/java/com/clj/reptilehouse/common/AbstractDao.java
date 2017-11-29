package com.clj.reptilehouse.common;

import java.util.List;

public interface AbstractDao<E> {
	
	List<E> list();
	
	List<E> list(String query);
	
	/**
     * 根据主键删除
     * 参数:主键
     * 返回:删除个数
     * @ibatorgenerated 2015-11-28 23:43:17
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入，空属性也会插入
     * 参数:pojo对象
     * 返回:删除个数
     * @ibatorgenerated 2015-11-28 23:43:17
     */
    int insert(E record);

    /**
     * 插入，空属性不会插入
     * 参数:pojo对象
     * 返回:删除个数
     * @ibatorgenerated 2015-11-28 23:43:17
     */
    int insertSelective(E record);

    /**
     * 根据主键查询
     * 参数:查询条件,主键值
     * 返回:对象
     * @ibatorgenerated 2015-11-28 23:43:17
     */
    E selectByPrimaryKey(Long id);

    /**
     * 根据主键修改，空值条件不会修改成null
     * 参数:1.要修改成的值
     * 返回:成功修改个数
     * @ibatorgenerated 2015-11-28 23:43:17
     */
    int updateByPrimaryKeySelective(E record);

    /**
     * 根据主键修改，空值条件会修改成null
     * 参数:1.要修改成的值
     * 返回:成功修改个数
     * @ibatorgenerated 2015-11-28 23:43:17
     */
    int updateByPrimaryKey(E record);
}
