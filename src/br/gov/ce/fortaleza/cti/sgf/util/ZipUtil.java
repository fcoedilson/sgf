package br.gov.ce.fortaleza.cti.sgf.util;

import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

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
}