package hu.gaborbalazs.crawler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import hu.gaborbalazs.enums.Restaurant;
import hu.gaborbalazs.redis.entity.Menu;

@Component
public class DezsoMenuCrawler implements MenuCrawler {

	private static final String PATTERN_FOR_DAILY_MENU = ".*?</li>";

	private static final String PATTERN_FOR_MENU_ELEMENTS = "<div class=\"sppb-menu-text\">.*?</div>";

	private static final String REPLACE_1 = "<div class=\"sppb-menu-text\">";

	private static final String REPLACE_2 = "</div>";

	private static final String SPLIT = "<br />";

	@Autowired
	private Logger logger;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${dezsoba}")
	private String url;

	@Override
	public Menu getMenu() {
		logger.debug("DezsoMenuCrawler is crawling...");

		List<String> menuElements = new ArrayList<>();
		String menu = "";
		String page = restTemplate.getForObject(url, String.class);
		Pattern patternForDailyMenu = Pattern.compile(getDay() + PATTERN_FOR_DAILY_MENU);
		Pattern patternForMenuElements = Pattern.compile(PATTERN_FOR_MENU_ELEMENTS);
		Matcher pageMatcher = patternForDailyMenu.matcher(page);
		if (pageMatcher.find()) {
			menu = pageMatcher.group(0);
			Matcher menuElementMatcher = patternForMenuElements.matcher(menu);
			if (menuElementMatcher.find()) {
				menu = menuElementMatcher.group(0);
				menu = menu.replace(REPLACE_1, "").replace(REPLACE_2, "");
			}
		}
		menu = StringEscapeUtils.unescapeHtml4(menu);
		Arrays.asList(menu.split(SPLIT)).forEach(menuElements::add);
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
