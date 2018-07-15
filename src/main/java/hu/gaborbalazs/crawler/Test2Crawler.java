package hu.gaborbalazs.crawler;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.gaborbalazs.redis.entity.Menu;

@Component
public class Test2Crawler implements MenuCrawler {

	@Autowired
	private Logger logger;

	@Override
	public Menu getMenu() {
		logger.info("test2crawler");
		return null;
	}

}
