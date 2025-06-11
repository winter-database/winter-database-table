package io.github.winter.database.table;

import jakarta.validation.constraints.NotNull;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 表名
 *
 * @author changebooks@qq.com
 */
public final class TableNameReader {

    private TableNameReader() {
    }

    /**
     * Read All Table Name
     *
     * @param conn the {@link Connection} instance
     * @return [ Table Name ]
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    public static List<String> read(@NotNull Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        String catalog = conn.getCatalog();

        ResultSet rs = metaData.getTables(catalog, null, null, new String[]{"TABLE"});
        if (rs == null) {
            return null;
        }

        List<String> result = new ArrayList<>();

        try (rs) {
            while (rs.next()) {
                String rawTableName = rs.getString("TABLE_NAME");
                if (rawTableName == null) {
                    continue;
                }

                String tableName = rawTableName.trim();
                if (tableName.isEmpty()) {
                    continue;
                }

                result.add(tableName);
            }
        }

        return result;
    }

}
