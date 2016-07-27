package cn.szse.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 匹配<img......src="[url]".......>
 * 如：<img src="boat.gif" alt="Big Boat"/>
 * 
 * 匹配css文件中的背景图片background-image:url(bgimage.gif);
 * 如：
 * @author bxiao01.oth
 *
 */
public class ImageResourceHandler extends ResourceHandler {
	public static final String REGEX_LINK = 
		"<\\s*img\\s+(?:[^>]\\s*){0,}src\\s*=\\s*[\"|']([^\"|']{1,}[a-zA-Z_0-9])\\s*[\"|']";
	
	public static final String REGEX_BACKGROUND_IMAGE = 
		"background(?:\\-image)?\\s*:[^;]*url\\(([^\\(|\\)]{1,}[a-zA-Z_0-9]['|\"]?)\\s*\\)";

	@Override
	protected Pattern getLabelPattern() {
		return Pattern.compile("("+REGEX_LINK+"|"+REGEX_BACKGROUND_IMAGE+")", CASE_IGNORE_FLAG);//match the label "<img src='...'/>"
	}
	
	protected String versionURI(Matcher m){
		String label = m.group();
		if(label.toLowerCase().matches(REGEX_LINK)) {//<img src="xxxx.jpg" />
			return super.versionURI(m, 2);
		} else if(label.toLowerCase().matches(REGEX_BACKGROUND_IMAGE)) {//background-image:url(xxxx.jpg)
			return this.versionBgImgURI(m);
		} else {
			throw new IllegalArgumentException("invalid parameter label:"+label);
		}
	}

	private String versionBgImgURI(Matcher m) {
		String label = m.group();//background-image:url(xxxx.jpg)
		try {
			String urlVal = m.group(3).trim();//xxxx.jpg
			int start = label.indexOf(urlVal);
			int length = urlVal.length();
			
			//handle it...
			if(urlVal.length() > 2) {
				if((urlVal.startsWith("'") && urlVal.endsWith("'")) 
					 || (urlVal.startsWith("\"") && urlVal.endsWith("\""))) {
					urlVal = urlVal.substring(1, urlVal.length()-1);
					start++;length--;length--;
				}
			}
			if(urlVal.contains("?")) {
				if(urlVal.contains("version")) {
					String[] urlArr = urlVal.split("\\?");
					urlVal = urlArr[0] + "?" +updateParamVersion(urlArr[1]);
				} else {
					urlVal = urlVal +"&version=" + this.getVersion();
				}
			} else {
				urlVal = urlVal + "?version="+ this.getVersion();
			}
			
			return label.substring(0, start) + urlVal + label.substring(start+length);
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Invalid label:"+label);
		}
		return label;
	}
	
	public static void main(String args[]) {
		ImageResourceHandler handler = new ImageResourceHandler();
		
		System.out.println(handler.handle("<img src=\"<%=context.getPath()%>/img/boat.gif \" \n\talt='Big Boat'></img >"));
		System.out.println(handler.handle("<img src=\"resource/img/boat.gif?key=value\" alt='Big Boat'/>"));
		System.out.println(handler.handle("<img src=\"resource/img/boat.gif?key=value&name=bxiao&version=bxiao\" alt='Big Boat'/>"));
		System.out.println(handler.handle("<img src=\"<render:resourceUrl path=\"<%= button.getImageSrc() %>\"/>\" />"));
		System.out.println(handler.handle("<img src='resource/img/boat.gif' alt='Big Boat' s='<%=context.getUrl()%>/bb.js'/>"));
		
		System.out.println(handler.handle("background-image: url(bgimage.gif );aa:url(abc);"));
		System.out.println(handler.handle("background:no-repeat right URL( ' /i/eg_bg_04.gif?a=xyz_') ;xyz"));
		System.out.println(handler.handle("BACKGROUND-image :  url( /i/eg_bg_04.gif) no-repeat;xyz"));
		System.out.println(handler.handle("BACKGROUND-image :  url(<%=ib.get() %>) no-repeat;xyz"));
		
	}
}
