package com.julio.xmlsax;

import java.util.List;

public class PersonResult {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		PersonSAXParser p = new PersonSAXParser();
		List<SMSUtil> persons = p.personSAXParser();
		for(SMSUtil pe : persons){
			System.out.println("type=" + pe.getType() + "\naddress=" 
					+ pe.getAddress() + "\ndate=" + pe.getDate() 
					+ "\nbody=" + pe.getBody());
		}

	}

}
