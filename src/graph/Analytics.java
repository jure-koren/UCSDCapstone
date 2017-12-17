/**
 * 
 */
package graph;

import java.util.ArrayList;

/**
 * @author Jure Koren
 * 
 * Analyze graph data for key nodes
 */
public class Analytics {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// load the data, analyze and report

		// settings: (For the full ucsd data to load I had to increase Xss, ie: -Xss100m)
		// Project menu -> Properties -> Arguments -> VM Arguments -> -Xss100m
		
		String filename = "data/facebook_1000.txt";
		int numOfKeyNodes = 10;
		int levelsDeep = 2;
		
		// graph for analysis
		CapGraph graph = new CapGraph();
		
		// read the first file
		GraphReader.readFile(filename, graph);
		
		
		// * analyze the graph *

		ArrayList<GraphNode> keyNodes = new ArrayList<GraphNode>(numOfKeyNodes); 
		keyNodes = graph.findKeyNodes(numOfKeyNodes, levelsDeep);
		
		// report the key nodes
		printNodes(keyNodes, "Selected " + numOfKeyNodes + " key nodes for " + filename + ":", true);
		
		
		
		
		// find articulation points
		ArrayList<GraphNode> aPoints = new ArrayList<GraphNode>();
		aPoints = graph.findArticulationPoints();
		
		// report the key nodes
		printNodes(keyNodes, "All articulation points for " + filename + ":", false);
		
		
		
		
		// a combination of both would be the best, 
		// so let's try to get top n nodes from the articulation points
		ArrayList<GraphNode> comboNodes = new ArrayList<GraphNode>(numOfKeyNodes);
		comboNodes = graph.getTopNodesFromList(aPoints, numOfKeyNodes);
		printNodes(keyNodes, "Selected " + numOfKeyNodes + " articulation points for " + filename + ":", true);
		
		
		
		System.out.println("Finished.");
	}
	
	
	
	
	// helper method to print list of nodes
	private static void printNodes(ArrayList<GraphNode> list, String text, boolean printWeight) {
		// report the key nodes
		System.out.println(text);
		for(GraphNode node : list ) {
			// just print the id... if we had real people we could print the name or some kind of medical id
			if (printWeight) {
				System.out.println("- node " + node.getNodeId() + " with weight: " + node.getNodeWeight() );
			} else {
				System.out.println("- node " + node.getNodeId()  );
			}
		}
		System.out.println("");		
	}

}
