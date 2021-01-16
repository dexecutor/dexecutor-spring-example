package com.github.dex.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SomeOtherBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(SomeOtherBean.class);

	public void doSomeWork() {
		LOGGER.info("Invoked");
	}
}
