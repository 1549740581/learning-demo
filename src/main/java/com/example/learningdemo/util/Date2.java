package com.example.learningdemo.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import org.springframework.lang.Nullable;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Date2 {

    public static final LocalDateTime EPOCH_DATE_TIME = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static LocalDate localDate(@Nullable java.sql.Date date) {
        return date == null ? null : date.toLocalDate();
    }

    public static LocalDateTime localDateTime(@Nullable Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    public static LocalTime localTime(@Nullable java.sql.Time time) {
        return time == null ? null : time.toLocalTime();
    }

    public static LocalDateTime localDateTime(@Nullable Date date) {
        return date == null ? null : new Timestamp(date.getTime()).toLocalDateTime();
    }

    public static LocalDate localDate(@Nullable Date date) {
        return date == null ? null : new java.sql.Date(date.getTime()).toLocalDate();
    }

    public static Date date(@Nullable LocalDateTime localDateTime) {
        return localDateTime == null ? null : Timestamp.valueOf(localDateTime);
    }

    public static Date date(@Nullable LocalDate localDate) {
        return date(Date2.startOfDay(localDate));
    }

    public static LocalDate parseLocalDate(@Nullable String localDate) {
        return localDate == null ? null : LocalDate.parse(localDate, DATE_FORMATTER);
    }

    public static LocalTime parseLocalTime(@Nullable String localTime) {
        return localTime == null ? null : LocalTime.parse(localTime, TIME_FORMATTER);
    }

    public static LocalDateTime parseLocalDateTime(@Nullable String localDateTime) {
        return localDateTime == null ? null : LocalDateTime.parse(localDateTime, DATE_TIME_FORMATTER);
    }

    public static String format(@Nullable LocalDate localDate) {
        return localDate == null ? null : localDate.format(DATE_FORMATTER);
    }

    public static String formatMonth(@Nullable LocalDate localDate) {
        return localDate == null ? null : localDate.format(MONTH_FORMATTER);
    }

    public static String formatMonth(@Nullable LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.format(MONTH_FORMATTER);
    }

    public static String format(@Nullable LocalTime localTime) {
        return localTime == null ? null : localTime.format(TIME_FORMATTER);
    }

    public static String format(@Nullable LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.format(DATE_TIME_FORMATTER);
    }

    public static List<LocalDate> listDays(@Nonnull LocalDate start, @Nonnull LocalDate end) {
        return listDays(start, end, true);
    }

    public static List<LocalDate> listDays(@Nonnull LocalDate start, @Nonnull LocalDate end, boolean includeEnd) {
        return listDays(start, end, includeEnd, Collections.emptySet());
    }

    public static List<LocalDate> listDays(@Nonnull LocalDate start, @Nonnull LocalDate end, Set<LocalDate> filterDays) {
        return listDays(start, end, true, filterDays);
    }

    public static List<LocalDate> listDays(@Nonnull LocalDate start, @Nonnull LocalDate end, boolean includeEnd, Set<LocalDate> filterDays) {
        List<LocalDate> localDateList = Lists.newArrayList();
        if (includeEnd) {
            end = end.plusDays(1);
        }
        while (start.isBefore(end)) {
            if (!filterDays.contains(start)) {
                localDateList.add(start);
            }
            start = start.plusDays(1);
        }
        return localDateList;
    }

    public static LocalDate min(@Nonnull LocalDate localDate1, @Nonnull LocalDate localDate2) {
        return localDate1.isBefore(localDate2) ? localDate1 : localDate2;
    }

    public static LocalTime min(@Nonnull LocalTime localTime1, @Nonnull LocalTime localTime2) {
        return localTime1.isBefore(localTime2) ? localTime1 : localTime2;
    }

    public static LocalDateTime min(@Nonnull LocalDateTime localDateTime1, @Nonnull LocalDateTime localDateTime2) {
        return localDateTime1.isBefore(localDateTime2) ? localDateTime1 : localDateTime2;
    }

    public static LocalDate max(@Nonnull LocalDate localDate1, @Nonnull LocalDate localDate2) {
        return localDate1.isAfter(localDate2) ? localDate1 : localDate2;
    }

    public static LocalTime max(@Nonnull LocalTime localTime1, @Nonnull LocalTime localTime2) {
        return localTime1.isAfter(localTime2) ? localTime1 : localTime2;
    }

    public static LocalDateTime max(@Nonnull LocalDateTime localDateTime1, @Nonnull LocalDateTime localDateTime2) {
        return localDateTime1.isAfter(localDateTime2) ? localDateTime1 : localDateTime2;
    }

    public static boolean isSameYearAndMonth(LocalDate localDate1, LocalDate localDate2) {
        return localDate1.getYear() == localDate2.getYear() && localDate1.getMonth().equals(localDate2.getMonth());
    }

    public static boolean isDouble11(@Nonnull LocalDate localDate) {
        return localDate.getMonthValue() == 11 && localDate.getDayOfMonth() == 11;
    }

    public static boolean isEpoch(LocalDateTime localDateTime) {
        return localDateTime.toEpochSecond(ZoneOffset.UTC) == 0;
    }

    public static LocalDateTime startOfDay(@Nullable LocalDate day) {
        return Optional.ofNullable(day).map(LocalDate::atStartOfDay).orElse(null);
    }

    public static LocalDateTime endOfDay(@Nullable LocalDate day) {
        return Optional.ofNullable(day).map(date -> date.atTime(23, 59, 59)).orElse(null);
    }

    public static long toEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long dayDuration(@Nonnull LocalDate start, @Nonnull LocalDate end) {
        return start.until(end, ChronoUnit.DAYS);
    }

    public static long dayDuration(@Nonnull LocalDateTime start, @Nonnull LocalDateTime end) {
        return start.until(end, ChronoUnit.DAYS);
    }

    public static long hourDuration(@Nonnull LocalDateTime start, @Nonnull LocalDateTime end) {
        return start.until(end, ChronoUnit.HOURS);
    }

    public static long minuteDuration(@Nonnull LocalDateTime start, @Nonnull LocalDateTime end) {
        return start.until(end, ChronoUnit.MINUTES);
    }

    public static long secondDuration(@Nonnull LocalDateTime start, @Nonnull LocalDateTime end) {
        return start.until(end, ChronoUnit.SECONDS);
    }

    public static long toEpochSecond(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    public static LocalDate yesterday() {
        return LocalDate.now().minusDays(1);
    }

    public static LocalDate tomorrow() {
        return LocalDate.now().plusDays(1);
    }

    /**
     * @param date1
     * @param date2
     * @return 返回date1 > date2
     */
    public static boolean gt(@Nonnull LocalDateTime date1, @Nonnull LocalDateTime date2) {
        return date1.isAfter(date2);
    }

    /**
     * @param date1
     * @param date2
     * @return 返回date1 >= date2
     */
    public static boolean gte(@Nonnull LocalDateTime date1, @Nonnull LocalDateTime date2) {
        return !date1.isBefore(date2);
    }

    /**
     * @param date1
     * @param date2
     * @return 返回date1 < date2
     */
    public static boolean lt(@Nonnull LocalDateTime date1, @Nonnull LocalDateTime date2) {
        return !gte(date1, date2);
    }

    /**
     * @param date1
     * @param date2
     * @return 返回date1 <= date2
     */
    public static boolean lte(@Nonnull LocalDateTime date1, @Nonnull LocalDateTime date2) {
        return !gt(date1, date2);
    }

    /**
     * @param date1
     * @param date2
     * @return 返回date1 > date2
     */
    public static boolean gt(@Nonnull LocalDate date1, @Nonnull LocalDate date2) {
        return date1.isAfter(date2);
    }

    /**
     * @param date1
     * @param date2
     * @return 返回date1 >= date2
     */
    public static boolean gte(@Nonnull LocalDate date1, @Nonnull LocalDate date2) {
        return !date1.isBefore(date2);
    }

    /**
     * @param date1
     * @param date2
     * @return 返回date1 < date2
     */
    public static boolean lt(@Nonnull LocalDate date1, @Nonnull LocalDate date2) {
        return !gte(date1, date2);
    }

    /**
     * @param date1
     * @param date2
     * @return 返回date1 <= date2
     */
    public static boolean lte(@Nonnull LocalDate date1, @Nonnull LocalDate date2) {
        return !gt(date1, date2);
    }

    /**
     * @param date1
     * @param date2
     * @return 返回date1 > date2
     */
    public static boolean gt(@Nonnull LocalTime date1, @Nonnull LocalTime date2) {
        return date1.isAfter(date2);
    }

    /**
     * @param date1
     * @param date2
     * @return 返回date1 >= date2
     */
    public static boolean gte(@Nonnull LocalTime date1, @Nonnull LocalTime date2) {
        return !date1.isBefore(date2);
    }

    /**
     * @param date1
     * @param date2
     * @return 返回date1 < date2
     */
    public static boolean lt(@Nonnull LocalTime date1, @Nonnull LocalTime date2) {
        return !gte(date1, date2);
    }

    /**
     * @param date1
     * @param date2
     * @return 返回date1 <= date2
     */
    public static boolean lte(@Nonnull LocalTime date1, @Nonnull LocalTime date2) {
        return !gt(date1, date2);
    }

    public static int roundMinutesBetween(long startSecond, long endSecond) {
        return roundToMinutes(Ints.checkedCast(endSecond - startSecond));
    }

    public static int roundToMinutes(int seconds) {
        int minutes = seconds / 60;
        int remainSeconds = seconds % 60;
        return minutes + remainSeconds / 30;
    }

    public static long milliseconds(LocalDateTime time) {
        return time.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static LocalDate endOfMonth(@Nonnull LocalDate date) {
        return startOfMonth(date).plusMonths(1).minusDays(1);
    }

    public static LocalDate startOfMonth(@Nonnull LocalDate date) {
        return date.withDayOfMonth(1);
    }

}
