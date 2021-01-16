package com.github.dex.spring;

import org.springframework.stereotype.Component;

import com.github.dexecutor.core.task.Task;
import com.github.dexecutor.core.task.TaskProvider;

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