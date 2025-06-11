package io.github.winter.database.table;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 表描述
 *
 * @author changebooks@qq.com
 */
public final class Table implements Serializable {
    /**
     * 表名
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 引擎
     */
    private String engine;

    /**
     * 编码
     */
    private String charset;

    /**
     * 排序规则
     */
    private String collate;

    /**
     * 自增
     */
    private Long autoIncrement;

    /**
     * [ 字段描述 ]
     */
    private List<Column> columns;

    /**
     * 主键索引
     * [ 字段名 ]
     */
    private List<String> primaryKey;

    /**
     * 唯一索引
     * [ 索引名 : [ 字段名 ] ]
     */
    private Map<String, List<String>> uniqueKeys;

    /**
     * 普通索引
     * [ 索引名 : [ 字段名 ] ]
     */
    private Map<String, List<String>> keys;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCollate() {
        return collate;
    }

    public void setCollate(String collate) {
        this.collate = collate;
    }

    public Long getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(Long autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<String> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(List<String> primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Map<String, List<String>> getUniqueKeys() {
        return uniqueKeys;
    }

    public void setUniqueKeys(Map<String, List<String>> uniqueKeys) {
        this.uniqueKeys = uniqueKeys;
    }

    public Map<String, List<String>> getKeys() {
        return keys;
    }

    public void setKeys(Map<String, List<String>> keys) {
        this.keys = keys;
    }

}
