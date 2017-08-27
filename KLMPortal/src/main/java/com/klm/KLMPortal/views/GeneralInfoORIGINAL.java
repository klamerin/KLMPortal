package com.klm.KLMPortal.views;

import java.time.LocalDate;
import java.util.ArrayList;

import com.klm.KLMPortal.beans.EventBean;
import com.klm.KLMPortal.beans.InfoBean;
import com.klm.KLMPortal.data.DAOFactory;
import com.klm.KLMPortal.data.DAO.IGeneralInfoDAO;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;

public class GeneralInfoORIGINAL extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private DAOFactory mssqlDAOFactory = DAOFactory.getMSSQLFactory();
	// private IGeneralInfoDAO generalInfoDAO =
	// mssqlDAOFactory.getGeneralInfoDAO();
	private DAOFactory mysqlDAOFactory = DAOFactory.getMYSQLFactory();
	private IGeneralInfoDAO generalInfoDAO = mysqlDAOFactory.getGeneralInfoDAO();

	private Navigator nav = UI.getCurrent().getNavigator();
	private HorizontalLayout navLayout = new HorizontalLayout();
	private static final String FILMSVIEW = "films";
	protected static final String MUSICSVIEW = "music";

	private HorizontalLayout infoLayout = new HorizontalLayout();
	private VerticalLayout eventLayout = new VerticalLayout();
	private HorizontalSplitPanel panel = new HorizontalSplitPanel(infoLayout, eventLayout);

	private HorizontalLayout eventsButtonLayout = new HorizontalLayout();

	private HorizontalLayout postEventsLayout;
	private Button postEventsButton;
	private Grid<EventBean> postEventsTable;

	private HorizontalLayout toDoEventsLayout;
	private Button toDoEventsButton;
	private Grid<EventBean> toDoEventsTable;

	private HorizontalLayout generalEventsLayout;
	private Button generalEventsButton;
	private Grid<EventBean> generalEventsTable;

	private HorizontalLayout monthlyEventsLayout;
	private Button monthlyEventsButton;
	private Grid<EventBean> monthlyEventsTable;

	private Grid<InfoBean> infoTable;

	private enum EvenType {
		POST("POST_", "RECEIVED", "NOT RECEIVED"), TODO("TODO_", "DONE", "NOT DONE"), GENERAL("GENERAL_", "",
				""), MONTHLY("MONTHLY_", "", "");

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

	public GeneralInfoORIGINAL() {
		buildMainLayout();
	}

	private void buildMainLayout() {
		setSizeFull();
		setInfoLayout();
		setEventLayout();
		setNavLayout();
		addComponent(panel);
		setExpandRatio(navLayout, 1);
		setExpandRatio(panel, 10);

		setStyleName("generalInfoBackground");

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
		Button filmsButton = new Button("Films", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				nav.navigateTo(FILMSVIEW);
			}
		});
		navLayout.addComponent(filmsButton);
		navLayout.setComponentAlignment(filmsButton, Alignment.TOP_CENTER);

		Button musicButton = new Button("Music", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				nav.navigateTo(MUSICSVIEW);
			}
		});
		navLayout.addComponent(musicButton);
		navLayout.setComponentAlignment(musicButton, Alignment.TOP_CENTER);
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

		infoTable = new Grid<InfoBean>(InfoBean.class);
		infoTable.setItems(generalInfoDAO.getAllInfos());
		infoTable.setStyleName("infoTable");

		GridContextMenu<InfoBean> contextMenu = new GridContextMenu<InfoBean>(infoTable);
		contextMenu.addGridBodyContextMenuListener(event -> {
			contextMenu.removeItems();
			if (event.getItem() != null) {
				contextMenu.addItem("Open", VaadinIcons.OPEN_BOOK, selectedMenuItem -> {
					openInfoWindow((InfoBean) event.getItem(), false);
				});
				contextMenu.addItem("Delete", VaadinIcons.DEL, selectedMenuItem -> {
					generalInfoDAO.deleteInfo(((InfoBean) event.getItem()).getId());
					refreshInfoTable();
				});
				contextMenu.addItem("Edit", VaadinIcons.EDIT, selectedMenuItem -> {
					openInfoWindow((InfoBean) event.getItem(), true);
				});
				contextMenu.addItem("New", VaadinIcons.SCREWDRIVER, selectedMenuItem -> {
					openInfoWindow(null, true);
				});
			}
		});

		infoTable.setColumns("infoKey", "infoValue", "comment");
		infoTable.getColumn("infoKey").setCaption("Info Key");
		infoTable.getColumn("infoValue").setCaption("Info Value");
		infoTable.getColumn("comment").setCaption("Comment");
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

		monthlyEventsButton = new Button("MONTHLY", new EventButtonListener(EvenType.MONTHLY.type));
		eventsButtonLayout.addComponent(monthlyEventsButton);

		eventsButtonLayout.setExpandRatio(postEventsButton, 1);
		eventsButtonLayout.setExpandRatio(toDoEventsButton, 1);
		eventsButtonLayout.setExpandRatio(generalEventsButton, 1);
		eventLayout.addComponent(eventsButtonLayout);
	}

	private void removeAllEventComponents() {
		if (toDoEventsLayout != null) {
			eventLayout.removeComponent(toDoEventsLayout);
		}

		if (generalEventsLayout != null) {
			eventLayout.removeComponent(generalEventsLayout);
		}

		if (postEventsLayout != null) {
			eventLayout.removeComponent(postEventsLayout);
		}

		if (monthlyEventsLayout != null) {
			eventLayout.removeComponent(monthlyEventsLayout);
		}
	}

	private void setPostEvents() {
		Button addNewEventButton = new Button("Add Post", new AddEventButtonListener());

		postEventsLayout = new HorizontalLayout();
		postEventsLayout.setSpacing(true);
		postEventsLayout.setMargin(true);
		postEventsLayout.addComponent(addNewEventButton);
		postEventsLayout.setComponentAlignment(addNewEventButton, Alignment.TOP_LEFT);

		postEventsTable = new Grid<EventBean>(
				EventBean.class);
		/*
									 * { private static final long
									 * serialVersionUID = 1L;
									 * 
									 * @Override protected String
									 * formatPropertyValue(Object rowId, Object
									 * colId, Property<?> property) { Object v =
									 * property.getValue(); if (v instanceof
									 * Date) { SimpleDateFormat df = new
									 * SimpleDateFormat("dd/MM/yyyy"); Date
									 * dateValue = (Date) v; return
									 * df.format(dateValue); } return
									 * super.formatPropertyValue(rowId, colId,
									 * property); } };
									 */
		postEventsTable.setStyleName("infoTable");
		// postEventsTable.addGeneratedColumn("Received?", new ColumnGenerator()
		// {
		//
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public Object generateCell(Table source, Object itemId, Object
		// columnId) {
		// EventBean currentEvent = (EventBean) itemId;
		// if (currentEvent.getComment().equals(EvenType.POST.undone)) {
		// return new Button("Got It!", new
		// SetEventETADateNowButtonListener(currentEvent.getId()));
		// } else {
		// return new Embedded(null, new
		// ThemeResource("icons/received_main.png"));
		// }
		// }
		// });
		postEventsTable.setItems(generalInfoDAO.getPostEventsNotReceived());
		postEventsTable.setColumns("eventName", "eventSetDate"/* , "Received?" */);
		
		GridContextMenu<EventBean> contextMenu = new GridContextMenu<EventBean>(postEventsTable);
		setEventsActionHandler(contextMenu);
		postEventsLayout.addComponent(postEventsTable);
		eventLayout.addComponent(postEventsLayout);
	}

	private void setTODOEvents() {
		Button addNewEventButton = new Button("Add TODO", new AddEventButtonListener());

		toDoEventsLayout = new HorizontalLayout();
		toDoEventsLayout.setSpacing(true);
		toDoEventsLayout.setMargin(true);
		toDoEventsLayout.addComponent(addNewEventButton);
		toDoEventsLayout.setComponentAlignment(addNewEventButton, Alignment.TOP_LEFT);

		toDoEventsTable = new Grid<EventBean>(
				EventBean.class);/*
									 * {
									 * 
									 * @Override protected String
									 * formatPropertyValue(Object rowId, Object
									 * colId, Property<?> property) { Object v =
									 * property.getValue(); if (v instanceof
									 * Date) { SimpleDateFormat df = new
									 * SimpleDateFormat("dd/MM/yyyy"); Date
									 * dateValue = (Date) v; return
									 * df.format(dateValue); } return
									 * super.formatPropertyValue(rowId, colId,
									 * property); } };
									 */
		toDoEventsTable.setStyleName("infoTable");
		/*
		 * toDoEventsTable.addGeneratedColumn("Done?", new ColumnGenerator() {
		 * 
		 * private static final long serialVersionUID = 1L;
		 * 
		 * @Override public Object generateCell(Table source, Object itemId,
		 * Object columnId) { EventBean currentEvent = (EventBean) itemId; if
		 * (currentEvent.getComment().equals(EvenType.TODO.undone)) { return new
		 * Button("Done!", new
		 * SetEventETADateNowButtonListener(currentEvent.getId())); } else {
		 * return new Embedded(null, new ThemeResource("icons/done_main.png"));
		 * } } });
		 */
		setToDoEventTable();
		 GridContextMenu<EventBean> contextMenu = new GridContextMenu<EventBean>(toDoEventsTable);
			setEventsActionHandler(contextMenu);
		toDoEventsLayout.addComponent(toDoEventsTable);
		eventLayout.addComponent(toDoEventsLayout);
	}

	private void setGeneralEvents() {
		Button addNewEventButton = new Button("Add General", new AddEventButtonListener());

		generalEventsLayout = new HorizontalLayout();
		generalEventsLayout.setSpacing(true);
		generalEventsLayout.setMargin(true);
		generalEventsLayout.addComponent(addNewEventButton);
		generalEventsLayout.setComponentAlignment(addNewEventButton, Alignment.TOP_LEFT);

		generalEventsTable = new Grid<EventBean>(
				EventBean.class);/*
									 * { private static final long
									 * serialVersionUID = 1L;
									 * 
									 * @Override protected String
									 * formatPropertyValue(Object rowId, Object
									 * colId, Property<?> property) { Object v =
									 * property.getValue(); if (v instanceof
									 * Date) { SimpleDateFormat df = new
									 * SimpleDateFormat("dd/MM/yyyy"); Date
									 * dateValue = (Date) v; return
									 * df.format(dateValue); } return
									 * super.formatPropertyValue(rowId, colId,
									 * property); } };
									 */
		generalEventsTable.setStyleName("infoTable");
		setGeneralEventTable();
		GridContextMenu<EventBean> contextMenu = new GridContextMenu<EventBean>(generalEventsTable);
		setEventsActionHandler(contextMenu);
		generalEventsLayout.addComponent(generalEventsTable);
		eventLayout.addComponent(generalEventsLayout);
	}

	private void setEventsActionHandler(GridContextMenu<EventBean> contextMenu) {
		contextMenu.addGridBodyContextMenuListener(event -> {
			contextMenu.removeItems();
			if (event.getItem() != null) {
				contextMenu.addItem("Open", VaadinIcons.OPEN_BOOK, selectedMenuItem -> {
					openEventWindow((EventBean) event.getItem(), false);
				});
				contextMenu.addItem("Delete", VaadinIcons.DEL, selectedMenuItem -> {
					generalInfoDAO.deleteEvent(((EventBean) event.getItem()).getId());
					refreshEventTable();
				});
				contextMenu.addItem("Edit", VaadinIcons.EDIT, selectedMenuItem -> {
					openEventWindow((EventBean) event.getItem(), true);
				});
				contextMenu.addItem("New", VaadinIcons.SCREWDRIVER, selectedMenuItem -> {
					openEventWindow(null, true);
				});
			}
		});
	}
	
	

	private void setMonthlyEvents() {
		Button addNewEventButton = new Button("Add Monthly", new AddEventButtonListener());

		monthlyEventsLayout = new HorizontalLayout();
		monthlyEventsLayout.setSpacing(true);
		monthlyEventsLayout.setMargin(true);
		monthlyEventsLayout.addComponent(addNewEventButton);
		monthlyEventsLayout.setComponentAlignment(addNewEventButton, Alignment.TOP_LEFT);

		monthlyEventsTable = new Grid<EventBean>(EventBean.class);
		monthlyEventsTable.setStyleName("infoTable");
		setMonthlyEventTable();
		GridContextMenu<EventBean> contextMenu = new GridContextMenu<EventBean>(monthlyEventsTable);
		setEventsActionHandler(contextMenu);
		monthlyEventsLayout.addComponent(monthlyEventsTable);
		eventLayout.addComponent(monthlyEventsLayout);
	}

	private void createNewEvent() {

		final Window newEventWindow = new Window();
		VerticalLayout newEventLayout = new VerticalLayout();
		newEventLayout.setSpacing(true);
		newEventLayout.setMargin(true);
		final TextField newEventNameField = new TextField();
		newEventNameField.setPlaceholder("event name");
		final TextField newEventCommentField = new TextField();
		newEventCommentField.setPlaceholder("event comment");
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

		Button closeButton = new Button("Close", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				newEventWindow.close();

			}
		});

		newEventLayout.addComponent(newEventNameField);
		newEventLayout.addComponent(newEventCommentField);
		newEventLayout.addComponent(newEventSetDateField);
		newEventLayout.addComponent(newEventETADateField);
		newEventLayout.addComponent(newEventSubmitButton);
		newEventLayout.addComponent(closeButton);
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
			newInfoKeyField.setPlaceholder("enter info key");
		} else {
			newInfoKeyField.setValue(info.getInfoKey() != null ? info.getInfoKey() : "");
			newInfoKeyField.setReadOnly(true);
		}
		final TextField newInfoValueField = new TextField();
		if (info == null) {
			newInfoValueField.setPlaceholder("enter info value");
		} else {
			newInfoValueField.setValue(info.getInfoValue() != null ? info.getInfoValue() : "");
			if (!enabledForEditing) {
				newInfoValueField.setReadOnly(true);
			}
		}

		final TextField newInfoCommentField = new TextField();
		if (info == null) {
			newInfoCommentField.setPlaceholder("enter comment");
		} else {
			newInfoCommentField.setValue(info.getComment() != null ? info.getComment() : "");
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
		Button closeButton = new Button("Close", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				newInfoWindow.close();

			}
		});
		newInfoLayout.addComponent(closeButton);

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
			eventNameField.setPlaceholder("enter event name");
		} else {
			eventNameField.setValue(eventBean.getEventName() != null ? eventBean.getEventName() : "");
			if (!enabledForEditing) {
				eventNameField.setReadOnly(true);
			}
		}

		final DateField eventSetDateField = new DateField("Set Date");
		if (eventBean != null) {
			eventSetDateField.setValue(eventBean.getEventSetDate() != null ? eventBean.getEventSetDate() : LocalDate.now());
			if (!enabledForEditing) {
				eventSetDateField.setReadOnly(true);
			}
		}

		final DateField eventETADateField = new DateField("ETA Date");
		if (eventBean != null) {
			eventETADateField.setValue(eventBean.getEventETADate() != null ? eventBean.getEventETADate() : LocalDate.now());
			if (!enabledForEditing) {
				eventETADateField.setReadOnly(true);
			}
		}

		final TextField eventCommentField = new TextField();
		if (eventBean == null) {
			eventCommentField.setPlaceholder("enter comment");
		} else {
			eventCommentField.setValue(eventBean.getComment() != null ? eventBean.getComment() : "");
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
							generalInfoDAO.addNewEvent(eventName, eventSetDateField.getValue(),
									eventETADateField.getValue(), eventCommentField.getValue(), eventType);
						} else {
							generalInfoDAO.setEventData(eventBean.getId(), eventNameField.getValue(),
									eventSetDateField.getValue(), eventETADateField.getValue(),
									eventCommentField.getValue(), eventType);
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
		Button closeButton = new Button("Close", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				eventWindow.close();

			}
		});
		eventLayout.addComponent(closeButton);

		eventWindow.setContent(eventLayout);
		eventWindow.setWidth("25%");
		eventWindow.setHeight("35%");
		eventWindow.center();
		getUI().addWindow(eventWindow);
	}

	private void refreshInfoTable() {
		infoTable.setItems(generalInfoDAO.getAllInfos());
		infoTable.setColumns("infoKey", "infoValue", "comment");
		infoTable.getColumn("infoKey").setCaption("Info Key");
		infoTable.getColumn("infoValue").setCaption("Info Value");
		infoTable.getColumn("comment").setCaption("Comment");
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

	private void setToDoEventTable() {
		toDoEventsTable.setItems(generalInfoDAO.getAllEventsByType(EvenType.TODO.type));
		toDoEventsTable.setColumns("eventName", "eventSetDate", "eventETADate"/* , "Done?" */);
		toDoEventsTable.getColumn("eventName").setCaption("Event");
		toDoEventsTable.getColumn("eventSetDate").setCaption("Set Date");
		toDoEventsTable.getColumn("eventETADate").setCaption("ETA Date");
		// toDoEventsTable.getColumn("Done?").setCaption("Done?");
	}

	private void setPostEventTable() {
		postEventsTable.setItems(generalInfoDAO.getPostEventsNotReceived());
		postEventsTable.setColumns("eventName", "eventSetDate"/* , "Received?" */);
		toDoEventsTable.getColumn("eventName").setCaption("Event");
		toDoEventsTable.getColumn("eventSetDate").setCaption("Set Date");
	}

	private void setGeneralEventTable() {
		generalEventsTable.setItems(generalInfoDAO.getAllEventsByType(EvenType.GENERAL.type));
		generalEventsTable.setColumns("eventName", "eventSetDate", "eventETADate", "comment");
		generalEventsTable.getColumn("eventName").setCaption("Event");
		generalEventsTable.getColumn("eventSetDate").setCaption("Set Date");
		generalEventsTable.getColumn("eventETADate").setCaption("ETA Date");
		generalEventsTable.getColumn("comment").setCaption("Comment");
	}

	private void setMonthlyEventTable() {

		ArrayList<EventBean> allMonthlies = generalInfoDAO.getAllEventsByType(EvenType.MONTHLY.type);
		monthlyEventsTable.setItems(allMonthlies);
		monthlyEventsTable.setColumns("eventName", "comment");
		generalEventsTable.getColumn("eventName").setCaption("Monthly");
		generalEventsTable.getColumn("comment").setCaption("Payment");

		FooterRow footer = generalEventsTable.appendFooterRow();
		double totalSum = 0.0;
		for (EventBean eventBean : allMonthlies) {
			try {
				int tmpSum = Integer.valueOf(eventBean.getComment());
				totalSum += tmpSum;
			} catch (NumberFormatException e) {
				// do nothing
			}
		}

		footer.getCell("eventName").setText("Total");
		footer.getCell("comment").setText(totalSum + "");
	}

	@Override
	public void enter(ViewChangeEvent event) {
		Notification.show("HIIII, GEN MEN=)");

	}

	private class SetEventETADateNowButtonListener implements RendererClickListener<EventBean> {// ClickListener
																								// {

		private static final long serialVersionUID = 1L;

		private Integer eventId;

		public SetEventETADateNowButtonListener(Integer eventId) {
			this.eventId = eventId;
		}

		// @Override
		// public void buttonClick(ClickEvent event) {
		// if (eventType.equals(EvenType.POST.type)) {
		// generalInfoDAO.setEventETADate(eventId, new Date());
		// generalInfoDAO.setEventComment(eventId, EvenType.POST.done);
		// } else if (eventType.equals(EvenType.TODO.type)) {
		// generalInfoDAO.setEventComment(eventId, EvenType.TODO.done);
		// }
		// refreshEventTable();
		//
		// }

		@Override
		public void click(RendererClickEvent<EventBean> event) {
			if (eventType.equals(EvenType.POST.type)) {
				generalInfoDAO.setEventETADate(eventId, LocalDate.now());
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
			removeAllEventComponents();
			eventType = tempEventType;
			if (eventType.equals(EvenType.POST.type)) {
				setPostEvents();
			} else if (eventType.equals(EvenType.TODO.type)) {
				setTODOEvents();
			} else if (eventType.equals(EvenType.MONTHLY.type)) {
				setMonthlyEvents();
			} else {
				setGeneralEvents();
			}
		}

	}

}
