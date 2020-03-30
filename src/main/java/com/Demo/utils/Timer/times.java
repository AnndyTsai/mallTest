/**
 * 跟时间相关的方法
 * */
package com.Demo.utils.Timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class times {
    
    /**
     * 指定日期加上天数后的日期
     * @param num 为增加的天数
     * @param newDate 创建时间
     * @param timeMode yyyy-MM-dd HH:mm:ss
     * @return
     * @throws Exception 
     */
    public static String plusDay(int num,String newDate, String timeMode){
        SimpleDateFormat format = new SimpleDateFormat(timeMode);
        String enddate = null;
		try {
			Date currdate = format.parse(newDate);
			System.out.println("现在的日期是：" + currdate);
	        Calendar ca = Calendar.getInstance();
	        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
	        currdate = ca.getTime();
	        enddate = format.format(currdate);
	        System.out.println("增加天数以后的日期：" + enddate);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return enddate;
    }

    /**
     * 当前日期加上天数后的日期
     * @param num 为增加的天数
     * @param timeMode 例如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String plusDay2(int num, String timeMode){
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat(timeMode);
        String currdate = format.format(d);
        System.out.println("现在的日期是：" + currdate);

        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        d = ca.getTime();
        String enddate = format.format(d);
        System.out.println("增加天数以后的日期：" + enddate);
        return enddate;
    }
    
    public static void main(String[] args) {
		
		System.out.println(times.plusDay2(0, "yyyy-MM-dd"));
	}
}
