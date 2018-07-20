package hu.gaborbalazs.crawler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import hu.gaborbalazs.enums.Restaurant;
import hu.gaborbalazs.exception.ExceptionMessage;
import hu.gaborbalazs.redis.entity.FacebookPostPage;
import hu.gaborbalazs.redis.entity.Menu;

@Component
public class DagobaMenuCrawler implements MenuCrawler {

	private static final int PAGE_LIMIT = 10;

	@Autowired
	private Logger logger;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${dagoba.resource}")
	private String resourceUrl;

	@Value("${dagoba.url}")
	private String url;

	@Value("${facebook.page-limit}")
	private String facebookPageLimit;

	@Override
	public Menu getMenu() {
		List<String> menuElements = new ArrayList<>();
		try {
			FacebookPostPage postPage = restTemplate.getForObject(
					resourceUrl + "?access_token={access_token}&limit={limit}", FacebookPostPage.class,
					System.getenv("FACEBOOK_ACCESS_TOKEN"), parsePageLimit(facebookPageLimit));
			postPage.getPosts().forEach(post -> menuElements.add(post.getMessage()));
			logger.debug(">> " + postPage);
		} catch (RestClientException e) {
			logger.error(ExceptionMessage.WRONG_FACEBOOK_ACCESS_TOKEN, e);
		}
		return new Menu(Restaurant.DAGOBA.getId(), Restaurant.DAGOBA.getName(), url, menuElements);
	}

	private String parsePageLimit(String pageLimit) {
		int pageLimitAsInt = PAGE_LIMIT;
		try {
			pageLimitAsInt = Integer.parseInt(pageLimit);
		} catch (NumberFormatException e) {
			logger.warn(ExceptionMessage.WRONG_FACEBOOK_PAGE_LIMIT);
		}
		return Integer.toString(pageLimitAsInt);
	}

}
