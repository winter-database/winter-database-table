package io.github.winter.database.table;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sql Type mapping Value Type
 *
 * @author changebooks@qq.com
 */
public final class TypeMap {

    public static final Map<Integer, Class<?>> STANDARD_MAPPING = new ConcurrentHashMap<>(64);

    static {
        STANDARD_MAPPING.put(Types.CHAR, String.class);
        STANDARD_MAPPING.put(Types.NCHAR, String.class);
        STANDARD_MAPPING.put(Types.VARCHAR, String.class);
        STANDARD_MAPPING.put(Types.NVARCHAR, String.class);
        STANDARD_MAPPING.put(Types.VARBINARY, String.class);
        STANDARD_MAPPING.put(Types.LONGVARCHAR, String.class);
        STANDARD_MAPPING.put(Types.LONGNVARCHAR, String.class);
        STANDARD_MAPPING.put(Types.LONGVARBINARY, String.class);
        STANDARD_MAPPING.put(Types.BOOLEAN, Integer.class);
        STANDARD_MAPPING.put(Types.TINYINT, Integer.class);
        STANDARD_MAPPING.put(Types.SMALLINT, Integer.class);
        STANDARD_MAPPING.put(Types.INTEGER, Integer.class);
        STANDARD_MAPPING.put(Types.BIGINT, Long.class);
        STANDARD_MAPPING.put(Types.FLOAT, BigDecimal.class);
        STANDARD_MAPPING.put(Types.REAL, BigDecimal.class);
        STANDARD_MAPPING.put(Types.DOUBLE, BigDecimal.class);
        STANDARD_MAPPING.put(Types.NUMERIC, BigDecimal.class);
        STANDARD_MAPPING.put(Types.DECIMAL, BigDecimal.class);
        STANDARD_MAPPING.put(Types.DATE, Date.class);
        STANDARD_MAPPING.put(Types.TIME, Date.class);
        STANDARD_MAPPING.put(Types.TIME_WITH_TIMEZONE, Date.class);
        STANDARD_MAPPING.put(Types.TIMESTAMP, Date.class);
        STANDARD_MAPPING.put(Types.TIMESTAMP_WITH_TIMEZONE, Date.class);
        STANDARD_MAPPING.put(Types.NULL, Object.class);
        STANDARD_MAPPING.put(Types.JAVA_OBJECT, Object.class);
        STANDARD_MAPPING.put(Types.DATALINK, Object.class);
    }

    private TypeMap() {
    }

    public static Class<?> lookup(int sqlType) {
        return STANDARD_MAPPING.getOrDefault(sqlType, Object.class);
    }

}
