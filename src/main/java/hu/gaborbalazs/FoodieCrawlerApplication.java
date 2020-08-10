package hu.gaborbalazs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import hu.gaborbalazs.batch.CrawlerScheduler;

@EnableAspectJAutoProxy
@SpringBootApplication
@EnableScheduling
public class FoodieCrawlerApplication implements ApplicationRunner {

	@Autowired
	private CrawlerScheduler crawlerScheduler;

	public static void main(String[] args) {
		SpringApplication.run(FoodieCrawlerApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// crawlerScheduler.crawl();
	}
}
