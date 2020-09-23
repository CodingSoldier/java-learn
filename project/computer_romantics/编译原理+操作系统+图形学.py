汇编模拟器
	下载mars mips simulator。地址 https://courses.missouristate.edu/KenVollmar/mars/MARS_4_5_Aug2014/Mars4_5.jar
	运行  java -jar Mars4_5.jar


进程的状态
	运行、就绪、阻塞

Linux下将socket连接抽象为文件（文件句柄）
    select模式
        句柄使用数组存储
    poll模式
        句柄使用链表存储

Linux的select/poll模型（多路复用），是异步阻塞模型
    select/poll发生后，交由内核执行，直到有接收数据的通知出现。因此是异步模型
    select/poll发生后，把线程中管理的所有文件句柄发给内核，线程会被阻塞（进入阻塞状态），等待内核通知线程，数据处理完成，因此是阻塞模型

epoll
    可以向内核增删文件句柄
    内核返回事件（而不是文件句柄），事件中带有可以被读写的文件句柄，避免线程遍历



