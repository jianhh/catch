package com.work.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class test {

	public static void main(String[] args) throws Exception {
		String sendUrl = "http://2016058.1688.com/page/offerlist.htm?sortType=timeup&pageNum=1";
		for (int i = 1; i < 40; i++) {
			String xml = JsoupUtil.conUrl(sendUrl, true, "gbk", "", 0);
			// System.out.println(xml);
			Document doc = Jsoup.parse(xml);
			// Document doc = JsoupUtil.getDocument(sendUrl);
			String companyName = JsoupUtil.commodityUrl(doc, sendUrl);// ¹«Ë¾Ãû
			System.out.println(companyName + "    " + i);
		}
	}
}
