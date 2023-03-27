package p02_stack_and_queues.p03_array_queue;

/**
 * @author chenpq05
 * @since 2023/3/27 15:15
 */
public interface Queue<E> {

    void enqueue(E e);

    E dequeue();

    int size();

    boolean isEmpty();

    E getFront();

}
