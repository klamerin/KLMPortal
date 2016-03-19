package com.klm.KLMPortal.views;

import com.vaadin.data.Item;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class KlmTable extends CustomComponent implements Action.Handler, ClickListener {

	private static final Action ACTION_DELETE = new Action("Delete");
	private static final Action ACTION_OPEN = new Action("Open");
	private static final Action ACTION_NEW = new Action("New");
	private static final Action ACTION_EDIT = new Action("Edit");
	// Action sets
	private static final Action[] ACTIONS = new Action[] { ACTION_DELETE, ACTION_OPEN, ACTION_NEW, ACTION_EDIT };

	// Button saveSelectedButton;
	Button deleteSelectedButton;
	Button openSelectedButton;
	Button newSelectedButton;
	Button editSelectedButton;

	Table table;

	public KlmTable(String caption) {
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		setCompositionRoot(mainLayout);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(new MarginInfo(false, false, true, false));

		newSelectedButton = new Button("New", this);
		buttonLayout.addComponent(newSelectedButton);

		deleteSelectedButton = new Button("Delete", this);
		buttonLayout.addComponent(deleteSelectedButton);

		openSelectedButton = new Button("Open", this);
		buttonLayout.addComponent(openSelectedButton);

		editSelectedButton = new Button("Edit", this);
		buttonLayout.addComponent(editSelectedButton);
		mainLayout.addComponent(buttonLayout);

		table = new Table(caption);
		table.setSelectable(true);
		table.setImmediate(true);
		table.addActionHandler(this);
		mainLayout.addComponent(table);
	}

	public Button getDeleteSelectedButton() {
		return deleteSelectedButton;
	}

	public Button getOpenSelectedButton() {
		return openSelectedButton;
	}

	public Button getNewSelectedButton() {
		return newSelectedButton;
	}

	public Button getEditSelectedButton() {
		return editSelectedButton;
	}

	public Table getTable() {
		return table;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		final Button b = event.getButton();
		Object selectedId = table.getValue();
		Item item = table.getItem(selectedId);
		if (b == openSelectedButton) {
			open(item);
		} else if (b == editSelectedButton) {
			edit(item);
		} else if (b == deleteSelectedButton) {
			delete(item);
		} else if (b == newSelectedButton) {
			createNew();
		}
	}

	@Override
	public Action[] getActions(Object target, Object sender) {
		return ACTIONS;
	}

	@Override
	public void handleAction(Action action, Object sender, Object target) {
		if (sender == table) {
			Item item = table.getItem(target);
			if (action == ACTION_OPEN) {
				open(item);
			} else if (action == ACTION_DELETE) {
				delete(item);
			} else if (action == ACTION_EDIT) {
				edit(item);
			} else if (action == ACTION_NEW) {
				createNew();
			}
		}

	}

	public void open(Item target){
		//do sth
	}

	public void delete(Item target){
		//do sth
	}

	public void edit(Item target){
		//do sth
	}

	public void createNew(){
		//do sth
	}
	

	private class TableSelectListener implements ItemClickListener {

		@Override
		public void itemClick(ItemClickEvent event) {
			if (event.isDoubleClick()) {
				open(event.getItem());
			}
			
		}
		
	}
}
