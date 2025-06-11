package io.github.winter.database.table;

import jakarta.validation.constraints.NotNull;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表描述
 *
 * @author changebooks@qq.com
 */
public final class TableUtils {
    /**
     * 编码正则
     */
    private static final Pattern CHARSET_PATTERN = Pattern.compile(" DEFAULT CHARSET\\s*=\\s*(\\w+)", Pattern.CASE_INSENSITIVE);

    private TableUtils() {
    }

    /**
     * 引擎
     *
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @return Engine
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    public static String readEngine(@NotNull Connection conn, @NotNull String tableName) throws SQLException {
        String command = "SELECT ENGINE FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
        PreparedStatement stat = conn.prepareStatement(command);
        if (stat == null) {
            return null;
        }

        try (stat) {
            String tableSchema = conn.getCatalog();

            stat.setString(1, tableSchema);
            stat.setString(2, tableName);

            ResultSet rs = stat.executeQuery();
            if (rs == null) {
                return null;
            }

            try (rs) {
                if (rs.next()) {
                    return rs.getString("ENGINE");
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * 编码
     *
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @return Default Charset
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    public static String readCharset(@NotNull Connection conn, @NotNull String tableName) throws SQLException {
        String createTable = readCreateTable(conn, tableName);
        if (createTable == null) {
            return null;
        }

        Matcher matcher = CHARSET_PATTERN.matcher(createTable);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    /**
     * 排序规则
     *
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @return Table Collation
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    public static String readCollation(@NotNull Connection conn, @NotNull String tableName) throws SQLException {
        String command = "SELECT TABLE_COLLATION FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
        PreparedStatement stat = conn.prepareStatement(command);
        if (stat == null) {
            return null;
        }

        try (stat) {
            String tableSchema = conn.getCatalog();

            stat.setString(1, tableSchema);
            stat.setString(2, tableName);

            ResultSet rs = stat.executeQuery();
            if (rs == null) {
                return null;
            }

            try (rs) {
                if (rs.next()) {
                    return rs.getString("TABLE_COLLATION");
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * 自增
     *
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @return Next Auto Increment
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    public static Long readAutoIncrement(@NotNull Connection conn, @NotNull String tableName) throws SQLException {
        String command = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
        PreparedStatement stat = conn.prepareStatement(command);
        if (stat == null) {
            return null;
        }

        try (stat) {
            String tableSchema = conn.getCatalog();

            stat.setString(1, tableSchema);
            stat.setString(2, tableName);

            ResultSet rs = stat.executeQuery();
            if (rs == null) {
                return null;
            }

            try (rs) {
                if (rs.next()) {
                    return rs.getLong("AUTO_INCREMENT");
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Show Create Table
     *
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @return Create Table
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    public static String readCreateTable(@NotNull Connection conn, @NotNull String tableName) throws SQLException {
        try (Statement stat = conn.createStatement()) {
            String command = "SHOW CREATE TABLE " + tableName;
            ResultSet rs = stat.executeQuery(command);
            if (rs == null) {
                return null;
            }

            try (rs) {
                if (rs.next()) {
                    return rs.getString("Create Table");
                } else {
                    return null;
                }
            }
        }
    }

}
