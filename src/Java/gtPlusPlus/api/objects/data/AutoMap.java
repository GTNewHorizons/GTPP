package gtPlusPlus.api.objects.data;

import java.io.Serializable;
import java.util.*;

public class AutoMap<V> implements Iterable<V>, Cloneable, Serializable {

	/**
	 * The Internal Map
	 */
	protected final Map<Integer, V> mInternalMap;
	
	/**
	 * The Internal ID
	 */
	private int mInternalID = 0;
	private static final long serialVersionUID = 3771412318075131790L;

	
	public AutoMap() {
		this(new HashMap<Integer, V>());
	}
	
	public AutoMap(Map<Integer, V> defaultMapType) {
		mInternalMap = defaultMapType;
	}
	
	@Override
	public Iterator<V> iterator() {		
		return values().iterator();
	}

	public synchronized boolean setValue(V object){
		int mOriginalID = this.mInternalID;
		put(object);		
		if (this.mInternalMap.get(mOriginalID).equals(object) || mOriginalID > this.mInternalID){
			return true;
		}
		else {
			return false;
		}
	}
	
	public synchronized V put(V object){
		return set(object);
	}
	
	public synchronized V set(V object){
		return mInternalMap.put(mInternalID++, object);
	}
	
	public synchronized V get(int id){
		return mInternalMap.get(id);
	}
	
	public synchronized Collection<V> values(){
		return mInternalMap.values();
	}
	
	public synchronized int size(){
		return mInternalMap.size();
	}
	
	public synchronized int hashCode(){
		return mInternalMap.hashCode();
	}
	
	public synchronized boolean containsKey(int key){
		return mInternalMap.containsKey(key);
	}
	
	public synchronized boolean containsValue(V value){
		return mInternalMap.containsValue(value);
	}
	
	public synchronized boolean isEmpty(){
		return mInternalMap.isEmpty();
	}
	
	public synchronized boolean clear(){
		this.mInternalID = 0;
		this.mInternalMap.clear();
		return true;
	}
	
	public synchronized V[] toArray() {		
		Collection<V> col = this.mInternalMap.values();
		@SuppressWarnings("unchecked")
		V[] val = (V[]) col.toArray();		
		return val;
	}

	public synchronized final int getInternalID() {
		return mInternalID;
	}
  
}
