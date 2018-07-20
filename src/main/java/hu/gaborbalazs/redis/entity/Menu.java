package hu.gaborbalazs.redis.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.RedisHash;

@SuppressWarnings("serial")
@RedisHash("Menu")
public class Menu implements Serializable {

	private String id;
	private String restaurant;
	private String url;
	private List<String> elements;

	public Menu() {
	}

	public Menu(String id, String restaurant, String url, List<String> elements) {
		super();
		this.id = id;
		this.restaurant = restaurant;
		this.url = url;
		this.elements = elements;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getElements() {
		if (elements == null) {
			elements = new ArrayList<>();
		}
		return elements;
	}

	public void setElements(List<String> elements) {
		this.elements = elements;
	}

	@Override
	public String toString() {
		return "Menu [id=" + id + ", restaurant=" + restaurant + ", url=" + url + ", elements=" + elements + "]";
	}

}
