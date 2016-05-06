import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

/*
 * Book My Show
Admin Persona :-
1.	I can define number of screens & capacity of each screen
2.	I should be able to add a movie for screens available and its timings.
Movie Details: Movie Name
3.	I can add a movie in advance. 
Like  I can add movie “Sachin: A Billion Dreams” right now mentioning that it would be on Screens 1, 3, 6, 8 starting from 1st June 2016.
4.	I should be able to pull a movie out @ the max 3 days in advance.

Online booking Persona :-
1.	I can search by movie name and it should present 
a.	if movie is available on any/all screens with timing of each
b.	Number of seats available
2.	I can only book for Today, Tomorrow and Day After Tomorrow.
3.	I can book @ the max 10 seats @ a time

 */
public class TheatreSpec {
	private static final Integer ONE_SCREEN = 1;
	private static final Integer SCREEN_CAPACITY_20 = 20;
	private final List<String> singleScreenName = Arrays.asList("A");
	private final List<String> multipleScreenNames = Arrays.asList("A", "B", "C", "D", "E");

	private final List<Integer> singleScreenCapacities = Arrays.asList(20);
	private final List<Integer> multipleScreenCapacities = Arrays.asList(20, 50, 100, 200, 10);

	private LocalTime _10_OClock = LocalTime.of(10, 0);
	private LocalTime _12_OClock = LocalTime.of(12, 0);
	private LocalDate _1_June = LocalDate.of(2016, Month.JUNE, 1);
	private LocalDate _2_June = LocalDate.of(2016, Month.JUNE, 2);

	@Test
	public void adminAddsMovieToATheatre() throws Exception {
		// Given
		Theatre t = createSingleScreenTheatre();
		Movie m = new Movie("THE SOCIAL NETWORK");

		// When
		t.addMovie(m, Arrays.asList("A"), _1_June);

		assertFalse(t.getShows(m, _1_June).isEmpty());
	}
	
	@Test
	public void adminAddsMovieToAMultiplex() throws Exception {
		// Given
		Theatre t = createMultipleScreenTheatre();
		Movie m = new Movie("THE SOCIAL NETWORK");
		List<String> screensNames = Arrays.asList("A", "D");
		
		// When
		t.addMovie(m, screensNames, _1_June);

		assertFalse(t.getShows(m, _1_June).isEmpty());
	}


	@Test
	public void adminAddsMovieWithTimingsToATheatre() throws Exception {
		// Given
		Theatre t = createSingleScreenTheatre();
		Movie m = new Movie("THE SOCIAL NETWORK");
		LocalDate _1_June = LocalDate.of(2016, Month.JUNE, 1);

		// When
		t.addMovie(m, singleScreenName, _1_June, _10_OClock, _12_OClock);

		assertEquals(2, t.getShows(m, _1_June).size());
	}

	@Test
	public void adminPullsOutAMovie() throws Exception {
		// Given
		Theatre t = createSingleScreenTheatre();
		Movie m = new Movie("THE SOCIAL NETWORK");
		LocalDate _1_June = LocalDate.of(2016, Month.JUNE, 1);
		LocalDate _2_June = LocalDate.of(2016, Month.JUNE, 2);

		t.addMovie(m, singleScreenName, _1_June, _10_OClock);
		t.addMovie(m, singleScreenName, _2_June, _10_OClock);

		// When
		t.pullOutMovie(m, singleScreenName, _1_June);

		assertTrue(t.getShows(m, _1_June).isEmpty());
		assertEquals(1, t.getShows(m, _2_June).size());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void adminCannotPullOutAMovie3DaysBeforeRelease() throws Exception {
		// Given
		Theatre t = createSingleScreenTheatre();
		Movie m = new Movie("THE SOCIAL NETWORK");
		LocalDate now = LocalDate.now();
		LocalDate tomorrow = now.plusDays(1);

		t.addMovie(m, singleScreenName, tomorrow, _10_OClock);

		// When
		t.pullOutMovie(m, singleScreenName, tomorrow);
	}

	@Test
	public void userSeesMovies() throws Exception {
		// Given
		Theatre t = createSingleScreenTheatre();
		Movie m = new Movie("THE SOCIAL NETWORK");
		LocalDate today = LocalDate.now();

		// When
		t.addMovie(m, singleScreenName, today);
		// Then
		List<Show> availableScreens = t.getShows(m, today);
		assertEquals(1, availableScreens.size());
		assertEquals(20, availableScreens.get(0).getAvailableSeats());

	}

	@Test
	public void userBooksTicket() throws Exception {
		// Given
		Theatre t = createSingleScreenTheatre();
		Movie m = new Movie("THE SOCIAL NETWORK");
		LocalDate today = LocalDate.now();
		LocalTime defaultTime = LocalTime.of(10, 0);
		// When
		t.addMovie(m, Arrays.asList("A"), today);
		// Then
		assertTrue(t.book(m, "A", today, defaultTime).isBooked());
		List<Show> availableShows = t.getShows(m, today);
		assertEquals(1, availableShows.size());
		assertEquals(19, availableShows.get(0).getAvailableSeats());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void userBooksTicketThreeDaysHence() throws Exception {
		// Given
		Theatre t = createSingleScreenTheatre();
		Movie m = new Movie("THE SOCIAL NETWORK");
		LocalDate threeDaysHence = LocalDate.now().plusDays(3);
		LocalTime defaultTime = LocalTime.of(10, 0);
		// When
		t.addMovie(m, Arrays.asList("A"), threeDaysHence);
		// Then
		t.book(m, "A", threeDaysHence, defaultTime);
	}

	@Test
	public void userBooksMultipleTickets() throws Exception {
		// Given
		Theatre t = createSingleScreenTheatre();
		Movie m = new Movie("THE SOCIAL NETWORK");
		List<Integer> screens = Arrays.asList(0);
		LocalDate today = LocalDate.now();
		LocalTime defaultTime = LocalTime.of(10, 0);
		// When
		t.addMovie(m, Arrays.asList("A"), today);
		// Then
		assertTrue(t.book(m, "A", today, defaultTime, 2).isBooked());
		List<Show> availableShows = t.getShows(m, today);
		assertEquals(1, availableShows.size());
		assertEquals(18, availableShows.get(0).getAvailableSeats());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void userCannotBookMoreThan10Tickets() throws Exception {
		// Given
		Theatre t = createSingleScreenTheatre();
		Movie m = new Movie("THE SOCIAL NETWORK");
		LocalDate today = LocalDate.now();
		LocalTime defaultTime = LocalTime.of(10, 0);
		// When
		t.addMovie(m, singleScreenName, today);
		// Then
		assertTrue(t.book(m, "A", today, defaultTime, 11).isBooked());

	}

	@Test
	public void bookShouldReturnTicketwithSeatNumber() throws Exception {
		// Given
		Theatre t = createSingleScreenTheatre();
		Movie m = new Movie("THE SOCIAL NETWORK");
		LocalDate today = LocalDate.now();
		LocalTime defaultTime = LocalTime.of(10, 0);
		
		// When
		t.addMovie(m, singleScreenName, today);
		
		// Then
		Tickets tickets = t.book(m, "A", today, defaultTime, 2);
		assertEquals(m, tickets.getMovie());
		assertEquals(defaultTime, tickets.getTime());
		assertEquals(today, tickets.getDate());
		assertEquals(2, tickets.getSeatNumbers().size());
	}

	@Test(expected = RuntimeException.class)
	public void canNotBookAboveCapacity() throws Exception {
		// Given
		Theatre t = createTheatre(1, Arrays.asList("A"), Arrays.asList(5));
		Movie m = new Movie("THE SOCIAL NETWORK");
		LocalDate today = LocalDate.now();
		LocalTime defaultTime = LocalTime.of(10, 0);
		
		// When
		t.addMovie(m, singleScreenName, today);
		
		// Then
		t.book(m, "A", today, defaultTime, 6);
	}
	
	public Theatre createSingleScreenTheatre() {
		return createTheatre(1, singleScreenName, singleScreenCapacities);
	}
	
	public Theatre createMultipleScreenTheatre() {
		return createTheatre(5, multipleScreenNames, multipleScreenCapacities);
	}
	
	public Theatre createTheatre(final Integer numberOfScreens, final List<String> screenNames,
			final List<Integer> screenCapacities) {
		
		List<Screen> allScreens = new ArrayList<>(numberOfScreens);
		for (int i = 0; i < numberOfScreens; i++) {
			allScreens.add(new Screen(screenCapacities.get(i), screenNames.get(i)));
		}
		return new Theatre(new Screens(allScreens));
	}
}
