/**
 * 
 */
package com.focus.test.transaction;
/**
* @author 作者 E-mail:
* @version 创建时间：2020年12月21日 下午2:14:01
* 类说明
*/
/**
 * @author futuremap
 *
 */
public class test3 {

	
	public static void main(String args []) {
		String code = "1.2";
		System.out.println(code.indexOf("."));
		System.out.println(code.lastIndexOf("."));
		//String parentIdStr = code.substring(code.indexOf(".")+1, code.lastIndexOf("."));
		//String idStr = code.substring(code.lastIndexOf(".")+1, code.length());
		
		String parentIdStr = code.substring(0, code.indexOf("."));
		String idStr = code.substring(code.indexOf(".")+1, code.length());
		
		
		System.out.println(parentIdStr);
		System.out.println(idStr);
		
	
	}
}
