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
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setWidthUndefined();
		Button filmsButton = new Button("Go to Films View", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				nav.navigateTo(FILMSVIEW);
			}
		});
		buttonsLayout.addComponent(filmsButton);
		buttonsLayout.setComponentAlignment(filmsButton, Alignment.MIDDLE_CENTER);
		Button genInfobutton = new Button("Go to General Info View", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				nav.navigateTo(GEN_INFO);
			}
		});
		genInfobutton.setStyleName("transparentButton");
		buttonsLayout.addComponent(genInfobutton);
		buttonsLayout.setComponentAlignment(genInfobutton, Alignment.MIDDLE_CENTER);
		
		Button musicButton = new Button("Go to Music View", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				nav.navigateTo(MUSICSVIEW);
			}
		});
		buttonsLayout.addComponent(musicButton);
		buttonsLayout.setComponentAlignment(musicButton, Alignment.MIDDLE_CENTER);
		
		buttonsLayout.setSpacing(true);
		buttonsLayout.setMargin(true);
		addComponent(buttonsLayout);
		setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
