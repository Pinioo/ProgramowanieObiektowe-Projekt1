package agh.evolutiongame;

import java.util.HashMap;
import java.util.LinkedList;

public class ListsHashMap<K, V> {
    private HashMap<K, LinkedList<V>> listsHashMap;

    public ListsHashMap(){
        listsHashMap = new HashMap<>();
    }

    // Put object in list at key
    public void put(K key, V object){
        // If listsHashMap[key] exists -> add object to list
        if(this.listsHashMap.containsKey(key))
            this.listsHashMap.get(key).add(object);

        // Else -> create new list ay listsHashMap[key] and add object to list
        else {
            LinkedList<V> newList = new LinkedList<>();
            newList.add(object);
            this.listsHashMap.put(key, newList);
        }
    }

    // Remove object from list at key (if exists)
    public void remove(K key, V object) {
        if(this.listsHashMap.get(key) == null)
            return;
        this.listsHashMap.get(key).remove(object);
        if(this.listsHashMap.get(key).isEmpty())
            this.listsHashMap.remove(key);
    }

    // Returns LinkedList at listsHashMap[key];
    public LinkedList<V> get(K key) {
        if(!this.listsHashMap.containsKey(key))
            return null;
        return this.listsHashMap.get(key);
    }

    // Returns all objects in hash map's lists
    public LinkedList<V> allElementsList(){
        var result = new LinkedList<V>();
        this.listsHashMap.values().forEach(result::addAll);
        return result;
    }

    public int size(){
        return this.listsHashMap.size();
    }

    public HashMap<K, LinkedList<V>> getMap(){
        return this.listsHashMap;
    }
}
