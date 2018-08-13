package com.designpattern.iterator.i1;

public class ConcreteAggregate extends Aggregate {

    private Object[] objArray = null;

    public ConcreteAggregate(Object[] objArray){
        this.objArray = objArray;
    }

    @Override
    public Iterator createIterator() {
        return new ConcreteIterator();
    }

    private class ConcreteIterator implements Iterator{
        private int index = 0;
        private int size = 0;
        public ConcreteIterator(){
            this.size = objArray.length;
            index = 0;
        }

        @Override
        public void first() {
            index=0;
        }

        @Override
        public void next() {
            if (index < size)
                index++;
        }

        @Override
        public boolean isDone() {
            return index >= size;
        }

        @Override
        public Object currentItem() {
            return objArray[index];
        }
    }

}
