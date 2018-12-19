package org.turnera.server.config;

import io.undertow.Undertow;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UndertowConfiguration {
	@Bean
	public UndertowServletWebServerFactory servletWebServerFactory() {
		UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
		factory.addBuilderCustomizers(new UndertowBuilderCustomizer() {

			@Override
			public void customize(Undertow.Builder builder) {
				builder.addHttpListener(8080, "0.0.0.0");
			}

		});
		return factory;
	}
}
