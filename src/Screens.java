import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Screens {

	private final List<Screen> screens;

	public Screens(List<Screen> screens) {
		this.screens = screens;
	}
	
	public Optional<Screen> getScreen(String screenName) {
		return screens.stream().filter(screen -> screen.nameMatches(screenName)).findFirst();
	}

	public void addMovie(Movie m, List<String> screenNames, LocalDate showDate, LocalTime[] timings) {
		Stream<Screen> matchingScreens = screens.stream().filter(screen -> screen.nameMatches(screenNames));
		matchingScreens.forEach(screen -> screen.addMovie(m, showDate, timings));
	}

	public List<Show> getShows(Movie movie, LocalDate today) {
		final Stream<Screen> screensPlayingMovie = screens.stream().filter(screen -> screen.isMoviePresent(movie, today));
		final Stream<Show> showsPlayingMovie = screensPlayingMovie.flatMap(screen -> screen.getShows(movie, today));		
		return showsPlayingMovie.collect(Collectors.toList());
	}

	public Tickets book(Movie movie, String screenName, LocalDate date, LocalTime time, int numOfTickets) {
		Optional<Screen> screenOpt = getScreen(screenName);
		if(!screenOpt.isPresent()) {
			throw new IllegalArgumentException(String.format("Screen %s is not present" , screenName));
		}
		
		Screen screen = screenOpt.get();
		return screen.book(movie, date, time, numOfTickets);
	}

	public void pullOutMovie(Movie movie, List<String> screenNames, LocalDate date) {
		Stream<Screen> screensPlayingMovie = screenNames.stream().map(name -> getScreen(name)).filter(Optional::isPresent).map(Optional::get);
		screensPlayingMovie.forEach(screen -> screen.pullOutMovie(movie, date));
	}
	
}
