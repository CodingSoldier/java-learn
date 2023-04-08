package p07_set_map.p01_set;

public interface Set<E> {

    void add(E e);
    boolean contains(E e);
    void remove(E e);
    int getSize();
    boolean isEmpty();
}
