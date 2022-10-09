import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import mazegame.Maze;

/**
 * Tests functionality for the Maze class.
 * Most methods are not runnable due to limited visibility of the mazegame classes.
 *
 * To do:
 * - update all methods to run given limited visibility (after implementing MVC).
 */
public class TestsMaze {
    Random random = new Random(1);
  Maze testMaze = new Maze(10, 10, new Random(1));

  @Before
  void initData() {
    this.random = new Random(1);
    this.testMaze = new Maze(10, 10, new Random(1));
  }

  // test the generateNodes method of Maze
  @Test
  void testGenerateNodes() {
    this.testMaze.generateNodes(10, 10);
    assertEquals(this.testMaze.nodes.size(), 10);
    assertEquals(this.testMaze.nodes.get(0).size(), 10);
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        assertEquals(this.testMaze.nodes.get(i).get(j), new Node(j, i));
      }
    }
  }

  // test the generateEdges method of Maze
  @Test
  void testGenerateEdges() {
    
    this.testMaze.generateEdges();

    assertEquals(this.testMaze.edges.size(), 180);
    assertEquals(this.testMaze.edges.get(0).node1, this.testMaze.nodes.get(0).get(0));
    assertEquals(this.testMaze.edges.get(0).node2, this.testMaze.nodes.get(0).get(1));
  }

  // test the sortEdges method of Maze
  @Test
  void testSortEdges() {
    
    this.testMaze.generateEdges();
    this.testMaze.sortEdges();

    for (int i = 1; i < this.testMaze.edges.size(); i++) {
      assertEquals(this.testMaze.edges.get(i).weight >= this.testMaze.edges.get(i - 1).weight,
          true);
    }
  }

  // test the simplifyEdges method of Maze
  @Test
  void testSimplifyEdges() {
    
    
    this.testMaze.generateEdges();
    this.testMaze.sortEdges();
    this.testMaze.simplifyEdges();

    assertEquals(this.testMaze.edges.size() < 180, true);
  }

  // test the connectNodes method of Maze
  @Test
  void testConnectNodesMaze() {
    
    this.testMaze.generateNodes(10, 10);
    this.testMaze.generateEdges();
    this.testMaze.connectNodes();

    for (int i = 1; i < this.testMaze.nodes.get(0).size() - 1; i++) {
      for (int j = 1; j < this.testMaze.nodes.size() - 1; j++) {
        assertEquals(this.testMaze.nodes.get(j).get(i).outgoingNodes.size(), 4);
      }
    }
  }

  // test the onKeyEvent method of Maze
  @Test
  void testOnKeyEvent() {
    
    Maze testMaze2 = new Maze(10, 10, new Random(1));

    this.testMaze.onKeyEvent("");
    assertEquals(this.testMaze, testMaze2);

    this.testMaze.onKeyEvent("b");
    Queue<Node> testWorklist1 = new Queue<Node>();
    testWorklist1.add(this.testMaze.nodes.get(0).get(0));
    assertEquals(this.testMaze.worklist, testWorklist1);
    assertEquals(this.testMaze.bfsSteps, 1);

    this.testMaze.onKeyEvent("d");
    Stack<Node> testWorklist2 = new Stack<Node>();
    testWorklist2.add(this.testMaze.nodes.get(0).get(0));
    assertEquals(this.testMaze.worklist, testWorklist2);
    assertEquals(this.testMaze.dfsSteps, 1);

    this.testMaze.onKeyEvent("r");
    ICollection<Node> testWorklist3 = null;
    assertEquals(this.testMaze.worklist, testWorklist3);
    t.checkFail(this.testMaze, testMaze2);
    assertEquals(this.testMaze.dfsSteps, 0);
    assertEquals(this.testMaze.bfsSteps, 0);
  }


  // test the onTick method of Maze
  @Test
  void testOnTick() {
    
    Maze testMaze2 = new Maze(10, 10, new Random(1));

    this.testMaze.onTick();
    assertEquals(this.testMaze, testMaze2);

    this.testMaze.onKeyEvent("b");
    testMaze2.onKeyEvent("b");
    this.testMaze.onTick();
    testMaze2.processWorklist();
    assertEquals(this.testMaze, testMaze2);
  }


  // test the resetNodes method of Maze
  @Test
  void testResetNodes() {
    
    Maze testMaze2 = new Maze(10, 10, new Random(1));

    assertEquals(this.testMaze, testMaze2);

    this.testMaze.onKeyEvent("b");
    this.testMaze.onTick();
    this.testMaze.onTick();
    this.testMaze.onTick();
    t.checkFail(this.testMaze, testMaze2);

    this.testMaze.resetNodes();
    this.testMaze.bfsSearching = false;
    this.testMaze.bfsSteps = 0;
    this.testMaze.dfsSteps = 0;
    this.testMaze.worklist = null;
    assertEquals(this.testMaze, testMaze2);
  }

  // test the processWorklist method of Maze
  @Test
  void testProcessWorklist() {
    
    Maze testMaze2 = new Maze(10, 10, new Random(1));
    this.testMaze.generateNodes(10, 10);
    this.testMaze.generateEdges();
    this.testMaze.connectNodes();
    testMaze2.generateNodes(10, 10);
    testMaze2.generateEdges();
    testMaze2.connectNodes();
    this.testMaze.onKeyEvent("b");
    testMaze2.onKeyEvent("b");

    this.testMaze.processWorklist();

    Node next = testMaze2.worklist.remove();
    assertEquals(next.process(new Node(0, 0)), false);
    testMaze2.worklist.add(next.outgoingNodes.get(0));
    testMaze2.cameFromNode.put(next.outgoingNodes.get(0).posn, next);
    testMaze2.worklist.add(next.outgoingNodes.get(1));
    testMaze2.cameFromNode.put(next.outgoingNodes.get(1).posn, next);
    testMaze2.bfsSearching = true;
    testMaze2.bfsSteps = 2;

    assertEquals(this.testMaze, testMaze2);

    this.testMaze.resetNodes();
    testMaze2.resetNodes();
    this.testMaze.onKeyEvent("d");
    testMaze2.onKeyEvent("d");

    this.testMaze.processWorklist();

    next = testMaze2.worklist.remove();
    assertEquals(next.process(new Node(0, 0)), false);
    testMaze2.worklist.add(next.outgoingNodes.get(0));
    testMaze2.cameFromNode.put(next.outgoingNodes.get(0).posn, next);
    testMaze2.worklist.add(next.outgoingNodes.get(1));
    testMaze2.cameFromNode.put(next.outgoingNodes.get(1).posn, next);
    testMaze2.bfsSearching = false;
    testMaze2.dfsSteps = 2;

    assertEquals(this.testMaze, testMaze2);

    this.testMaze.resetNodes();
    testMaze2.resetNodes();
    this.testMaze.cameFromNode.put(this.testMaze.nodes.get(9).get(9).posn,
        this.testMaze.nodes.get(0).get(0));
    testMaze2.cameFromNode.put(testMaze2.nodes.get(9).get(9).posn, testMaze2.nodes.get(0).get(0));
    this.testMaze.worklist.add(this.testMaze.nodes.get(9).get(9));
    testMaze2.worklist.add(testMaze2.nodes.get(9).get(9));

    this.testMaze.processWorklist();

    next = testMaze2.worklist.remove();
    assertEquals(next.process(testMaze2.nodes.get(9).get(9)), true);
    testMaze2.worklist = null;
    testMaze2.nodes.get(9).get(9).isPath = true;
    testMaze2.nodes.get(0).get(0).isPath = true;
    testMaze2.dfsSteps = 3;

    assertEquals(this.testMaze, testMaze2);
  }

  // test the reconstruct method of Maze
  @Test
  void testReconstruct() {
    
    Maze testMaze2 = new Maze(10, 10, new Random(1));

    this.testMaze.cameFromNode.put(new Posn(4, 4), this.testMaze.nodes.get(0).get(0));
    this.testMaze.cameFromNode.put(new Posn(9, 9), this.testMaze.nodes.get(4).get(4));
    testMaze2.cameFromNode.put(new Posn(4, 4), testMaze2.nodes.get(0).get(0));
    testMaze2.cameFromNode.put(new Posn(9, 9), testMaze2.nodes.get(4).get(4));

    this.testMaze.reconstruct(this.testMaze.nodes.get(9).get(9));

    testMaze2.nodes.get(9).get(9).isPath = true;
    testMaze2.nodes.get(4).get(4).isPath = true;
    testMaze2.nodes.get(0).get(0).isPath = true;

    assertEquals(this.testMaze, testMaze2);
  }

  // test the makeScene method of Maze
  @Test
  void testMakeScene() {
    

    WorldScene scene = new WorldScene(700, 700);

    scene.placeImageXY(new RectangleImage(610, 610, OutlineMode.SOLID, Color.black), 350, 350);
    scene.placeImageXY(new RectangleImage(200, 30, OutlineMode.SOLID, Color.black), 350, 35);
    scene.placeImageXY(new TextImage("M  A  Z  E", 16, FontStyle.BOLD, Color.white), 350, 35);
    scene.placeImageXY(new TextImage("KEY:  'b' = BFS  |  'd' = DFS  |  'r' = Refresh", 12,
        FontStyle.BOLD, Color.black), 350, 665);
    scene.placeImageXY(
        new TextImage("DFS steps: " + this.testMaze.dfsSteps, 12, FontStyle.BOLD, Color.black), 100,
        38);
    scene.placeImageXY(
        new TextImage("BFS steps: " + this.testMaze.bfsSteps, 12, FontStyle.BOLD, Color.black), 600,
        38);

    for (ArrayList<Node> row : this.testMaze.nodes) {
      for (Node node : row) {
        node.drawMazeOutline(scene, 600.0 / this.testMaze.nodes.get(0).size(),
            600.0 / this.testMaze.nodes.size(), 50);
      }
    }

    for (Edge edge : this.testMaze.edges) {
      edge.draw(scene, 600.0 / this.testMaze.nodes.get(0).size(),
          600.0 / this.testMaze.nodes.size(), 50);
    }

    for (ArrayList<Node> row : this.testMaze.nodes) {
      for (Node node : row) {
        node.drawMazeFill(scene, 600.0 / this.testMaze.nodes.get(0).size(),
            600.0 / this.testMaze.nodes.size(), 50);
      }
    }

    this.testMaze.nodes.get(0).get(0).drawMazeFill(scene, 600.0 / this.testMaze.nodes.get(0).size(),
        600.0 / this.testMaze.nodes.size(), 50, Color.blue);
    this.testMaze.nodes.get(this.testMaze.nodes.size() - 1)
        .get(this.testMaze.nodes.get(0).size() - 1).drawMazeFill(scene,
            600.0 / this.testMaze.nodes.get(0).size(), 600.0 / this.testMaze.nodes.size(), 50,
            Color.red);

    assertEquals(this.testMaze.makeScene(), scene);
  }

  // test the Draw methods of Node and Edge
  @Test
  void testDraw() {
    Node testNode = new Node(1, 1);
    Node testNode2 = new Node(2, 1);
    Edge testEdge = new Edge(testNode, testNode2, 1);

    WorldScene testScene = new WorldScene(700, 700);
    WorldScene scene = new WorldScene(700, 700);
    scene.placeImageXY(new RectangleImage((int) 10, (int) 10, OutlineMode.SOLID, Color.black),
        (int) (50 + 10 + 10 / 2), (int) (50 + 10 + 10 / 2));
    testNode.drawMazeOutline(testScene, 10, 10, 50);

    assertEquals(testScene, scene);

    testScene = new WorldScene(700, 700);
    scene = new WorldScene(700, 700);
    scene.placeImageXY(
        new RectangleImage((int) (10 * 0.8), (int) (10 * 0.8), OutlineMode.SOLID, Color.white),
        (int) (50 + 10 + 10 / 2), (int) (50 + 10 + 10 / 2));
    testNode.drawMazeFill(testScene, 10, 10, 50);

    assertEquals(testScene, scene);

    testNode.isPath = true;
    testScene = new WorldScene(700, 700);
    scene = new WorldScene(700, 700);
    scene.placeImageXY(new RectangleImage((int) (10 * 0.8), (int) (10 * 0.8), OutlineMode.SOLID,
        Color.DARK_GRAY.darker().darker()), (int) (50 + 10 + 10 / 2), (int) (50 + 10 + 10 / 2));
    testNode.drawMazeFill(testScene, 10, 10, 50);

    assertEquals(testScene, scene);

    testNode.isPath = false;
    testNode.isProcessed = true;
    testScene = new WorldScene(700, 700);
    scene = new WorldScene(700, 700);
    scene.placeImageXY(
        new RectangleImage((int) (10 * 0.8), (int) (10 * 0.8), OutlineMode.SOLID,
            new Color(testNode.computeBlueness(1, 1, 10, 10), 0,
                255 - testNode.computeBlueness(1, 1, 10, 10))),
        (int) (50 + 10 + 10 / 2), (int) (50 + 10 + 10 / 2));
    testNode.drawMazeFill(testScene, 10, 10, 50);

    assertEquals(testScene, scene);

    testScene = new WorldScene(700, 700);
    scene = new WorldScene(700, 700);
    scene.placeImageXY(
        new RectangleImage((int) (10 * 0.8), (int) (10 * 0.8), OutlineMode.SOLID, Color.red),
        (int) (50 + 10 + 10 / 2), (int) (50 + 10 + 10 / 2));
    testNode.drawMazeFill(testScene, 10, 10, 50, Color.red);

    assertEquals(testScene, scene);

    testScene = new WorldScene(700, 700);
    scene = new WorldScene(700, 700);
    scene.placeImageXY(
        new RectangleImage((int) (10 * 0.8), (int) (10 * 0.8), OutlineMode.SOLID, Color.white),
        (int) (50 + (3) / 2.0 * 10 + 10 / 2), (int) (50 + (2) / 2.0 * 10 + 10 / 2));
    testEdge.draw(testScene, 10, 10, 50);

    assertEquals(testScene, scene);

    testNode2.isProcessed = true;
    testScene = new WorldScene(700, 700);
    scene = new WorldScene(700, 700);
    scene.placeImageXY(
        new RectangleImage((int) (10 * 0.8), (int) (10 * 0.8), OutlineMode.SOLID,
            new Color(testNode.computeBlueness(3 / 2.0, 2 / 2.0, 10, 10), 0,
                255 - testNode.computeBlueness(3 / 2.0, 2 / 2.0, 10, 10))),
        (int) (50 + (3) / 2.0 * 10 + 10 / 2), (int) (50 + (2) / 2.0 * 10 + 10 / 2));
    testEdge.draw(testScene, 10, 10, 50);

    assertEquals(testScene, scene);

    testNode2.isPath = true;
    testNode.isPath = true;
    testScene = new WorldScene(700, 700);
    scene = new WorldScene(700, 700);
    scene.placeImageXY(
        new RectangleImage((int) (10 * 0.8), (int) (10 * 0.8), OutlineMode.SOLID,
            Color.DARK_GRAY.darker().darker()),
        (int) (50 + (3) / 2.0 * 10 + 10 / 2), (int) (50 + (2) / 2.0 * 10 + 10 / 2));
    testEdge.draw(testScene, 10, 10, 50);

    assertEquals(testScene, scene);
  }

  // test the computeBlueness method of Node
  @Test
  void testComputeBlueness() {
    Node node1 = new Node(0, 0);

    assertEquals(node1.computeBlueness(0, 0, 600, 600), 0);
    assertEquals(node1.computeBlueness(0, 9, 60, 60), 159);
    assertEquals(node1.computeBlueness(59, 59, 10, 10), 245);
  }

  // tests the process method of Node
  @Test
  void testProcess() {
    Node node1 = new Node(0, 0);
    Node node1Processed = new Node(0, 0);
    node1Processed.isProcessed = true;
    Node node2 = new Node(1, 1);
    Node node2Unprocessed = new Node(1, 1);
    Node node2Processed = new Node(1, 1);
    node2Processed.isProcessed = true;

    assertEquals(node1.process(node2), false);
    assertEquals(node1.process(node1), true);
    assertEquals(node1, node1Processed);
    assertEquals(node2, node2Unprocessed);
    assertEquals(node2.process(node1), false);
    assertEquals(node2.process(node2), true);
    assertEquals(node2, node2Processed);
    assertEquals(node1, node1Processed);
  }

  // test the connectToNode method of Node
  @Test
  void testConnectToNode() {
    Node node0 = new Node(0, 0);
    Node node1 = new Node(1, 0);
    Node node2 = new Node(2, 0);

    node0.connectToNode(node1);
    node1.connectToNode(node0);

    assertEquals(node0.outgoingNodes, new ArrayList<Node>(Arrays.asList(node1)));
    assertEquals(node1.outgoingNodes, new ArrayList<Node>(Arrays.asList(node0)));
    assertEquals(node2.outgoingNodes, new ArrayList<Node>());
  }

  // test the connectNodes method of Edge
  @Test
  void testConnectNodes() {
    Node node0 = new Node(0, 0);
    Node node1 = new Node(1, 0);
    Node node2 = new Node(2, 0);
    Edge edge1 = new Edge(node0, node1, 1);

    edge1.connectNodes();

    assertEquals(node0.outgoingNodes, new ArrayList<Node>(Arrays.asList(node1)));
    assertEquals(node1.outgoingNodes, new ArrayList<Node>(Arrays.asList(node0)));
    assertEquals(node2.outgoingNodes, new ArrayList<Node>());
  }

  @Test
  public void testBigBang() {
    (new Maze(50, 50)).bigBang(700, 700, 1 / 200.0);
  }
}