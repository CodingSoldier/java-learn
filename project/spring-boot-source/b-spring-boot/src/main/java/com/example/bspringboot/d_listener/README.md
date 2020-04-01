ApplicationListener 应用程序事件监听器

@FunctionalInterface 注解的类只能有一个抽象方法

ApplicationEventMulticaster 应用程序广播器，管理监听器并广播Event

事件发送顺序：
    框架启动、starting、environmentPrepared、contextInitialized、prepared、started、ready、启动完毕





添加监听器    
org.springframework.context.event.AbstractApplicationEventMulticaster.addApplicationListener   

       
广播事件
org.springframework.context.event.SimpleApplicationEventMulticaster#multicastEvent(org.springframework.context.ApplicationEvent, org.springframework.core.ResolvableType)

    获取所有监听器
    org.springframework.context.event.AbstractApplicationEventMulticaster#getApplicationListeners(org.springframework.context.ApplicationEvent, org.springframework.core.ResolvableType)
        
        
        org.springframework.context.event.AbstractApplicationEventMulticaster.retrieveApplicationListeners
          返回监听了当前事件的监听器
          if (supportsEvent(listener, eventType, sourceType)) {
            if (retriever != null) {
              retriever.applicationListeners.add(listener);
            }
            allListeners.add(listener);
          }        

调用事件监听
org.springframework.context.event.SimpleApplicationEventMulticaster#doInvokeListener

 
获取事件泛型
GenericApplicationListenerAdapter#resolveDeclaredEventType(java.lang.Class<?>)

https://juejin.im/post/5ce3c268f265da1b7c60ed88

SpringApplicationRunListeners
# SpringApplication运行方法的侦听器集合，在SpringApplication运行到事件时间点的时候调用响应的事件方法。
# this.listeners是多个SpringApplicationRunListener的集合，默认有一个SpringApplicationRunListener实现EventPublishingRunListener。添加SpringApplicationRunListeners并设置this.listeners为SpringApplicationRunListener的集合是方便我们拓展
# 事件方法内循环this.listeners，调用SpringApplicationRunListener实现类

	SpringApplicationRunListener
	# SpringApplication运行方法的侦听器集合，在SpringApplication运行到事件时间点的时候调用响应的事件方法。

		EventPublishingRunListener
		# SpringApplicationRunListener的实现类，在SpringApplication事件节点调用自身方法
		# 包含一个属性SimpleApplicationEventMulticaster initialMulticaster，用于广播事件

			SimpleApplicationEventMulticaster#multicastEvent()
			#有线程池则用线程池，没有就是用当前线程执行invokeListener(listener, event)
				
				SimpleApplicationEventMulticaster#resolveDefaultEventType()
				# 获取eventType
					ResolvableType#resolved就是具体Event的Class

				AbstractApplicationEventMulticaster#getApplicationListeners()
				# 返回与给定事件类型匹配的ApplicationListeners的集合。 不匹配的听众会被早期排除。
					
					AbstractApplicationEventMulticaster#supportsEvent()
					# 监听器是否支持此事件，
					# 如果是GenericApplicationListener的实现类，直接调用实现类的 supportsEventType()、supportsSourceType()。
					# 如果不是，创建GenericApplicationListenerAdapter，此类也有supportsEventType()、supportsSourceType()
					
					# 调试
					# 打断点 listener 等于 DelegatingApplicationListener

						GenericApplicationListenerAdapter#supportsEventType()
						# this.declaredEventType.isAssignableFrom(eventType))

				SimpleApplicationEventMulticaster#invokeListener(ApplicationListener<?> listener, ApplicationEvent event)
				通过event调用listener

					SimpleApplicationEventMulticaster#doInvokeListener()
					# 执行调用动作




