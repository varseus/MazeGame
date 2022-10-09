package mazegame;

import java.util.ArrayList;
import java.util.HashMap;

import javalib.worldimages.Posn;

/**
 * Represents a member of the population of a Genetic Algorithm.
 */
class Member {
  ArrayList<Integer> chromosome;
  Double fitness;

  /**
   * Constructs this Member with the given chromosome.
   * @param chromosome the Chromosome representing the way a
   *                   given member solves the Maze
   */
  Member(ArrayList<Integer> chromosome) {
    this.chromosome = chromosome;
    this.fitness = 0.0;
  }

  /**
   * Determines how successful this Member is at solving the maze.
   * Fitness is the sqrt of player score + sqr of antiplayerscore + penalties.
   * @param maze that the Member is solving
   * @return the fitness as a number, where lower numbers are more fit
   */
  double fitness(Maze maze) {
    double penalties = 0;
    maze.resetPlayer();

    HashMap<Posn, Boolean> visitedPosns = new HashMap<Posn, Boolean>();

    for (Integer gene : this.chromosome) {
      if (gene == 1 && !maze.movePlayer(new Posn(0, 1))) {
        penalties += .01;
      }
      else if (gene == 2 && !maze.movePlayer(new Posn(0, -1))) {
        penalties += .01;
      }
      else if (gene == 3 && !maze.movePlayer(new Posn(-1, 0))) {
        penalties += .01;
      }
      else if (gene == 4 && !maze.movePlayer(new Posn(1, 0))) {
        penalties += .01;
      }

      // make this more efficient:
      if (visitedPosns.get(maze.player) != null) {
        penalties += .05;
      }
      else {
        visitedPosns.put(maze.player, true);
      }

      if (maze.player.x == maze.nodes.get(0).size() - 1 && maze.player.y == maze.nodes.size() - 1) {
        penalties -= 10000;
        return 0;
      }
    }


    this.fitness = Math.sqrt(maze.getPlayerScore())
            - Math.sqrt(maze.getPlayerDistanceTravelledScore()) + penalties;
    return this.fitness;
  }
}