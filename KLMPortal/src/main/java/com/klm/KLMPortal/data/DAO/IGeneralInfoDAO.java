package com.klm.KLMPortal.data.DAO;

import java.util.ArrayList;
import java.util.Date;

import com.klm.KLMPortal.beans.EventBean;
import com.klm.KLMPortal.beans.InfoBean;

public interface IGeneralInfoDAO {
	
	public ArrayList<InfoBean> getAllInfos();
	
	public void addNewInfo(String infoKey, String infoValue, String comment);
	
	public void setInfoData(Integer infoId, String infoValue, String comment);
	
	public void deleteInfo(Integer infoId);
	
	public ArrayList<EventBean> getAllEvents();
	
	public ArrayList<EventBean> getAllEventsByType(String eventType);
	
	public void addNewEvent(String eventName, Date eventSetDate, Date eventETADate, String comment, String eventType);
	
	public void setEventSetDate(Integer eventId, Date eventSetDate);
	
	public void setEventETADate(Integer eventId, Date eventETADate);
	
	public void deleteEvent(Integer eventId);
	
	public void setEventComment(Integer eventId, String comment);
	
	public void setEventData(Integer eventId, String eventName, Date eventSetDate, Date eventETADate, String comment, String eventType);
	
	public ArrayList<EventBean> getPostEventsNotReceived();
}
