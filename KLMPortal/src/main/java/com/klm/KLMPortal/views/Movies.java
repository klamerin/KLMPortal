package com.klm.KLMPortal.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

//import org.tepi.filtertable.FilterTable;
import org.vaadin.dialogs.ConfirmDialog;

import com.klm.KLMPortal.beans.MovieBean;
import com.klm.KLMPortal.data.DAOFactory;
import com.klm.KLMPortal.data.DAO.IMovieDAO;
import com.klm.KLMPortal.entities.Film;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.SingleSelectionModel;

import info.bliki.api.Page;
import info.bliki.api.PageInfo;
import info.bliki.api.User;
import info.bliki.wiki.model.WikiModel;

public class Movies extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	private DAOFactory mysqlDAOFactory = DAOFactory.getMYSQLFactory();
	private IMovieDAO movieDAO = mysqlDAOFactory.getMovieDAO();

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
	protected static final String MUSICSVIEW = "music";

	private Grid<MovieBean> moviesTable;
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

	private Button addMovieButton;
	private ComboBox<TableType> moviesCombobox;
	private ComboBox<MovieBean> searchMovieComboBox;

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
	private ComboBox<Double> movieRatingComboBox;
	private CheckBox movieRecommendedCheckBox;
	private TextArea movieCommentArea;
	private DateField movieDateField;
	private TextField movieWatchedBecauseTextField;
	private ComboBox<Double> movieSadnessLevelComboBox;
	private ComboBox<Integer> movieDesireLevelComboBox;
	private CheckBox movieRewatchNeededCheckBox;
	private Button watchCountButton;
	private TextField watchCountTextField;
	private HorizontalLayout rewatchLayout;
	private HorizontalLayout movieWindowButtonsLayout;
	private Button editButton;
	private Button submitButton;
	private Button deleteButton;
	private CheckBox socialCheckBox;

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | ------------D A T A — F I E L D S---------------|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	private boolean editFlag = false;
	private boolean newlyWatched = false;

	@SuppressWarnings("unused")
	private Button JPAButton;
	private Grid<MovieBean> JPATable;
	// EntityManager em =
	// JPAContainerFactory.createEntityManagerForPersistenceUnit("KLMPortal");
	// JPAContainer<Film> cont = JPAContainerFactory.make(Film.class, em);
	private JPAContainer<Film> cont;

	// private enum TableType {
	// ALL, WATCHED, RECOMMENDED, TO_WATCH;
	// }

	public enum TableType {
		ALL("All"), WATCHED("Watched"), RECOMMENDED("Recommended"), TO_WATCH_ALONE("Watch Alone"), TO_WATCH_SOCIAL(
				"Watch with Tina");

		private final String value;

		TableType(String value) {
			this.value = value;
		}

		public String getTableTypeValue() {
			return this.value;
		}

	}

	private TableType tableType = TableType.ALL;

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | -----------C O N S T R U C T O R S--------------|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	public Movies(EntityManager em) {

		buildMainLayout();
		// em =
		// JPAContainerFactory.createEntityManagerForPersistenceUnit("KLMPortal");
		if (em == null) {
			System.out.println("no entity manager=<");
		} else {
			cont = JPAContainerFactory.make(Film.class, em);
			if (cont == null) {
				System.out.println("no jpa container=>");
			} else {
				System.out.println("yes jpa container " + cont.size());
			}
		}
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

		Button musicButton = new Button("Music", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				nav.navigateTo(MUSICSVIEW);
			}
		});
		navLayout.addComponent(musicButton);
		navLayout.setComponentAlignment(musicButton, Alignment.TOP_CENTER);

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

		// setIcon(new ThemeResource("icons/movies-main.jpg"));
		setStyleName("moviesMainBackground");
		moviesLayout.setWidth("100%");
		setNavLayout();

		setButtonsLayout();

		setTableLayout();

		addComponent(moviesLayout);
		setExpandRatio(navLayout, 1);
		setExpandRatio(moviesLayout, 10);
		initTable();
	}

	private void setButtonsLayout() {
		buttonsLayout = new VerticalLayout();
		buttonsLayout.setSizeFull();

		moviesCombobox = new ComboBox<TableType>("Show");
		moviesCombobox.setItemCaptionGenerator(tableType -> tableType.getTableTypeValue());
		moviesCombobox.setItems(TableType.values());

		moviesCombobox.addValueChangeListener(new MoviesComboBoxListener());
		buttonsLayout.addComponent(moviesCombobox);
		buttonsLayout.setComponentAlignment(moviesCombobox, Alignment.TOP_LEFT);

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
		moviesTable = new Grid<MovieBean>(MovieBean.class);
		moviesTable.setStyleName("moviesTable");
//		moviesTable.setSelectionMode(SelectionMode.SINGLE);
		SingleSelectionModel<MovieBean> model = (SingleSelectionModel<MovieBean>) moviesTable.getSelectionModel();
		model.addSingleSelectionListener(movieItemClickListener);
		moviesTable.setWidth("100%");
//		moviesTable.addSelectionListener(movieItemClickListener);
		// moviesTable.addItemClickListener(movieItemClickListener);

		tableLayout.addComponent(moviesTable);
		tableLayout.setExpandRatio(moviesTable, 20);

		moviesLayout.addComponent(tableLayout);
		moviesLayout.setExpandRatio(tableLayout, 10.0f);
	}

	/*
	 * —————————————————————————————————————————————————\
	 * 
	 * | -------G U I — H E L P E R — M E T H O D S------|
	 * 
	 * —————————————————————————————————————————————————/
	 */

	private void openMoviePopupWindow(boolean newMovie, boolean watched, MovieBean movie) {

		newlyWatched = false;
		setMoviePopupWindow(movie);
		if (!newMovie) {
			watched = movie.isWatched();
		}
		setMovieNameTextField(newMovie, movie);

		setMovieCommentField(newMovie, movie);

		setMovieCheckBoxes(newMovie, movie);

		// getMovieWatchedCheckBox(newMovie, movie);

		// getSocialCheckBox(newMovie, movie);

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

		movieWatchedCheckBox.addValueChangeListener(new MovieWatchedCheckBoxListener(movie));
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
				JPATable = new Grid<MovieBean>("JPA");
				tableLayout.removeAllComponents();
				tableLayout.addComponent(JPATable);
			}
		}));

		// Button wikiButton = new Button("Wiki", new WikiAPIListener());
		// buttonsLayout.addComponent(wikiButton);
		// buttonsLayout.setComponentAlignment(wikiButton,
		// Alignment.BOTTOM_RIGHT);
	}

	private void setSearchMovieComboBox() {
		searchMovieComboBox = new ComboBox<MovieBean>("Search");
		searchMovieComboBox.setItems(movieDAO.getAllMovies());
		searchMovieComboBox.setItemCaptionGenerator(MovieBean::getName);
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

		submitButton = new Button("Submit", new SubmitMovieButtonListener(movie != null ? movie : null));
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
			movieWatchedBecauseTextField.setPlaceholder("watch why?");
		} else {
			if (movie.getWatchedBecause() != null) {
				movieWatchedBecauseTextField.setValue(movie.getWatchedBecause() != null ? movie.getWatchedBecause() : "");
			}
			movieWatchedBecauseTextField.setReadOnly(true);
		}
		// movieWatchedBecauseTextField.setNullRepresentation("");
		movieWindowLayout.addComponent(movieWatchedBecauseTextField);
	}

	private void setMovieSadnessLevelComboBox(boolean newMovie, boolean watched, MovieBean movie) {
		movieSadnessLevelComboBox = new ComboBox<Double>("Sadness Level");
		movieSadnessLevelComboBox.setItems(Arrays.asList(0.0, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0,
				6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0));
		movieSadnessLevelComboBox.setPlaceholder("sadness level?");
		if (newMovie) {
			movieSadnessLevelComboBox.setVisible(false);
		} else if (watched) {
			movieSadnessLevelComboBox.setVisible(true);
			movieSadnessLevelComboBox.setValue(movie.getSadnessLevel() != null ? movie.getSadnessLevel() : -1);
			movieSadnessLevelComboBox.setReadOnly(true);
		} else {
			movieSadnessLevelComboBox.setVisible(false);
			movieSadnessLevelComboBox.setReadOnly(true);
		}
		movieWindowLayout.addComponent(movieSadnessLevelComboBox);
	}

	private void setMovieDesireLevelComboBox(boolean newMovie, boolean watched, MovieBean movie) {
		movieDesireLevelComboBox = new ComboBox<Integer>("Desire Level");
		movieDesireLevelComboBox.setItems(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
		movieDesireLevelComboBox.setPlaceholder("desire level?");
		if (newMovie) {
			movieDesireLevelComboBox.setVisible(true);
			movieDesireLevelComboBox.setReadOnly(false);
		} else if (watched) {
			movieDesireLevelComboBox.setVisible(false);
		} else {
			movieDesireLevelComboBox.setVisible(true);
			movieDesireLevelComboBox.setValue(movie.getDesireLevel() != null ? movie.getDesireLevel() : -1);
			movieDesireLevelComboBox.setReadOnly(false);
		}
		movieWindowLayout.addComponent(movieDesireLevelComboBox);
	}

	private void setMovieDateField(boolean newMovie, boolean watched, MovieBean movie) {
		movieDateField = new DateField("Date");
		movieDateField.setVisible(false);
		if (newMovie) {
			movieDateField.setPlaceholder("watched when?");
		} else if (watched) {
			if (movie.getWatchedDate() != null) {
				movieDateField.setValue(movie.getWatchedDate());
			}
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
		movieRatingComboBox = new ComboBox<Double>("Rating");
		movieRatingComboBox.setItems(Arrays.asList(0.0, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0,
				7.5, 8.0, 8.5, 9.0, 9.5, 10.0));
		movieRatingComboBox.setPlaceholder("rating?");
		if (newMovie) {
			movieRatingComboBox.setVisible(false);
		} else if (watched) {
			movieRatingComboBox.setValue(movie.getRating() != null ? movie.getRating().doubleValue() : -1);
			movieRatingComboBox.setReadOnly(true);
		} else {
			movieRatingComboBox.setVisible(false);
		}
		movieWindowLayout.addComponent(movieRatingComboBox);
	}

	private void setMovieCheckBoxes(boolean newMovie, MovieBean movie) {
		HorizontalLayout checkBoxesLayout = new HorizontalLayout();
		checkBoxesLayout.addComponent(getMovieWatchedCheckBox(newMovie, movie));
		checkBoxesLayout.addComponent(getSocialCheckBox(newMovie, movie));
		movieWindowLayout.addComponent(checkBoxesLayout);
	}

	private CheckBox getMovieWatchedCheckBox(boolean newMovie, MovieBean movie) {
		movieWatchedCheckBox = new CheckBox("Watched?");

		// movieWatchedCheckBox.setImmediate(true);
		if (!newMovie) {
			movieWatchedCheckBox.setValue(movie.isWatched());
			movieWatchedCheckBox.setReadOnly(true);
		}
		return movieWatchedCheckBox;
	}

	private CheckBox getSocialCheckBox(boolean newMovie, MovieBean movie) {
		socialCheckBox = new CheckBox("Social?");

		// socialCheckBox.setImmediate(true);
		if (!newMovie) {
			socialCheckBox.setValue(movie.isSocial());
			socialCheckBox.setReadOnly(true);
		}
		return socialCheckBox;
	}

	private void setMovieCommentField(boolean newMovie, MovieBean movie) {
		movieCommentArea = new TextArea("Comment");
		// movieCommentArea.setRows(1);
		// movieCommentArea.setMaxRows(5);
		movieCommentArea.setWidth("70%");
		if (newMovie) {
			movieCommentArea.setPlaceholder("care to comment?");
		} else {
			movieCommentArea.setValue(movie.getComment() != null ? movie.getComment() : "");
			movieCommentArea.setReadOnly(true);
		}
		// movieCommentArea.setImmediate(true);
		movieWindowLayout.addComponent(movieCommentArea);
	}

	private void setMovieNameTextField(boolean newMovie, MovieBean movie) {
		movieNameTextField = new TextField("Name");
		movieNameTextField.setWidth("70%");
		if (newMovie) {
			movieNameTextField.setPlaceholder("enter name");
		} else {
			movieNameTextField.setValue(movie.getName() != null ? movie.getName() : "");
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

		watchCountTextField = new TextField();
		watchCountTextField.setWidth("30px");
		if (newMovie || !watched) {
			watchCountTextField.setVisible(false);
		} else if (watched) {
			watchCountTextField.setValue(movie.getWatchCount() + "");
			watchCountTextField.setReadOnly(true);
		}
		watchCountButton = new Button("RE!", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (movie != null) {
					movieDAO.incrementWatchCount(movie.getId());
					movie.incrementWatchCount();
					watchCountTextField.setReadOnly(false);
					watchCountTextField.setValue(movie.getWatchCount() + "");
					watchCountTextField.setReadOnly(true);
					initTable();
				}

			}
		});
		if (newMovie || !watched) {
			watchCountButton.setVisible(false);
		}

		rewatchLayout.addComponent(watchCountTextField);
		rewatchLayout.addComponent(movieRewatchNeededCheckBox);
		rewatchLayout.addComponent(watchCountButton);
		rewatchLayout.setComponentAlignment(movieRewatchNeededCheckBox, Alignment.MIDDLE_LEFT);
		rewatchLayout.setComponentAlignment(watchCountTextField, Alignment.MIDDLE_LEFT);
		rewatchLayout.setComponentAlignment(watchCountButton, Alignment.MIDDLE_RIGHT);
		movieWindowLayout.addComponent(rewatchLayout);
	}

	private void setMoviePopupWindow(MovieBean movie) {
		movieWindow = new Window();
		movieWindow.setStyleName("moviesWindowBackground");
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
		movieWindow.setModal(true);
	}

	private void setAllMoviesTable() {
		ArrayList<MovieBean> allMovies = movieDAO.getAllMovies();
		if (allMovies != null && allMovies.size() > 0) {
			moviesTable.setItems(allMovies);
			moviesTable.setHeightByRows(15);
			moviesTable.setColumns("name", "rating", "sadnessLevel", "recommend", "watched", "comment",
					"watchedBecause", "desireLevel", "rewatchNeeded", "watchCount", "social");

			moviesTable.getColumn("name").setCaption("Name");
			moviesTable.getColumn("rating").setCaption("Rating");
			moviesTable.getColumn("sadnessLevel").setCaption("Sadness");
			moviesTable.getColumn("recommend").setCaption("Recommend");
			moviesTable.getColumn("watched").setCaption("Watched");
			moviesTable.getColumn("comment").setCaption("Comment");
			moviesTable.getColumn("watchedBecause").setCaption("Watch Because");
			moviesTable.getColumn("desireLevel").setCaption("Desire Level");
			moviesTable.getColumn("rewatchNeeded").setCaption("Rewatch Needed");
			moviesTable.getColumn("watchCount").setCaption("Watch Count");
			moviesTable.getColumn("social").setCaption("Social?");

			moviesTable.setSortOrder(GridSortOrder.desc(moviesTable.getColumn("rating"))
					.thenDesc(moviesTable.getColumn("sadnessLevel")));
			movieItemClickListener.setNewMovie(false);
			movieItemClickListener.setWatched(true);
		} else {
			Notification.show("Cannot load all movies", Type.ERROR_MESSAGE);
			System.out.println("NO PortalDAO available to load Films=/");
		}
	}

	private void setWatchedMoviesTable() {
		ArrayList<MovieBean> watchedMovies = movieDAO.getAllWatchedMovies();

		if (watchedMovies != null && watchedMovies.size() > 0) {
			moviesTable.setItems(watchedMovies);
			moviesTable.setColumns("name", "rating", "sadnessLevel", "recommend", "comment", "watchedBecause",
					"rewatchNeeded", "watchCount");
			moviesTable.getColumn("name").setCaption("Name");
			moviesTable.getColumn("rating").setCaption("Rating");
			moviesTable.getColumn("sadnessLevel").setCaption("Sadness");
			moviesTable.getColumn("recommend").setCaption("Recommend");
			moviesTable.getColumn("comment").setCaption("Comment");
			moviesTable.getColumn("watchedBecause").setCaption("Watch Because");
			moviesTable.getColumn("rewatchNeeded").setCaption("Rewatch Needed");
			moviesTable.getColumn("watchCount").setCaption("Watch Count");

			moviesTable.getColumn("name").setExpandRatio(3);
			moviesTable.getColumn("rating").setExpandRatio(1);
			moviesTable.getColumn("sadnessLevel").setExpandRatio(1);
			moviesTable.getColumn("recommend").setExpandRatio(1);
			moviesTable.getColumn("comment").setExpandRatio(1);
			moviesTable.getColumn("watchedBecause").setExpandRatio(1);
			moviesTable.getColumn("rewatchNeeded").setExpandRatio(1);
			moviesTable.getColumn("watchCount").setExpandRatio(1);

			moviesTable.setSortOrder(GridSortOrder.desc(moviesTable.getColumn("rating"))
					.thenDesc(moviesTable.getColumn("sadnessLevel")));
			movieItemClickListener.setNewMovie(false);
			movieItemClickListener.setWatched(true);
		} else {
			Notification.show("Cannot load all watched movies", Type.ERROR_MESSAGE);
			System.out.println("NO PortalDAO available to load Films=/");
		}
	}

	private void setToWatchAloneMoviesTable() {
		ArrayList<MovieBean> toWatchMovies = movieDAO.getToWatchAloneFilms();

		if (toWatchMovies != null && toWatchMovies.size() > 0) {

			moviesTable.setItems(toWatchMovies);
			moviesTable.setColumns("name", "comment", "watchedBecause", "desireLevel");
			moviesTable.getColumn("name").setCaption("Name");
			moviesTable.getColumn("comment").setCaption("Comment");
			moviesTable.getColumn("watchedBecause").setCaption("Watch Because");
			moviesTable.getColumn("desireLevel").setCaption("Desire Level");

			moviesTable.setSortOrder(GridSortOrder.desc(moviesTable.getColumn("desireLevel")));
			movieItemClickListener.setNewMovie(false);
			movieItemClickListener.setWatched(false);
		} else {
			Notification.show("Cannot load all to-watch movies", Type.ERROR_MESSAGE);
			System.out.println("NO PortalDAO available to load Films=/");
		}
	}

	private void setToWatchSocialMoviesTable() {
		ArrayList<MovieBean> toWatchMovies = movieDAO.getToWatchWithTinaFilms();

		if (toWatchMovies != null && toWatchMovies.size() > 0) {

			moviesTable.setItems(toWatchMovies);
			// moviesTable.setCaption("Movies (To watch with Tina)");
			moviesTable.setColumns("name", "comment", "watchedBecause", "desireLevel");
			moviesTable.getColumn("name").setCaption("Name");
			moviesTable.getColumn("comment").setCaption("Comment");
			moviesTable.getColumn("watchedBecause").setCaption("Watch Because");
			moviesTable.getColumn("desireLevel").setCaption("Desire Level");

			moviesTable.setSortOrder(GridSortOrder.desc(moviesTable.getColumn("desireLevel")));
			movieItemClickListener.setNewMovie(false);
			movieItemClickListener.setWatched(false);
		} else {
			Notification.show("Cannot load all to-watch movies", Type.ERROR_MESSAGE);
			System.out.println("NO PortalDAO available to load Films=/");
		}
	}

	private void setRecommendedMoviesTable() {
		ArrayList<MovieBean> recommendedMovies = movieDAO.getAllRecommendedMovies();

		if (recommendedMovies != null && recommendedMovies.size() > 0) {
			moviesTable.setItems(recommendedMovies);
			moviesTable.setColumns("name", "rating", "sadnessLevel", "comment", "watchedBecause", "rewatchNeeded",
					"watchCount");
			moviesTable.getColumn("name").setCaption("Name");
			moviesTable.getColumn("rating").setCaption("Rating");
			moviesTable.getColumn("sadnessLevel").setCaption("Sadness");
			moviesTable.getColumn("comment").setCaption("Comment");
			moviesTable.getColumn("watchedBecause").setCaption("Watch Because");
			moviesTable.getColumn("rewatchNeeded").setCaption("Rewatch Needed");
			moviesTable.getColumn("watchCount").setCaption("Watch Count");

			moviesTable.setSortOrder(GridSortOrder.desc(moviesTable.getColumn("rating"))
					.thenDesc(moviesTable.getColumn("sadnessLevel")));
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
		case TO_WATCH_ALONE:
			setToWatchAloneMoviesTable();
			break;
		case TO_WATCH_SOCIAL:
			setToWatchSocialMoviesTable();
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

	private class MoviesComboBoxListener implements ValueChangeListener<TableType> {
		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent<TableType> event) {
			if (moviesCombobox.getValue() != null) {

				tableType = (TableType) moviesCombobox.getValue();

				initTable();
			}
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

		MovieBean movie;

		private SubmitMovieButtonListener(MovieBean movie) {
			this.movie = movie;
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
						movieWindow.close();
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
					movieWindow.close();
					initTable();
				}
			} else {
				Notification.show("Pls, fill all required fields");
			}

		}

		private void addToWatchMovie() {
			movieDAO.addNewMovie(movieNameTextField.getValue(), false, null, movieCommentArea.getValue(), null, null,
					movieWatchedBecauseTextField.getValue(), null,
					movieDesireLevelComboBox.getValue() != null
							? Integer.valueOf(movieDesireLevelComboBox.getValue().toString()) : null,
					null, socialCheckBox.getValue());
		}

		private void editToWatchMovie() {
			movieDAO.editMovie(movieNameTextField.getValue(), false, null, movieCommentArea.getValue(), null, null,
					movieWatchedBecauseTextField.getValue(), null,
					movieDesireLevelComboBox.getValue() != null
							? Integer.valueOf(movieDesireLevelComboBox.getValue().toString()) : null,
					null, socialCheckBox.getValue(), null, movie.getId());
		}

		private void addNewWatchedMovie() {
			movieDAO.addNewMovie(movieNameTextField.getValue(), true,
					Double.valueOf(movieRatingComboBox.getValue().toString()),
					movieCommentArea.getValue() != null ? movieCommentArea.getValue() : null, movieDateField.getValue(),
					movieRecommendedCheckBox.getValue(), movieWatchedBecauseTextField.getValue(),
					movieSadnessLevelComboBox.getValue() != null
							? Double.valueOf(movieSadnessLevelComboBox.getValue().toString()) : null,
					movieDesireLevelComboBox.getValue() != null
							? Integer.valueOf(movieDesireLevelComboBox.getValue().toString()) : null,
					movieRewatchNeededCheckBox.getValue(), socialCheckBox.getValue());
		}

		private void editWatchedMovie() {
			movieDAO.editMovie(movieNameTextField.getValue(), true,
					Double.valueOf(movieRatingComboBox.getValue().toString()),
					movieCommentArea.getValue() != null ? movieCommentArea.getValue() : null, movieDateField.getValue(),
					movieRecommendedCheckBox.getValue(), movieWatchedBecauseTextField.getValue(),
					movieSadnessLevelComboBox.getValue() != null
							? Double.valueOf(movieSadnessLevelComboBox.getValue().toString()) : null,
					movieDesireLevelComboBox.getValue() != null
							? Integer.valueOf(movieDesireLevelComboBox.getValue().toString()) : null,
					movieRewatchNeededCheckBox.getValue(), socialCheckBox.getValue(),
					newlyWatched ? 1 : movie.getWatchCount(), movie.getId());
		}
	}

	private class EditMovieButtonListener implements ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			editFlag = true;
			movieNameTextField.setReadOnly(false);
			movieWatchedCheckBox.setReadOnly(false);
			movieCommentArea.setReadOnly(false);
			movieWatchedBecauseTextField.setReadOnly(false);
			socialCheckBox.setReadOnly(false);
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
						movieDAO.deleteMovie(id);
						movieWindow.close();
						dialog.close();
					}
				}
			});
		}
	}

	private class MovieWatchedCheckBoxListener implements ValueChangeListener<Boolean> {

		private static final long serialVersionUID = 1L;
		private MovieBean movie;

		public MovieWatchedCheckBoxListener(MovieBean movie) {
			this.movie = movie;
		}

		@Override
		public void valueChange(ValueChangeEvent<Boolean> event) {
			if (movieWatchedCheckBox.getValue()) {
				movieRatingComboBox.setVisible(true);
				movieRatingComboBox.setReadOnly(false);
				movieRecommendedCheckBox.setVisible(true);
				movieDateField.setVisible(true);
				movieSadnessLevelComboBox.setVisible(true);
				movieSadnessLevelComboBox.setReadOnly(false);
				movieDesireLevelComboBox.setVisible(false);
				movieRewatchNeededCheckBox.setVisible(true);
				if (movie != null && movie.getWatchCount() == 0) {
					newlyWatched = true;
				}
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

	private class MovieItemClickListener implements /* ItemClickListener */SingleSelectionListener<MovieBean> {

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
		public void selectionChange(SingleSelectionEvent<MovieBean> event) {
			if (event.getSelectedItem().isPresent()) {

				MovieBean selectedMovie = event.getSelectedItem().get();
				if (selectedMovie != null) {
					openMoviePopupWindow(newMovie, watched, selectedMovie);
				}
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
			try {
				getWiki(movieName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class SeachMovieComboBoxListener implements ValueChangeListener<MovieBean> {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent<MovieBean> event) {
			if (searchMovieComboBox.getValue() != null) {
				openMoviePopupWindow(false, true, (MovieBean) searchMovieComboBox.getValue());
			}
		}

	}

	private void getWiki(String movieName) throws IOException {

		String[] listOfMovieStrings = { movieName + " (film)" };
		for (String string : listOfMovieStrings) {
			System.out.println("listOfMovieString: " + string);
		}
		User movieUser = new User("", "", "http://en.wikipedia.org/w/api.php");
		System.out.println(movieUser.login() == true ? " login succes" : "login faled");
		List<Page> listOfMoviePages = movieUser.queryContent(listOfMovieStrings);
		System.out.println(listOfMoviePages.size() > 0 ? "listOfMoviePages size is: " + listOfMoviePages.size()
				: "listOfMoviePages is NULLL");
		for (Page page : listOfMoviePages) {
			WikiModel wikiMovieModel = new WikiModel("${image}", "${title}");
			String movieHtml = wikiMovieModel.render(page.toString());
			System.out.println(movieHtml);
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
