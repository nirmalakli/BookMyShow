import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Theatre {

	private final Screens screens;

	public Theatre(Screens screens) {
		this.screens = screens;
	}

	public void addMovie(Movie movie, List<String> screenNames, LocalDate showDate, LocalTime... timings) {
		screens.addMovie(movie, screenNames, showDate, timings);
	}

	public List<Show> getShows(Movie movie, LocalDate today) {
		return screens.getShows(movie, today);
	}

	public Tickets book(Movie movie , String screenName, LocalDate date, LocalTime time) {
		return book(movie, screenName, date, time, 1);
	}

	public Tickets book(Movie movie , String screenName, LocalDate date, LocalTime time, int numOfTickets) {
		final LocalDate now = LocalDate.now();
		if (now.isAfter(date)) {
			throw new UnsupportedOperationException();
		}
		if ((Math.abs(ChronoUnit.DAYS.between(now, date)) >= 2))
			throw new UnsupportedOperationException();
		if (numOfTickets > 10)
			throw new UnsupportedOperationException();
		
		return screens.book(movie, screenName, date, time, numOfTickets);
	}

	public void pullOutMovie(Movie movie , List<String> screenNames, LocalDate date) {
		final LocalDate now = LocalDate.now();
		if (now.isAfter(date)) {
			throw new UnsupportedOperationException();
		}
		if ((Math.abs(ChronoUnit.DAYS.between(now, date)) <= 2))
			throw new UnsupportedOperationException();
		
		screens.pullOutMovie(movie, screenNames, date);
	}
}
