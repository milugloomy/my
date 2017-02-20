package bocai.bean;

public class Bocai {
	
	/**一组最终盈利*/
	private Double earn;
	/**一组中盈利最大值*/
	private Double earnMax;
	/**一组中盈利最小值*/
	private Double earnMin;
	/**全部猜对*/
	private long win;
	/**3串1全对*/
	private long win31=0;
	/**3串2全对*/
	private long win32=0;
	/**3串2对2场*/
	private long win32_2=0;
	/**猜错*/
	private long lose;
	
	public Double getEarn() {
		return earn;
	}
	public void setEarn(Double earn) {
		this.earn = earn;
	}
	public Double getEarnMax() {
		return earnMax;
	}
	public void setEarnMax(Double earnMax) {
		this.earnMax = earnMax;
	}
	public Double getEarnMin() {
		return earnMin;
	}
	public void setEarnMin(Double earnMin) {
		this.earnMin = earnMin;
	}
	public long getWin() {
		return win;
	}
	public void setWin(long win) {
		this.win = win;
	}
	public long getWin31() {
		return win31;
	}
	public void setWin31(long win31) {
		this.win31 = win31;
	}
	public long getWin32() {
		return win32;
	}
	public void setWin32(long win32) {
		this.win32 = win32;
	}
	public long getWin32_2() {
		return win32_2;
	}
	public void setWin32_2(long win32_2) {
		this.win32_2 = win32_2;
	}
	public long getLose() {
		return lose;
	}
	public void setLose(long lose) {
		this.lose = lose;
	}
	
}
