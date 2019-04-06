package cn.ehi.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * @author 28476
 * 常用系统方法工具类
 */
public class SystemUtil {
    private static final Logger LOG = LoggerFactory.getLogger(SystemUtil.class);
    private static final String DEFALUT_PROPERTIE_PATH = "project.properties";

    /**
     * 获取后几天的日期
     *
     * @param days
     * @return date
     */
    public static String getDatesAfter(Integer days) {
        if (days != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
            Calendar calendar = new GregorianCalendar();
            Date date = new Date();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, days);
            date = calendar.getTime();
            return simpleDateFormat.format(date);
        } else {
            throw new RuntimeException("参数不合法");
        }
    }

    /**
     *
     * @param days
     * @param hour 一天中的时间(hour)
     * @return
     */
    public static Date getDateTimeAfter(Integer days, int hour) {
        if (days != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Calendar calendar = new GregorianCalendar();
            Date date = new Date();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, days);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            date = calendar.getTime();
            return date;
        } else {
            throw new RuntimeException("参数不合法");
        }
    }

    /**
     * 获取后几天的时间
     *
     * @param hours
     * @return time
     */
    public static String getTimesAfter(Integer hours) {
        if (hours == null || hours == 0)
            throw new RuntimeException("参数不合法");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, hours);
        calendar.set(Calendar.MINUTE, 0);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 传值格式  日期：04-05 时间：18:00
     *
     * @param date 时间1 例如：04-05 18:00
     * @param day  推延天数 例如 3天
     * @return 04-08 18:00
     */
    public static String putOffDateAndTime(String date, int day) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date d = df.parse(date);
            d = new Date(d.getTime() + day * 1000 * 60 * 60 * 24);
            return df.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 传值格式  日期：04-05 时间：18:00
     *
     * @param date1 时间1 例如：04-0518:00
     * @param date2 时间2
     * @return 1： dt1大于dt2 0：相等 -1：dt1小于dt2
     */
    public static int compareDateAndTime(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("MM-ddHH:mm");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                LOG.debug("dt1大于dt2");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                LOG.debug("dt1小于dt2");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 传值格式  日期：04-05
     *
     * @param date1 日期1 例如：04-05
     * @param date2 日期2
     * @return 1： dt1大于dt2 0：相等 -1：dt1小于dt2
     */
    public static int compareDate(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("MM-dd");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                LOG.debug("dt1:" + date1 + "大于dt2:" + date2);
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                LOG.debug("dt1:" + date1 + "小于dt2:" + date2);
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 删除字符串某一个值
     *
     * @param //删除前的字符串 ：distance
     * @param //删除的字符   ：remove
     * @return 返回删除后的字符串：string
     **/
    public static String removed(String distance, String remove) {
        String[] sourceStrArray = distance.split("");
//		String[] removed = ArrayUtil.removeFromArray(sourceStrArray, remove);
        String string = "";
        for (int i = 0; i < sourceStrArray.length; i++) {
            if (!remove.equals(sourceStrArray[i]))
                string = string + sourceStrArray[i];
        }
        return string;
    }

    /**
     * 获取字符长度
     */
    public static String getRandomNumberString(int count) {
        String randomSequence = "";
        for (int i = 0; i < count; i++) {
            Integer number = Integer.valueOf((new Random()).nextInt(10));
            number.toString();
            randomSequence = (new StringBuilder(String.valueOf(randomSequence))).append(number.toString()).toString();
        }
        return randomSequence;
    }

    /**
     * 获取文件大小
     */
    public static long getFileSize(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile())
            return file.getTotalSpace();
        else
            return -1L;
    }


    /**
     * 设置日期格式
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
        return df.format(new Date());
    }

    /**
     * 设置日期格式
     */
    public static String getCurrentDateTimeFormat() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        return df.format(new Date()).substring(5, 16).replace(" ", "");
    }


    /**
     * 暂时关闭
     * 抓取Logcat
     */
    public static void logcat(String className) {
        String logPath = System.getProperty("user.dir") + File.separator + "logcat" + File.separator;
        String fileName = (new StringBuilder("/mnt/sdcard/Appium/logcat/")).append(className)
                .append(getCurrentDateTime()).append(".txt").toString();
        String cmd = (new StringBuilder("adb logcat -d -f ")).append(fileName).toString();
//		LoggerUtils.debug(Util.class,"--------->test:"+cmd);
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
