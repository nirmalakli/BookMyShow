import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Screen {

	private Integer size;
	private String name;
	private Map<LocalDate,List<Show>> dateToMovieMap = new HashMap<>();
	
	public Screen(Integer size, String name) {
		this.size = size;
		this.name = name;
	}

	public void addMovie(Movie m, LocalDate date, LocalTime... timings) {
		
		if(timings.length == 0) {
			addMovie(m, date, LocalTime.of(10, 0));
			return;
		}
		
		for (LocalTime time : timings) {
			addMovie(m, date, time);
		}
	}

	public void addMovie(Movie m, LocalDate date, LocalTime time) {
		final Show show = new Show(m, time, size);
		List<Show> shows = dateToMovieMap.get(date);
		if (shows == null) {
			shows = new ArrayList<>();
			dateToMovieMap.put(date, shows);
		}
		shows.add(show);
	}

	public boolean isMoviePresent(Movie m, LocalDate date) {
		return dateToMovieMap.get(date) != null && dateToMovieMap.get(date).stream().anyMatch(show -> show.getMovie().equals(m));
	}

	public int getAvailableSeats() {
		return size;
	}

	public Tickets book(Movie m, LocalDate today, LocalTime time, int numOfTickets) {
		
		Optional<Show> currentShow = dateToMovieMap.get(today).stream().filter(show->show.getTime().equals(time)).findFirst() ;
		if(currentShow.isPresent()){
			Show currentShowObj = currentShow.get();
			Show newShowObj = currentShowObj.book(numOfTickets);
			
			List<Show> shows = dateToMovieMap.get(today);
			shows.remove(currentShowObj);
			shows.add(newShowObj);
			
			int prevSeats = currentShowObj.getAvailableSeats();
			int newSeats = newShowObj.getAvailableSeats();
			List<String> seatNumbers = IntStream.range(newSeats,prevSeats).boxed().map(i-> ((Integer)i).toString()).collect(Collectors.toList());
			return new Tickets(m, today, time,seatNumbers);
		}
		return Tickets.emptyTicket;
	}

	public Stream<Show> getShows(Movie m, LocalDate today) {
		return dateToMovieMap.get(today).stream().filter(show -> show.getMovie().equals(m));
	}

	public void pullOutMovie(Movie m, LocalDate date) {
		List<Show> shows = dateToMovieMap.get(date);
		if (shows != null) {
			List<Show> newShows = shows.stream().filter(show -> !show.getMovie().equals(m)).collect(Collectors.toList());
			dateToMovieMap.put(date, newShows);
		}
	}

	
	@Override
	public String toString() {
		return "Screen [size=" + size + ", name=" + name + ", dateToMovieMap=" + dateToMovieMap + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Screen other = (Screen) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		return true;
	}
	
	public boolean nameMatches(String... names) {
		return Stream.of(names).anyMatch(screenName -> name.equals(screenName));
	}
	
	public boolean nameMatches(Collection<String> names) {
		return names.stream().anyMatch(screenName -> name.equals(screenName));
	}
	

}
