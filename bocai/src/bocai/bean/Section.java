package bocai.bean;

public class Section {
	private Double start;
	private Double end;
	private Double avg;
	private int count;
	
	public Section(){
		this.count=0;
	}
	
	public Double getStart() {
		return start;
	}
	public void setStart(Double start) {
		this.start = start;
	}
	public Double getEnd() {
		return end;
	}
	public void setEnd(Double end) {
		this.end = end;
	}
	public Double getAvg() {
		return avg;
	}
	public void setAvg(Double avg) {
		this.avg = avg;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		String str="Start:"+start+
				"\nEnd:"+end+
				"\nAvg:"+avg+
				"\nCount:"+count;
		return str;
	}
}
