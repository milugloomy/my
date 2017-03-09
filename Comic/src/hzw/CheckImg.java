package hzw;

import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

public class CheckImg {

	public static void main(String[] args) throws BadElementException, MalformedURLException, IOException {
		Image image = Image.getInstance("src/007.png");
		System.out.println(image);
	}

}
