package com.klm.KLMPortal.views;

import com.vaadin.event.Action;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.ItemClick;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.ItemClickListener;

public class KlmTable<T> extends CustomComponent implements Action.Handler, ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	Grid<T> table;

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

		table = new Grid<T>(caption);
//		table.setSelectable(true);
//		table.setImmediate(true);
//		table.addActionHandler(this);
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

	public Grid<T> getTable() {
		return table;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		final Button b = event.getButton();
		Object item = table.getSelectedItems().iterator().next();
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
			Object item = table.getSelectedItems().iterator().next();
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

	public void open(Object target){
		//do sth
	}

	public void delete(Object target){
		//do sth
	}

	public void edit(Object target){
		//do sth
	}

	public void createNew(){
		//do sth
	}
	

	private class TableSelectListener implements ItemClickListener<T> {

		@Override
		public void itemClick(ItemClick<T> event) {
			if (event.getMouseEventDetails().isDoubleClick()) {
				open(event.getItem());
			}
			
		}
		
	}
}
