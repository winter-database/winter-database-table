package io.github.winter.database.table;

import io.github.winter.boot.tuple.Value;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 表概要
 *
 * @author changebooks@qq.com
 */
public final class TableSchemaReader {
    /**
     * the {@link TableReader} instance
     */
    private static TableReader tableReader = new TableReader();

    private TableSchemaReader() {
    }

    /**
     * Read Schema
     *
     * @param conn      the {@link Connection} instance
     * @param tableName Table Name
     * @return the {@link TableSchema} instance
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or
     *                      this method is called on a closed result set
     */
    public static TableSchema read(@NotNull Connection conn, @NotNull String tableName) throws SQLException {
        TableReader tableReader = getTableReader();

        Table table = tableReader.read(conn, tableName);
        if (table != null) {
            return read(table);
        } else {
            return null;
        }
    }

    /**
     * Read Schema
     *
     * @param table the {@link Table} instance
     * @return the {@link TableSchema} instance
     */
    @NotNull
    public static TableSchema read(@NotNull Table table) {
        TableSchema result = new TableSchema();

        setTableName(result, table);
        setIdName(result, table);
        setColumns(result, table);
        setColumnNames(result);
        setColumnsOnAutoIncrement(result);
        setValueTypes(result);
        setDefaultValues(result);
        setJoinedColumnsOnSelect(result);
        setColumnsOnInsert(result);
        setDefaultCurrentDateOnInsert(result);
        setJoinedColumnsOnInsert(result);
        setJoinedValuesOnInsert(result);
        setColumnsOnUpdate(result);
        setDefaultCurrentDateOnUpdate(result);

        return result;
    }

    /**
     * FROM table
     * INSERT INTO table
     * UPDATE table
     * DELETE FROM table
     *
     * @param tableSchema the {@link TableSchema} instance
     * @param table       the {@link Table} instance
     */
    private static void setTableName(@NotNull TableSchema tableSchema, @NotNull Table table) {
        String tableName = table.getName();
        tableSchema.setTableName(tableName);
    }

    /**
     * Primary Key
     *
     * @param tableSchema the {@link TableSchema} instance
     * @param table       the {@link Table} instance
     */
    private static void setIdName(@NotNull TableSchema tableSchema, @NotNull Table table) {
        List<String> primaryKey = table.getPrimaryKey();
        if (primaryKey == null) {
            return;
        }

        String idName = primaryKey.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(Predicate.not(String::isEmpty))
                .findFirst()
                .orElse("");
        tableSchema.setIdName(idName);
    }

    /**
     * [ Column ]
     *
     * @param tableSchema the {@link TableSchema} instance
     * @param table       the {@link Table} instance
     */
    private static void setColumns(@NotNull TableSchema tableSchema, @NotNull Table table) {
        List<Column> rawColumns = table.getColumns();
        if (rawColumns == null) {
            return;
        }

        List<Column> columns = rawColumns.stream()
                .filter(Objects::nonNull)
                .peek(x -> {
                    String columnName = x.getName();
                    String name = columnName != null ? columnName.trim() : "";
                    x.setName(name);
                })
                .toList();
        tableSchema.setColumns(columns);
    }

    /**
     * ALL [ Column Name ]
     *
     * @param tableSchema the {@link TableSchema} instance
     */
    private static void setColumnNames(@NotNull TableSchema tableSchema) {
        List<Column> columns = tableSchema.getColumns();

        List<String> columnNames = columns.stream()
                .map(Column::getName)
                .filter(Predicate.not(String::isEmpty))
                .toList();
        tableSchema.setColumnNames(columnNames);
    }

    /**
     * [ AUTO_INCREMENT ]
     *
     * @param tableSchema the {@link TableSchema} instance
     */
    private static void setColumnsOnAutoIncrement(@NotNull TableSchema tableSchema) {
        List<Column> columns = tableSchema.getColumns();

        Set<String> columnNames = columns.stream()
                .filter(Column::isAutoIncrement)
                .map(Column::getName)
                .filter(Predicate.not(String::isEmpty))
                .collect(Collectors.toSet());
        tableSchema.setColumnsOnAutoIncrement(columnNames);
    }

    /**
     * [ Column Name : Value Type ]
     *
     * @param tableSchema the {@link TableSchema} instance
     */
    private static void setValueTypes(@NotNull TableSchema tableSchema) {
        List<Column> columns = tableSchema.getColumns();

        Map<String, Class<?>> valueTypes = columns.stream()
                .collect(
                        Collectors.toMap(
                                Column::getName,
                                Column::getClazz,
                                (x, y) -> y
                        )
                );
        tableSchema.setValueTypes(valueTypes);
    }

    /**
     * [ Column Name : Default Value ]
     *
     * @param tableSchema the {@link TableSchema} instance
     */
    private static void setDefaultValues(@NotNull TableSchema tableSchema) {
        List<Column> columns = tableSchema.getColumns();

        Map<String, Value> defaultValues = columns.stream()
                .collect(
                        Collectors.toMap
                                (
                                        Column::getName,
                                        column ->
                                        {
                                            Class<?> clazz = column.getClazz();
                                            if (clazz == null) {
                                                throw new RuntimeException(String.format("clazz must not be null, columnName: %s", column.getName()));
                                            }

                                            if (clazz == String.class) {
                                                return new Value(column.getDefaultString());
                                            }

                                            if (clazz == Integer.class) {
                                                return new Value(column.getDefaultInteger());
                                            }

                                            if (clazz == Long.class) {
                                                return new Value(column.getDefaultLong());
                                            }

                                            if (clazz == BigDecimal.class) {
                                                return new Value(column.getDefaultBigDecimal());
                                            }

                                            if (clazz == Date.class) {
                                                return new Value(column.getDefaultDate());
                                            }

                                            throw new RuntimeException(String.format("unsupported clazz, clazz: %s, columnName: %s", clazz, column.getName()));
                                        },
                                        (x, y) -> y
                                )
                );
        tableSchema.setDefaultValues(defaultValues);
    }

    /**
     * "column, column" used for SELECT
     *
     * @param tableSchema the {@link TableSchema} instance
     */
    private static void setJoinedColumnsOnSelect(@NotNull TableSchema tableSchema) {
        List<String> columnNames = tableSchema.getColumnNames();

        String joinedColumns = String.join(", ", columnNames);
        tableSchema.setJoinedColumnsOnSelect(joinedColumns);
    }

    /**
     * [ Column Name ] used for INSERT
     *
     * @param tableSchema the {@link TableSchema} instance
     */
    private static void setColumnsOnInsert(@NotNull TableSchema tableSchema) {
        List<Column> columns = tableSchema.getColumns();

        List<String> columnNames = columns.stream()
                .filter(Predicate.not(Column::isDefaultCurrentDateOnInsert))
                .map(Column::getName)
                .filter(Predicate.not(String::isEmpty))
                .toList();
        tableSchema.setColumnsOnInsert(columnNames);
    }

    /**
     * [ DEFAULT CURRENT_TIMESTAMP ] used for INSERT
     *
     * @param tableSchema the {@link TableSchema} instance
     */
    private static void setDefaultCurrentDateOnInsert(@NotNull TableSchema tableSchema) {
        List<Column> columns = tableSchema.getColumns();

        Set<String> columnNames = columns.stream()
                .filter(Column::isDefaultCurrentDateOnInsert)
                .map(Column::getName)
                .filter(Predicate.not(String::isEmpty))
                .collect(Collectors.toSet());
        tableSchema.setDefaultCurrentDateOnInsert(columnNames);
    }

    /**
     * "column, column" used for INSERT
     *
     * @param tableSchema the {@link TableSchema} instance
     */
    private static void setJoinedColumnsOnInsert(@NotNull TableSchema tableSchema) {
        List<String> columnNames = tableSchema.getColumnsOnInsert();

        String joinedColumns = String.join(", ", columnNames);
        tableSchema.setJoinedColumnsOnInsert(joinedColumns);
    }

    /**
     * "?, ?" used for INSERT
     *
     * @param tableSchema the {@link TableSchema} instance
     */
    private static void setJoinedValuesOnInsert(@NotNull TableSchema tableSchema) {
        List<String> columnNames = tableSchema.getColumnsOnInsert();

        String joinedValues = columnNames.stream()
                .map(x -> "?")
                .collect(Collectors.joining(", "));
        tableSchema.setJoinedValuesOnInsert(joinedValues);
    }

    /**
     * [ Column Name ] used for UPDATE
     *
     * @param tableSchema the {@link TableSchema} instance
     */
    private static void setColumnsOnUpdate(@NotNull TableSchema tableSchema) {
        List<Column> columns = tableSchema.getColumns();

        Set<String> columnNames = columns.stream()
                .filter(Predicate.not(Column::isDefaultCurrentDateOnUpdate))
                .filter(Predicate.not(Column::isDefaultCurrentDateOnInsert))
                .map(Column::getName)
                .filter(Predicate.not(String::isEmpty))
                .collect(Collectors.toSet());
        tableSchema.setColumnsOnUpdate(columnNames);
    }

    /**
     * [ DEFAULT ON UPDATE CURRENT_TIMESTAMP ] used for UPDATE
     *
     * @param tableSchema the {@link TableSchema} instance
     */
    private static void setDefaultCurrentDateOnUpdate(@NotNull TableSchema tableSchema) {
        List<Column> columns = tableSchema.getColumns();

        Set<String> columnNames = columns.stream()
                .filter(Column::isDefaultCurrentDateOnUpdate)
                .map(Column::getName)
                .filter(Predicate.not(String::isEmpty))
                .collect(Collectors.toSet());
        tableSchema.setDefaultCurrentDateOnUpdate(columnNames);
    }

    @NotNull
    public static TableReader getTableReader() {
        return tableReader;
    }

    public static void setTableReader(@NotNull TableReader tableReader) {
        TableSchemaReader.tableReader = tableReader;
    }

}
