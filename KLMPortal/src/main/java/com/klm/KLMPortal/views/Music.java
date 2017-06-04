package com.klm.KLMPortal.views;

import java.util.Arrays;

import com.klm.KLMPortal.beans.MusicBean;
import com.klm.KLMPortal.data.DAOFactory;
import com.klm.KLMPortal.data.DAO.IMusicDAO;
//import com.vaadin.data.Property;
//import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.PopupDateField;
//import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

//import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
//import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
//import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
//import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;

public class Music extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	private DAOFactory mysqlDAOFactory = DAOFactory.getMYSQLFactory();
	private IMusicDAO musicDAO = mysqlDAOFactory.getMusicDAO();

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | -------------G U I — F I E L D S----------------|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	private Navigator nav = UI.getCurrent().getNavigator();
	private HorizontalLayout navLayout = new HorizontalLayout();
	private HorizontalLayout musicLayout = new HorizontalLayout();
	private HorizontalLayout buttonsLayout = new HorizontalLayout();
	private HorizontalLayout sessionsLayout = new HorizontalLayout();
	private static final String GEN_INFO = "generalInfo";
	private static final String FILMSVIEW = "films";

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | -------B U T T O N S — G U I — F I E L D S------|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	private Grid<MusicBean> sessionsTable;
	private Button addListeningSessionButton;
	private Window listeningSessionWindow;
	private FormLayout listeningSessionWindowLayout;
	private TextField bandNameTextField;
	private TextField albumNameTextField;
	private ComboBox<String> portionNameComboBox;
	private TextField commentTextField;
	private DateField dateField;
	private Button submitSessionButton;

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | ------------D A T A — F I E L D S---------------|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	public Music() {
		buildMainLayout();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	private void buildMainLayout() {
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		setMainLayout();
	}

	private void setMainLayout() {

		// setIcon(new ThemeResource("icons/movies-main.jpg"));
		setStyleName("musicMainBackground");

		setNavLayout();

		addComponent(musicLayout);
		setExpandRatio(navLayout, 2);
		setExpandRatio(musicLayout, 10);
		init();
	}

	private void setNavLayout() {
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

		Button genInfoButton = new Button("General Info View", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				nav.navigateTo(GEN_INFO);
			}
		});
		navLayout.addComponent(genInfoButton);
		navLayout.setComponentAlignment(genInfoButton, Alignment.TOP_CENTER);

		addComponent(navLayout);
		setComponentAlignment(navLayout, Alignment.TOP_CENTER);
	}

	private void setMusicPopupWindow() {
		listeningSessionWindow = new Window();
		listeningSessionWindow.setStyleName("musicWindowBackground");
		listeningSessionWindow.setWidth("30%");
		listeningSessionWindow.setHeight("80%");
		listeningSessionWindow.center();
		listeningSessionWindowLayout = new FormLayout();
		listeningSessionWindowLayout.setSpacing(true);
		listeningSessionWindowLayout.setMargin(true);
		// movieWindow.setModal(true);
	}

	private void init() {
		setMusicLayout();
		setSessionsTable();
		musicLayout.setWidth("100%");
		sessionsLayout.setWidth("100%");
		musicLayout.addComponent(buttonsLayout);
		musicLayout.addComponent(sessionsLayout);
		musicLayout.setExpandRatio(buttonsLayout, 1);
		musicLayout.setExpandRatio(sessionsLayout, 4);
		musicLayout.setSpacing(true);
	}

	private void setMusicLayout() {
		addListeningSessionButton = new Button("Add session");
		addListeningSessionButton.addClickListener(new AddListeningSessionButtonListener());
		buttonsLayout.addComponent(addListeningSessionButton);
	}

	private void setSessionsTable() {
//		BeanItemContainer<MusicBean> sessionsContainer = new BeanItemContainer<MusicBean>(MusicBean.class);
//		sessionsContainer.addAll(musicDAO.getAllListeningSessions());
		sessionsTable = new Grid<MusicBean>(MusicBean.class); /*{
			private static final long serialVersionUID = 1L;

			@Override
			protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
				Object v = property.getValue();
				if (v instanceof Timestamp) {
					Timestamp dateValue = (Timestamp) v;
					return new SimpleDateFormat("dd.MM.yyyy").format(dateValue);
				}
				return super.formatPropertyValue(rowId, colId, property);
			}

		};*/
		sessionsTable.setWidth("50%");
		sessionsTable.setStyleName("moviesTable");
		sessionsTable.setItems(musicDAO.getAllListeningSessions());
		sessionsTable.setHeightByRows(15);

		sessionsTable.setColumns("bandName", "albumName", "portion", "comment", "sessionDate");
		sessionsTable.getColumn("bandName").setCaption("Band");
		sessionsTable.getColumn("albumName").setCaption("Album");
		sessionsTable.getColumn("portion").setCaption("Portion");
		sessionsTable.getColumn("comment").setCaption("Comment");
		sessionsTable.getColumn("sessionDate").setCaption("Date");

		sessionsLayout.addComponent(sessionsTable);
	}

	private void openListeningSessionWindow() {
		setMusicPopupWindow();

//		AutocompleteSuggestionProvider bandSuggestions = new CollectionSuggestionProvider(musicDAO.getAllBands(),
//				MatchMode.CONTAINS, true);
//		AutocompleteSuggestionProvider albumsSuggestions = new CollectionSuggestionProvider(musicDAO.getAllAlbums(),
//				MatchMode.CONTAINS, true);
		bandNameTextField = new TextField("Band");
//		bandNameTextField.setSuggestionProvider(bandSuggestions);
		albumNameTextField = new TextField("Album");
//		albumNameTextField.setSuggestionProvider(albumsSuggestions);
		portionNameComboBox = new ComboBox<String>("Portion",
				Arrays.asList(new String[] { "1", "1/2", "1/3", "2/3", "1/4", "2/4", "3/4" }));
		portionNameComboBox.setValue("1");
		commentTextField = new TextField("Comment");
		dateField = new DateField("Date");
		submitSessionButton = new Button("Submit", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				musicDAO.addListeningSession(bandNameTextField.getValue(), albumNameTextField.getValue(),
						portionNameComboBox.getValue().toString(), commentTextField.getValue(),
						dateField.getValue());
				listeningSessionWindow.close();
			}
		});

		listeningSessionWindowLayout.addComponent(bandNameTextField);
		listeningSessionWindowLayout.addComponent(albumNameTextField);
		listeningSessionWindowLayout.addComponent(portionNameComboBox);
		listeningSessionWindowLayout.addComponent(commentTextField);
		listeningSessionWindowLayout.addComponent(dateField);
		listeningSessionWindowLayout.addComponent(submitSessionButton);
		listeningSessionWindow.setContent(listeningSessionWindowLayout);
		getUI().addWindow(listeningSessionWindow);

	}

	private class AddListeningSessionButtonListener implements Button.ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			openListeningSessionWindow();

		}

	}

}
