package graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

/**
 * @author Jure Koren
 * 
 * Read a graph from a text file
 * The file should have one node per line with from and to integers
 */
public class GraphReader {
	public static void readFile(String filename, Graph g) {
		System.out.println("Reading file: " + filename + ": ");
		
		
		// scanner object to parse numbers from string
		Scanner sc;
		// Scanner.useDelimeter("\\D+")
		
		// read file, line by line, using buffered reader
		// i guess we could use scanner for the whole file, 
		// but then 1 error would make all the other data wrong
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			int i = 0;
		    while ((line = br.readLine()) != null) {
		       // process the line.
				sc = new Scanner(line);
				// get first vertex
				if (sc.hasNextInt()) {
					int v1 = sc.nextInt();
					// get 2nd vertex
					if (sc.hasNextInt()) {
						int v2 = sc.nextInt();
						
						// add them to the graph
						g.addVertex(v1);
						g.addVertex(v2);
						g.addEdge(v1, v2);
						
						// progress display
						i++;
						if(i % 100==0) {
							System.out.print(".");
						}						
					}
				}
		    }
		    System.out.println(" OK");
		} catch (Exception e) {
            e.printStackTrace();
            return;
        }	
		System.out.println("Reading file: " + filename + " finished.");
		System.out.println("");
	}

}
