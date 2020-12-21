package gateway;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.*;

/**
 * Convert the below Html table To Pdf Using Itext library.
 *
 * Use an object of the StringReader class to read HTML, and
 * then provide it to the ParseXHtml method of the XMLWorkerHelper
 * class object, which converts it into a PDF document and saves
 * it to a class InputStream object.
 *
 * Finally, use the File function to convert the InputStream class
 * object into a byte array, and then export and download it as a PDF file.
 *
 * Itext7 pdfHTML provides a convenient API that enables
 * users to convert HTML into PDF files or iText element lists.
 *
 * This gives users complete control over how to parse and
 * insert HTML elements.
 *
 * The semantic and structural information contained in HTML
 * files is an excellent source for pdfHTML to convert HTML files into
 * rich and formatted PDF documents.
 *
 * @see StringBuilder
 * @see usecase.download.facade.EventDownload
 *
 *
 */

public class PDFGenerate {
	
	public void writeTo(File file, String content) throws IOException, DocumentException{

		//step1
		OutputStream fos = new FileOutputStream(file);

		//step2

		Document document = new Document();
		PdfWriter pdfWriter = PdfWriter.getInstance(document, fos);

		//step3

		document.open();
		InputStream is = new ByteArrayInputStream(content.getBytes());

		//step4

		XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, is);

		//step5

		document.close();
		fos.close();
	}
}
