# rtest - Remote integration testing framework for JVM applications

**rtest** (_r_ stands for _remote_) is a framework for integration testing of products running on top of JVM.

Term _integration test_ denotes the type of testing intended for checking the behaviour of 
component in the real / semi-real environment.


The framework is developed to achieve the goals based on beliefs gained by technical experience and takes some basic assumptions

### Goals:


* **The most important thing**: Tests are developer's friends. The test development should be **easy** and **fun**

* Tests should run as fast as possible. The time spent to startup and run the test should be negligible 
compared to the time it takes the business logic that is being in test to get executed

* Tests should run right on the JVM of the application. Despite that, the test should not be a part of the application distribution
and will be triggered from another JVM

* Developers should be able to change and re-run the test in any possible way as many times as they want during the development process.
 There should not be a need to restart the whole application or container that hosts the application between these changes

* **Note:** Rtest framework is developed for *integration* tests. For unit and system/functional tests other frameworks will probably work much better

* The framework itself should have as least external dependencies as possible

### Basic Assumptions:
 
 
##### The application can be of any type:

* Tomcat or Jetty running the application WAR
* An application running some kind of embedded web container (like Jetty)
* Not a web application at all (e.g. no HTTP / REST layer exposed at all)

##### The application will benefit the most from this framework if it uses dependency injection container
* Currently there is an integration with spring and its possible to *attach* the test to the application context,
  but again, its not a necessarily requirement. If DI container is not used in the application, the framework will work but
  accessing the components inside the running server will be under the application developer's responsibility.
  The integration with other DI containers (like Google Guice) can be added later

##### The framework should be seamlessly supported by any tool that runs the tests:
* IDE for development purposes (IntelliJ/Eclipse/Netbeans)
* ANT/Maven/Gradle for build
* Programmatic invocation of test for whatever purpose

In fact the invocation of the test will be transparent and they will treat the test as a regular unit test
 
##### The codebase of tests themselves should not be a part of the application distribution. Tests can be still run on top of this application
* For example, if the application is built as a WAR file that will be deployed in production, the jar module of tests should not be a part of the WAR
This allows really agile and fast development of test itself and doesn't bloat the distribution with unnecessary code

##### There are two different JVMs involved in the testing process: the JVM that runs the test and the JVM that runs the application
They can be on the same machine or on different machines - the framework doesn't do any assumptions in this area

##### The tests can be run in parallel against the same application
As long as the code-under-test permits such an execution there should not be any issue of doing so from the RTest framework's side.


### Requirements:

* The JVM running the application will be of version 7 at least (java 8 is supported of course)
* The tests themselves will be written in [Spock framework](http://spockframework.org/) So some learning curve is required
Good news are that Spock itself is really a good tool so even if you don't know groovy, you'll be able to run spock after probably a hour of study
*Later on you'll probably discover that it works better even for other types of tests ;)*

>The concepts behind this framework can be easily implemented on top of Junit as well, Spock was taken solely 
>out of belief that developing tests in spock is much easier and funnier process than developing tests with JUnit (4.x at least).

* The framework _does require some change in application_ namely exposing a component that will accept remote connection on some tcp port 
and (optionally) the folder for storing the server side test reports. Since the server side component is fully customizable,
its perfectly valid to turn it off in production/protect the port with some kind of firewall 

### Basic Flow

Here is a schematic high level explanation of how to does the rtest framework actually work

* The architecture is a well-known client-server architecture. The application acts as a server and as such it exposes an opened port to accept connections.

*  The test running process will always initiated on remote JVM (IDE, maven surefire plugin, and so forth) which will be referred in this document as a client.  
   The host/port of the remote server can be supplied externally but there exist reasonable defaults
   The test code will be compiled with server side classes in classpath, because the nature of test assumes that it has an access to the server side component

*  When the test actually starts, the framework contacts the server and __'asks'__ to run the test. Since the bytecode of the test doesn't exist on server at this point, it will supplied dynamically,
   the framework handles this transparently

*  The server runs the test and obtains the results

*  The results get serialized back to the client

*  The client tool will render the result of test execution, like this test has been run locally and finished with the obtained result.

*  Since the reports can be generated by spock as a part of the framework, they are stored on the server side in some folder - this is really customizable, but can be requested by client after the test has actually been run.
    
    
    
The basic flow is depicted in the following figure 

![figure 1](https://cloud.githubusercontent.com/assets/12389201/21518634/91788606-ccf0-11e6-8391-6bb46bc5d894.png)

In this example we have a build system that triggers a test **Foo**. So the setup would be preparing the application and running it. 
For example if its a web application, the tomcat/jetty web container should be started with the war of the application

Once this happens the build tool can start triggering tests. 
The actual test code acts as a proxy for server side execution but from the point of view of building system its just transparent.
When the server runs the test code there can be failures in tests, or all the tests will run successfully. In any case the results will be obtained by the client side
and will be propagated to the build system.

### More explanations 

#### Creating a basic test

First of all we should create a test in a module that will have a dependency on the testing framework.


    <dependency>
      <groupId>org.rtest</groupId>
      <artifactId>rtest-framework</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>


This will bring the dependencies on groovy, spock, etc.
Note that this dependency should be added to both application and the module that will actually contain the integration tests

Now it time to create the first test

In plain spock all tests extends _spock.lang.Specification_ class. The basic test looks like this:


    class MySampleTest extends Specification {
      def "check that 1 and 1 is 2" () {
         expect:
         1 + 1 == 2
      }
    }

RTest introduces an abstraction of _org.rtest.spock.specification.RemoteTestSpecification_
So the basic test that runs with RTest will look like this:

    import org.rtest.spock.specification.RemoteTestSpecification 
    
    class MySampleRemoteTest extends RemoteTestSpecification {
      def "check that 1 and 1 is 2" () {
         expect:
         1 + 1 == 2
      }
    }

An attempt to run this test immediately will fail because the server application is not available
So we'll have to start the server. But before doing that we'll have to register the RTest class that will 
actually get the request from clients

RTest is totally agnostic of technology that is used on server. The only thing the developer should know is that the 
entry point of the server is _org.rtest.framework.server.RTestRemoteServer_ class

So it should be created and started by means of calling the _start()_ method on it
The instance of the server should be available in memory as long as the server runs.

When its time to close the application, one of the following methods should be called: _shutdown()_ / _shutdownNow()_ 
They work pretty much like thread pool methods - _shutdown_ will wait for the current thread(s) to finish the execution,
while _shutdownNow_ will attempt to interrupt the threads before actually shutting down the service

Here is an example of web listener definition like this:


    public class RTestIntegrationListener implements ServletContextListener {
        private RTestRemoteServer remoteServer;

        public void contextInitialized(ServletContextEvent servletContextEvent) {
            remoteServer = new RTestRemoteServer();
            remoteServer.start();
        }
    
        public void contextDestroyed(ServletContextEvent servletContextEvent) {
            remoteServer.shutdown();
        }
    }
    
Alternatively the RTestRemoteServer can be defined as a Spring bean or in any other way
TODO: Add spring bean declaration example

Now when the application is started the port 7890 is ready to accept connections and will allow running one test at a time

At this point we can start the application and when its up and running - to run the test just like its a regular unit test
If the configuration is done correctly, the test will be green.


So what's the hassle one might ask?
Lets slightly modify the test (well, of course, its not a real test, but it illustrates what actually happens)
The server application can be up and running during these manipulations:


    import org.rtest.spock.specification.RemoteTestSpecification 
    
    class MySampleRemoteTest extends RemoteTestSpecification {
      
      def "check that 1 and 1 is 2" () {
               expect:
               1 + 1 == 2
      }
      
      def "check that 1 and 1 is 2 but this time with fancy print" () {
         when:
         println "Hello RTest world"
         then:
         1 + 1 == 2
      }
    }

Lets rerun the test and we'll notice that the print message has appeared on server application stdout 


OK, so at this point its easy to see that from the test its possible to call any object's method available on the server side and this is a real power of this framework.

But how does the server side object get's available in the from the test code?

Well, there are many different ways:

*  Just create the needed object by using a 'new' keyword
*  If the object is global/singleton - just get the instance of it
*  Get the reference to the object if the application uses "LookupObject" pattern

If the application uses spring framework, then probably the components that should be tested are deployed as spring beans.
The RTest framework can take advantage of this fact.

In fact, by the time the test starts running on server, all the spring beans should be deployed and available, in other words the application context should already exist

So, the framework will just connect to the existing application context and will inject all the beans to the data fields marked by **@Autowired** annotation

Here is an example:


Let's assume that the application is spring driven and there is a bean, called _calculator_
It has the following interface:

    
    interface Calculator {
       int add (int a, int b);
    }
    
    
And there exists some implementation of this interface that actually can calculate a sum of two numbers _a_ and _b_ and return the result like defined in the interface

The implementation is defined somewhere in Spring and is bound to application context in Runtime.


Lets also assume that the test should check the _Calculator_ implementation component
 
So the test should basically show how to do 2 new things:
 
* Integrate Spring 
* Autowire the _Calculator_ bean

In order to integration the Spring there are two different options:

* Use a bound  spring web integration module to obtain an application context from 
 Spring's ContextLoader. Its good when the spring is used in web project.
 In this case just add the following dependency to the project (both client and server modules):
 
 
   
   <dependency>
     <groupId>org.rtest</groupId>
     <artifactId>rtest-framework-spring-integration-web</artifactId>
     <version>${rtest.version}</version>
   </dependency>
   
   
* Use the spring core integration for non-web applications
  In this case use the following annotation:
  
   <dependency>
       <groupId>org.rtest</groupId>
       <artifactId>rtest-framework-spring-integration-core</artifactId>
       <version>${rtest.version}</version>
   </dependency>
    
   
   
_Note_: Since rtest-framework-spring-integration-web depends on rtest-framework-spring-integration-core it will be added transitively when
declaring a dependency on web framework part


Now, when working with spring web integration, the following annotation has to be placed on the Specification:

    org.rtest.integration.spring.api.SpringIntegrated(WebApplicationContextProvider)
 
This will cause the framework to connect to an existing application context on server
 
 _Note_: If the application doesn't work with web, there is no place that the framework can retrieve 
 an existing application context from. In this case its the responsibility of the application so supply an application context
 
 For doing this, one has to implement the following interface 
   
    org.rtest.integration.spring.api.ApplicationContextProvider
    

And pass this implementation to the value attribute inside "SpringIntegrated" annotation.


After this step is done, injecting the actual Calculator bean into the test should be very easy.
Just use a well-known _@Autowired_ field 


The example of the whole test appears below:


    @SpringIntegrated
    class CalculatorBeanTest extends RemoteTestSpecification {
    
       @Autowired
       Calculator calculator
       
      
       def "check that the calculator can add two numbers"() {
          expect:
          10 == calculator.add(6,4)
       }
    }

### RTest internal logging

There are different logging systems currently available in the Java Universe. 
For the internal logging of the framework itself, there are no any special assumptions about the logging system.
Instead it tries to recognize in runtime which logging system is actually in the classpath, 
and depending on what's available, will set up the logging adapter for the concrete system by the following algorithm:

* If slf4j is available it will be used
* Otherwise if log4j 1.x is available - it will be used
* Otherwise java.util.logging implementation will be used as a fallback because its always available in the classpath


The logging framework dependencies in rtest framework's are intentionally made 'optional' and therefor non-transitive.

### Configuration



### Reports
TODO: Add me



### A word on Dynamic reloading of the test

### Conclusions

