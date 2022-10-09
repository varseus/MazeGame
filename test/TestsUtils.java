import org.junit.Before;

import mazegame.*;

import static org.junit.Assert.assertEquals;


// Tests for Utils classes.
// Most methods are not runnable due to limited visibility of the mazegame classes.
// TO DO: update all methods to run given limited visibility (after implementing MVC).
public class TestsUtils {
  ArrayList<Edge> edges;
  ArrayList<Edge> emptyEdges;
  ArrayList<Edge> edges1;
  ArrayList<Edge> edges1Test;
  ArrayList<Edge> edgesSorted;
  ArrayList<Edge> edgesSwapped;
  ArrayList<Edge> edgesPartitioned;

  Edge edge1;

  @Before
  void initData() {
    edge1 = new Edge(null, null, 1);

    this.emptyEdges = new ArrayList<Edge>();

    this.edges1 = new ArrayList<Edge>();
    this.edges1.add(this.edge1);

    this.edges1Test = new ArrayList<Edge>();
    this.edges1Test.add(this.edge1);

    this.edges = new ArrayList<Edge>();
    this.edges.add(new Edge(null, null, 10));
    this.edges.add(new Edge(null, null, 5));
    this.edges.add(new Edge(null, null, 2));
    this.edges.add(this.edge1);
    this.edges.add(new Edge(null, null, 3));
    this.edges.add(new Edge(null, null, 6));
    this.edges.add(new Edge(null, null, 7));
    this.edges.add(new Edge(null, null, 4));
    this.edges.add(new Edge(null, null, 9));
    this.edges.add(new Edge(null, null, 8));

    this.edgesSwapped = new ArrayList<Edge>();
    this.edgesSwapped.add(new Edge(null, null, 8));
    this.edgesSwapped.add(new Edge(null, null, 5));
    this.edgesSwapped.add(new Edge(null, null, 2));
    this.edgesSwapped.add(this.edge1);
    this.edgesSwapped.add(new Edge(null, null, 3));
    this.edgesSwapped.add(new Edge(null, null, 6));
    this.edgesSwapped.add(new Edge(null, null, 7));
    this.edgesSwapped.add(new Edge(null, null, 4));
    this.edgesSwapped.add(new Edge(null, null, 9));
    this.edgesSwapped.add(new Edge(null, null, 10));

    this.edgesSorted = new ArrayList<Edge>();
    this.edgesSorted.add(this.edge1);
    this.edgesSorted.add(new Edge(null, null, 2));
    this.edgesSorted.add(new Edge(null, null, 3));
    this.edgesSorted.add(new Edge(null, null, 4));
    this.edgesSorted.add(new Edge(null, null, 5));
    this.edgesSorted.add(new Edge(null, null, 6));
    this.edgesSorted.add(new Edge(null, null, 7));
    this.edgesSorted.add(new Edge(null, null, 8));
    this.edgesSorted.add(new Edge(null, null, 9));
    this.edgesSorted.add(new Edge(null, null, 10));

    this.edgesPartitioned = new ArrayList<Edge>();
    this.edgesPartitioned.add(this.edge1);
    this.edgesPartitioned.add(new Edge(null, null, 5));
    this.edgesPartitioned.add(new Edge(null, null, 2));
    this.edgesPartitioned.add(new Edge(null, null, 10));
    this.edgesPartitioned.add(new Edge(null, null, 3));
    this.edgesPartitioned.add(new Edge(null, null, 6));
    this.edgesPartitioned.add(new Edge(null, null, 7));
    this.edgesPartitioned.add(new Edge(null, null, 4));
    this.edgesPartitioned.add(new Edge(null, null, 9));
    this.edgesPartitioned.add(new Edge(null, null, 8));
  }

  // test the quicksort method of Utils and its helpers
  @Test
  void testQuicksort() {
    this.initData();
    Utils u = new Utils();

    u.quicksort(this.edges, new EdgeComparator());
    assertEquals(this.edges, this.edgesSorted);

    u.quicksort(this.edges1, new EdgeComparator());
    assertEquals(this.edges1, this.edges1Test);

    u.quicksort(this.emptyEdges, new EdgeComparator());
    assertEquals(this.emptyEdges, new ArrayList<Edge>());

    this.initData();

    u.quicksortHelp(this.edges, new EdgeComparator(), 0, this.edges.size());
    assertEquals(this.edges, this.edgesSorted);

    u.quicksortHelp(this.edges1, new EdgeComparator(), 0, 1);
    assertEquals(this.edges1, this.edges1Test);

    u.quicksortHelp(this.emptyEdges, new EdgeComparator(), 0, 0);
    assertEquals(this.emptyEdges, new ArrayList<Edge>());

    this.initData();

    u.partition(this.edges, new EdgeComparator(), 0, this.edges.size(), this.edge1);
    assertEquals(this.edges, this.edgesPartitioned);

    u.partition(this.edges1, new EdgeComparator(), 0, 1, this.edge1);
    assertEquals(this.edges1, this.edges1Test);

    this.initData();
    u.swap(this.edges1, 0, 0);
    assertEquals(this.edges1, this.edges1Test);

    u.swap(this.edges, 0, 9);
    assertEquals(this.edges, this.edgesSwapped);
  }

  // test the getRoot method of Utils
  @Test
  void testGetRoot() {
    Utils u = new Utils();
    HashMap<Integer, Integer> testMap = new HashMap<Integer, Integer>();

    testMap.put(1, 2);
    testMap.put(2, 3);
    testMap.put(3, 4);
    testMap.put(4, 4);
    testMap.put(5, 5);

    assertEquals(u.getRoot(testMap, 1), 4);
    assertEquals(u.getRoot(testMap, 2), 4);
    assertEquals(u.getRoot(testMap, 4), 4);
    assertEquals(u.getRoot(testMap, 5), 5);
  }

  // test the hasOneRoot method of Utils
  @Test
  void testHasOneRoot() {
    Utils u = new Utils();
    HashMap<Posn, Posn> testMap = new HashMap<Posn, Posn>();

    testMap.put(new Posn(1, 1), new Posn(2, 2));
    testMap.put(new Posn(2, 2), new Posn(3, 3));
    testMap.put(new Posn(3, 3), new Posn(4, 4));
    testMap.put(new Posn(4, 4), new Posn(4, 4));
    testMap.put(new Posn(5, 5), new Posn(5, 5));

    ArrayList<ArrayList<Node>> listOfPosns = new ArrayList<ArrayList<Node>>();
    listOfPosns.add(new ArrayList<Node>());
    listOfPosns.get(0).add(new Node(1, 1));
    listOfPosns.get(0).add(new Node(2, 2));
    listOfPosns.get(0).add(new Node(3, 3));
    listOfPosns.get(0).add(new Node(4, 4));
    listOfPosns.get(0).add(new Node(5, 5));

    assertEquals(u.hasOneRoot(testMap, listOfPosns), false);

    testMap.put(new Posn(4, 4), new Posn(5, 5));

    assertEquals(u.hasOneRoot(testMap, listOfPosns), true);
  }
}