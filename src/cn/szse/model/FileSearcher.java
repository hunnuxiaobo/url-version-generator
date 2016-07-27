package cn.szse.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class FileSearcher extends Thread {
	private static final int MAX_THREAD_NUM = 10;//specify the max number of searcher threads
	public static AtomicInteger counter = new AtomicInteger(0);
	public static LinkedBlockingQueue<String> targetFileQueue = new LinkedBlockingQueue<String>();
	
	private String path;
	private Set<String> postfixSet;
	
	public FileSearcher(String path, Set<String> postfixSet) {
		this.path = path;
		this.postfixSet = postfixSet;
		counter.incrementAndGet();
		//System.out.println("thread number: "+counter.get());
	}
	
	@Override
	public void run() {
		try {
			this.filter(path);			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			counter.decrementAndGet();
		}
	}
	
	public static boolean isSearchingFinished() {
		return FileSearcher.counter.get() < 1;
	}
	
	private void filter(String path) throws InterruptedException {
		File file = new File(path);
		if(file.isFile()) {
			if(this.isFileMatched(file.getAbsolutePath())) {
				targetFileQueue.put(file.getAbsolutePath());
			}
		} else {
			List<String> matchedFiles = this.listMatchedFiles(file);
			for(String f : matchedFiles)
				targetFileQueue.put(f);

			List<File> dirs = this.listDirs(file);
			for(File dir : dirs) {
				if(counter.get() < MAX_THREAD_NUM) {
					FileSearcher fs = new FileSearcher(dir.getAbsolutePath(), this.postfixSet);
					fs.start();
					continue;
				}
				this.filter(dir.getAbsolutePath());
			}
		}
	}
	
	private List<File> listDirs(File dir) {
		List<File> dirs = new ArrayList<File>();
		File[] files = dir.listFiles();
		for(File f : files){
			if(f.isDirectory())
				dirs.add(f);
		}
		return dirs;
	}
	
	private List<String> listMatchedFiles(File dir) {
		List<String> names = new ArrayList<String>();
		File[] files = dir.listFiles();
		for(File f : files){
			if(f.isFile() && this.isFileMatched(f.getName()))
				names.add(f.getAbsolutePath());
		}
		return names;
	}
	
	private boolean isFileMatched(String fileName) {
		String postfix = fileName.substring(fileName.lastIndexOf(".") + 1);
		if(this.postfixSet.contains(postfix.toLowerCase())) {
			return true;
		}
			
		return false;
	}
	
	public static void main(String argts[]) throws InterruptedException {
		Set<String> ps = new HashSet<String>();
		ps.add("html");
		ps.add("js");
		ps.add("css");
		ps.add("jsp");
		ps.add("jpf");
		ps.add("xml");
		ps.add("java");
		String dir = "D:\\workshop_projects\\WEB130135-CI";
		
		FileSearcher fs = new FileSearcher(dir, ps);
		fs.start();
		long start = System.currentTimeMillis();
		Thread.sleep(10);
		while(counter.get() > 0) {
			Thread.sleep(10);
		}
		long cost = System.currentTimeMillis() - start - 10;
		LinkedBlockingQueue<String> files = FileSearcher.targetFileQueue;
		String[] fileArr = files.toArray(new String[0]);
		Arrays.sort(fileArr, new Comparator<String>(){
			
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});
		for(String f : fileArr) {
			System.out.println(f);
		}
		System.out.println("\ntotal number:"+files.size());
		System.out.println("cost time : "+cost+" millis");
	}
}
