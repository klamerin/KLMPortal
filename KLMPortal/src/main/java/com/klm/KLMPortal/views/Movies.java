package com.klm.KLMPortal.views;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import com.google.gwt.user.client.ui.SubmitButton;
import com.klm.KLMPortal.beans.MovieBean;
import com.klm.KLMPortal.data.DAOFactory;
import com.klm.KLMPortal.data.DAO.IMovieDAO;
import com.klm.KLMPortal.entities.Film;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
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

public class Movies extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	private Navigator nav = UI.getCurrent().getNavigator();
	private HorizontalLayout navLayout = new HorizontalLayout();
	private HorizontalLayout moviesLayout = new HorizontalLayout();
	private static final String GEN_INFO = "generalInfo";

	private DAOFactory mssqlDAOFactory = DAOFactory.getFactory();
	private IMovieDAO portalDAO = mssqlDAOFactory.getPortalDAO();

	private Button getAllMoviesButton;
	private Button getWatchedMoviesButton;
	private Button getToWatchMoviesButton;
	private Button getRecommendedMoviesButton;
	private Button addMovieButton;

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
	
	private HorizontalLayout movieWindowButtonsLayout;
	private Button editButton;
	private Button submitButton;
	private Button deleteButton;

	boolean editFlag = false;

	private Button JPAButton;
	private Table JPATable;
	// EntityManager em =
	// JPAContainerFactory.createEntityManagerForPersistenceUnit("KLMPortal");
	// JPAContainer<Film> cont = JPAContainerFactory.make(Film.class, em);
	private JPAContainer<Film> cont;

	private ComboBox searchMovieComboBox;

	private Table moviesTable;
	private MovieItemClickListener movieItemClickListener = new MovieItemClickListener();
	private VerticalLayout buttonsLayout;
	private VerticalLayout tablesLayout;

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

	@Override
	public void enter(ViewChangeEvent event) {
		// Notification.show("Welcome to the Animal Farm REALITY");
	}

	private void buildMainLayout() {
		setSizeFull();
		setMargin(true);
		setSpacing(true);

		setMainLayout();

	}

	private void setMainLayout() {

		setNavLayout();
		
		
		buttonsLayout = new VerticalLayout();
		buttonsLayout.setSizeFull();

		getAllMoviesButton = new Button("All", new AllFilmsButtonListener());
		buttonsLayout.addComponent(getAllMoviesButton);
		buttonsLayout.setComponentAlignment(getAllMoviesButton, Alignment.TOP_LEFT);

		getWatchedMoviesButton = new Button("Watched", new WatchedFilmsButtonListener());
		buttonsLayout.addComponent(getWatchedMoviesButton);
		buttonsLayout.setComponentAlignment(getWatchedMoviesButton, Alignment.TOP_LEFT);

		getToWatchMoviesButton = new Button("To Watch", new ToWatchFilmsButtonListener());
		buttonsLayout.addComponent(getToWatchMoviesButton);
		buttonsLayout.setComponentAlignment(getToWatchMoviesButton, Alignment.TOP_LEFT);

		getRecommendedMoviesButton = new Button("Recommended", new RecommendedFilmsButtonListener());
		buttonsLayout.addComponent(getRecommendedMoviesButton);
		buttonsLayout.setComponentAlignment(getRecommendedMoviesButton, Alignment.TOP_LEFT);

		addMovieButton = new Button("Add", new AddMovieButtonListener());
		buttonsLayout.addComponent(addMovieButton);
		buttonsLayout.setComponentAlignment(addMovieButton, Alignment.TOP_LEFT);

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

		buttonsLayout.addComponent(new Button("JPA", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				JPATable = new Table("JPA", cont);
				tablesLayout.removeAllComponents();
				tablesLayout.addComponent(JPATable);
			}
		}));

		Button wikiButton = new Button("Wiki", new WikiAPIListener());
		// buttonsLayout.addComponent(wikiButton);
		// buttonsLayout.setComponentAlignment(wikiButton,
		// Alignment.BOTTOM_RIGHT);

		tablesLayout = new VerticalLayout();
		tablesLayout.setSpacing(true);
		moviesTable = new Table("Movies");
		moviesTable.setSelectable(true);
		moviesTable.setNullSelectionAllowed(false);
		moviesTable.addItemClickListener(movieItemClickListener);
		tablesLayout.addComponent(moviesTable);

		
		moviesLayout.addComponent(buttonsLayout);
		moviesLayout.addComponent(tablesLayout);
		moviesLayout.setExpandRatio(buttonsLayout, 1.0f);
		moviesLayout.setExpandRatio(tablesLayout, 5.0f);
		addComponent(moviesLayout);
		setExpandRatio(navLayout, 2);
		setExpandRatio(moviesLayout, 10);
	}

	private void setNavLayout() {
//		navLayout.setMargin(true);
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

	private void setMoviePopupWindow(boolean newMovie, boolean watched, MovieBean movie) {

		movieWindow = new Window("New Movie");
		movieWindow.setWidth("30%");
		movieWindow.setHeight("70%");
		movieWindowLayout = new FormLayout();
		movieWindowLayout.setSpacing(true);
		movieWindowLayout.setMargin(true);
		movieWindow.setModal(true);

		if (!newMovie) {
			watched = movie.isWatched();
		}
		movieNameTextField = new TextField("Name");
		if (newMovie) {
			movieNameTextField.setInputPrompt("enter name");
		} else {
			movieNameTextField.setValue(movie.getName());
			movieNameTextField.setReadOnly(true);
		}
		movieWindowLayout.addComponent(movieNameTextField);

		movieCommentField = new TextField("Comment");
		if (newMovie) {
			movieCommentField.setInputPrompt("care to comment?");
		} else {
			movieCommentField.setValue(movie.getComment());
			movieCommentField.setReadOnly(true);
		}
		movieCommentField.setImmediate(true);
		movieWindowLayout.addComponent(movieCommentField);

		movieWatchedCheckBox = new CheckBox("Watched?");

		movieWindowLayout.addComponent(movieWatchedCheckBox);

		movieRatingComboBox = new ComboBox("Rating");
		movieRatingComboBox
				.addItems(Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0));
		if (newMovie) {
			movieRatingComboBox.setInputPrompt("rating?");
			movieRatingComboBox.setVisible(false);
		} else if (watched) {
			movieRatingComboBox.setVisible(true);
			System.out.println("movie.getRating().doubleValue()" + movie.getRating().doubleValue());
			movieRatingComboBox.setValue(movie.getRating().doubleValue());
			movieRatingComboBox.setReadOnly(true);
		} else {
			movieRatingComboBox.setVisible(false);
			movieRatingComboBox.setReadOnly(true);
		}
		movieWindowLayout.addComponent(movieRatingComboBox);

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
		
		movieSadnessLevelComboBox = new ComboBox("Sadness Level");
		movieSadnessLevelComboBox
				.addItems(Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0));
		if (newMovie) {
			movieSadnessLevelComboBox.setInputPrompt("sadness level?");
			movieSadnessLevelComboBox.setVisible(false);
		} else if (watched) {
			movieSadnessLevelComboBox.setVisible(true);
			System.out.println("movie.getSadnessLevel() " + movie.getSadnessLevel());
			movieSadnessLevelComboBox.setValue(movie.getSadnessLevel());
			movieSadnessLevelComboBox.setReadOnly(true);
		} else {
			movieSadnessLevelComboBox.setVisible(false);
			movieSadnessLevelComboBox.setReadOnly(true);
		}
		movieWindowLayout.addComponent(movieSadnessLevelComboBox);
		
		
		movieWatchedBecauseTextField = new TextField("Watch Because");
		if (newMovie) {
			movieWatchedBecauseTextField.setInputPrompt("watch why?");
		} else {
			movieWatchedBecauseTextField.setValue(movie.getWatchedBecause());
			movieWatchedBecauseTextField.setReadOnly(true);
		}
		movieWatchedBecauseTextField.setNullRepresentation("");
		movieWindowLayout.addComponent(movieWatchedBecauseTextField);

		movieWatchedCheckBox.addValueChangeListener(new AddMovieWatchedCheckBoxListener());
		movieWatchedCheckBox.setImmediate(true);
		if (!newMovie) {
			movieWatchedCheckBox.setValue(movie.isWatched());
			movieWatchedCheckBox.setReadOnly(true);
		}

		
		
		
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
		movieWindow.setContent(movieWindowLayout);
		getUI().addWindow(movieWindow);
		movieWindow.center();
	}

	private void setAllMoviesTable(ArrayList<MovieBean> allMovieNames) {
		BeanItemContainer<MovieBean> container = new BeanItemContainer<MovieBean>(MovieBean.class);
		container.addAll(allMovieNames);
		moviesTable.setCaption("Movies (All)");
		moviesTable.setContainerDataSource(container);
		moviesTable.setVisibleColumns(new Object[] {"name", "rating", "sadnessLevel", "recommend", "watched", "comment", "watchedBecause"});
		moviesTable.setColumnHeaders(new String[] {"Name", "Rating", "Sadness", "Recommend", "Watched", "Comment", "Watch Because"});
		movieItemClickListener.setNewMovie(false);
		movieItemClickListener.setWatched(true);
		// moviesTable.removeItemClickListener(movieItemClickListener);
		// moviesTable.addItemClickListener(new MovieItemClickListener(false,
		// true));
	}

	private void setWatchedMoviesTable(ArrayList<MovieBean> watchedMovies) {
		BeanItemContainer<MovieBean> container = new BeanItemContainer<MovieBean>(MovieBean.class);
		container.addAll(watchedMovies);
		moviesTable.setCaption("Movies (Watched)");
		moviesTable.setContainerDataSource(container);
		moviesTable.setVisibleColumns(new Object[] {"name", "rating", "sadnessLevel", "recommend", "comment", "watchedBecause"});
		moviesTable.setColumnHeaders(new String[] {"Name", "Rating", "Sadness", "Recommend", "Comment", "Watch Because"});
		movieItemClickListener.setNewMovie(false);
		movieItemClickListener.setWatched(true);
		// moviesTable.removeItemClickListener(movieItemClickListener);
		// moviesTable.addItemClickListener(new MovieItemClickListener(false,
		// true));
	}

	private void setToWatchMoviesTable(ArrayList<MovieBean> toWatchMovies) {
		BeanItemContainer<MovieBean> container = new BeanItemContainer<MovieBean>(MovieBean.class);
		container.addAll(toWatchMovies);
		moviesTable.setCaption("Movies (To watch)");
		moviesTable.setContainerDataSource(container);
		moviesTable.setVisibleColumns(new Object[] { "name", "comment", "watchedBecause"});
		moviesTable.setColumnHeaders(new String[] { "Name", "Comment", "Watch Because"});
		movieItemClickListener.setNewMovie(false);
		movieItemClickListener.setWatched(false);
		// moviesTable.removeItemClickListener(movieItemClickListener);
		// moviesTable.addItemClickListener(new MovieItemClickListener(false,
		// false));
	}

	private void setRecommendedMoviesTable(ArrayList<MovieBean> recommendedMovies) {
		BeanItemContainer<MovieBean> container = new BeanItemContainer<MovieBean>(MovieBean.class);
		container.addAll(recommendedMovies);
		moviesTable.setCaption("Movies (Recommended)");
		moviesTable.setContainerDataSource(container);
		moviesTable.setVisibleColumns(new Object[] {"name", "rating", "sadnessLevel", "comment", "watchedBecause"});
		moviesTable.setColumnHeaders(new String[] {"Name", "Rating", "Sadness", "Comment", "Watch Because"});
		movieItemClickListener.setNewMovie(false);
		movieItemClickListener.setWatched(true);
		// moviesTable.removeItemClickListener(movieItemClickListener);
		// moviesTable.addItemClickListener(new MovieItemClickListener(false,
		// true));
	}

	// -------------------------------------------------//

	// --------------L I S T E N E R S------------------//
	// -------------------------------------------------//

	private class AllFilmsButtonListener implements ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			ArrayList<MovieBean> allMovieNames = portalDAO.getAllMovies();
			if (allMovieNames != null && allMovieNames.size() > 0) {
				setAllMoviesTable(allMovieNames);
			} else {
				Notification.show("Cannot load all movies");
				System.out.println("NO PortalDAO available to load Films=/");
			}
		}

	}

	private class WatchedFilmsButtonListener implements ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			ArrayList<MovieBean> watchedMovies = portalDAO.getAllWatchedMovies();

			if (watchedMovies != null && watchedMovies.size() > 0) {
				setWatchedMoviesTable(watchedMovies);
			} else {
				Notification.show("Cannot load all watched movies");
				System.out.println("NO PortalDAO available to load Films=/");
			}
		}

	}

	private class ToWatchFilmsButtonListener implements ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			ArrayList<MovieBean> toWatchMovies = portalDAO.getAllUnwatchedMovies();

			if (toWatchMovies != null && toWatchMovies.size() > 0) {
				setToWatchMoviesTable(toWatchMovies);
			} else {
				Notification.show("Cannot load all to-watch movies");
				System.out.println("NO PortalDAO available to load Films=/");
			}

		}

	}

	private class RecommendedFilmsButtonListener implements ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			ArrayList<MovieBean> toWatchMovies = portalDAO.getAllRecommendedMovies();

			if (toWatchMovies != null && toWatchMovies.size() > 0) {
				setRecommendedMoviesTable(toWatchMovies);
			} else {
				Notification.show("Cannot load all to-watch movies");
				System.out.println("NO PortalDAO available to load Films=/");
			}

		}

	}

	private class AddMovieButtonListener implements ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			editFlag = false;
			setMoviePopupWindow(true, false, null);// TODO

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
							portalDAO
									.editMovie(movieNameTextField.getValue(), true,
											Double.valueOf(movieRatingComboBox.getValue().toString()),
											movieCommentField.getValue() != null ? movieCommentField.getValue() : null,
											movieDateField.getValue() != null
													? new Date(movieDateField.getValue().getTime()) : null,
									movieRecommendedCheckBox.getValue(), 
									movieWatchedBecauseTextField.getValue(),
									movieSadnessLevelComboBox.getValue() != null ? Double.valueOf(movieSadnessLevelComboBox.getValue().toString()) : null,
									id);
						} else {
							portalDAO
									.addNewMovie(movieNameTextField.getValue(), true,
											Double.valueOf(movieRatingComboBox.getValue().toString()),
											movieCommentField.getValue() != null ? movieCommentField.getValue() : null,
											movieDateField.getValue() != null
													? new Date(movieDateField.getValue().getTime()) : null,
									movieRecommendedCheckBox.getValue(),
									movieWatchedBecauseTextField.getValue(),
									movieSadnessLevelComboBox.getValue() != null ? Double.valueOf(movieSadnessLevelComboBox.getValue().toString()) : null);
						}
					} else {
						Notification.show("Pls, fill all required fields");
					}
				} else {
					if (editFlag) {
						portalDAO.editMovie(movieNameTextField.getValue(), false, null, movieCommentField.getValue(),
								null, null, movieWatchedBecauseTextField.getValue(), null, id);
					} else {
						portalDAO.addNewMovie(movieNameTextField.getValue(), false, null, movieCommentField.getValue(),
								null, null, movieWatchedBecauseTextField.getValue(), null);
					}
				}
			} else {
				Notification.show("Pls, fill all required fields");
			}
			movieWindow.close();
			
		}
	}

	private class EditMovieButtonListener implements ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			editFlag = true;
			movieNameTextField.setVisible(true);
			movieWatchedCheckBox.setVisible(true);
			movieCommentField.setVisible(true);
			movieWatchedBecauseTextField.setVisible(true);
			movieNameTextField.setReadOnly(false);
			movieWatchedCheckBox.setReadOnly(false);
			movieCommentField.setReadOnly(false);
			movieWatchedBecauseTextField.setReadOnly(false);
			if (movieWatchedCheckBox.getValue()) {
				movieRatingComboBox.setVisible(true);
				movieRecommendedCheckBox.setVisible(true);
				movieDateField.setVisible(true);
				movieSadnessLevelComboBox.setVisible(true);
				movieRatingComboBox.setReadOnly(false);
				movieRecommendedCheckBox.setReadOnly(false);
				movieDateField.setReadOnly(false);
				movieSadnessLevelComboBox.setReadOnly(false);
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
			portalDAO.deleteMovie(id);
			movieWindow.close();
		}

	}

	public class AddMovieWatchedCheckBoxListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			if (movieWatchedCheckBox.getValue()) {
				movieRatingComboBox.setVisible(true);
				movieRecommendedCheckBox.setVisible(true);
				movieDateField.setVisible(true);
				movieSadnessLevelComboBox.setVisible(true);
			} else {
				movieRatingComboBox.setVisible(false);
				movieRecommendedCheckBox.setVisible(false);
				movieDateField.setVisible(false);
				movieSadnessLevelComboBox.setVisible(false);
			}
		}

	}

	private class MovieItemClickListener implements ItemClickListener {

		private boolean newMovie;
		private boolean watched;

		public MovieItemClickListener() {
		}

		public MovieItemClickListener(boolean newMovie, boolean watched) {
			this.newMovie = newMovie;
			this.watched = watched;
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
				setMoviePopupWindow(newMovie, watched, selectedMovie);
			}
		}

	}

	public class WikiAPIListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			getWiki();

		}

	}

	private class SeachMovieComboBoxListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			if (searchMovieComboBox.getValue() != null) {
				setMoviePopupWindow(false, true, (MovieBean) searchMovieComboBox.getValue());
			}
		}

	}

	public void getWiki() {

		String[] listOfMovieStrings = { "Coming Home (2014 film)", "Dancer in the Dark" };
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
