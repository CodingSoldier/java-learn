SpringApplication.getOrCreateEnvironment 
创建 environment

AbstractApplicationContext.prepareBeanFactory
注册environment bean

idea配置java系统属性 ——> VM Options ——> -Duser.city.name=中文   。属性前面加上-D


javax.validation.constraints.NotEmpty.message
搜索属性的值 Ctrl+Shift+F ——> Scope ——> Project and Libraries


扩展Environment最好放在SpringApplication.refreshContext()方法执行前