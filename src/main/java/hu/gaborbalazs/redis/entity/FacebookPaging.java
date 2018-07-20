package hu.gaborbalazs.redis.entity;

public class FacebookPaging {

	private String previous;
	private String next;

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "FacebookPaging [previous=" + previous + ", next=" + next + "]";
	}

}
