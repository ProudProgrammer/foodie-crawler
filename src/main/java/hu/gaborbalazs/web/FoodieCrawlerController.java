package hu.gaborbalazs.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hu.gaborbalazs.redis.entity.Menu;
import hu.gaborbalazs.redis.repository.MenuRepository;

@RestController
public class FoodieCrawlerController {

	@Autowired
	private MenuRepository menuRepository;

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/menus", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public List<Menu> getMenus() {
		List<Menu> menus = new ArrayList<>();
		menuRepository.findAll().forEach(menus::add);
		return menus;
	}
}
