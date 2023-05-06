package p09_segment_tree.p01;

/**
 * @author chenpq05
 * @since 2023/5/4 10:56
 */
public interface Merger<E> {

  E merge(E o1, E o2);

}
