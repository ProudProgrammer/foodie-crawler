package hu.gaborbalazs.ocr;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import hu.gaborbalazs.exception.ExceptionMessage;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;

public class Tess4jCharacterRecognizer implements CharacterRecognizer {

	private static final Pattern MENU_PATTERN = Pattern.compile("Men.\\s(.|\\s)*?\\d");

	private static final String NEWLINE_REGEX = "[\\r\\n]+";

	@Autowired
	private Logger logger;

	@Autowired
	private ITesseract tesseract;

	@Override
	public List<String> getLinesFromImage(String imagePath) {
		List<String> lines = new ArrayList<>();
		String imgText = "";
		try {
			imgText = tesseract.doOCR(new File(imagePath));
			logger.debug(System.lineSeparator() + Arrays.asList(imgText.split(NEWLINE_REGEX)).stream()
					.map(line -> ">> " + line).collect(Collectors.joining(System.lineSeparator())));
		} catch (TesseractException e) {
			logger.error(ExceptionMessage.IMAGE_READ, e);
		}
		Matcher menuMatcher = MENU_PATTERN.matcher(imgText);
		while (menuMatcher.find()) {
			String[] menuElementsStringArray = menuMatcher.group(0).split(NEWLINE_REGEX);
			for (int i = 0; i < menuElementsStringArray.length; i++) {
				if (i != 0 && i != menuElementsStringArray.length - 1) {
					lines.add(menuElementsStringArray[i]);
				}
			}
		}
		return lines;
	}

}
