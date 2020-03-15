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


请求：localhost:8080/hello-world/param?message=123456
RequestMappingHandlerAdapter.invokeHandlerMethod
    ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
    invocableMethod的method属性就是HelloworldRestController.helloWorldParam

    if (this.argumentResolvers != null) {
        invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
    }
    有参数，invocableMethod设置参数解析器，总共有26个参数解析器

    invocableMethod.invokeAndHandle(webRequest, mavContainer);
    开始调用方法
        InvocableHandlerMethod.invokeForRequest
            Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
            将请求参数，转换为方法实际参数列表

            Object returnValue = doInvoke(args);
            InvocableHandlerMethod.doInvoke
            调用方法
                getBridgedMethod().invoke(getBean(), args);
                反射调用方法

            ### 后面的乱了 ###
            MethodParameter[] parameters = getMethodParameters();
            获取到方法参数

            HandlerMethodArgumentResolverComposite.resolveArgument
                HandlerMethodArgumentResolver resolver = getArgumentResolver(parameter);
                最终获取到RequestParamMethodArgumentResolver
                    Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
                    获取请求参数
                        this.argumentResolvers.supportsParameter(parameter)
                        判断argumentResolvers是否支持此参数
                            RequestParamMethodArgumentResolver.supportsParameter
                            判断参数是否有@RequestParam
                resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
                AbstractNamedValueMethodArgumentResolver.resolveArgument
                    NamedValueInfo namedValueInfo = getNamedValueInfo(parameter);
                    获取方法中的参数名
                        StandardReflectionParameterNameDiscoverer#getParameterNames(java.lang.reflect.Method)
                        实际上是通过反射获取方法参数名列表
                    Object arg = resolveName(resolvedName.toString(), nestedParameter, webRequest);
                    通过参数名获取请求中参数的值
                    RequestParamMethodArgumentResolver.resolveName()
                        String[] paramValues = request.getParameterValues(name);
                        最终是通过request.getParameterValues(name);获取参数值

ServletInvocableHandlerMethod.invokeAndHandle
处理请求
    Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
    调用controller方法，获得返回值

    this.returnValueHandlers.handleReturnValue(returnValue, getReturnValueType(returnValue), mavContainer, webRequest);
    this.returnValueHandlers是HandlerMethodReturnValueHandlerComposite，处理返回结果
    HandlerMethodReturnValueHandlerComposite.returnValueHandlers共有15个对象
        HandlerMethodReturnValueHandler handler = selectHandler(returnValue, returnType);
        HandlerMethodReturnValueHandlerComposite.selectHandler
            迭代this.returnValueHandlers，通过supportsReturnType()方法返回处理返回值的HandlerMethodReturnValueHandler
            最终返回RequestResponseBodyMethodProcessor
        handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
        RequestResponseBodyMethodProcessor.handleReturnValue 处理请求
            writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
            Converters是一些序列化、反序列化器


POST请求：
localhost:8080/echo/user1
    {
        "id": 1,
        "name": "名称"
    }

调用栈：
user1:16, UserRestController (com.example.ee3rest.controller2)
........................

    returnValue返回的是User实例
    Object returnValue = doInvoke(args);
    执行controller方法
doInvoke:209, InvocableHandlerMethod (org.springframework.web.method.support)

    this.argumentResolvers是一个组合对象类，共有26种参数解析器
    InvocableHandlerMethod.getMethodArgumentValues
    Object[] args = this.getMethodArgumentValues(request, mavContainer, providedArgs);
invokeForRequest:136, InvocableHandlerMethod (org.springframework.web.method.support)
invokeAndHandle:102, ServletInvocableHandlerMethod (org.springframework.web.servlet.mvc.method.annotation)
invokeHandlerMethod:877, RequestMappingHandlerAdapter (org.springframework.web.servlet.mvc.method.annotation)
handleInternal:783, RequestMappingHandlerAdapter (org.springframework.web.servlet.mvc.method.annotation)
handle:87, AbstractHandlerMethodAdapter (org.springframework.web.servlet.mvc.method)

    HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
    处理此请求的HandlerAdapter是RequestMappingHandlerAdapter
doDispatch:991, DispatcherServlet (org.springframework.web.servlet)
doService:925, DispatcherServlet (org.springframework.web.servlet)
processRequest:974, FrameworkServlet (org.springframework.web.servlet)
doPost:877, FrameworkServlet (org.springframework.web.servlet)


POST请求：
localhost:8080/echo/user1
处理返回值流程
doInvoke:209, InvocableHandlerMethod (org.springframework.web.method.support)
    AbstractMessageConverterMethodProcessor.writeWithMessageConverters
        List<MediaType> requestedMediaTypes = getAcceptableMediaTypes(request);
        是请求头中Accept的值 */*

        List<MediaType> producibleMediaTypes = getProducibleMediaTypes(request, valueType, declaredType);
        RequestResponseBodyMethodProcessor能返回的媒体类型，有4个

        requestedType.isCompatibleWith(producibleType)
        请求的媒体类型，生成的媒体类型是否兼容
        由于请求头的Accept是 */*，即请求的媒体类型与所有生成的媒体兼容

            AbstractGenericHttpMessageConverter.write
            向前端输出响应结果


localhost:8080/echo/user/produces
指定具体的生成媒体类型
AbstractMessageConverterMethodProcessor.writeWithMessageConverters()
    List<MediaType> requestedMediaTypes = getAcceptableMediaTypes(request);
    List<MediaType> producibleMediaTypes = getProducibleMediaTypes(request, valueType, declaredType);
    controller接口指定了一个生产媒体类型，producibleMediaTypes就只有一个
        AbstractMessageConverterMethodProcessor.getProducibleMediaTypes()
        返回可以生产的媒体类型，从@RequestMapping.produces，或者从this.messageConverters中取值，或者返回全部媒体类型



自定义messageConverter
    1、新增 PropertiesHttpMessageConverter
    2、RestWebMvcConfigurer添加PropertiesHttpMessageConverter
    3、新增ControllerConverter.addProps
    4、发请求
        curl --location --request POST 'localhost:8080/add/props' \
        --header 'Content-Type: text/properties' \
        --data-raw 'id:1
        name:我自己'


