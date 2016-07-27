package cn.szse.model;

import java.util.regex.Pattern;

/**
 * 匹配<a........ href="[url]".......>
 * @author bxiao01.oth
 *
 */
public class LinkResourceHandler extends ResourceHandler {
	public static final String REGEX_LINK = "<\\s*a\\s+(?:[^>]\\s*){0,}href\\s*=\\s*[\"|']([^\"|']{1,}[a-zA-Z_0-9])\\s*[\"|']";

	@Override
	protected Pattern getLabelPattern() {
		return Pattern.compile(REGEX_LINK, CASE_IGNORE_FLAG);//match the label "<a href='...'>...</a>"
	}
	
	public static void main(String args[]) {
		LinkResourceHandler handler = new LinkResourceHandler();
		
		System.out.println(handler.handle("<a href=\"resource/page/hello.html\">hello</a>"));
		System.out.println(handler.handle("<a href=\"resource/page/hello.html?\">hello</ a >"));
		System.out.println(handler.handle("<a href=\"resource/page/hello.html?key=value\">hello</a>"));
		System.out.println(handler.handle("<a href=\"resource/page/hello.html?key=bbb&version=1&a&name=bxiao\">hello</a>"));
	}

}
