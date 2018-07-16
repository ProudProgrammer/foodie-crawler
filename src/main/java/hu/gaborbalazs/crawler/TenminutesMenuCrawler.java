package hu.gaborbalazs.crawler;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.gaborbalazs.enums.Restaurant;
import hu.gaborbalazs.exception.ExceptionMessage;
import hu.gaborbalazs.redis.entity.Menu;

import org.bytedeco.javacpp.*;
import static org.bytedeco.javacpp.lept.*;
import static org.bytedeco.javacpp.tesseract.*;

@Component
public class TenminutesMenuCrawler implements MenuCrawler {

	@Autowired
	private Logger logger;

	@Value("${tenminutes}")
	private String url;

	@Override
	public Menu getMenu() {
		logger.debug("TenminutesMenuCrawler is crawling...");

		try {
			URL imageUrl = new URL(url);
			InputStream in = new BufferedInputStream(imageUrl.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] response = out.toByteArray();
			FileOutputStream fos = new FileOutputStream("C:\\Balazs\\Temp\\home_1_06.png");
			fos.write(response);
			fos.close();
			
			BytePointer outText;

	        TessBaseAPI api = new TessBaseAPI();
	        // Initialize tesseract-ocr with English, without specifying tessdata path
	        if (api.Init(null, "eng") != 0) {
	            System.err.println("Could not initialize tesseract.");
	            System.exit(1);
	        }

	        // Open input image with leptonica library
	        PIX image = pixRead("C:\\Balazs\\Temp\\home_1_06.png");
	        api.SetImage(image);
	        // Get OCR result
	        outText = api.GetUTF8Text();
	        System.out.println("OCR output:\n" + outText.getString());

	        // Destroy used object and release memory
	        api.End();
	        outText.deallocate();
	        pixDestroy(image);
		} catch (Exception e) {
			logger.error(ExceptionMessage.IMAGE_READ, e);
		}

		List<String> menuElements = new ArrayList<>();
		return new Menu(Restaurant.TENMINUTES.getId(), Restaurant.TENMINUTES.getName(), menuElements);
	}
}
