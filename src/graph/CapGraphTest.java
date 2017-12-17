/**
 * 
 */
package graph;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.Before;

/**
 * @author User
 *
 */
public class CapGraphTest {

	CapGraph graph1;
	CapGraph graph2;
	CapGraph graph3;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		graph1 = new CapGraph();
		addVerticesAndEdge(graph1, 1, 2);
		addVerticesAndEdge(graph1, 2, 3);
		addVerticesAndEdge(graph1, 3, 1);
		addVerticesAndEdge(graph1, 3, 4);
		addVerticesAndEdge(graph1, 3, 5);
		addVerticesAndEdge(graph1, 4, 5);
		
		graph2 = new CapGraph();
		addVerticesAndEdge(graph2, 1, 2);
		
		graph3 = new CapGraph();
		addVerticesAndEdge(graph3, 1, 2);
		addVerticesAndEdge(graph3, 2, 3);

	}	
	
	// helper method for tests
	private void addVerticesAndEdge(CapGraph graph, int from, int to) {
		graph.addVertex(from);
		graph.addVertex(to);
		graph.addEdge(from, to);
		graph.addEdge(to, from);
	}
	
	/** Test if the data is loaded
	 */
	@Test
	public void testGraph()
	{
		// basic tests
		assertEquals("Testing size for graph 1", 5, graph1.getNodes().size());
		assertEquals("Testing size for graph 2", 2, graph2.getNodes().size());
		assertEquals("Testing size for graph 3", 3, graph3.getNodes().size());
		
		
		
	}	

	/** Test if the data is loaded
	 */
	@Test
	public void testFindPoints()
	{
		
		ArrayList<GraphNode> kNodes = new ArrayList<GraphNode>();
		ArrayList<GraphNode> aPoints = new ArrayList<GraphNode>();
		
		kNodes = graph1.findKeyNodes(1, 1);
		
		aPoints = graph1.findArticulationPoints();
		assertEquals("Testing art. point value for graph 1", 3, aPoints.get(0).getNodeId() );

		aPoints = graph2.findArticulationPoints();
		assertEquals("Testing art. points for graph 2", 0, aPoints.size());

		aPoints = graph3.findArticulationPoints();
		assertEquals("Testing art. point value for graph 3", 2, aPoints.get(0).getNodeId() );
		
	}	
	
	
}
