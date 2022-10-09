package mazegame;

import java.util.ArrayList;

/**
 * This class represents a stack (last in first out).
 * @param <T> the type of each element in the stack
 */
public class Stack<T> implements ICollection<T> {
  ArrayList<T> contents;

  /**
   * Constructs this stack as empty.
   */
  public Stack() {
    this.contents = new ArrayList<T>();
  }

  /**
   * Determines if this stack is empty.
   * @return is this stack empty?
   */
  public boolean isEmpty() {
    return this.contents.isEmpty();
  }

  /**
   * Removes and returns the first item of this stack.
   * @return the first item of this stack or null if empty
   */
  public T remove() {
    if (this.isEmpty()) {
      return null;
    }
    return this.contents.remove(0);
  }

  /**
   * Adds the given item to the front of the stack.
   * @param item to add to this stack
   */
  public void add(T item) {
    this.contents.add(0, item);
  }
}
