package br.gov.ce.fortaleza.cti.sgf.util;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImageServlet extends HttpServlet {

	private static final Log logger = LogFactory.getLog(ImageServlet.class);

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		execute(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		execute(req, resp);
	}

	@SuppressWarnings("unchecked")
	protected void execute(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		String id = request.getParameter("id");
		response.setContentType("image/png");
		ServletOutputStream out = response.getOutputStream();

		try {
			InputStream imageIn = getServletContext().getResourceAsStream("/images/icons/user.png");
			BufferedInputStream in = new BufferedInputStream(imageIn);
			BufferedImage bi = ImageIO.read(in);
			Graphics g = bi.getGraphics();

			HashMap attrs = new HashMap();
			attrs.put(TextAttribute.FAMILY, "Arial");
			attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
			attrs.put(TextAttribute.SIZE, 12);
			attrs.put(TextAttribute.WIDTH, TextAttribute.WIDTH_CONDENSED);
			Font font = new Font(attrs);

			g.setFont(font);
			g.setColor(Color.RED);
			int textWidth = g.getFontMetrics().stringWidth(id);
			textWidth = (textWidth > 15 ? 15 : textWidth);
			g.drawString(id, 16 - textWidth - 1, 12);

			ImageIO.write(bi, "png", out);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		out.flush();
		out.close();
	}

}

