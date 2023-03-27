package p02_stack_and_queues.p03_array_queue;

/**
 * @author chenpq05
 * @since 2023/3/27 16:54
 */
public class LoopQueue<E> implements Queue<E> {

    private E[] array;
    private int front;
    private int tail;
    private int size;

    public LoopQueue() {
        this(10);
    }

    public LoopQueue(int capacity) {
        this.array = (E[]) new Object[capacity + 1];
        this.front = 0;
        this.tail = 0;
        this.size = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public E getFront() {
        if (isEmpty()) {
            throw new IllegalArgumentException("队列为空");
        }
        return array[front];
    }

    public int getCapacity() {
        return array.length - 1;
    }

    @Override
    public void enqueue(E e) {
        if ((tail + 1) % array.length == front) {
            resize(getCapacity() * 2);
        }

        array[tail] = e;
        tail = (tail + 1) % array.length;
        size++;
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new IllegalArgumentException("队列为空");
        }
        E e = array[front];
        array[front] = null;
        front = (front + 1) % array.length;
        size--;
        if (size == getCapacity() / 4 && getCapacity() > 10) {
            resize(getCapacity() / 2);
        }
        return e;
    }

    private void resize(int capacity) {
        E[] newArray = (E[])new Object[capacity + 1];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[(front + i) % array.length];
        }
        array = newArray;
        front=0;
        tail=size;
    }

    @Override
    public String toString(){
        StringBuilder res = new StringBuilder();
        res.append(String.format("Queue: size = %d , capacity = %d\n", size, getCapacity()));
        res.append("front [");
        for (int i = front; i != tail; i = (i + 1) % array.length) {
            res.append(array[i]);
            if (tail != (i + 1) % array.length) {
                res.append(", ");
            }
        }
        res.append("] tail");
        return res.toString();
    }

    public static void main(String[] args) {
        LoopQueue<Integer> queue = new LoopQueue<>();
        for (int i = 0 ; i < 10 ; i ++) {
            queue.enqueue(i);
            System.out.println(queue);
            if(i % 3 == 2){
                queue.dequeue();
                System.out.println(queue);
            }
        }
    }

}
