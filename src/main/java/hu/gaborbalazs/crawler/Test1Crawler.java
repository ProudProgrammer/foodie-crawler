package hu.gaborbalazs.crawler;

import java.util.Arrays;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.gaborbalazs.redis.entity.Menu;

@Component
public class Test1Crawler implements MenuCrawler {

	@Autowired
	private Logger logger;
	
	@Override
	public Menu getMenu() {
		logger.debug("Test1Crawler is crawling...");
		return new Menu("Test1ID", "Test1Restaurant", Arrays.asList("Food1", "Food2"));
	}

}
