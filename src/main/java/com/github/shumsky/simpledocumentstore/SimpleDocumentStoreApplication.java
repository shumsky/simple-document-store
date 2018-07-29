package com.github.shumsky.simpledocumentstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SimpleDocumentStoreApplication {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleDocumentStoreApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder()
				.sources(SimpleDocumentStoreApplication.class)
				.bannerMode(Banner.Mode.OFF)
				.logStartupInfo(false)
				.run(args);

		LOG.info("Listening on port " + context.getEnvironment().getProperty("server.port"));
	}
}
