package mishanesterenko.iad.lb1.core.cache;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class Cache<K, V> {
	private Map<K, WeakReference<V>> m_storage;

	public Cache() {
		m_storage = new HashMap<K, WeakReference<V>>();
	}

	public V get(K key) {
		return m_storage.get(key).get();
	}

	public void put(K key, V value) {
		m_storage.put(key, new WeakReference<V>(value));
	}

	public void remove(K key) {
		m_storage.remove(key);
	}
}
