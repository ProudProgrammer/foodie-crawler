package hu.gaborbalazs.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.gaborbalazs.redis.entity.Menu;
import hu.gaborbalazs.redis.repository.MenuRepository;

@RestController
public class FoodieCrawlerController {

	@Autowired
	private MenuRepository menuRepository;

	@GetMapping("/menus")
	public List<Menu> getMenus() {
		List<Menu> menus = new ArrayList<>();
		menuRepository.findAll().forEach(menu -> menus.add(menu));
		return menus;
	}
}
