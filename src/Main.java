import org.jgrapht.Graph;
import org.jgrapht.alg.flow.mincost.CapacityScalingMinimumCostFlow;
import org.jgrapht.alg.flow.mincost.MinimumCostFlowProblem;
import org.jgrapht.alg.interfaces.MinimumCostFlowAlgorithm;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void minflowtest()
    {
         /*
        EMD graph
        Two clusters P and Q
        sizeof(P) = sizeof(Q) = n
        P(i) is connected with every element of Q (so this is essentially K(5,5) graph)
        Input: the distances (cost) between Pi and Qj. That is, n (n - 1)/2 cases
        Input: weights of cluster P
        Input: weights of cluster Q
         */
        int n = 5;
        int[] distP = new int[n];
        int[] distQ = new int[n];
        int[] weightsP = new int[n];
        int[] weightsQ = new int[n];
        // construct the flow graph so that we can reduce to min-flow
        Graph<Integer, DefaultWeightedEdge> gtest = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        HashMap<DefaultWeightedEdge, Integer> d1 = new HashMap<>();
        gtest.addVertex(0);
        gtest.addVertex(1);
        gtest.addVertex(2);
        gtest.addVertex(3);
        DefaultWeightedEdge dw = gtest.addEdge(0, 1);
        gtest.setEdgeWeight(dw, 1);
        d1.put(dw, 2);
        dw = gtest.addEdge(1, 2);
        gtest.setEdgeWeight(dw, 1);
        d1.put(dw, 3);
        dw = gtest.addEdge(2, 3);
        gtest.setEdgeWeight(dw, 1);
        d1.put(dw, 6);
        dw = gtest.addEdge(1, 3);
        gtest.setEdgeWeight(dw, 1);
        d1.put(dw, 1);
        dw = gtest.addEdge(0, 2);
        gtest.setEdgeWeight(dw, 1);
        d1.put(dw, 4);
        // supply and demand node
        HashMap<Integer, Integer> d2 = new HashMap<>();
        d2.put(0, 5);
//        d2.put(1, 50000);
//        d2.put(2, 50000);
        d2.put(3, -5);
        MinimumCostFlowProblem<Integer, DefaultWeightedEdge> mincostflow = new MinimumCostFlowProblem.MinimumCostFlowProblemImpl<>(gtest, v->d2.getOrDefault(v,0), v->d1.getOrDefault(v,0));
        CapacityScalingMinimumCostFlow<Integer, DefaultWeightedEdge> minimumCostFlowAlgorithm =
                new CapacityScalingMinimumCostFlow<>(1000);
        MinimumCostFlowAlgorithm.MinimumCostFlow<DefaultWeightedEdge> minimumCostFlow =
                minimumCostFlowAlgorithm.getMinimumCostFlow(mincostflow);
        System.out.println(minimumCostFlow.getCost());
        System.out.println("foo");
    }

    /**
     *
     * @param vertices
     * @param edges
     * @param edge_weights
     * @param supply
     * @param demand
     * @param capacity
     * @return
     */
    public static double getEMD(int[] vertices, int[][] edges, int[] edge_weights, int[][] supply, int[][] demand, int[] capacity)
    {
        Graph<Integer, DefaultWeightedEdge> grapher = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        HashMap<Integer, Integer> supplydemand = new HashMap<>();
        HashMap<DefaultWeightedEdge, Integer> cap = new HashMap<>();
        // first add the vertices
        for (int i: vertices)
        {
            grapher.addVertex(i);
        }
        // then add the supply vertices and the demand vertices
        for (int[] i : supply)
        {
            supplydemand.put(i[0], i[1]);
        }
        // and the demand (value must be negative)
        for (int[] i : demand)
        {
            supplydemand.put(i[0], -1*i[1]);
        }
        // now add the edges and their weights, and their capacities
        for (int i = 0; i < edges.length; i++)
        {
            // get the edge
            DefaultWeightedEdge dw = grapher.addEdge(edges[i][0], edges[i][1]);
            grapher.setEdgeWeight(dw, edge_weights[i]);
            cap.put(dw, capacity[i]);
        }
        MinimumCostFlowProblem<Integer, DefaultWeightedEdge> mincostflow = new MinimumCostFlowProblem.MinimumCostFlowProblemImpl<>(grapher, v->supplydemand.getOrDefault(v,0), v->cap.getOrDefault(v,0));
        CapacityScalingMinimumCostFlow<Integer, DefaultWeightedEdge> minimumCostFlowAlgorithm =
                new CapacityScalingMinimumCostFlow<>(1000);
        MinimumCostFlowAlgorithm.MinimumCostFlow<DefaultWeightedEdge> minimumCostFlow =
                minimumCostFlowAlgorithm.getMinimumCostFlow(mincostflow);
        Map<DefaultWeightedEdge, Double> h = minimumCostFlow.getFlowMap();
        System.out.println(minimumCostFlow.getCost());
        // get sum of all flows
        double flowcost = 0;
        for (Double d: minimumCostFlow.getFlowMap().values())
        {
            flowcost+=d;
        }
        return minimumCostFlow.getCost()/flowcost;
    }
    public static void main(String[] args) {
	// write your code here
//    int[] vertices = new int[]{1, 2};
//    int[][] edges = new int[][]{{1,2}};
//    int[] edge_weights = new int[]{5};
//    int[][] supply = new int[][]{{1,3}};
//    int[][] demand = new int[][]{{2,3}}; // must be positive
//    int[] capacity = new int[]{4};
      int[] vertices = new int[]{1,2,3,4};
      int[][] edges = new int[][]{{1,2},{1,3}, {2,3}, {2,4}, {3,4}};
      int[] edge_weights = new int[]{2,3,1,6,2};
      int[][] supply = new int[][]{{1,5}};
      int[][] demand = new int[][]{{4, 5}};
      int[] capacity = new int[]{4,1,1,5,4};
    System.out.println(getEMD(vertices, edges, edge_weights, supply, demand, capacity));
    }
}
