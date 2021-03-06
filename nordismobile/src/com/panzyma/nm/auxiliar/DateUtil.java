package com.panzyma.nm.auxiliar;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 */
public class DateUtil {
	DateUtil() {
	}
	
	public static long getLastDayOfMonth(long date) {
		String dia = "";
		String mes = "";
		String anio = "";
		String fechaVencimiento = String.valueOf(date);
		anio = fechaVencimiento.substring(0, 4);
		mes  = fechaVencimiento.substring(4, 6);
		mes  = String.valueOf(Integer.parseInt(mes)+1);
		if(mes.length()==1) mes = "0"+mes;
		dia  = "01";
		long fecha = Long.valueOf(anio.concat(mes).concat(dia));
		fecha = addDay(fecha, -1);
		return fecha;
	}

	public static int time2int(long time) {
		Date d = new Date(time);
		return d2i(d);
	}
	
	public static long strDateToLong(String fecha) {

		String[] part = fecha.split("/");
		String anio = part[2];
		String mes = part[1];		
		String dia = part[0];

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));
		cal.set(Calendar.MONTH, ( Integer.parseInt(mes) - 1) );
		cal.set(Calendar.YEAR, Integer.parseInt(anio));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return d2i(cal.getTime());
	}

	public static long strTimeToLong(String fecha) {

		String[] part = fecha.split("/");
		String anio = "20" + part[2];
		String mes = part[1];
		mes = (Integer.parseInt(mes) - 1) + "";
		String dia = part[0];

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));
		cal.set(Calendar.MONTH, Integer.parseInt(mes));
		cal.set(Calendar.YEAR, Integer.parseInt(anio));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return d2i(cal.getTime());
	}

	public static int strTimeToInt(String fecha) {

		String[] part = fecha.split("/");
		String anio = "20" + part[2];
		String mes = part[1];
		mes = (Integer.parseInt(mes) - 1) + "";
		String dia = part[1];

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));
		cal.set(Calendar.MONTH, Integer.parseInt(mes));
		cal.set(Calendar.YEAR, Integer.parseInt(anio));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return d2i(cal.getTime());
	}
	
	public static int strToIntTime(String fecha) {

		String[] part = fecha.split("/");
		String anio = part[2];
		String mes = part[1];
		mes = (Integer.parseInt(mes)) + "";
		String dia = part[0];

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));
		cal.set(Calendar.MONTH, (Integer.parseInt(mes)-1));
		cal.set(Calendar.YEAR, Integer.parseInt(anio));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return d2i(cal.getTime());
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
	
	public static Calendar getCalendar2(long idate) {
		Calendar cal = Calendar.getInstance();
		String sdate = idate + "";
		String anio = sdate.substring(0, 4);
		String mes = sdate.substring(4, 6);
		mes = (Integer.parseInt(mes) - 1) + "";
		String dia = sdate.substring(6, 8); 

		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));
		cal.set(Calendar.MONTH, Integer.parseInt(mes));
		cal.set(Calendar.YEAR, Integer.parseInt(anio)); 
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	public static long getTime() {
		return getCalendar().getTime().getTime();
	}

	public static long getFecha() {
		Calendar fecha = Calendar.getInstance();
		int year = fecha.get(Calendar.YEAR);
		int month = fecha.get(Calendar.MONTH) + 1;
		String strMonth = ((month < 10) ? "0" : "") + month;
		int day = fecha.get(Calendar.DAY_OF_MONTH);
		String strDay = ((day < 10) ? "0" : "") + day;
		long _fecha = Long.parseLong(year + "" + strMonth + "" + strDay);
		return _fecha;
	}

	public static long getTime(long idate) {
		return getCalendar(idate).getTime().getTime();
	}

	public static long getTime(int idate) {
		return getCalendar(idate).getTime().getTime();
	}

	public static String idateToStr(long idate) {
		if (idate == 0)
			return "";

		String strDate = idate + "";
		strDate = strDate.substring(0, 8);
		return strDate.substring(6, 8) + "/" + strDate.substring(4, 6) + "/"
				+ strDate.substring(0, 4);
	}

	public static String idateToStr(int idate) {
		if (idate == 0)
			return "";

		String strDate = idate + "";
		return strDate.substring(6, 8) + "/" + strDate.substring(4, 6) + "/"
				+ strDate.substring(0, 4);
	}

	public static String idateToStrYY(int idate) {
		if (idate == 0)
			return "";
		String strDate = idate + "";
		if (strDate.length() == 6)
			strDate = "20" + strDate;
		return strDate.substring(6, 8) + "/" + strDate.substring(4, 6) + "/"
				+ strDate.substring(2, 4);
	}

	public static String idateToStrYY(long idate) {
		if (idate == 0)
			return "";

		String strDate = idate + "";
		return strDate.substring(6, 8) + "/" + strDate.substring(4, 6) + "/"
				+ strDate.substring(2, 4);
	}

	public static String idateToStrYYYY(long idate) {
		if (idate == 0)
			return "";

		String strDate = idate + "";
		return strDate.substring(6, 8) + "/" + strDate.substring(4, 6) + "/"
				+ strDate.substring(0, 4);
	}
	
	public static int d2i(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int d = cal.get(Calendar.DAY_OF_MONTH);
		int m = cal.get(Calendar.MONTH) + 1; // Cuenta de 0 a 11
		int y = cal.get(Calendar.YEAR);
		String sd = d + "";
		if (d < 10)
			sd = "0" + sd;
		String sm = m + "";
		if (m < 10)
			sm = "0" + sm;
		String sy = y + "";
		String sdate = sy + sm + sd;
		return Integer.parseInt(sdate);
	}
	
	public static long dt2iFC(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int d = cal.get(Calendar.DAY_OF_MONTH);
		int m = cal.get(Calendar.MONTH) + 1; // Cuenta de 0 a 11
		int y = cal.get(Calendar.YEAR);
		int h = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int seg = cal.get(Calendar.SECOND);

		String sd = d + "";
		if (d < 10)
			sd = "0" + sd;
		String sm = m + "";
		if (m < 10)
			sm = "0" + sm;
		String sy = y + "";
		

		String sdate = sy + sm + sd;
		return Long.parseLong(sdate);
	}

	public static Date getFirstDateOfCurrentMonth()
	{
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(cal.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	
	public static Date getLastDateOfCurrentMonth()
	{
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(cal.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	
	public static boolean isValidExpiredDate(Date fechavencimiento,
			boolean esdevencido) {
		Calendar today=Calendar.getInstance();
		today.setTime(new Date());
		
		Calendar fechavence=Calendar.getInstance();
		fechavence.setTime(fechavencimiento);
		 
		if(esdevencido)
		{
			if(fechavence.get(Calendar.MONTH)==today.get(Calendar.MONTH) 
					&& fechavence.get(Calendar.YEAR)==today.get(Calendar.YEAR))
				return true;	 

			today.add(Calendar.MONTH, 1);
	        
			if(fechavence.get(Calendar.MONTH)==today.get(Calendar.MONTH)
					&& fechavence.get(Calendar.YEAR)==today.get(Calendar.YEAR))
				return true;
		}else
		{
			today.setTime(new Date());
			if((fechavence.get(Calendar.MONTH)>today.get(Calendar.MONTH) 
					&& fechavence.get(Calendar.YEAR)>=today.get(Calendar.YEAR)
				) ||  fechavence.get(Calendar.YEAR)>today.get(Calendar.YEAR))
			{
				today.add(Calendar.MONTH, 1);
				if((fechavence.get(Calendar.MONTH)>today.get(Calendar.MONTH) 
						&& fechavence.get(Calendar.YEAR)>=today.get(Calendar.YEAR)
						) ||  fechavence.get(Calendar.YEAR)>today.get(Calendar.YEAR))
					return true;
			} 
		}		
		 return false;
	}
	
	public static long dt2i(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int d = cal.get(Calendar.DAY_OF_MONTH);
		int m = cal.get(Calendar.MONTH) + 1; // Cuenta de 0 a 11
		int y = cal.get(Calendar.YEAR);
		int h = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int seg = cal.get(Calendar.SECOND);

		String sd = d + "";
		if (d < 10)
			sd = "0" + sd;
		String sm = m + "";
		if (m < 10)
			sm = "0" + sm;
		String sy = y + "";
		String sh = h + "";
		if (h < 10)
			sh = "0" + sh;
		String smin = min + "";
		if (min < 10)
			smin = "0" + smin;
		String sseg = seg + "";
		if (seg < 10)
			sseg = "0" + sseg;

		String sdate = sy + sm + sd + sh + smin + sseg;
		return Long.parseLong(sdate);
	}

	// Regresa la diferencia en minutos entre dos horas
	// expresadas en milisegundos
	public static long diffMinutes(long startTime, long endTime) {
		long difMS = endTime - startTime;
		long diffMinutes = (long) Math.floor((difMS / 60000L)); // Un minuto =
																// 60 * 1000 =
																// 60000
																// milisecs
		return diffMinutes;
	}

	public static int diffDays(long startTime, long endTime) {
		return (int) (diffMinutes(startTime, endTime) / (1440)); // Un d�a = 24
																	// * 60 =
																	// 1440
																	// minutos
	}

	public static long addDays(long time, int days) {
		return time + days * 86400000; // Un d�a = 24 * 60 * 60 * 1000 =
										// 86400000 milisegundos
	}

	public static long addDay(long time, int days) {
		Calendar fecha = Calendar.getInstance();
		int year = Integer.parseInt(String.valueOf(time).substring(0, 4));
		int month = Integer.parseInt(String.valueOf(time).substring(4, 6)) - 1;
		int day = Integer.parseInt(String.valueOf(time).substring(6, 8));
		fecha.set(year, month, day);
		fecha.add(Calendar.DAY_OF_MONTH, days);
		year = fecha.get(Calendar.YEAR);
		month = fecha.get(Calendar.MONTH) + 1;
		day = fecha.get(Calendar.DAY_OF_MONTH);
		String strMes = ((month<10)?"0":"")+month;
		String strDia = ((day<10)?"0":"")+day;
		long _fecha = Long.parseLong(fecha.get(Calendar.YEAR) + ""
				+ strMes + ""
				+ strDia);
		return _fecha;
	}

}