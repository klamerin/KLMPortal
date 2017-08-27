package com.klm.KLMPortal.beans;

public class MonthlyEventBean {

	private Integer id;
	private String name;
	private String description;
	private Integer amount;
	private String comment;
	
	public MonthlyEventBean() {
		
	}
	
	public MonthlyEventBean(Integer id, String name, String description, Integer amount, String comment) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.amount = amount;
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

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	

	
}
