import java.lang.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Assignment {

	private static Queue<String> group1Queue = new LinkedList<String>();
	private static Queue<String> group2Queue = new LinkedList<String>();
	private static Queue<String> group3Queue = new LinkedList<String>();
	private static HashSet<String> processedFileSet = new HashSet<String>();

	private static class ParallelTask implements Runnable {

		private String groupName;

		public ParallelTask(String group) {
			this.groupName = group;
		}

		@Override public void run() {
			Queue<String> queue = null;
			if(this.groupName == "group1"){
				queue = group1Queue;
			}else if(this.groupName == "group2"){
				queue = group2Queue;
			}else if(this.groupName == "group3"){
				queue = group3Queue;
			}

			while(!queue.isEmpty()){
				String source = queue.poll();
				readAndWriteFile(source, source.replace(".txt", ".out"));
			}

		}
	}

	public static void main(String[] args) {

		Thread t1 = new Thread(new ParallelTask("group1"), "Thread - T1");
		Thread t2 = new Thread(new ParallelTask("group2"), "Thread - T2");
		Thread t3 = new Thread(new ParallelTask("group3"), "Thread - T3");

		File folder = new File("./");

		for (File fileEntry : folder.listFiles()) {
			if(!processedFileSet.contains(fileEntry.getName())) {
				processedFileSet.add(fileEntry.getName());
				if (fileEntry.getName().startsWith("group1")) {
					group1Queue.offer(fileEntry.getName());
				} else if (fileEntry.getName().startsWith("group2")) {
					group2Queue.offer(fileEntry.getName());
				} else if (fileEntry.getName().startsWith("group3")) {
					group3Queue.offer(fileEntry.getName());
				}
			}
		}

		t1.start();
		t2.start();
		t3.start();
	}
	public static void readAndWriteFile(String source, String destination){
		try {
			BufferedWriter buf = new BufferedWriter(new FileWriter(destination, true));
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(source), StandardCharsets.UTF_8));
			String line;
			while ((line = br.readLine()) != null) {
				buf.append(line.toLowerCase());
				buf.newLine();
			}
			buf.close();
		}catch (Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
