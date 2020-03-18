package com.focus.test.util;
import java.util.HashMap;
import java.util.Map;


public class DateUtil {
	
	static String mount [] = {"January","February","March","April","May","June","July","August","September","October","November","December"};
	
	
	public static void main(String[] args) {
		
		
		formatAmaZonDate("November 13, 2017");
	}
	
    public static String formatAmaZonDate(String dateStr) {
    	Map hashMap = new HashMap();
    	hashMap.put("January","01");
    	hashMap.put("February","02");
    	hashMap.put("March","03");
    	hashMap.put("April","04");
    	hashMap.put("May","05");
    	hashMap.put("June","06");
    	hashMap.put("July","07");
    	hashMap.put("August","08");
    	hashMap.put("September","09");
    	hashMap.put("October","10");
    	hashMap.put("November","11");
    	hashMap.put("December","12");
    	String [] dateArray= dateStr.split(" ");
    	String returnStr = "";
    	if(dateArray.length == 3) {
    		//mount
        	String str = dateArray[0];
        	String mouthStr = (String) hashMap.get(str);
        	String date = dateArray[1].replaceAll(",", "");
        	
        	String year = dateArray[2];
        	if(date.length() < 2) {
        	   date = "0" + date; 
        	}
        	returnStr = year + "-" +mouthStr+"-" +date;
    	}else {
    		//mount
        	String str = dateArray[1];
        	String mouthStr = (String) hashMap.get(str);
        	String date = dateArray[2].replaceAll(",", "");
        	
        	String year = dateArray[3];
        	if(date.length() < 2) {
        	   date = "0" + date; 
        	}
        	returnStr = year + "-" +mouthStr+"-" +date;
    	}
    	
    	
    	
    	System.out.println(dateStr);
    	System.out.println(returnStr);
    	return returnStr;
    	
    }
}
