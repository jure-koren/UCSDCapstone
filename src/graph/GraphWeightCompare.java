/**
 * 
 */
package graph;

import java.util.Comparator;

/**
 * @author Jure Koren
 *
 * Compare two nodes by their weight (# of friends/acquaintances)
 */
class GraphWeightCompare implements Comparator<GraphNode> {
	
	@Override
    public int compare(GraphNode e1, GraphNode e2) {
    	Float w1 = e1.getNodeWeight();
    	Float w2 = e2.getNodeWeight();
    	// compare the weights, but invert it - we want the biggest weight first
    	int retval =  w1.compareTo(w2)*-1;
    	return retval;
    }
    
}