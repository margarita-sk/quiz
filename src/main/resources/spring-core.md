<details>
  <summary>What is Spring?</summary>
Spring is a framework for building Java applications that contains a lot of different modules that can be added or not depending on needs. 
The key feature of the Spring framework is the inversion of control.
</details>

<details>
  <summary>What are Spring modules?</summary>
  
  - Spring Core container
  - Spring AOP
  - Spring Web
  - Spring Test
  - Spring Security, etc.
</details>


<details>
  <summary>What is Inversion of Control?</summary>
  It is the design principle when the dependent part is not responsible for creating and managing its dependencies. 
</details>

<details>
  <summary>What is Dependency Injection?</summary>
  DI is a form of IoC when an IoC container injects dependencies into dependent parts.
</details>

<details>
  <summary>What is Dependency Lookup?</summary>
  It's a form of IoC when the dependent part looks for dependencies.
</details>

<details>
  <summary>Advantages and Disadvantages of Dependency Injection</summary>
  
  Advantages:
  - low coupling
  - reusability of code
  - readability and maintainability
  - easy testing
    
  Disadvantages:
  - increase complexity, especially in small apps
  - runtime errors
</details>

<details>
  <summary>BeanFacory vs ApplicationContext</summary>

  | |BeanFactory|ApplicationContext|
  |-----|-----|----|
  | | simplest container providing DI support| implements BeanFactory and adds more features|
  |bean initialization | lazy | by default eager | 
  | when to use | lightweight apps where memory consumption is critical | all other cases | 
  | event propogation| no | supports |
  | BeanFactoryPostProcessor | no automatic registration | automatic registration |
  | BeanPostProcessor | no automatic registration | automatic registration |
  | message resource handling| no | yes |
  | internationalization| no | yes |
</details>

<details>
  <summary>What is the ApplicationContext?</summary>
  Interface that represents IoC container and is responsible for bean management.
</details>

<details>
  <summary>What is a Bean?</summary>
  Object that is handled by Spring IoC container
</details>

<details>
  <summary>Can @Bean annotated methods be static?</summary>
  Yes, but they won't participate in the bean lifecycle as normal beans. They will be created before bean instantiation and without a proxy mechanism. 
  It can be useful if we would like to create BeanFactoryPostProcessor and BeanPostProcessor beans.
</details>

<details>
  <summary>Can @Bean methods be final?</summary>
  No, it will cause a compilation error.
</details>

<details>
  <summary>How ApplicationContext define which objects to instantiate, configure, and assemble?</summary>
BeanDefinitionReader reads configuration metadata: 1 XML, 2 Java annotations, 3 Java code and creates BeanDefinitions. Based on them beans are 
  instantiated and configured (scope, dependencies, other configuration settings)
</details>

<details>
  <summary>Dependency Injection mechanisms</summary>

  1. Constructor DI
  2. Setter DI
  3. Field DI (reflection)
</details>

<details>
  <summary>Can the collection be injected?</summary>
  Yes, but the qualifier should be specified.
  ```
  @Service
public class CollectionInjection {
    @Autowired
    @Qualifier("map")
    private Map<String, Object> map;
}
  ```
</details>

<details>
  <summary>Constructor-based or setter-based DI or field DI?</summary>
  
  ||constructor-based DI| setter-based DI | field DI |
  |---|---|---|---|
  |when|during instantiation | during initialization | during initialization|
  |no dependency in IoC container| throws NoSuchBeanDefinitionException | dependency can be null | dependency can be null|
  |usecase| for mandatory dependencies | for optional dependencies | for optional dependencies |
  |testing| easy  | easy  | harder |
</details>

<details>
  <summary>When beans are created?</summary>
  Singleton scoped beans are created during the container creation, others - when they are requested.
</details>

<details>
  <summary>When circular dependency may appear?</summary>
  When using a constructor or field injection class A requires class B in the constructor and class B requires class A when the app startup appears
  
  ``The dependencies of some of the beans in the application context form a cycle: ...``
</details>

<details>
  <summary>What is Lookup Method Injection?</summary>
  It is a feature in the Spring Framework that allows a bean to override the lookup method to get a new instance of a dependency whenever the method is called. This is particularly useful in scenarios where you want to obtain a new instance of a prototype-scoped bean within a singleton-scoped bean.

  ```
@Component
@Scope("prototype")
public class MyPrototypeBean {
    // Prototype bean implementation
}


import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public class MySingletonBean {

    // This method will be overridden by Spring to provide a new instance of MyPrototypeBean
    @Lookup
    public MyPrototypeBean getPrototypeBean() {
        return null; // The actual implementation is generated by Spring at runtime
    }

    public void doSomething() {
        // Obtain a new instance of MyPrototypeBean through the lookup method
        MyPrototypeBean prototypeBean = getPrototypeBean();
        
        // Use the prototype bean
        // ...
    }
}
```
</details>

<details>
  <summary>What are bean scopes?</summary>

  - singleton - is created when the application context is created
  - prototype - one instance every time a request for that specific bean is made
  - session - one instance per one HTTP request
  - request - one instance per one HTTP session
  - application - one instance per one lifecycle of ServletContext 
  - websocket - one instance per one lifecycle of WebSocket
+ you can configure your own scope, or register SimpleThreadScope(it exists but is not registered)
</details>

<details>
  <summary>How to handle Singleton Beans with Prototype-bean Dependencies?</summary>
  Lookup method injection
</details>

<details>
  <summary>What is the difference between bean id, alias, and bean qualifier?</summary>
The ID is a unique identifier - it can be only one. There can be a lot of aliases as well as qualifiers.
  
  ```
  @Component
public class Singer { } //id=singer, aliases=

@Component("johnMayer")
public class Singer { } //id=johnMayer, aliases=

@Configuration
public class Config {

	@Bean(name={"john", "johnny"}) //id=singer, aliases=["john", "johnny"]
  public Singer singer(){
	  return new Singer();
	}

	@Bean //id=johnMayer
  public Singer johnMayer(){
	  return new Singer();
	}
}
  ```
If we have more than one bean that qualifies for spring injection, then we use `@Qualifer` to specify which needs to be used for injection.

There **is** a difference: `@Bean("simpleCar")` (or `@Component("car")`) gives your bean with the name "car" in the Spring Context, whereas `@Qualifier("car")` only adds information **without changing the name of the bean**.

```
@Configuration
public class Config {
    @Bean("car")
    public Vehicle car(){ return ...}
}

@Component("car")
public class MuscleCar implement Vehicle {...}

@Component
@Qualifier("car")
public class ElectroCar implement Vehicle {...}


public class DriveService {

    private final Vehicle vehicle;

    @Autowired
    public Driver(@Qualifier("car") Vehicle vehicle) {
      this.vehicle = vehicle;
    }
}


@Component
@Qualifier("beanQualifier")
class BeanTwo implements TypeOne { }

@Component
@Qualifier("beanQualifier")
class BeanThree implements TypeOne { }

@Autowired
@Qualifier("beanQualifier")
Map<String, TypeOne> typeOneMap;
// The map will only contain the 2 beans with the qualifier "beanQualifier".
// {beanThree=BeanThree@9f674ac, beanTwo=BeanTwo@1da4b3f9}
```
</details>

<details>
	<summary>Spring beans lifecycle</summary>

 **Bean instantiation and DI**

1. BeanDefinitionReader parses configuration (xml, java, @) and creates BeanDefinitions

2. BeanFactoryPostProcessor modificates BeanDefinitions (property resolution, custom annotation processing)

2. BeanFactory instantiates Beans based on BeanDefinitions by invoking constructors (constructor-based DI)

3. Inject bean dependencies by calling setters and field injection (reflection)

**BeanPostProcessor adjusts beans - postProcessBeforeInitialization() - 1st round**

**Bean initialization**

1. Check for Spring Awareness

- If bean **implements BeanNameAware** - call **setBeanName()**
- If bean implements **BeanClassLoaderAware** - call **setBeanClassLoader()**
- If bean implements **ApplicationContextAware** - call **setApplicationContext()**

2. Bean Creation Lifecycle Callback

- If **@PostConstruct (JSR-250)** is present - call method annotated with it
- If bean type **implements InitialazingBean** - call method **afterPropertiesSet()**
- If bean definition contains **init-method** or **@Bean(initMethod=””)** - call the init method

**BeanPostProcessor - postProcessAfterInitialization() - 2d round** (for proxies)

**Bean Destruction Lifecycle callback**

- If **@PreDestroy (JSR-250)** is present - call method annotated with it
- If bean type **implements DisposableBean** - call method **destroy()**
- If bean definition contains **destroy-method** or **@Bean(destroyMethod=””)** - call the destroy method
</details>

<details>
	<summary>What is BeanPostProcessor?</summary>
It is an interface with two default methods (that we may overrite): postProcessBeforeInitialization and postProcessAfterInitialization. We can configure several custom postProcessors and set the order.	
</details>

<details>
	<summary>Bean annotations?</summary>
	
- @Component
- @Service
- @Controller
- @Repository
- @Bean
- @RestController
</details>

<details>
	<summary>How does Spring framework resolve autowired entries?</summary>
	By default, Spring resolves autowired entries by type.
	
*If more than one bean of the same type is available in the container, the framework will throw NoUniqueBeanDefinitionException,* indicating that more than one bean is available for autowiring.
</details>

<details>
	<summary>What is @Order and @Priority ?</summary>
Used to define the order of a bean when it is part of an ordered collection or when it needs to be ordered relative to other beans. It can be applied to classes or methods

It is the same, but @Priority is JSR 250 and @Order is spring
</details>

<details>
	<summary>What is @Value?</summary>
Spring annotation that is used to inject externalized properties.
</details>

<details>
	<summary>What is PropertySourcesPlaceholderConfigurer?</summary>
Bean to configure application.properties. It should be static.
</details>

<details>
	<summary>What is @Import annotation?</summary>
	Spring annotation is used to import one or more configuration classes into another configuration class.
</details>

<details>
	<summary>What is difference between @Repository, @Service, @Component, @Controller, @RestController? </summary>
	
- @Repository - @Component + is a repository (DDD) + Platform exceptions can be translated into DataAcessExceptions if using PersistenceExceptionTranslationPostProcessor
- @Service - @Component with no specific logic added
- @Controller - @Component + presentation layer + request mapping (dispatcher scans the annotated classes  and detects methods annotated)
- @RestController - @Component + @Controller + @ResponseBody + can return JSON
- @Component - Component scan scans only beans annotated with a component annotation.
</details>

<details>
	<summary>How Spring detects stereotyped classes and register corresponding BeanDefinition instances with the ApplicationContext ?</summary>
	@ComponentScan annotation points to the packages that should be scanned.
</details>

<details>
	<summary>What is “lite” @Bean mode? </summary>
When @Bean annotated method is placed out of the configuration class it is called lite mode bean. It means it won't be proxied and will be treated just like the ordinary factory method. But it is managed by Spring container and can be injected.
</details>

<details>
	<summary>How to conditionally include configuration or bean?</summary>

1. We can use @Profile - it can be declared on class or method.
2. @Conditional - example: @Conditional("prod & cloud & !test"), @Conditional({"prod", "dev"}), @Conditional("(prod & cloud) | test") - if there is AND + OR always should be ().
</details>

<details>
	<summary>How to activate profiles?</summary>
	
1. spring.profiles.active=... in application.properties
2. @ActiveProfiles(...) in tests
3. context.getEnvironment().setActiveProfiles("dev")
</details>

<details>
	<summary>What is @PropertySource?</summary>
Annotation that allows the addition of property source to the environment. To be used in configuration classes: @PropertySource("classpath:/com/myco/app.properties")
</details>

<details>
	<summary>How internalization is implemented in Spring?</summary>
	Spring provides i18 - **`MessageSource`**
messages.properties
</details>

<details>
	<summary>What is ApplicationEvent in Spring>?</summary>
	Event handling in the ApplicationContext is provided through the ApplicationEvent class and the ApplicationListener interface. If a bean that implements the ApplicationListener interface is deployed into the context, every time an ApplicationEvent gets published to the ApplicationContext, that bean is notified.
</details>

<details>
	<summary>Which if placed inside application.properties will expose the "loggers" endpoint via HTTP?</summary>
	management.endpoints.web.exposure.loggers.include=true
</details>

<details>
	<summary>What are implementations of ApplicationContext interface?</summary>
	
1. Standalone: AnnotationConfigApplicationContext, ClassPathXmlApplicationContext, FileSystemXmlApplicationContext
2. Web Applications: GenericWebApplicationContext, XmlWebApplicationContext, AnnotationConfigWebApplicationContext
	
</details>

<details>
	<summary>Which is the default severity order for the Health Indicator statuses, provided by Spring Boot?</summary>
	DOWN, OUT_OF_SERVICE, UP, UNKNOWN
</details>

<details>
	<summary>What annotations are equivalent to @SpringBootApplication?</summary>

 - @SpringBootConfiguration
 - @EnableAutoConfiguration
 - @ComponentScan
</details>

<details>
	<summary>Which is the default SpEL Compiler mode?</summary>
	The SpEL compiler is not turned on by default, thus its default mode is OFF. The other two valid modes are IMMEDIATE and MIXED.
</details>

<details>
	<summary>Which endpoints are exposed by default via HTTP, after adding spring-boot-starter-actuator?</summary>
	Spring Boot Actuator exposes only two endpoints by default via HTTP. They are /actuator/health and /actuator/info.
</details>

<details>
	<summary>Can BeanPostProcessor be used to swap a Bean implementation during runtime?</summary>
	Since BeanPostProcessor interface defines two methods - postProcessBeforeInitialization and postProcessAfterInitialization. Both take two arguments - one of type Object representing the bean and one of type String representing the bean name. Furthermore, both have a return-type Object. That said, nothing can stop you from creating a custom class implementing BeanPostProcessor and providing logic for replacing, swapping, or casting the bean on certain conditions.
</details>

<details>
	<summary>Which is the default logging system used by Spring Boot?</summary>
Logback.
</details>

<details>
	<summary>Which protocols can be used to access actuator endpoints?</summary>
	Spring Boot Actuator supports two endpoints - HTTP and JMX.
</details>

<details>
	<summary>Sintaxis of Value annotation</summary>
	
- @Value("#{environment['my.special.property']}") - if just form env
- @Value("${my.special.property}") - if from property file that is already registered with the @PropertySource annotation

</details>

<details>
	<summary>What is the correct order in which Spring Boot will look for externalized configuration - properties files?
		
1 - Profile Specific inside jar
2 - Application Specific inside jar
3 - Profile specific outside jar
4 - Application specific outside jar</summary>

Currently, for Spring 2.5.* config data files are considered in the following order:
Application properties packaged inside your jar ( application.properties and YAML variants).
Profile-specific application properties packaged inside your jar (application-{profile}.properties and YAML variants).
Application properties  outside of your packaged jar (application.properties and YAML variants).
Profile-specific application properties outside of your packaged jar (application-{profile}.properties sand YAML variants).
Which would make the correct order 2,1,4,3.
</details>

<details>
	<summary>Which annotations can be used on top of a field in Spring, for the field to be a candidate for dependency injection?</summary>
	
- @Autowired
- @Inject
- @Resource
</details>

<details>
	<summary>What would "T(com.example.demo.model.Person).getId()" SPeL expression do?</summary>
	Read the static property "id" of Person
</details>

<details>
	<summary>What would "@car.horsePower" SpEL expression do? </summary>
	Read the property horsePower of a Spring bean named "car".
</details>

<details>
	<summary>The default embedded container that Spring Boot uses is Tomcat. Which of the following steps must be performed to change it to Jetty(for example)?</summary>
	Exclude spring-boot-starter-tomcat and include spring-boot-starter-jetty as a dependency
</details>

<details>
	<summary>Which protocol can be used to access the "loggers" endpoint by default, without using any additional configurations?</summary>
	JMX (Java Management Extensions)
</details>
