# -*- encoding=utf-8 -*-
import time

from b_system import task, pool


class SimpleTask(task.Task):
    
    def __init__(self, callable):
        super(SimpleTask, self).__init__(callable)
        
def process():
    time.sleep(1)
    print('simple-task callable 1111')
    time.sleep(2)
    print('simple-task callable 2222')

def test():
    test_pool = pool.ThreadPool()
    test_pool.start()
    for i in range(10):
        simple_task = SimpleTask(process)
        test_pool.put(simple_task)

if __name__ == '__main__':
    test()


