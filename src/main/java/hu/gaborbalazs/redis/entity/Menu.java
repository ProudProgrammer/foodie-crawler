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
	private List<String> elements;

	public Menu() {
	}

	public Menu(String id, String restaurant, List<String> elements) {
		super();
		this.id = id;
		this.restaurant = restaurant;
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

	public List<String> getElements() {
		if (elements.isEmpty()) {
			elements = new ArrayList<>();
		}
		return elements;
	}

	public void setElements(List<String> elements) {
		this.elements = elements;
	}

}
