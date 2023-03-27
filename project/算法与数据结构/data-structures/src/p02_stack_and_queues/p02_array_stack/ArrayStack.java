package p02_stack_and_queues.p02_array_stack;

/**
 * @author chenpq05
 * @since 2023/3/24 10:15
 */
public class ArrayStack<E> implements Stack<E>{

    private Array<E> array;

    public ArrayStack(Array<E> array) {
        this.array = array;
    }

    public ArrayStack() {
        this.array = new Array<>();
    }

    @Override
    public int getSize() {
        return array.getSize();
    }

    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }

    @Override
    public void push(E e) {
        array.addLast(e);
    }

    @Override
    public E pop() {
        return array.removeLast();
    }

    @Override
    public E peek() {
        return array.getLast();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Stack: [");
        for (int i = 0; i < array.getSize(); i++) {
            sb.append(array.get(i));
            if (i != array.getSize() -1) {
                sb.append(", ");
            }
        }
        sb.append("] top");
        return sb.toString();
    }
}
