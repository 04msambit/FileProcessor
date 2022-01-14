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

		Thread t1 = new Thread(new ParallelTask("group1"));
		Thread t2 = new Thread(new ParallelTask("group2"));
		Thread t3 = new Thread(new ParallelTask("group3"));

		// Populate the group queues based on the initial contents of the directory
		populateQueue();

		t1.start();
		t2.start();
		t3.start();

		try {
			/***
			 * This is a continuous running process , so that if we keep adding files to the directory, it will
			 * be processed. The program exits if there is an exception or the user forcefully exits it.
			 * The main thread sleeps for 60 seconds between loops.
			 */
			while (true) {

				Thread.sleep(60000);
				populateQueue();

				t1.join();
				t1 = new Thread(new ParallelTask("group1"));
				t1.start();

				t2.join();
				t2 = new Thread(new ParallelTask("group2"));
				t2.start();

				t3.join();
				t3 = new Thread(new ParallelTask("group3"));
				t3.start();
			}
		}catch (Exception ex){
			System.out.println("Exception Caught - Exiting: " + ex.getMessage());
		}
	}

	/***
	 *
	 * Method which reads the file from the disk and creates a .out file. We load a single line onto to memory
	 * and the write it to the output file. We do not load the entire file into the memory. This can help to process
	 * larger files which likely not fit in the memory
	 *
	 * @param source Path of the Input .txt file
	 * @param destination Path of the processed .out file
	 */
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
			System.out.println("Created: " + destination + " for " + source);
		}catch (Exception ex){
			System.out.println(ex.getMessage());
		}
	}

	/***
	 * Method which populates the specific Queues for each group of files
	 */
	public static void populateQueue() {
		File folder = new File("./");
		for (File fileEntry : folder.listFiles()) {
			if(!processedFileSet.contains(fileEntry.getName()) && fileEntry.getName().endsWith(".txt")) {
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
	}
}
