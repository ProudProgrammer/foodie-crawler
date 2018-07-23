package hu.gaborbalazs.crawler;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import hu.gaborbalazs.enums.Restaurant;
import hu.gaborbalazs.exception.ExceptionMessage;
import hu.gaborbalazs.redis.entity.Menu;

@Component
public class DezsoMenuCrawler implements MenuCrawler {

	private static final Locale HUN_LOCALE = new Locale("hu", "HU");

	private static final String DAILY_MENU_REGEX = "(.|\\s)*?</li>";

	private static final Pattern MENU_ELEMENTS_PATTERN = Pattern.compile("<div class=\"sppb-menu-text\">(.|\\s)*?</div>");

	private static final String REPLACE_1 = "<div class=\"sppb-menu-text\">";

	private static final String REPLACE_2 = "</div>";

	private static final String SPLIT = "<br />";

	@Autowired
	private Logger logger;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Clock clock;

	@Value("${dezsoba.resource}")
	private String resourceUrl;

	@Value("${dezsoba.url}")
	private String url;

	@Override
	public Menu getMenu() {
		Menu menu = new Menu(Restaurant.DEZSOBA.getId(), Restaurant.DEZSOBA.getName(), url, new ArrayList<>());
		String menuAsString = "";
		String htmlPage = "";
		try {
			htmlPage = restTemplate.getForObject(resourceUrl, String.class);
		} catch (RestClientException e) {
			logger.error(ExceptionMessage.HTML_PAGE_DOWNLOAD, e);
			return menu;
		}
		Pattern patternForDailyMenu = Pattern.compile(
				LocalDate.now(clock).getDayOfWeek().getDisplayName(TextStyle.FULL, HUN_LOCALE) + DAILY_MENU_REGEX,
				Pattern.CASE_INSENSITIVE);
		Matcher pageMatcher = patternForDailyMenu.matcher(htmlPage);
		if (pageMatcher.find()) {
			menuAsString = pageMatcher.group(0);
			logger.debug(menuAsString);
			Matcher menuElementMatcher = MENU_ELEMENTS_PATTERN.matcher(menuAsString);
			if (menuElementMatcher.find()) {
				menuAsString = menuElementMatcher.group(0);
				menuAsString = menuAsString.replace(REPLACE_1, "").replace(REPLACE_2, "");
			}
		}
		menuAsString = StringEscapeUtils.unescapeHtml4(menuAsString);
		Arrays.asList(menuAsString.split(SPLIT)).stream().filter(StringUtils::isNoneBlank)
				.forEach(menu.getElements()::add);
		return menu;
	}

}
