package hu.gaborbalazs.crawler;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import hu.gaborbalazs.enums.Restaurant;
import hu.gaborbalazs.exception.ExceptionMessage;
import hu.gaborbalazs.ocr.CharacterRecognizer;
import hu.gaborbalazs.redis.entity.Menu;

@Component
public class TenminutesMenuCrawler implements MenuCrawler {

	@Autowired
	private Logger logger;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private CharacterRecognizer characterRecognizer;

	@Autowired
	@Qualifier("TenMinutesImagePath")
	private String imagePath;

	@Value("${tenminutes.resource}")
	private String imageUrl;

	@Value("${tenminutes.url}")
	private String url;

	@Override
	public Menu getMenu() {
		List<String> menuElements = new ArrayList<>();
		try {
			byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);
			Files.write(Paths.get(imagePath), imageBytes);
			menuElements.addAll(characterRecognizer.getLinesFromImage(imagePath));
		} catch (Exception e) {
			logger.error(ExceptionMessage.IMAGE_DOWNLOAD_OR_SAVE, e);
		}
		return new Menu(Restaurant.TENMINUTES.getId(), Restaurant.TENMINUTES.getName(), url, menuElements);
	}
}
