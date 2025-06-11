package io.github.winter.database.table;

import java.io.Serializable;
import java.util.List;

/**
 * 索引描述
 *
 * @author changebooks@qq.com
 */
public final class Key implements Serializable {
    /**
     * 索引名
     */
    private String name;

    /**
     * [ 字段名 ]
     */
    private List<String> columnNames;

    /**
     * 主键？
     */
    private boolean primary;

    /**
     * 唯一？
     */
    private boolean unique;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

}
