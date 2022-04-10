package com.milkwang.util.common;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DateUtils {
    public static final DateTimeFormatter formatterDate = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static final DateTimeFormatter formatterDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter formatterNoSplitDateTime = DateTimeFormat.forPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter formatterDotDateTime = DateTimeFormat.forPattern("yyyy.MM.dd");
    public static final DateTimeFormatter formatterDateCN = DateTimeFormat.forPattern("yyyy年MM月dd日");
    public static final DateTimeFormatter formatterNoSecDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter formatterTimeNoSec = DateTimeFormat.forPattern("HH:mm");
    public static final DateTimeFormatter formatterTimeCN = DateTimeFormat.forPattern("HH时mm分ss秒");
    public static final DateTimeFormatter formatterNoSplitDate = DateTimeFormat.forPattern("yyyyMMdd");
    public static final DateTimeFormatter formatterDateNoYearCN = DateTimeFormat.forPattern("MM月dd日");
    public static final DateTimeFormatter formatterNoSplitDateNoYear = DateTimeFormat.forPattern("MMdd");
    public static final DateTimeFormatter formatterDateNoDay = DateTimeFormat.forPattern("yyyy-MM");
    public static final DateTimeFormatter formatterTime = DateTimeFormat.forPattern("HH:mm:ss");
    public static final DateTimeFormatter formatterDateNotYear = DateTimeFormat.forPattern("MM-dd");
    public static final DateTimeFormatter formatterNoBigYearDate = DateTimeFormat.forPattern("yyMMdd");


    /**
     * 获取指定时间段内的随机时间, 如果开始时间大于结束时间，则返回null
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 随机时间
     */
    public static Date randomDate(Date beginTime, Date endTime) {
        if (beginTime.after(endTime)) {
            return null;
        }
        int interval = intervalSecond(beginTime, endTime);
        if (interval == 0) {
            return beginTime;
        }
        int randomSecond = MathUtils.random(0D, interval + 0D).intValue();
        return afterSeconds(beginTime, randomSecond);
    }
    /**
     * 按照yyyy-MM-dd 格式输出日期文本
     *
     * @param date 日期
     * @return 文本格式
     */
    public static String parseDateToString(Date date) {
        return formatterDate.print(new DateTime(date));
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss 格式输出日期文本
     *
     * @param date 日期
     * @return 格式化文本
     */
    public static String parseDateTimeToString(Date date) {
        return formatterDateTime.print(new DateTime(date));
    }

    /**
     * 格式化输出时间格式
     *
     * @param date      日期
     * @param formatter 可选值为DateUtils.formatter*
     * @return 格式化时间文本
     */
    public static String format(Date date, DateTimeFormatter formatter) {
        return formatter.print(new DateTime(date));
    }

    /**
     * 格式化输出时间格式
     *
     * @param date      日期时间
     * @param formatter 可选值为DateUtils.formatter*
     * @return 格式化时间文本
     */
    public static String format(DateTime date, DateTimeFormatter formatter) {
        return formatter.print(date);
    }

    /**
     * 将字符串转换为日期类型，格式为：yyyy-MM-dd
     *
     * @param format 时间字符串
     * @return 日期
     */
    public static Date parseStringToDate(String format) {
        return formatterDate.parseDateTime(format).toDate();
    }

    /**
     * 将字符串转换为日期类型，格式为：yyyy-MM-dd HH:mm:ss
     *
     * @param format 时间字符串
     * @return 日期
     */
    public static Date parseStringToDateTime(String format) {
        return formatterDateTime.parseDateTime(format).toDate();
    }

    /**
     * 计算今天到指定日期差了多少周(7天), 按照7天进位，向下取整，不满会取0。如果是过去的时间，会返回负数
     *
     * @param date 日期
     * @return 差了多少周
     */
    public static int intervalWeek(Date date) {
        return Weeks.weeksBetween(new DateTime(), new DateTime(date)).getWeeks();
    }

    /**
     * 计算两个事件的星期(7天)差，按照7天进位，向下取整，不满会取0。如果第一个时间大于第二个时间，会返回负数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 星期差
     */
    public static int intervalWeek(Date date1, Date date2) {
        return Weeks.weeksBetween(new DateTime(date1), new DateTime(date2)).getWeeks();
    }

    /**
     * 计算当前时间与指定时间差了多少天，会计算为绝对值<br/>
     * 按照24小时计算，向下取整。例如两个时间差了23小时，则返回值为0，差了24.1小时，则返回值为1
     *
     * @param date 指定时间
     * @return 天数差
     */
    public static int intervalDayAbs(Date date) {
        return Math.abs(Days.daysBetween(new DateTime(date), new DateTime().withTimeAtStartOfDay()).getDays());
    }

    /**
     * 计算两个时间的日期差，差了多少天。会计算为绝对值<br/>
     * 按照24小时计算，向下取整。例如两个时间差了23小时，则返回值为0，差了24.1小时，则返回值为1
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 天数差
     */
    public static int intervalDayAbs(Date date1, Date date2) {
        return Math.abs(Days.daysBetween(new DateTime(date1), new DateTime(date2)).getDays());
    }

    /**
     * 计算两个时间的日期差，差了多少天。如果第一个时间大于第二个时间，会返回负数<br/>
     * 按照24小时计算，向下取整。例如两个时间差了23小时，则返回值为0，差了24.1小时，则返回值为1
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 天数差
     */
    public static int intervalDay(Date date1, Date date2) {
        return Days.daysBetween(new DateTime(date1), new DateTime(date2)).getDays();
    }

    /**
     * 计算当前时间与指定时间差了多少分钟，如果是过去的时间，会返回负数<br/>
     * 按照60秒计算，向下取整。
     *
     * @param date 指定时间
     * @return 差了多少分钟
     */
    public static int intervalMinute(Date date) {
        return Minutes.minutesBetween(new DateTime(), new DateTime(date)).getMinutes();
    }

    /**
     * 计算两个时间的分钟差，如果第一个时间大于第二个时间，会返回负数<br/>
     * 按照60秒计算，向下取整。
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 差了多少分钟
     */
    public static int intervalMinute(Date date1, Date date2) {
        return Minutes.minutesBetween(new DateTime(date1), new DateTime(date2)).getMinutes();
    }

    /**
     * 计算当前时间与指定时间差了多少秒，如果是过去的时间，会返回负数<br/>
     *
     * @param date 指定时间
     * @return 差了多少秒
     */
    public static int intervalSecond(Date date) {
        return Seconds.secondsBetween(new DateTime(), new DateTime(date)).getSeconds();
    }

    /**
     * 计算两个时间差了多少秒，如果第一个时间大于第二个时间，会返回负数<br/>
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 差了多少秒
     */
    public static int intervalSecond(Date date1, Date date2) {
        return Seconds.secondsBetween(new DateTime(date1), new DateTime(date2)).getSeconds();
    }

    /**
     * 当前时间的Timestamp，单位为秒
     *
     * @return 当前时间的Timestamp
     */
    public static long currentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 返回将指定时间向后推x秒钟的时间
     *
     * @param date      指定时间
     * @param increment x秒钟
     * @return 日期类型
     */
    public static Date afterSeconds(Date date, int increment) {
        return new DateTime(date).plusSeconds(increment).toDate();
    }

    /**
     * 返回将指定时间向后推x分钟的时间
     *
     * @param date      指定时间
     * @param increment x分钟
     * @return 日期类型
     */
    public static Date afterMinutes(Date date, int increment) {
        return new DateTime(date).plusMinutes(increment).toDate();
    }

    /**
     * 返回将指定时间向后推x小时的时间
     *
     * @param date      指定时间
     * @param increment x小时
     * @return 日期类型
     */
    public static Date afterHours(Date date, int increment) {
        return new DateTime(date).plusHours(increment).toDate();
    }

    /**
     * 返回将指定时间向后推x天的时间
     *
     * @param date      指定时间
     * @param increment x天
     * @return 日期类型
     */
    public static Date afterDays(Date date, int increment) {
        return new DateTime(date).plusDays(increment).toDate();
    }

    /**
     * 返回将指定时间向后推x月的时间
     *
     * @param date      指定时间
     * @param increment x月
     * @return 日期类型
     */
    public static Date afterMonths(Date date, int increment) {
        return new DateTime(date).plusMonths(increment).toDate();
    }

    /**
     * 返回将指定时间向后推x年的时间
     *
     * @param date      指定时间
     * @param increment x年
     * @return 日期类型
     */
    public static Date afterYears(Date date, int increment) {
        return new DateTime(date).plusYears(increment).toDate();
    }

    /**
     * 返回将指定时间向前推x秒钟的时间
     *
     * @param date      指定时间
     * @param increment x秒钟
     * @return 日期类型
     */
    public static Date beforeSeconds(Date date, int increment) {
        return new DateTime(date).minusSeconds(increment).toDate();
    }

    /**
     * 返回将指定时间向前推x分钟的时间
     *
     * @param date      指定时间
     * @param increment x分钟
     * @return 日期类型
     */
    public static Date beforeMinutes(Date date, int increment) {
        return new DateTime(date).minusMinutes(increment).toDate();
    }

    /**
     * 返回将指定时间向前推x小时的时间
     *
     * @param date      指定时间
     * @param increment x小时
     * @return 日期类型
     */
    public static Date beforeHours(Date date, int increment) {
        return new DateTime(date).minusHours(increment).toDate();
    }

    /**
     * 返回将指定时间向前推x天的时间
     *
     * @param date      指定时间
     * @param increment x天
     * @return 日期类型
     */
    public static Date beforeDays(Date date, int increment) {
        return new DateTime(date).minusDays(increment).toDate();
    }

    /**
     * 返回将指定时间向前推x月的时间
     *
     * @param date      指定时间
     * @param increment x月
     * @return 日期类型
     */
    public static Date beforeMonths(Date date, int increment) {
        return new DateTime(date).minusMonths(increment).toDate();
    }

    /**
     * 返回将指定时间向前推x年的时间
     *
     * @param date      指定时间
     * @param increment x年
     * @return 日期类型
     */
    public static Date beforeYears(Date date, int increment) {
        return new DateTime(date).minusYears(increment).toDate();
    }

    /**
     * 判断两个时间是不是同一天
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 是否同一天
     */
    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    /**
     * 判断指定日期是不是今天
     *
     * @param date 指定日期
     * @return 是否今天2020-07-22 12:33:25
     */
    public static boolean isToday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String cmpDate = sdf.format(date).substring(0, 10);
        String today = sdf.format(new Date()).substring(0, 10);
        return today.equals(cmpDate);
    }

    /**
     * 总日期内解析出年份, 从1开始
     *
     * @param date 日期
     * @return 年份
     */
    public static Integer getYear(Date date) {
        return new DateTime(date).getYear();
    }

    /**
     * 从日期中解析出月份, 从1开始
     *
     * @param date 日期
     * @return 月份
     */
    public static Integer getMonth(Date date) {
        return new DateTime(date).getMonthOfYear();
    }

    /**
     * 从日期中解析出天，当月第几天, 从1开始
     *
     * @param date 日期
     * @return 天，当月第几天
     */
    public static Integer getDay(Date date) {
        return new DateTime(date).getDayOfMonth();
    }

    /**
     * 获取指定日期下一个星期一
     *
     * @param gmtCreate 指定日期
     * @return 下一个星期一的日期
     */
    public static Date getNextMonday(Date gmtCreate) {
        return new DateTime(gmtCreate.getTime()).plusWeeks(1).withDayOfWeek(DateTimeConstants.MONDAY).toDate();
    }

    /**
     * 获取指定日期当周星期一的日期
     *
     * @param gmtCreate 指定日期
     * @return 指定日期当周星期一的日期
     */
    public static Date getThisMonday(Date gmtCreate) {
        return new DateTime(gmtCreate).withDayOfWeek(DayOfWeek.MONDAY.getValue()).toDate();
    }

    /**
     * 判断指定日期是否该年月
     *
     * @param date  指定日期
     * @param year  年份
     * @param month 月份
     * @return 是否该年月
     */
    public static Boolean isSameYearMonth(Date date, Integer year, Integer month) {
        DateTime dateTime = new DateTime(date);
        Boolean samYear = Objects.equals(dateTime.getYear(), year);
        Boolean sameMonth = Objects.equals(dateTime.getMonthOfYear(), month);
        return samYear && sameMonth;
    }

    /**
     * 获取指定时间年份的最后一秒
     *
     * @param date 指定时间
     * @return 指定时间当年的最后一秒
     */
    public static Date endOfYear(Date date) {
        return new DateTime(date).monthOfYear().withMaximumValue().dayOfMonth().withMaximumValue().secondOfDay().withMaximumValue().toDate();
    }

    /**
     * 获取指定时间的第一个月的第一秒
     *
     * @param date 指定时间
     * @return 指定时间当年的第一秒
     */
    public static Date startOfYear(Date date) {
        return new DateTime(date).monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue().withTimeAtStartOfDay().toDate();
    }

    /**
     * 获取指定时间月份的最后一秒
     *
     * @param date 指定时间
     * @return 指定时间当月的最后一秒
     */
    public static Date endOfMonth(Date date) {
        return new DateTime(date).dayOfMonth().withMaximumValue().secondOfDay().withMaximumValue().toDate();
    }

    /**
     * 获取今年指定月份的第一天
     *
     * @param date 指定时间
     * @return 今年指定月份的第一天
     */
    public static Date startOfMonth(Date date) {
        return new DateTime(date).dayOfMonth().withMinimumValue().withTimeAtStartOfDay().toDate();
    }

    /**
     * 获取指定日期最后一秒
     *
     * @param date 日期
     * @return 指定日期最后一秒的时间
     */
    public static Date endOfDay(Date date) {
        return new DateTime(date).secondOfDay().withMaximumValue().toDate();
    }

    /**
     * 返回指定时间的一个副本，获取指定时间当天的起始时间点
     *
     * @param date 日期
     * @return 指定日期第一秒的时间
     */
    public static Date startOfDay(Date date) {
        return new DateTime(date).withTimeAtStartOfDay().toDate();
    }

    /**
     * 返回指定时间的一个副本，获取指定时间那个小时的最后一秒
     *
     * @param date 指定时间
     * @return 指定时间那个小时的最后一秒
     */
    public static Date endOfHour(Date date) {
        return new DateTime(date).minuteOfHour().withMaximumValue().secondOfMinute().withMaximumValue().toDate();
    }

    /**
     * 返回指定时间的一个副本，获取指定时间小时的起始时间点
     *
     * @param date 指定时间
     * @return 将分钟及更小单位设置为0后的副本
     */
    public static Date startOfHour(Date date) {
        DateTime dateTime = new DateTime(date);
        int hour = dateTime.hourOfDay().get();
        return dateTime.withTime(hour, 0, 0, 0).toDate();
    }

    /**
     * 获取指定时间的一个副本，获取指定时间分钟的最后一秒
     *
     * @param date 指定时间
     * @return 指定时间分钟的最后一秒
     */
    public static Date endOfMinute(Date date) {
        return new DateTime(date).secondOfMinute().withMaximumValue().toDate();
    }

    /**
     * 返回指定时间的一个副本，获取指定时间分钟的起始时间点
     *
     * @param date 指定时间
     * @return 将秒及更小单位设置为0后的副本
     */
    public static Date startOfMinute(Date date) {
        return new DateTime(date).secondOfMinute().withMinimumValue().toDate();
    }

    /**
     * 获取指定日期的指定时间
     *
     * @param hour   小时
     * @param minute 分钟
     * @param second 秒
     * @param offset 当前日期+ -
     * @return 指定时间
     */
    public static Date getAppointTime(int hour, int minute, int second, int offset) {
        return new DateTime().withTime(hour, minute, second, 0).plusDays(offset).toDate();
    }
}
