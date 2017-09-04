package com.taotao.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.BasePojo;

public abstract class BaseService<T extends BasePojo> {

    @Autowired
    private Mapper<T> mapper;

    // public abstract Mapper<T> getMapper();

    /**
     * 根据主键id查询数据
     * 
     * @param id
     * @return
     */
    public T queryById(Long id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有的数据
     * 
     * @return
     */
    public List<T> queryAll() {
        return this.mapper.select(null);
    }

    /**
     * 根据条件查询一条数据
     * 
     * @param record
     * @return
     */
    public T queryOne(T record) {
        return this.mapper.selectOne(record);
    }

    /**
     * 根据条件查询列表数据
     * 
     * @param record
     * @return
     */
    public List<T> queryListByWhere(T record) {
        return this.mapper.select(record);
    }

    /**
     * 根据条件分页查询数据
     * 
     * @param record
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<T> queryPageListByWhere(T record, Integer page, Integer rows) {
        // 设置分页参数
        PageHelper.startPage(page, rows);
        List<T> list = this.queryListByWhere(record);
        return new PageInfo<T>(list);
    }

    /**
     * 保存数据（所有的属性作为插入的字段）
     * 
     * @param record
     * @return
     */
    public Integer save(T record) {
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return this.mapper.insert(record);
    }

    /**
     * 保存数据（选择不为null的属性作为插入的字段）
     * 
     * @param record
     * @return
     */
    public Integer saveSelective(T record) {
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return this.mapper.insertSelective(record);
    }

    /**
     * 更新数据（所有的属性作为更新的字段）
     * 
     * @param record
     * @return
     */
    public Integer update(T record) {
        record.setUpdated(new Date());
        return this.mapper.updateByPrimaryKey(record);
    }

    /**
     * 更新数据（选择不为null的属性作为更新的字段）
     * 
     * @param record
     * @return
     */
    public Integer updateSelective(T record) {
        record.setUpdated(new Date());
        record.setCreated(null); // 强制设置创建时间为null，创建时间永远不会被更新
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键id删除数据
     * 
     * @param id
     * @return
     */
    public Integer deleteById(Long id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据条件删除数据
     * 
     * @param record
     * @return
     */
    public Integer deleteByWhere(T record) {
        return this.mapper.delete(record);
    }

    /**
     * 批量删除数据
     * 
     * @param clazz
     * @param property
     * @param ids
     * @return
     */
    public Integer deleteByIds(Class<T> clazz, String property, List<Object> ids) {
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, ids);
        return this.mapper.deleteByExample(example);
    }
    
    /**
     * 排序查询数据列表
     * 
     * @param page
     * @param rows
     * @param orderBy
     * @param clazz
     * @return
     */
    public PageInfo<T> queryPageListOrderBy(Integer page, Integer rows, String orderBy, Class<T> clazz) {
        PageHelper.startPage(page, rows);
        Example example = new Example(clazz);
        example.setOrderByClause(orderBy);
        List<T> list = this.mapper.selectByExample(example);
        return new PageInfo<T>(list);
    }
}
