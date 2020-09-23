package test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
//import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.MyLinkedList;

public class MyLinkedListTest {

	// private MyLinkedList<? extends Object> myLLInstance;
	private MyLinkedList<Integer> myLLInstance;

	@BeforeEach
	private void setup() {
		myLLInstance = new MyLinkedList<Integer>();
		myLLInstance.add(1);
		myLLInstance.add(2);
	}

	@AfterEach
	private void clear() {
		myLLInstance.clear();
	}

	@Test
	public void getFirstElement() {
		assertEquals(Integer.valueOf(1), myLLInstance.getFirst());
	}

	@Test
	public void getLastElement() {
		assertEquals(Integer.valueOf(2), myLLInstance.getLast());
	}
	
	@Test
	public void setOnIndexElement() {
		assertEquals(2, (int) myLLInstance.set(1, 1));
		assertThrows(IndexOutOfBoundsException.class, () -> myLLInstance.get(2));
	}

	@Test
	public void getElementOnIndex() {
		assertEquals(Integer.valueOf(1), myLLInstance.get(0));
		assertEquals(Integer.valueOf(2), myLLInstance.get(1));
		assertThrows(IndexOutOfBoundsException.class, () -> myLLInstance.get(2));
	}
	
	@Test
	public void addOnIndexElement() {
		myLLInstance.add(1, 3); // [1] <-> [2] => [1] <-> [3] <-> [2]

		assertEquals(Integer.valueOf(3), myLLInstance.get(1));
	}

	@Test
	public void addAllElements() {
		Collection<Integer> c = new LinkedList<Integer>();
		c.addAll(Arrays.asList(1, 2, 3));
		myLLInstance.addAll(c);
		// [1] <-> [2] + addAll() [1] <-> [2] <-> [3] ==> [1] <-> [2] <-> [1] <-> [2]
		// <-> [3]

		assertEquals(5, myLLInstance.size());
	}
	
	@Test
	public void addAllЕlementsOnIndex() {
		Collection<Integer> c = new LinkedList<Integer>();
		c.addAll(Arrays.asList(1, 2, 3));
		myLLInstance.addAll(1, c);
		// [1] <-> [2] + addAll(1,c) [1] <-> [2] <-> [3] ==> [1] <-> [1] <-> [2] <-> [3]
		// <-> [2]

		assertEquals(5, myLLInstance.size());
		assertEquals(Integer.valueOf(1), myLLInstance.get(1));
	}
	
	@Test
	public void removeFirst() {
		assertEquals(2, myLLInstance.size());
		assertEquals(1, myLLInstance.removeFirst());
		assertEquals(1, myLLInstance.size());
	}
	
	@Test
	public void removeLast() {
		assertEquals(2, myLLInstance.size());
		assertEquals(2, myLLInstance.removeLast());
		assertEquals(1, myLLInstance.size());
	}
	
	@Test
	public void removeObject() {
		Collection<Integer> c = new LinkedList<Integer>();
		c.addAll(Arrays.asList(1, 2, 3));
		myLLInstance.addAll(c);
		// [1] <-> [2] + addAll() [1] <-> [2] <-> [3] ==> 
		// [1] <-> [2] <-> [1] <-> [2] <-> [3]
		assertEquals(true, myLLInstance.remove(Integer.valueOf(3)));
		
	}
	
	@Test
	public void removeElementOnIndex() {
		assertEquals(1, myLLInstance.remove(1));
	}
	
	@Test
	public void removeAllElementsFromList() {
		Collection<Integer> c = new LinkedList<Integer>();
		c.addAll(Arrays.asList(1, 2, 3, 4, 5));
		myLLInstance.addAll(c);
		// [1] <-> [2] + addAll() [1] <-> [2] <-> [3] <-> [4] <-> [5] ==> 
		// [1] <-> [2] <-> [1] <-> [2] <-> [3] <-> [4] <-> [5]
		myLLInstance.removeAll(Arrays.asList(Integer.valueOf(1),Integer.valueOf(3),Integer.valueOf(4)));
		assertArrayEquals(new Integer[] {2,2,5}, myLLInstance.toArray(new Integer[myLLInstance.size()]), "assertArrayEquals");
		
	}
	
	//removeAll
	//indexOf
	//lastIndexOf
	//contains
	//containsAll
	//sublist
	//retainAll
	//isEmpty
	//size
	//clear
	//toArray
	//toArray(T[]
	//iterator
	//listIterator(int index)
	//listIterator

}
