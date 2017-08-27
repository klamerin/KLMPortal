package com.klm.KLMPortal.views;

import com.klm.KLMPortal.beans.InfoBean;
import com.klm.KLMPortal.data.DAOFactory;
import com.klm.KLMPortal.data.DAO.IGeneralInfoDAO;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class Info extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DAOFactory mysqlDAOFactory = DAOFactory.getMYSQLFactory();
	private IGeneralInfoDAO generalInfoDAO = mysqlDAOFactory.getGeneralInfoDAO();

	private Grid<InfoBean> infoTable;
	private Button addNewInfoButton;
	private Window infoWindow;

	public Info() {
		buildMainLayout();
	}

	private void buildMainLayout() {
		setSpacing(true);
		setMargin(true);
		setSizeFull();
		setInfoLayout();
		setStyleName("generalInfoBackground");
	}

	private void setInfoLayout() {
		addNewInfoButton = new Button("Add Info", new AddInfoButtonListener());
		addComponent(addNewInfoButton);
		setComponentAlignment(addNewInfoButton, Alignment.TOP_LEFT);

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
		addComponent(infoTable);
		setComponentAlignment(infoTable, Alignment.TOP_RIGHT);
	}

	private void openInfoWindow(final InfoBean info, boolean enabledForEditing) {
		infoWindow = new Window();

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

		HorizontalLayout buttonsLayout = new HorizontalLayout();
		
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
					infoWindow.close();
				}
			});
			buttonsLayout.addComponent(newInfoSubmitButton);
		}
		Button closeButton = new Button("Close", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				infoWindow.close();

			}
		});
		buttonsLayout.addComponent(closeButton);
		newInfoLayout.addComponent(buttonsLayout);
		infoWindow.setContent(newInfoLayout);
		infoWindow.setWidth("25%");
		infoWindow.setHeight("35%");
		infoWindow.center();
		getUI().addWindow(infoWindow);
	}


	private void refreshInfoTable() {
		infoTable.setItems(generalInfoDAO.getAllInfos());
		infoTable.setColumns("infoKey", "infoValue", "comment");
		infoTable.getColumn("infoKey").setCaption("Info Key");
		infoTable.getColumn("infoValue").setCaption("Info Value");
		infoTable.getColumn("comment").setCaption("Comment");
	}

	private class AddInfoButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			openInfoWindow(null, true);
		}
	}

}
