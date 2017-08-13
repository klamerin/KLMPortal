package com.klm.KLMPortal.data.DAO;

import java.time.LocalDate;
import java.util.ArrayList;

import com.klm.KLMPortal.beans.EventBean;
import com.klm.KLMPortal.beans.GeneralEventBean;
import com.klm.KLMPortal.beans.InfoBean;
import com.klm.KLMPortal.beans.MonthlyEventBean;
import com.klm.KLMPortal.beans.PostEventBean;

public interface IGeneralInfoDAO {
	
	public ArrayList<InfoBean> getAllInfos();
	
	public void addNewInfo(String infoKey, String infoValue, String comment);
	
	public void setInfoData(Integer infoId, String infoValue, String comment);
	
	public void deleteInfo(Integer infoId);
	
	public ArrayList<EventBean> getAllEvents();
	
	public ArrayList<EventBean> getAllEventsByType(String eventType);
	
	
	public void addNewEvent(String eventName, LocalDate eventSetLocalDate, LocalDate eventETALocalDate, String comment, String eventType);
	
	
	public void setEventSetDate(Integer eventId, LocalDate eventSetLocalDate);
	
	public void setEventETADate(Integer eventId, LocalDate eventETALocalDate);
	
	public void deleteEvent(Integer eventId);
	
	public void setEventComment(Integer eventId, String comment);
	
	public void setEventData(Integer eventId, String eventName, LocalDate eventSetLocalDate, LocalDate eventETALocalDate, String comment, String eventType);
	
	public ArrayList<EventBean> getPostEventsNotReceived();
	
	
	//************************REFACTORE**********************************//
	
	public ArrayList<GeneralEventBean> getAllGeneralEvents();
	
	public void addNewGeneralEvent(String eventName, String eventDescription, String comment, LocalDate eventDate);
	
	public void updateGeneralEvent(Integer eventId, String eventName, String eventDescription, String comment, LocalDate eventDate);
	
	public void deleteGeneralEvent(Integer eventId);
	
	public ArrayList<MonthlyEventBean> getAllMonthlyEvents();
	
	public void addNewMonthlyEvent(MonthlyEventBean bean);
	
	public void updateMonthlyEvent(MonthlyEventBean bean);
	
	public void deleteMonthlyEvent(Integer eventId);
	
	public ArrayList<PostEventBean> getPostEvents(boolean notReceived);
	
	public void addNewPostEvent(PostEventBean bean);
	
	public void updatePostEvent(PostEventBean bean);
	
	public void deletePostEvent(Integer eventId);
}
