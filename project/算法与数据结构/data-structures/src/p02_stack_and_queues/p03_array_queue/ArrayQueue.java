package p02_stack_and_queues.p03_array_queue;

/**
 * @author chenpq05
 * @since 2023/3/27 15:18
 */
public class ArrayQueue<E> implements Queue<E> {

    private Array<E> array;

    public ArrayQueue() {
        this(10);
    }

    public ArrayQueue(int capacity) {
        array = new Array<>(capacity);
    }

    @Override
    public void enqueue(E e) {
        array.addLast(e);
    }

    @Override
    public E dequeue() {
        return array.removeFirst();
    }

    @Override
    public int size() {
        return array.getSize();
    }

    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }

    @Override
    public E getFront() {
        return array.getFirst();
    }

    @Override
    public String toString(){
        StringBuilder res = new StringBuilder();
        res.append("Queue：front [");
        for (int i = 0; i < array.getSize(); i++) {
            res.append(array.get(i));
            if (i < array.getSize() - 1) {
                res.append(", ");
            }
        }
        res.append("] tail");
        return res.toString();
    }

    public static void main(String[] args) {

        ArrayQueue<Integer> queue = new ArrayQueue<>();
        for(int i = 0 ; i < 10 ; i ++){
            queue.enqueue(i);
            System.out.println(queue);
            if(i % 3 == 2){
                queue.dequeue();
                System.out.println(queue);
            }
        }
    }
}
