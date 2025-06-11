package io.github.winter.database.table;

import io.github.winter.boot.tuple.Value;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.*;

/**
 * 表概要
 *
 * @author changebooks@qq.com
 */
public final class TableSchema implements Serializable {
    /**
     * FROM table
     * INSERT INTO table
     * UPDATE table
     * DELETE FROM table
     */
    private String tableName;

    /**
     * Primary Key
     */
    private String idName;

    /**
     * [ Column ]
     */
    private List<Column> columns = new ArrayList<>();

    /**
     * ALL [ Column Name ]
     */
    private List<String> columnNames = new ArrayList<>();

    /**
     * [ AUTO_INCREMENT ]
     */
    private Set<String> columnsOnAutoIncrement = new HashSet<>();

    /**
     * [ Column Name : Value Type ]
     */
    private Map<String, Class<?>> valueTypes = new HashMap<>();

    /**
     * [ Column Name : Default Value ]
     */
    private Map<String, Value> defaultValues = new HashMap<>();

    /**
     * "column, column" used for SELECT
     */
    private String joinedColumnsOnSelect;

    /**
     * [ Column Name ] used for INSERT
     */
    private List<String> columnsOnInsert = new ArrayList<>();

    /**
     * [ DEFAULT CURRENT_TIMESTAMP ] used for INSERT
     */
    private Set<String> defaultCurrentDateOnInsert = new HashSet<>();

    /**
     * "column, column" used for INSERT
     */
    private String joinedColumnsOnInsert;

    /**
     * "?, ?" used for INSERT
     */
    private String joinedValuesOnInsert;

    /**
     * [ Column Name ] used for UPDATE
     */
    private Set<String> columnsOnUpdate = new HashSet<>();

    /**
     * [ DEFAULT ON UPDATE CURRENT_TIMESTAMP ] used for UPDATE
     */
    private Set<String> defaultCurrentDateOnUpdate = new HashSet<>();

    @NotNull
    public String getTableName() {
        return tableName != null ? tableName : "";
    }

    public void setTableName(String tableName) {
        this.tableName = tableName != null ? tableName.trim() : "";
    }

    @NotNull
    public String getIdName() {
        return idName != null ? idName : "";
    }

    public void setIdName(String idName) {
        this.idName = idName != null ? idName.trim() : "";
    }

    @NotNull
    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns != null ? columns : new ArrayList<>();
    }

    @NotNull
    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames != null ? columnNames : new ArrayList<>();
    }

    @NotNull
    public Set<String> getColumnsOnAutoIncrement() {
        return columnsOnAutoIncrement;
    }

    public void setColumnsOnAutoIncrement(Set<String> columnsOnAutoIncrement) {
        this.columnsOnAutoIncrement = columnsOnAutoIncrement != null ? columnsOnAutoIncrement : new HashSet<>();
    }

    @NotNull
    public Map<String, Class<?>> getValueTypes() {
        return valueTypes;
    }

    public void setValueTypes(Map<String, Class<?>> valueTypes) {
        this.valueTypes = valueTypes != null ? valueTypes : new HashMap<>();
    }

    @NotNull
    public Map<String, Value> getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(Map<String, Value> defaultValues) {
        this.defaultValues = defaultValues != null ? defaultValues : new HashMap<>();
    }

    @NotNull
    public String getJoinedColumnsOnSelect() {
        return joinedColumnsOnSelect != null ? joinedColumnsOnSelect : "";
    }

    public void setJoinedColumnsOnSelect(String joinedColumnsOnSelect) {
        this.joinedColumnsOnSelect = joinedColumnsOnSelect != null ? joinedColumnsOnSelect.trim() : "";
    }

    @NotNull
    public List<String> getColumnsOnInsert() {
        return columnsOnInsert;
    }

    public void setColumnsOnInsert(List<String> columnsOnInsert) {
        this.columnsOnInsert = columnsOnInsert != null ? columnsOnInsert : new ArrayList<>();
    }

    @NotNull
    public Set<String> getDefaultCurrentDateOnInsert() {
        return defaultCurrentDateOnInsert;
    }

    public void setDefaultCurrentDateOnInsert(Set<String> defaultCurrentDateOnInsert) {
        this.defaultCurrentDateOnInsert = defaultCurrentDateOnInsert != null ? defaultCurrentDateOnInsert : new HashSet<>();
    }

    @NotNull
    public String getJoinedColumnsOnInsert() {
        return joinedColumnsOnInsert != null ? joinedColumnsOnInsert : "";
    }

    public void setJoinedColumnsOnInsert(String joinedColumnsOnInsert) {
        this.joinedColumnsOnInsert = joinedColumnsOnInsert != null ? joinedColumnsOnInsert.trim() : "";
    }

    @NotNull
    public String getJoinedValuesOnInsert() {
        return joinedValuesOnInsert != null ? joinedValuesOnInsert : "";
    }

    public void setJoinedValuesOnInsert(String joinedValuesOnInsert) {
        this.joinedValuesOnInsert = joinedValuesOnInsert != null ? joinedValuesOnInsert.trim() : "";
    }

    @NotNull
    public Set<String> getColumnsOnUpdate() {
        return columnsOnUpdate;
    }

    public void setColumnsOnUpdate(Set<String> columnsOnUpdate) {
        this.columnsOnUpdate = columnsOnUpdate != null ? columnsOnUpdate : new HashSet<>();
    }

    @NotNull
    public Set<String> getDefaultCurrentDateOnUpdate() {
        return defaultCurrentDateOnUpdate;
    }

    public void setDefaultCurrentDateOnUpdate(Set<String> defaultCurrentDateOnUpdate) {
        this.defaultCurrentDateOnUpdate = defaultCurrentDateOnUpdate != null ? defaultCurrentDateOnUpdate : new HashSet<>();
    }

}
