package com.panzyma.nm.auxiliar;
 
import java.util.Calendar;
import java.util.Date;

/**
 * 
 */
public class DateUtil {
    DateUtil() {    }
    
    public static int time2int(long time) {
        Date d = new Date(time);
        return d2i(d);
    }
    
    public static long getNow() {
        return dt2i(getCalendar().getTime());
    }
    
    public static int getToday() {
        return d2i(getCalendar().getTime());
    }
    
    public static Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        return cal;
    }
       
    public static Calendar getCalendar(int idate) {
        Calendar cal = Calendar.getInstance();
        String sdate = idate + "";
        String anio = sdate.substring(0, 4);
        String mes = sdate.substring(4, 6);
        mes = (Integer.parseInt(mes) - 1) + "";
        String dia = sdate.substring(6, 8);
        
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));
        cal.set(Calendar.MONTH, Integer.parseInt(mes));
        cal.set(Calendar.YEAR, Integer.parseInt(anio));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }
    
    public static Calendar getCalendar(long idate) {
        Calendar cal = Calendar.getInstance();
        String sdate = idate + "";
        String anio = sdate.substring(0, 4);
        String mes = sdate.substring(4, 6);
        mes = (Integer.parseInt(mes) - 1) + "";        
        String dia = sdate.substring(6, 8);
        String hora = sdate.substring(8, 10);
        String min = sdate.substring(10, 12);
        String seg = sdate.substring(12, 14);        
        
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));
        cal.set(Calendar.MONTH, Integer.parseInt(mes));
        cal.set(Calendar.YEAR, Integer.parseInt(anio));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hora));
        cal.set(Calendar.MINUTE, Integer.parseInt(min));
        cal.set(Calendar.SECOND, Integer.parseInt(seg));
        cal.set(Calendar.MILLISECOND, 0);        
        return cal;
    }
   
    public static long getTime() {
        return getCalendar().getTime().getTime();
    }
    
    public static long getTime(long idate) {
        return getCalendar(idate).getTime().getTime();
    }
    
    public static long getTime(int idate) {        
        return getCalendar(idate).getTime().getTime();
    }
    
    public static String idateToStr(long idate) {
        if (idate == 0) return "";        
        
        String strDate = idate + "";
        strDate = strDate.substring(0, 8);
        return strDate.substring(6, 8) + "/" + strDate.substring(4, 6) + "/" + strDate.substring(0, 4);
    }
    
    public static String idateToStr(int idate) {
        if (idate == 0) return "";
        
        String strDate = idate + "";
        return strDate.substring(6, 8) + "/" + strDate.substring(4, 6) + "/" + strDate.substring(0, 4);
    }
    
    public static String idateToStrYY(int idate) {
        if (idate == 0) return "";
        
        String strDate = idate + "";
        return strDate.substring(6, 8) + "/" + strDate.substring(4, 6) + "/" + strDate.substring(2, 4);
    }
    
    public static String idateToStrYY(long idate) {
        if (idate == 0) return "";
        
        String strDate = idate + "";
        return strDate.substring(6, 8) + "/" + strDate.substring(4, 6) + "/" + strDate.substring(2, 4);
    }
    
    public static int d2i(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int d = cal.get(Calendar.DAY_OF_MONTH);
        int m = cal.get(Calendar.MONTH) + 1; //Cuenta de 0 a 11
        int y = cal.get(Calendar.YEAR);
        String sd = d + "";
        if (d < 10) sd = "0" + sd;
        String sm = m + "";
        if (m < 10) sm = "0" + sm;
        String sy = y + "";
        String sdate = sy + sm + sd;
        return Integer.parseInt(sdate);
    }
    
    public static long dt2i(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int d = cal.get(Calendar.DAY_OF_MONTH);
        int m = cal.get(Calendar.MONTH) + 1; //Cuenta de 0 a 11
        int y = cal.get(Calendar.YEAR);
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int seg = cal.get(Calendar.SECOND);
        
        String sd = d + "";
        if (d < 10) sd = "0" + sd;
        String sm = m + "";
        if (m < 10) sm = "0" + sm;
        String sy = y + "";
        String sh = h + "";
        if (h < 10) sh = "0" + sh;
        String smin = min + "";
        if (min < 10) smin = "0" + smin;
        String sseg = seg + "";
        if (seg < 10) sseg = "0" + sseg;
        
        String sdate = sy + sm + sd + sh + smin + sseg;
        return Long.parseLong(sdate);
    }
    
    //Regresa la diferencia en minutos entre dos horas 
    //expresadas en milisegundos
    public static long diffMinutes(long startTime, long endTime) {
        long difMS = endTime - startTime;
        long diffMinutes = (long)Math.floor((difMS / 60000L)); //Un minuto = 60 * 1000 = 60000 milisecs
        return diffMinutes;
    }
    
    public static int diffDays(long startTime, long endTime) {
        return (int)(diffMinutes(startTime, endTime) / (1440)); //Un día = 24 * 60 = 1440 minutos
    }
    
    public static long addDays(long time, int days) {
        return time + days * 86400000; //Un día = 24 * 60 * 60 * 1000 = 86400000 milisegundos
    }
    
} 