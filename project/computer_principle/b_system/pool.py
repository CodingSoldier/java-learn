# -*- encoding=utf-8 -*-

import threading

from b_system.queue import ThreadSafeQueue
from b_system.task import Task


class ProcessThread(threading.Thread):

    def __init__(self, task_queue, *args, **kwargs):
        threading.Thread.__init__(self, *args, **kwargs)
        # 线程停止标记
        self.dismiss_flag = threading.Event()
        # 任务队列
        self.task_queue = task_queue
        self.args = args
        self.kwargs = kwargs

    def run(self):
        while True:
            if  self.dismiss_flag.is_set():
                break
            task = self.task_queue.pop()
            result = task.callable(*task.args, **task.kwargs)

    def dismiss(self):
        self.dismiss_flag.set()

    def stop(self):
        self.dismiss()

# 线程池
class ThreadPool:

    def __init__(self, size=0):
        if not size:
            # 约定线程池的大小为CPU核数的两倍（最佳实践）
            size = 4
        # 线程池
        self.pool = ThreadSafeQueue(size)
        # 任务队列
        self.task_queue = ThreadSafeQueue()

        for i in range(size):
            self.pool.put(ProcessThread(self.task_queue))

    # 启动线程池
    def start(self):
        for i in range(self.pool.size()):
            thread = self.pool.get(i)
            thread.start()

    # 停止线程池
    def join(self):
        for i in range(self.pool.size()):
            thread = self.pool.get(i)
            thread.stop()
        while self.pool.size():
            thread = self.pool.pop()
            thread.join()

    # 往线程池提交任务
    def put(self, item):
        if not isinstance(item, Task):
            raise TaskTypeErrorException()
        self.task_queue.put(item)

    # 批量提交
    def batch_put(self, item_list):
        if not isinstance(item_list, list):
            item_list = list(item_list)
        for item in item_list:
            self.put(item)

    def size(self):
        return self.pool.size()


class TaskTypeErrorException(Exception):
    pass

