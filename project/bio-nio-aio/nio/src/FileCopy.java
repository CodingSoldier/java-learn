/**
 * NIO使用Channel替代了stream
 *   stream是单方向的
 *   Channel再次双向操作，可读可写
 * 使用Selector监控多条Channel，查看那条Channel的数据已经准备好了
 * 可以在一个线程中处理多个Channel/IO
 *
 * Channel必须配合buffer使用
 *
 * channel之间可以进行数据交换
 */
public class FileCopy {
}
