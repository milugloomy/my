package bocai.bean;

public class Row {
	private String name;
	private Object data;
	
	public Row(){
		super();
	}
	public Row(String name, Object data) {
		super();
		this.name = name;
		this.data = data;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
