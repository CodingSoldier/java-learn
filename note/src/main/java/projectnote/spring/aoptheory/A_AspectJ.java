//package projectnote.spring.aoptheory;
//
//public aspect A_AspectJ
//        {
//// 指定执行 Hello.sayHello() 方法时执行下面代码块
//        void around():call(void Hello.sayHello()){System.out.println("开始事务 ...");proceed();System.out.println("事务结束 ...");}
//        }