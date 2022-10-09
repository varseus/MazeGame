package mazegame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;


/**
 * This class represents the model, view, and controller for a maze
 * that solves itself using BFS, DFS, or a genetic algorithm.
 *
 * To do:
 * - Make model/view/controller separate classes
 * - Make BFS/DFS/Genetic Algorithm separate models that extend Algorithm interface
 */
public class Maze extends World {
  ArrayList<ArrayList<Node>> nodes;
  ArrayList<Edge> edges;
  Random random;
  HashMap<Posn, Node> cameFromNode;
  ICollection<Node> worklist;

  GeneticAlgorithm geneticAlgorithm;
  int generation;
  Posn player;

  int bfsSteps;
  boolean bfsSearching;
  int dfsSteps;

  // -------- Constructors --------//

  /**
   * Constructs this maze with the given width and height
   * @param width in blocks of the maze
   * @param height in blocks of the maze
   */
  public Maze(int width, int height) {
    this.constructMazeNodesAndEdges(width, height);
  }

  /**
   * Constructs this maze with pseudorandom for testing
   * @param width
   * @param height
   * @param random
   */
  public Maze(int width, int height, Random random) {
    this.random = random;
    this.constructMazeNodesAndEdges(width, height);
  }

  void constructMazeNodesAndEdges(int width, int height) {
    this.player = new Posn(0, 0);
    this.generation = 0;
    this.bfsSteps = 0;
    this.dfsSteps = 0;
    this.bfsSearching = false;
    this.cameFromNode = new HashMap<Posn, Node>();
    this.generateNodes(width, height);
    this.generateEdges();
    this.sortEdges();
    this.simplifyEdges();
    this.connectNodes();
  }

  /**
   * Resets pseudorandom for testing.
   */
  void resetRandom() {
    this.random = new Random(1);
  }

  // generate 2d array of nodes representing cells in the maze

  /**
   * Generate 2D array of nodes representing cells in the maze.
   * @param width the length of each column in the array
   * @param height the length of each row in the array
   */
  void generateNodes(int width, int height) {
    this.nodes = new ArrayList<ArrayList<Node>>();

    for (int i = 0; i < width; i++) {
      ArrayList<Node> row = new ArrayList<Node>();
      for (int j = 0; j < height; j++) {
        row.add(new Node(j, i));
      }
      this.nodes.add(row);
    }
  }

  /**
   * Generate edges with random weights between all nodes in this maze.
   */
  void generateEdges() {
    this.edges = new ArrayList<Edge>();

    for (int i = 0; i < this.nodes.size(); i++) {
      for (int j = 0; j < this.nodes.get(i).size(); j++) {
        if (i > 0) {
          this.edges.add(new Edge(this.nodes.get(i - 1).get(j), this.nodes.get(i).get(j),
                  Math.abs(this.random.nextInt())));
        }
        if (j > 0) {
          this.edges.add(new Edge(this.nodes.get(i).get(j - 1), this.nodes.get(i).get(j),
                  Math.abs(this.random.nextInt())));
        }
      }
    }
  }

  /**
   * Quicksort edges by weight.
   */
  void sortEdges() {
    (new Utils()).quicksort(this.edges, new EdgeComparator());
  }

  /**
   * Make a minimum spanning tree out of weighted edges.
   */
  void simplifyEdges() {
    Utils u = new Utils();
    ArrayList<Edge> edgesInTree = new ArrayList<Edge>();
    HashMap<Posn, Posn> representatives = new HashMap<Posn, Posn>();
    for (ArrayList<Node> row : this.nodes) {
      for (Node node : row) {
        representatives.put(node.posn, node.posn);
      }
    }

    while (!u.hasOneRoot(representatives, this.nodes)) {
      Edge nextEdge = this.edges.remove(0);

      if (u.getRoot(representatives, nextEdge.node1.posn) != u.getRoot(representatives,
              nextEdge.node2.posn)) {
        edgesInTree.add(nextEdge);
        representatives.put(u.getRoot(representatives, nextEdge.node2.posn),
                u.getRoot(representatives, nextEdge.node1.posn));
      }
    }

    this.edges = edgesInTree;
  }

  /**
   * Connect all nodes according to their edges.
   */
  void connectNodes() {
    for (Edge edge : this.edges) {
      edge.connectNodes();
    }
  }

  // --------------- View ----------------//

  /**
   * Draws this maze.
   * @return the WorldScene depicting this scene
   */
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(700, 700);

    // background and texts
    scene.placeImageXY(new RectangleImage(610, 610, OutlineMode.SOLID, Color.black), 350, 350);
    scene.placeImageXY(new RectangleImage(200, 30, OutlineMode.SOLID, Color.black), 350, 35);
    scene.placeImageXY(new TextImage("M  A  Z  E", 16, FontStyle.BOLD, Color.white), 350, 35);
    scene.placeImageXY(
            new TextImage("KEY:  'b' = BFS  |  'd' = DFS  |  'g' = Genetic  |'r' = Refresh", 12,
                    FontStyle.BOLD, Color.black),
            350, 665);

    // DFS and BFS step counters
    scene.placeImageXY(
            new TextImage("DFS steps: " + this.dfsSteps, 12, FontStyle.BOLD, Color.black), 100, 38);
    scene.placeImageXY(
            new TextImage("BFS steps: " + this.bfsSteps, 12, FontStyle.BOLD, Color.black), 600, 38);
    scene.placeImageXY(
            new TextImage("Genetic Generation: " + this.generation, 12, FontStyle.BOLD, Color.black), 350, 10);

    // draw maze walls
    for (ArrayList<Node> row : this.nodes) {
      for (Node node : row) {
        node.drawMazeOutline(scene, 600.0 / this.nodes.get(0).size(), 600.0 / this.nodes.size(),
                50);
      }
    }

    // hide walls between connected nodes
    for (Edge edge : this.edges) {
      edge.draw(scene, 600.0 / this.nodes.get(0).size(), 600.0 / this.nodes.size(), 50);
    }

    // draw maze fills
    for (ArrayList<Node> row : this.nodes) {
      for (Node node : row) {
        node.drawMazeFill(scene, 600.0 / this.nodes.get(0).size(), 600.0 / this.nodes.size(), 50);
      }
    }

    // mark starting and end locations
    this.nodes.get(0).get(0).drawMazeFill(scene, 600.0 / this.nodes.get(0).size(),
            600.0 / this.nodes.size(), 50, Color.blue);
    this.nodes.get(this.nodes.size() - 1).get(this.nodes.get(0).size() - 1).drawMazeFill(scene,
            600.0 / this.nodes.get(0).size(), 600.0 / this.nodes.size(), 50, Color.red);

    return scene;
  }

  // ----------------- Controller ------------------//

  /**
   * Handles key presses.
   * @param key do BSF if "b",
   *            do DSF if "d"
   *            do genetic algorithm of "g"
   *            reset if "r"
   */
  public void onKeyEvent(String key) {
    if (key.equals("b")) {
      this.resetNodes();
      this.geneticAlgorithm = null;
      this.worklist = new Queue<Node>();
      this.worklist.add(this.nodes.get(0).get(0));
      this.bfsSteps = 1;
      this.bfsSearching = true;
    }
    else if (key.equals("d")) {
      this.resetNodes();
      this.geneticAlgorithm = null;
      this.worklist = new Stack<Node>();
      this.worklist.add(this.nodes.get(0).get(0));
      this.dfsSteps = 1;
      this.bfsSearching = false;
    }
    else if (key.equals("g")) {
      this.worklist = null;
      this.geneticAlgorithm = new GeneticAlgorithm(50 * 50, 500, 0.5);
      this.generation = 1;
      this.highlightPath(this.geneticAlgorithm.fittestMember(this));
    }
    else if (key.equals("r")) {
      this.worklist = null;
      this.cameFromNode = new HashMap<Posn, Node>();
      this.bfsSteps = 0;
      this.dfsSteps = 0;
      this.generation = 1;
      this.generateNodes(this.nodes.get(0).size(), this.nodes.size());
      this.generateEdges();
      this.sortEdges();
      this.simplifyEdges();
      this.connectNodes();
    }
  }

  /**
   * Process the next node from the worklist do BFS/DFS or introduce the next generation of the genetic
   * algorithm.
   */
  public void onTick() {
    if (this.worklist != null && !this.worklist.isEmpty()) {
      this.processWorklist();
    }
    else if (this.geneticAlgorithm != null) {
      this.geneticAlgorithm.crossOver(this);
      this.highlightPath(this.geneticAlgorithm.fittestMember(this));
      this.generation++;
    }
  }

  /**
   * Set the nodes in this maze to its original state (no searches).
   */
  public void resetNodes() {
    this.cameFromNode = new HashMap<Posn, Node>();
    for (ArrayList<Node> row : this.nodes) {
      for (Node node : row) {
        node.isPath = false;
        node.isProcessed = false;
      }
    }
  }

  /**
   *  Mark the next Node in the worklist as processed
   *  and add its unprocessed outgoing nodes to the worklist
   *  and reconstruct the path if the final node in the maze is found.
   *  Assumes this worklist is not empty.
   */
  void processWorklist() {
    Node next = this.worklist.remove();
    if (this.bfsSearching) {
      this.bfsSteps++;
    }
    else {
      this.dfsSteps++;
    }

    if (next.process(this.nodes.get(this.nodes.size() - 1).get(this.nodes.get(0).size() - 1))) {
      this.worklist = null;
      this.reconstruct(next);
    }
    else {
      for (Node node : next.outgoingNodes) {
        if (!node.isProcessed) {
          this.worklist.add(node);
          this.cameFromNode.put(node.posn, next);
        }
      }

    }
  }

  /**
   * Change the color of all nodes in this path needed to reach the end.
   * @param endNode the node for which the destination in the maze was reached
   */
  void reconstruct(Node endNode) {
    endNode.isPath = true;

    if (this.cameFromNode.containsKey(endNode.posn)) {
      Node next = this.cameFromNode.get(endNode.posn);
      if (!next.isPath) {
        this.reconstruct(next);
      }
    }
  }

  /**
   * Set a Member's location in the maze back to zero.
   */
  void resetPlayer() {
    this.player = new Posn(0, 0);
  }

  /**
   * Move the member in the maze according to its chromosome's instructions.
   * @param direction the gene, indicating direction to move the member
   * @return whether or not the move was successful
   */
  boolean movePlayer(Posn direction) {
    Posn newPosn = new Posn(this.player.x + direction.x, this.player.y + direction.y);

    if (newPosn.x >= 0 && newPosn.x < this.nodes.size() && newPosn.y >= 0
            && newPosn.y < this.nodes.get(0).size()) {
      if (this.nodes.get(this.player.y).get(this.player.x).outgoingNodes
              .contains(this.nodes.get(newPosn.y).get(newPosn.x))) {
        this.player = newPosn;
        return true;
      }
    }
    return false;
  }

  /**
   * Get the current member's distance from the end.
   * @return double representing distance from the end of the maze
   */
  double getPlayerScore() {
    return Math.sqrt(Math.pow((this.nodes.get(0).size() - 1) - player.x, 2)
            + Math.pow((this.nodes.size() - 1) - player.y, 2));
  }

  /**
   * Get the current member's distance from the beginning.
   * @return double representing distance from the beginning of the maze
   */  double getPlayerDistanceTravelledScore() {
    return Math.sqrt(Math.pow(player.x, 2) + Math.pow(player.y, 2));
  }

  /**
   * Highlight the path taken by a given member.
   */
  public void highlightPath(Member member) {
    this.resetPath();
    this.resetPlayer();

    for (Integer gene : member.chromosome) {
      Posn direction = new Posn(0, 0);
      if (gene == 0) {
      }
      else if (gene == 1) {
        direction = new Posn(0, 1);
      }
      else if (gene == 2) {
        direction = new Posn(0, -1);
      }
      else if (gene == 3) {
        direction = new Posn(-1, 0);
      }
      else if (gene == 4) {
        direction = new Posn(1, 0);
      }

      Posn newPosn = new Posn(this.player.x + direction.x, this.player.y + direction.y);

      if (newPosn.x >= 0 && newPosn.x < this.nodes.size() && newPosn.y >= 0
              && newPosn.y < this.nodes.get(0).size()) {
        if (this.nodes.get(this.player.y).get(this.player.x).outgoingNodes
                .contains(this.nodes.get(newPosn.y).get(newPosn.x))) {
          this.player = newPosn;
          this.nodes.get(this.player.y).get(this.player.x).isProcessed = true;
        }
      }
    }
  }

  /**
   * Resets all node colors but does not reset the came from
   * @dev REPLACE THIS WITH RESETNODES
   */
  public void resetPath() {
    for (ArrayList<Node> row : this.nodes) {
      for (Node node : row) {
        node.isPath = false;
        node.isProcessed = false;
      }
    }
  }
}
