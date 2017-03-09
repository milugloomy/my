package common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class HttpBusiness {

	public Response httpPost(String url) throws IOException {

		Map<String, String> map = cookie();
		Response res = Jsoup.connect(url).cookies(map).method(Method.GET)
				.execute();
		// ParamBusiness.gFile(res.body(), "a.html");

		return res;
	}

	private static Map<String, String> cookie() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("YF-V5-G0", "54feb8bb5695f8d651cb139f392595ba");
		map.put("YF-Page-G0", "27b9c6f0942dad1bd65a7d61efdfa013");
		map.put("SUB",
				"_2AkMg5ja0f8NhqwJRmP0TyWLha45yyQvEieLBAH7sJRMxHRl-yT83qhY7tRDJ_yVoa9D7SBrutykZiLtJQgZ-sA..");
		map.put("SUBP",
				"0033WrSXqPxfM72-Ws9jqgMF55529P9D9WhHq.WDh1-guTUxe6aKGzPg");
		map.put("_s_tentry", "-");
		map.put("Apache", "8154441556383.008.1471854987590");
		map.put("SINAGLOBAL", "8154441556383.008.1471854987590");
		map.put("ULV", "1471854987664:1:1:1:8154441556383.008.1471854987590:");
		map.put("login_sid_t", "421ee5a6d7049e9651f0959c4115ff5a");
		map.put("YF-Ugrow-G0", "56862bac2f6bf97368b95873bc687eef");
		map.put("WBStore", "ab2489e83fd8587e|undefined");
		return map;
	}

}
