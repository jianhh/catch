package com.work.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

import com.framework.util.DateUtil;
import com.framework.util.StringUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.work.commodity.util.GoodsUtil;

/**
 * 二维码加图片合成
 * 
 * @author tangbiao
 * 
 */
public class FotoMix {

	/**
	 * @param args
	 * @throws IOException
	 * @throws WriterException
	 */
	static int s_qr_size = 640;

	// public static void main(String[] args) throws Exception {
	// // 网络图片
	// String[] urlArray = {
	// "http://gd3.alicdn.com/bao/uploaded/i3/TB1ycIFGXXXXXXpXpXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg",
	// "http://gd4.alicdn.com/imgextra/i4/685316775/TB2YjA2apXXXXcXXXXXXXXXXXXX-685316775.jpg_400x400.jpg",
	// "http://gd3.alicdn.com/imgextra/i3/685316775/TB2Tg3SapXXXXbwXpXXXXXXXXXX-685316775.jpg_400x400.jpg",
	// "http://gd3.alicdn.com/bao/uploaded/i3/TB1f7nfFVXXXXXsXpXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg",
	// "http://gd4.alicdn.com/imgextra/i4/685316775/TB2hA_bXVXXXXXXXpXXXXXXXXXX-685316775.jpg_400x400.jpg"
	// };
	// wImage("10000", "10000", urlArray);
	// }

	/**
	 * 生成带二维码的图片
	 * 
	 * @param shopId
	 *            店铺id
	 * @param goodsId
	 *            商品id
	 * @param urlArray
	 *            图片数组
	 * @return
	 * @throws Exception
	 */
	public static String wImage(String shopId, String goodsId,
			String urlArray[]) throws Exception {

		List<BufferedImage> buf_list = new ArrayList<BufferedImage>();

		// 本地图片
		// ArrayList img_list = new ArrayList();
		// img_list = fileList(s_dir);
		// System.out.println(img_list);

		// load source images
		int totalHeight = 0;

		/*
		 * for(int i = 0;i < img_list.size(); i ++) {
		 * buf_list.add(ImageIO.read(new File((String) img_list.get(i)))); }
		 */
		for (String str : urlArray) {
			if (StringUtils.isNotEmpty(str)) {
				buf_list.add(ImageIO.read(new URL(str)));
			}
		}

		for (int i = 0; i < buf_list.size(); i++) {
			BufferedImage bufferedImage = buf_list.get(i);
			int wid_tmp = 0;
			int hgt_tmp = 0;
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();

			if (width > 640) {
				wid_tmp = 640;
				hgt_tmp = (640 * height) / width;

				buf_list.set(i, (BufferedImage) Scalr.resize(bufferedImage,
						Method.QUALITY, wid_tmp, hgt_tmp, Scalr.OP_ANTIALIAS));

			} else if (width == 640) {
				wid_tmp = 640;
				hgt_tmp = height;
			} else {
				wid_tmp = width;
				hgt_tmp = height;
			}

			totalHeight += hgt_tmp;

		}

		BufferedImage combined = new BufferedImage(640,
				totalHeight + s_qr_size, BufferedImage.TYPE_INT_RGB);

		// paint both images, preserving the alpha channels
		Graphics g = combined.getGraphics();

		int curHeight = 0;

		Graphics2D g2d = combined.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 640, totalHeight + s_qr_size);

		for (int i = 0; i < buf_list.size(); i++) {

			if (i == 0) {
				drawQr(g2d, curHeight, goodsId, shopId);// 生成二维码
				curHeight += s_qr_size;
			}

			BufferedImage bufObj = buf_list.get(i);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);

			int beginX = (640 - bufObj.getWidth()) / 2;
			g2d.drawImage(bufObj, beginX, curHeight, null);
			curHeight += bufObj.getHeight();

		}

		Color myColour = new Color(0, 0, 0, 128);
		g.setColor(myColour);
		g.fillRect(0, curHeight - 64, 640, 64);

		drawText(g2d, curHeight - 64);

		g2d.dispose();

		BufferedImage image = combined;
		Iterator writers = (Iterator) ImageIO.getImageWritersBySuffix("jpeg");
		if (!writers.hasNext())
			throw new IllegalStateException("No writers for jpeg?!");
		ImageWriter writer = (ImageWriter) writers.next();
		ImageWriteParam imageWriteParam = writer.getDefaultWriteParam();
		imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		List thumbNails = null;
		IIOImage iioImage = new IIOImage(image, thumbNails, null);

		String filePath = JsoupUtil.SystemPathImage(shopId);// 设置图片下载的根目录
		// 如果该目录不存在,则创建之(多重目录自动创建)
		File uploadFilePath = new File(filePath);
		if (uploadFilePath.exists() == false) {
			uploadFilePath.mkdirs();
		}
		String fileName = goodsId + DateUtil.format.format(new Date()) + ".jpg";
		filePath = filePath + fileName;
		write(writer, imageWriteParam, iioImage, filePath, image, 1.0f);// 保存图片

		boolean ret = GoodsUtil.localFile(filePath, fileName);// 上传到七牛
		if (ret) {
			return fileName;
		}
		return null;
	}

	public static void drawQr(Graphics2D g2d, int curHeight, String goodsId,
			String shopId) throws WriterException, IOException {
		// http://localhost:8080/wg_bsp/static/shop/item_detail.jsp?goods_id=xxx
		// http://wegostores.duapp.com/static/shop/index.html
		String qrCodeText = "http://183.62.225.105/static/shop/item_detail.jsp?goods_id="
				+ goodsId + "&shop_id=" + shopId;
		int size = s_qr_size;
		String fileType = "jpg";

		// 生成的二维码图片存本地
		// String filePath = s_dir + "qr.jpg";
		// File qrFile = new File(filePath);
		// BufferedImage qrBuf = ImageIO.read(new File(filePath));

		BufferedImage qrBuf = createQRImage(null, qrCodeText, size, fileType);
		int beginX = (640 - s_qr_size) / 2;

		g2d.drawImage(qrBuf, beginX, curHeight, null);

	}

	public static void drawText(Graphics2D g2d, int curHeight) throws Exception {
		// Color cc =new Color(114,214,142);
		Color cc = new Color(255, 255, 255);
		g2d.setColor(cc);
		g2d.setFont(new Font("Microsoft YaHei", Font.PLAIN, 24));

		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// g2d.drawString("长按图片", 320, curHeight+64);
		// g2d.drawString("选择识别图中二维码进入", 320, curHeight+64+36);
		g2d.drawString("长按图片2秒,选择识别图中二维码进入购买", (640 - 20 * 24) / 2 + 12,
				curHeight + 40);

		// g2d.drawString("选择识别图中二维码进入购买", 320-11*12, curHeight+56);
	}

	public static void write(ImageWriter writer,
			ImageWriteParam imageWriteParam, IIOImage iioImage,
			String filename, BufferedImage image, float compressionQuality)
			throws Exception {
		ImageOutputStream out = ImageIO.createImageOutputStream(new File(
				filename));
		imageWriteParam.setCompressionQuality(compressionQuality);
		writer.setOutput(out);

		IIOMetadata data = writer.getDefaultImageMetadata(
				new ImageTypeSpecifier(image), writer.getDefaultWriteParam());

		image.flush();
		writer.write(data, new IIOImage(image, null, data), writer
				.getDefaultWriteParam());
		out.flush();
		out.close();
	}

	public static BufferedImage createResizedCopy(Image originalImage,
			int scaledWidth, int scaledHeight, boolean preserveAlpha) {
		System.out.println("resizing...");
		int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight,
				imageType);
		Graphics2D g = scaledBI.createGraphics();
		if (preserveAlpha) {
			g.setComposite(AlphaComposite.Src);
		}
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		g.dispose();
		return scaledBI;
	}

	public static ArrayList fileList(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		ArrayList list = new ArrayList();

		int i = 0;
		if (files != null) {
			for (File f : files) {
				list.add(f.getPath());
			}
		}

		return list;
	}

	private static BufferedImage createQRImage(File qrFile, String qrCodeText,
			int size, String fileType) throws WriterException, IOException {
		// Create the ByteMatrix for the QR-Code that encodes the given String
		Hashtable hintMap = new Hashtable();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText,
				BarcodeFormat.QR_CODE, size, size, hintMap);
		// Make the BufferedImage that are to hold the QRCode
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth, matrixWidth,
				BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		// Paint and save the image using the ByteMatrix
		graphics.setColor(Color.BLACK);

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		// ImageIO.write(image, fileType, qrFile);

		return image;
	}

	public static void main(String[] args) throws Exception {
		String shopId = "35325108";
		String text = "http://www.truedian.com/static/shop/index.jsp?is_share=1&shop_id="
				+ shopId;

		String filePath = JsoupUtil.SystemPathImage(shopId);// 设置图片下载的根目录
		// 如果该目录不存在,则创建之(多重目录自动创建)
		File uploadFilePath = new File(filePath);
		if (uploadFilePath.exists() == false) {
			uploadFilePath.mkdirs();
		}
		String fileName = shopId + "_index.jpg";
		filePath = filePath + fileName;
		File qrFile = new File(filePath);
		createShopQRImage(qrFile, text, 640, "jpg");
	}

	@SuppressWarnings("unchecked")
	public static void createShopQRImage(File qrFile, String qrCodeText,
			int size, String fileType) throws WriterException, IOException {
		Hashtable hintMap = new Hashtable();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText,
				BarcodeFormat.QR_CODE, size, size, hintMap);
		// Make the BufferedImage that are to hold the QRCode
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth, matrixWidth,
				BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		// Paint and save the image using the ByteMatrix
		graphics.setColor(Color.BLACK);

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		ImageIO.write(image, fileType, qrFile);
	}

}
