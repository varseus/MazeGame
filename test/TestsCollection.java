import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import mazegame.*;

/**
 * Tests functionality of the collection interface.
 */
public class TestsCollection {
  Stack<Integer> stack1;
  Stack<Integer> stack2;
  Queue<Integer> queue1;
  Queue<Integer> queue2;

  @Before
  public void initData() {
    this.stack1 = new Stack<Integer>();
    this.stack2 = new Stack<Integer>();
    this.stack2.add(1);
    this.stack2.add(2);
    this.stack2.add(3);
    this.queue1 = new Queue<Integer>();
    this.queue2 = new Queue<Integer>();
    this.queue2.add(1);
    this.queue2.add(2);
    this.queue2.add(3);
  }

  // test the isEmpty method
  @Test
  public void testIsEmpty() {
    assertEquals(this.stack1.isEmpty(), true);
    assertEquals(this.stack2.isEmpty(), false);
    assertEquals(this.queue1.isEmpty(), true);
    assertEquals(this.queue2.isEmpty(), false);
  }

  // test the add and remove methods
  @Test
  public void testAdd() {
    this.stack1.add(1);
    this.stack1.add(2);
    this.stack1.add(3);

    assertEquals((int) this.stack1.remove(), 3);
    assertEquals((int) this.stack1.remove(), 2);
    assertEquals((int) this.stack1.remove(), 1);
    assertEquals((int) this.stack1.remove(), null);

    this.queue1.add(1);
    this.queue1.add(2);
    this.queue1.add(3);

    assertEquals((int) this.queue1.remove(), 1);
    assertEquals((int) this.queue1.remove(), 2);
    assertEquals((int) this.queue1.remove(), 3);
    assertEquals((int) this.queue1.remove(), null);
  }
}