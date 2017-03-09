package hzw;

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
	public static void main(String[] args) throws IOException,
			DocumentException {
		genePdf();
		mergePdf();
	}
	public static void genePdf() throws DocumentException, IOException{
		File[] hzwDir = new File("hzw").listFiles();
		for (int i = 0; i < hzwDir.length; i++) {
			File dir = hzwDir[i];
			System.out.println(dir);
			File[] imgList = dir.listFiles();
			
			for (int j = 0; j < imgList.length; j++) {
				File tempDir=new File(dir.getPath() + "\\temp");
				if(!tempDir.exists())tempDir.mkdir();
				
				String imgPath = imgList[j].getPath();
				String pdfPath = dir.getPath() + "\\temp\\" + 
						imgList[j].getName().split("\\.")[0] + ".pdf";
				
				if (new File(imgPath).isDirectory())
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
		File[] hzwDir = new File("hzw").listFiles();
		for (int m = 0; m < hzwDir.length; m++) {
			System.out.println(hzwDir[m].getPath());
			String tempPath=hzwDir[m].getPath()+"\\temp";
			File[] fileList=new File(tempPath).listFiles();
			FileOutputStream fos = new FileOutputStream(hzwDir[m].getPath()+"\\"+hzwDir[m].getName()+".pdf");
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
