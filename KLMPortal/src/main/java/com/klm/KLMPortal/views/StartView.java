package com.klm.KLMPortal.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

public class StartView extends HorizontalLayout implements View {

	private static final long serialVersionUID = 1L;

	private static final String FILMSVIEW = "films";
	private static final String GEN_INFO = "generalInfo";
	private static final String MUSICSVIEW = "music";
	
	private Navigator nav = UI.getCurrent().getNavigator();

	public StartView() {

		setSizeFull();
		addStyleName("startViewBackground");
		Button filmsButton = new Button("Go to Films View", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				nav.navigateTo(FILMSVIEW);
			}
		});
		addComponent(filmsButton);
		setComponentAlignment(filmsButton, Alignment.MIDDLE_CENTER);
		Button genInfobutton = new Button("Go to General Info View", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				nav.navigateTo(GEN_INFO);
			}
		});
		genInfobutton.setStyleName("transparentButton");
		addComponent(genInfobutton);
		setComponentAlignment(genInfobutton, Alignment.MIDDLE_CENTER);
		
		Button musicButton = new Button("Music", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				nav.navigateTo(MUSICSVIEW);
			}
		});
		addComponent(musicButton);
		setComponentAlignment(musicButton, Alignment.MIDDLE_CENTER);
		
		setSpacing(true);
		setMargin(true);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
