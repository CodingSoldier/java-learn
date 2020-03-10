DispatcherServlet
    org.springframework.web.servlet.DispatcherServlet.initViewResolvers
        AnnotationAwareOrderComparator.sort(this.viewResolvers);
        对所获取到的view进行排序
    @Nullable
    protected View resolveViewName(String viewName, @Nullable Map<String, Object> model,
      Locale locale, HttpServletRequest request) throws Exception {
      if (this.viewResolvers != null) {
        for (ViewResolver viewResolver : this.viewResolvers) {
          View view = viewResolver.resolveViewName(viewName, locale);
          按优先级查找能处理viewName的viewResolver，找到了就返回view，不再处理
          if (view != null) {
            return view;
          }
        }
      }
      return null;
    }


访问index.jsp时，spring使用ContentNegotiatingViewResolver来处理，
ContentNegotiatingViewResolver.viewResolvers包含了除它之外的所有resolver
最终返回的view是ThymeleafView

WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.defaultViewResolver配置
    @Bean
    @ConditionalOnMissingBean
    public InternalResourceViewResolver defaultViewResolver() {
          InternalResourceViewResolver resolver = new InternalResourceViewResolver();
          resolver.setPrefix(this.mvcProperties.getView().getPrefix());
          resolver.setSuffix(this.mvcProperties.getView().getSuffix());
          return resolver;
    }

@ConditionalOnMissingBean IOC容器用没有此Bean类或bean名，就算符合条件

内容协商：
    web客户端根据不同的请求策略，实现服务端响应对应视图内容输出，这些策略包括：
        Accept请求头，如 Accept: text/*
        请求参数查询，如 /path?format=pdf
        请求拓展名，如 /abc.pdf


内容协商，获取最好的viewResolver
org.springframework.web.servlet.view.ContentNegotiatingViewResolver.getBestView
1、viewResolver列表根据Order接口的值排序，先处理优先级高的
2、循环viewResolver列表，获取viewResolver的contentType，
   假如viewResolver.contentType能处理request的mediaType就返回此viewResolver
   request的mediaType是切割request的Accept获得



####jsp和Thymeleaf混合bug太多了，不建议使用






