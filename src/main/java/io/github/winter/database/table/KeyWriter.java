package io.github.winter.database.table;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 索引描述
 *
 * @author changebooks@qq.com
 */
public class KeyWriter {
    /**
     * Build SQL
     *
     * @param table the {@link Table} instance
     * @return [ KEY SQL ]
     */
    public List<String> write(@NotNull Table table) {
        List<String> result = new ArrayList<>();

        String primaryKey = getPrimaryKey(table);
        if (primaryKey != null) {
            result.add(primaryKey);
        }

        List<String> uniqueKeys = getUniqueKeys(table);
        if (uniqueKeys != null) {
            result.addAll(uniqueKeys);
        }

        List<String> keys = getKeys(table);
        if (keys != null) {
            result.addAll(keys);
        }

        return result;
    }

    /**
     * 主键索引
     *
     * @param table the {@link Table} instance
     * @return PRIMARY KEY (column, column, ...)
     */
    protected String getPrimaryKey(@NotNull Table table) {
        List<String> primaryKey = table.getPrimaryKey();
        return buildPrimaryKey(primaryKey);
    }

    /**
     * 唯一索引
     *
     * @param table the {@link Table} instance
     * @return [ UNIQUE KEY (column, column, ...) ]
     */
    protected List<String> getUniqueKeys(@NotNull Table table) {
        Map<String, List<String>> uniqueKeys = table.getUniqueKeys();
        return buildUniqueKeys(uniqueKeys);
    }

    /**
     * 普通索引
     *
     * @param table the {@link Table} instance
     * @return [ KEY (column, column, ...) ]
     */
    protected List<String> getKeys(@NotNull Table table) {
        Map<String, List<String>> keys = table.getKeys();
        return buildKeys(keys);
    }

    /**
     * 主键索引
     *
     * @param columnNames [ Column Name ]
     * @return PRIMARY KEY (column, column, ...)
     */
    protected String buildPrimaryKey(List<String> columnNames) {
        return KeyUtils.build("PRIMARY", "", columnNames);
    }

    /**
     * 唯一索引
     *
     * @param data [ Key Name : [ Column Name ] ]
     * @return [ UNIQUE KEY (column, column, ...) ]
     */
    protected List<String> buildUniqueKeys(Map<String, List<String>> data) {
        return KeyUtils.build("UNIQUE", data);
    }

    /**
     * 普通索引
     *
     * @param data [ Key Name : [ Column Name ] ]
     * @return [ KEY (column, column, ...) ]
     */
    protected List<String> buildKeys(Map<String, List<String>> data) {
        return KeyUtils.build("", data);
    }

}
