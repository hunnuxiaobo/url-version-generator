package cn.szse.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceHandlerTest {

	public static void main(String args[]) {
		String[] hrefArr = {
				"href=\"/szseAllotmentPortal/framework/skins/default/css/default.css \"",
				"href=\"/resources/css/theme.css?a=kelly\"",
				"href=\"/resources/css/theme.css?version=1234&a=kelly\"",
				"href=\"/resources/css/theme.css?key=%E4%B8%AD%E5%9B%BD\"  ",
				"href=\"<%=request.getContextPath()%>/resources/js/validation_fun.js\"",
				"href=\"<%=themeUrl%>\"",
				"href=\"<%=baseDir%>/css/theme.css\"",
				"href=\"<%=context.get(\"baseDir\") %>/css/theme.css\"",
				"href=\"<%=context.get(\"themeUrl\") %>\"",
				"href=\"${themeUrl}\"",
				"href=\"${baseDir}/css/theme.css\""
		};
		
		String regx = "href\\s*=\\s*[\"|']([^\"|']{1,}[a-zA-Z_0-9])\\s*[\"|']";
		Pattern pattern = Pattern.compile(regx);
		
		for(String href : hrefArr) {
			Matcher m = pattern.matcher(href);
			StringBuffer sb = new StringBuffer();
			
			while (m.find()) {//寻找下一个包含URI的匹配对象
				System.out.println("group0: "+m.group());
				System.out.println("group1: "+m.group(1));
				m.appendReplacement(sb, "___");
			}
			m.appendTail(sb);
			
			System.out.println("result: "+sb.toString());
			//System.out.println(href.matches(regx)+"  "+href);
		}
	}
}
