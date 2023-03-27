package p01_array;

import javax.xml.bind.ValidationEvent;
import javax.xml.crypto.Data;
import java.util.Stack;

/**
 * @author chenpq05
 * @since 2023/3/24 15:27
 */
public class Array<E> {

    private E[] data;
    private int size;

    public Array() {
        this(10);
    }

    public Array(int size) {
        data = (E[]) new Object[size];
        this.size = 0;
    }

    public int getCapacity() {
        return data.length;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size==0;
    }

    public void add(int index, E e) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("非法参数");
        }
        if (size == data.length) {
            resize(2 * size);
        }
        for (int i = size - 1; i >= index ; i--) {
            data[i + 1] = data[i];
        }
        data[index] = e;
        size++;
    }

    public void addLast(E e) {
        add(size, e);
    }

    public void addFirst(E e) {
        add(0, e);
    }

    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("非法参数异常");
        }
        return data[index];
    }

    public E getLast() {
        return get(size - 1);
    }

    public E getFirst() {
        return get(0);
    }

    public void set(int index, E e) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("非法参数异常");
        }
        data[index] = e;
    }

    public int find(E e) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(e)) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(E e) {
        int i = find(e);
        return i != -1;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("非法参数");
        }
        if (size == data.length / 4) {
            resize(data.length / 2);
        }
        E temp = data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        size--;
        data[size] = null;
        return temp;
    }

    public E removeFirst() {
        return remove(0);
    }

    public E removeLast() {
        return remove(size - 1);
    }

    @Override
    public String toString(){
        StringBuilder res = new StringBuilder();
        res.append(String.format("Array: size = %d , capacity = %d\n", size, data.length));
        res.append('[');
        for(int i = 0 ; i < size ; i ++){
            res.append(data[i]);
            if(i != size - 1)
                res.append(", ");
        }
        res.append(']');
        return res.toString();
    }

    private void resize(int capacity) {
        E[] newData = (E[])new Object[capacity];
        for (int i = 0; i < data.length; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    public static void main(String[] args) {

        // Array<Integer> arr = new Array<>();
        // for(int i = 0 ; i < 10 ; i ++)
        //     arr.addLast(i);
        // System.out.println(arr);
        //
        // arr.add(1, 100);
        // System.out.println(arr);
        //
        // arr.addFirst(-1);
        // System.out.println(arr);
        //
        // arr.remove(2);
        // System.out.println(arr);
        //
        // arr.removeFirst();
        // System.out.println(arr);
        //
        // for(int i = 0 ; i < 4 ; i ++){
        //     arr.removeFirst();
        //     System.out.println(arr);
        // }


        // Array<Student> arr = new Array<>();
        // arr.addLast(new Student("Alice", 100));
        // arr.addLast(new Student("Bob", 66));
        // arr.addLast(new Student("Charlie", 88));
        // System.out.println(arr);

    }


}
