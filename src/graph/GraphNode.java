/**
 * 
 */
package graph;

import java.util.HashSet;

/**
 * @author Jure Koren
 * 
 * Object for graph nodes
 */
public class GraphNode {
	private HashSet<GraphEdge> edges = new HashSet<GraphEdge>();
	private int nodeId;
	private float nodeWeight;
	private float prevNodeWeight;
	
	/**
	 * @param name
	 */
	public GraphNode(int newNodeId) {
		super();
		this.nodeId = newNodeId;
	}

	/**
	 * @return the nodeId
	 */
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * Add new edge to my list of edges
	 * 
	 * @param newEdge - edge to add
	 */
	public void addEdge(GraphEdge newEdge) {
		edges.add(newEdge);
	}
	
	/**
	 * @return the edges
	 */
	public HashSet<GraphEdge> getEdges() {
		return edges;
	}

	/**
	 * @param edges the edges to set
	 */
	public void setEdges(HashSet<GraphEdge> edges) {
		this.edges = edges;
	}
	
	public float getNodeWeight() {
		return nodeWeight;
	}

	public void setNodeWeight(float nodeWeight) {
		this.nodeWeight = nodeWeight;
	}	
	
	/**
	 * @return the prevNodeWeight
	 */
	public float getPrevNodeWeight() {
		return prevNodeWeight;
	}

	/**
	 * @param prevNodeWeight the prevNodeWeight to set
	 */
	public void setPrevNodeWeight(float prevNodeWeight) {
		this.prevNodeWeight = prevNodeWeight;
	}

	/**
	 * @return the neighbors
	 */
	public HashSet<GraphNode> getNeighbors() {
		HashSet<GraphNode> neighbors = new HashSet<GraphNode>(); 
		for(GraphEdge edge : edges) {
			neighbors.add(edge.getToNode() );
		}
		return neighbors;
	}	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GraphNode [edges=" + edges + ", nodeId=" + nodeId + "]";
	}
	
	
	
}
