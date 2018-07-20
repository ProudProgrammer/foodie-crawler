package hu.gaborbalazs.ocr;

import java.util.List;

public interface CharacterRecognizer {

	List<String> getLinesFromImage(String imagePath);
}
