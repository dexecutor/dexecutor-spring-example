
### Create prototype scoped ExecutorService

```java
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	ExecutorService newExecutorService() {
		return Executors.newFixedThreadPool(ThreadPoolUtil.ioIntesivePoolSize());
	}
```

### Create task provider

```java
@Component
public class SleepyTaskProvider implements TaskProvider<Integer, Integer> {

	private SomeOtherBean someOtherBean;

	public SleepyTaskProvider(SomeOtherBean bean) {
		this.someOtherBean = bean;
	}

	public Task<Integer, Integer> provideTask(final Integer id) {

		return new Task<Integer, Integer>() {

			private static final long serialVersionUID = 1L;

			public Integer execute() {
				someOtherBean.doSomeWork();
				return id;
			}
		};
	}
}
```


### Invoke dexecutor by building it.

```java
@Component
public class ScheduledTasks {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);
	
	private ExecutorService executorService;
	private TaskProvider<Integer, Integer> taskProvider;
	
	public ScheduledTasks(ExecutorService executorService, TaskProvider<Integer, Integer> taskProvider) {
		this.executorService = executorService;
		this.taskProvider = taskProvider;
	}

	@Scheduled(fixedRate = 5000)
	public void runDexecutor() {
		LOGGER.debug("Running..");

		DefaultDexecutor<Integer, Integer> executor = newDexecutor();
		addDependency(executor);
		executor.execute(ExecutionConfig.NON_TERMINATING);
	}

	private void addDependency(DefaultDexecutor<Integer, Integer> executor) {
		executor.addDependency(1, 2);
	}

	private DefaultDexecutor<Integer, Integer> newDexecutor() {
		DexecutorConfig<Integer, Integer> config = dexConfig();
		DefaultDexecutor<Integer, Integer> executor = new DefaultDexecutor<Integer, Integer>(config);
		return executor;
	}

	private DexecutorConfig<Integer, Integer> dexConfig() {
		return new DexecutorConfig<>(executorService, taskProvider);
	}
}
```

### Console output

```bash

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.4.2)

2021-01-16 15:46:14.618  INFO 7592 --- [           main] c.g.dex.spring.DexSpringApplication      : Starting DexSpringApplication using Java 1.8.0_271 on DESKTOP-9AES3TT with PID 7592 (E:\githubRepos\dexecutor-spring-example\target\classes started by reach in E:\githubRepos\dexecutor-spring-example)
2021-01-16 15:46:14.620 DEBUG 7592 --- [           main] c.g.dex.spring.DexSpringApplication      : Running with Spring Boot v2.4.2, Spring v5.3.3
2021-01-16 15:46:14.620  INFO 7592 --- [           main] c.g.dex.spring.DexSpringApplication      : No active profile set, falling back to default profiles: default
2021-01-16 15:46:14.952  INFO 7592 --- [           main] o.s.s.c.ThreadPoolTaskScheduler          : Initializing ExecutorService 'taskScheduler'
2021-01-16 15:46:14.961 DEBUG 7592 --- [   scheduling-1] com.github.dex.spring.ScheduledTasks     : Running..
2021-01-16 15:46:14.964  INFO 7592 --- [           main] c.g.dex.spring.DexSpringApplication      : Started DexSpringApplication in 0.589 seconds (JVM running for 0.835)
2021-01-16 15:46:14.966 DEBUG 7592 --- [   scheduling-1] c.g.dexecutor.core.DefaultDexecutor      : Submitting 1 node for execution
2021-01-16 15:46:14.967 DEBUG 7592 --- [   scheduling-1] c.g.d.core.DefaultExecutionEngine        : Received Task 1 
2021-01-16 15:46:14.967 DEBUG 7592 --- [pool-1-thread-1] c.github.dexecutor.core.task.LoggerTask  : Executing Node # 1
2021-01-16 15:46:14.967  INFO 7592 --- [pool-1-thread-1] com.github.dex.spring.SomeOtherBean      : Invoked
2021-01-16 15:46:14.967 DEBUG 7592 --- [pool-1-thread-1] c.github.dexecutor.core.task.LoggerTask  : Node # 1, Execution Done!
2021-01-16 15:46:14.968 DEBUG 7592 --- [   scheduling-1] c.g.dexecutor.core.DefaultDexecutor      : Processing of node 1 done, with status SUCCESS
2021-01-16 15:46:14.968 DEBUG 7592 --- [   scheduling-1] c.g.dexecutor.core.DefaultDexecutor      : Submitting 2 node for execution
2021-01-16 15:46:14.968 DEBUG 7592 --- [   scheduling-1] c.g.d.core.DefaultExecutionEngine        : Received Task 2 
2021-01-16 15:46:14.969 DEBUG 7592 --- [pool-1-thread-2] c.github.dexecutor.core.task.LoggerTask  : Executing Node # 2
2021-01-16 15:46:14.969  INFO 7592 --- [pool-1-thread-2] com.github.dex.spring.SomeOtherBean      : Invoked
2021-01-16 15:46:14.969 DEBUG 7592 --- [pool-1-thread-2] c.github.dexecutor.core.task.LoggerTask  : Node # 2, Execution Done!
2021-01-16 15:46:14.969 DEBUG 7592 --- [   scheduling-1] c.g.dexecutor.core.DefaultDexecutor      : Processing of node 2 done, with status SUCCESS
2021-01-16 15:46:14.969 DEBUG 7592 --- [   scheduling-1] c.g.dexecutor.core.DefaultDexecutor      : Total Time taken to process 2 jobs is 4 ms.
2021-01-16 15:46:14.969 DEBUG 7592 --- [   scheduling-1] c.g.dexecutor.core.DefaultDexecutor      : Processed Nodes Ordering [1, 2]
2021-01-16 15:46:19.965 DEBUG 7592 --- [   scheduling-1] com.github.dex.spring.ScheduledTasks     : Running..
2021-01-16 15:46:19.966 DEBUG 7592 --- [   scheduling-1] c.g.dexecutor.core.DefaultDexecutor      : Submitting 1 node for execution
2021-01-16 15:46:19.966 DEBUG 7592 --- [   scheduling-1] c.g.d.core.DefaultExecutionEngine        : Received Task 1 
2021-01-16 15:46:19.967 DEBUG 7592 --- [pool-1-thread-3] c.github.dexecutor.core.task.LoggerTask  : Executing Node # 1
2021-01-16 15:46:19.967  INFO 7592 --- [pool-1-thread-3] com.github.dex.spring.SomeOtherBean      : Invoked
2021-01-16 15:46:19.968 DEBUG 7592 --- [pool-1-thread-3] c.github.dexecutor.core.task.LoggerTask  : Node # 1, Execution Done!
2021-01-16 15:46:19.968 DEBUG 7592 --- [   scheduling-1] c.g.dexecutor.core.DefaultDexecutor      : Processing of node 1 done, with status SUCCESS
2021-01-16 15:46:19.968 DEBUG 7592 --- [   scheduling-1] c.g.dexecutor.core.DefaultDexecutor      : Submitting 2 node for execution
2021-01-16 15:46:19.968 DEBUG 7592 --- [   scheduling-1] c.g.d.core.DefaultExecutionEngine        : Received Task 2 
2021-01-16 15:46:19.969 DEBUG 7592 --- [pool-1-thread-4] c.github.dexecutor.core.task.LoggerTask  : Executing Node # 2
2021-01-16 15:46:19.970  INFO 7592 --- [pool-1-thread-4] com.github.dex.spring.SomeOtherBean      : Invoked
2021-01-16 15:46:19.970 DEBUG 7592 --- [pool-1-thread-4] c.github.dexecutor.core.task.LoggerTask  : Node # 2, Execution Done!
2021-01-16 15:46:19.970 DEBUG 7592 --- [   scheduling-1] c.g.dexecutor.core.DefaultDexecutor      : Processing of node 2 done, with status SUCCESS
2021-01-16 15:46:19.971 DEBUG 7592 --- [   scheduling-1] c.g.dexecutor.core.DefaultDexecutor      : Total Time taken to process 2 jobs is 5 ms.
2021-01-16 15:46:19.971 DEBUG 7592 --- [   scheduling-1] c.g.dexecutor.core.DefaultDexecutor      : Processed Nodes Ordering [1, 2]

```