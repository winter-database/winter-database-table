package io.github.winter.database.table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 表描述
 *
 * @author changebooks@qq.com
 */
public class TableReader {
    /**
     * the {@link KeyReader} instance
     */
    private static final KeyReader KEY_READER = new KeyReader();

    /**
     * the {@link ColumnReader} instance
     */
    private final ColumnReader columnReader;

    public TableReader() {
        this.columnReader = new ColumnReader();
    }

    public TableReader(ColumnReader columnReader) {
        Objects.requireNonNull(columnReader, "columnReader must not be null");

        this.columnReader = columnReader;
    }

    /**
     * Read Table
     *
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @return the {@link Table} instance
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    public Table read(@NotNull Connection conn, @NotBlank String tableName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        String catalog = conn.getCatalog();

        ResultSet rs = metaData.getTables(catalog, null, tableName, new String[]{"TABLE"});
        if (rs == null) {
            return null;
        }

        try (rs) {
            if (rs.next()) {
                Table result = new Table();

                setName(result, rs, metaData, catalog, conn);
                setRemark(result, rs, metaData, catalog, conn);
                setEngine(result, rs, metaData, catalog, conn);
                setCharset(result, rs, metaData, catalog, conn);
                setCollate(result, rs, metaData, catalog, conn);
                setAutoIncrement(result, rs, metaData, catalog, conn);
                setColumns(result, metaData, catalog, conn);
                setKeys(result, conn);

                afterPropertiesSet(result, rs, metaData, catalog, conn);
                return result;
            } else {
                return null;
            }
        }
    }

    /**
     * 表名
     *
     * @param record   the {@link Table} instance
     * @param rs       the {@link ResultSet} instance
     * @param metaData the {@link DatabaseMetaData} instance
     * @param catalog  Database Name
     * @param conn     the {@link Connection} instance
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setName(@NotNull Table record,
                           @NotNull ResultSet rs,
                           @NotNull DatabaseMetaData metaData, @NotNull String catalog,
                           @NotNull Connection conn) throws SQLException {
        String tableName = rs.getString("TABLE_NAME");
        if (tableName == null) {
            return;
        }

        String name = tableName.trim();
        record.setName(name);
    }

    /**
     * 备注
     *
     * @param record   the {@link Table} instance
     * @param rs       the {@link ResultSet} instance
     * @param metaData the {@link DatabaseMetaData} instance
     * @param catalog  Database Name
     * @param conn     the {@link Connection} instance
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setRemark(@NotNull Table record,
                             @NotNull ResultSet rs,
                             @NotNull DatabaseMetaData metaData, @NotNull String catalog,
                             @NotNull Connection conn) throws SQLException {
        String remark = rs.getString("REMARKS");
        record.setRemark(remark);
    }

    /**
     * 引擎
     *
     * @param record   the {@link Table} instance
     * @param rs       the {@link ResultSet} instance
     * @param metaData the {@link DatabaseMetaData} instance
     * @param catalog  Database Name
     * @param conn     the {@link Connection} instance
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setEngine(@NotNull Table record,
                             @NotNull ResultSet rs,
                             @NotNull DatabaseMetaData metaData, @NotNull String catalog,
                             @NotNull Connection conn) throws SQLException {
        String tableName = record.getName();
        if (tableName == null) {
            return;
        }

        String engine = TableUtils.readEngine(conn, tableName);
        record.setEngine(engine);
    }

    /**
     * 编码
     *
     * @param record   the {@link Table} instance
     * @param rs       the {@link ResultSet} instance
     * @param metaData the {@link DatabaseMetaData} instance
     * @param catalog  Database Name
     * @param conn     the {@link Connection} instance
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setCharset(@NotNull Table record,
                              @NotNull ResultSet rs,
                              @NotNull DatabaseMetaData metaData, @NotNull String catalog,
                              @NotNull Connection conn) throws SQLException {
        String tableName = record.getName();
        if (tableName == null) {
            return;
        }

        String charset = TableUtils.readCharset(conn, tableName);
        record.setCharset(charset);
    }

    /**
     * 排序规则
     *
     * @param record   the {@link Table} instance
     * @param rs       the {@link ResultSet} instance
     * @param metaData the {@link DatabaseMetaData} instance
     * @param catalog  Database Name
     * @param conn     the {@link Connection} instance
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setCollate(@NotNull Table record,
                              @NotNull ResultSet rs,
                              @NotNull DatabaseMetaData metaData, @NotNull String catalog,
                              @NotNull Connection conn) throws SQLException {
        String tableName = record.getName();
        if (tableName == null) {
            return;
        }

        String collate = TableUtils.readCollation(conn, tableName);
        record.setCollate(collate);
    }

    /**
     * 自增
     *
     * @param record   the {@link Table} instance
     * @param rs       the {@link ResultSet} instance
     * @param metaData the {@link DatabaseMetaData} instance
     * @param catalog  Database Name
     * @param conn     the {@link Connection} instance
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setAutoIncrement(@NotNull Table record,
                                    @NotNull ResultSet rs,
                                    @NotNull DatabaseMetaData metaData, @NotNull String catalog,
                                    @NotNull Connection conn) throws SQLException {
        String tableName = record.getName();
        if (tableName == null) {
            return;
        }

        Long autoIncrement = TableUtils.readAutoIncrement(conn, tableName);
        record.setAutoIncrement(autoIncrement);
    }

    /**
     * 字段描述
     *
     * @param record   the {@link Table} instance
     * @param metaData the {@link DatabaseMetaData} instance
     * @param catalog  Database Name
     * @param conn     the {@link Connection} instance
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setColumns(@NotNull Table record,
                              @NotNull DatabaseMetaData metaData, @NotNull String catalog,
                              @NotNull Connection conn) throws SQLException {
        String tableName = record.getName();
        if (tableName == null) {
            return;
        }

        ResultSet rs = metaData.getColumns(catalog, null, tableName, null);
        if (rs == null) {
            return;
        }

        ColumnReader columnReader = getColumnReader();
        List<Column> columns = new ArrayList<>();

        try (rs) {
            List<String> primaryKey = record.getPrimaryKey();
            while (rs.next()) {
                Column column = columnReader.read(rs, conn, tableName, primaryKey);
                columns.add(column);
            }
        }

        record.setColumns(columns);
    }

    /**
     * 索引
     *
     * @param record the {@link Table} instance
     * @param conn   the {@link Connection} instance
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setKeys(@NotNull Table record,
                           @NotNull Connection conn) throws SQLException {
        String tableName = record.getName();
        if (tableName == null) {
            return;
        }

        List<Key> keys = KEY_READER.read(conn, tableName);
        if (keys == null) {
            return;
        }

        setPrimaryKey(record, keys);
        setUniqueKeys(record, keys);
        setKeys(record, keys);
    }

    /**
     * 主键索引
     *
     * @param record the {@link Table} instance
     * @param keys   [ the {@link Key} instance ]
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setPrimaryKey(@NotNull Table record,
                                 @NotNull List<Key> keys) throws SQLException {
        List<String> data = keys.stream()
                .filter(Objects::nonNull)
                .filter(Key::isPrimary)
                .map(Key::getColumnNames)
                .filter(Objects::nonNull)
                .filter(x -> !x.isEmpty())
                .findFirst()
                .orElse(Collections.emptyList());
        if (data.isEmpty()) {
            return;
        }

        record.setPrimaryKey(data);
    }

    /**
     * 唯一索引
     *
     * @param record the {@link Table} instance
     * @param keys   [ the {@link Key} instance ]
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setUniqueKeys(@NotNull Table record,
                                 @NotNull List<Key> keys) throws SQLException {
        Map<String, List<String>> data = keys.stream()
                .filter(Objects::nonNull)
                .filter(Key::isUnique)
                .filter(x -> !x.isPrimary())
                .collect(Collectors.toMap(Key::getName, Key::getColumnNames));
        if (data.isEmpty()) {
            return;
        }

        record.setUniqueKeys(data);
    }

    /**
     * 普通索引
     *
     * @param record the {@link Table} instance
     * @param keys   [ the {@link Key} instance ]
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setKeys(@NotNull Table record,
                           @NotNull List<Key> keys) throws SQLException {
        Map<String, List<String>> data = keys.stream()
                .filter(Objects::nonNull)
                .filter(x -> !x.isPrimary())
                .filter(x -> !x.isUnique())
                .collect(Collectors.toMap(Key::getName, Key::getColumnNames));
        if (data.isEmpty()) {
            return;
        }

        record.setKeys(data);
    }

    /**
     * After Properties Set
     *
     * @param record   the {@link Table} instance
     * @param rs       the {@link ResultSet} instance
     * @param metaData the {@link DatabaseMetaData} instance
     * @param catalog  Database Name
     * @param conn     the {@link Connection} instance
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void afterPropertiesSet(@NotNull Table record,
                                      @NotNull ResultSet rs,
                                      @NotNull DatabaseMetaData metaData, @NotNull String catalog,
                                      @NotNull Connection conn) throws SQLException {
    }

    @NotNull
    public ColumnReader getColumnReader() {
        return columnReader;
    }

}
