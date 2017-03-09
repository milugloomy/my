package common;

import java.util.List;

public class WB {
	private String mid;//该条微博编号
	private WB owb;//引用微博
	private String time;//发布时间
	private String name;//发布者
	private String content;//内容
	private String target;//原文链接
	private List<String> imgUrlList;//图片列表
	private List<String> simgUrlList;//小图片列表
	private String videoUrl;//视频链接
	
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public WB getOwb() {
		return owb;
	}
	public void setOwb(WB owb) {
		this.owb = owb;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public List<String> getImgUrlList() {
		return imgUrlList;
	}
	public void setImgUrlList(List<String> imgUrlList) {
		this.imgUrlList = imgUrlList;
	}
	public List<String> getSimgUrlList() {
		return simgUrlList;
	}
	public void setSimgUrlList(List<String> simgUrlList) {
		this.simgUrlList = simgUrlList;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
}
