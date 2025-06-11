package io.github.winter.database.table;

import jakarta.validation.constraints.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 字段描述
 *
 * @author changebooks@qq.com
 */
public final class ColumnUtils {

    private ColumnUtils() {
    }

    /**
     * 扩展
     *
     * @param conn       the {@link Connection} instance
     * @param tableName  Table Name
     * @param columnName Column Name
     * @return Column Extra
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    public static String readExtra(@NotNull Connection conn, @NotNull String tableName, @NotNull String columnName) throws SQLException {
        String command = "SELECT EXTRA FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? AND COLUMN_NAME = ?";
        PreparedStatement stat = conn.prepareStatement(command);
        if (stat == null) {
            return null;
        }

        try (stat) {
            String tableSchema = conn.getCatalog();

            stat.setString(1, tableSchema);
            stat.setString(2, tableName);
            stat.setString(3, columnName);

            ResultSet rs = stat.executeQuery();
            if (rs == null) {
                return null;
            }

            try (rs) {
                if (rs.next()) {
                    return rs.getString("EXTRA");
                } else {
                    return null;
                }
            }
        }
    }

}
