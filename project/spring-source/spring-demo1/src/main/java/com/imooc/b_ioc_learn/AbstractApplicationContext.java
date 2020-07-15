/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.support;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.support.ResourceEditorRegistrar;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.LifecycleProcessor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.context.weaving.LoadTimeWeaverAwareProcessor;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

/**
 * Abstract implementation of the {@link org.springframework.context.ApplicationContext}
 * interface. Doesn't mandate the type of storage used for configuration; simply
 * implements common context functionality. Uses the Template Method design pattern,
 * requiring concrete subclasses to implement abstract methods.
 *
 * <p>In contrast to a plain BeanFactory, an ApplicationContext is supposed
 * to detect special beans defined in its internal bean factory:
 * Therefore, this class automatically registers
 * {@link org.springframework.beans.factory.config.BeanFactoryPostProcessor BeanFactoryPostProcessors},
 * {@link org.springframework.beans.factory.config.BeanPostProcessor BeanPostProcessors},
 * and {@link org.springframework.context.ApplicationListener ApplicationListeners}
 * which are defined as beans in the context.
 *
 * <p>A {@link org.springframework.context.MessageSource} may also be supplied
 * as a bean in the context, with the name "messageSource"; otherwise, message
 * resolution is delegated to the parent context. Furthermore, a multicaster
 * for application events can be supplied as an "applicationEventMulticaster" bean
 * of type {@link org.springframework.context.event.ApplicationEventMulticaster}
 * in the context; otherwise, a default multicaster of type
 * {@link org.springframework.context.event.SimpleApplicationEventMulticaster} will be used.
 *
 * <p>Implements resource loading by extending
 * {@link org.springframework.core.io.DefaultResourceLoader}.
 * Consequently treats non-URL resource paths as class path resources
 * (supporting full class path resource names that include the package path,
 * e.g. "mypackage/myresource.dat"), unless the {@link #getResourceByPath}
 * method is overridden in a subclass.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Stephane Nicoll
 * @author Sam Brannen
 * @since January 21, 2001
 * @see #refreshBeanFactory
 * @see #getBeanFactory
 * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor
 * @see org.springframework.beans.factory.config.BeanPostProcessor
 * @see org.springframework.context.event.ApplicationEventMulticaster
 * @see org.springframework.context.ApplicationListener
 * @see org.springframework.context.MessageSource
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader
		implements ConfigurableApplicationContext {

	/**
	 * Name of the MessageSource bean in the factory.
	 * If none is supplied, message resolution is delegated to the parent.
	 * 在此工厂中，国际化消息 MessageSource 的 bean的名称。
	 * 如果没有提供消息，消息解析将委托给父节点。
	 * @see MessageSource
	 */
	public static final String MESSAGE_SOURCE_BEAN_NAME = "messageSource";

	/**
	 * Name of the LifecycleProcessor bean in the factory.
	 * If none is supplied, a DefaultLifecycleProcessor is used.
	 * 在此工厂中，SpringBean 的生命周期LifecycleProcessor 的 bean的名称
	 * 如果没有提供，则使用DefaultLifecycleProcessor。
	 * @see org.springframework.context.LifecycleProcessor
	 * @see org.springframework.context.support.DefaultLifecycleProcessor
	 */
	public static final String LIFECYCLE_PROCESSOR_BEAN_NAME = "lifecycleProcessor";

	/**
	 * Name of the ApplicationEventMulticaster bean in the factory.
	 * If none is supplied, a default SimpleApplicationEventMulticaster is used.
	 * 在此工厂中，应用事件多路广播器 的 bean的名称。
	 * 如果没有提供，则使用默认的simpleapplicationeventmultiaster。
	 * @see org.springframework.context.event.ApplicationEventMulticaster
	 * @see org.springframework.context.event.SimpleApplicationEventMulticaster
	 */
	public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";


	static {
		// Eagerly load the ContextClosedEvent class to avoid weird classloader issues
		// on application shutdown in WebLogic 8.1. (Reported by Dustin Woods.)
		ContextClosedEvent.class.getName();
	}


	/** Logger used by this class. Available to subclasses. */
	protected final Log logger = LogFactory.getLog(getClass());

	/** Unique id for this context, if any. */
	/** 此上下文的唯一id(如果有的话)。 */
	private String id = ObjectUtils.identityToString(this);

	/** Display name. */
	/** 容器的显示名称 */
	private String displayName = ObjectUtils.identityToString(this);

	/** Parent context. */
	/** 父上下文*/
	@Nullable
	private ApplicationContext parent;

	/** Environment used by this context. */
	/** 此上下文使用的环境。 */
	@Nullable
	private ConfigurableEnvironment environment;

	/** BeanFactoryPostProcessors to apply on refresh. */
	/** 应用于刷新的 BeanFactoryPostProcessors */
	private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

	/** System time in milliseconds when this context started. */
	/** 此上下文启动时的系统时间(毫秒)*/
	private long startupDate;

	/** Flag that indicates whether this context is currently active. */
	/** 指示此上下文当前是否处于活动状态的标志 */
	private final AtomicBoolean active = new AtomicBoolean();

	/** Flag that indicates whether this context has been closed already. */
	/** 指示此上下文是否已关闭的标志 */
	private final AtomicBoolean closed = new AtomicBoolean();

	/** Synchronization monitor for the "refresh" and "destroy". */
	/** 用于"刷新" 和 "销毁" 时的同步监视器锁*/
	private final Object startupShutdownMonitor = new Object();

	/** Reference to the JVM shutdown hook, if registered. */
	/** 如果已注册，则引用JVM关闭链接。 */
	@Nullable
	private Thread shutdownHook;

	/** ResourcePatternResolver used by this context. */
	/** 此上下文使用的 ResourcePatternResolver 资源模式解析器 */
	private ResourcePatternResolver resourcePatternResolver;

	/** LifecycleProcessor for managing the lifecycle of beans within this context. */
	/** LifecycleProcessor 生命周期处理器，用于在此上下文中管理bean的生命周期 */
	@Nullable
	private LifecycleProcessor lifecycleProcessor;

	/** MessageSource we delegate our implementation of this interface to. */
	/** 我们将这个接口的实现委托给 MessageSource */
	@Nullable
	private MessageSource messageSource;

	/** Helper class used in event publishing. */
	/** 事件发布所使用的辅助类 */
	@Nullable
	private ApplicationEventMulticaster applicationEventMulticaster;

	/** Statically specified listeners. */
	/** 静态的、指定的 listeners 监听器Set集合*/
	private final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();

	/** Local listeners registered before refresh. */
	/** 早期发布的 ApplicationEvents 应用事件Set集合*/
	@Nullable
	private Set<ApplicationListener<?>> earlyApplicationListeners;

	/** ApplicationEvents published before the multicaster setup. */
	/** 早期发布的 ApplicationEvents 应用事件Set集合 */
	@Nullable
	private Set<ApplicationEvent> earlyApplicationEvents;


	/**
	 * Create a new AbstractApplicationContext with no parent.
	 * 创建一个没有父元素的新的AbstractApplicationContext
	 */
	public AbstractApplicationContext() {
		this.resourcePatternResolver = getResourcePatternResolver();
	}

	/**
	 * Create a new AbstractApplicationContext with the given parent context.
	 * 使用给定的父上下文来创建一个新的AbstractApplicationContext
	 * @param parent the parent context
	 */
	public AbstractApplicationContext(@Nullable ApplicationContext parent) {
		this();
		setParent(parent);
	}


	//---------------------------------------------------------------------
	// Implementation of ApplicationContext interface
	//---------------------------------------------------------------------

	/**
	 * 设置此应用程序上下文的惟一id。
	 * 默认值是上下文实例的对象id，或者上下文bean的名称(如果上下文本身定义为bean的话)。
	 * @param id 上下文的唯一id
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
	/** 获取此应用程序上下文的id */
	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getApplicationName() {
		return "";
	}

	/**
	 * 为这个上下文设置一个(显示)名称。
	 * 通常在具体上下文实现的初始化过程中完成。
	 * 默认值是上下文实例的对象id。
	 */
	public void setDisplayName(String displayName) {
		Assert.hasLength(displayName, "Display name must not be empty");
		this.displayName = displayName;
	}

	/**
	 * 获取并返回此应用上下文的展示名称
	 * @return 此应用上下文的展示名称（非null）
	 */
	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	/**
	 * 返回父上下文，如果没有父上下文，则返回 null。
	 * (返回null意为着，此应用上下文是整个应用上下文体系的根上下文）。
	 */
	@Override
	@Nullable
	public ApplicationContext getParent() {
		return this.parent;
	}

	/**
	 * 为这个应用程序上下文设置 Environment 环境
	 * 默认值由 createEnvironment()方法决定。
	 * 用这个方法替换默认值不是唯一选择，还可通过 getEnvironment() 方法进行配置。
	 * 但不管在哪一种情况下，这些修改都应该在 refresh() 方法前执行。
	 * @see org.springframework.context.support.AbstractApplicationContext#createEnvironment
	 */
	@Override
	public void setEnvironment(ConfigurableEnvironment environment) {
		this.environment = environment;
	}

	/**
	 * 获取并返回此应用上下文的环境，如果为null，则通过 createEnvironment() 方法进行创建并返回。
	 */
	@Override
	public ConfigurableEnvironment getEnvironment() {
		if (this.environment == null) {
			this.environment = createEnvironment();
		}
		return this.environment;
	}

	/**
	 * 创建并返回新的StandardEnvironment 标准环境。
	 * 子类可以重写此方法，以便提供自定义 ConfigurableEnvironment 可配置环境的实现。
	 */
	protected ConfigurableEnvironment createEnvironment() {
		return new StandardEnvironment();
	}

	/**
	 * 如果此应用上下文已经可用，则将此上下文的内部bean工厂返回为AutowireCapableBeanFactory。
	 * @see #getBeanFactory()
	 */
	@Override
	public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
		return getBeanFactory();
	}

	/**
	 * Return the timestamp (ms) when this context was first loaded.
	 * 在首次加载此上下文时返回时间戳(ms)
	 */
	@Override
	public long getStartupDate() {
		return this.startupDate;
	}

	/**
	 * 将给定的事件发布给所有监听器。
	 * 注意:监听器在 MessageSource 之后初始化，以便能够在监听器实现中访问它。
	 * 因此，MessageSource 实现不能发布事件。要发布的事件(可能是特定于应用程序或标准框架事件)
	 */
	@Override
	public void publishEvent(ApplicationEvent event) {
		publishEvent(event, null);
	}

	/**
	 * 将给定的事件发布给所有监听器。
	 * 注意:监听器在 MessageSource 之后初始化，以便能够在监听器实现中访问它。
	 * 因此，MessageSource 实现不能发布事件。
	 * 要发布的事件(可能是 ApplicationEvent 或要转换为 PayloadApplicationEvent 的有效负载对象)
	 */
	@Override
	public void publishEvent(Object event) {
		publishEvent(event, null);
	}

	/**
	 * 将给定的事件发布给所有监听器。
	 * @param event 要发布的事件(可能是 ApplicationEvent 或要转换为 PayloadApplicationEvent 的有效负载对象)
	 * @param eventType 可解析的事件类型(如果已知)（前面的文章已经提到过）
	 * @since 4.2
	 */
	protected void publishEvent(Object event, @Nullable ResolvableType eventType) {
		Assert.notNull(event, "Event must not be null");

		// 必要时将事件装饰为 ApplicationEvent
		ApplicationEvent applicationEvent;
		if (event instanceof ApplicationEvent) {
			applicationEvent = (ApplicationEvent) event;
		}
		else {
			applicationEvent = new PayloadApplicationEvent<>(this, event);
			if (eventType == null) {
				eventType = ((PayloadApplicationEvent<?>) applicationEvent).getResolvableType();
			}
		}

		// 如果可能的话，现在就进行多播——或者在初始化多播器之后延迟
		if (this.earlyApplicationEvents != null) {
			this.earlyApplicationEvents.add(applicationEvent);
		}
		else {
			getApplicationEventMulticaster().multicastEvent(applicationEvent, eventType);
		}

		// 通过父上下文发布事件…
		if (this.parent != null) {
			if (this.parent instanceof AbstractApplicationContext) {
				((AbstractApplicationContext) this.parent).publishEvent(event, eventType);
			}
			else {
				this.parent.publishEvent(event);
			}
		}
	}

	/**
	 * 返回上下文使用的内部 ApplicationEventMulticaster 应用事件多路广播器
	 * @return 上下文使用的内部 ApplicationEventMulticaster 应用事件多路广播器（从不为null）
	 * @throws IllegalStateException 如果上下文尚未初始化
	 */
	ApplicationEventMulticaster getApplicationEventMulticaster() throws IllegalStateException {
		if (this.applicationEventMulticaster == null) {
			throw new IllegalStateException("ApplicationEventMulticaster not initialized - " +
					"call 'refresh' before multicasting events via the context: " + this);
		}
		return this.applicationEventMulticaster;
	}

	/**
	 * 返回上下文使用的内部生命周期处理器。
	 * @return 内部生命周期处理器。 (从不为null)
	 * @throws IllegalStateException 如果上下文尚未初始化
	 */
	LifecycleProcessor getLifecycleProcessor() throws IllegalStateException {
		if (this.lifecycleProcessor == null) {
			throw new IllegalStateException("LifecycleProcessor not initialized - " +
					"call 'refresh' before invoking lifecycle methods via the context: " + this);
		}
		return this.lifecycleProcessor;
	}

	/**
	 * 返回此 ResourcePatternResolver资源模式解析器，
	 * 用于将多个资源的位置按照模式解析到资源实例中。
	 * 默认是org.springframework.core.io.support.PathMatchingResourcePatternResolver。
	 * 支持ant风格的位置模式。
	 * 可以在子类中重写，用于扩展解析策略，例如在web环境中。在需要解决位置模式时不要调用此函数。
	 * 相反，调用上下文的getResources方法，它将委托给ResourcePatternResolver。
	 * @return 此应用上下文的 ResourcePatternResolver 资源模式解析器
	 * @see #getResources
	 * @see org.springframework.core.io.support.PathMatchingResourcePatternResolver
	 */
	protected ResourcePatternResolver getResourcePatternResolver() {
		return new PathMatchingResourcePatternResolver(this);
	}


	//---------------------------------------------------------------------
	// ConfigurableApplicationContext接口的实现
	//---------------------------------------------------------------------

	/**
	 * 设置此应用程序上下文的父上下文。
	 * 父级ApplicationContext#getEnvironment()环境是
	 * ConfigurableEnvironment#merge(ConfigurableEnvironment)，如果父级非null，
	 * 并且它的环境是ConfigurableEnvironment的实例，那么它就会与这个(子级)应用程序上下文环境合并
	 * @see ConfigurableEnvironment#merge(ConfigurableEnvironment)
	 */
	@Override
	public void setParent(@Nullable ApplicationContext parent) {
		this.parent = parent;
		if (parent != null) {
			Environment parentEnvironment = parent.getEnvironment();
			if (parentEnvironment instanceof ConfigurableEnvironment) {
				getEnvironment().merge((ConfigurableEnvironment) parentEnvironment);
			}
		}
	}
	/** 添加Bean工厂后置处理器 */
	@Override
	public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
		Assert.notNull(postProcessor, "BeanFactoryPostProcessor must not be null");
		this.beanFactoryPostProcessors.add(postProcessor);
	}

	/**
	 * 返回将应用到内部BeanFactory的BeanFactoryPostProcessors列表。
	 */
	public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
		return this.beanFactoryPostProcessors;
	}
	/** 添加应用上下文监听器 */
	@Override
	public void addApplicationListener(ApplicationListener<?> listener) {
		Assert.notNull(listener, "ApplicationListener must not be null");
		if (this.applicationEventMulticaster != null) {
			this.applicationEventMulticaster.addApplicationListener(listener);
		}
		this.applicationListeners.add(listener);
	}

	/**
	 * 返回静态指定的ApplicationListeners集合.
	 */
	public Collection<ApplicationListener<?>> getApplicationListeners() {
		return this.applicationListeners;
	}
	/**
	 * 加载或刷新一个持久化的配置，可能是XML文件、属性文件或关系数据库模式。
	 * 由于这是一种启动方法，如果失败，应该销毁已经创建的单例，以避免悬空资源。
	 * 换句话说，在调用该方法之后，要么全部实例化，要么完全不实例化。
	 * @throws 如果bean工厂无法初始化，则抛出 BeansException 异常
	 * @throws 如果已经初始化且不支持多次刷新，则会抛出 IllegalStateException 异常
	 */
	@Override
	public void refresh() throws BeansException, IllegalStateException {
		// 给容器refresh加锁，避免容器处在refresh阶段时，容器进行了初始化或者销毁的操作
		synchronized (this.startupShutdownMonitor) {
			// 调用容器准备刷新的方法，获取容器的当时时间，同时给容器设置同步标识，具体方法
			prepareRefresh();

			//告诉子类启动refreshBeanFactory()方法，Bean定义资源文件的载入从
			//子类的refreshBeanFactory()方法启动，里面有抽象方法
			//针对xml配置，最终创建内部容器，该容器负责 Bean 的创建与管理，此步会进行BeanDefinition的注册
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			// 注册一些容器中需要的系统Bean.例如classloader，beanfactoryPostProcessor等
			prepareBeanFactory(beanFactory);

			try {
				//允许容器的子类去注册postProcessor  ，钩子方法
				postProcessBeanFactory(beanFactory);

				// 激活在容器中注册为bean的BeanFactoryPostProcessors
				//对于注解容器，org.springframework.context.annotation.ConfigurationClassPostProcessor#postProcessBeanDefinitionRegistry
				//方法扫描应用中所有BeanDefinition并注册到容器之中
				invokeBeanFactoryPostProcessors(beanFactory);

				// 注册拦截bean创建过程的BeanPostProcessor
				registerBeanPostProcessors(beanFactory);

				// 找到“messageSource”的Bean提供给ApplicationContext使用，
				// 使得ApplicationContext具有国际化能力。
				initMessageSource();

				// 初始化ApplicationEventMulticaster该类作为事件发布者，
				// 可以存储所有事件监听者信息，并根据不同的事件，通知不同的事件监听者。
				initApplicationEventMulticaster();

				// 预留给 AbstractApplicationContext 的子类用于初始化其他特殊的 bean，
				// 该方法需要在所有单例 bean 初始化之前调用
				// 比如Web容器就会去初始化一些和主题展示相关的Bean（ThemeSource）
				onRefresh();

				// 注册监听器（检查监听器的bean并注册它们）
				registerListeners();

				//设置自定义的类型转化器ConversionService，
				// 设置自定义AOP相关的类LoadTimeWeaverAware，
				// 清除临时的ClassLoader
				// ，实例化所有的类（懒加载的类除外）
				finishBeanFactoryInitialization(beanFactory);

				// 初始化容器的生命周期事件处理器，（默认使用DefaultLifecycleProcessor），调用扩展了SmartLifecycle接口的start方法
				// 当Spring容器加载所有bean并完成初始化之后，会接着回调实现该接口的类中对应的方法（start()方法）
				// 并发布容器刷新完毕事件ContextRefreshedEvent给对应的事件监听者
				finishRefresh();
			}

			catch (BeansException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Exception encountered during context initialization - " +
							"cancelling refresh attempt: " + ex);
				}

				// Destroy already created singletons to avoid dangling resources.
				//销毁已创建的Bean
				destroyBeans();

				// Reset 'active' flag.
				//取消refresh操作，重置容器的同步标识
				cancelRefresh(ex);

				// Propagate exception to caller.
				throw ex;
			}

			finally {
				// 重置Spring内核中的共用的缓存，因为我们可能再也不需要单例bean的元数据了……
				resetCommonCaches();
			}
		}
	}

	/**
	 * 准备这个上下文来刷新、设置它的启动日期和活动标志以及执行属性源的任何初始化。
	 */
	protected void prepareRefresh() {
		// Switch to active.
		this.startupDate = System.currentTimeMillis();
		this.closed.set(false);
		// 1.设置容器的状态为激活
		this.active.set(true);

		if (logger.isDebugEnabled()) {
			if (logger.isTraceEnabled()) {
				logger.trace("Refreshing " + this);
			}
			else {
				logger.debug("Refreshing " + getDisplayName());
			}
		}

		// 2.初始化 Environment 的 propertySources 属性
		//样例<context:property-placeholder location="classpath*:/config/load.properties"/>
		initPropertySources();

		// 3.校验 Environment 的 requiredProperties 是否都存在
		// 请参考 ConfigurablePropertyResolver#setRequiredProperties
		getEnvironment().validateRequiredProperties();

		// Store pre-refresh ApplicationListeners...
		if (this.earlyApplicationListeners == null) {
			this.earlyApplicationListeners = new LinkedHashSet<>(this.applicationListeners);
		}
		else {
			// Reset local application listeners to pre-refresh state.
			this.applicationListeners.clear();
			this.applicationListeners.addAll(this.earlyApplicationListeners);
		}

		// 4.创建事件集合
		this.earlyApplicationEvents = new LinkedHashSet<>();
	}

	/**
	 * 用实际实例替换任何存根属性源。
	 * @see org.springframework.core.env.PropertySource.StubPropertySource
	 * @see org.springframework.web.context.support.WebApplicationContextUtils#initServletPropertySources
	 */
	protected void initPropertySources() {
		// 对于子类：默认情况下不执行任何操作。
	}

	/**
	 * 通知此上下文的子类去加载或刷新其内在的bean工厂
	 * @return 新的bean工厂实例
	 * @see #refreshBeanFactory()
	 * @see #getBeanFactory()
	 */
	protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
		refreshBeanFactory();
		return getBeanFactory();
	}

	/**
	 * 为BeanFactory设置各种功能，
	 * 例如容器的ClassLoader类加载器和post-processors后置处理器。
	 * @param beanFactory 要配置的bean工厂
	 */
	protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		// 告诉内部bean工厂使用容器的类加载器
		beanFactory.setBeanClassLoader(getClassLoader());
		// 设置beanFactory的表达式语言处理器,Spring3开始增加了对语言表达式的支持,默认可以使用#{bean.xxx}的形式来调用相关属性值
		beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
		// 为beanFactory增加一个默认的propertyEditor
		beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));
		//添加该处理器的作用：当应用程序定义的Bean实现ApplicationContextAware接口时注入ApplicationContext对象
		beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
		// 如果某个 bean 依赖于以下几个接口的实现类，在自动装配的时候忽略它们，
		// Spring 会通过其他方式来处理这些依赖。
		beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
		beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
		beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
		beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);




		// 修正依赖，这里是注册一些自动装配的特殊规则，比如是BeanFactory class接口的实现类，则在运行时修指定为当前BeanFactory
		beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
		beanFactory.registerResolvableDependency(ResourceLoader.class, this);
		beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
		beanFactory.registerResolvableDependency(ApplicationContext.class, this);

		// 注册早期后置处理器，用于检测内部bean作为应用程序监听器
		//ApplicationListenerDetector的作用就是判断某个Bean是否是ApplicationListener，
		// 如果是，加入到事件监听者队列。
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));

		// 如果找到一个LoadTimeWeaver，那么就准备将后置处理器“织入”bean工厂
		if (beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
			beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
			// 为类型匹配设置临时类加载器.
			beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
		}

		// 注册默认environment环境bean
		if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) {
			beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
		}
		if (!beanFactory.containsLocalBean(SYSTEM_PROPERTIES_BEAN_NAME)) {
			beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, getEnvironment().getSystemProperties());
		}
		if (!beanFactory.containsLocalBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {
			beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, getEnvironment().getSystemEnvironment());
		}
	}

	/**
	 * 在标准初始化后修改应用程序上下文的内部bean工厂。
	 * 所有bean定义都将被加载，但是没有bean会被实例化。
	 * 这允许在某些应用上下文实现中注册特殊的BeanPostProcessors等。
	 * @param beanFactory 应用环境下的Bean工厂
	 */
	protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
	}

	/**
	 * 实例化并调用所有已注册的BeanFactoryPostProcessor 的 bean，如果已给出顺序，请按照顺序。
	 * 必须在单实例实例化之前调用。
	 */
	protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
		PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());

		// 如何找到一个LoadTimeWeaver，那么就准备将后置处理器“织入”bean工厂
		// (例如，一个 @Bean 方法通过ConfigurationClassPostProcessor来注册)
		if (beanFactory.getTempClassLoader() == null && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
			beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
			beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
		}
	}

	/**
	 * BeanPostProcessor 的 bean，如果给出显式顺序，请按照顺序。
	 * 必须在应用程序bean的任何实例化之前调用。
	 */
	protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
		PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory, this);
	}

	/**
	 * 初始化MessageSource。
	 * 如果在此上下文中未定义国际化资源，则使用父上下文的国际化资源。
	 */
	protected void initMessageSource() {
		ConfigurableListableBeanFactory beanFactory = getBeanFactory();
		if (beanFactory.containsLocalBean(MESSAGE_SOURCE_BEAN_NAME)) {
			this.messageSource = beanFactory.getBean(MESSAGE_SOURCE_BEAN_NAME, MessageSource.class);
			// 使用此上下文的 MessageSource 知道父上下文的 MessageSource.
			if (this.parent != null && this.messageSource instanceof HierarchicalMessageSource) {
				HierarchicalMessageSource hms = (HierarchicalMessageSource) this.messageSource;
				if (hms.getParentMessageSource() == null) {
					// 如果没有已注册的父MessageSource，则只将父上下文设置为父MessageSource
					// registered already.
					hms.setParentMessageSource(getInternalParentMessageSource());
				}
			}
			if (logger.isTraceEnabled()) {
				logger.trace("Using MessageSource [" + this.messageSource + "]");
			}
		}
		else {
			// 使用空MessageSource可以接受getMessage方法的调用
			DelegatingMessageSource dms = new DelegatingMessageSource();
			dms.setParentMessageSource(getInternalParentMessageSource());
			this.messageSource = dms;
			beanFactory.registerSingleton(MESSAGE_SOURCE_BEAN_NAME, this.messageSource);
			if (logger.isTraceEnabled()) {
				logger.trace("No '" + MESSAGE_SOURCE_BEAN_NAME + "' bean, using [" + this.messageSource + "]");
			}
		}
	}

	/**
	 * 初始化ApplicationEventMulticaster。
	 * 如果在上下文中没有定义，则使用SimpleApplicationEventMulticaster。
	 * @see org.springframework.context.event.SimpleApplicationEventMulticaster
	 */
	protected void initApplicationEventMulticaster() {
		ConfigurableListableBeanFactory beanFactory = getBeanFactory();
		// 如果容器存在ApplicationEventMulticaster的bean实例，则赋值给容器的applicationEventMulticaster
		if (beanFactory.containsLocalBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME)) {
			this.applicationEventMulticaster =
					beanFactory.getBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);
			if (logger.isTraceEnabled()) {
				logger.trace("Using ApplicationEventMulticaster [" + this.applicationEventMulticaster + "]");
			}
		}
		else {
			// 没有则新建 SimpleApplicationEventMulticaster，
			// 并完成 SimpleApplicationEventMulticaster Bean 的注册
			this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
			beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
			if (logger.isTraceEnabled()) {
				logger.trace("No '" + APPLICATION_EVENT_MULTICASTER_BEAN_NAME + "' bean, using " +
						"[" + this.applicationEventMulticaster.getClass().getSimpleName() + "]");
			}
		}
	}

	/**
	 * 初始化LifecycleProcessor.
	 * 如果在当前上下文中没有定义，则使用 DefaultLifecycleProcessor
	 * @see org.springframework.context.support.DefaultLifecycleProcessor
	 */
	protected void initLifecycleProcessor() {
		ConfigurableListableBeanFactory beanFactory = getBeanFactory();
		if (beanFactory.containsLocalBean(LIFECYCLE_PROCESSOR_BEAN_NAME)) {
			this.lifecycleProcessor =
					beanFactory.getBean(LIFECYCLE_PROCESSOR_BEAN_NAME, LifecycleProcessor.class);
			if (logger.isTraceEnabled()) {
				logger.trace("Using LifecycleProcessor [" + this.lifecycleProcessor + "]");
			}
		}
		else {
			DefaultLifecycleProcessor defaultProcessor = new DefaultLifecycleProcessor();
			defaultProcessor.setBeanFactory(beanFactory);
			this.lifecycleProcessor = defaultProcessor;
			beanFactory.registerSingleton(LIFECYCLE_PROCESSOR_BEAN_NAME, this.lifecycleProcessor);
			if (logger.isTraceEnabled()) {
				logger.trace("No '" + LIFECYCLE_PROCESSOR_BEAN_NAME + "' bean, using " +
						"[" + this.lifecycleProcessor.getClass().getSimpleName() + "]");
			}
		}
	}

	/**
	 * 模板方法，可以重写以添加特定于上下文的刷新工作。
	 * 在特殊实例实例化之前调用特殊bean的初始化。
	 * 此实现为空。
	 * @throws BeansException in case of errors
	 * @see #refresh()
	 */
	protected void onRefresh() throws BeansException {
		// 对于子类：默认情况下不做任何事情。
	}

	/**
	 * 添加应用程序监听器作为监听器的bean。
	 * 不影响其他监听器，可以在没有bean的情况下添加。
	 */
	protected void registerListeners() {
		// 首先注册静态的指定的监听器
		for (ApplicationListener<?> listener : getApplicationListeners()) {
			getApplicationEventMulticaster().addApplicationListener(listener);
		}

		// 不要在这里初始化factorybean:我们需要保留所有未初始化的常规bean（事件监听器），
		// 让后处理器应用到它们!
		String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
		for (String listenerBeanName : listenerBeanNames) {
			getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
		}

		// 至此，已经完成将监听器注册到ApplicationEventMulticaster中，
		// 现在我们最终拥有一个多路广播器来发布前期的应用程序事件给监听器.
		Set<ApplicationEvent> earlyEventsToProcess = this.earlyApplicationEvents;
		this.earlyApplicationEvents = null;
		if (earlyEventsToProcess != null) {
			for (ApplicationEvent earlyEvent : earlyEventsToProcess) {
				getApplicationEventMulticaster().multicastEvent(earlyEvent);
			}
		}
	}

	/** 完成此上下文的bean工厂的初始化，初始化所有剩余的单例bean。 */
	protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
		// 初始化此容器的转换器
		// 转换器的职责是处理通过配置给Bean实例成员变量赋值的时候的类型转换工作
		if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME) &&
				beanFactory.isTypeMatch(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class)) {
			beanFactory.setConversionService(
					beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class));
		}

		// 如果没有注册过bean后置处理器post-processor，则注册默认的解析器
		// (例如主要用于解析properties文件的PropertyPlaceholderConfigurer )
		// @value注解或在xml中使用${}的方式进行环境相关的配置
		if (!beanFactory.hasEmbeddedValueResolver()) {
			beanFactory.addEmbeddedValueResolver(strVal -> getEnvironment().resolvePlaceholders(strVal));
		}

		// AOP分为三种方式：编译期织入、类加载期织入和运行期织入
		// LoadTimeWeaving属于第二种，主要通过JVM进行织入
		// 先初始化LoadTimeWeaverAware bean，以便尽早注册它们的transformers
		String[] weaverAwareNames = beanFactory.getBeanNamesForType(LoadTimeWeaverAware.class, false, false);
		for (String weaverAwareName : weaverAwareNames) {
			getBean(weaverAwareName);
		}

		// 停止使用临时类加载器进行类型匹配.
		beanFactory.setTempClassLoader(null);

		// 允许缓存所有bean定义元数据，不希望有进一步的更改
		beanFactory.freezeConfiguration();

		// 实例化所有剩余的(non-lazy-init非延时加载的)单例
		beanFactory.preInstantiateSingletons();
	}

	/**
	 * 完成此上下文的刷新，调用LifecycleProcessor的onRefresh()方法
	 * 并发布org.springframework.context.event.ContextRefreshedEvent 事件.
	 */
	protected void finishRefresh() {
		// 清除上下文级别的资源缓存(如扫描的ASM元数据)
		clearResourceCaches();

		// 为这个上下文初始化生命周期处理器
		initLifecycleProcessor();

		// 首先将刷新传播到生命周期处理器
		getLifecycleProcessor().onRefresh();

		// 发布最终事件
		publishEvent(new ContextRefreshedEvent(this));

		// 如果处于激活状态，将参与到 LiveBeansView MBean 中.
		LiveBeansView.registerApplicationContext(this);
	}

	/**
	 * 取消此上下文的刷新尝试，在抛出异常后重置 active 标志。
	 * @param ex 导致取消的异常
	 */
	protected void cancelRefresh(BeansException ex) {
		this.active.set(false);
	}

	/**
	 * 重置Spring的共同的反射元数据缓存，
	 * 特别是 ReflectionUtils, AnnotationUtils, ResolvableType
	 * 和 CachedIntrospectionResults 缓存
	 * @since 4.2
	 * @see ReflectionUtils#clearCache()
	 * @see AnnotationUtils#clearCache()
	 * @see ResolvableType#clearCache()
	 * @see CachedIntrospectionResults#clearClassLoader(ClassLoader)
	 */
	protected void resetCommonCaches() {
		ReflectionUtils.clearCache();
		AnnotationUtils.clearCache();
		ResolvableType.clearCache();
		CachedIntrospectionResults.clearClassLoader(getClassLoader());
	}


	/**
	 * 向JVM运行时注册一个关闭链接，在JVM关闭时关闭这个上下文，除非此时它已经关闭。
	 * 委托给 doClose() 方法去关闭，用于实际的关闭过程。
	 * @see Runtime#addShutdownHook
	 * @see ConfigurableApplicationContext#SHUTDOWN_HOOK_THREAD_NAME
	 * @see #close()
	 * @see #doClose()
	 */
	@Override
	public void registerShutdownHook() {
		if (this.shutdownHook == null) {
			// No shutdown hook registered yet.
			this.shutdownHook = new Thread(SHUTDOWN_HOOK_THREAD_NAME) {
				@Override
				public void run() {
					synchronized (startupShutdownMonitor) {
						doClose();
					}
				}
			};
			Runtime.getRuntime().addShutdownHook(this.shutdownHook);
		}
	}

	/**
	 * 用于销毁此实例的回调，最初附加到 DisposableBean 的实现(spring 5.0中不再存在)。
	 * close() 方法是关闭 ApplicationContext 本应用上下文的方法，
	 * 该destroy()方法只是委托给close()方法。
	 * @deprecated 从Spring Framework 5.0开始，本方法被标记为过时，而支持 close()方法
	 */
	@Deprecated
	public void destroy() {
		close();
	}

	/**
	 * 关闭此应用程序上下文，销毁其bean工厂中的所有bean。
	 * 实际关闭过程是委派 doClose()方法。
	 * 同时，如果已在JVM注册了关闭链接，也删除其关闭链接，因为它不再需要了。
	 * @see #doClose()
	 * @see #registerShutdownHook()
	 */
	@Override
	public void close() {
		synchronized (this.startupShutdownMonitor) {
			doClose();
			// 如果已在JVM注册了关闭链接，现在我们不再需要它了：
			// 我们已经明确地关闭了上下文。
			if (this.shutdownHook != null) {
				try {
					Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
				}
				catch (IllegalStateException ex) {
					// 忽略已经关闭的VM
				}
			}
		}
	}

	/**
	 * 实际上执行上下文关闭:
	 * 发布ContextClosedEvent山下文关闭事件，并销毁此应用程序上下文的bean工厂中的单例对象。
	 * 如果有的话，则调用 close() 方法和一个JVM关闭链接。
	 * @see #destroyBeans()
	 * @see #close()
	 * @see #registerShutdownHook()
	 */
	protected void doClose() {
		// Check whether an actual close attempt is necessary...
		if (this.active.get() && this.closed.compareAndSet(false, true)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Closing " + this);
			}

			LiveBeansView.unregisterApplicationContext(this);

			try {
				// 发布上下文关闭事件
				publishEvent(new ContextClosedEvent(this));
			}
			catch (Throwable ex) {
				logger.warn("Exception thrown from ApplicationListener handling ContextClosedEvent", ex);
			}

			// 停止所有 Lifecycle bean，以避免单个地销毁期间而产生延迟。
			if (this.lifecycleProcessor != null) {
				try {
					this.lifecycleProcessor.onClose();
				}
				catch (Throwable ex) {
					logger.warn("Exception thrown from LifecycleProcessor on context close", ex);
				}
			}

			// 销毁上下文的bean工厂中所有缓存的单例对象
			destroyBeans();

			// 关闭该上下文本身的状态
			closeBeanFactory();

			// 让子类做一些最后的清理...
			onClose();

			// 设置此应用上下文为非活跃状态.
			if (this.earlyApplicationListeners != null) {
				this.applicationListeners.clear();
				this.applicationListeners.addAll(this.earlyApplicationListeners);
			}

			// 将容器的状态设置为未激活.
			this.active.set(false);
		}
	}

	/**
	 * 模板方法，用于销毁该上下文管理的所有bean。
	 * 调用 DisposableBean.destroy() 或 指定的“destroy-method”销毁方法，
	 * 默认销毁将此实现的上下文中所有缓存的单例对象。
	 * 可以重写，以在标准单例销毁之前或之后添加上下文特定的bean销毁步骤，
	 * 同时上下文的BeanFactory仍然处于活动状态。
	 * @see #getBeanFactory()
	 * @see org.springframework.beans.factory.config.ConfigurableBeanFactory#destroySingletons()
	 */
	protected void destroyBeans() {
		getBeanFactory().destroySingletons();
	}

	/**
	 * 模板方法，用于销毁该上下文管理的所有bean。
	 * 调用 DisposableBean.destroy() 或 指定的“destroy-method”销毁方法，
	 * 默认销毁将此实现的上下文中所有缓存的单例对象。
	 * 可以重写，以在标准单例销毁之前或之后添加上下文特定的bean销毁步骤，
	 * 同时上下文的BeanFactory仍然处于活动状态。
	 * @see #getBeanFactory()
	 * @see org.springframework.beans.factory.config.ConfigurableBeanFactory#destroySingletons()
	 */
	protected void onClose() {
		// For subclasses: do nothing by default.
	}

	@Override
	public boolean isActive() {
		return this.active.get();
	}

	/**
	 * 声明此上下文的BeanFactory当前是活动的，如果不是，则抛出 IllegalStateException。
	 * 由所有依赖于活动上下文的 BeanFactory 委托方法调用，特别是所有bean访问方法。
	 * 默认实现检查 isActive() 这个上下文的“active”状态。
	 * 可能被覆盖用于更具体的检查，或用于如果 getBeanFactory() 本身在这种情况下抛出异常。
	 */
	protected void assertBeanFactoryActive() {
		if (!this.active.get()) {
			if (this.closed.get()) {
				throw new IllegalStateException(getDisplayName() + " has been closed already");
			}
			else {
				throw new IllegalStateException(getDisplayName() + " has not been refreshed yet");
			}
		}
	}


	//---------------------------------------------------------------------
	// BeanFactory 接口的实现
	//---------------------------------------------------------------------

	@Override
	public Object getBean(String name) throws BeansException {
		assertBeanFactoryActive();
		return getBeanFactory().getBean(name);
	}

	@Override
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		assertBeanFactoryActive();
		return getBeanFactory().getBean(name, requiredType);
	}

	@Override
	public Object getBean(String name, Object... args) throws BeansException {
		assertBeanFactoryActive();
		return getBeanFactory().getBean(name, args);
	}

	@Override
	public <T> T getBean(Class<T> requiredType) throws BeansException {
		assertBeanFactoryActive();
		return getBeanFactory().getBean(requiredType);
	}

	@Override
	public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
		assertBeanFactoryActive();
		return getBeanFactory().getBean(requiredType, args);
	}

	@Override
	public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) {
		assertBeanFactoryActive();
		return getBeanFactory().getBeanProvider(requiredType);
	}

	@Override
	public <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType) {
		assertBeanFactoryActive();
		return getBeanFactory().getBeanProvider(requiredType);
	}

	@Override
	public boolean containsBean(String name) {
		return getBeanFactory().containsBean(name);
	}

	@Override
	public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		assertBeanFactoryActive();
		return getBeanFactory().isSingleton(name);
	}

	@Override
	public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
		assertBeanFactoryActive();
		return getBeanFactory().isPrototype(name);
	}

	@Override
	public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
		assertBeanFactoryActive();
		return getBeanFactory().isTypeMatch(name, typeToMatch);
	}

	@Override
	public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
		assertBeanFactoryActive();
		return getBeanFactory().isTypeMatch(name, typeToMatch);
	}

	@Override
	@Nullable
	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		assertBeanFactoryActive();
		return getBeanFactory().getType(name);
	}

	@Override
	@Nullable
	public Class<?> getType(String name, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException {
		assertBeanFactoryActive();
		return getBeanFactory().getType(name, allowFactoryBeanInit);
	}

	@Override
	public String[] getAliases(String name) {
		return getBeanFactory().getAliases(name);
	}


	//---------------------------------------------------------------------
	// ListableBeanFactory 接口的实现
	//---------------------------------------------------------------------

	@Override
	public boolean containsBeanDefinition(String beanName) {
		return getBeanFactory().containsBeanDefinition(beanName);
	}

	@Override
	public int getBeanDefinitionCount() {
		return getBeanFactory().getBeanDefinitionCount();
	}

	@Override
	public String[] getBeanDefinitionNames() {
		return getBeanFactory().getBeanDefinitionNames();
	}

	@Override
	public String[] getBeanNamesForType(ResolvableType type) {
		assertBeanFactoryActive();
		return getBeanFactory().getBeanNamesForType(type);
	}

	@Override
	public String[] getBeanNamesForType(ResolvableType type, boolean includeNonSingletons, boolean allowEagerInit) {
		assertBeanFactoryActive();
		return getBeanFactory().getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
	}

	@Override
	public String[] getBeanNamesForType(@Nullable Class<?> type) {
		assertBeanFactoryActive();
		return getBeanFactory().getBeanNamesForType(type);
	}

	@Override
	public String[] getBeanNamesForType(@Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
		assertBeanFactoryActive();
		return getBeanFactory().getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
	}

	@Override
	public <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException {
		assertBeanFactoryActive();
		return getBeanFactory().getBeansOfType(type);
	}

	@Override
	public <T> Map<String, T> getBeansOfType(@Nullable Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
			throws BeansException {

		assertBeanFactoryActive();
		return getBeanFactory().getBeansOfType(type, includeNonSingletons, allowEagerInit);
	}

	@Override
	public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
		assertBeanFactoryActive();
		return getBeanFactory().getBeanNamesForAnnotation(annotationType);
	}

	@Override
	public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType)
			throws BeansException {

		assertBeanFactoryActive();
		return getBeanFactory().getBeansWithAnnotation(annotationType);
	}

	@Override
	@Nullable
	public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
			throws NoSuchBeanDefinitionException {

		assertBeanFactoryActive();
		return getBeanFactory().findAnnotationOnBean(beanName, annotationType);
	}


	//---------------------------------------------------------------------
	// HierarchicalBeanFactory 接口的实现
	//---------------------------------------------------------------------

	@Override
	@Nullable
	public BeanFactory getParentBeanFactory() {
		return getParent();
	}

	@Override
	public boolean containsLocalBean(String name) {
		return getBeanFactory().containsLocalBean(name);
	}

	/**
	 * 如果父上下文实现了ConfigurableApplicationContext接口，
	 * 则返回父上下文的内部bean工厂；否则，返回父上下文本身。
	 * @see org.springframework.context.ConfigurableApplicationContext#getBeanFactory
	 */
	@Nullable
	protected BeanFactory getInternalParentBeanFactory() {
		return (getParent() instanceof ConfigurableApplicationContext ?
				((ConfigurableApplicationContext) getParent()).getBeanFactory() : getParent());
	}


	//---------------------------------------------------------------------
	// MessageSource 接口的实现
	//---------------------------------------------------------------------

	@Override
	public String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage, Locale locale) {
		return getMessageSource().getMessage(code, args, defaultMessage, locale);
	}

	@Override
	public String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException {
		return getMessageSource().getMessage(code, args, locale);
	}

	@Override
	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		return getMessageSource().getMessage(resolvable, locale);
	}

	/**
	 * 返回上下文使用的内部 MessageSource。
	 * @return 此上下文内部的 MessageSource (非 null})
	 * @throws IllegalStateException 如果上下文尚未初始化
	 */
	private MessageSource getMessageSource() throws IllegalStateException {
		if (this.messageSource == null) {
			throw new IllegalStateException("MessageSource not initialized - " +
					"call 'refresh' before accessing messages via the context: " + this);
		}
		return this.messageSource;
	}

	/**
	 * 如果父上下文也是AbstractApplicationContext抽象类，则返回父上下文的内部 MessageSource；
	 * 否则，返回父上下文本身。
	 */
	@Nullable
	protected MessageSource getInternalParentMessageSource() {
		return (getParent() instanceof AbstractApplicationContext ?
				((AbstractApplicationContext) getParent()).messageSource : getParent());
	}


	//---------------------------------------------------------------------
	// ResourcePatternResolver 接口的实现
	//---------------------------------------------------------------------

	@Override
	public Resource[] getResources(String locationPattern) throws IOException {
		return this.resourcePatternResolver.getResources(locationPattern);
	}


	//---------------------------------------------------------------------
	// Lifecycle 接口的实现
	//---------------------------------------------------------------------

	@Override
	public void start() {
		getLifecycleProcessor().start();
		publishEvent(new ContextStartedEvent(this));
	}

	@Override
	public void stop() {
		getLifecycleProcessor().stop();
		publishEvent(new ContextStoppedEvent(this));
	}

	@Override
	public boolean isRunning() {
		return (this.lifecycleProcessor != null && this.lifecycleProcessor.isRunning());
	}


	//---------------------------------------------------------------------
	// 必须由子类实现的抽象方法
	//---------------------------------------------------------------------

	/**
	 * 子类必须实现此方法来执行实际配置的加载。
	 * 在任何其他初始化工作之前，可通过 refresh() 调用此方法。
	 * 子类要么创建一个新的bean工厂并持有对它的引用，
	 * 要么返回一个它持有的BeanFactory实例。
	 * 在后一种情况下，如果多次刷新上下文，它通常会抛出一个IllegalStateException。
	 * @throws BeansException 如果bean工厂的初始化失败
	 * @throws IllegalStateException 如果已经初始化并且不支持多次刷新尝试
	 */
	protected abstract void refreshBeanFactory() throws BeansException, IllegalStateException;

	/**
	 * 子类必须实现此方法来释放它们内部bean工厂。
	 * 在所有其他关闭工作之后， close() 将调用此方法。
	 * 永远不要抛出异常，而是日志关闭失败。
	 */
	protected abstract void closeBeanFactory();

	/**
	 * 子类必须在这里返回其内部bean工厂。
	 * 它们应该有效地实现查找，这样就可以重复调用查找，而不会影响性能。
	 * 注意:在返回内部bean工厂之前，子类应该检查上下文是否仍然是活动的。
	 * 一旦上下文关闭，内部工厂通常被认为不可用。
	 * @return 此应用上下文的内部bean工厂(非null)
	 * @throws IllegalStateException 如果上下文还没有包含内部bean工厂(通常是 refresh()}从未被调用)，或者上下文已经被关闭
	 * @see #refreshBeanFactory()
	 * @see #closeBeanFactory()
	 */
	@Override
	public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;


	/**
	 * 返回关于此上下文的信息
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(getDisplayName());
		sb.append(", started on ").append(new Date(getStartupDate()));
		ApplicationContext parent = getParent();
		if (parent != null) {
			sb.append(", parent: ").append(parent.getDisplayName());
		}
		return sb.toString();
	}

}
