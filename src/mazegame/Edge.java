package mazegame;

import java.awt.Color;

import javalib.impworld.WorldScene;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;

/**
 * This class represents an edge between two adjacent nodes
 * representing a path between cells in a maz
 */
class Edge {
  Node node1;
  Node node2;
  int weight;

  /**
   * Instantiate this edge between two nodes
   * with a given weight.
   * @param node1 the first node
   * @param node2 the second node
   * @param weight the weight of the edge between the two nodes
   */
  public Edge(Node node1, Node node2, int weight) {
    this.node1 = node1;
    this.node2 = node2;
    this.weight = weight;
  }

  /**
   * Connects this edge's nodes
   * assumes that the nodes are not null.
   */
  void connectNodes() {
    this.node1.connectToNode(this.node2);
    this.node2.connectToNode(this.node1);
  }

  /**
   * Draw this edge onto a scene.
   * @param scene to draw this edge on
   * @param width of this edge
   * @param height of this edge
   * @param offset x/y offset from the top left corner of the scene
   */
  void draw(WorldScene scene, double width, double height, int offset) {
    double avgX = (this.node1.posn.x + this.node2.posn.x) / 2.0;
    double avgY = (this.node1.posn.y + this.node2.posn.y) / 2.0;

    Color fillColor;
    if (this.node1.isPath && this.node2.isPath) {
      fillColor = Color.DARK_GRAY.darker().darker();
    }
    else if (this.node1.isProcessed && this.node2.isProcessed) {
      fillColor = new Color(this.node1.computeBlueness(avgX, avgY, width, height), 0,
              255 - this.node1.computeBlueness(avgX, avgY, width, height));
    }
    else {
      fillColor = Color.white;
    }
    scene.placeImageXY(
            new RectangleImage((int) (width * 0.8), (int) (height * 0.8), OutlineMode.SOLID, fillColor),
            (int) (offset + avgX * width + width / 2), (int) (offset + avgY * height + height / 2));
  }
}