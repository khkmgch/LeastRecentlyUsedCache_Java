// ここから開発しましょう。
import java.util.Map;
import java.util.HashMap;
import java.lang.StringBuilder;

interface Stack<E>{
    public abstract E peekBack();
    public abstract E popBack();
    public abstract void pushBack(Node<E> node);
}
interface Queue<E>{
    public abstract E peekFront();
    public abstract E popFront();
    public abstract void pushBack(Node<E> node);
}
class Node<E>{
    public int key;
    public E data;
    public Node<E> prev;
    public Node<E> next;

    public Node(int key, E data){
        this.key = key;
        this.data = data;
    }
}
abstract class AbstractDoublyLinkedList<E> implements Stack<E>, Queue<E>{
    protected Node<E> head;
    protected Node<E> tail;

    public AbstractDoublyLinkedList(){}

    public abstract void deleteNode(Node<E> node);
}
class CacheList<E> extends AbstractDoublyLinkedList<E>{
    public CacheList(){
        super();
    }

    public E peekBack(){
        if(this.tail == null)return this.peekFront();
        return this.tail.data;
    }
    public E popBack(){
        if(this.tail == null)return null;

        Node<E> temp = this.tail;
        this.tail = this.tail.prev;
        if(this.tail != null)this.tail.next = null;
        else this.head = null;

        return temp.data;
    }
    public void pushBack(Node<E> node){
        if(this.head == null){
            this.head = node;
            this.tail = this.head;
        }else{
            node.prev = this.tail;
            this.tail.next = node;
            this.tail = this.tail.next;
        }
    }
    public E peekFront(){
        if(this.head == null)return null;
        return this.head.data;
    }
    public E popFront(){
        if(this.head == null)return null;

        Node<E> temp = this.head;
        this.head = this.head.next;
        if(this.head != null)this.head.prev = null;
        else this.tail = null;

        return temp.data;
    }
    public void deleteNode(Node<E> node){
        if(node == null)return;

        if(node == this.head)this.popFront();
        else if(node == this.tail)this.popBack();
        else{
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }
    public String toString(){
        StringBuilder str = new StringBuilder("[");
        Node<E> iterator = this.head;
        while(iterator != null){
            str.append("< " + iterator.key + ", " + iterator.data + " >, ");
            iterator = iterator.next;
        }
        str.append("]");
        return str.toString();
    }
}
abstract class AbstractLRUCache<E>{
    protected int capacity;
    protected Map<Integer, Node<E>> cacheMap;
    protected CacheList<E> cacheList;

    public AbstractLRUCache(int capacity){
        this.capacity = capacity;
        this.cacheMap = new HashMap<Integer, Node<E>>();
        this.cacheList = new CacheList<E>();
    }
    
    public abstract E get(int key);
    public abstract void put(int key, E data);
}
class LRUCache<E> extends AbstractLRUCache<E>{
    public LRUCache(int capacity){
        super(capacity);
    }

    public E get(int key){
        if(!this.cacheMap.containsKey(key))return null;

        Node<E> temp = this.cacheMap.get(key);
        this.cacheList.deleteNode(temp);
        temp.prev = null;
        temp.next = null;
        this.cacheList.pushBack(temp);

        return temp.data;
    }
    public void put(int key, E data){
        Node<E> temp;
        if(this.cacheMap.containsKey(key)){
            temp = this.cacheMap.get(key);
            this.cacheList.deleteNode(temp);
            temp.data = data;
            temp.prev = null;
            temp.next = null;
        }else if(this.cacheMap.size() == this.capacity){
            temp = this.cacheList.head;
            this.cacheList.deleteNode(temp);
            this.cacheMap.remove(temp.key);
            temp = new Node<E>(key, data);
        }else{
            temp = new Node<E>(key, data);
            this.cacheMap.put(key, temp);
        }
        this.cacheList.pushBack(temp);
    }
}
class Main{
    public static void main(String[] args){
        LRUCache<String> c = new LRUCache<String>(10);
        c.put(0, "Hello");
        c.put(4, "Good Morning");
        c.put(1, "Good Afternoon");
        c.put(7, "Good Evening");
        c.put(4, "GoodBye");
        System.out.println(c.get(0));
        System.out.println(c.cacheList);
    }
}
