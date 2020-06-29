package com.focus.test.java.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReadWirteMain {

	public static void main (String args[]) throws IOException {
		String readPath = "D:\\FFOutput\\test.txt";
		String wirtePath = "D:\\FFOutput\\test2.txt";
		readWirteFileBuff(readPath, wirtePath);
	}
	
	
	public static void readWirteFile(String readPath, String wirtePath) throws IOException {
    	FileInputStream fi = new FileInputStream(readPath);
    	FileOutputStream fo = new FileOutputStream(wirtePath);

    	byte[] bb  = new byte[1024];
    	int len = 0;
    	try {
    		while((len  = fi.read(bb)) > 0) {
        		System.out.print(new String(bb));
        		fo.write(bb, 0, len);
        	}
    		fo.flush();
    	}catch (Exception e) {
    		try {
    			if(fi != null) {
    				fi.close();
    			}
    			
    		}catch (Exception fie) {
    			
			}
    		
    		try {
    			if(fo != null) {
    				fo.close();
    			}
    		}catch (Exception foe) {
				
			}
		}
    	
    	
    	
    }
	
	public static void readWirteFileBuff(String readPath, String wirtePath) throws IOException {
    	InputStream is = new FileInputStream(readPath);
    	
    	BufferedInputStream bfis  = new BufferedInputStream(is);
    	
    	OutputStream os = new FileOutputStream(wirtePath);
    	
    	BufferedOutputStream bfos = new BufferedOutputStream(os);

    	byte[] bb  = new byte[1024];
    	int len = 0;
    	try {
    		while((len  = bfis.read(bb)) > 0) {
        		System.out.print(new String(bb));
        		bfos.write(bb, 0, len);
        	}
    	}catch (Exception e) {
    		try {
    			if(is != null) {
    				is.close();
    			}
    			
    		}catch (Exception fie) {
    			
			}
    		
    		try {
    			if(os != null) {
    				os.close();
    			}
    		}catch (Exception foe) {
				
			}
		}
    	
    	
    	
    }
	
	
    public static void readFile(String path) throws IOException {
    	FileInputStream fi = new FileInputStream(path);
    	byte[] bb  = new byte[1024];
    	int len = 0;
    	while((len  = fi.read(bb)) > 0) {
    		System.out.print(new String(bb));
    	}
    	
    }
    
    
}
