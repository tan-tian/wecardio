package com.borsam.repository.hibernate.cfg;

import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.internal.util.StringHelper;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-07-31 14:29
 * </pre>
 */
public class ExtImprovedNamingStrategy extends ImprovedNamingStrategy {
    /**
     * Return the unqualified class name, mixed case converted to
     * underscores
     */
    public String classToTableName(String className) {
        return addUnderscores(StringHelper.unqualify(className));
    }

    /**
     * Return the full property path with underscore seperators, mixed
     * case converted to underscores
     */
    public String propertyToColumnName(String propertyName) {
        return addUnderscores(StringHelper.unqualify(propertyName));
    }

    /**
     * Convert mixed case to underscores
     */
    public String tableName(String tableName) {
        return addUnderscores(tableName);
    }

    /**
     * Convert mixed case to underscores
     */
    public String columnName(String columnName) {
        return addUnderscores(columnName);
    }

    protected static String addUnderscores(String name) {
        if (name.indexOf("'") != -1 || name.indexOf("`") != -1) {
            return name;
        }

        StringBuilder buf = new StringBuilder(name.replace('.', '_'));
        for (int i = 1; i < buf.length() - 1; i++) {
            if (
                    Character.isLowerCase(buf.charAt(i - 1)) &&
                            Character.isUpperCase(buf.charAt(i)) &&
                            Character.isLowerCase(buf.charAt(i + 1))
                    ) {
                buf.insert(i++, '_');
            }
        }
        return buf.toString().toLowerCase();
    }
}
