package com.imgl.wx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="images")
public class Images {
	private int imgId;
	private int activityId;
	private String imgUrl;
	private int mainPic;
	@Id
	@GeneratedValue
	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getMainPic() {
		return mainPic;
	}
	public void setMainPic(int mainPic) {
		this.mainPic = mainPic;
	}
}
