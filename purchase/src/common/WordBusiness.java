package common;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblGridCol;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;

public class WordBusiness {

	private static final String path = "temp/";
	private HttpBusiness http;
	
	public WordBusiness(){
		http=new HttpBusiness();
	}

	public void geneWord(List<Announce> newList) {
		for (Announce announce : newList) {
			String href = announce.getHref();
			String fileName = announce.getName();
			try {
				String res = http.httpPost(href);
						
				Element iframe = Jsoup.parse(res)
						.getElementsByTag("iframe").get(0);
				String realUrl = iframe.attr("src");
				realUrl = "http://www.ccgp-hubei.gov.cn" + realUrl;
				
				String realRes = http.httpPost(realUrl);
				Document doc = Jsoup.parse(realRes);
				doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml)
						.escapeMode(Entities.EscapeMode.xhtml); // 转为 xhtml 格式
				// 精简
				Element table = cut(doc);
				// 生成word
				geneWord(table.toString(), fileName + ".docx");

			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	public Element cut(Document doc) {
		Element table = doc.getElementsByClass("mtop").get(0);
		return table;
	}

	private void geneWord(String html, String fileName) {
		try {
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
					.createPackage(PageSizePaper.valueOf("A4"), false); // A4纸，//横版:true
			XHTMLImporterImpl xhtmlImporter = new XHTMLImporterImpl(
					wordMLPackage);
			wordMLPackage.getMainDocumentPart().getContent()
					.addAll(xhtmlImporter.convert(html, null));

			List<Object> objList = wordMLPackage.getMainDocumentPart()
					.getContent();
			for (Object obj : objList) {
				Tbl tbl = (Tbl) obj;
				TblGridCol tgc = tbl.getTblGrid().getGridCol().get(0);
				tgc.setW(new BigInteger("10000"));
				tbl.getTblGrid().getGridCol().set(0, tgc);
			}
			File file = new File(path + fileName);
			wordMLPackage.save(file); // 保存到 docx 文件
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
