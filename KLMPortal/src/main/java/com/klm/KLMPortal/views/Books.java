package com.klm.KLMPortal.views;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.klm.KLMPortal.beans.MovieBean;
import com.klm.KLMPortal.data.DAOFactory;
import com.klm.KLMPortal.data.DAO.IMovieDAO;
import com.klm.KLMPortal.entities.Film;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import info.bliki.api.Page;
import info.bliki.api.PageInfo;
import info.bliki.api.User;
import info.bliki.wiki.model.WikiModel;

public class Books extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	private DAOFactory mssqlDAOFactory = DAOFactory.getFactory();
	private IMovieDAO portalDAO = mssqlDAOFactory.getPortalDAO();

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | -------------G U I — F I E L D S----------------|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	private Navigator nav = UI.getCurrent().getNavigator();
	private HorizontalLayout navLayout = new HorizontalLayout();
	private HorizontalLayout moviesLayout = new HorizontalLayout();
	private static final String GEN_INFO = "generalInfo";

	private Table moviesTable;
	private MovieItemClickListener movieItemClickListener = new MovieItemClickListener();
	private VerticalLayout buttonsLayout;
	private VerticalLayout tableLayout;

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | -------B U T T O N S — G U I — F I E L D S------|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	private Button getAllMoviesButton;
	private Button getWatchedMoviesButton;
	private Button getToWatchMoviesButton;
	private Button getRecommendedMoviesButton;
	private Button addMovieButton;

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | --P O P U P — W I N D O W — G U I — F I E L D S-|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	private Window movieWindow;
	private FormLayout movieWindowLayout;
	private TextField movieNameTextField;
	private CheckBox movieWatchedCheckBox;
	private ComboBox movieRatingComboBox;
	private CheckBox movieRecommendedCheckBox;
	private TextField movieCommentField;
	private PopupDateField movieDateField;
	private TextField movieWatchedBecauseTextField;
	private ComboBox movieSadnessLevelComboBox;
	private ComboBox movieDesireLevelComboBox;
	private CheckBox movieRewatchNeededCheckBox;
	private Button watchCountButton;
	private HorizontalLayout rewatchLayout;
	private HorizontalLayout movieWindowButtonsLayout;
	private Button editButton;
	private Button submitButton;
	private Button deleteButton;
	private ComboBox searchMovieComboBox;
	
	
//	private HorizontalLayout toBuyLayout;
//	private Button toBuyButton;
//	private Table toBuyTable;
//	toBuyButton = new Button("TOBUY", new EventButtonListener(EvenType.TOBUY.type));
//	eventsButtonLayout.addComponent(toBuyButton);
//	private void setToBuyEvents() {
//		if (postEventsLayout != null) {
//			eventLayout.removeComponent(postEventsLayout);
//		}
//
//		if (toDoEventsLayout != null) {
//			eventLayout.removeComponent(toDoEventsLayout);
//		}
//
//		if (generalEventsLayout != null) {
//			eventLayout.removeComponent(generalEventsLayout);
//		}
//		
//		Button addNewEventButton = new Button("Add TO_BUY", new AddEventButtonListener());
//
//		toBuyLayout = new HorizontalLayout();
//		toBuyLayout.setSpacing(true);
//		toBuyLayout.setMargin(true);
//		toBuyLayout.addComponent(addNewEventButton);
//		toBuyLayout.setComponentAlignment(addNewEventButton, Alignment.TOP_LEFT);
//
//		toBuyTable = new Table() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//	        protected String formatPropertyValue(Object rowId, Object colId, Property< ? > property)
//	        {
//	            Object v = property.getValue();
//	            if (v instanceof Date)
//	            {
//	            	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//	            	Date dateValue = (Date)v;
//	                return df.format(dateValue);
//	            }
//	            return super.formatPropertyValue(rowId, colId, property);
//	        }
//		};
//		setToBuyTable();
//		toBuyTable.addActionHandler(new Handler() {
//			private static final long serialVersionUID = 1L;
//			private final Action ACTION_DELETE = new Action("Delete");
//			private final Action ACTION_OPEN = new Action("Open");
//			private final Action ACTION_NEW = new Action("New");
//			private final Action ACTION_EDIT = new Action("Edit");
//			// Action sets
//			private final Action[] ACTIONS = new Action[] { ACTION_DELETE, ACTION_OPEN, ACTION_NEW, ACTION_EDIT };
//
//			@Override
//			public void handleAction(Action action, Object sender, Object target) {
//				if (action == ACTION_OPEN) {
//					openEventWindow((EventBean) toBuyTable.getValue(), false);
//				} else if (action == ACTION_DELETE) {
//					generalInfoDAO.deleteEvent(((EventBean) toBuyTable.getValue()).getId());
//					refreshEventTable();
//				} else if (action == ACTION_EDIT) {
//					openEventWindow((EventBean) toBuyTable.getValue(), true);
//				} else if (action == ACTION_NEW) {
//					openEventWindow(null, true);
//				}
//			}
//
//			@Override
//			public Action[] getActions(Object target, Object sender) {
//				return ACTIONS;
//			}
//		});
//		toBuyTable.setSelectable(true);
//		toBuyLayout.addComponent(toBuyTable);
//		eventLayout.addComponent(toBuyLayout);
//	}

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | ------------D A T A — F I E L D S---------------|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	boolean editFlag = false;

	@SuppressWarnings("unused")
	private Button JPAButton;
	private Table JPATable;
	// EntityManager em =
	// JPAContainerFactory.createEntityManagerForPersistenceUnit("KLMPortal");
	// JPAContainer<Film> cont = JPAContainerFactory.make(Film.class, em);
	private JPAContainer<Film> cont;

	private enum TableType {
		ALL, WATCHED, RECOMMENDED, TO_WATCH;
	}

	private TableType tableType = TableType.ALL;

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | -----------C O N S T R U C T O R S--------------|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	public Books() {
		buildMainLayout();
	}

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | ------------M A I N — M E T H O D S-------------|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	@Override
	public void enter(ViewChangeEvent event) {
		// Notification.show("Welcome to the Animal Farm REALITY");
	}

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | --------M A I N — G U I — M E T H O D S-----------|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	private void buildMainLayout() {
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		setMainLayout();

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

	private void setMainLayout() {

		setNavLayout();

		setButtonsLayout();

		setTableLayout();

		addComponent(moviesLayout);
		setExpandRatio(navLayout, 2);
		setExpandRatio(moviesLayout, 10);
	}

	private void setButtonsLayout() {
		buttonsLayout = new VerticalLayout();
		buttonsLayout.setSizeFull();

		getAllMoviesButton = new Button("All", new MoviesButtonListener(TableType.ALL));
		buttonsLayout.addComponent(getAllMoviesButton);
		buttonsLayout.setComponentAlignment(getAllMoviesButton, Alignment.TOP_LEFT);

		getWatchedMoviesButton = new Button("Watched", new MoviesButtonListener(TableType.WATCHED));
		buttonsLayout.addComponent(getWatchedMoviesButton);
		buttonsLayout.setComponentAlignment(getWatchedMoviesButton, Alignment.TOP_LEFT);

		getToWatchMoviesButton = new Button("To Watch", new MoviesButtonListener(TableType.TO_WATCH));
		buttonsLayout.addComponent(getToWatchMoviesButton);
		buttonsLayout.setComponentAlignment(getToWatchMoviesButton, Alignment.TOP_LEFT);

		getRecommendedMoviesButton = new Button("Recommended", new MoviesButtonListener(TableType.RECOMMENDED));
		buttonsLayout.addComponent(getRecommendedMoviesButton);
		buttonsLayout.setComponentAlignment(getRecommendedMoviesButton, Alignment.TOP_LEFT);

		addMovieButton = new Button("Add", new AddMovieButtonListener());
		buttonsLayout.addComponent(addMovieButton);
		buttonsLayout.setComponentAlignment(addMovieButton, Alignment.TOP_LEFT);

		setSearchMovieComboBox();

		setExperimentalButtons();

		moviesLayout.addComponent(buttonsLayout);
		moviesLayout.setExpandRatio(buttonsLayout, 1.0f);
	}

	private void setTableLayout() {
		tableLayout = new VerticalLayout();
		tableLayout.setSpacing(true);
		moviesTable = new Table("Movies");
		moviesTable.setSelectable(true);
		moviesTable.setNullSelectionAllowed(false);
		moviesTable.addItemClickListener(movieItemClickListener);
		tableLayout.addComponent(moviesTable);
		moviesLayout.addComponent(tableLayout);
		moviesLayout.setExpandRatio(tableLayout, 5.0f);
	}

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | -------G U I — H E L P E R — M E T H O D S------|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	private void openMoviePopupWindow(boolean newMovie, boolean watched, MovieBean movie) {

		setMoviePopupWindow(movie);

		if (!newMovie) {
			watched = movie.isWatched();
		}
		setMovieNameTextField(newMovie, movie);

		setMovieCommentField(newMovie, movie);

		setMovieWatchedCheckBox(newMovie, movie);

		setMovieRatingComboBox(newMovie, watched, movie);

		setMovieRecommendedCheckBox(newMovie, watched, movie);

		setMovieDateField(newMovie, watched, movie);

		setMovieSadnessLevelComboBox(newMovie, watched, movie);

		setMovieWatchedBecauseTextField(newMovie, movie);

		setMovieDesireLevelComboBox(newMovie, watched, movie);

		setRewatchLayout(newMovie, watched, movie);

		setMovieWindowButtonsLayout(newMovie, movie);

		if (movie != null) {
			movieWindowLayout.addComponent(new Button("Wiki", new WikiAPIListener(movie.getName())));
		}

		movieWatchedCheckBox.addValueChangeListener(new MovieWatchedCheckBoxListener());
		movieWindow.setContent(movieWindowLayout);
		getUI().addWindow(movieWindow);
		movieWindow.center();
	}

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | -------------G U I — S E T T E R S--------------|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	private void setExperimentalButtons() {
		buttonsLayout.addComponent(new Button("JPA", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				JPATable = new Table("JPA", cont);
				tableLayout.removeAllComponents();
				tableLayout.addComponent(JPATable);
			}
		}));

//		Button wikiButton = new Button("Wiki", new WikiAPIListener());
		// buttonsLayout.addComponent(wikiButton);
		// buttonsLayout.setComponentAlignment(wikiButton,
		// Alignment.BOTTOM_RIGHT);
	}

	private void setSearchMovieComboBox() {
		searchMovieComboBox = new ComboBox("Search");
		BeanItemContainer<MovieBean> moviesContainer = new BeanItemContainer<>(MovieBean.class,
				portalDAO.getAllMovies());
		searchMovieComboBox.setContainerDataSource(moviesContainer);
		searchMovieComboBox.setFilteringMode(FilteringMode.CONTAINS);
		searchMovieComboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		searchMovieComboBox.setItemCaptionPropertyId("name");
		searchMovieComboBox.addValueChangeListener(new SeachMovieComboBoxListener());
		buttonsLayout.addComponent(searchMovieComboBox);
		buttonsLayout.setComponentAlignment(searchMovieComboBox, Alignment.TOP_LEFT);
	}

	private void setMovieWindowButtonsLayout(boolean newMovie, MovieBean movie) {
		movieWindowButtonsLayout = new HorizontalLayout();
		movieWindowButtonsLayout.setMargin(true);
		movieWindowButtonsLayout.setSpacing(true);

		editButton = new Button("Edit", new EditMovieButtonListener());
		movieWindowButtonsLayout.addComponent(editButton);

		submitButton = new Button("Submit", new SubmitMovieButtonListener(movie != null ? movie.getId() : null));
		movieWindowButtonsLayout.addComponent(submitButton);

		deleteButton = new Button("Delete", new DeleteMovieButtonListener(movie != null ? movie.getId() : null));
		movieWindowButtonsLayout.addComponent(deleteButton);

		Button closeButton = new Button("Close", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				movieWindow.close();

			}
		});
		movieWindowButtonsLayout.addComponent(closeButton);

		if (newMovie) {
			editButton.setVisible(false);
			deleteButton.setVisible(false);
		} else {
			submitButton.setVisible(false);
		}
		movieWindowLayout.addComponent(movieWindowButtonsLayout);
	}

	private void setMovieWatchedBecauseTextField(boolean newMovie, MovieBean movie) {
		movieWatchedBecauseTextField = new TextField("Watch Because");
		if (newMovie) {
			movieWatchedBecauseTextField.setInputPrompt("watch why?");
		} else {
			movieWatchedBecauseTextField.setValue(movie.getWatchedBecause());
			movieWatchedBecauseTextField.setReadOnly(true);
		}
		movieWatchedBecauseTextField.setNullRepresentation("");
		movieWindowLayout.addComponent(movieWatchedBecauseTextField);
	}

	private void setMovieSadnessLevelComboBox(boolean newMovie, boolean watched, MovieBean movie) {
		movieSadnessLevelComboBox = new ComboBox("Sadness Level");
		movieSadnessLevelComboBox.addItems(Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5,
				7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0));
		movieSadnessLevelComboBox.setInputPrompt("sadness level?");
		if (newMovie) {
			movieSadnessLevelComboBox.setVisible(false);
		} else if (watched) {
			movieSadnessLevelComboBox.setVisible(true);
			movieSadnessLevelComboBox.setValue(movie.getSadnessLevel());
			movieSadnessLevelComboBox.setReadOnly(true);
		} else {
			movieSadnessLevelComboBox.setVisible(false);
			movieSadnessLevelComboBox.setReadOnly(true);
		}
		movieWindowLayout.addComponent(movieSadnessLevelComboBox);
	}

	private void setMovieDesireLevelComboBox(boolean newMovie, boolean watched, MovieBean movie) {
		movieDesireLevelComboBox = new ComboBox("Desire Level");
		movieDesireLevelComboBox.addItems(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
		movieDesireLevelComboBox.setInputPrompt("desire level?");
		if (newMovie) {
			movieDesireLevelComboBox.setVisible(true);
			movieDesireLevelComboBox.setReadOnly(false);
		} else if (watched) {
			movieDesireLevelComboBox.setVisible(false);
		} else {
			movieDesireLevelComboBox.setVisible(true);
			movieDesireLevelComboBox.setValue(movie.getDesireLevel());
			movieDesireLevelComboBox.setReadOnly(false);
		}
		movieWindowLayout.addComponent(movieDesireLevelComboBox);
	}

	private void setMovieDateField(boolean newMovie, boolean watched, MovieBean movie) {
		movieDateField = new PopupDateField("Date");
		movieDateField.setVisible(false);
		if (newMovie) {
			movieDateField.setInputPrompt("watched when?");
		} else if (watched) {
			movieDateField.setValue(movie.getWatchedDate());
			movieDateField.setVisible(true);
			movieDateField.setReadOnly(true);
		}
		movieWindowLayout.addComponent(movieDateField);
	}

	private void setMovieRecommendedCheckBox(boolean newMovie, boolean watched, MovieBean movie) {
		movieRecommendedCheckBox = new CheckBox("recommend?");
		if (newMovie) {
			movieRecommendedCheckBox.setVisible(false);
		} else if (watched) {
			movieRecommendedCheckBox.setValue(movie.isRecommend());
			movieRecommendedCheckBox.setReadOnly(true);
			movieRecommendedCheckBox.setVisible(true);
		} else {
			movieRecommendedCheckBox.setVisible(false);
		}
		movieWindowLayout.addComponent(movieRecommendedCheckBox);
	}

	private void setMovieRatingComboBox(boolean newMovie, boolean watched, MovieBean movie) {
		movieRatingComboBox = new ComboBox("Rating");
		movieRatingComboBox.addItems(Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5,
				8.0, 8.5, 9.0, 9.5, 10.0));
		movieRatingComboBox.setInputPrompt("rating?");
		if (newMovie) {
			movieRatingComboBox.setVisible(false);
		} else if (watched) {
			movieRatingComboBox.setValue(movie.getRating().doubleValue());
			movieRatingComboBox.setReadOnly(true);
		} else {
			movieRatingComboBox.setVisible(false);
		}
		movieWindowLayout.addComponent(movieRatingComboBox);
	}

	private void setMovieWatchedCheckBox(boolean newMovie, MovieBean movie) {
		movieWatchedCheckBox = new CheckBox("Watched?");

		movieWatchedCheckBox.setImmediate(true);
		if (!newMovie) {
			movieWatchedCheckBox.setValue(movie.isWatched());
			movieWatchedCheckBox.setReadOnly(true);
		}
		movieWindowLayout.addComponent(movieWatchedCheckBox);
	}

	private void setMovieCommentField(boolean newMovie, MovieBean movie) {
		movieCommentField = new TextField("Comment");
		if (newMovie) {
			movieCommentField.setInputPrompt("care to comment?");
		} else {
			movieCommentField.setValue(movie.getComment());
			movieCommentField.setReadOnly(true);
		}
		movieCommentField.setImmediate(true);
		movieWindowLayout.addComponent(movieCommentField);
	}

	private void setMovieNameTextField(boolean newMovie, MovieBean movie) {
		movieNameTextField = new TextField("Name");
		if (newMovie) {
			movieNameTextField.setInputPrompt("enter name");
		} else {
			movieNameTextField.setValue(movie.getName());
			movieNameTextField.setReadOnly(true);
		}
		movieWindowLayout.addComponent(movieNameTextField);
	}

	private void setRewatchLayout(boolean newMovie, boolean watched, final MovieBean movie) {
		rewatchLayout = new HorizontalLayout();
		rewatchLayout.setSpacing(true);
		movieRewatchNeededCheckBox = new CheckBox("rewatch needed?");
		if (newMovie || !watched) {
			movieRewatchNeededCheckBox.setVisible(false);
		} else if (watched) {
			movieRewatchNeededCheckBox.setValue(movie.isRewatchNeeded());
			movieRewatchNeededCheckBox.setReadOnly(true);
		}

		watchCountButton = new Button("RE!", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (movie != null) {
					portalDAO.incrementWatchCount(movie.getId());
					initTable();
				}

			}
		});
		if (newMovie || !watched) {
			watchCountButton.setVisible(false);
		}

		rewatchLayout.addComponent(movieRewatchNeededCheckBox);
		rewatchLayout.addComponent(watchCountButton);
		rewatchLayout.setComponentAlignment(movieRewatchNeededCheckBox, Alignment.MIDDLE_LEFT);
		rewatchLayout.setComponentAlignment(watchCountButton, Alignment.MIDDLE_RIGHT);
		movieWindowLayout.addComponent(rewatchLayout);
	}

	private void setMoviePopupWindow(MovieBean movie) {
		movieWindow = new Window();
		if (movie != null) {
			movieWindow.setCaption(movie.getName());
		} else {
			movieWindow.setCaption("New Movie");
		}
		movieWindow.setWidth("30%");
		movieWindow.setHeight("80%");
		movieWindowLayout = new FormLayout();
		movieWindowLayout.setSpacing(true);
		movieWindowLayout.setMargin(true);
//		movieWindow.setModal(true);
	}

	private void setAllMoviesTable() {
		ArrayList<MovieBean> allMovieNames = portalDAO.getAllMovies();
		if (allMovieNames != null && allMovieNames.size() > 0) {
			BeanItemContainer<MovieBean> container = new BeanItemContainer<MovieBean>(MovieBean.class);
			container.addAll(allMovieNames);
			container.sort(new Object[] { "rating", "sadnessLevel" }, new boolean[] { false, false });
			moviesTable.setCaption("Movies (All)");
			moviesTable.setContainerDataSource(container);
			moviesTable.setVisibleColumns(new Object[] { "name", "rating", "sadnessLevel", "recommend", "watched",
					"comment", "watchedBecause", "desireLevel", "rewatchNeeded", "watchCount" });
			moviesTable.setColumnHeaders(new String[] { "Name", "Rating", "Sadness", "Recommend", "Watched", "Comment",
					"Watch Because", "Desire Level", "Rewatch Needed", "Watch Count" });
			movieItemClickListener.setNewMovie(false);
			movieItemClickListener.setWatched(true);
		} else {
			Notification.show("Cannot load all movies", Type.ERROR_MESSAGE);
			System.out.println("NO PortalDAO available to load Films=/");
		}
	}

	private void setWatchedMoviesTable() {
		ArrayList<MovieBean> watchedMovies = portalDAO.getAllWatchedMovies();

		if (watchedMovies != null && watchedMovies.size() > 0) {
			BeanItemContainer<MovieBean> container = new BeanItemContainer<MovieBean>(MovieBean.class);
			container.addAll(watchedMovies);
			container.sort(new Object[] { "rating", "sadnessLevel" }, new boolean[] { false, false });
			moviesTable.setCaption("Movies (Watched)");
			moviesTable.setContainerDataSource(container);
			moviesTable.setVisibleColumns(new Object[] { "name", "rating", "sadnessLevel", "recommend", "comment",
					"watchedBecause", "rewatchNeeded", "watchCount" });
			moviesTable.setColumnHeaders(new String[] { "Name", "Rating", "Sadness", "Recommend", "Comment",
					"Watch Because", "Rewatch Needed", "Watch Count" });
			movieItemClickListener.setNewMovie(false);
			movieItemClickListener.setWatched(true);
		} else {
			Notification.show("Cannot load all watched movies", Type.ERROR_MESSAGE);
			System.out.println("NO PortalDAO available to load Films=/");
		}
	}

	private void setToWatchMoviesTable() {
		ArrayList<MovieBean> toWatchMovies = portalDAO.getAllUnwatchedMovies();

		if (toWatchMovies != null && toWatchMovies.size() > 0) {

			BeanItemContainer<MovieBean> container = new BeanItemContainer<MovieBean>(MovieBean.class);
			container.addAll(toWatchMovies);
			container.sort(new Object[] { "desireLevel" }, new boolean[] { false });
			moviesTable.setCaption("Movies (To watch)");
			moviesTable.setContainerDataSource(container);
			moviesTable.setVisibleColumns(new Object[] { "name", "comment", "watchedBecause", "desireLevel" });
			moviesTable.setColumnHeaders(new String[] { "Name", "Comment", "Watch Because", "Desire Level" });
			movieItemClickListener.setNewMovie(false);
			movieItemClickListener.setWatched(false);
		} else {
			Notification.show("Cannot load all to-watch movies", Type.ERROR_MESSAGE);
			System.out.println("NO PortalDAO available to load Films=/");
		}
	}

	private void setRecommendedMoviesTable() {
		ArrayList<MovieBean> recommendedMovies = portalDAO.getAllRecommendedMovies();

		if (recommendedMovies != null && recommendedMovies.size() > 0) {
			BeanItemContainer<MovieBean> container = new BeanItemContainer<MovieBean>(MovieBean.class);
			container.addAll(recommendedMovies);
			container.sort(new Object[] { "rating", "sadnessLevel" }, new boolean[] { false, false });
			moviesTable.setCaption("Movies (Recommended)");
			moviesTable.setContainerDataSource(container);
			moviesTable.setVisibleColumns(new Object[] { "name", "rating", "sadnessLevel", "comment", "watchedBecause",
					"rewatchNeeded", "watchCount" });
			moviesTable.setColumnHeaders(new String[] { "Name", "Rating", "Sadness", "Comment", "Watch Because",
					"Rewatch Needed", "Watch Count" });
			movieItemClickListener.setNewMovie(false);
			movieItemClickListener.setWatched(true);
		} else {
			Notification.show("Cannot load all recommended movies", Type.ERROR_MESSAGE);
			System.out.println("NO PortalDAO available to load Films=/");
		}
	}

	private void initTable() {
		System.out.println("tableType: " + tableType + "; " + tableType.toString());
		switch (tableType) {
		case ALL:
			setAllMoviesTable();
			break;
		case WATCHED:
			setWatchedMoviesTable();
			break;
		case TO_WATCH:
			setToWatchMoviesTable();
			break;
		case RECOMMENDED:
			setRecommendedMoviesTable();
			break;
		}

	}

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | --------------L I S T E N E R S-----------------|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	private class MoviesButtonListener implements ClickListener {
		private static final long serialVersionUID = 1L;

		TableType innerTableType;

		public MoviesButtonListener(TableType tableType) {
			this.innerTableType = tableType;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			tableType = innerTableType;
			initTable();
		}

	}

	private class AddMovieButtonListener implements ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			editFlag = false;
			openMoviePopupWindow(true, false, null);
		}
	}

	private class SubmitMovieButtonListener implements ClickListener {
		private static final long serialVersionUID = 1L;

		Integer id;

		private SubmitMovieButtonListener(Integer id) {
			this.id = id;
		}

		@Override
		public void buttonClick(ClickEvent event) {

			if (movieNameTextField.getValue() != null && movieWatchedCheckBox.getValue() != null) {
				if (movieWatchedCheckBox.getValue()) {
					if (movieRatingComboBox.getValue() != null && movieRecommendedCheckBox.getValue() != null) {
						if (editFlag) {
							editWatchedMovie();
						} else {
							addNewWatchedMovie();
						}
						initTable();
					} else {
						Notification.show("Pls, fill all required fields");
					}
				} else {
					if (editFlag) {
						editToWatchMovie();
					} else {
						addToWatchMovie();
					}
					initTable();
				}
			} else {
				Notification.show("Pls, fill all required fields");
			}
			movieWindow.close();

		}

		private void addToWatchMovie() {
			portalDAO.addNewMovie(movieNameTextField.getValue(), false, null, movieCommentField.getValue(), null, null,
					movieWatchedBecauseTextField.getValue(), null, movieDesireLevelComboBox.getValue() != null
							? Integer.valueOf(movieDesireLevelComboBox.getValue().toString()) : null,
					null);
		}

		private void editToWatchMovie() {
			portalDAO.editMovie(movieNameTextField.getValue(), false, null, movieCommentField.getValue(), null, null,
					movieWatchedBecauseTextField.getValue(), null, movieDesireLevelComboBox.getValue() != null
							? Integer.valueOf(movieDesireLevelComboBox.getValue().toString()) : null,
					null, id);
		}

		private void addNewWatchedMovie() {
			portalDAO.addNewMovie(movieNameTextField.getValue(), true,
					Double.valueOf(movieRatingComboBox.getValue().toString()),
					movieCommentField.getValue() != null ? movieCommentField.getValue() : null,
					movieDateField.getValue() != null ? new Date(movieDateField.getValue().getTime()) : null,
					movieRecommendedCheckBox.getValue(), movieWatchedBecauseTextField.getValue(),
					movieSadnessLevelComboBox.getValue() != null
							? Double.valueOf(movieSadnessLevelComboBox.getValue().toString()) : null,
					movieDesireLevelComboBox.getValue() != null
							? Integer.valueOf(movieDesireLevelComboBox.getValue().toString()) : null,
					movieRewatchNeededCheckBox.getValue());
		}

		private void editWatchedMovie() {
			portalDAO.editMovie(movieNameTextField.getValue(), true,
					Double.valueOf(movieRatingComboBox.getValue().toString()),
					movieCommentField.getValue() != null ? movieCommentField.getValue() : null,
					movieDateField.getValue() != null ? new Date(movieDateField.getValue().getTime()) : null,
					movieRecommendedCheckBox.getValue(), movieWatchedBecauseTextField.getValue(),
					movieSadnessLevelComboBox.getValue() != null
							? Double.valueOf(movieSadnessLevelComboBox.getValue().toString()) : null,
					movieDesireLevelComboBox.getValue() != null
							? Integer.valueOf(movieDesireLevelComboBox.getValue().toString()) : null,
					movieRewatchNeededCheckBox.getValue(), id);
		}
	}

	private class EditMovieButtonListener implements ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			editFlag = true;
			movieNameTextField.setReadOnly(false);
			movieWatchedCheckBox.setReadOnly(false);
			movieCommentField.setReadOnly(false);
			movieWatchedBecauseTextField.setReadOnly(false);
			if (movieWatchedCheckBox.getValue()) {
				movieRatingComboBox.setReadOnly(false);
				movieRecommendedCheckBox.setReadOnly(false);
				movieDateField.setReadOnly(false);
				movieSadnessLevelComboBox.setReadOnly(false);
				movieRewatchNeededCheckBox.setReadOnly(false);
			} else {
				movieDesireLevelComboBox.setReadOnly(false);
			}
			submitButton.setVisible(true);
		}

	}

	private class DeleteMovieButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		private Integer id;

		private DeleteMovieButtonListener(Integer id) {
			this.id = id;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			ConfirmDialog.show(getUI(), new ConfirmDialog.Listener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						portalDAO.deleteMovie(id);
						movieWindow.close();
						dialog.close();
					}
				}
			});
		}
	}

	private class MovieWatchedCheckBoxListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			if (movieWatchedCheckBox.getValue()) {
				movieRatingComboBox.setVisible(true);
				movieRatingComboBox.setReadOnly(false);
				movieRecommendedCheckBox.setVisible(true);
				movieDateField.setVisible(true);
				movieSadnessLevelComboBox.setVisible(true);
				movieSadnessLevelComboBox.setReadOnly(false);
				movieDesireLevelComboBox.setVisible(false);
				movieRewatchNeededCheckBox.setVisible(true);
			} else {
				movieRatingComboBox.setVisible(false);
				movieRecommendedCheckBox.setVisible(false);
				movieDateField.setVisible(false);
				movieSadnessLevelComboBox.setVisible(false);
				movieDesireLevelComboBox.setVisible(true);
				movieRewatchNeededCheckBox.setVisible(false);
			}
		}

	}

	private class MovieItemClickListener implements ItemClickListener {

		private boolean newMovie;
		private boolean watched;

		public MovieItemClickListener() {
		}

		public void setNewMovie(boolean newMovie) {
			this.newMovie = newMovie;
		}

		public void setWatched(boolean watched) {
			this.watched = watched;
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void itemClick(ItemClickEvent event) {
			if (event.isDoubleClick()) {
				MovieBean selectedMovie = (MovieBean) moviesTable.getValue();

				System.out.println(
						"Open MOVIE window NOW! id: " + selectedMovie.getId() + " name: " + selectedMovie.getName());
				System.out.println("newMovie: " + newMovie + " watched: " + watched);
				openMoviePopupWindow(newMovie, watched, selectedMovie);
			}
		}

	}

	private class WikiAPIListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		String movieName;
		
		public WikiAPIListener(String movieName) {
			this.movieName = movieName;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			getWiki(movieName);

		}

	}

	private class SeachMovieComboBoxListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			if (searchMovieComboBox.getValue() != null) {
				openMoviePopupWindow(false, true, (MovieBean) searchMovieComboBox.getValue());
			}
		}

	}

	private void getWiki(String movieName) {

		String[] listOfMovieStrings = { movieName + "(film)"};
		User movieUser = new User("", "", "http://en.wikipedia.org/w/api.php");
		movieUser.login();
		List<Page> listOfMoviePages = movieUser.queryContent(listOfMovieStrings);
		for (Page page : listOfMoviePages) {
			WikiModel wikiMovieModel = new WikiModel("${image}", "${title}");
			String movieHtml = wikiMovieModel.render(page.toString());
			System.out.println("movieeees page image: " + page.getImageUrl());
			System.out.println("movieeees page title: " + page.getTitle());
			System.out.println("movies getCurrentContent: " + page.getCurrentContent());
			System.out.println("movies getImageThumbUrl: " + page.getImageThumbUrl());
			System.out.println("movies page.sizeOfCategoryList(): " + page.sizeOfCategoryList());
			for (int j = 0; j < page.sizeOfCategoryList(); j++) {
				PageInfo cat = page.getCategory(j);
				// print every category in this page
				System.out.println("cat.getTitle()" + cat.getTitle());
				System.out.println("cat.toString()" + cat.toString());
				System.out.println("page.getLink(j).toString()" + page.getLink(j).toString());
			}
		}
	}

}