package com.cellon.sdback.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

/**
 * @author Baron.Hu
 * */

public class SAXParser {
	
	private StringBuffer sb;
	
	public List<SMSUtil> getSms(){
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			javax.xml.parsers.SAXParser parser = factory.newSAXParser();
			MyHandler handler = new MyHandler();
			parser.parse(new File("/sdcard/moto_backup/sms.xml"), handler);
			return handler.getSms();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private final class MyHandler extends DefaultHandler{
		private String tag;
		private SMSUtil sms = null;
		private List<SMSUtil> listSms = null;
		
		private List<SMSUtil> getSms(){
			return listSms;
		}
		
		public void startDocument(){
			sb = new StringBuffer();
			listSms = new ArrayList<SMSUtil>();
		}
		
		public void startElement(String uri, String localName, String qName, Attributes attr) 
			throws SAXException{
			sb.delete(0, sb.length());//for handling the special symbols (& < " ')
			if ("msg".equals(qName)) {
				sms = new SMSUtil();
				if (attr.getLength() > 0) {
					sms.setId(Integer.parseInt(attr.getValue(0)));
				}
			}
			tag = qName;
		}
		
		public void characters(char[] ch, int start, int length){
			if (tag != null) {
				if ("type".equals(tag)) {
					sb.append(ch, start, length);
					sms.setType(Integer.parseInt(sb.toString()));
				} else if ("address".equals(tag)) {
					sb.append(ch, start, length);
					sms.setAddress(sb.toString());
				} else if ("body".equals(tag)) {
					sb.append(ch, start, length);
					Log.d("baron.hu", "body = " + sb.toString());
					sms.setBody(sb.toString());
				} else if ("date".equals(tag)) {
					sb.append(ch, start, length);
					sms.setDate(sb.toString());
				}
			}
		}
		
		public void endElement(String uri, String localName, String qName){
			if ("msg".equals(qName)) {
				listSms.add(sms);
				sms = null;
			}
			tag = null;
		}
	}

}
