package com.klm.KLMPortal.beans;

import java.util.Date;

public class EventBean {

	private Integer id;
	private String eventName;
	private Date eventSetDate;
	private Date eventETADate;
	private String comment;
	
	public EventBean(Integer id, String eventName, Date eventSetDate, Date eventETADate, String comment) {
		super();
		this.id = id;
		this.eventName = eventName;
		this.eventSetDate = eventSetDate;
		this.eventETADate = eventETADate;
		this.comment = comment;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Date getEventSetDate() {
		return eventSetDate;
	}

	public void setEventSetDate(Date eventSetDate) {
		this.eventSetDate = eventSetDate;
	}

	public Date getEventETADate() {
		return eventETADate;
	}

	public void setEventETADate(Date eventETADate) {
		this.eventETADate = eventETADate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
}
