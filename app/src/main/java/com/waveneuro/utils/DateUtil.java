package com.waveneuro.utils;

import java.lang.ref.SoftReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DateUtil {

    public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String PATTERN_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String PATTERN_ISO_DATE = "MMMM dd, yyyy";

    public static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    /**
     * Formats the given date according to the RFC 1123 pattern.
     *
     * @param date The date to format.
     * @return An RFC 1123 formatted date string.
     *
     * @see #PATTERN_RFC1123
     */
    public static String formatDate(final Date date) {
        return formatDate(date, PATTERN_RFC1123);
    }

    /**
     * Formats the given date according to the specified pattern.  The pattern
     * must conform to that used by the {@link SimpleDateFormat simple date
     * format} class.
     *
     * @param date The date to format.
     * @param pattern The pattern to use for formatting the date.
     * @return A formatted date string.
     *
     * @throws IllegalArgumentException If the given date pattern is invalid.
     *
     * @see SimpleDateFormat
     */
    public static String formatDate(final Date date, final String pattern) {
        final SimpleDateFormat formatter = DateFormatHolder.formatFor(pattern);
        return formatter.format(date);
    }

    public static String parseDate(final String date, String inputPattern, String outputPattern) {
        Date formattedDate = parseDate(date, inputPattern);
        return formatDate(formattedDate, outputPattern);
    }

    public static Date parseDate(final String date) {
        return parseDate(date, PATTERN_RFC1123);
    }

    public static Date parseDate(final String date, final String pattern) {
        final SimpleDateFormat formatter = DateFormatHolder.formatFor(pattern);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Clears thread-local variable containing {@link java.text.DateFormat} cache.
     *
     * @since 4.3
     */
    public static void clearThreadLocal() {
        DateFormatHolder.clearThreadLocal();
    }

    /** This class should not be instantiated. */
    private DateUtil() {
    }

    public static String getCurrentDate() {
        return formatDate(Calendar.getInstance().getTime(), PATTERN_ISO8601);
    }

    final static class DateFormatHolder {

        private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>
                THREADLOCAL_FORMATS = new ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>() {

            @Override
            protected SoftReference<Map<String, SimpleDateFormat>> initialValue() {
                return new SoftReference<>(
                        new HashMap<>());
            }

        };

        /**
         * creates a {@link SimpleDateFormat} for the requested format string.
         *
         * @param pattern
         *            a non-<code>null</code> format String according to
         *            {@link SimpleDateFormat}. The format is not checked against
         *            <code>null</code> since all paths go through
         *            {@link DateUtil}.
         * @return the requested format. This simple dateformat should not be used
         *         to {@link SimpleDateFormat#applyPattern(String) apply} to a
         *         different pattern.
         */
        public static SimpleDateFormat formatFor(final String pattern) {
            final SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
            Map<String, SimpleDateFormat> formats = ref.get();
            if (formats == null) {
                formats = new HashMap<>();
                THREADLOCAL_FORMATS.set(
                        new SoftReference<>(formats));
            }

            SimpleDateFormat format = formats.get(pattern);
            if (format == null) {
                format = new SimpleDateFormat(pattern);
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                formats.put(pattern, format);
            }

            return format;
        }

        public static void clearThreadLocal() {
            THREADLOCAL_FORMATS.remove();
        }

    }
}
