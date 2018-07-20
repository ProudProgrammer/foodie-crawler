package hu.gaborbalazs.crawler;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import hu.gaborbalazs.redis.entity.Menu;

@RunWith(MockitoJUnitRunner.class)
public class DezsoMenuCrawlerTests {

	private static final String TEST_URL = "http://test.com";

	private static final String FIELD_RESOURCE_URL = "resourceUrl";
	
	private static final String FIELD_CLOCK = "clock";
	
	private static final Clock CLOCK_FOR_MONDAY = Clock.fixed(LocalDate.of(2018, 7, 16).atStartOfDay().toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
	
	private static final Clock CLOCK_FOR_TUESDAY = Clock.fixed(LocalDate.of(2018, 7, 17).atStartOfDay().toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
	
	private static final Clock CLOCK_FOR_WEDNESDAY = Clock.fixed(LocalDate.of(2018, 7, 18).atStartOfDay().toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
	
	private static final Clock CLOCK_FOR_THURSDAY = Clock.fixed(LocalDate.of(2018, 7, 19).atStartOfDay().toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
	
	private static final Clock CLOCK_FOR_FRIDAY = Clock.fixed(LocalDate.of(2018, 7, 20).atStartOfDay().toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
	
	private static final Clock CLOCK_FOR_WEEKEND = Clock.fixed(LocalDate.of(2018, 7, 21).atStartOfDay().toInstant(ZoneOffset.UTC), ZoneId.systemDefault());

	private static final String TEST_PAGE = "<div class=\"page-content\">\r\n"
			+ "    <section class=\"sppb-section \" style=\"margin:0px;padding:170px 0px 100px;\">\r\n"
			+ "        <div class=\"sppb-container\">\r\n" + "            <div class=\"sppb-row\">\r\n"
			+ "                <div class=\"sppb-col-sm-12 \">\r\n"
			+ "                    <div class=\"sppb-addon-container\" style=\"\">\r\n"
			+ "                        <div class=\"sppb-addon sppb-addon-fancy-menu \">\r\n"
			+ "                            <div class=\"sppb-row sppb-no-gutter\">\r\n"
			+ "                                <div class=\"sppb-col-sm-7\">\r\n"
			+ "                                    <div class=\"sppb-addon-content\" style=\"\">\r\n"
			+ "                                        <i class=\"sb-bistro-menu sppb-fancy-menu-icon\"></i>\r\n"
			+ "                                        <h3 class=\"sppb-fancy-menu-title\">Heti menü</h3>\r\n"
			+ "                                        <div class=\"sp-fancy-menu-content\">\r\n"
			+ "                                            <ol>\r\n"
			+ "                                                <li class=\"sppb-wow fadeInLeft\" data-sppb-wow-delay=\"0ms\">\r\n"
			+ "                                                    <h4>Hétfő</h4>\r\n"
			+ "                                                    <span class=\"sppb-menu-price\">850 Ft</span>\r\n"
			+ "                                                    <div class=\"sppb-menu-dots\"></div>\r\n"
			+ "                                                    <div class=\"sppb-menu-text\">Karfiolleves\r\n"
			+ "                                                        <br />Szez&aacute;mmagos r&aacute;ntott csirkemell\r\n"
			+ "                                                        <br />Coleslaw sali, petr. burgonya\r\n"
			+ "                                                        <br />Desszert</div>\r\n"
			+ "                                                </li>\r\n"
			+ "                                                <li class=\"sppb-wow fadeInLeft\" data-sppb-wow-delay=\"100ms\">\r\n"
			+ "                                                    <h4>Kedd</h4>\r\n"
			+ "                                                    <span class=\"sppb-menu-price\">850 Ft</span>\r\n"
			+ "                                                    <div class=\"sppb-menu-dots\"></div>\r\n"
			+ "                                                    <div class=\"sppb-menu-text\">Csontleves\r\n"
			+ "                                                        <br />Z&ouml;ldbors&oacute;főzel&eacute;k\r\n"
			+ "                                                        <br />Fasirt\r\n"
			+ "                                                        <br />Desszert</div>\r\n"
			+ "                                                </li>\r\n"
			+ "                                                <li class=\"sppb-wow fadeInLeft\" data-sppb-wow-delay=\"200ms\">\r\n"
			+ "                                                    <h4>Szerda</h4>\r\n"
			+ "                                                    <span class=\"sppb-menu-price\">850 Ft</span>\r\n"
			+ "                                                    <div class=\"sppb-menu-dots\"></div>\r\n"
			+ "                                                    <div class=\"sppb-menu-text\">Brokkolikr&eacute;mleves\r\n"
			+ "                                                        <br />Z&ouml;lds&eacute;ges,currys rizsesh&uacute;s\r\n"
			+ "                                                        <br />Desszert</div>\r\n"
			+ "                                                </li>\r\n"
			+ "                                                <li class=\"sppb-wow fadeInLeft\" data-sppb-wow-delay=\"300ms\">\r\n"
			+ "                                                    <h4>Csütörtök</h4>\r\n"
			+ "                                                    <span class=\"sppb-menu-price\">850 Ft</span>\r\n"
			+ "                                                    <div class=\"sppb-menu-dots\"></div>\r\n"
			+ "                                                    <div class=\"sppb-menu-text\">Guly&aacute;sleves\r\n"
			+ "                                                        <br />Tejszines gomb&aacute;s penne\r\n"
			+ "                                                        <br />Desszert</div>\r\n"
			+ "                                                </li>\r\n"
			+ "                                                <li class=\"sppb-wow fadeInLeft\" data-sppb-wow-delay=\"400ms\">\r\n"
			+ "                                                    <h4>Péntek</h4>\r\n"
			+ "                                                    <span class=\"sppb-menu-price\">850 Ft</span>\r\n"
			+ "                                                    <div class=\"sppb-menu-dots\"></div>\r\n"
			+ "                                                    <div class=\"sppb-menu-text\">Paradicsomleves\r\n"
			+ "                                                        <br />T&ouml;lt&ouml;tt csirkecomb\r\n"
			+ "                                                        <br />burgonyap&uuml;r&eacute;\r\n"
			+ "                                                        <br />Desszert</div>\r\n"
			+ "                                                </li>\r\n"
			+ "                                            </ol>\r\n"
			+ "                                        </div>\r\n" + "                                    </div>\r\n"
			+ "                                </div>\r\n"
			+ "                                <div class=\"sppb-col-sm-5 sbbp-img-container\">\r\n"
			+ "                                    <div class=\"position-right-bottom\">\r\n"
			+ "                                        <img src=\"/images/cta3.jpg\" alt=\"Heti menü\">\r\n"
			+ "                                    </div>\r\n" + "                                </div>\r\n"
			+ "                            </div>\r\n" + "                        </div>\r\n"
			+ "                    </div>\r\n" + "                </div>\r\n" + "            </div>\r\n"
			+ "        </div>\r\n" + "    </section>\r\n" + "</div>\r\n" + "</div>";

	@Mock
	private Logger logger;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private DezsoMenuCrawler underTest;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		Mockito.when(restTemplate.getForObject(ArgumentMatchers.eq(TEST_URL), ArgumentMatchers.eq(String.class)))
				.thenReturn(TEST_PAGE);

		ReflectionTestUtils.setField(underTest, FIELD_RESOURCE_URL, TEST_URL);
	}

	@Test
	public void testIfPageCannotBeDownloaded() {
		Mockito.when(restTemplate.getForObject(ArgumentMatchers.eq(TEST_URL), ArgumentMatchers.eq(String.class)))
				.thenThrow(RestClientException.class);
		int expectedSize = 0;
		Menu menu = underTest.getMenu();
		Assert.assertEquals(expectedSize, menu.getElements().size());
	}

	@Test
	public void testWithMonday() {
		ReflectionTestUtils.setField(underTest, FIELD_CLOCK, CLOCK_FOR_MONDAY);
		int expectedSize = 4;
		Menu menu = underTest.getMenu();
		Assert.assertEquals(expectedSize, menu.getElements().size());
	}

	@Test
	public void testWithTuesday() {
		ReflectionTestUtils.setField(underTest, FIELD_CLOCK, CLOCK_FOR_TUESDAY);
		int expectedSize = 4;
		Menu menu = underTest.getMenu();
		Assert.assertEquals(expectedSize, menu.getElements().size());
	}

	@Test
	public void testWithWednesday() {
		ReflectionTestUtils.setField(underTest, FIELD_CLOCK, CLOCK_FOR_WEDNESDAY);
		int expectedSize = 3;
		Menu menu = underTest.getMenu();
		Assert.assertEquals(expectedSize, menu.getElements().size());
	}

	@Test
	public void testWithThursday() {
		ReflectionTestUtils.setField(underTest, FIELD_CLOCK, CLOCK_FOR_THURSDAY);
		int expectedSize = 3;
		Menu menu = underTest.getMenu();
		Assert.assertEquals(expectedSize, menu.getElements().size());
	}

	@Test
	public void testWithFriday() {
		ReflectionTestUtils.setField(underTest, FIELD_CLOCK, CLOCK_FOR_FRIDAY);
		int expectedSize = 4;
		Menu menu = underTest.getMenu();
		Assert.assertEquals(expectedSize, menu.getElements().size());
	}

	@Test
	public void testWithWeekend() {
		ReflectionTestUtils.setField(underTest, FIELD_CLOCK, CLOCK_FOR_WEEKEND);
		int expectedSize = 0;
		Menu menu = underTest.getMenu();
		Assert.assertEquals(expectedSize, menu.getElements().size());
	}
}
