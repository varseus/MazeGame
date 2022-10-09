package mazegame;

import java.awt.Color;
import java.util.ArrayList;

import javalib.impworld.WorldScene;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;

/**
 * This class represents a node connecting two edges in a graph.
 */
class Node {
  Posn posn;
  boolean isProcessed;
  boolean isPath;
  ArrayList<Node> outgoingNodes;

  /**
   * Constructs this node at the given location.
   * @param x
   * @param y
   */
  Node(int x, int y) {
    this.posn = new Posn(x, y);
    this.isProcessed = false;
    this.isPath = false;
    this.outgoingNodes = new ArrayList<Node>();
  }

  /**
   * Draws an outline of this node as a rectangle.
   * @param scene to draw this node on
   * @param width in pixels
   * @param height in pixels
   * @param offset in pixels from top left of scene
   */
  void drawMazeOutline(WorldScene scene, double width, double height, int offset) {
    scene.placeImageXY(
            new RectangleImage((int) width, (int) height, OutlineMode.SOLID, Color.black),
            (int) (offset + this.posn.x * width + width / 2),
            (int) (offset + this.posn.y * height + height / 2));
  }

  /**
   * Draws this node. White if unprocessed by BFS/DFS, colorful if processed, and grey if it is part
   * of the final path.
   * @param scene to draw this node on
   * @param width in pixels
   * @param height in pixels
   * @param offset in pixels from top left of scene
   */
  void drawMazeFill(WorldScene scene, double width, double height, int offset) {
    Color fillColor;
    if (this.isPath) {
      fillColor = Color.DARK_GRAY.darker().darker();
    }
    else if (this.isProcessed) {
      fillColor = new Color(this.computeBlueness(this.posn.x, this.posn.y, width, height), 0,
              255 - this.computeBlueness(this.posn.x, this.posn.y, width, height));
    }
    else {
      fillColor = Color.white;
    }

    drawMazeFill(scene, width, height, offset, fillColor);
  }

  /**
   * Draw this node filled with a specific color onto a scene.
   * @param scene to draw this node on.
   * @param width in pixels
   * @param height in pixels
   * @param offset in pixels from top left of scene
   * @param color
   */
  void drawMazeFill(WorldScene scene, double width, double height, int offset, Color color) {
    scene.placeImageXY(
            new RectangleImage((int) (width * 0.8), (int) (height * 0.8), OutlineMode.SOLID, color),
            (int) (offset + this.posn.x * width + width / 2),
            (int) (offset + this.posn.y * height + height / 2));
  }

  /**
   * Find the blue value for the given position's RGB gradient.
   * @param x
   * @param y
   * @param width
   * @param height
   * @return the blue value
   */
  int computeBlueness(double x, double y, double width, double height) {
    return (int) (Math.sqrt(
            Math.pow(x * width / 600 / Math.sqrt(2), 2) + Math.pow(y * height / 600 / Math.sqrt(2), 2))
            * 250);
  }

  /**
   * Process this node and determine whether it is the target
   * destination in the maze.
   * @param targetNode
   * @return whether this node is the target node
   */
  boolean process(Node targetNode) {
    this.isProcessed = true;
    return this == targetNode;
  }

  // add the given node to this node's list of outgoingNodes

  /**
   * Add the given node to this node's list of outgoingNodes.
   * @param outgoingNode to connect to this node
   */
  void connectToNode(Node outgoingNode) {
    this.outgoingNodes.add(outgoingNode);
  }
}