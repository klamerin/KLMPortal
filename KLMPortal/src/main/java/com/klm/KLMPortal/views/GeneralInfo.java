package com.klm.KLMPortal.views;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.klm.KLMPortal.beans.EventBean;
import com.klm.KLMPortal.beans.InfoBean;
import com.klm.KLMPortal.data.DAOFactory;
import com.klm.KLMPortal.data.DAO.IGeneralInfoDAO;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class GeneralInfo extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DAOFactory mssqlDAOFactory = DAOFactory.getFactory();
	private IGeneralInfoDAO generalInfoDAO = mssqlDAOFactory.getGeneralInfoDAO();

	private Navigator nav = UI.getCurrent().getNavigator();
	private HorizontalLayout navLayout = new HorizontalLayout();
	private static final String FILMSVIEW = "films";
	
	
	private HorizontalLayout infoLayout = new HorizontalLayout();
	private VerticalLayout eventLayout = new VerticalLayout();
	private HorizontalSplitPanel panel = new HorizontalSplitPanel(infoLayout, eventLayout);

	private HorizontalLayout eventsButtonLayout = new HorizontalLayout();

	private HorizontalLayout postEventsLayout;
	private Button postEventsButton;
	private Table postEventsTable;

	private HorizontalLayout toDoEventsLayout;
	private Button toDoEventsButton;
	private Table toDoEventsTable;

	private HorizontalLayout generalEventsLayout;
	private Button generalEventsButton;
	private Table generalEventsTable;
	
	private Table infoTable;

	private enum EvenType {
		POST("POST_", "RECEIVED", "NOT RECEIVED"), TODO("TODO_", "DONE", "NOT DONE"), GENERAL("GENERAL_", "", "");

		private String type;
		private String done;
		private String undone;

		private EvenType(String type, String done, String undone) {
			this.type = type;
			this.done = done;
			this.undone = undone;
		}
	}

	private String eventType;

	public GeneralInfo() {
		buildMainLayout();
	}

	private void buildMainLayout() {
		setSizeFull();
		setInfoLayout();
		setEventLayout();
		setNavLayout();
		addComponent(panel);
//		setExpandRatio(navLayout, 1);
//		setExpandRatio(panel, 10);
	}
	
	private void setNavLayout() {
		navLayout.setMargin(true);
		navLayout.setSpacing(true);
		Button startViewButton = new Button("Start View", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				nav.navigateTo("");
			}
		});
		navLayout.addComponent(startViewButton);
		navLayout.setComponentAlignment(startViewButton, Alignment.TOP_CENTER);
		Button genInfoButton = new Button("Films", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				nav.navigateTo(FILMSVIEW);
			}
		});
		navLayout.addComponent(genInfoButton);
		navLayout.setComponentAlignment(genInfoButton, Alignment.TOP_CENTER);
		addComponent(navLayout);
		setComponentAlignment(navLayout, Alignment.TOP_CENTER);
	}
	
	
	private void setInfoLayout() {
		infoLayout.setMargin(true);
		infoLayout.setSpacing(true);
		Label infoLabel = new Label("Infos");
		infoLayout.addComponent(infoLabel);
		infoLayout.setComponentAlignment(infoLabel, Alignment.TOP_CENTER);
		Button addNewInfoButton = new Button("Add Info", new AddInfoButtonListener());
		infoLayout.addComponent(addNewInfoButton);
		infoLayout.setComponentAlignment(addNewInfoButton, Alignment.TOP_LEFT);

		BeanItemContainer<InfoBean> infosContainer = new BeanItemContainer<>(InfoBean.class,
				generalInfoDAO.getAllInfos());
		infoTable = new Table();
		infoTable.setContainerDataSource(infosContainer);
		infoTable.setSelectable(true);
		infoTable.addActionHandler(new Handler() {
			private static final long serialVersionUID = 1L;
			private final Action ACTION_DELETE = new Action("Delete");
			private final Action ACTION_OPEN = new Action("Open");
			private final Action ACTION_NEW = new Action("New");
			private final Action ACTION_EDIT = new Action("Edit");
			// Action sets
			private final Action[] ACTIONS = new Action[] { ACTION_DELETE, ACTION_OPEN, ACTION_NEW, ACTION_EDIT };

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				if (action == ACTION_OPEN) {
					openInfoWindow((InfoBean) infoTable.getValue(), false);
				} else if (action == ACTION_DELETE) {
					generalInfoDAO.deleteInfo(((InfoBean) infoTable.getValue()).getId());
					refreshInfoTable();
				} else if (action == ACTION_EDIT) {
					openInfoWindow((InfoBean) infoTable.getValue(), true);
				} else if (action == ACTION_NEW) {
					openInfoWindow(null, true);
				}
			}

			@Override
			public Action[] getActions(Object target, Object sender) {
				return ACTIONS;
			}
		});
		infoTable.setVisibleColumns("infoKey", "infoValue", "comment");
		infoTable.setColumnHeaders("Info Key", "Info Value", "Comment");
		infoLayout.addComponent(infoTable);
		infoLayout.setComponentAlignment(infoTable, Alignment.TOP_RIGHT);
	}

	private void setEventLayout() {
		eventLayout.setMargin(true);
		eventLayout.setSpacing(true);
		Label eventLabel = new Label("Events");
		eventLayout.addComponent(eventLabel);
		eventLayout.setComponentAlignment(eventLabel, Alignment.TOP_CENTER);

		eventsButtonLayout.setSpacing(true);
		eventsButtonLayout.setMargin(true);
		postEventsButton = new Button("POST", new EventButtonListener(EvenType.POST.type));
		eventsButtonLayout.addComponent(postEventsButton);

		toDoEventsButton = new Button("TODO", new EventButtonListener(EvenType.TODO.type));
		eventsButtonLayout.addComponent(toDoEventsButton);

		generalEventsButton = new Button("GENERAL", new EventButtonListener(EvenType.GENERAL.type));
		eventsButtonLayout.addComponent(generalEventsButton);
		
		eventsButtonLayout.setExpandRatio(postEventsButton, 1);
		eventsButtonLayout.setExpandRatio(toDoEventsButton, 1);
		eventsButtonLayout.setExpandRatio(generalEventsButton, 1);
		eventLayout.addComponent(eventsButtonLayout);
	}

	private void setPostEvents() {
		if (toDoEventsLayout != null) {
			eventLayout.removeComponent(toDoEventsLayout);
		}

		if (generalEventsLayout != null) {
			eventLayout.removeComponent(generalEventsLayout);
		}

		Button addNewEventButton = new Button("Add Post", new AddEventButtonListener());

		postEventsLayout = new HorizontalLayout();
		postEventsLayout.setSpacing(true);
		postEventsLayout.setMargin(true);
		postEventsLayout.addComponent(addNewEventButton);
		postEventsLayout.setComponentAlignment(addNewEventButton, Alignment.TOP_LEFT);

		postEventsTable = new Table() {
			private static final long serialVersionUID = 1L;

			@Override
	        protected String formatPropertyValue(Object rowId, Object colId, Property< ? > property)
	        {
	            Object v = property.getValue();
	            if (v instanceof Date)
	            {
	            	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	            	Date dateValue = (Date)v;
	                return df.format(dateValue);
	            }
	            return super.formatPropertyValue(rowId, colId, property);
	        }
		};
		postEventsTable.addGeneratedColumn("Received?", new ColumnGenerator() {

			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				EventBean currentEvent = (EventBean) itemId;
				if (currentEvent.getComment().equals(EvenType.POST.undone)) {
					return new Button("Got It!", new SetEventETADateNowButtonListener(currentEvent.getId()));
				} else {
					return new Embedded(null, new ThemeResource("icons/received_main.png"));
				}
			}
		});
		setPostEventTable();
		postEventsTable.addActionHandler(new Handler() {
			private static final long serialVersionUID = 1L;
			private final Action ACTION_DELETE = new Action("Delete");
			private final Action ACTION_OPEN = new Action("Open");
			private final Action ACTION_NEW = new Action("New");
			private final Action ACTION_EDIT = new Action("Edit");
			// Action sets
			private final Action[] ACTIONS = new Action[] { ACTION_DELETE, ACTION_OPEN, ACTION_NEW, ACTION_EDIT };

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				if (action == ACTION_OPEN) {
					openEventWindow((EventBean) postEventsTable.getValue(), false);
				} else if (action == ACTION_DELETE) {
					generalInfoDAO.deleteEvent(((EventBean) postEventsTable.getValue()).getId());
					refreshEventTable();
				} else if (action == ACTION_EDIT) {
					openEventWindow((EventBean) postEventsTable.getValue(), true);
				} else if (action == ACTION_NEW) {
					openEventWindow(null, true);
				}
			}

			@Override
			public Action[] getActions(Object target, Object sender) {
				return ACTIONS;
			}
		});
		postEventsTable.setSelectable(true);
		postEventsLayout.addComponent(postEventsTable);
		eventLayout.addComponent(postEventsLayout);
	}

	private void setTODOEvents() {
		if (postEventsLayout != null) {
			eventLayout.removeComponent(postEventsLayout);
		}

		if (generalEventsLayout != null) {
			eventLayout.removeComponent(generalEventsLayout);
		}

		Button addNewEventButton = new Button("Add TODO", new AddEventButtonListener());

		toDoEventsLayout = new HorizontalLayout();
		toDoEventsLayout.setSpacing(true);
		toDoEventsLayout.setMargin(true);
		toDoEventsLayout.addComponent(addNewEventButton);
		toDoEventsLayout.setComponentAlignment(addNewEventButton, Alignment.TOP_LEFT);

		toDoEventsTable = new Table() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
	        protected String formatPropertyValue(Object rowId, Object colId, Property< ? > property)
	        {
	            Object v = property.getValue();
	            if (v instanceof Date)
	            {
	            	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	            	Date dateValue = (Date)v;
	                return df.format(dateValue);
	            }
	            return super.formatPropertyValue(rowId, colId, property);
	        }
		};
		toDoEventsTable.addGeneratedColumn("Done?", new ColumnGenerator() {

			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				EventBean currentEvent = (EventBean) itemId;
				if (currentEvent.getComment().equals(EvenType.TODO.undone)) {
					return new Button("Done!", new SetEventETADateNowButtonListener(currentEvent.getId()));
				} else {
					return new Embedded(null, new ThemeResource("icons/done_main.png"));
				}
			}
		});
		setToDoEventTable();
		toDoEventsTable.addActionHandler(new Handler() {
			private static final long serialVersionUID = 1L;
			private final Action ACTION_DELETE = new Action("Delete");
			private final Action ACTION_OPEN = new Action("Open");
			private final Action ACTION_NEW = new Action("New");
			private final Action ACTION_EDIT = new Action("Edit");
			// Action sets
			private final Action[] ACTIONS = new Action[] { ACTION_DELETE, ACTION_OPEN, ACTION_NEW, ACTION_EDIT };

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				if (action == ACTION_OPEN) {
					openEventWindow((EventBean) toDoEventsTable.getValue(), false);
				} else if (action == ACTION_DELETE) {
					generalInfoDAO.deleteEvent(((EventBean) toDoEventsTable.getValue()).getId());
					refreshEventTable();
				} else if (action == ACTION_EDIT) {
					openEventWindow((EventBean) toDoEventsTable.getValue(), true);
				} else if (action == ACTION_NEW) {
					openEventWindow(null, true);
				}
			}

			@Override
			public Action[] getActions(Object target, Object sender) {
				return ACTIONS;
			}
		});
		toDoEventsTable.setSelectable(true);
		toDoEventsLayout.addComponent(toDoEventsTable);
		eventLayout.addComponent(toDoEventsLayout);
	}

	private void setGeneralEvents() {
		if (postEventsLayout != null) {
			eventLayout.removeComponent(postEventsLayout);
		}

		if (toDoEventsLayout != null) {
			eventLayout.removeComponent(toDoEventsLayout);
		}

		Button addNewEventButton = new Button("Add General", new AddEventButtonListener());

		generalEventsLayout = new HorizontalLayout();
		generalEventsLayout.setSpacing(true);
		generalEventsLayout.setMargin(true);
		generalEventsLayout.addComponent(addNewEventButton);
		generalEventsLayout.setComponentAlignment(addNewEventButton, Alignment.TOP_LEFT);

		generalEventsTable = new Table() {
			private static final long serialVersionUID = 1L;

			@Override
	        protected String formatPropertyValue(Object rowId, Object colId, Property< ? > property)
	        {
	            Object v = property.getValue();
	            if (v instanceof Date)
	            {
	            	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	            	Date dateValue = (Date)v;
	                return df.format(dateValue);
	            }
	            return super.formatPropertyValue(rowId, colId, property);
	        }
		};
		setGeneralEventTable();
		generalEventsTable.addActionHandler(new Handler() {
			private static final long serialVersionUID = 1L;
			private final Action ACTION_DELETE = new Action("Delete");
			private final Action ACTION_OPEN = new Action("Open");
			private final Action ACTION_NEW = new Action("New");
			private final Action ACTION_EDIT = new Action("Edit");
			// Action sets
			private final Action[] ACTIONS = new Action[] { ACTION_DELETE, ACTION_OPEN, ACTION_NEW, ACTION_EDIT };

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				if (action == ACTION_OPEN) {
					openEventWindow((EventBean) generalEventsTable.getValue(), false);
				} else if (action == ACTION_DELETE) {
					generalInfoDAO.deleteEvent(((EventBean) generalEventsTable.getValue()).getId());
					refreshEventTable();
				} else if (action == ACTION_EDIT) {
					openEventWindow((EventBean) generalEventsTable.getValue(), true);
				} else if (action == ACTION_NEW) {
					openEventWindow(null, true);
				}
			}

			@Override
			public Action[] getActions(Object target, Object sender) {
				return ACTIONS;
			}
		});
		generalEventsTable.setSelectable(true);
		generalEventsLayout.addComponent(generalEventsTable);
		eventLayout.addComponent(generalEventsLayout);
	}

	private void createNewEvent() {

		final Window newEventWindow = new Window();
		VerticalLayout newEventLayout = new VerticalLayout();
		newEventLayout.setSpacing(true);
		newEventLayout.setMargin(true);
		final TextField newEventNameField = new TextField();
		newEventNameField.setInputPrompt("event name");
		final TextField newEventCommentField = new TextField();
		newEventCommentField.setInputPrompt("event comment");
		final DateField newEventSetDateField = new DateField("Set Date");
		final DateField newEventETADateField = new DateField("ETA Date");
		if (eventType.equals(EvenType.POST.type)) {
			newEventETADateField.setVisible(false);
			newEventCommentField.setVisible(false);
			newEventCommentField.setValue(EvenType.POST.undone);
		} else if (eventType.equals(EvenType.TODO.type)) {
			newEventCommentField.setVisible(false);
			newEventCommentField.setValue(EvenType.TODO.undone);
		}

		Button newEventSubmitButton = new Button("Submit", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				String newEventName = newEventNameField.getValue();
				if (newEventName != null && newEventName.length() > 0) {
					generalInfoDAO.addNewEvent(newEventName, newEventSetDateField.getValue(),
							newEventETADateField.getValue(), newEventCommentField.getValue(), eventType);
					refreshEventTable();
				} else {
					Notification.show("Please enter value for Event name");
				}
				newEventWindow.close();
			}
		});
		newEventLayout.addComponent(newEventNameField);
		newEventLayout.addComponent(newEventCommentField);
		newEventLayout.addComponent(newEventSetDateField);
		newEventLayout.addComponent(newEventETADateField);
		newEventLayout.addComponent(newEventSubmitButton);
		newEventWindow.setContent(newEventLayout);
		newEventWindow.setWidth("25%");
		newEventWindow.setHeight("35%");
		newEventWindow.center();
		getUI().addWindow(newEventWindow);

	}

	private void openInfoWindow(final InfoBean info, boolean enabledForEditing) {
		final Window newInfoWindow = new Window();

		VerticalLayout newInfoLayout = new VerticalLayout();
		newInfoLayout.setSpacing(true);
		newInfoLayout.setMargin(true);

		final TextField newInfoKeyField = new TextField();
		if (info == null) {
			newInfoKeyField.setInputPrompt("enter info key");
		} else {
			newInfoKeyField.setValue(info.getInfoKey());
			newInfoKeyField.setReadOnly(true);
		}
		final TextField newInfoValueField = new TextField();
		if (info == null) {
			newInfoValueField.setInputPrompt("enter info value");
		} else {
			newInfoValueField.setValue(info.getInfoValue());
			if (!enabledForEditing) {
				newInfoValueField.setReadOnly(true);
			}
		}

		final TextField newInfoCommentField = new TextField();
		if (info == null) {
			newInfoCommentField.setInputPrompt("enter comment");
		} else {
			newInfoCommentField.setValue(info.getComment());
			if (!enabledForEditing) {
				newInfoCommentField.setReadOnly(true);
			}
		}

		newInfoLayout.addComponent(newInfoKeyField);
		newInfoLayout.addComponent(newInfoValueField);
		newInfoLayout.addComponent(newInfoCommentField);

		if (enabledForEditing) {
			Button newInfoSubmitButton = new Button("Submit", new ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					String newInfoKey = newInfoKeyField.getValue();
					if (newInfoKey != null && newInfoKey.length() > 0) {
						if (info == null) {
							generalInfoDAO.addNewInfo(newInfoKey, newInfoValueField.getValue(),
									newInfoCommentField.getValue());
							refreshInfoTable();
						} else {
							generalInfoDAO.setInfoData(info.getId(), newInfoValueField.getValue(),
									newInfoCommentField.getValue());
							refreshInfoTable();
						}
					} else {
						Notification.show("Enter value for key");
					}
					newInfoWindow.close();
				}
			});
			newInfoLayout.addComponent(newInfoSubmitButton);
		}
		newInfoWindow.setContent(newInfoLayout);
		newInfoWindow.setWidth("25%");
		newInfoWindow.setHeight("35%");
		newInfoWindow.center();
		getUI().addWindow(newInfoWindow);
	}
	
	private void openEventWindow(final EventBean eventBean, boolean enabledForEditing) {
		final Window eventWindow = new Window();

		VerticalLayout eventLayout = new VerticalLayout();
		eventLayout.setSpacing(true);
		eventLayout.setMargin(true);

		final TextField eventNameField = new TextField();
		if (eventBean == null) {
			eventNameField.setInputPrompt("enter event name");
		} else {
			eventNameField.setValue(eventBean.getEventName());
			if (!enabledForEditing) {
				eventNameField.setReadOnly(true);
			}
		}
		
		final DateField eventSetDateField = new DateField("Set Date");
		if (eventBean != null) {
			eventSetDateField.setValue(eventBean.getEventSetDate());
			if (!enabledForEditing) {
				eventSetDateField.setReadOnly(true);
			}
		}

		final DateField eventETADateField = new DateField("ETA Date");
		if (eventBean != null) {
			eventETADateField.setValue(eventBean.getEventETADate());
			if (!enabledForEditing) {
				eventETADateField.setReadOnly(true);
			}
		}
		
		final TextField eventCommentField = new TextField();
		if (eventBean == null) {
			eventCommentField.setInputPrompt("enter comment");
		} else {
			eventCommentField.setValue(eventBean.getComment());
			if (!enabledForEditing) {
				eventCommentField.setReadOnly(true);
			}
		}

		eventLayout.addComponent(eventNameField);
		eventLayout.addComponent(eventSetDateField);
		eventLayout.addComponent(eventETADateField);
		eventLayout.addComponent(eventCommentField);

		if (enabledForEditing) {
			Button eventSubmitButton = new Button("Submit", new ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					String eventName = eventNameField.getValue();
					if (eventName != null && eventName.length() > 0) {
						if (eventBean == null) {
							generalInfoDAO.addNewEvent(eventName, eventSetDateField.getValue(), eventETADateField.getValue(), eventCommentField.getValue(), eventType);
						} else {
							generalInfoDAO.setEventData(eventBean.getId(), eventNameField.getValue(), eventSetDateField.getValue(), eventETADateField.getValue(), eventCommentField.getValue(), eventType);
						}
						refreshEventTable();
					} else {
						Notification.show("Enter value for key");
					}
					eventWindow.close();
				}
			});
			eventLayout.addComponent(eventSubmitButton);
		}
		eventWindow.setContent(eventLayout);
		eventWindow.setWidth("25%");
		eventWindow.setHeight("35%");
		eventWindow.center();
		getUI().addWindow(eventWindow);
	}

	private void refreshInfoTable(){
		BeanItemContainer<InfoBean> infosContainer = new BeanItemContainer<>(InfoBean.class,
				generalInfoDAO.getAllInfos());
		infoTable.setContainerDataSource(infosContainer);
		infoTable.setVisibleColumns("infoKey", "infoValue", "comment");
		infoTable.setColumnHeaders("Info Key", "Info Value", "Comment");
	}
	
	private void refreshEventTable() {
		if (eventType.equals(EvenType.POST.type)) {
			setPostEventTable();
		} else if (eventType.equals(EvenType.TODO.type)) {
			setToDoEventTable();
		} else if (eventType.equals(EvenType.GENERAL.type)) {
			setGeneralEventTable();
		}
	}
	
	private void setToDoEventTable(){
		BeanItemContainer<EventBean> toDoEventsContainer = new BeanItemContainer<>(EventBean.class,
				generalInfoDAO.getAllEventsByType(EvenType.TODO.type));
		toDoEventsTable.setContainerDataSource(toDoEventsContainer);
		toDoEventsTable.setVisibleColumns("eventName", "eventSetDate", "eventETADate", "Done?");
		toDoEventsTable.setColumnHeaders("Event", "Set Date", "ETA Date", "Done?");
	}
	
	private void setPostEventTable(){
		BeanItemContainer<EventBean> postEventsContainer = new BeanItemContainer<>(EventBean.class,
				generalInfoDAO.getAllEventsByType(EvenType.POST.type));
		postEventsTable.setContainerDataSource(postEventsContainer);
		postEventsTable.setVisibleColumns("eventName", "eventSetDate", "Received?");
		postEventsTable.setColumnHeaders("Event", "Order Date", "Received?");
		
	}
	
	private void setGeneralEventTable(){
		BeanItemContainer<EventBean> generalEventsContainer = new BeanItemContainer<>(EventBean.class,
				generalInfoDAO.getAllEventsByType(EvenType.GENERAL.type));
		generalEventsTable.setContainerDataSource(generalEventsContainer);
		generalEventsTable.setVisibleColumns("eventName", "eventSetDate", "eventETADate", "comment");
		generalEventsTable.setColumnHeaders("Event", "Set Date", "ETA Date", "Comment");
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		Notification.show("HIIII, GEN MEN=)");

	}
	

	private class SetEventETADateNowButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		private Integer eventId;

		public SetEventETADateNowButtonListener(Integer eventId) {
			this.eventId = eventId;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			if (eventType.equals(EvenType.POST.type)) {
				generalInfoDAO.setEventETADate(eventId, new Date());
				generalInfoDAO.setEventComment(eventId, EvenType.POST.done);
			} else if (eventType.equals(EvenType.TODO.type)) {
				generalInfoDAO.setEventComment(eventId, EvenType.TODO.done);
			}
			refreshEventTable();

		}
	}


	private class AddEventButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			createNewEvent();
		}

	}

	private class AddInfoButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			openInfoWindow(null, true);
		}
	}

	private class EventButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		private String tempEventType;

		public EventButtonListener(String tempEventType) {
			this.tempEventType = tempEventType;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			eventType = tempEventType;
			if (eventType.equals(EvenType.POST.type)) {
				setPostEvents();
			} else if (eventType.equals(EvenType.TODO.type)) {
				setTODOEvents();
			} else {
				setGeneralEvents();
			}
		}

	}

}
