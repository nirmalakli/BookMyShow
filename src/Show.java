import java.time.LocalTime;

public class Show {

	private Movie m;
	private LocalTime time;
	private Integer size;

	public Show(Movie m, LocalTime time, Integer size) {
		this.m = m;
		this.time = time;
		this.size = size;
	}

	public Movie getMovie() {
		return m;
	}

	public Object getTime() {
		return time;
	}

	public Show book(int numOfTickets) {
		if(size<numOfTickets || size <= 0)throw new RuntimeException();
		
		return new Show(m,time,size-numOfTickets);
	}

	public int getAvailableSeats() {
		return size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m == null) ? 0 : m.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		Show other = (Show) obj;
		if (m == null) {
			if (other.m != null)
				return false;
		} else if (!m.equals(other.m))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Show [m=" + m + ", time=" + time + ", size=" + size + "]";
	}
}
