package com.klm.KLMPortal.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the FILMS database table.
 * 
 */
@Entity
@Table(name="FILMS")
@NamedQuery(name="Film.findAll", query="SELECT f FROM Film f")
public class Film implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private int id;

	@Column(name="[COMMENT]")
	private String comment;

	@Column(name="LAST_MODIFIED")
	private Timestamp lastModified;

	@Column(name="NAME")
	private String name;

	@Column(name="RATING")
	private int rating;

	@Column(name="RECOMMEND")
	private boolean recommend;

	@Column(name="WATCHED")
	private boolean watched;

	@Column(name="WATCHED_DATE")
	private Timestamp watchedDate;

	public Film() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Timestamp getLastModified() {
		return this.lastModified;
	}

	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRating() {
		return this.rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public boolean getRecommend() {
		return this.recommend;
	}

	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}

	public boolean getWatched() {
		return this.watched;
	}

	public void setWatched(boolean watched) {
		this.watched = watched;
	}

	public Timestamp getWatchedDate() {
		return this.watchedDate;
	}

	public void setWatchedDate(Timestamp watchedDate) {
		this.watchedDate = watchedDate;
	}

}