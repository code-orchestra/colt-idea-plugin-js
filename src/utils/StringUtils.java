package utils;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author Alexander Eliseyev
 */
public class StringUtils {

    private static String outputEncoding;

    public static abstract class ILeftCombinator {
        public ILeftCombinator() {
        }

        public abstract String combine(String s, String t);

        public String invoke(String s, String t) {
            return combine(s, t);
        }
    }

    public static String safe(String str) {
        return str == null ? "" : str;
    }

    public static final String EMPTY = "";

    public static String generateId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static String generateId(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }

    public static String generateIdNumeric(int length) {
        String id = UUID.randomUUID().toString().substring(0, length);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < id.length(); i++) {
            String s = Integer.valueOf((int) id.charAt(i)).toString();
            sb.append(s.charAt(s.length() - 1));
        }

        return sb.toString();
    }

    public static boolean equals(String object1, String object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }
        return object1.equals(object2);
    }

    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuffer(str).reverse().toString();
    }

    public static int lastIndexOf(String str, String searchChar) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.lastIndexOf(searchChar);
    }

    public static int countMatches(String str, String sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    public static String foldLeft(Iterable<String> seq, String seed, ILeftCombinator combinator) {
        String s = seed;
        for (Iterator<String> it = seq.iterator(); it.hasNext(); ) {
            s = combinator.invoke(s, it.next());
        }
        return s;
    }

    /**
     * Equivalent to testee.startsWith(firstPrefix + secondPrefix) but avoids
     * creating an object for concatenation.
     *
     * @param testee
     * @param firstPrefix
     * @param secondPrefix
     * @return
     */
    public static boolean startsWithConcatenationOf(String testee, String firstPrefix, String secondPrefix) {
        int l1 = firstPrefix.length();
        int l2 = secondPrefix.length();
        if (testee.length() < l1 + l2)
            return false;
        return testee.startsWith(firstPrefix) && testee.regionMatches(l1, secondPrefix, 0, l2);
    }

    public static boolean isEmpty(String command) {
        return command == null || command.trim().isEmpty();
    }

    public static String join(final List<String> strings, final String separator) {
        if (strings == null) {
            return "";
        }
        return join(strings.toArray(new String[strings.size()]), separator);
    }

    public static String join(final String[] strings, final String separator) {
        return join(strings, 0, strings.length, separator);
    }

    public static String join(final String[] strings, int startIndex, int endIndex, final String separator) {
        final StringBuilder result = new StringBuilder();
        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex)
                result.append(separator);
            result.append(strings[i]);
        }
        return result.toString();
    }

    public static boolean isNotEmpty(String str) {
        return str != null && str.trim().length() > 0;
    }

}
