package br.gov.ce.fortaleza.cti.sgf.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.ce.fortaleza.cti.sgf.bean.BaseBean;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class DownloadFileUtil extends BaseBean {

	private static final Log logger = LogFactory.getLog(DownloadFileUtil.class);
	public static final int TYPE_PDF = 1;
	public static int TYPE_RTF = 2;
	
	public static byte[] zip(String fileName, byte[] content) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(out);
		ZipEntry entry = new ZipEntry(fileName);
		zos.putNextEntry(entry);
		zos.setMethod(ZipOutputStream.DEFLATED);
		zos.write(content, 0, content.length);
		zos.finish();
		return out.toByteArray();
	}

	public static synchronized String downloadKMLFile(byte[] content) {
		try {
			return downloadFile(zip("sgf.kml", content), "sgf.kmz", "application/vnd.google-earth.kmz");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return FAIL;
		}
	}

	public static synchronized String downloadKMLNoZipFile(byte[] content) {
		try {
			return downloadFile(content, "sgf.kmz", "application/vnd.google-earth.kmz");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return FAIL;
		}
	}
	
	public static synchronized String downloadFile(byte[] content, String fileName, String mimeType) {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext context = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setContentType(mimeType);
		response.setContentLength(content.length);
		try {
			ServletOutputStream out = response.getOutputStream();
			out.write(content, 0, content.length);
			out.flush();
			facesContext.responseComplete();
			return SUCCESS;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return FAIL;
		}
	}

	public static void getAutorizacaoPDF(String header, String body, String fileName, int typeId) throws IOException, DocumentException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext context = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String path = request.getSession().getServletContext().getRealPath("images/fortaleza-bela.gif");
		@SuppressWarnings("unused")
		Document document = prepareDocument(header, body, typeId, response, baos, path);
		response.setHeader("Content-Disposition", "attachment; filename="+ fileName);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		facesContext.responseComplete();
	}

	public static void getAutorizacaoSolTrocaLubrificantePDF(String header, String body, String fileName, int typeId) throws IOException, DocumentException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext context = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String path = request.getSession().getServletContext().getRealPath("images/fortaleza-bela.gif");
		@SuppressWarnings("unused")
		Document document = documentAutorizacaoSolTrocaLubrificante(header, body, typeId, response, baos, path);
		response.setHeader("Content-Disposition", "attachment; filename="+ fileName);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		facesContext.responseComplete();
	}

	public static Document documentAutorizacaoSolTrocaLubrificante(String headerPage, String bodyPage, int typeId, HttpServletResponse response, ByteArrayOutputStream baos, String path) throws MalformedURLException, IOException{

		Document document = new Document(PageSize.A4);

		try {

			if( typeId == TYPE_PDF){

				PdfWriter.getInstance(document, baos);

				response.setContentType("application/pdf");
			}

			String[] headerPageData = headerPage.split("#");

			String info = "Descri��o do Ve�culo: " + headerPageData[0] + "\n" +
			"\nUA:" + headerPageData[1] +  "\n" +
			"\nMotorista:" + headerPageData[2] +  "\n" +
			"\nPosto:" + headerPageData[3] + "\n" +
			"\nTipo de Servi�o:" + headerPageData[4] + "\n" +
			"\nUsu�rio Autorizador: " + headerPageData[5] +  "\n" +
			"\nData da autoriza��o: " + headerPageData[6] + "\n" +
			"\nJustificativa: " + headerPageData[7];

			float[] tableHeaderWidth = {150};

			PdfPTable tableHeader = documentHeader(path); // Header do documento

			PdfPTable tableDocTitle = new PdfPTable(tableHeaderWidth);
			tableDocTitle.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

			tableDocTitle.addCell(new Paragraph("Autoriza��o de Troca de Lubrificante\n\n", new Font(12)));

			PdfPTable tableDetails = new PdfPTable(tableHeaderWidth);

			tableDetails.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

			tableDetails.getDefaultCell().setHorizontalAlignment(PdfPTable.ALIGN_MIDDLE);

			tableDetails.addCell(new Paragraph(info, new Font(12)));

			document.open();

			document.add(tableHeader);
			
			document.add(tableDocTitle);

			document.add(new Paragraph("\n"));

			document.add(tableDetails);

			document.add(new Paragraph("\n\n"));

			document.add(new Paragraph("\n\n\n\n\n"));

			PdfPTable tableFooter = documentFooter(); // Footer do documento

			document.add(tableFooter);

			document.close();

		} catch(DocumentException e) {

			logger.error(e.getMessage(), e);
		}

		return document;
	}

	public static Document prepareDocument(String headerPage, String bodyPage, int typeId, HttpServletResponse response, ByteArrayOutputStream baos, String path) throws MalformedURLException, IOException{

		Document document = new Document(PageSize.A4);

		try {

			if( typeId == TYPE_PDF){

				PdfWriter.getInstance(document, baos);

				response.setContentType("application/pdf");
			}

			String[] headerPageData = headerPage.split("#");

			String info = "Descri��o do Ve�culo: " + headerPageData[0] + 
			"\nUA:" + headerPageData[1] + 
			"\nNome da Oficina: " + headerPageData[2] + 
			"\nMec�nico respons�vel:" + headerPageData[3] + 
			"\nData de In�cio da manuten��o: " + headerPageData[4] + 
			"\nData de fim da manuten��o: " + headerPageData[5];

			float[] tableHeaderWidth = {150};
			float[] tableTitleWidth = {120};

			PdfPTable tableHeader = documentHeader(path); // Header do documento

			PdfPTable tableDocTitle = new PdfPTable(tableTitleWidth);
			tableDocTitle.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

			tableDocTitle.addCell(new Paragraph("Autoriza��o de Manuten��o de Ve�culo", new Font(12)));

			PdfPTable tableDetails = new PdfPTable(tableHeaderWidth);

			tableDetails.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

			tableDetails.getDefaultCell().setHorizontalAlignment(PdfPTable.ALIGN_MIDDLE);

			tableDetails.addCell(new Paragraph(info, new Font(12)));

			document.open();

			document.add(tableHeader);

			document.add(tableDocTitle);

			document.add(new Paragraph("\n"));

			document.add(tableDetails);

			document.add(new Paragraph("\n\n"));

			String[] bodyPageData = bodyPage.split("#");

			String[] defFonts = bodyPageData[0].split(";");

			Font[] fonts = new Font[defFonts.length+1];

			for ( int k = 0; k <= defFonts.length; k++) {

				fonts[k] = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);
			}

			Font fonteHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 12);

			float tableBodyWidth[] =  {20F,60F,20F,20F,20F};

			PdfPTable tableBody = new PdfPTable(tableBodyWidth);

			tableBody.getDefaultCell().setHorizontalAlignment(PdfPTable.ALIGN_MIDDLE);

			tableBody.addCell(new PdfPCell(new Paragraph("N�", fonteHeader)));

			tableBody.addCell(new PdfPCell(new Paragraph("Tipo Servi�o", fonteHeader)));

			tableBody.addCell(new PdfPCell(new Paragraph("Quant.", fonteHeader)));

			tableBody.addCell(new PdfPCell(new Paragraph("Vr. Unit.", fonteHeader)));

			tableBody.addCell(new PdfPCell(new Paragraph("Vr. Tot.", fonteHeader)));

			for (String s : bodyPageData) {

				String[] row = s.split(";");

				for (int j = 0; j < row.length; j++) {

					PdfPCell c2 = new PdfPCell();

					c2.addElement(new Paragraph(row[j], fonts[j]));

					tableBody.addCell(c2);
				}
			}

			document.add(tableBody);

			document.add(new Paragraph("\n\n\n\n\n"));

			PdfPTable tableFooter = documentFooter(); // Footer do documento

			document.add(tableFooter);

			document.close();

		} catch(DocumentException e) {

			logger.error(e.getMessage(), e);
		}

		return document;
	}

	public static PdfPTable documentHeader(String path) throws BadElementException, MalformedURLException, IOException{

		float[] tableHeaderWidth = {120, 180};

		PdfPTable tableHeader = new PdfPTable(tableHeaderWidth);

		tableHeader.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		tableHeader.getDefaultCell().setHorizontalAlignment(PdfPTable.ALIGN_MIDDLE);

		Image logo = Image.getInstance(path);

		logo.scalePercent(60);

		tableHeader.setWidthPercentage(80);

		tableHeader.addCell(logo);

		tableHeader.addCell(new Paragraph("\n\nPrefeitura Municipal de Fortaleza - PMF", new Font(14)));

		return tableHeader;
	}

	public static PdfPTable  documentFooter(){

		float[] tableFooterWidth = {150};

		PdfPTable tableFooter = new PdfPTable(tableFooterWidth);

		tableFooter.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		tableFooter.getDefaultCell().setHorizontalAlignment(PdfPTable.ALIGN_MIDDLE);

		String[] current  = DateUtil.parseAsString("dd/MM/yyyy", new Date()).split("/");

		tableFooter.addCell(new Paragraph("________________________________________________"));

		tableFooter.addCell(new Paragraph("               Assinatura do Respons�vel"));

		tableFooter.addCell(new Paragraph("\n\n"));

		tableFooter.addCell(new Paragraph("Fortaleza-Cear� " + current[0] + " de " + DateUtil.MONTHS_OF_YEAR[Integer.parseInt(current[1])-1] + " de " + current[2]));

		return tableFooter;
	}
}