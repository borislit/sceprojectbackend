package sce.finalprojects.sceprojectbackend.datatypes;

import java.util.LinkedHashMap;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -892531816449133046L;
	private final int capacity;
	private long accessCount = 0;
	private long hitCount = 0;

	public LRUCache(int capacity) {
		super(capacity + 1, 1.1f, true);
		this.capacity = capacity;
	}

	@Override
	public V get(Object key) {
		  accessCount++;
		    if (containsKey(key))
		    {
		      hitCount++;
		    }
		    V value = super.get(key);
		    return value;
	}
	
	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
		return size() > capacity;
	}

	public long getAccessCount() {
		return accessCount;
	}

	public long getHitCount() {
		return hitCount;
	}
}