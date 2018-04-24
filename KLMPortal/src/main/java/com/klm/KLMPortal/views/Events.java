package com.klm.KLMPortal.views;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class Events extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private HorizontalLayout eventsButtonLayout = new HorizontalLayout();

	private EventPost postEventsLayout;
	private Button postEventsButton;

	private EventToDo toDoEventsLayout;
	private Button toDoEventsButton;

	private EventGeneral generalEventsLayout;
	private Button generalEventsButton;

	private EventMonthly monthlyEventsLayout;
	private Button monthlyEventsButton;

	private enum EvenType {
		POST, TODO, GENERAL, MONTHLY;
	}

	public Events() {
		buildMainLayout();
	}

	private void buildMainLayout() {
		setSizeFull();
		setEventLayout();

		setStyleName("generalInfoBackground");

	}

	private void setEventLayout() {
//		setMargin(true);
		setSpacing(true);

		eventsButtonLayout.setSpacing(true);
//		eventsButtonLayout.setMargin(true);
		postEventsButton = new Button("POST", new EventButtonListener(EvenType.POST));
		eventsButtonLayout.addComponent(postEventsButton);

		toDoEventsButton = new Button("TODO", new EventButtonListener(EvenType.TODO));
		eventsButtonLayout.addComponent(toDoEventsButton);

		generalEventsButton = new Button("GENERAL", new EventButtonListener(EvenType.GENERAL));
		eventsButtonLayout.addComponent(generalEventsButton);

		monthlyEventsButton = new Button("MONTHLY", new EventButtonListener(EvenType.MONTHLY));
		eventsButtonLayout.addComponent(monthlyEventsButton);

		eventsButtonLayout.setExpandRatio(postEventsButton, 1);
		eventsButtonLayout.setExpandRatio(toDoEventsButton, 1);
		eventsButtonLayout.setExpandRatio(generalEventsButton, 1);
		addComponent(eventsButtonLayout);
	}

	private void removeAllEventComponents() {
		if (toDoEventsLayout != null) {
			removeComponent(toDoEventsLayout);
		}

		if (generalEventsLayout != null) {
			removeComponent(generalEventsLayout);
		}

		if (postEventsLayout != null) {
			removeComponent(postEventsLayout);
		}

		if (monthlyEventsLayout != null) {
			removeComponent(monthlyEventsLayout);
		}
	}

	private void setPostEvents() {
		postEventsLayout = new EventPost();
		addComponent(postEventsLayout);
	}

	private void setTODOEvents() {
		toDoEventsLayout = new EventToDo();
		addComponent(toDoEventsLayout);
	}

	private void setGeneralEvents() {
		generalEventsLayout = new EventGeneral();
		addComponent(generalEventsLayout);
	}

	private void setMonthlyEvents() {
		monthlyEventsLayout = new EventMonthly();
		addComponent(monthlyEventsLayout);
	}

	private class EventButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		private EvenType eventType;

		public EventButtonListener(EvenType eventType) {
			this.eventType = eventType;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			removeAllEventComponents();

			switch (eventType) {
			case POST:
				setPostEvents();
				break;
			case TODO:
				setTODOEvents();
				break;
			case MONTHLY:
				setMonthlyEvents();
				break;
			default:
				setGeneralEvents();
				break;
			}
		}
	}

}
