package io.github.winter.database.table;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 字段描述
 *
 * @author changebooks@qq.com
 */
public class ColumnWriter {
    /**
     * Build SQL
     *
     * @param table the {@link Table} instance
     * @return [ COLUMN SQL ]
     */
    public List<String> write(@NotNull Table table) {
        List<Column> columns = table.getColumns();
        return write(columns);
    }

    /**
     * Build SQL
     *
     * @param list [ the {@link Column} instance ]
     * @return [ COLUMN SQL ]
     */
    public List<String> write(@NotNull List<Column> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .map(this::write)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(x -> !x.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Build SQL
     *
     * @param record the {@link Column} instance
     * @return name type[(size,scale)] [ unsigned ] [ NOT NULL ] [ DEFAULT | AUTO_INCREMENT ] [ COMMENT ]
     */
    public String write(@NotNull Column record) {
        String name = getName(record);
        String type = getType(record);
        String unsigned = getUnsigned(record);
        String nullable = getNullable(record);
        String defaultValue = getDefaultValue(record);
        String remark = getRemark(record);

        return name + type + unsigned + nullable + defaultValue + remark;
    }

    /**
     * 字段名
     *
     * @param record the {@link Column} instance
     * @return Column Name
     */
    protected String getName(@NotNull Column record) {
        String name = record.getName();
        if (name != null) {
            return name.trim();
        } else {
            return "";
        }
    }

    /**
     * 类型
     *
     * @param record the {@link Column} instance
     * @return Type[(Size, Scale)]
     */
    protected String getType(@NotNull Column record) {
        String rawTypeName = record.getTypeName();
        if (rawTypeName == null) {
            return "";
        }

        String typeName = rawTypeName.trim();
        if (typeName.isEmpty()) {
            return "";
        }

        int size = record.getSize();
        int scale = record.getScale();
        if (size > 0) {
            if (scale > 0) {
                return " " + typeName + "(" + size + "," + scale + ")";
            } else {
                return " " + typeName + "(" + size + ")";
            }
        } else {
            if (scale > 0) {
                return " " + typeName + "(0," + scale + ")";
            } else {
                return " " + typeName;
            }
        }
    }

    /**
     * 非负
     *
     * @param record the {@link Column} instance
     * @return unsigned
     */
    protected String getUnsigned(@NotNull Column record) {
        boolean unsigned = record.isUnsigned();
        if (unsigned) {
            return " unsigned";
        } else {
            return "";
        }
    }

    /**
     * 可空
     *
     * @param record the {@link Column} instance
     * @return NOT NULL
     */
    protected String getNullable(@NotNull Column record) {
        boolean nullable = record.isNullable();
        if (nullable) {
            return "";
        } else {
            return " NOT NULL";
        }
    }

    /**
     * 默认值
     *
     * @param record the {@link Column} instance
     * @return DEFAULT '0' | AUTO_INCREMENT
     */
    protected String getDefaultValue(@NotNull Column record) {
        String autoIncrement = getAutoIncrement(record);
        if (autoIncrement != null && !autoIncrement.isEmpty()) {
            return autoIncrement;
        }

        Class<?> clazz = record.getClazz();
        if (clazz == null) {
            return "";
        }

        if (clazz == String.class) {
            return getDefaultString(record);
        }

        if (clazz == Integer.class) {
            return getDefaultInteger(record);
        }

        if (clazz == Long.class) {
            return getDefaultLong(record);
        }

        if (clazz == BigDecimal.class) {
            return getDefaultBigDecimal(record);
        }

        if (clazz == Date.class) {
            return getDefaultCurrentDate(record);
        }

        return "";
    }

    /**
     * 默认字符串
     *
     * @param record the {@link Column} instance
     * @return DEFAULT ''
     */
    protected String getDefaultString(@NotNull Column record) {
        String defaultValue = record.getDefaultString();
        if (defaultValue != null) {
            return " DEFAULT '" + defaultValue + "'";
        } else {
            return "";
        }
    }

    /**
     * 默认整数
     *
     * @param record the {@link Column} instance
     * @return DEFAULT '0'
     */
    protected String getDefaultInteger(@NotNull Column record) {
        Integer defaultValue = record.getDefaultInteger();
        if (defaultValue != null) {
            return " DEFAULT '" + defaultValue + "'";
        } else {
            return "";
        }
    }

    /**
     * 默认长整数
     *
     * @param record the {@link Column} instance
     * @return DEFAULT '0'
     */
    protected String getDefaultLong(@NotNull Column record) {
        Long defaultValue = record.getDefaultLong();
        if (defaultValue != null) {
            return " DEFAULT '" + defaultValue + "'";
        } else {
            return "";
        }
    }

    /**
     * 默认小数
     *
     * @param record the {@link Column} instance
     * @return DEFAULT '0.0'
     */
    protected String getDefaultBigDecimal(@NotNull Column record) {
        BigDecimal defaultValue = record.getDefaultBigDecimal();
        if (defaultValue != null) {
            return " DEFAULT '" + defaultValue + "'";
        } else {
            return "";
        }
    }

    /**
     * 默认当前时间
     *
     * @param record the {@link Column} instance
     * @return DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
     */
    protected String getDefaultCurrentDate(@NotNull Column record) {
        String defaultCurrentDateOnInsert = getDefaultCurrentDateOnInsert(record);
        String defaultCurrentDateOnUpdate = getDefaultCurrentDateOnUpdate(record);
        return defaultCurrentDateOnInsert + defaultCurrentDateOnUpdate;
    }

    /**
     * 新增记录，默认当前时间
     *
     * @param record the {@link Column} instance
     * @return DEFAULT CURRENT_TIMESTAMP
     */
    protected String getDefaultCurrentDateOnInsert(@NotNull Column record) {
        boolean defaultCurrentDateOnInsert = record.isDefaultCurrentDateOnInsert();
        if (defaultCurrentDateOnInsert) {
            return " DEFAULT CURRENT_TIMESTAMP";
        } else {
            return "";
        }
    }

    /**
     * 修改记录，默认当前时间
     *
     * @param record the {@link Column} instance
     * @return ON UPDATE CURRENT_TIMESTAMP
     */
    protected String getDefaultCurrentDateOnUpdate(@NotNull Column record) {
        boolean defaultCurrentDateOnUpdate = record.isDefaultCurrentDateOnUpdate();
        if (defaultCurrentDateOnUpdate) {
            return " ON UPDATE CURRENT_TIMESTAMP";
        } else {
            return "";
        }
    }

    /**
     * 自增
     *
     * @param record the {@link Column} instance
     * @return AUTO_INCREMENT
     */
    protected String getAutoIncrement(@NotNull Column record) {
        boolean autoIncrement = record.isAutoIncrement();
        if (autoIncrement) {
            return " AUTO_INCREMENT";
        } else {
            return "";
        }
    }

    /**
     * 备注
     *
     * @param record the {@link Column} instance
     * @return COMMENT ''
     */
    protected String getRemark(@NotNull Column record) {
        String remark = record.getRemark();
        if (remark != null) {
            return " COMMENT '" + remark + "'";
        } else {
            return "";
        }
    }

}
