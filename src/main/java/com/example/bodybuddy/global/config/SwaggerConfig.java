package com.example.bodybuddy.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI LckAPI() {
		Info info = new Info()
			.title("BodyBuddy Server API")
			.description("BodyBuddy API 명세서")
			.version("1.0.0");

		final String JWT_SCHEME = "JWT Token";

		// SecuritySchemes 생성
		SecurityScheme scheme = new SecurityScheme()
			.name(JWT_SCHEME)
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER);

		// API 요청헤더에 인증정보 포함
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(JWT_SCHEME);

		return new OpenAPI()
			.addServersItem(new Server().url("/"))
			.info(info)
			.components(new Components().addSecuritySchemes(JWT_SCHEME, scheme))
			.security(List.of(securityRequirement));
	}
}