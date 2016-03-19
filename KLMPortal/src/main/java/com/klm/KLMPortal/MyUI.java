package com.klm.KLMPortal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.annotation.WebServlet;

import com.klm.KLMPortal.views.GeneralInfo;
import com.klm.KLMPortal.views.Movies;
import com.klm.KLMPortal.views.StartView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.klm.KLMPortal.MyAppWidgetset")
public class MyUI extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Navigator nav;
	protected static final String FILMSVIEW = "films";
	private static final String GEN_INFO = "generalInfo";

	@PersistenceContext(name="persistence/em",unitName="KLMPortal")
	private EntityManager entityManager;
	
	@Override
	protected void init(VaadinRequest vaadinRequest) {
		getPage().setTitle("KLM Portal");
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

//		Button button = new Button("Click Me");
//		button.addClickListener(new Button.ClickListener() {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				layout.addComponent(new Label("Thank you for clicking"));
//			}
//		});
//		layout.addComponent(button);

		// Create a navigator to control the views
		nav = new Navigator(this, this);

		// Create and register the views
		nav.addView("", new StartView());
		if (entityManager == null) {
			System.out.println("entityManager is null even at UI");
		} else {
			System.out.println("entityManager is not null at UI");
		}
		nav.addView(FILMSVIEW, new Movies(entityManager));
		nav.addView(GEN_INFO, new GeneralInfo());

	}

	private void buildMainLayout() {

	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	}
}
