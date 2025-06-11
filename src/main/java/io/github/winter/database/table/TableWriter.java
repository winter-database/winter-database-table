package io.github.winter.database.table;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 表描述
 *
 * @author changebooks@qq.com
 */
public class TableWriter {
    /**
     * the {@link KeyWriter} instance
     */
    private static final KeyWriter KEY_WRITER = new KeyWriter();

    /**
     * the {@link ColumnWriter} instance
     */
    private final ColumnWriter columnWriter;

    public TableWriter() {
        this.columnWriter = new ColumnWriter();
    }

    public TableWriter(ColumnWriter columnWriter) {
        Objects.requireNonNull(columnWriter, "columnWriter must not be null");

        this.columnWriter = columnWriter;
    }

    /**
     * Build SQL
     *
     * @param record the {@link Table} instance
     * @return CREATE TABLE name (...) ENGINE AUTO_INCREMENT DEFAULT CHARSET COLLATE COMMENT
     */
    public String write(@NotNull Table record) {
        String name = getName(record);
        String engine = getEngine(record);
        String autoIncrement = getAutoIncrement(record);
        String charset = getCharset(record);
        String collate = getCollate(record);
        String remark = getRemark(record);

        List<String> elements = new ArrayList<>();

        List<String> columns = getColumns(record);
        if (columns != null) {
            elements.addAll(columns);
        }

        List<String> keys = getKeys(record);
        if (keys != null) {
            elements.addAll(keys);
        }

        String body = "  " + String.join(",\n  ", elements);
        return "CREATE TABLE " + name + " (\n" + body + "\n)" + engine + autoIncrement + charset + collate + remark + ";\n";
    }

    /**
     * 表名
     *
     * @param record the {@link Table} instance
     * @return Table Name
     */
    protected String getName(@NotNull Table record) {
        String name = record.getName();
        if (name != null) {
            return name.trim();
        } else {
            return "";
        }
    }

    /**
     * 备注
     *
     * @param record the {@link Table} instance
     * @return COMMENT=''
     */
    protected String getRemark(@NotNull Table record) {
        String remark = record.getRemark();
        if (remark != null) {
            return " COMMENT='" + remark + "'";
        } else {
            return "";
        }
    }

    /**
     * 引擎
     *
     * @param record the {@link Table} instance
     * @return ENGINE
     */
    protected String getEngine(@NotNull Table record) {
        String rawEngine = record.getEngine();
        if (rawEngine == null) {
            return "";
        }

        String engine = rawEngine.trim();
        if (engine.isEmpty()) {
            return "";
        } else {
            return " ENGINE=" + engine;
        }
    }

    /**
     * 编码
     *
     * @param record the {@link Table} instance
     * @return DEFAULT CHARSET
     */
    protected String getCharset(@NotNull Table record) {
        String rawCharset = record.getCharset();
        if (rawCharset == null) {
            return "";
        }

        String charset = rawCharset.trim();
        if (charset.isEmpty()) {
            return "";
        } else {
            return " DEFAULT CHARSET=" + charset;
        }
    }

    /**
     * 排序规则
     *
     * @param record the {@link Table} instance
     * @return COLLATE
     */
    protected String getCollate(@NotNull Table record) {
        String rawCollate = record.getCollate();
        if (rawCollate == null) {
            return "";
        }

        String collate = rawCollate.trim();
        if (collate.isEmpty()) {
            return "";
        } else {
            return " COLLATE=" + collate;
        }
    }

    /**
     * 自增
     *
     * @param record the {@link Table} instance
     * @return AUTO_INCREMENT
     */
    protected String getAutoIncrement(@NotNull Table record) {
        Long autoIncrement = record.getAutoIncrement();
        if (autoIncrement == null) {
            return "";
        }

        if (autoIncrement <= 1) {
            return "";
        } else {
            return " AUTO_INCREMENT=" + autoIncrement;
        }
    }

    /**
     * [ 字段描述 ]
     *
     * @param record the {@link Table} instance
     * @return [ COLUMN SQL ]
     */
    protected List<String> getColumns(@NotNull Table record) {
        ColumnWriter columnWriter = getColumnWriter();
        return columnWriter.write(record);
    }

    /**
     * [ 索引描述 ]
     *
     * @param record the {@link Table} instance
     * @return [ KEY SQL ]
     */
    protected List<String> getKeys(@NotNull Table record) {
        return KEY_WRITER.write(record);
    }

    @NotNull
    public ColumnWriter getColumnWriter() {
        return columnWriter;
    }

}
