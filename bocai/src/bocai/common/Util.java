package bocai.common;

import java.util.Calendar;

public class Util {
	/**2014赛季常规赛起始时间*/
	public static String s2014="2014-10-28";
	/**2015赛季常规赛起始时间*/
	public static String s2015="2015-10-27";
	/**2016赛季常规赛起始时间*/
	public static String s2016="2016-10-25";
	
	public static int getSeason(){
		Calendar calendar = Calendar.getInstance();
		int season=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH);
		//8月之前，算上个赛季
		if(month<8){
			season-=1;
		}
		return season;
	}
	
	//赛季起始时间
	public static String seasonStart(){
		int season=getSeason();
		String startTime=season+"-10-01";
		return startTime;
	}
}
