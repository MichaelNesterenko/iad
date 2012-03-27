package mishanesterenko.iad.lb1.core.misc;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class ArrayNonBlockingQueue<T> implements Queue<T> {
	private int left;
	private int right;
	private T[] internalStorage;

	public ArrayNonBlockingQueue(int size) {
		if (size <= 0)
			throw new IllegalArgumentException();
		this.internalStorage = (T[]) new Object[size];
		left = -1;
		right = -1;
	}

	public void enqueue(T value) {
		if (value == null) {
			throw new NullPointerException();
		}
		if ((left == right) && (left == -1)) {// first element
			left = right = 0;
			this.internalStorage[left] = value;
		} else if ((right + 1) % internalStorage.length != left) {
			right = (right + 1) % internalStorage.length;
			this.internalStorage[right] = value;
		} else {
			throw new IllegalStateException();
		}
	}

	public T dequeue() {
		if (left != -1) {
			int temp = left;
			if (left == right)
				left = right = -1;
			else {
				left = (left + 1) % internalStorage.length;
			}
			return this.internalStorage[temp];
		}
		throw new IllegalStateException();
	}

	public boolean canEnqueue() {
		return size() + 1 <= internalStorage.length;
	}

	public int size() {
		return left == -1 ? 0 : Math.abs(right - left) + 1;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	public Iterator<T> iterator() {
		throw new UnsupportedOperationException();
	}

	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(Collection<? extends T> c) {
		try {
			for(T item : c) {
				enqueue(item);
			}
		} catch (IllegalStateException e) {
			return false;
		}
		return true;
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		left = right = -1;
	}

	public boolean add(T e) {
		throw new UnsupportedOperationException();
	}

	public boolean offer(T e) {
		throw new UnsupportedOperationException();
	}

	public T remove() {
		throw new UnsupportedOperationException();
	}

	public T poll() {
		if (size() == 0) {
			return null;
		}
		return dequeue();
	}

	public T element() {
		throw new UnsupportedOperationException();
	}

	public T peek() {
		throw new UnsupportedOperationException();
	}
}
