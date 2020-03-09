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




