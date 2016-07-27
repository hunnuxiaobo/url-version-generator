package cn.szse.model;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class FileHandlerTask implements Callable<List<String>>{
	private static final int perWriteSize = 256 * 1024;
	private static final String charsetName = "UTF-8";
	private ResourceHandler handler;
	
	public FileHandlerTask(ResourceHandler handler){
		this.handler = handler;
	}
	
	@Override
	public List<String> call() throws Exception {
		List<String> files = new ArrayList<String>();
		while(true) {
			String fileName = FileSearcher.targetFileQueue.poll(200, TimeUnit.MILLISECONDS);
			if(fileName != null) {
				System.out.println("handle file: "+fileName);//TODO
				String origText = this.readAsText(fileName);
				String finalText = handler.handle(origText);
				if(!origText.equals(finalText)) {
					this.writeText(fileName, finalText);
					files.add(fileName);
				}
			} else if(FileSearcher.isSearchingFinished()) {
				break;
			}
		}
		
		return files;
	}
	
	private String readAsText(String fileName) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		transit(new FileInputStream(fileName), bos);
		return bos.toString(charsetName);
	}
	
	private void writeText(String file, String content) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(content.getBytes(charsetName));
		fos.flush();
		fos.close();
	}

	private void transit(InputStream in, OutputStream out) throws IOException {
		int len = 0;
		byte[] buff = new byte[perWriteSize];
		byte[] temp = null; 
		while((len = in.read(buff)) > 0) {
			if(len == buff.length) {
				temp = buff;
			} else {
				temp = new byte[len];
				System.arraycopy(buff, 0, temp, 0, len);
			}
			
			out.write(temp);
		}
		
		in.close();
		out.flush();
		out.close();
	}
}
