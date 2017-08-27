package com.klm.KLMPortal.views;

import com.klm.KLMPortal.beans.PostEventBean;
import com.klm.KLMPortal.data.DAOFactory;
import com.klm.KLMPortal.data.DAO.IGeneralInfoDAO;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class EventPost extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DAOFactory mysqlDAOFactory = DAOFactory.getMYSQLFactory();
	private IGeneralInfoDAO generalInfoDAO = mysqlDAOFactory.getGeneralInfoDAO();

	private Button addNewEventButton;
	private Grid<PostEventBean> postEventsTable;
	private Window eventWindow;

	public EventPost() {
		buildMainLayout();
	}

	private void buildMainLayout() {
		setSpacing(true);
		setMargin(true);
		setSizeFull();
		setPostEvents();
		setStyleName("generalInfoBackground");
	}

	private void setPostEvents() {
		addNewEventButton = new Button("Add Post", new AddEventButtonListener());

		addComponent(addNewEventButton);
		setComponentAlignment(addNewEventButton, Alignment.TOP_LEFT);

		postEventsTable = new Grid<PostEventBean>(PostEventBean.class);
		/*
		 * { private static final long serialVersionUID = 1L;
		 * 
		 * @Override protected String formatPropertyValue(Object rowId, Object
		 * colId, Property<?> property) { Object v = property.getValue(); if (v
		 * instanceof Date) { SimpleDateFormat df = new
		 * SimpleDateFormat("dd/MM/yyyy"); Date dateValue = (Date) v; return
		 * df.format(dateValue); } return super.formatPropertyValue(rowId,
		 * colId, property); } };
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
		setPostEventTable();

		GridContextMenu<PostEventBean> contextMenu = new GridContextMenu<PostEventBean>(postEventsTable);
		setEventsActionHandler(contextMenu);

		addComponent(postEventsTable);
	}

	private void setEventsActionHandler(GridContextMenu<PostEventBean> contextMenu) {
		contextMenu.addGridBodyContextMenuListener(event -> {
			contextMenu.removeItems();
			if (event.getItem() != null) {
				contextMenu.addItem("Open", VaadinIcons.OPEN_BOOK, selectedMenuItem -> {
					openEventWindow((PostEventBean) event.getItem(), false);
				});
				contextMenu.addItem("Delete", VaadinIcons.DEL, selectedMenuItem -> {
					generalInfoDAO.deletePostEvent(((PostEventBean) event.getItem()).getId());
					setPostEventTable();
				});
				contextMenu.addItem("Edit", VaadinIcons.EDIT, selectedMenuItem -> {
					openEventWindow((PostEventBean) event.getItem(), true);
				});
				contextMenu.addItem("New", VaadinIcons.SCREWDRIVER, selectedMenuItem -> {
					openEventWindow(null, true);
				});
			}
		});
	}

	private void openEventWindow(PostEventBean eventBean, boolean enabledForEditing) {
		boolean newBean = false;

		if (eventBean == null) {
			eventBean = new PostEventBean();
			newBean = true;
		}
		eventWindow = new Window();

		Binder<PostEventBean> binder = new Binder<PostEventBean>(PostEventBean.class);

		VerticalLayout eventLayout = new VerticalLayout();
		eventLayout.setSpacing(true);
		eventLayout.setMargin(true);

		final TextField eventNameField = new TextField("Name");
		if (!enabledForEditing) {
			eventNameField.setReadOnly(true);
		}

		final TextField eventDescriptionField = new TextField("Description");
		if (!enabledForEditing) {
			eventDescriptionField.setReadOnly(true);
		}

		final DateField eventSetDateField = new DateField("Set Date");
		if (!enabledForEditing) {
			eventSetDateField.setReadOnly(true);
		}

		final CheckBox receivedCheckBox = new CheckBox("Received");
		if (!enabledForEditing) {
			receivedCheckBox.setReadOnly(true);
		}
		binder.forField(eventNameField).withValidator(str -> str.length() > 0, "Must be at least 2 chars")
				.bind(PostEventBean::getName, PostEventBean::setName);
		binder.bind(eventDescriptionField, PostEventBean::getDescription, PostEventBean::setDescription);
		binder.bind(eventSetDateField, PostEventBean::getEventSetDate, PostEventBean::setEventSetDate);
		binder.bind(receivedCheckBox, PostEventBean::getReceived, PostEventBean::setReceived);

		eventLayout.addComponent(eventNameField);
		eventLayout.addComponent(eventDescriptionField);
		eventLayout.addComponent(eventSetDateField);
		eventLayout.addComponent(receivedCheckBox);

		binder.readBean(eventBean);
		HorizontalLayout buttonsLayout = new HorizontalLayout();

		if (enabledForEditing) {
			Button eventSubmitButton = new Button("Submit", new EventWindowSubmitListener(eventBean, newBean, binder));
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
		eventLayout.addComponent(buttonsLayout);
		eventWindow.setContent(eventLayout);
		eventWindow.setWidth("25%");
		eventWindow.setHeight("35%");
		eventWindow.center();
		getUI().addWindow(eventWindow);
	}

	private void setPostEventTable() {
		postEventsTable.setItems(generalInfoDAO.getPostEvents(false));
		postEventsTable.setColumns("name", "description", "eventSetDate", "received");
		postEventsTable.getColumn("name").setCaption("Name");
		postEventsTable.getColumn("description").setCaption("Description");
		postEventsTable.getColumn("eventSetDate").setCaption("Set Date");
		postEventsTable.getColumn("received").setCaption("Received");
	}

	private class AddEventButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			openEventWindow(null, true);
		}

	}

	private class EventWindowSubmitListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		public EventWindowSubmitListener(PostEventBean bean, boolean newBean, Binder<PostEventBean> binder) {
			this.bean = bean;
			this.newBean = newBean;
			this.binder = binder;
		}

		private PostEventBean bean;
		private boolean newBean;
		private Binder<PostEventBean> binder;

		@Override
		public void buttonClick(ClickEvent event) {
			try {
				binder.writeBean(bean);
			} catch (ValidationException e) {
				Notification.show("Sth is wrong with the fields: " + e.getMessage(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}
			if (newBean) {
				generalInfoDAO.addNewPostEvent(bean);
			} else {
				generalInfoDAO.updatePostEvent(bean);
			}
			setPostEventTable();
			eventWindow.close();
		}
	}
}
