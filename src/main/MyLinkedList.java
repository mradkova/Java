package main;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements List<E> {

	private static class Node<E> {
		E item;
		Node<E> next;
		Node<E> prev;
	
		Node(Node<E> prev, E item, Node<E> next) {
			this.item = item;
			this.next = next;
			this.prev = prev;
		}
	}

	// from AbstractSequentialList<E>
	protected int modCount = 0;
	private int size = 0;
	Node<E> first;
	Node<E> last;

	private boolean checkIndex(int index) {
		if (index >= 0 && index <= size) {
			return true;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	public MyLinkedList() {
		// constructs an empty List
	}

	public MyLinkedList(Collection<E> c) {
		this();
		addAll(c);
	}

	private void linkFirst(E e) {
		final Node<E> f = first;
		Node<E> newNode = new Node<E>(null, e, f);
		first = newNode;
		if (f == null)
			last = newNode;
		else
			f.prev = newNode;
		size++;
		modCount++;
	}

	private void linkLast(E e) {
		final Node<E> l = last;
		Node<E> newNode = new Node<E>(l, e, null);
		last = newNode;
		if (l == null) {
			first = newNode;
		} else {
			l.next = newNode;
		}
		size++;
		modCount++;
	}

	private void linkBefore(E e, Node<E> succ) {
		// assert succ != null;
		final Node<E> pred = succ.prev;
		final Node<E> newNode = new Node<>(pred, e, succ);
		succ.prev = newNode;
		if (pred == null)
			first = newNode;
		else
			pred.next = newNode;
		size++;
		modCount++;
	}

	private E unlinkFirst(Node<E> f) {
		// assert f == first && f != null;
		final E element = f.item;
		final Node<E> next = f.next;
		f.item = null;
		f.next = null; // help GC
		first = next;
		if (next == null)
			last = null;
		else
			next.prev = null;
		size--;
		modCount++;
		return element;
	}

	private E unlinkLast(Node<E> l) {
		// assert l == last && l != null;
		final E element = l.item;
		final Node<E> prev = l.prev;
		l.item = null;
		l.prev = null; // help GC
		last = prev;
		if (prev == null)
			first = null;
		else
			prev.next = null;
		size--;
		modCount++;
		return element;
	}

	E unlink(Node<E> x) {
		final E element = x.item;
		final Node<E> next = x.next;
		final Node<E> prev = x.prev;

		if (prev == null) {
			first = next;
		} else {
			prev.next = next;
			x.prev = null;
		}

		if (next == null) {
			last = prev;
		} else {
			next.prev = prev;
			x.next = null;
		}

		x.item = null;
		size--;
		modCount++;
		return element;
	}

	Node<E> node(int index) {
		checkIndex(index);
		Node<E> result = first;
		for (int i = 0; i < index; i++) {
			result = result.next;
		}
		return result;
	}

	public E getFirst() {
		final Node<E> f = first;
		if (f == null) {
			throw new NoSuchElementException();
		}
		return f.item;
	}

	public E getLast() {
		if (last == null) {
			throw new NoSuchElementException();
		}
		return last.item;
	}

	@Override
	public E get(int index) {
		if (index >= 0 && index < size) {
			Node<E> result = first;
			for (int i = 0; i < index; i++) {
				result = result.next;
			}
			return result.item;
		}
		throw new IndexOutOfBoundsException();
	}

	@Override
	public E set(int index, E element) {
		checkIndex(index);
		Node<E> x = node(index);
		E oldValue = x.item;
		x.item = element;
		return oldValue;
	}

	public boolean add(E e) {
		linkLast(e);
		return true;
	}

	public void addFirst(E e) {
		linkFirst(e);
	}

	public void addLast(E e) {
		linkLast(e);
	}

	@Override
	public void add(int index, E element) {
		checkIndex(index);
		
		if(index == size)
			linkLast(element);
		else
			linkBefore(element, node(index));
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		Iterator<? extends E> iter = c.iterator();
		while(iter.hasNext()) {
			addLast(iter.next());
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		checkIndex(index);
		if(c.size() == 0)
			return false;
		
		Iterator<? extends E> iter = c.iterator();
		int insert_index = index;
		while(iter.hasNext()) {
			add(insert_index, iter.next());
			insert_index++;
		}
		return true;
	}

	public E removeFirst() {
		final Node<E> f = first;
		if (f == null)
			throw new NoSuchElementException();
		return unlinkFirst(f);
	}

	public E removeLast() {
		final Node<E> l = last;
		if (l == null)
			throw new NoSuchElementException();
		return unlinkLast(l);
	}

	@Override
	public boolean remove(Object o) {
		if (o == null) {
			for (Node<E> x = first; x != null; x = x.next) {
				if (x.item == null) {
					unlink(x);
					return true;
				}
			}
		} else {
			for (Node<E> x = first; x != null; x = x.next) {
				if (o.equals(x.item)) {
					unlink(x);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public E remove(int index) {
		checkIndex(index);
		return unlink(node(index));
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		Iterator<E> iter = (Iterator<E>) c.iterator();
		boolean flag = false;
		while(iter.hasNext()) {
			if(this.contains(iter.next())) {
				this.remove(iter.next());
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public int indexOf(Object o) {
		int index = 0;
		if (o == null) {
			for (Node<E> x = first; x != null; x = x.next) {
				if (x.item == null)
					return index;
				index++;
			}
		} else {
			for (Node<E> x = first; x != null; x = x.next) {
				if (o.equals(x.item))
					return index;
				index++;
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		int index = size;
		if (o == null) {
			for (Node<E> x = last; x != null; x = x.prev) {
				index--;
				if (x.item == null)
					return index;
			}
		} else {
			for (Node<E> x = last; x != null; x = x.prev) {
				index--;
				if (o.equals(x.item))
					return index;
			}
		}
		return -1;
	}

	@Override
	public boolean contains(Object o) {
		if (o == null) {
			for (Node<E> x = first; x != null; x = x.next) {
				if (x.item == null) {
					return true;
				}
			}
		} else {
			for (Node<E> x = first; x != null; x = x.next) {
				if (x.item.equals(o))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for( Object o : c ) {
			if(indexOf(o) < 0) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		checkIndex(fromIndex);
		checkIndex(toIndex);
		if(fromIndex > toIndex) {
			throw new IndexOutOfBoundsException();
		}
		
		MyLinkedList<E> result = new MyLinkedList<E>();
		Iterator<E> iter = listIterator(fromIndex);
		for(int i = 0; i < (toIndex - fromIndex); i++) {
			result.add(iter.next());
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		Iterator<E> iter = iterator();
		boolean flag = false;
		while(iter.hasNext()) {
			if(! c.contains(iter.next())) {
				remove(iter.next());
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public void clear() {
		for (Node<E> x = first; x != null;) {
			Node<E> next = x.next;
			x.item = null;
			x.prev = null;
			x.next = next;
			x = next;
		}
		first = last = null;
		size = 0;
		modCount++;
	
	}

	@Override
	public Object[] toArray() {
		Object[] result = new Object[size];
		int i = 0;
		for (Node<E> x = first; x != null; x = x.next) {
			result[i++] = x.item;
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (a.length < size) {
			a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
		}
		int i = 0;
		Object[] result = a;
		for (Node<E> x = first; x != null; x = x.next)
			result[i++] = x.item;
	
		if (a.length > size)
			a[size] = null;
		return a;
	}

	@Override
	public Iterator<E> iterator() {
		return new ListItr(0);
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		checkIndex(index);
		return new ListItr(index);
	}

	@Override
	public ListIterator<E> listIterator() {
		return new ListItr(0);
	}

	private class ListItr implements ListIterator<E> {
		private Node<E> lastReturned;
		private Node<E> next;
		private int nextIndex;
		protected int expectedModCount = modCount;

		ListItr(int index) {
			next = (index == size) ? null : node(index);
			nextIndex = index;
		}

		@Override
		public boolean hasNext() {
			return nextIndex < size;
		}

		@Override
		public E next() {
			checkForComodification();
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			lastReturned = next;
			next = next.next;
			nextIndex++;
			return lastReturned.item;
		}

		@Override
		public boolean hasPrevious() {
			return nextIndex > 0;
		}

		@Override
		public E previous() {
			checkForComodification();
			if (!hasPrevious())
				throw new NoSuchElementException();

			lastReturned = next = (next == null) ? last : next.prev;
			nextIndex--;
			return lastReturned.item;
		}

		@Override
		public int nextIndex() {
			return nextIndex;
		}

		@Override
		public int previousIndex() {
			return nextIndex - 1;
		}

		@Override
		public void set(E e) {
			if (lastReturned == null) {
				throw new IllegalStateException();
			}
			checkForComodification();
			lastReturned.item = e;
		}

		@Override
		public void add(E e) {
			checkForComodification();
			lastReturned = null;
			if (next == null)
				linkLast(e);
			else
				linkBefore(e, next);
			nextIndex++;
			expectedModCount++;
		}

		@Override
		public void remove() {
			checkForComodification();
			if (lastReturned == null)
				throw new IllegalStateException();
		
			Node<E> lastNext = lastReturned.next;
			unlink(lastReturned);
			if (next == lastReturned)
				next = lastNext;
			else
				nextIndex--;
			lastReturned = null;
			expectedModCount++;
		}

		final void checkForComodification() {
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
		}
	}

}
