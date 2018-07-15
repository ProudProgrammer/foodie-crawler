package hu.gaborbalazs.crawler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.gaborbalazs.enums.Restaurant;
import hu.gaborbalazs.exception.ExceptionMessage;
import hu.gaborbalazs.redis.entity.Menu;

//@Component
public class DezsoMenuCrawler implements MenuCrawler {

	@Autowired
	private Logger logger;

	@Value("${dezsoba}")
	private String url;

	@Override
	public Menu getMenu() {
		List<String> menuElements = new ArrayList<>();
		try {
			Document document = Jsoup.connect(url).get();
			Elements elements = document.select("div.sppb-menu-text");
			elements.forEach(element -> logger.info(element.html()));
		} catch (IOException e) {
			logger.error(ExceptionMessage.JSOUP_PARSE + " " + url, e);
		}
		return new Menu(Restaurant.DEZSOBA.getId(), Restaurant.DEZSOBA.getName(), menuElements);
	}
	
	private String getDay() {
		switch (LocalDate.now().getDayOfWeek()) {
		case MONDAY:
			return "Hétfő";
		case TUESDAY:
			return "Kedd";
		case WEDNESDAY:
			return "Szerda";
		case THURSDAY:
			return "Csütörtök";
		case FRIDAY:
			return "Péntek";
		default:
			return "";
		}
	}

}
