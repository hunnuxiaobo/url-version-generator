package cn.szse.model;

import java.util.regex.Pattern;

/**
 * 匹配<script type="text/javascript" src="js/lib/require.js" ></script>
 * @author bxiao01.oth
 *
 */
public class JSResourceHandler extends ResourceHandler {
	public static final String REGEX_LINK = "<\\s*script\\s+(?:[^>]\\s*){0,}src\\s*=\\s*[\"|']([^\"|']{1,}[a-zA-Z_0-9])\\s*[\"|']";
	
	@Override
	protected Pattern getLabelPattern() {
		return Pattern.compile(REGEX_LINK, CASE_IGNORE_FLAG);//match the label "<script src='...'>...</script>"
	}
	
	public static void main(String args[]) {
		JSResourceHandler handler = new JSResourceHandler();
		
		System.out.println(handler.handle("<script type='text/javascript' src = 'js/app/main.js' ></script>"));
		System.out.println(handler.handle("<script type='text/javascript' src = \"js/app/main.js?\" ></ script >"));
		System.out.println(handler.handle("<script type='text/javascript' src = \"js/app/main.js?key=value&key1=value1\" ></script>"));
		System.out.println(handler.handle("<script type='text/javascript' src = \"js/app/main.js?key=value&version=124&key1=value1\" ></script>"));
	}
}
