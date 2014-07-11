package greed.util;

import java.util.Collection;

/**
 * Greed is good! Cheers!
 */
public class StringUtil {
    public static String join(String[] strs, String sep) {
        StringBuilder bud = new StringBuilder();
        for (String s: strs) {
            if (bud.length() > 0)
                bud.append(sep);
            bud.append(s);
        }
        return bud.toString();
    }

    public static String join(Collection<String> strs, String sep) {
        StringBuilder bud = new StringBuilder();
        for (String s: strs) {
            if (bud.length() > 0)
                bud.append(sep);
            bud.append(s);
        }
        return bud.toString();
    }

    public static String join(Object[] arrays, String sep) {
        int n = arrays.length;
        String[] strs = new String[n];
        for(int i = 0; i < n; ++ i) strs[i] = arrays[i].toString();
        return join(strs, sep);
    }
}
