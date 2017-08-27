package com.klm.KLMPortal.views;

import com.klm.KLMPortal.beans.TodoEventBean;
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
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class EventToDo extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DAOFactory mysqlDAOFactory = DAOFactory.getMYSQLFactory();
	private IGeneralInfoDAO generalInfoDAO = mysqlDAOFactory.getGeneralInfoDAO();

	private Button addNewEventButton;
	private Grid<TodoEventBean> toDoEventsTable;
	private Window eventWindow;

	public EventToDo() {
		buildMainLayout();
	}

	private void buildMainLayout() {
		setMargin(true);
		setSpacing(true);
		setSizeFull();
		setTODOEvents();
		setStyleName("generalInfoBackground");
	}

	private void setTODOEvents() {
		addNewEventButton = new Button("Add TODO", new AddEventButtonListener());

		addComponent(addNewEventButton);
		setComponentAlignment(addNewEventButton, Alignment.TOP_LEFT);

		toDoEventsTable = new Grid<TodoEventBean>(
				TodoEventBean.class);/*
										 * {
										 * 
										 * @Override protected String
										 * formatPropertyValue(Object rowId,
										 * Object colId, Property<?> property) {
										 * Object v = property.getValue(); if (v
										 * instanceof Date) { SimpleDateFormat
										 * df = new
										 * SimpleDateFormat("dd/MM/yyyy"); Date
										 * dateValue = (Date) v; return
										 * df.format(dateValue); } return
										 * super.formatPropertyValue(rowId,
										 * colId, property); } };
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
		GridContextMenu<TodoEventBean> contextMenu = new GridContextMenu<TodoEventBean>(toDoEventsTable);
		setEventsActionHandler(contextMenu);
		addComponent(toDoEventsTable);
	}

	private void setEventsActionHandler(GridContextMenu<TodoEventBean> contextMenu) {
		contextMenu.addGridBodyContextMenuListener(event -> {
			contextMenu.removeItems();
			if (event.getItem() != null) {
				contextMenu.addItem("Open", VaadinIcons.OPEN_BOOK, selectedMenuItem -> {
					openEventWindow((TodoEventBean) event.getItem(), false);
				});
				contextMenu.addItem("Delete", VaadinIcons.DEL, selectedMenuItem -> {
					generalInfoDAO.deleteToDoEvent(((TodoEventBean) event.getItem()).getId());
					setToDoEventTable();
				});
				contextMenu.addItem("Edit", VaadinIcons.EDIT, selectedMenuItem -> {
					openEventWindow((TodoEventBean) event.getItem(), true);
				});
				contextMenu.addItem("New", VaadinIcons.SCREWDRIVER, selectedMenuItem -> {
					openEventWindow(null, true);
				});
			}
		});
	}

	private void openEventWindow(TodoEventBean eventBean, boolean enabledForEditing) {
		boolean newBean = false;

		if (eventBean == null) {
			eventBean = new TodoEventBean();
			newBean = true;
		}

		eventWindow = new Window();

		Binder<TodoEventBean> binder = new Binder<TodoEventBean>(TodoEventBean.class);

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

		final DateField eventETADateField = new DateField("ETA Date");
		if (!enabledForEditing) {
			eventETADateField.setReadOnly(true);
		}

		final TextField eventCommentField = new TextField();
		if (!enabledForEditing) {
			eventCommentField.setReadOnly(true);
		}

		eventLayout.addComponent(eventNameField);
		eventLayout.addComponent(eventSetDateField);
		eventLayout.addComponent(eventETADateField);
		eventLayout.addComponent(eventCommentField);

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

	private void setToDoEventTable() {
		toDoEventsTable.setItems(generalInfoDAO.getAllToDoEvents());
		toDoEventsTable.setColumns("name", "description", "comment", "setDate", "etaDate"/* , "Done?" */);
		toDoEventsTable.getColumn("name").setCaption("Name");
		toDoEventsTable.getColumn("description").setCaption("Description");
		toDoEventsTable.getColumn("comment").setCaption("Comment");
		toDoEventsTable.getColumn("setDate").setCaption("Set Date");
		toDoEventsTable.getColumn("etaDate").setCaption("ETA Date");
		// toDoEventsTable.getColumn("Done?").setCaption("Done?");
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

		public EventWindowSubmitListener(TodoEventBean bean, boolean newBean, Binder<TodoEventBean> binder) {
			this.bean = bean;
			this.newBean = newBean;
			this.binder = binder;
		}

		private TodoEventBean bean;
		private boolean newBean;
		private Binder<TodoEventBean> binder;

		@Override
		public void buttonClick(ClickEvent event) {
			try {
				binder.writeBean(bean);
			} catch (ValidationException e) {
				Notification.show("Sth is wrong with the fields: " + e.getMessage(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}
			if (newBean) {
				generalInfoDAO.addNewToDoEvent(bean);
			} else {
				generalInfoDAO.updateToDoEvent(bean);
			}
			setToDoEventTable();
			eventWindow.close();
		}
	}

}
