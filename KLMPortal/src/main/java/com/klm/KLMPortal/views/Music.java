package com.klm.KLMPortal.views;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.vaadin.addons.autocomplete.AutocompleteExtension;
import org.vaadin.addons.autocomplete.generator.SuggestionGenerator;
import com.klm.KLMPortal.beans.MusicBean;
import com.klm.KLMPortal.data.DAOFactory;
import com.klm.KLMPortal.data.DAO.IMusicDAO;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
//import com.vaadin.data.Property;
//import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
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
import com.vaadin.ui.components.grid.SingleSelectionModel;

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
	private CheckBox conceptCheckBox;
	private Button submitSessionButton;

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | ------------D A T A — F I E L D S---------------|
	 * 
	 * —————————————————————————————————————————————————/
	 */
	
	
	Collection<String> albumNameAutoCompleteSuggestionList = new ArrayList<String>();
	Collection<String> bandNameAutoCompleteSuggestionList = new ArrayList<String>();
	
	

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
		listeningSessionWindow.setModal(true);
	}
	
	private void setAlbumAutoCompleteSuggestions(String band) {
		if(band != null) {
			albumNameAutoCompleteSuggestionList = musicDAO.getAllAlbumsForBand(band);
		} else {
			albumNameAutoCompleteSuggestionList = musicDAO.getAllAlbums();
		}
	}
	
	private void setBandAutoCompleteSuggestions(String album) {
		if(album != null) {
			bandNameAutoCompleteSuggestionList = musicDAO.getAllBandsForAlbum(album);
		} else {
			bandNameAutoCompleteSuggestionList = musicDAO.getAllBands();
		}
	}

	private void init() {
		setAlbumAutoCompleteSuggestions(null);
		setBandAutoCompleteSuggestions(null);
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
		sessionsTable = new Grid<MusicBean>(MusicBean.class);
		sessionsTable.setWidth("70%");
		sessionsTable.setStyleName("moviesTable");
		sessionsTable.setItems(musicDAO.getAllListeningSessions());
		sessionsTable.setHeightByRows(15);
		
		SingleSelectionModel<MusicBean> selectionModel = (SingleSelectionModel<MusicBean>) sessionsTable.getSelectionModel();
		selectionModel.addSingleSelectionListener(new SessionsTableSelectionListener());
		
		sessionsTable.setColumns("bandName", "albumName", "portion", "comment", "concept", "sessionDate");
		sessionsTable.getColumn("bandName").setCaption("Band");
		sessionsTable.getColumn("albumName").setCaption("Album");
		sessionsTable.getColumn("portion").setCaption("Portion");
		sessionsTable.getColumn("comment").setCaption("Comment");
		sessionsTable.getColumn("concept").setCaption("Concept?");
		sessionsTable.getColumn("sessionDate").setCaption("Date");

		sessionsLayout.addComponent(sessionsTable);
	}

	private void openListeningSessionWindow(MusicBean listeningSession) {
		setMusicPopupWindow();

//		AutocompleteSuggestionProvider bandSuggestions = new CollectionSuggestionProvider(musicDAO.getAllBands(),
//				MatchMode.CONTAINS, true);
//		AutocompleteSuggestionProvider albumsSuggestions = new CollectionSuggestionProvider(musicDAO.getAllAlbums(),
//				MatchMode.CONTAINS, true);
		bandNameTextField = new TextField ("Band");
//		bandNameTextField.setWidth("50%");
		bandNameTextField.setValueChangeMode(ValueChangeMode.BLUR);
		bandNameTextField.addValueChangeListener(event -> {
		    String band = event.getValue();
		    albumNameAutoCompleteSuggestionList = musicDAO.getAllAlbumsForBand(band);
		});
		  AutocompleteExtension<String> bandExtension = new AutocompleteExtension<>(
		    		bandNameTextField);
		  bandExtension.setSuggestionGenerator(new SuggestionGenerator<String>() {
				
				@Override
				public List<String> apply(String query, Integer limit) {
					return new ArrayList<String>(bandNameAutoCompleteSuggestionList);
				}
			});

		    
		    
		albumNameTextField = new TextField ("Album");
//		albumNameTextField.setWidth("50%");
		albumNameTextField.setValueChangeMode(ValueChangeMode.BLUR);
		albumNameTextField.addValueChangeListener(event -> {
		    String album = event.getValue();
		    bandNameAutoCompleteSuggestionList = musicDAO.getAllAlbumsForBand(album);
		});
	    AutocompleteExtension<String> albumExtension = new AutocompleteExtension<>(
	    		albumNameTextField);
	    albumExtension.setSuggestionGenerator(new SuggestionGenerator<String>() {
			
			@Override
			public List<String> apply(String query, Integer limit) {
				return new ArrayList<String>(albumNameAutoCompleteSuggestionList);
			}
		});
	    
	  
		
		commentTextField = new TextField("Comment");
		commentTextField.setWidth("58%");
		HorizontalLayout l = new HorizontalLayout();
		portionNameComboBox = new ComboBox<String>("Portion",
				Arrays.asList(new String[] { "1", "1/2", "1/3", "2/3", "1/4", "2/4", "3/4" }));
		portionNameComboBox.setValue("1");
		dateField = new DateField("Date");
		conceptCheckBox = new CheckBox("Concept?");
		conceptCheckBox.setValue(true);
		
		l.addComponent(dateField);
		l.addComponent(portionNameComboBox);
		
		if (listeningSession != null) {
			bandNameTextField.setValue(listeningSession.getBandName() != null ? listeningSession.getBandName() : "");
			albumNameTextField.setValue(listeningSession.getAlbumName() != null ? listeningSession.getAlbumName() : "");
			portionNameComboBox.setValue(listeningSession.getPortion() != null ? listeningSession.getPortion() : "1");
			commentTextField.setValue(listeningSession.getComment() != null ? listeningSession.getComment() : "");
			dateField.setValue(listeningSession.getSessionDate() != null ? listeningSession.getSessionDate() : LocalDate.now());
			conceptCheckBox.setValue(listeningSession.getConcept() != null ? listeningSession.getConcept() : true);
		}
		
		submitSessionButton = new Button("Submit", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if (listeningSession != null) {
					musicDAO.editListeningSession(bandNameTextField.getValue(), albumNameTextField.getValue(),
							portionNameComboBox.getValue().toString(), commentTextField.getValue(),
							dateField.getValue(), conceptCheckBox.getValue(), listeningSession.getId());
				} else {
				musicDAO.addListeningSession(bandNameTextField.getValue(), albumNameTextField.getValue(),
						portionNameComboBox.getValue().toString(), commentTextField.getValue(),
						dateField.getValue(), conceptCheckBox.getValue());
				}
				listeningSessionWindow.close();
			}
		});

		listeningSessionWindowLayout.addComponent(bandNameTextField);
		listeningSessionWindowLayout.addComponent(albumNameTextField);
//		listeningSessionWindowLayout.addComponent(portionNameComboBox);
		listeningSessionWindowLayout.addComponent(commentTextField);
		listeningSessionWindowLayout.addComponent(l);
		listeningSessionWindowLayout.addComponent(conceptCheckBox);
		listeningSessionWindowLayout.addComponent(submitSessionButton);
		listeningSessionWindow.setContent(listeningSessionWindowLayout);
		getUI().addWindow(listeningSessionWindow);

	}
	
	
	@SuppressWarnings("unused")
	private void editListeningSessionWindow(MusicBean listeningSession) {
		
		Window editSessionWindow = new Window();
		editSessionWindow.setModal(true);
		
		
		getUI().addWindow(editSessionWindow);
		
	}

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | --------------L I S T E N E R S-----------------|
	 * 
	 * —————————————————————————————————————————————————/
	 */
	
	
	private class SessionsTableSelectionListener implements SingleSelectionListener<MusicBean> {

		private static final long serialVersionUID = 1L;

		@Override
		public void selectionChange(SingleSelectionEvent<MusicBean> event) {
			if (event.getSelectedItem().isPresent()) {
				MusicBean listeningSession = event.getSelectedItem().get();
				if (listeningSession != null) {
					openListeningSessionWindow(listeningSession);
				}
			}
			
		}
		
	}

	private class AddListeningSessionButtonListener implements Button.ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			openListeningSessionWindow(null);

		}

	}

}
