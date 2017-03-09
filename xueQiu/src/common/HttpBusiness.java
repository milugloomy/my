package common;

import java.io.IOException;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class HttpBusiness {

	public static final int timeout = 30 * 1000;

	public String httpPost(String url) throws IOException {

		Response res = Jsoup.connect(url).timeout(timeout).method(Method.GET)
				.execute();

		return res.body();
	}

}
