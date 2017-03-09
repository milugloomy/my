package dmzj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class Jpg2Pdf {
	static String dirPath="C:\\Users\\qijunhao\\Desktop\\任务";
	public static void main(String[] args) throws IOException,
			DocumentException {
		File tempDir=new File(dirPath+ "\\temp");
		if(!tempDir.exists())tempDir.mkdir();

		File[] kzgjDir = new File(dirPath).listFiles();
		for (int j = 0; j < kzgjDir.length; j++) {
			
			String imgPath = kzgjDir[j].getPath();
			String pdfPath = tempDir.getPath()+"\\" + 
					kzgjDir[j].getName().split("\\.")[0] + ".pdf";
			
			if (!imgPath.substring(imgPath.indexOf(".") + 1).equals("jpg"))
				continue;
			
			FileOutputStream fos = new FileOutputStream(pdfPath);
			Document doc = new Document(null, 0, 0, 0, 0);
			PdfWriter.getInstance(doc, fos);
			Image image = Image.getInstance(imgPath);
			doc.setPageSize(new Rectangle(image.getWidth(),image.getHeight()));
			doc.open();
			doc.add(image);
			doc.close();
		}
		kzgjDir = new File(dirPath+"\\temp").listFiles();
		FileOutputStream fos = new FileOutputStream(dirPath+"\\temp\\任务.pdf");
		Document document = new Document();;
		PdfCopy copy = new PdfCopy(document, fos);
		document.open();
		for (int i = 0; i < kzgjDir.length; i++) {
			PdfReader reader = new PdfReader(kzgjDir[i].getPath());
			int n = reader.getNumberOfPages();
			for (int j = 1; j <= n; j++) {
				document.newPage();
				PdfImportedPage page = copy.getImportedPage(reader, j);
				copy.addPage(page);
			}
		}
		document.close();
		
//		genePdf();
//		mergePdf();
	}
	public static void genePdf() throws DocumentException, IOException{
		File[] kzgjDir = new File("kzgj").listFiles();
		for (int i = 0; i < kzgjDir.length; i++) {
			File dir = kzgjDir[i];
			System.out.println(dir);
			File[] imgList = dir.listFiles();
			
			for (int j = 0; j < imgList.length; j++) {
				File tempDir=new File(dir.getPath() + "\\temp");
				if(!tempDir.exists())tempDir.mkdir();
				
				String imgPath = imgList[j].getPath();
				String pdfPath = dir.getPath() + "\\temp\\" + 
						imgList[j].getName().split("\\.")[0] + ".pdf";
				
				if (!imgPath.substring(imgPath.indexOf(".") + 1).equals("jpg"))
					continue;
				
				FileOutputStream fos = new FileOutputStream(pdfPath);
				Document doc = new Document(null, 0, 0, 0, 0);
				PdfWriter.getInstance(doc, fos);
				Image image = Image.getInstance(imgPath);
				doc.setPageSize(new Rectangle(image.getWidth(),image.getHeight()));
				doc.open();
				doc.add(image);
				doc.close();
			}
		}
	}
	
	public static void mergePdf() throws DocumentException, IOException{
		File[] kzgjDir = new File("kzgj").listFiles();
		for (int m = 0; m < kzgjDir.length; m++) {
			System.out.println(kzgjDir[m].getPath());
			String tempPath=kzgjDir[m].getPath()+"\\temp";
			File[] fileList=new File(tempPath).listFiles();
			FileOutputStream fos = new FileOutputStream(kzgjDir[m].getPath()+"\\"+kzgjDir[m].getName()+".pdf");
			Document document = new Document();;
			PdfCopy copy = new PdfCopy(document, fos);
			document.open();
			for (int i = 0; i < fileList.length; i++) {
				PdfReader reader = new PdfReader(fileList[i].getPath());
				int n = reader.getNumberOfPages();
				for (int j = 1; j <= n; j++) {
					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader, j);
					copy.addPage(page);
				}
			}
			document.close();
			
			for (int i = 0; i < fileList.length; i++) {
				fileList[i].delete();
			}
			new File(tempPath).delete();
		}
	}
}
