package hu.gaborbalazs.redis.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacebookPost {

	private String id;
	@JsonProperty("created_time")
	private Date createdTime;
	private String message;

	public FacebookPost() {
	}

	public FacebookPost(String id, Date createdTime, String message) {
		super();
		this.id = id;
		this.createdTime = createdTime;
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "FacebookPost [id=" + id + ", createdTime=" + createdTime + ", message=" + message + "]";
	}

}
