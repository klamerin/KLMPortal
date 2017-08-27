package com.klm.KLMPortal.views;

import java.time.LocalDate;

import com.klm.KLMPortal.beans.GeneralEventBean;
import com.klm.KLMPortal.data.DAOFactory;
import com.klm.KLMPortal.data.DAO.IGeneralInfoDAO;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class EventGeneral extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DAOFactory mysqlDAOFactory = DAOFactory.getMYSQLFactory();
	private IGeneralInfoDAO generalInfoDAO = mysqlDAOFactory.getGeneralInfoDAO();

	private Grid<GeneralEventBean> generalEventsTable;
	
	private Button addNewEventButton;
	private Window eventWindow;

	public EventGeneral() {
		super();
		buildMainLayout();
	}

	private void buildMainLayout() {
		setSpacing(true);
		setMargin(true);
		setSizeFull();
		setStyleName("generalInfoBackground");
		setGeneralEvents();
	}

	private void setGeneralEvents() {
		addNewEventButton = new Button("Add", new AddEventButtonListener());

		addComponent(addNewEventButton);
		setComponentAlignment(addNewEventButton, Alignment.TOP_LEFT);

		generalEventsTable = new Grid<GeneralEventBean>(
				GeneralEventBean.class);
		generalEventsTable.setStyleName("infoTable");
		setGeneralEventTable();
		GridContextMenu<GeneralEventBean> contextMenu = new GridContextMenu<GeneralEventBean>(generalEventsTable);
		setEventsActionHandler(contextMenu);
		addComponent(generalEventsTable);
	}

	private void setEventsActionHandler(GridContextMenu<GeneralEventBean> contextMenu) {
		contextMenu.addGridBodyContextMenuListener(event -> {
			contextMenu.removeItems();
			if (event.getItem() != null) {
				contextMenu.addItem("Open", VaadinIcons.OPEN_BOOK, selectedMenuItem -> {
					openEventWindow((GeneralEventBean) event.getItem(), false);
				});
				contextMenu.addItem("Delete", VaadinIcons.DEL, selectedMenuItem -> {
					generalInfoDAO.deleteGeneralEvent(((GeneralEventBean) event.getItem()).getId());
					setGeneralEventTable();
				});
				contextMenu.addItem("Edit", VaadinIcons.EDIT, selectedMenuItem -> {
					openEventWindow((GeneralEventBean) event.getItem(), true);
				});
				contextMenu.addItem("New", VaadinIcons.SCREWDRIVER, selectedMenuItem -> {
					openEventWindow(null, true);
				});
			}
		});
	}
	

	private void openEventWindow(final GeneralEventBean eventBean, boolean enabledForEditing) {
		eventWindow = new Window();

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
		eventNameField.setWidth("70%");
		
		final TextField eventDescriptionField = new TextField();
		if (eventBean == null) {
			eventDescriptionField.setPlaceholder("enter description name");
		} else {
			eventDescriptionField.setValue(eventBean.getEventDescription() != null ? eventBean.getEventDescription() : "");
			if (!enabledForEditing) {
				eventDescriptionField.setReadOnly(true);
			}
		}
		eventDescriptionField.setWidth("70%");

		final DateField eventDateField = new DateField("Set Date");
		if (eventBean != null) {
			eventDateField.setValue(eventBean.getEventDate() != null ? eventBean.getEventDate() : LocalDate.now());
			if (!enabledForEditing) {
				eventDateField.setReadOnly(true);
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
		eventCommentField.setWidth("70%");

		eventLayout.addComponent(eventNameField);
		eventLayout.addComponent(eventDescriptionField);
		eventLayout.addComponent(eventCommentField);
		eventLayout.addComponent(eventDateField);

		HorizontalLayout buttonsLayout = new HorizontalLayout();
		eventLayout.addComponent(buttonsLayout);
		
		if (enabledForEditing) {
			Button eventSubmitButton = new Button("Submit", new ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					String eventName = eventNameField.getValue();
					if (eventName != null && eventName.length() > 0) {
						if (eventBean == null) {
							generalInfoDAO.addNewGeneralEvent(eventName, eventDescriptionField.getValue(), eventCommentField.getValue(), eventDateField.getValue());
						} else {
							generalInfoDAO.updateGeneralEvent(eventBean.getId(), eventNameField.getValue(),
									eventDescriptionField.getValue(),
									eventCommentField.getValue(),
									eventDateField.getValue());
						}
						setGeneralEventTable();
					} else {
						Notification.show("Enter value for key");
					}
					eventWindow.close();
				}
			});
			buttonsLayout.addComponent(eventSubmitButton);
		}
		Button closeButton = new Button("Close", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				eventWindow.close();

			}
		});
		buttonsLayout.addComponent(closeButton);

		eventWindow.setContent(eventLayout);
		eventWindow.setWidth("25%");
		eventWindow.setHeight("35%");
		eventWindow.center();
		getUI().addWindow(eventWindow);
	}


	private void setGeneralEventTable() {
		generalEventsTable.setItems(generalInfoDAO.getAllGeneralEvents());
		generalEventsTable.setColumns("eventName", "eventDescription", "comment", "eventDate");
		generalEventsTable.getColumn("eventName").setCaption("Event");
		generalEventsTable.getColumn("eventDescription").setCaption("Description");
		generalEventsTable.getColumn("eventDate").setCaption("Date");
		generalEventsTable.getColumn("comment").setCaption("Comment");
	}


	private class AddEventButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			openEventWindow(null, true);
		}

	}
}
