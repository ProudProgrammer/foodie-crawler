package hu.gaborbalazs.web;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import hu.gaborbalazs.redis.entity.Menu;
import hu.gaborbalazs.redis.repository.MenuRepository;

@RunWith(MockitoJUnitRunner.class)
public class FoodieCrawlerControllerTests {

	private MockMvc mvc;

	@Mock
	private MenuRepository menuRepository;

	@InjectMocks
	private FoodieCrawlerController controller;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		mvc = MockMvcBuilders.standaloneSetup(controller).build();

		List<Menu> menus = new ArrayList<>();
		List<String> menuElements1 = new ArrayList<>();
		menuElements1.add("MenuElement1");
		menuElements1.add("MenuElement2");
		Menu menu1 = new Menu("TestId", "TestRestaurant", "TestUrl", menuElements1);
		menus.add(menu1);
		Mockito.when(menuRepository.findAll()).thenReturn(menus);
	}

	@Test
	public void testGetMenus() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/menus").accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is2xxSuccessful()).andExpect(content().json(
						"[{'id':'TestId','restaurant':'TestRestaurant','url':'TestUrl','elements':['MenuElement1','MenuElement2']}]"));
	}
}
