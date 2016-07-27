package cn.szse.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CoreFacade {
	private static final int maxHandlerCount = 50;
	private FileSearcher searcher;
	private List<String> targetFiles = new ArrayList<String>();
	
	//firstly: searching
	public void search(String path, Set<String> postfixSet) throws Exception {
		FileSearcher.targetFileQueue.clear();//clear history records
		searcher = new FileSearcher(path, postfixSet);
		searcher.start();
		this.sleep();
	}
	
	//secondly: handle files
	public void handle(Set<String> typeSet) throws Exception {
		ExecutorService exec = Executors.newFixedThreadPool(maxHandlerCount);
		try {
			List<Future<List<String>>> futures = exec.invokeAll(this.initFileHandlerTasks(typeSet));
			for(Future<List<String>> f : futures) {
				targetFiles.addAll(f.get());//waiting the result
			}
		} catch(Exception e) {
			throw e;
		} finally {
			try {exec.shutdown();} catch(Exception ex) {ex.printStackTrace();}
		}
	}
	
	//thirdly: retrieve result
	public List<String> getTargetFiles() {
		return targetFiles;
	}
	
	private List<FileHandlerTask> initFileHandlerTasks(Set<String> typeSet) throws Exception {
		List<FileHandlerTask> tasks = new ArrayList<FileHandlerTask>();
		for(int i = 0; i < maxHandlerCount; i++) {
			FileHandlerTask task = new FileHandlerTask(ResourceHandlerFactory.buildChainHandler(typeSet));
			tasks.add(task);
		}
		return tasks;
	}
	
	private void sleep() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws Exception {
		Set<String> postfixSet = new HashSet<String>();
		postfixSet.add("html");
		postfixSet.add("aspx");
		postfixSet.add("jsp");
		postfixSet.add("css");
		
		Set<String> typeSet = new HashSet<String>();
		typeSet.add("a");
		typeSet.add("script");
		typeSet.add("link");
		typeSet.add("img");
		
		String path = "D:/tmp/version_test/assets2";
		
		CoreFacade controller = new CoreFacade();
		long start = System.currentTimeMillis();
		controller.search(path, postfixSet);
		controller.handle(typeSet);
		long cost = System.currentTimeMillis() -start;
		
		System.out.println("cost time : "+cost+" millis");
		for(String file : controller.getTargetFiles()) {
			System.out.println(file);
		}
	}
}
