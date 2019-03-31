package misc.graphs;

import datastructures.concrete.ArrayDisjointSet;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.ISet;
import misc.exceptions.NoPathExistsException;
import static misc.Searcher.topKSort;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated then usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've contrained Graph
    //   so that E *must* always be an instance of Edge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the Edge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.

    
    private IDictionary<V, IList<E>> adjacencyList;
    private IList<E> edges;
    private IList<V> vertices;
    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException  if any of the edges have a negative weight
     * @throws IllegalArgumentException  if one of the edges connects to a vertex not
     *                                   present in the 'vertices' list
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        for (E edge : edges) {
            if (edge.getWeight() < 0 || !vertices.contains(edge.getVertex1()) 
                    || !vertices.contains(edge.getVertex2())) {
                throw new IllegalArgumentException();
            }
        }
        
        this.edges = edges;
        this.vertices = vertices;
        adjacencyList = new ChainedHashDictionary<V, IList<E>>();
        for (V vertex : vertices) {
            adjacencyList.put(vertex, new DoubleLinkedList<E>());
            
        }
        for (E edge : edges) {
            adjacencyList.get(edge.getVertex1()).add(edge); 
            adjacencyList.get(edge.getVertex2()).add(edge); 
        }
        
        
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return vertices.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return edges.size();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        ISet<E> mst = new ChainedHashSet<E>();
        IDisjointSet<V> components = new ArrayDisjointSet<V>();
        for (V vertex : vertices) {
            components.makeSet(vertex);
        }
        IList<E> sortedEdges = topKSort(numEdges(), edges);
        for (E edge : sortedEdges) {
            V vertex1 = edge.getVertex1();
            V vertex2 = edge.getVertex2();
            if (components.findSet(vertex1) != components.findSet(vertex2)) {
                mst.add(edge);
                components.union(vertex1, vertex2);
                
            }
        }
        return mst;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        ISet<Vertex> visited = new ChainedHashSet<Vertex>();
        ISet<Vertex> unvisited = new ChainedHashSet<Vertex>();
        IPriorityQueue<Vertex> nextVertex = new ArrayHeap<Vertex>();
        IDictionary<V, Vertex> vDic = new ChainedHashDictionary<V, Vertex>();
        IList<E> path = new DoubleLinkedList<E>();
        //return empty list if source equals to target
        if (start.equals(end)) {
            return path;
        }
        //initialize vertices information
        for (V vertex : vertices) {
            Vertex vertexToAdd;
            if (vertex.equals(start)) {
                //add source to queue and set its dist to 0
                vertexToAdd = new Vertex(vertex, null, 0.0);
                nextVertex.insert(vertexToAdd);
            } else {
                //add them to unvisited and set their dist to infinity
                vertexToAdd = new Vertex(vertex, null, Double.POSITIVE_INFINITY);   
            }
            unvisited.add(vertexToAdd);
            vDic.put(vertex, vertexToAdd);
            //nextVertex.insert(vertexToAdd);
        }
        
        while (!unvisited.isEmpty() && !nextVertex.isEmpty()) {
            //get u's info from removing queue
            Vertex uInfo = nextVertex.removeMin();
            if (visited.contains(uInfo)) {
                continue;
            }
            //remove it from unvisited
            unvisited.remove(uInfo);
             
            //after processing u's neighbor, mark u to be visited
            visited.add(uInfo);  

            //for each neighbor of u
            for (E edge : adjacencyList.get(uInfo.getVertex())) {
                
                //get neighbor vertex and info
                V v = edge.getOtherVertex(uInfo.getVertex());
                Vertex vInfo = vDic.get(v);
                
                //if v is unvisited do
                if (unvisited.contains(vInfo)) {
                    Double weight = edge.getWeight();
                    Double uCost = uInfo.getCost();
                    Double vCost = vInfo.getCost();
                    if (uCost + weight < vCost) {
                        //update v's predecessor and cost and insert it into queue
                        vInfo.update(uCost + weight, edge);
                        nextVertex.insert(vInfo);
                    }    
                }    
            }  
        }
        //if there is the target, find its path
        if (visited.contains(vDic.get(end))) {
            path = findPath(path, start, vDic.get(end), vDic);
        } else {
            throw new NoPathExistsException();
        }
        return path;
    }
    
    private IList<E> findPath(IList<E> path, V start, Graph<V, E>.Vertex uInfo,
            IDictionary<V, Graph<V, E>.Vertex> vDic) {
        
        E edge = uInfo.getPredecessor();
        V u = uInfo.getVertex();
        if (edge == null) {
            throw new NoPathExistsException();
        }
        while (edge != null && !u.equals(start)) {
            path.insert(0, edge);
            Vertex parentInfo = vDic.get(edge.getOtherVertex(u));
            edge = parentInfo.getPredecessor();
            u = parentInfo.getVertex();
        }
        
        if (!u.equals(start)) {
            throw new NoPathExistsException();
        }
        return path;
    }

    private class Vertex implements Comparable<Vertex> {
        private V vertex;
        private E predecessor;
        private Double cost;
        public Vertex(V vertex, E predecessor, double d) {
            
            this.vertex = vertex;
            
            this.predecessor = predecessor;
            this.cost = d;
        }
        public E getPredecessor() {
            
            return this.predecessor;
        }
        public void update(double d, E edge) {
          
            this.cost = d;
 
            this.predecessor = edge;
            
        }
        public Double getCost() {
           
            return this.cost;
        }
        public V getVertex() {
            
            return this.vertex;
        }
        
        
        @Override
        public int compareTo(Graph<V, E>.Vertex o) {
            
            if (this.cost < o.getCost()) {
                return -1;
            } else if (this.cost > o.getCost()) {
                return 1;
            } else {
                return 0;
            }
            
        }

    }
    
}
