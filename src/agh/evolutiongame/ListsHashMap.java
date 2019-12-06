package agh.evolutiongame;

import java.security.KeyException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Stream;

public class ListsHashMap<K, V> {
    private HashMap<K, LinkedList<V>> listsHashMap;

    public ListsHashMap(){
        listsHashMap = new HashMap<>();
    }

    public void put(K key, V object){
        if(this.listsHashMap.containsKey(key))
            this.listsHashMap.get(key).add(object);
        else {
            LinkedList<V> newList = new LinkedList<>();
            newList.add(object);
            this.listsHashMap.put(key, newList);
        }
    }

    public void remove(K key, V object) {
        this.listsHashMap.get(key).remove(object);
        if(this.listsHashMap.get(key).isEmpty())
            this.listsHashMap.remove(key);
    }

    public LinkedList<V> get(K key) {
        if(!this.listsHashMap.containsKey(key))
            return null;
        return this.listsHashMap.get(key);
    }

    public LinkedList<V> allElementsList(){
        var result = new LinkedList<V>();
        this.listsHashMap.values().forEach(result::addAll);
        return result;
    }

    public HashMap<K, LinkedList<V>> getMap(){
        return this.listsHashMap;
    }
}
