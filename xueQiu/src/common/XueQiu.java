package common;

import java.util.List;

public class XueQiu {
	private String id;//该条雪球编号
	private XueQiu retweet;//引用雪球
	private String info;//时间和来自
	private String userId;//发布者
	private String nickName;//发布者
	private String title;//标题
	private String target;//链接地址
	private String text;//内容
	private String source;//来自
	private List<String> imgUrlList;//图片列表
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public XueQiu getRetweet() {
		return retweet;
	}
	public void setRetweet(XueQiu retweet) {
		this.retweet = retweet;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<String> getImgUrlList() {
		return imgUrlList;
	}
	public void setImgUrlList(List<String> imgUrlList) {
		this.imgUrlList = imgUrlList;
	}
	
}
