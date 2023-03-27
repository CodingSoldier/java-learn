package p02_stack_and_queues.p02_array_stack;

/**
 * @author chenpq05
 * @since 2023/3/24 10:14
 */
public interface Stack<E> {
    int getSize();

    boolean isEmpty();

    void push(E e);

    E pop();

    E peek();
}
