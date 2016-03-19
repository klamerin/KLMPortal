package com.klm.KLMPortal.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the GENERAL_EVENTS database table.
 * 
 */
@Entity
@Table(name="GENERAL_EVENTS")
@NamedQuery(name="GeneralEvent.findAll", query="SELECT g FROM GeneralEvent g")
public class GeneralEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private int id;

	@Column(name="EVENT_NAME")
	private String eventName;

	@Column(name="EVENT_TIMESTAMP")
	private Timestamp eventTimestamp;

	public GeneralEvent() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEventName() {
		return this.eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Timestamp getEventTimestamp() {
		return this.eventTimestamp;
	}

	public void setEventTimestamp(Timestamp eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}

}