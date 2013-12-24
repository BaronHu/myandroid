package com.cellon.sdback.util;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

/**
 * analysis the XML file by DOMPaser
 * @author Baron.Hu
 * @date 2011-07-15
 * */
public class DomPaser {
	
	private final String LOG_TAG = "DomPaser";
	
	/**
	 * read XML file and analysis it, Here uses Generic Program for security
	 * @param fileName the file name of XML file
	 * @return Vector for storing the SMSUtil object which stored data from XML
	 * */
	public Vector<SMSUtil> readXMLFile(String filename) {  
		Vector<SMSUtil> sms_Vector = new Vector<SMSUtil>();
        File mFile = null;  
        try {
        	mFile = new File(filename);
        	mFile.createNewFile();  
        } catch (IOException e) {  
        	Log.d(LOG_TAG, "createNewFile() : " + e.getMessage());
            e.printStackTrace();  
        }  
        // ready to analysis, create DocumentBuilder object by Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        DocumentBuilder db = null;  
        try {  
            db = dbf.newDocumentBuilder();  
        } catch (ParserConfigurationException pce) {  
        	Log.d(LOG_TAG, "DocumentBuilder : " + pce.getMessage());
            System.err.println(pce); // if there are any exceptions, exist
            System.exit(1);  
        }  
        Document doc = null;  
        try {  
            try {
				doc = db.parse(mFile);
			} catch (SAXException e) {
				Log.d(LOG_TAG, "Document: " + e.getMessage());
				e.printStackTrace();
			}  
        } catch (DOMException dom) {  
        	Log.d(LOG_TAG, "DOMException : " + dom.getMessage());
            System.err.println(dom.getMessage());  
            System.exit(1);  
        } catch (IOException ioe) {  
        	Log.d(LOG_TAG, "IOException : " + ioe.getMessage());
            System.err.println(ioe);  
            System.exit(1);  
        }  
        // start to analysis
        Element root = doc.getDocumentElement();  
        // get "msg" element
        NodeList msgNodes = root.getElementsByTagName("msg");  
        for (int i = 0; i < msgNodes.getLength(); i++) {  
            // get every "msg" element
            Element msgNode = (Element) msgNodes.item(i);  
            // create a SMSUtil object  
            SMSUtil smsBean = new SMSUtil();  
            
            // get sms's attribute "id" 
//            NodeList id = msgNode.getElementsByTagName("id");
//            if(id.getLength() == 1){
//            	Element e = (Element) id.item(0);
//            	Node t = e.getFirstChild();
//            	smsBean.setId(Integer.parseInt(t.getNodeValue()));
//            }
                         
            // get "type" element,the same as bellowing  
            NodeList type = msgNode.getElementsByTagName("type");  
            if (type.getLength() == 1) {  
                Element e = (Element) type.item(0);  
                Node t = e.getFirstChild();  
                smsBean.setType(Integer.parseInt(t.getNodeValue()));  
            }  
            
            NodeList address = msgNode.getElementsByTagName("address");  
            if (address.getLength() == 1) {  
                Element e = (Element) address.item(0);  
                Node t = e.getFirstChild();  
                smsBean.setAddress(t.getNodeValue());  
            }  
            
            NodeList date = msgNode.getElementsByTagName("date");  
            if (date.getLength() == 1) {  
                Element e = (Element) date.item(0);  
                Node t = e.getFirstChild();  
                smsBean.setDate(t.getNodeValue());  
            }
            
            NodeList body = msgNode.getElementsByTagName("body");
            if(body.getLength() == 1){
            	Element e = (Element) body.item(0);
            	Node t = e.getFirstChild();
            	smsBean.setBody(t.getNodeValue());
            }
            
            sms_Vector.add(smsBean);  
        }
        return sms_Vector;
    }

}
