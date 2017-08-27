package com.klm.KLMPortal.views;

import java.util.ArrayList;

import com.klm.KLMPortal.beans.MonthlyEventBean;
import com.klm.KLMPortal.data.DAOFactory;
import com.klm.KLMPortal.data.DAO.IGeneralInfoDAO;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.FooterCell;
import com.vaadin.ui.components.grid.FooterRow;

public class EventMonthly extends HorizontalLayout {

	private static final long serialVersionUID = 1L;
	private DAOFactory mysqlDAOFactory = DAOFactory.getMYSQLFactory();
	private IGeneralInfoDAO generalInfoDAO = mysqlDAOFactory.getGeneralInfoDAO();

	private Button addNewEventButton;
	private Grid<MonthlyEventBean> monthlyEventsTable;
	private Window eventWindow;

	public EventMonthly() {
		super();
		buildMainLayout();
	}

	private void buildMainLayout() {
		setSpacing(true);
		setMargin(true);
		setSizeFull();
		setMonthlyEvents();
		setStyleName("generalInfoBackground");

	}

	private void setEventsActionHandler(GridContextMenu<MonthlyEventBean> contextMenu) {
		contextMenu.addGridBodyContextMenuListener(event -> {
			contextMenu.removeItems();
			if (event.getItem() != null) {
				contextMenu.addItem("Open", VaadinIcons.OPEN_BOOK, selectedMenuItem -> {
					openEventWindow((MonthlyEventBean) event.getItem(), false);
				});
				contextMenu.addItem("Delete", VaadinIcons.DEL, selectedMenuItem -> {
					generalInfoDAO.deleteMonthlyEvent(((MonthlyEventBean) event.getItem()).getId());
					setMonthlyEventTable();
				});
				contextMenu.addItem("Edit", VaadinIcons.EDIT, selectedMenuItem -> {
					openEventWindow((MonthlyEventBean) event.getItem(), true);
				});
				contextMenu.addItem("New", VaadinIcons.SCREWDRIVER, selectedMenuItem -> {
					openEventWindow(null, true);
				});
			}
		});
	}

	private void setMonthlyEvents() {
		addNewEventButton = new Button("Add", new AddEventButtonListener());

		addComponent(addNewEventButton);
		setComponentAlignment(addNewEventButton, Alignment.TOP_LEFT);

		monthlyEventsTable = new Grid<MonthlyEventBean>(MonthlyEventBean.class);
		monthlyEventsTable.setStyleName("infoTable");
		setMonthlyEventTable();
		GridContextMenu<MonthlyEventBean> contextMenu = new GridContextMenu<MonthlyEventBean>(monthlyEventsTable);
		setEventsActionHandler(contextMenu);
		addComponent(monthlyEventsTable);
	}

	private void openEventWindow(MonthlyEventBean eventBean, boolean enabledForEditing) {
		boolean newBean = false;
		
		if (eventBean == null) {
			eventBean = new MonthlyEventBean();
			newBean = true;
		}
		eventWindow = new Window();

		Binder<MonthlyEventBean> binder = new Binder<MonthlyEventBean>();
		

		VerticalLayout eventLayout = new VerticalLayout();
		eventLayout.setSpacing(true);
		eventLayout.setMargin(true);

		final TextField eventNameField = new TextField();
		eventNameField.setPlaceholder("enter event name");
		if (!enabledForEditing) {
			eventNameField.setReadOnly(true);
		}

		final TextField amountField = new TextField();
		amountField.setPlaceholder("enter amount");
		if (!enabledForEditing) {
			amountField.setReadOnly(true);
		}

		final TextField descriptionField = new TextField();
		descriptionField.setPlaceholder("enter description");
		if (!enabledForEditing) {
			descriptionField.setReadOnly(true);
		}

		final TextField eventCommentField = new TextField();
		eventCommentField.setPlaceholder("enter comment");
		if (!enabledForEditing) {
			eventCommentField.setReadOnly(true);
		}

		binder.forField(eventNameField).withValidator(str -> str.length() > 0, "Must be at least 2 chars")
				.bind(MonthlyEventBean::getName, MonthlyEventBean::setName);
		binder.forField(amountField).withConverter(new StringToIntegerConverter("Must be Integer"))
				.bind(MonthlyEventBean::getAmount, MonthlyEventBean::setAmount);
		binder.bind(descriptionField, MonthlyEventBean::getDescription, MonthlyEventBean::setDescription);
		binder.bind(eventCommentField, MonthlyEventBean::getComment, MonthlyEventBean::setComment);

		eventLayout.addComponent(eventNameField);
		eventLayout.addComponent(amountField);
		eventLayout.addComponent(descriptionField);
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

	private void setMonthlyEventTable() {

		ArrayList<MonthlyEventBean> allMonthlies = generalInfoDAO.getAllMonthlyEvents();
		monthlyEventsTable.setItems(allMonthlies);
		monthlyEventsTable.setColumns("name", "description", "amount", "comment");
		monthlyEventsTable.getColumn("description").setCaption("Description");
		monthlyEventsTable.getColumn("amount").setCaption("Amount");
		monthlyEventsTable.getColumn("name").setCaption("Name");
		monthlyEventsTable.getColumn("comment").setCaption("Comment");

		double totalSum = 0.0;
		for (MonthlyEventBean eventBean : allMonthlies) {
			totalSum += eventBean.getAmount();
		}
		if (monthlyEventsTable.getFooterRowCount() > 0) {
			monthlyEventsTable.removeFooterRow(0);
		}
		
		FooterRow footer = monthlyEventsTable.appendFooterRow();
		FooterCell totalCell = footer.join("name", "description");
		FooterCell amountCell = footer.join("amount", "comment");
			
		totalCell.setText("Total");
		amountCell.setText(totalSum + "");

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

		public EventWindowSubmitListener(MonthlyEventBean bean, boolean newBean, Binder<MonthlyEventBean> binder) {
			this.bean = bean;
			this.newBean = newBean;
			this.binder = binder;
		}

		private MonthlyEventBean bean;
		private boolean newBean;
		private Binder<MonthlyEventBean> binder;

		@Override
		public void buttonClick(ClickEvent event) {
			try {
				binder.writeBean(bean);
			} catch (ValidationException e) {
				Notification.show("Sth is wrong with the fields: " + e.getMessage(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}
			if (newBean) {
				generalInfoDAO.addNewMonthlyEvent(bean);
			} else {
				generalInfoDAO.updateMonthlyEvent(bean);
			}
			setMonthlyEventTable();
			eventWindow.close();
		}
	}
}
