package mazegame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import javalib.worldimages.Posn;

/**
 * This class bundles useful methods for ArrayLists.
 */
class Utils {
  /**
   * Sorts the given ArrayList according to the given comparator.
   * @param arr array to sort
   * @param comp comparator to sort the array by
   * @param <T> type of each element in the array
   */
  <T> void quicksort(ArrayList<T> arr, Comparator<T> comp) {
    quicksortHelp(arr, comp, 0, arr.size());
  }

  // sorts the source array according to comp, in the range of indices [loIdx,
  // hiIdx)

  /**
   * Sorts the given array according to the given comparator, within the
   * given range of indices.
   * @param source array to sort
   * @param comp comparator to sort the array by
   * @param loIdx lower index to sort from
   * @param hiIdx upper index to sort from
   *              (uninclusive)
   * @param <T>
   */
  <T> void quicksortHelp(ArrayList<T> source, Comparator<T> comp, int loIdx, int hiIdx) {
    // Step 0: check for completion
    if (loIdx >= hiIdx) {
      return; // There are no items to sort
    }
    // Step 1: select pivot
    T pivot = source.get(loIdx);
    // Step 2: partition items to lower or upper portions of the temp list
    int pivotIdx = partition(source, comp, loIdx, hiIdx, pivot);
    // Step 3: sort both halves of the list
    quicksortHelp(source, comp, loIdx, pivotIdx);
    quicksortHelp(source, comp, pivotIdx + 1, hiIdx);
  }

  /**
   * Modifies the source list in the range [loIdx, hiIdx) such that
   * all values to the left of the pivot are less than (or equal to) the pivot
   * and all values to the right of the pivot are greater than it.
   * @param source array to be sorted
   * @param comp comparator to sort the array by
   * @param loIdx lower index to sort from
   * @param hiIdx upper index to sort from
   *              (uninclusive)
   * @param pivot item to pivot around
   * @return the index of the pivot
   * @param <T> the type of each element in the list
   */
  <T> int partition(ArrayList<T> source, Comparator<T> comp, int loIdx, int hiIdx, T pivot) {
    int curLo = loIdx;
    int curHi = hiIdx - 1;
    while (curLo < curHi) {

      while (curLo < hiIdx && comp.compare(source.get(curLo), pivot) <= 0) {
        curLo = curLo + 1;
      }

      while (curHi >= loIdx && comp.compare(source.get(curHi), pivot) > 0) {
        curHi = curHi - 1;
      }
      if (curLo < curHi) {
        swap(source, curLo, curHi);
      }
    }

    swap(source, loIdx, curHi); // place the pivot in the remaining spot
    return curHi;
  }

  /**
   * Swaps the elements in two given indices in a given ArrayList.
   * @param source the list
   * @param index1 first index to swap
   * @param index2 second index to swap
   * @param <T> the type of each element in the list
   */
  <T> void swap(ArrayList<T> source, int index1, int index2) {
    T t = source.get(index1);
    source.set(index1, source.get(index2));
    source.set(index2, t);
  }

  /**
   * Retrieves the root representative of a key
   * @param representatives a hashmap of representatives (a tree)
   * @param key  the key for which the root is sought in the hashmap
   *             (the node in the tree for which the root is being sought)
   * @return the root element
   * @param <T> the type of objects in the hashmap
   */
  <T> T getRoot(HashMap<T, T> representatives, T key) {
    T value = representatives.get(key);

    if (value == key) {
      return value;
    }
    else {
      return this.getRoot(representatives, value);
    }
  }

  /**
   * Determines whether a given representative hashmap is one tree.
   * @param representatives representatives hashmap (a tree)
   * @param nodes all nodes in the hashmap
   * @return whether the hasmap is one tree
   */
  boolean hasOneRoot(HashMap<Posn, Posn> representatives, ArrayList<ArrayList<Node>> nodes) {
    Posn previousVal = this.getRoot(representatives, nodes.get(0).get(0).posn);

    for (ArrayList<Node> row : nodes) {
      for (Node node : row) {
        if (previousVal != this.getRoot(representatives, node.posn)) {
          return false;
        }
      }
    }

    return true;
  }
}

/**
 * Comparator to compare edge weights.
 *
 * To do:
 * - Refactor comparator classes to be a subclasses for edge
 */
class EdgeComparator implements Comparator<Edge> {
  public int compare(Edge e1, Edge e2) {
    return e1.weight - e2.weight;
  }
}

/**
 * Comparator to compare member fitness in a population.
 *
 * To do:
 * - Refactor comparator classes to be a subclasses for edge
 */
class MemberComparator implements Comparator<Member> {
  Maze maze;

  MemberComparator(Maze maze) {
    this.maze = maze;
  }

  public int compare(Member m1, Member m2) {
    if (m1.fitness(this.maze) > m2.fitness(this.maze)) {
      return 1;
    }
    else if (m1.fitness(this.maze) < m2.fitness(this.maze)) {
      return -1;
    }
    else {
      return 0;
    }
  }
}
