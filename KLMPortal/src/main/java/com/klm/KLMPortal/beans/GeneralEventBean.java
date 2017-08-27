package com.klm.KLMPortal.beans;

import java.time.LocalDate;

public class GeneralEventBean {

	private Integer id;
	private String eventName;
	private String eventDescription;
	private LocalDate eventDate;
	private String comment;
	
	public GeneralEventBean() {
		
	}
	
	public GeneralEventBean(Integer id, String eventName, String eventDescription, String comment, LocalDate eventDate) {
		super();
		this.id = id;
		this.eventName = eventName;
		this.eventDescription = eventDescription;
		this.eventDate = eventDate;
		this.comment = comment;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public LocalDate getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDate eventDate) {
		this.eventDate = eventDate;
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

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	
}
