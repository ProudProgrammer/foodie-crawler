package hu.gaborbalazs.batch;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import hu.gaborbalazs.crawler.DezsoMenuCrawler;
import hu.gaborbalazs.crawler.MenuCrawler;
import hu.gaborbalazs.enums.Restaurant;
import hu.gaborbalazs.redis.repository.MenuRepository;

@Component
public class CrawlerScheduler {

	@Autowired
	private Logger logger;

	@Autowired
	private MenuRepository menuRepository;

//	@Autowired
//	private DezsoMenuCrawler dezsoMenuCrawler;
	
	@Autowired
	private List<MenuCrawler> crawlers;

	@Scheduled(cron = "0 0/10 * * * ?")
	public void crawl() {
		logger.info("Crawlers are running...");
//		if (!menuRepository.findById(Restaurant.DEZSOBA.getId()).isPresent()) {
//			menuRepository.save(dezsoMenuCrawler.getMenu());
//		}
		crawlers.forEach(MenuCrawler::getMenu);
	}
}
