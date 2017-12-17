/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

/**
 * @author Jure Koren
 * 
 * Graph structure with analysis methods
 *
 */
public class CapGraph implements Graph {
	private HashMap<Integer, GraphNode> nodes = new HashMap<Integer, GraphNode>();
	private HashSet<GraphEdge> edges = new HashSet<GraphEdge>();
	private static boolean debugMode = false;
	private int dfsDiscTime = 0;
	
	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) {
		//System.out.println("addVertex: " + num);
		if (!nodes.containsKey(num) ) {
			// create new node object	
			// for now we only have the num parameter, but maybe later we'll need to add a name, profile, ...
			GraphNode newNode = new GraphNode(num);
			// add it to the list of nodes
			// we could use an array, but it will probably come handy to have and object in the future
			nodes.put(num, newNode);
		}
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		//System.out.println("addEdge: from " + from + " to " + to);
		// check arguments
		if (!nodes.containsKey(from) || !nodes.containsKey(to)) {
			throw new IllegalArgumentException("Please add the vertices/nodes first.");
		}
		
		// find nodes
		GraphNode fromNode = nodes.get(from);
		GraphNode toNode = nodes.get(to);

		// create new edge/connection and add it to the list
		// for now we only have the connection, but we could further expand it later with a relationship type, ...		
		GraphEdge newEdge = new GraphEdge(fromNode, toNode);
		edges.add(newEdge);
		
	}
	
	/*
	 * Find all key nodes in a graph
	 * Calculate the weight based on the num. of neighbors
	 * Count distant neighbors too by using a formula:
	 * # of edges/level
	 * ie: 
	 * 1st level = the number of neighbors
	 * 2nd level = number of neighbors the neighbor has, but divide by 2
	 * and so on.
	 */
	public ArrayList<GraphNode> findKeyNodes(int numOfKeyNodes, int levelsDeep) {
		System.out.print("findKeyNodes: find " + numOfKeyNodes + " key nodes (l: " + levelsDeep + ")... ");
		
		if (levelsDeep < 1) {
			throw new IllegalArgumentException("Levels should be >= 1");
		} else if (numOfKeyNodes < 1) {
			throw new IllegalArgumentException("Number of returned nodes should be >= 1");
		} else if (nodes.size() < 1) {
			throw new IllegalArgumentException("No nodes");
		}
		
		// I'll be using a sorted TreeSet for this task
		TreeSet<GraphNode> sortedTree = new TreeSet<GraphNode>(new GraphWeightCompare());
		
		// set initial weight to 1
		for (HashMap.Entry<Integer, GraphNode> node : getNodes().entrySet()) {
			node.getValue().setNodeWeight((float) 1);
		}
		
		// for all levels (level: how much weight has one neighbor)		
        for(int l = 1; l<=levelsDeep; l++){
    		// check all nodes
        	
        	// first set all nodes curr. weight to prev.
        	for (HashMap.Entry<Integer, GraphNode> node : getNodes().entrySet()) {
        		node.getValue().setPrevNodeWeight(node.getValue().getNodeWeight() );        		
        	}
        	
        	// go through all nodes
        	for (HashMap.Entry<Integer, GraphNode> node : getNodes().entrySet()) {
        		// get node
        		GraphNode n = node.getValue();
        		// calculate weight to add
        		float addWeight = getNeighborsPrevWeight(n) / (float)l;
        		// add weight
        		n.setNodeWeight( n.getPrevNodeWeight() + addWeight );
        		
        		// in the last loop add the node to the tree set
        		if (l == levelsDeep) {
        			sortedTree.add(n);
        		}
        	}
        	System.out.print(".");
        }		
		
        // get the last elements from the tree set and return them as an arraylist
        ArrayList<GraphNode> keyNodes = new ArrayList<GraphNode>(numOfKeyNodes);
		
        int n = 0;
        Iterator<GraphNode> iter = sortedTree.iterator();
        while (n < numOfKeyNodes && iter.hasNext()) {
            GraphNode t = iter.next();
            keyNodes.add(t);
            n++;
        }
        System.out.println(" OK");
        
		return keyNodes;
	}
	
	/*
	 * sum of all node's neighbors prev. weights
	 */
	private float getNeighborsPrevWeight(GraphNode node) {
		float sum = 0f;
		
		// for all my neighbors; add their weight to my sum
		for (GraphNode n : node.getNeighbors()) {
			// add that node's prev. weight
			sum += n.getPrevNodeWeight();
		}
		
		return sum;
	}
	
	
	
	
	
	
	/*
	 * Return the cut nodes / cut vertices
	 * = The elements that would separate the graph if we were to remove them
	 */
	public ArrayList<GraphNode> findArticulationPoints() {
		if (nodes.size() < 1) {
			throw new IllegalArgumentException("No nodes");
		}
		System.out.print("findArticulationPoints: searching ... ");
		
		ArrayList<GraphNode> artPoints = new ArrayList<GraphNode>();
		
		// set discovery time to 0
		dfsDiscTime = 0;
		
		// keep track of visited and found nodes
		Set<GraphNode> visited = new HashSet<>();
		Set<GraphNode> articulationPoints = new HashSet<>();
		
		// get first node
		GraphNode startNode = nodes.entrySet().iterator().next().getValue();
		
		// keep track of time visited
		Map<GraphNode, Integer> timeVisited = new HashMap<>();
        Map<GraphNode, Integer> lowestTime = new HashMap<>();
        
        Map<GraphNode, GraphNode> parentNode = new HashMap<>();
        
		// do the recursive dfs search
        dfs(visited,articulationPoints,startNode,timeVisited,lowestTime,parentNode);
		
        // convert it to an arraylist, so we return the same structure as findKeyNodes
        artPoints = new ArrayList<GraphNode> (articulationPoints);
        
        System.out.println(" OK");
		return artPoints;
	}
	
	/*
	 * dfs search method that is used by findArticulationPoints 
	 */
	private void dfs(Set<GraphNode> visited,
					Set<GraphNode> articulationPoints,
					GraphNode node,
					Map<GraphNode, Integer> timeVisited,
					Map<GraphNode, Integer> lowestTime,
					Map<GraphNode, GraphNode> parentNode) {
		
		// add first node to the lists
        visited.add(node);
        timeVisited.put(node, dfsDiscTime);
        lowestTime.put(node, dfsDiscTime);
        
        dfsDiscTime++;
        
        boolean isArticulationPoint = false;
        int numOfChilds = 0;
        
        // loop through this node's neighbors
        for(GraphNode neighbor : node.getNeighbors() ){
            // skip if it's our parent - we won't go back
            if(!neighbor.equals(parentNode.get(node))) {
	            // check if we've visited it already
	            if(!visited.contains(neighbor)) {
	            	// keep track
	                parentNode.put(neighbor, node);
	                numOfChilds++;
	                
	                // print progress
					if(dfsDiscTime % 100==0) {
						System.out.print(".");
					}   
	                
	                // recursive visit that neighbor
	                dfs(visited, articulationPoints, neighbor, timeVisited, lowestTime, parentNode);
	
	                if(timeVisited.get(node) <= lowestTime.get(neighbor)) {
	                	// ok, we found it
	                    isArticulationPoint = true;
	                } else {
	                    // find lowest time
	                	lowestTime.compute(node, (currentVertex, time) -> Math.min(time, lowestTime.get(neighbor))
	                    );
	                }
	
	            } else { 
	            	// if we've already visited it, try to find better low time.
	                // find lowest time
	            	lowestTime.compute(node, (currentVertex, time) -> Math.min(time, timeVisited.get(neighbor))
	                );
	            }
            }
        }

        // if one of the two conditions are true -> it is articulation point.
        if((parentNode.get(node) == null && numOfChilds >= 2) || parentNode.get(node) != null && isArticulationPoint ) {
            articulationPoints.add(node);
        }
		
	}
	
	
	// return top n nodes from list (sorted by weight)
	public ArrayList<GraphNode> getTopNodesFromList(ArrayList<GraphNode> inputList, int maxNodes) {
		ArrayList<GraphNode> topNodes = new ArrayList<GraphNode>(maxNodes);
		
		TreeSet<GraphNode> sortedTree = new TreeSet<GraphNode>(new GraphWeightCompare());
		for(GraphNode node : inputList ) {
			sortedTree.add(node);
		}
		
        int n = 0;
        Iterator<GraphNode> iter = sortedTree.iterator();
        while (n < maxNodes && iter.hasNext()) {
            GraphNode t = iter.next();
            topNodes.add(t);
            n++;
        }
		
		return topNodes;
	}	
	
	
	
	
	

	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		/* 
		 * This method returns the nodes and edges in your graph in a format suitable for grading. 
		 * It should return a HashMap where the keys in the HashMap are all the vertices in the graph, 
		 * and the values are the Set of vertices that are reachable from the vertex key via a directed 
		 * edge. The returned representation ignores edge weights and multi-edges, but it's sufficient 
		 * for our grading of the two main methods on this assignment.   */
		
		// new map that we will return
		HashMap<Integer, HashSet<Integer>> fullMap = new HashMap<Integer, HashSet<Integer>>();
		
		// go through all the nodes
		for (HashMap.Entry<Integer, GraphNode> node : nodes.entrySet()) {
			// get entry
		    //System.out.println("Key = " + node.getKey() + ", Value = " + node.getValue());
			
			// get key
		    int key = node.getKey();
		    HashSet<Integer> reachableVertices = new HashSet<Integer>();
		    
		    // get node object
		    GraphNode currNode = node.getValue();
		    
		    // TODO: the values are the Set of vertices that are reachable from the vertex key via a directed edge
		    // iterate the edges of the current node
		    for(GraphEdge edge : currNode.getEdges() ) {
		    	// get the destination node value/id and add it to the reachable vertices
		    	reachableVertices.add(edge.getToNode().getNodeId() );
		    }
		    
		    // add what we found to the output hash map
		    fullMap.put(key, reachableVertices);
		}
		
		// return the map
		return fullMap;
	}
	
	// just convert the nodes to a stack
	private Stack<Integer> getNodeIdsAsStack() {
		Stack<Integer> vertices = new Stack<Integer>();
		for (HashMap.Entry<Integer, GraphNode> node : nodes.entrySet()) {
			vertices.push(node.getValue().getNodeId() );
		}
		return vertices;
	}
	
	
	
	/* getters/setters */
	public HashMap<Integer, GraphNode> getNodes() {
		return nodes;
	}

	public HashSet<GraphEdge> getEdges() {
		return edges;
	}
	/* getters/setters */

	
	
	/* debuggging */
	private static void debugPrint(String text) {
		if (debugMode) {
			System.out.println(text);
		}
	}
		
	
	/* print map for debugging to system.out
	 * @param fullMap - the map returned from exportGraph
	 */
	public static void printMap(HashMap<Integer, HashSet<Integer>> fullMap) {
		if (debugMode) {
			System.out.println("");
	        System.out.println("Printing map...");
	
	        for (HashMap.Entry<Integer, HashSet<Integer>> entry : fullMap.entrySet()) {
	        	System.out.println("Entry: " + entry.getKey() + ", links: ");
	        	System.out.print(" - link to: ");
	        	for(Integer i : entry.getValue() ) {
	        		System.out.print(i + ", ");            		
	        	}
	        	System.out.println("");
	        }		
		}
	}
	
	// for debugging
	private static void printStackOfNodes(Stack<GraphNode> stackOfNodes) {
		if (debugMode) {
			System.out.print("Nodes: ");
			for(GraphNode node : stackOfNodes) {
				System.out.print(node.getNodeId() + ", " );
			}
			System.out.println(".");
		}
	}	
	
	// for debugging
	private void printGraph(CapGraph graph) {
		if (debugMode) {
			System.out.println("");
			System.out.print("[print] Graph nodes: ");
			for (HashMap.Entry<Integer, GraphNode> node : graph.getNodes().entrySet()) {
				System.out.print(node.getKey() + ", " );
			}
			System.out.println("");
			
			System.out.print("[print] Graph edges: ");
			for (GraphEdge edge : graph.getEdges() ) {
				System.out.print(edge.toString() + ", " );
			}
			System.out.println("");
		}
	}	
	
	
}
