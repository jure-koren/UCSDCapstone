/**
 * 
 */
package graph;

/**
 * @author Jure Koren
 *
 */
public class GraphEdge {
	private GraphNode fromNode;
	private GraphNode toNode;
	
	
	/**
	 * empty edge
	 */
	public GraphEdge() {
		
	}
	
	
	/**
	 * @param fromNode
	 * @param toNode
	 */
	public GraphEdge(GraphNode fromNode, GraphNode toNode) {
		super();
		this.fromNode = fromNode;
		this.toNode = toNode;
		// set link
		fromNode.addEdge(this);
	}
	/**
	 * @return the fromNode
	 */
	public GraphNode getFromNode() {
		return fromNode;
	}
	/**
	 * @param fromNode the fromNode to set
	 */
	public void setFromNode(GraphNode fromNode) {
		this.fromNode = fromNode;
	}
	/**
	 * @return the toNode
	 */
	public GraphNode getToNode() {
		return toNode;
	}
	/**
	 * @param toNode the toNode to set
	 */
	public void setToNode(GraphNode toNode) {
		this.toNode = toNode;
	}


	@Override
	public String toString() {
		return "[" + fromNode.getNodeId() + " -> " + toNode.getNodeId() + "]";
	}
	
	
}
