package hu.gaborbalazs.crawler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import hu.gaborbalazs.redis.entity.FacebookPost;
import hu.gaborbalazs.redis.entity.FacebookPostPage;
import hu.gaborbalazs.redis.entity.Menu;

@RunWith(MockitoJUnitRunner.class)
public class DagobaMenuCrawlerTests {

	private static final String TEST_URL = "http://test.com";

	private static final String QUERY_PARAMETERS = "?access_token={access_token}&limit={limit}";

	private static final String FIELD_RESOURCE_URL = "resourceUrl";

	private static final String FIELD_FACEBOOK_PAGE_LIMIT = "facebookPageLimit";

	@Mock
	private Logger logger;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private DagobaMenuCrawler underTest;

	private FacebookPostPage facebookPostPage;

	private String urlWithParameters;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(underTest, FIELD_RESOURCE_URL, TEST_URL);

		urlWithParameters = TEST_URL + QUERY_PARAMETERS;

		facebookPostPage = new FacebookPostPage();
		List<FacebookPost> facebookPosts = new ArrayList<>();
		FacebookPost facebookPost1 = new FacebookPost("id1", new Date(), "message1");
		FacebookPost facebookPost2 = new FacebookPost("id2", new Date(), "message2");
		facebookPosts.add(facebookPost1);
		facebookPosts.add(facebookPost2);
		facebookPostPage.setPosts(facebookPosts);
	}

	@Test
	public void testWithProperFacebookAccessToken() {
		ReflectionTestUtils.setField(underTest, FIELD_FACEBOOK_PAGE_LIMIT, "7");
		Mockito.when(restTemplate.getForObject(ArgumentMatchers.eq(urlWithParameters),
				ArgumentMatchers.eq(FacebookPostPage.class), Mockito.<String>any())).thenReturn(facebookPostPage);
		int expectedSize = 2;
		Menu menu = underTest.getMenu();
		Assert.assertEquals(expectedSize, menu.getElements().size());
	}

	@Test
	public void testWithWrongFacebookAccessToken() {
		Mockito.when(restTemplate.getForObject(ArgumentMatchers.eq(urlWithParameters),
				ArgumentMatchers.eq(FacebookPostPage.class), Mockito.<Object>any()))
				.thenThrow(RestClientException.class);
		int expectedSize = 0;
		Menu menu = underTest.getMenu();
		Assert.assertEquals(expectedSize, menu.getElements().size());
	}

	@Test
	public void testWithWrongFacebookPageLimit() {
		ReflectionTestUtils.setField(underTest, FIELD_FACEBOOK_PAGE_LIMIT, "xy");
		Mockito.when(restTemplate.getForObject(ArgumentMatchers.eq(urlWithParameters),
				ArgumentMatchers.eq(FacebookPostPage.class), Mockito.<Object>any())).thenReturn(facebookPostPage);
		int expectedSize = 2;
		Menu menu = underTest.getMenu();
		Assert.assertEquals(expectedSize, menu.getElements().size());
	}

}
