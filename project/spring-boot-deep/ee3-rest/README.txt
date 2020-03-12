处理方法参数解析器：  HandlerMethodArgumentRestResolver
处理方法返回值解析器： HandlerMethodReturnValueHandler
内容协商处理器：ContentNegotiationManager
媒体类型：MediaType
消费媒体类型：@RequestMapping#consumes
生产媒体类型：@RequestMapping#produces
HTTP消息转换器：HttpMessageConverter
REST配置器：WebMvcConfigurer


请求：localhost:8080/hello-world

DispatcherServlet.doDispatch
    mappedHandler = getHandler(processedRequest);
    DispatcherServlet.getHandler
    遍历所有HandlerMapping，HandlerMapping是接口，共有7个实现类，
    7个实现类的方法基本上一样，但是属性不一样，方法处理属性时，返回的结果也就不一样
        HandlerMapping.getHandler
        在7个实现类尝试去通过请求获取HandlerExecutionChain
            AbstractUrlHandlerMapping.getHandlerInternal
            HandlerMapping实现类获取请求URI
                AbstractHandlerMethodMapping.lookupHandlerMethod
                this.mappingRegistry是一个HashMap，是url与controller方法（HandlerMethod）的集合，
                每个HandlerMapping实现类的mappingRegistry都不一样
        最终RequestMappingHandlerMapping的mappingRegistry能处理localhost:8080/hello-world
        返回HandlerExecutionChain
    HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
    mappedHandler.getHandler()返回的类型是Object，本请求的实际类型是HandlerMethod（即 HelloworldRestController.helloWorld()）
    getHandlerAdapter()循环this.handlerAdapters，
    HandlerAdapter共有3个实现类，通过实现类的supports(Object handler)方法判断能否处理参数Object handler
    RequestMappingHandlerAdapter的supports()判断handler是否是HandlerMethod类型，最终返回true
        RequestMappingHandlerAdapter.invokeHandlerMethod准备执行HandlerMethod
           ServletInvocableHandlerMethod.invokeAndHandle
           开始执行方法HelloworldRestController.helloWorld()本身就是ServletInvocableHandlerMethod类型





