package com.klm.KLMPortal.beans;

import java.time.LocalDate;
import java.util.Date;

public class EventBean {

	private Integer id;
	private String eventName;
	private LocalDate eventSetDate;
	private LocalDate eventETADate;
	private String comment;
	
	public EventBean(Integer id, String eventName, LocalDate eventSetDate, LocalDate eventETADate, String comment) {
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

	public LocalDate getEventSetDate() {
		return eventSetDate;
	}

	public void setEventSetDate(LocalDate eventSetDate) {
		this.eventSetDate = eventSetDate;
	}

	public LocalDate getEventETADate() {
		return eventETADate;
	}

	public void setEventETADate(LocalDate eventETADate) {
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
