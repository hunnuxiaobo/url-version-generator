package cn.szse.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * static resource handler
 * @author bxiao01.oth
 *
 */
public abstract class ResourceHandler {
	protected static final int CASE_IGNORE_FLAG = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
	private ResourceHandler nextHandler;
	private String currentVersion;

	protected abstract Pattern getLabelPattern();
	
	/**
	 * handle plain text
	 * @param content original text
	 * @return text which contains versions
	 */
	public String handle(String text) {
		String result = appendURLVersion(text);
		if(this.getNextHandler() != null) {
			result = this.getNextHandler().handle(result);
		}
		return result;
	}
	
	/**
	 * append version to URL as a parameter, if the URL has already contained
	 * a version parameter then update its value 
	 * @param text given text which contains URLs
	 * @return text which contains versions in URLs 
	 */
	private String appendURLVersion(String text) {
		Matcher m = getLabelPattern().matcher(text);
		StringBuffer sb = new StringBuffer();
		
		while (m.find()) {//find the next matched object which contains URI
			m.appendReplacement(sb, versionURI(m));
		}
		m.appendTail(sb);
		
		return sb.toString();
	}

	protected String versionURI(Matcher m){
		return this.versionURI(m, 1);
	}
	
	protected String versionURI(Matcher m, int group){
		String label = m.group();
		try {
			String hrefValue = m.group(group);
			
			int start = label.indexOf(hrefValue);
			int length = hrefValue.length();
			
			if(hrefValue.contains("?")) {
				if(hrefValue.contains("version")) {
					String[] urlArr = hrefValue.split("\\?");
					hrefValue = urlArr[0] + "?" +updateParamVersion(urlArr[1]);
				} else {
					hrefValue = hrefValue +"&version=" + this.getVersion();
				}
			} else {
				hrefValue = hrefValue + "?version="+ this.getVersion();
			}
			
			return label.substring(0, start) + hrefValue + label.substring(start+length);
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Invalid label:"+label);
		}
		return label;
	}
	
	protected String updateParamVersion(String paramStr) {
		String[] params = paramStr.split("&");
		for(int i = 0; i < params.length; i++) {
			if(params[i].contains("version")) {
				params[i] = "version="+this.getVersion();
			}
		}
		StringBuffer sb = new StringBuffer();
		for(String p : params) {
			sb.append(p).append("&");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	protected String getVersion() {
		if(this.currentVersion == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			this.currentVersion = sdf.format(new Date());
		}
		return this.currentVersion;
	}
	
	public ResourceHandler getNextHandler() {
		return nextHandler;
	}
	public void setNextHandler(ResourceHandler nextHandler) {
		this.nextHandler = nextHandler;
	}
}
