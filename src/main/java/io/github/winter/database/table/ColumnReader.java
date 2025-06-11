package io.github.winter.database.table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 字段描述
 *
 * @author changebooks@qq.com
 */
public class ColumnReader {
    /**
     * Read Column
     *
     * @param rs         the {@link ResultSet} instance
     * @param conn       the {@link Connection} instance
     * @param tableName  Table Name
     * @param primaryKey [ Column Name ]
     * @return the {@link Column} instance
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    public Column read(@NotNull ResultSet rs, @NotNull Connection conn, @NotBlank String tableName,
                       List<String> primaryKey) throws SQLException {
        Column result = new Column();

        setName(result, rs, conn, tableName);
        setRemark(result, rs, conn, tableName);
        setType(result, rs, conn, tableName);
        setTypeName(result, rs, conn, tableName);
        setClazz(result, rs, conn, tableName);
        setSize(result, rs, conn, tableName);
        setScale(result, rs, conn, tableName);
        setDefaultValue(result, rs, conn, tableName);
        setNullable(result, rs, conn, tableName);
        setUnsigned(result, rs, conn, tableName);
        setId(result, rs, conn, tableName, primaryKey);
        setAutoIncrement(result, rs, conn, tableName);

        afterPropertiesSet(result, rs, conn, tableName);
        return result;
    }

    /**
     * 字段名
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setName(@NotNull Column record,
                           @NotNull ResultSet rs,
                           @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        String columnName = rs.getString("COLUMN_NAME");
        if (columnName == null) {
            return;
        }

        String name = columnName.trim();
        record.setName(name);
    }

    /**
     * 备注
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setRemark(@NotNull Column record,
                             @NotNull ResultSet rs,
                             @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        String remark = rs.getString("REMARKS");
        record.setRemark(remark);
    }

    /**
     * 类型，{@link java.sql.Types}
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setType(@NotNull Column record,
                           @NotNull ResultSet rs,
                           @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        int type = rs.getInt("DATA_TYPE");
        record.setType(type);
    }

    /**
     * 类型名，{@link java.sql.Types}
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setTypeName(@NotNull Column record,
                               @NotNull ResultSet rs,
                               @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        String rawTypeName = rs.getString("TYPE_NAME");
        if (rawTypeName == null) {
            return;
        }

        String typeName = rawTypeName
                .toUpperCase()
                .replace(" UNSIGNED", "")
                .trim();
        record.setTypeName(typeName);
    }

    /**
     * 值类型
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setClazz(@NotNull Column record,
                            @NotNull ResultSet rs,
                            @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        int type = record.getType();
        Class<?> clazz = TypeMap.lookup(type);
        record.setClazz(clazz);
    }

    /**
     * 长度
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setSize(@NotNull Column record,
                           @NotNull ResultSet rs,
                           @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        int size = rs.getInt("COLUMN_SIZE");
        record.setSize(size);
    }

    /**
     * 精度
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setScale(@NotNull Column record,
                            @NotNull ResultSet rs,
                            @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        int scale = rs.getInt("DECIMAL_DIGITS");
        record.setScale(scale);
    }

    /**
     * 默认值
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setDefaultValue(@NotNull Column record,
                                   @NotNull ResultSet rs,
                                   @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        Class<?> clazz = record.getClazz();
        if (clazz == null) {
            throw new RuntimeException(String.format("clazz must not be null, columnName: %s", record.getName()));
        }

        if (clazz == String.class) {
            setDefaultString(record, rs, conn, tableName);
            return;
        }

        if (clazz == Integer.class) {
            setDefaultInteger(record, rs, conn, tableName);
            return;
        }

        if (clazz == Long.class) {
            setDefaultLong(record, rs, conn, tableName);
            return;
        }

        if (clazz == BigDecimal.class) {
            setDefaultBigDecimal(record, rs, conn, tableName);
            return;
        }

        if (clazz == Date.class) {
            setDefaultCurrentDate(record, rs, conn, tableName);
            return;
        }

        throw new RuntimeException(String.format("unsupported clazz, clazz: %s, columnName: %s", clazz, record.getName()));
    }

    /**
     * 默认字符串
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setDefaultString(@NotNull Column record,
                                    @NotNull ResultSet rs,
                                    @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        String defaultValue = rs.getString("COLUMN_DEF");
        record.setDefaultString(defaultValue);
    }

    /**
     * 默认整数
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setDefaultInteger(@NotNull Column record,
                                     @NotNull ResultSet rs,
                                     @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        Integer defaultValue = rs.getInt("COLUMN_DEF");
        record.setDefaultInteger(defaultValue);
    }

    /**
     * 默认长整数
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setDefaultLong(@NotNull Column record,
                                  @NotNull ResultSet rs,
                                  @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        Long defaultValue = rs.getLong("COLUMN_DEF");
        record.setDefaultLong(defaultValue);
    }

    /**
     * 默认小数
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setDefaultBigDecimal(@NotNull Column record,
                                        @NotNull ResultSet rs,
                                        @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        BigDecimal defaultValue = rs.getBigDecimal("COLUMN_DEF");
        record.setDefaultBigDecimal(defaultValue);
    }

    /**
     * 默认当前时间？
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setDefaultCurrentDate(@NotNull Column record,
                                         @NotNull ResultSet rs,
                                         @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        setDefaultCurrentDateOnInsert(record, rs, conn, tableName);
        setDefaultCurrentDateOnUpdate(record, rs, conn, tableName);
    }

    /**
     * 新增记录，默认当前时间？
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setDefaultCurrentDateOnInsert(@NotNull Column record,
                                                 @NotNull ResultSet rs,
                                                 @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        String defaultValue = rs.getString("COLUMN_DEF");
        if ("CURRENT_TIMESTAMP".equalsIgnoreCase(defaultValue)) {
            record.setDefaultCurrentDateOnInsert(true);
        }
    }

    /**
     * 修改记录，默认当前时间？
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setDefaultCurrentDateOnUpdate(@NotNull Column record,
                                                 @NotNull ResultSet rs,
                                                 @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        String columnName = record.getName();
        if (columnName == null) {
            return;
        }

        String rawExtra = ColumnUtils.readExtra(conn, tableName, columnName);
        if (rawExtra == null) {
            return;
        }

        String extra = rawExtra.toUpperCase();
        if (extra.contains("ON UPDATE CURRENT_TIMESTAMP")) {
            record.setDefaultCurrentDateOnUpdate(true);
        }
    }

    /**
     * 可空？
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setNullable(@NotNull Column record,
                               @NotNull ResultSet rs,
                               @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        String nullable = rs.getString("IS_NULLABLE");
        if ("YES".equalsIgnoreCase(nullable)) {
            record.setNullable(true);
        }
    }

    /**
     * 非负？
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setUnsigned(@NotNull Column record,
                               @NotNull ResultSet rs,
                               @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        String rawTypeName = rs.getString("TYPE_NAME");
        if (rawTypeName == null) {
            return;
        }

        String typeName = rawTypeName.toUpperCase();
        if (typeName.contains(" UNSIGNED")) {
            record.setUnsigned(true);
        }
    }

    /**
     * 主键？
     *
     * @param record     the {@link Column} instance
     * @param rs         the {@link ResultSet} instance
     * @param conn       the {@link Connection} instance
     * @param tableName  Table Name
     * @param primaryKey [ Column Name ]
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setId(@NotNull Column record,
                         @NotNull ResultSet rs,
                         @NotNull Connection conn, @NotNull String tableName,
                         List<String> primaryKey) throws SQLException {
        if (primaryKey == null) {
            return;
        }

        String name = record.getName();
        if (name == null) {
            return;
        }

        for (String columnName : primaryKey) {
            if (columnName == null) {
                continue;
            }

            if (name.equalsIgnoreCase(columnName)) {
                record.setId(true);
                break;
            }
        }
    }

    /**
     * 自增？
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void setAutoIncrement(@NotNull Column record,
                                    @NotNull ResultSet rs,
                                    @NotNull Connection conn, @NotNull String tableName) throws SQLException {
        String autoIncrement = rs.getString("IS_AUTOINCREMENT");
        if ("YES".equalsIgnoreCase(autoIncrement)) {
            record.setAutoIncrement(true);
        }
    }

    /**
     * After Properties Set
     *
     * @param record    the {@link Column} instance
     * @param rs        the {@link ResultSet} instance
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    protected void afterPropertiesSet(@NotNull Column record,
                                      @NotNull ResultSet rs,
                                      @NotNull Connection conn, @NotNull String tableName) throws SQLException {
    }

}
