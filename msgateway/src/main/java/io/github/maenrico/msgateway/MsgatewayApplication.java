package io.github.maenrico.msgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
@EnableEurekaClient
public class MsgatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsgatewayApplication.class, args);
	}

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder){
		return builder
				.routes()
					.route("cliente", c -> c.path("/cliente/**").uri("lb://msclientes"))
					.route("cartoes", c -> c.path("/cartoes/**").uri("lb://mscartoes"))
				.build();
	}

}
