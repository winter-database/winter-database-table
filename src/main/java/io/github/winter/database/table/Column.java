package io.github.winter.database.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 字段描述
 *
 * @author changebooks@qq.com
 */
public final class Column implements Serializable {
    /**
     * 字段名
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 类型，{@link java.sql.Types}
     */
    private int type;

    /**
     * 类型名，{@link java.sql.Types}
     */
    private String typeName;

    /**
     * 值类型
     */
    private Class<?> clazz;

    /**
     * 长度
     */
    private int size;

    /**
     * 精度
     */
    private int scale;

    /**
     * 默认字符串
     */
    private String defaultString;

    /**
     * 默认整数
     */
    private Integer defaultInteger;

    /**
     * 默认长整数
     */
    private Long defaultLong;

    /**
     * 默认小数
     */
    private BigDecimal defaultBigDecimal;

    /**
     * 默认日期时间
     */
    private Date defaultDate;

    /**
     * 新增记录，默认当前时间？
     */
    private boolean defaultCurrentDateOnInsert;

    /**
     * 修改记录，默认当前时间？
     */
    private boolean defaultCurrentDateOnUpdate;

    /**
     * 可空？
     */
    private boolean nullable;

    /**
     * 非负？
     */
    private boolean unsigned;

    /**
     * 主键？
     */
    private boolean id;

    /**
     * 自增？
     */
    private boolean autoIncrement;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getDefaultString() {
        return defaultString;
    }

    public void setDefaultString(String defaultString) {
        this.defaultString = defaultString;
    }

    public Integer getDefaultInteger() {
        return defaultInteger;
    }

    public void setDefaultInteger(Integer defaultInteger) {
        this.defaultInteger = defaultInteger;
    }

    public Long getDefaultLong() {
        return defaultLong;
    }

    public void setDefaultLong(Long defaultLong) {
        this.defaultLong = defaultLong;
    }

    public BigDecimal getDefaultBigDecimal() {
        return defaultBigDecimal;
    }

    public void setDefaultBigDecimal(BigDecimal defaultBigDecimal) {
        this.defaultBigDecimal = defaultBigDecimal;
    }

    public Date getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
    }

    public boolean isDefaultCurrentDateOnInsert() {
        return defaultCurrentDateOnInsert;
    }

    public void setDefaultCurrentDateOnInsert(boolean defaultCurrentDateOnInsert) {
        this.defaultCurrentDateOnInsert = defaultCurrentDateOnInsert;
    }

    public boolean isDefaultCurrentDateOnUpdate() {
        return defaultCurrentDateOnUpdate;
    }

    public void setDefaultCurrentDateOnUpdate(boolean defaultCurrentDateOnUpdate) {
        this.defaultCurrentDateOnUpdate = defaultCurrentDateOnUpdate;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isUnsigned() {
        return unsigned;
    }

    public void setUnsigned(boolean unsigned) {
        this.unsigned = unsigned;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

}
