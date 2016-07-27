package cn.szse.model;

import java.util.regex.Pattern;

/**
 * 匹配<link rel="stylesheet" href="css/app.css" />
 * 匹配css中背景图片地址 background-image: url(bgimage.gif);
 * 
 * @author bxiao01.oth
 *
 */
public class CSSResourceHandler extends ResourceHandler{
	public static final String REGEX_LINK = "<\\s*link\\s+(?:[^>]\\s*){0,}href\\s*=\\s*[\"|']([^\"|']{1,}[a-zA-Z_0-9])\\s*[\"|']";
	
	@Override
	protected Pattern getLabelPattern() {
		return Pattern.compile(REGEX_LINK, CASE_IGNORE_FLAG);//match the label "<link href='...'>...</link>"
	}

	public static void main(String args[]) {
		CSSResourceHandler handler = new CSSResourceHandler();
		
		System.out.println(handler.handle("<link rel='stylesheet' type='text/css' href='/c5.css ' />"));
		System.out.println(handler.handle("<link rel='stylesheet' type='text/css' href='/c5.css' > asdf</ link >"));
		System.out.println(handler.handle("<link rel='stylesheet' type='text/css' href='/c5.css?key=value' />"));
		System.out.println(handler.handle("<link rel='stylesheet' type='text/css' href='/c5.css?key=value&version=bbbb&name=bxiao' />"));
	}
}
