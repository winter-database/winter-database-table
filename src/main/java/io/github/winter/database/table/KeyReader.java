package io.github.winter.database.table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 索引描述
 *
 * @author changebooks@qq.com
 */
public class KeyReader {
    /**
     * Read Key
     *
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @return [ the {@link Key} instance ]
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    public List<Key> read(@NotNull Connection conn, @NotBlank String tableName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        String catalog = conn.getCatalog();

        ResultSet rs = metaData.getIndexInfo(catalog, null, tableName, false, false);
        if (rs == null) {
            return null;
        }

        // [ Index Name : Key ]
        Map<String, Key> data = new HashMap<>();

        try (rs) {
            while (rs.next()) {
                String rawIndexName = rs.getString("INDEX_NAME");
                if (rawIndexName == null) {
                    continue;
                }

                String rawColumnName = rs.getString("COLUMN_NAME");
                if (rawColumnName == null) {
                    continue;
                }

                String indexName = rawIndexName.trim();
                String columnName = rawColumnName.trim();

                Key record = data.getOrDefault(indexName, new Key());
                record.setName(indexName);

                List<String> rawColumnNames = record.getColumnNames();
                List<String> columnNames = Optional.ofNullable(rawColumnNames).orElse(new ArrayList<>());
                columnNames.add(columnName);
                record.setColumnNames(columnNames);

                if (isPrimary(indexName)) {
                    record.setPrimary(true);
                }

                if (isUnique(rs)) {
                    record.setUnique(true);
                }

                data.put(indexName, record);
            }
        }

        return new ArrayList<>(data.values());
    }

    /**
     * 主键？
     *
     * @param indexName 索引名
     * @return Primary Key ?
     */
    protected boolean isPrimary(@NotNull String indexName) {
        return "PRIMARY".equalsIgnoreCase(indexName);
    }

    /**
     * 唯一？
     *
     * @param rs the {@link ResultSet} instance
     * @return Unique Key ?
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected boolean isUnique(@NotNull ResultSet rs) throws SQLException {
        boolean nonUnique = rs.getBoolean("NON_UNIQUE");
        return !nonUnique;
    }

}
