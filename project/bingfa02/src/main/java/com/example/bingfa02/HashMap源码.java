// package com.example.bingfa02;
//
// import javax.swing.tree.TreeNode;
// import java.util.HashMap;
// import java.util.Map;
//
// public class HashMap源码 {
//
//     // 底层数组，可自动扩容，但是HashMap不支持缩容，长度总是2的N次方
//     transient Node<K,V>[] table;
//
//     // 初始容量大小，1左移4位结果是10000，转为十进制是16
//     static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
//
//     /**
//      * 同时满足“数组长度等于或大于64”、“链表长度等于或大于8” 两个条件，才将链表转为红黑树
//      */
// // 树化阀值
//     static final int TREEIFY_THRESHOLD = 8;
//     // 最小树化容量（树化是指将链表转为红黑树）
//     static final int MIN_TREEIFY_CAPACITY = 64;
//
//     // HashMap的数组最大长度
//     static final int MAXIMUM_CAPACITY = 1 << 30;
//
//     // 扩容的阈值
//     int threshold;
//
//     // 负载因子
//     static final float DEFAULT_LOAD_FACTOR = 0.75f;
//
//     /**
//      * 链表的Node
//      */
//     static class Node<K,V> implements Map.Entry<K,V> {
//         // key的hash值
//         final int hash;
//
//         // key
//         final K key;
//
//         // value
//         V value;
//
//         // 下一个元素
//         HashMap.Node<K, V> next;
//     }
//
//
//     /**
//      * 构造函数解析
//      */
//     public HashMap(int initialCapacity, float loadFactor) {
//
//         // 判断传入的参数是否合理
//         if (initialCapacity < 0)
//             throw new IllegalArgumentException("Illegal initial capacity: " +
//                     initialCapacity);
//         if (initialCapacity > MAXIMUM_CAPACITY)
//             initialCapacity = MAXIMUM_CAPACITY;
//         if (loadFactor <= 0 || Float.isNaN(loadFactor))
//             throw new IllegalArgumentException("Illegal load factor: " +
//                     loadFactor);
//
//         // 设置负载因子，默认是 0.75
//         this.loadFactor = loadFactor;
//
//         // 设置扩容阀值
//         // initialCapacity（初始容量大小）默认是16
//         // 用户设置的initialCapacity可以是任何大于0的数字，tableSizeFor(initialCapacity)返回结果是2的N次方。即Map的容量必然是2的N次方
//         this.threshold = tableSizeFor(initialCapacity);
//     }
//
//     /*
//         tableSizeFor(int cap)详解
//         返回大于等于cap，且一定是2的次方数
//         假设 cap = 10
//         n = 10 - 1 -> 9 -> 0b1001（0b表示二进制数）
//         0b1001 | 0b0100 -> 0b1101
//         0b1101 | 0b0010 -> 0b1111
//         0b1111 | 0b0100 -> 0b1111
//         最终n=15
//         return 15 + 1
//      */
//
//     /**
//      * tableSizeFor(int cap)方法解析
//      * 返回值大于等于cap，且一定是2的次方数
//      *
//      * 假设 cap = 10
//      * n = 10 - 1 -> 9 -> 0b1001（0b表示二进制数）
//      * n |= n >>> 1; 表示 n 等于 n 或 n右移一位
//      * 0b1001 | 0b0100 -> 0b1101  // n |= n >>> 1;
//      * 0b1101 | 0b0010 -> 0b1111  // n |= n >>> 2;
//      * 0b1111 | 0b0100 -> 0b1111  // n |= n >>> 4;
//      * 以此类推，最终 n=15
//      *
//      * return 16
//      */
//     static final int tableSizeFor(int cap) {
//         int n = cap - 1;
//         n |= n >>> 1;
//         n |= n >>> 2;
//         n |= n >>> 4;
//         n |= n >>> 8;
//         n |= n >>> 16;
//         return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
//     }
//
//
//     /**
//      * put方法解析
//      */
//     public V put(K key, V value) {
//         return putVal(hash(key), key, value, false, true);
//     }
//
//     /**
//      * ^异或：相同返回0，不同返回1
//      *
//      * hash的作用是让key的hash值的高16位也参与路由运算
//      * 例如：
//      * h = 0b 0010 0101 1010 1100 0011 1111 0010 1110
//      * h >>> 16 = 0b 0000 0000 0000 0000 0011 1111 0010 1110
//      *
//      * h ^ (h >>> 16) = 高16位不变，低16位变成了高16位和低16位的异或值
//      *
//      * 联系之前的“key路由算法”是 (table.length - 1) & node.hash
//      * 当table.length很小，是16、32、64，再减去1，换算成32位二进制，高位都是0，地位都是1
//      * 如果是 (table.length - 1) & h，则会导致key散列不均匀，
//      * (h = key.hashCode()) ^ (h >>> 16) 使得h的低16位更加复杂
//      * (table.length - 1) & (h = key.hashCode()) ^ (h >>> 16) 的路由算法结果的散列效果更好
//      */
//     /**
//      * 如果key是null，则返回0
//      * 如果key不是null，则使用 key的hashCode 异或 key的hashCode右移16位
//      */
//     static final int hash(Object key) {
//         int h;
//         return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
//     }
//
//
//     /**
//      * putVal方法分析
//      * @param hash key的hash值
//      * @param key key
//      * @param value value
//      * @param onlyIfAbsent key已经存在，是否改变value。如果为true，则不更改现有值；为false，修改value
//      * @param evict 如果为false，则表处于创建模式
//      * @return
//      */
//     final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
//         // tab: 引用当前HashMap的数组
//         // p: 数组的元素
//         // n：数组的长度
//         // i: 路由寻址的结果
//         HashMap.Node<K,V>[] tab; HashMap.Node<K,V> p; int n, i;
//
//         // 执行 new HashMap() 的时候并不会创建数组，节约内存，等首次插入键值对，才创建数组，这属于延迟初始化，所以会有table==null的判断
//         if ((tab = table) == null || (n = tab.length) == 0)
//             n = (tab = resize()).length;
//
//         // i = (n - 1) & hash 是key路由公式，tab[i = (n - 1) & hash] 找到key在数组中的位置
//         // 如果 tab[i] == null 证明当前位置还没有键值对，创建Node放到tab[i]中
//         if ((p = tab[i = (n - 1) & hash]) == null)
//             tab[i] = newNode(hash, key, value, null);
//
//             // tab[i]中已经有Node了
//         else {
//             // e: 一个临时的Node
//             // k: 一个临时的key
//             HashMap.Node<K,V> e; K k;
//
//             // key比较，桶位中的第一个元素与插入的key完全一致的情况
//             if (p.hash == hash &&
//                     ((k = p.key) == key || (key != null && key.equals(k))))
//                 // e后续要进行替换操作
//                 e = p;
//
//                 // p instanceof TreeNode 桶位是红黑树的情况
//             else if (p instanceof TreeNode)
//                 e = ((HashMap.TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
//
//                 // tab[i]是链表，并且链表第一个元素key与插入的key不一致
//             else {
//                 // 遍历链表
//                 for (int binCount = 0; ; ++binCount) {
//                     // (e = p.next) == null 表示迭代到了最后的元素
//                     if ((e = p.next) == null) {
//                         // 将插入的node放到链表末尾
//                         p.next = newNode(hash, key, value, null);
//                         // 新node插入到链表末尾，判断是否将链表转为红黑树
//                         // 链表长度等于或大于8，执行treeifyBin(tab, hash);
//                         // treeifyBin(Node<K,V>[] tab, int hash)方法中会判断数组长度小于MIN_TREEIFY_CAPACITY则执行扩容，否则执行链表变红黑树
//                         if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
//                             treeifyBin(tab, hash);
//                         break;
//                     }
//                     // 在遍历链表的过程中，找到了key完全相等的node元素
//                     // 退出循环，后续进行替换
//                     if (e.hash == hash &&
//                             ((k = e.key) == key || (key != null && key.equals(k))))
//                         break;
//                     p = e;
//                 }
//             }
//
//             // e != null 条件成立，说明插入的key在HashMap中已经存在，把值替换为新值即可，然后返回旧值
//             if (e != null) { // existing mapping for key
//                 V oldValue = e.value;
//                 if (!onlyIfAbsent || oldValue == null)
//                     // 替换value
//                     e.value = value;
//                 afterNodeAccess(e);
//                 // 返回旧值
//                 return oldValue;
//             }
//         }
//
//         // 散列表被修改的次数加一
//         // 替换node的value不算被修改，如果是替换操作，在上面的if (e != null)判断中return了，不会运行此处的代码
//         ++modCount;
//         // HashMap的node数量到达阈值，扩容
//         if (++size > threshold)
//             resize();
//         afterNodeInsertion(evict);
//         return null;
//     }
//
//     // 扩容
//     final HashMap.Node<K,V>[] resize() {
//         // oldTab：引用扩容前的数组
//         HashMap.Node<K,V>[] oldTab = table;
//         // oldCap: 扩容前数组table的长度
//         int oldCap = (oldTab == null) ? 0 : oldTab.length;
//         // oldThr：扩容前的扩容阈值
//         int oldThr = threshold;
//         // newCap：扩容后数组table的大小，先给个初值0
//         // newThr：扩容后的扩容阈值，先给个初值0
//         int newCap, newThr = 0;
//
//         // oldCap > 0 表示数组table已经初始化过了，是一次正常的扩容
//         if (oldCap > 0) {
//             // oldCap >= MAXIMUM_CAPACITY 数组的长度已经到达最大值，没法扩容了，直接return
//             if (oldCap >= MAXIMUM_CAPACITY) {
//                 threshold = Integer.MAX_VALUE;
//                 return oldTab;
//             }
//
//             // newCap = oldCap << 1 数字左移一位，等同于乘以2，但使用位运算更高效。新容量等于旧容量乘以2
//             // 例如：4 * 2 = 8 转为二进制左移操作：100 左移一位变为 1000
//             // (newCap = oldCap << 1) < MAXIMUM_CAPACITY -> 数组大小 < 最大限制值 ，这个判断条件基本都是true
//             // oldCap >= DEFAULT_INITIAL_CAPACITY 当前数组长度必须大于DEFAULT_INITIAL_CAPACITY
//             else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
//                     oldCap >= DEFAULT_INITIAL_CAPACITY)
//                 // 扩容阀值也要变化，新扩容阀值 = 旧扩容阀值左移一位，等同于乘以2
//                 newThr = oldThr << 1; // double threshold
//         }
//
//         /**
//          * oldCap == 0 && oldThr > 0 的情况
//          * 通过 new HashMap(int initialCapacity, float loadFactor)
//          *     new HashMap(int initialCapacity)
//          *     new HashMap(Map<? extends K, ? extends V> m)
//          * 这三种方式创建HashMap，构造函数会初始化oldThr，且 oldThr >= 16
//          */
//         else if (oldThr > 0) // initial capacity was placed in threshold
//             newCap = oldThr;
//
//         /**
//          * oldCap == 0 && oldThr == 0 的情况
//          * 通过 new HashMap() 创建的HashMap，构造函数不会初始化oldThr
//          */
//         else {               // zero initial threshold signifies using defaults
//             newCap = DEFAULT_INITIAL_CAPACITY;
//             // 扩容阈值是 负载因子 * 默认初始容量 = 12
//             newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
//         }
//
//         /**
//          * else if (oldThr > 0) 条件成立
//          *
//          * else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY
//          * && oldCap >= DEFAULT_INITIAL_CAPACITY) 条件不成立
//          *
//          * 这两种情况下，newThr == 0，需要计算扩容阈值
//          */
//         if (newThr == 0) {
//             float ft = (float)newCap * loadFactor;
//             newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
//                     (int)ft : Integer.MAX_VALUE);
//         }
//
//         // 得到扩容阈值
//         threshold = newThr;
//
//         // 前面的代码主要做两件事
//         // 1、计算出本次扩容后，table数组的长度
//         // 2、计算出下一次扩容的阈值
//
//         // 创建一个更大的数组，一般情况下是原数组的两倍长度
//         @SuppressWarnings({"rawtypes","unchecked"})
//         HashMap.Node<K,V>[] newTab = (HashMap.Node<K,V>[])new HashMap.Node[newCap];
//         table = newTab;
//
//         // oldTab != null 说明扩容前HashMap已经有数据
//         if (oldTab != null) {
//             // 遍历老数组
//             for (int j = 0; j < oldCap; ++j) {
//                 // 临时节点变量
//                 HashMap.Node<K,V> e;
//                 // (e = oldTab[j]) != null 当前桶位有数据，但是不知道是 单个Node、链表、红黑树 中的哪一种情况
//                 if ((e = oldTab[j]) != null) {
//                     // 方便JVM回收内存
//                     oldTab[j] = null;
//
//                     // e.next == null 当前桶位只有一个node
//                     if (e.next == null)
//                         // e.hash & (newCap - 1) 是 key的路由算法
//                         // 当前桶位只有一个元素，从未发生碰撞，可直接将当前元素放到新数组中
//                         newTab[e.hash & (newCap - 1)] = e;
//
//                     // e instanceof HashMap.TreeNode 桶位元素是红黑树
//                     else if (e instanceof HashMap.TreeNode)
//                         ((HashMap.TreeNode<K,V>)e).split(this, newTab, j, oldCap);
//
//                     // 桶位元素是链表
//                     else { // preserve order
//
//                         // 低位链表
//                         // 扩容之后的数组下标位置，与当前数组的下标位置一致
//                         // 假设原数组长度是16，table[15].hash = xxx0 1111，扩容后，还是在table[15]中
//                         HashMap.Node<K,V> loHead = null, loTail = null;
//
//                         // 高位链表
//                         // 扩容之后的数组下标位置 = 原数组下标 + 扩容之前数组的长度
//                         // 假设原数组长度是16，table[15].hash = xxx1 1111，扩容后，在table[31]中
//                         // 扩容之后的数组下标位置 = 当前数组下标位置 + 扩容之前数组的长度 -> 31 = 15 + 16 -> 1 1111 = 1111 + 10000
//                         HashMap.Node<K,V> hiHead = null, hiTail = null;
//
//                         // 临时变量
//                         HashMap.Node<K,V> next;
//
//                         do {
//                             next = e.next;
//                             // 假设原数组长度oldCap是16 ，转为二进制是 10000
//                             // 假设 e.hash = xxx0 1111 ，xxx0 1111 & 10000 = 0 ，扩容后node在低位链表中
//                             // 假设 e.hash = xxx1 1111 ，xxx1 1111 & 10000 = 10000 ，扩容后node在高位链表中
//                             if ((e.hash & oldCap) == 0) {
//                                 if (loTail == null)
//                                     loHead = e;
//                                 else
//                                     loTail.next = e;
//                                 loTail = e;
//                             }
//                             else {
//                                 if (hiTail == null)
//                                     hiHead = e;
//                                 else
//                                     hiTail.next = e;
//                                 hiTail = e;
//                             }
//                         } while ((e = next) != null);
//                         // loTail != null 低位链表有数据
//                         if (loTail != null) {
//                             // 新链表的最后一个node.next一定要设置为null
//                             // 因为在原链表中node.next可能还指向一个node
//                             loTail.next = null;
//                             // 低位链表还在原桶位中，即还在table[j]中
//                             newTab[j] = loHead;
//                         }
//                         // hiTail != null 高位链表有数据
//                         if (hiTail != null) {
//                             hiTail.next = null;
//                             // 高位链表放在 数组下标位置 = 当前数组下标位置 + 扩容之前数组的长度 的位置，即在table[[j + oldCap]]中
//                             newTab[j + oldCap] = hiHead;
//                         }
//                     }
//                 }
//             }
//         }
//         return newTab;
//     }
//
//
//     public V get(Object key) {
//         HashMap.Node<K,V> e;
//         return (e = getNode(hash(key), key)) == null ? null : e.value;
//     }
//
//     final HashMap.Node<K,V> getNode(int hash, Object key) {
//         // tab：HashMap底层数组
//         // first：桶位中的头元素
//         // e: 临时node元素
//         // n: table数组长度
//         HashMap.Node<K,V>[] tab; HashMap.Node<K,V> first, e; int n; K k;
//
//         // table不为null
//         // (n - 1) & hash 是key的路由算法，first = tab[(n - 1) & hash] 找到第一个桶元素
//         if ((tab = table) != null && (n = tab.length) > 0 &&
//                 (first = tab[(n - 1) & hash]) != null) {
//
//             // 头元素（如果是树，则称为根元素）正好是要查找的元素
//             if (first.hash == hash && // always check first node
//                     ((k = first.key) == key || (key != null && key.equals(k))))
//                 return first;
//             // 桶位不是单个node
//             if ((e = first.next) != null) {
//                 // 桶位是树
//                 if (first instanceof HashMap.TreeNode)
//                     return ((HashMap.TreeNode<K,V>)first).getTreeNode(hash, key);
//
//                 // 桶位是链表
//                 do {
//                     if (e.hash == hash &&
//                             ((k = e.key) == key || (key != null && key.equals(k))))
//                         return e;
//                 } while ((e = e.next) != null);
//             }
//         }
//         return null;
//     }
//
//     public V remove(Object key) {
//         HashMap.Node<K,V> e;
//         return (e = removeNode(hash(key), key, null, false, true)) == null ?
//                 null : e.value;
//     }
//
//     final HashMap.Node<K,V> removeNode(int hash, Object key, Object value,
//                                        boolean matchValue, boolean movable) {
//         // tab：HashMap底层数组
//         // p: 当前node元素
//         // n: 数组长度
//         // index: 寻址结果
//         HashMap.Node<K,V>[] tab; HashMap.Node<K,V> p; int n, index;
//
//         // 通过路由公式 (n - 1) & hash 查找到key所在桶位不为空
//         if ((tab = table) != null && (n = tab.length) > 0 &&
//                 (p = tab[index = (n - 1) & hash]) != null) {
//
//             // node：查找到的结果
//             // e: 当前node的下一个元素
//             HashMap.Node<K,V> node = null, e; K k; V v;
//
//             // 要删除的元素是桶位中的第一个元素
//             if (p.hash == hash &&
//                     ((k = p.key) == key || (key != null && key.equals(k))))
//                 node = p;
//
//             else if ((e = p.next) != null) {
//                 // 红黑树查找node
//                 if (p instanceof HashMap.TreeNode)
//                     node = ((HashMap.TreeNode<K,V>)p).getTreeNode(hash, key);
//
//                 // 链表的查找
//                 else {
//                     do {
//                         if (e.hash == hash &&
//                                 ((k = e.key) == key ||
//                                         (key != null && key.equals(k)))) {
//                             node = e;
//                             break;
//                         }
//                         p = e;
//                     } while ((e = e.next) != null);
//                 }
//             }
//
//             // 前面只是找到要删除的元素，并将元素赋值给node，下面执行删除操作
//             if (node != null && (!matchValue || (v = node.value) == value ||
//                     (value != null && value.equals(v)))) {
//                 // 红黑树删除元素
//                 if (node instanceof HashMap.TreeNode)
//                     ((HashMap.TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
//
//                 // node == p ，则p必然是桶位第一个元素
//                 // 删除桶位第一个元素
//                 else if (node == p)
//                     tab[index] = node.next;
//
//                 // 链表删除node，此时p是node的前一个元素
//                 else
//                     p.next = node.next;
//                 ++modCount;
//                 --size;
//                 afterNodeRemoval(node);
//                 return node;
//             }
//         }
//         return null;
//     }
//
// }
