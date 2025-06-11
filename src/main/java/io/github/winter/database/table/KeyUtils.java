package io.github.winter.database.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 索引描述
 *
 * @author changebooks@qq.com
 */
public final class KeyUtils {

    private KeyUtils() {
    }

    /**
     * Build SQL
     *
     * @param prefix PRIMARY | UNIQUE | ""
     * @param data   [ Key Name : [ Column Name ] ]
     * @return [ KEY SQL ]
     */
    public static List<String> build(String prefix, Map<String, List<String>> data) {
        if (data == null) {
            return null;
        }

        List<String> result = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : data.entrySet()) {
            if (entry == null) {
                continue;
            }

            String name = entry.getKey();
            if (name == null) {
                continue;
            }

            List<String> columnNames = entry.getValue();
            if (columnNames == null) {
                continue;
            }

            String sql = build(prefix, name, columnNames);
            if (sql == null) {
                continue;
            }

            result.add(sql);
        }

        return result;
    }

    /**
     * Build SQL
     *
     * @param prefix      PRIMARY | UNIQUE | ""
     * @param name        Key Name
     * @param columnNames [ Column Name ]
     * @return KEY SQL
     */
    public static String build(String prefix, String name, List<String> columnNames) {
        if (columnNames == null) {
            return null;
        }

        String joinedColumnNames = columnNames
                .stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(x -> !x.isEmpty())
                .collect(Collectors.joining(", "));
        if (joinedColumnNames.isEmpty()) {
            return "";
        }

        String cleanedPrefix = prefix == null ? "" : prefix.trim();
        String joinedPrefix = cleanedPrefix.isEmpty() ? "" : cleanedPrefix + " ";

        String cleanedName = name == null ? "" : name.trim();
        String joinedName = cleanedName.isEmpty() ? "" : cleanedName + " ";

        return joinedPrefix + "KEY " + joinedName + "(" + joinedColumnNames + ")";
    }

}
