package com.klm.KLMPortal.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class GeneralInfo extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Navigator nav = UI.getCurrent().getNavigator();
	private HorizontalLayout navLayout = new HorizontalLayout();
	private static final String FILMSVIEW = "films";
	protected static final String MUSICSVIEW = "music";

	private HorizontalLayout infoLayout = new HorizontalLayout();
	private VerticalLayout eventLayout = new VerticalLayout();
	private HorizontalSplitPanel panel = new HorizontalSplitPanel(infoLayout, eventLayout);

	private Events eventView;
	private Info infoView;


	public GeneralInfo() {
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
		
		infoView = new Info();
		infoLayout.addComponent(infoView);
		infoLayout.setComponentAlignment(infoView, Alignment.TOP_RIGHT);
	}
	
	private void setEventLayout() {
		eventLayout.setMargin(true);
		eventLayout.setSpacing(true);

		eventView = new Events();
		eventLayout.addComponent(eventView);
		infoLayout.setComponentAlignment(eventView, Alignment.TOP_RIGHT);
	}


	@Override
	public void enter(ViewChangeEvent event) {
		Notification.show("HIIII, GEN MEN=)");
	}
}
