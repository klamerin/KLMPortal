package com.klm.KLMPortal.beans;

import java.time.LocalDate;

public class TodoEventBean {

	private Integer id;
	private String name;
	private String description;
	private LocalDate setDate;
	private LocalDate etaDate;
	private String comment;
	
	public TodoEventBean() {
	}
	
	
	public TodoEventBean(Integer id, String name, String description, String comment, LocalDate setDate, LocalDate etaDate) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.setDate = setDate;
		this.etaDate = etaDate;
		this.comment = comment;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getSetDate() {
		return setDate;
	}

	public void setSetDate(LocalDate setDate) {
		this.setDate = setDate;
	}

	public LocalDate getEtaDate() {
		return etaDate;
	}

	public void setEtaDate(LocalDate etaDate) {
		this.etaDate = etaDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	
}
