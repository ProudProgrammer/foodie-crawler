package hu.gaborbalazs.config;

import java.nio.file.Paths;
import java.time.Clock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

import com.aspose.ocr.OcrEngine;

import hu.gaborbalazs.ocr.AsposeCharacterRecognizer;
import hu.gaborbalazs.ocr.CharacterRecognizer;
import hu.gaborbalazs.ocr.Tess4jCharacterRecognizer;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

@Configuration
public class GlobalConfig {

	@Value("${ocr.type}")
	private String ocr;

	@Bean("TenMinutesImagePath")
	public String getTenMinutesImagePath() {
		return Paths.get(Paths.get("src\\main\\resources\\download").toAbsolutePath().toString(), "tenminutes.png")
				.toString();
	}

	@Bean
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	public Logger getLogger(InjectionPoint ip) {
		return LoggerFactory.getLogger(ip.getMember().getDeclaringClass());
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public Clock getClock() {
		return Clock.systemUTC();
	}

	@Bean
	public ITesseract getTesseract() {
		ITesseract instance = new Tesseract();
		instance.setDatapath(Paths.get("src\\main\\resources\\tessdata").toAbsolutePath().toString());
		instance.setLanguage("hun");
		return instance;
	}

	@Bean
	public OcrEngine getOcrEngine() {
		return new OcrEngine();
	}

	@Bean
	public CharacterRecognizer getCharacterRecognizer() {
		if ("tess4j".equals(ocr)) {
			return new Tess4jCharacterRecognizer();
		} else if ("aspose".equals(ocr)) {
			return new AsposeCharacterRecognizer();
		} else {
			return new Tess4jCharacterRecognizer();
		}
	}
}
