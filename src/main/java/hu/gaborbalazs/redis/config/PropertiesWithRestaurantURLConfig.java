package hu.gaborbalazs.redis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:restaurant.properties")
public class PropertiesWithRestaurantURLConfig {

}
