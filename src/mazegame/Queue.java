package mazegame;

import java.util.ArrayList;

/**
 * This class represents a queue (first in first out).
 * @param <T> the element type of this list
 */
public class Queue<T> implements ICollection<T> {
  ArrayList<T> contents;

  /**
   * Constructs this queue as empty.
   */
  public Queue() {
    this.contents = new ArrayList<T>();
  }

  /**
   * Determines if this queue is empty.
   * @return is this queue empty?
   */
  public boolean isEmpty() {
    return this.contents.isEmpty();
  }

  /**
   * Removes and returns the first item of the queue.
   * @return the first item of the queue.
   */
  public T remove() {
    if (this.isEmpty()) {
      return null;
    }
    return this.contents.remove(0);
  }

  /**
   * Adds an item to the end of the queue.
   * @param item to add to this collection
   */
  public void add(T item) {
    this.contents.add(item);
  }
}