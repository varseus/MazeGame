package mazegame;

/**
 * This class represents a collection of items.
 * @param <T> the type of each element in the collection
 */
public interface ICollection<T> {

  /**
   * Is this collection empty?
   * @return whether this collection is empty
   */
  boolean isEmpty();

  /**
   * Adds the item to the collection
   * @param item to add to this collection
   */
  void add(T item);

  /**
   * Returns the first item of the collection and
   * removes that first item.
   * @return the first item of the collection
   */
  T remove();
}
