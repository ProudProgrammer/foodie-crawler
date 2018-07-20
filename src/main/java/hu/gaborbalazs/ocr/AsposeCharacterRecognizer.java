package hu.gaborbalazs.ocr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.aspose.ocr.ImageStream;
import com.aspose.ocr.OcrEngine;

import hu.gaborbalazs.exception.ExceptionMessage;

public class AsposeCharacterRecognizer implements CharacterRecognizer {

	private static final String NEWLINE_REGEX = "[\\r\\n]+";

	@Autowired
	private Logger logger;

	@Autowired
	private OcrEngine ocrEngine;

	@Override
	public List<String> getLinesFromImage(String imagePath) {
		List<String> lines = new ArrayList<>();
		try {
			ocrEngine.setImage(ImageStream.fromFile(imagePath));
			if (ocrEngine.process()) {
				String[] linesAsArray = ocrEngine.getText().toString().split(NEWLINE_REGEX);
				logger.debug(System.lineSeparator() + Arrays.asList(linesAsArray).stream().map(line -> ">> " + line)
						.collect(Collectors.joining(System.lineSeparator())));
				Arrays.asList(linesAsArray).forEach(lines::add);
			}
		} catch (Exception e) {
			logger.error(ExceptionMessage.IMAGE_READ, e);
		}
		return lines;
	}

}
