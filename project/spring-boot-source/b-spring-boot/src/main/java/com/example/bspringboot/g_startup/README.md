spring计时器StopWatch
org.springframework.util.StopWatch.start()

启动加载器实现方式：
    方式一：
        实现CommandLineRunner接口
        重写run方法
        通过@Order排序
    方式二：
        实现ApplicationRunner接口
        重写run方法
        通过@Order排序

排序规则：
    通过@Order值指定排序
    @Order值相同ApplicationRunner实现优先运行

源码
org.springframework.boot.SpringApplication.callRunners()
    org.springframework.context.support.AbstractApplicationContext.getBeansOfType()  
    assertBeanFactoryActive()判断AbstractApplicationContext.active必须为true、AbstractApplicationContext.close必须为false。之前的AbstractApplicationContext.prepareRefresh()已经this.closed.set(false); this.active.set(true);
  后面的代码就是将CommandLineRunner、ApplicationRunner合并到一个List<Object>中，
  再通过AnnotationAwareOrderComparator.sort(runners);排序，
  然后遍历集合，运行实现的run方法
  结果就是优先使用@Order的值，值相同或无值，则先运行ApplicationRunner
        
