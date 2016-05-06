import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tickets {
	

	public static Tickets emptyTicket = new Tickets(null, null, null, Collections.emptyList());
	private Movie movie;
	private LocalTime time;
	private LocalDate date;
	private List<String> seatNumbers;

	public Tickets(Movie movie, LocalDate date, LocalTime time, List<String> seatNumbers) {
		this.movie = movie;
		this.time = time;
		this.date = date;
		this.seatNumbers = new ArrayList<>(seatNumbers);
	}

	public Object getMovie() {
		return movie;
	}

	public Object getTime() {
		return time;
	}

	public Object getDate() {
		return date;
	}
	
	public boolean isBooked() {
		return !seatNumbers.isEmpty();
	}
	
	public List<String> getSeatNumbers() {
		return Collections.unmodifiableList(seatNumbers);
	}

}
