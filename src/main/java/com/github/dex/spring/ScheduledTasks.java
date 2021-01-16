package com.github.dex.spring;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.dexecutor.core.DefaultDexecutor;
import com.github.dexecutor.core.DexecutorConfig;
import com.github.dexecutor.core.ExecutionConfig;
import com.github.dexecutor.core.task.TaskProvider;

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