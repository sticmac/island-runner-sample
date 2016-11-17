# Island Runner Template

  * author: Sebastien Mosser
  * version: 1.0 (2016)
 
When delivering an Island explorer, one might want to try her strategy on a specific set of Island(s). This project shows briefly:

  1. How to run an IExplorerRaid with a given context
  2. How to load a resource 
  3. How to configure maven to trigger a given main class
  3. How to integrate a logger in your code (opt)

  
## Installing your Explorer

The Explorer you are defining is a project by itself. The responsibility of this software module (defining an Explorer) should not be polluted by execution concerns. This is why we separate the Explorer and the Runner in two projects.

We will rely on the Maven dependency mechanisms to allow the Runner to use your Explorer. But first, you need to `install` your Explorer in your local repository. To perform such a task, simply run the following maven command (considering that your project is located in the `myExplorer` directory):

    azrael:myExplorer mosser$ mvn install
    
If the build is successful, your software module is now installed on your computer and available for the other modules. 

**Warning**: Each time you make change in your explorer, you'll have to re-install it to make it available to the runner.

**Pro Tip**: one can chain maven commands on a single invocation. To _clean_ the directory, _package_ the code as a JAR file and _install_ it, simply:

    azrael:myExplorer mosser$ mvn clean package install   

## Creating a Runner Project

We now create a new project that will consumes your Explorer as a dependency. In the `pom.xml` file associated to this new project, we load: (i) your Explorer, and (ii) the Runner engine.

To load our explorer, we add it in the `dependencies` area the necessary configuration (that should match the contents of the `pom.xml` file defined in your explorer).

````
<dependency>
  <groupId>fr.unice.polytech.3a.qgl</groupId>
  <artifactId>iaxx</artifactId>
  <version>0.1-SNAPSHOT</version>
</dependency>
````        

## Running an Explorer

The Island Runner comes with a _fluent API_ used to configure it. One can define a _main_ method that invokes this API to create a context for the execution.

[For example](https://github.com/ace-design/island-runner-sample/blob/master/src/main/java/runner/RunExploration.java), to trigger the execution of an Explorer implemented in the `sample.bot.Idiot` class, named _"My_Team_Name"_, using the map stored in `map.json`, with an initialisation seed of `0xB03CA1A997813D02L`, starting in the upper-left corner and looking east, with 20,000 action points as budget and 15 crew members, collecting 1,000 units of wood, 300 unit of quartz and 10 units of flower, storing the output data in the `results` directory and showing the delivered report, one can use the following code:

````
public static void main(String[] args) throws Exception {
  run(sample.bot.Idiot.class) 
    .withName("My_Team_Name")
    .exploring(loadResource("map.json"))
    .withSeed(0xB03CA1A997813D02L)
    .startingAt(1,1,"EAST")
    .backBefore(20000)
    .withCrew(15)
    .collecting(1000, "WOOD"  )
    .collecting( 300, "QUARTZ")
    .collecting(  10, "FLOWER")
    .storingInto("./results")
    .showReport()
    .fire()
  ;
}
````  

The data needed to initialise a context can be found:

  - In the [championship results](https://github.com/mosser/QGL-16-17/tree/master/championships/) directory for the map.json file;
  - In the [Island definition file](https://github.com/mosser/QGL-16-17/blob/master/arena/src/main/scala/library/Islands.scala) for the initialisation seed (to guarantee that the creeks and the emergency sites are at the same place each time);
  - In the [championship model](https://github.com/mosser/QGL-16-17/blob/master/arena/src/main/scala/championships/) for the contract data (location, budget, ...)

The runner will generates three files in the `results` directory:

  - `Idiot.svg`: a graphical map of this specific run;
  - `Idiot_My_Team_Name.json`: the exploration journal of this run;
  - `_pois.json`: a file describing where the creeks and emergency sites are located.

## Loading a Resource

The `map.json` file is a critical resource. Java defines a _resources_ mechanism to support the embedding of such files inside a software modules, and avoiding local file path issues.

Maven asks you to put resources in the `src/main/resources` directory. Then, you can access to the contents of this directory from your code through the class loader:

````
private static File loadResource(String resourceName) {
  ClassLoader loader = RunExploration.class.getClassLoader();
  return new File(loader.getResource(resourceName).getFile());
}
````    

## Triggering a Main class from the CLI

As Maven is handling the dependencies, it handles the setting of the `CLASSPATH`, making it hard for you to run your code outside of the maven environment (or your IDE).

The `exec-maven-plugin` mechanisms might support this task for you. In the `pom.xml`, we configure the model to use the `runner.RunExploration` class as the main one.

````
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>exec-maven-plugin</artifactId>
  <version>1.5.0</version>
  <executions>
    <execution>
      <goals>
        <goal>java</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <mainClass>runner.RunExploration</mainClass>
    <!-- Necessary to avoid thread cleanup after SVG generation (warning) -->
    <cleanupDaemonThreads>false</cleanupDaemonThreads>
  </configuration>
</plugin>
````

Using this configuration, one can use the `exec:java` goal to start the class. 

    azrael:island-runner-sample mosser$ mvn exec:java

**Warning**: the `exec:java` goal will uses the _latest compiled_ version of the current project. If you want to be sure to execute the current code, you can chain the compilation goal to the execution one:

    azrael:island-runner-sample mosser$ mvn compile exec:java

## Using a Logger

Your business code should never ever use input/output mechanisms such as `System.out.println` for logging purposes. If you want to do so, you should rely on logging mechanisms than can be deactivated by the people who use your code without having to edit the source code.

In your `pom.xml`, you can import the _"Logging for Java 2"_ [library](https://logging.apache.org/log4j/2.x/) (aka `log4j2`, pronounced log-for-j-two) with the two following dependencies:

```
<dependency>
  <groupId>org.apache.logging.log4j</groupId>
  <artifactId>log4j-api</artifactId>
  <version>2.7</version>
</dependency>
<dependency>
  <groupId>org.apache.logging.log4j</groupId>
  <artifactId>log4j-core</artifactId>
  <version>2.7</version>
</dependency>
```

Look at the [Idiot](https://github.com/ace-design/island-runner-sample/blob/master/src/main/java/sample/bot/Idiot.java) class. 

The class starts by creating a static logger, as this logger will be shared by all the instances of this Explorer (this is one of the few situation where static declaration makes sense in Java).

```
private static final Logger logger = LogManager.getLogger(Idiot.class);
```

Then, the logger is used to log information:

```
@Override
public void initialize(String s) { logger.info("Initializing the explorer"); }
```

In a resource file named `log4j2.xml`, one can configure:

  1. How the log will be displayed (by defining `Appender`s)
  2. What must be logged, and at which level (by defining `Logger`s)

In our [example](https://github.com/ace-design/island-runner-sample/blob/master/src/main/resources/log4j2.xml), we use:

  - the console as appender to display the logs;
    -  using the following pattern: "DATE LEVEL LOGGER MESSAGE \n"
  - and the Idiot class logger is configured to display _all_ the logs
    - linked to the console

```
<Appenders>
  <Console name="Console" target="SYSTEM_OUT">
    <PatternLayout pattern="%d{HH:mm:ss} %-5level %-13logger{1} %msg%n"/>
  </Console>
</Appenders>

<Loggers>
  <!-- ... -->
  <Logger name="sample.bot.Idiot" level="all" additivity="false">
    <AppenderRef ref="Console"/>
  </Logger>
  <!-- ... -->
</Loggers>  
``` 

Then, one can decide to ignore your logs by simply muting this logger (setting `level` to `off`). Or if you wan to display info but not trace logs, by setting `level` to `info`. There are several levels (each one is bound to a specific method on the logger instance) defined by log4j2, that complies to a total order:

  - TRACE < DEBUG < INFO < WARN < ERROR < FATAL 


*Pro Tip*: Setting the level to L will display any log with a level greater than or equals to L. Two special levels can be used in the configuration file: `ALL` for everything (virtually the lowest element in the previous order) and `OFF` to mute the logger (virtually the greatest element in the previous order).

*Pro Tip*: Using the class name to define a logger is a convention. You can use anything (typically when you want to log elements at the package or feature level). The only constraints is to use non conflicting names (e.g., prefixed with your team name)

