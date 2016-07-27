package cn.szse.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ResourceHandlerFactory {
	
	private static final Map<String, Class<? extends ResourceHandler>> 
		handlerMap = new HashMap<String,Class<? extends ResourceHandler>>();
	
	static {
		handlerMap.put("a", LinkResourceHandler.class);
		handlerMap.put("script", JSResourceHandler.class);
		handlerMap.put("link", CSSResourceHandler.class);
		handlerMap.put("img", ImageResourceHandler.class);
	}
	
	public static ResourceHandler buildChainHandler(Set<String> typeSet) throws Exception {
		if(typeSet == null || typeSet.size() < 1) 
			throw new IllegalArgumentException("parameter postfixSet is null or empty");
		ResourceHandler firstHandler = null;
		ResourceHandler previousHandler = null;
		for(String key : typeSet) {
			Class<? extends ResourceHandler> klass = handlerMap.get(key.toLowerCase());
			if(klass == null)
				throw new IllegalArgumentException("parameter [typeSet] contains an invalid type:"+key);
			ResourceHandler handler = klass.newInstance();
			if(previousHandler == null) {
				previousHandler = handler;
				firstHandler = handler;
			} else {
				previousHandler.setNextHandler(handler);
				previousHandler = handler;
			}
		}
			
		return firstHandler;
	}
	
	public static void main(String args[]) throws Exception{
		Set<String> typeSet = new HashSet<String>();
		typeSet.add("a");
		typeSet.add("script");
		typeSet.add("link");
		typeSet.add("img");
		
		ResourceHandler handler = ResourceHandlerFactory.buildChainHandler(typeSet);
		
		String html = "<link rel='stylesheet' type='text/css' href='/c5.css?key=value&version=bbbb&name=bxiao' />\n" +
		                  "<IMG src='resource/img/boat.gif' alt='Big Boat'/>\n" +
				          "<script type='text/javascript' src = \"js/app/main.js?key=value&key1=value1\" ></script>\n" +
		                  "<a href=\"resource/page/hello.html?key=bbb&version=1&a&name=bxiao\">hello</a>";
		String res = handler.handle(html);
		
		System.out.println(res);
	}
}
