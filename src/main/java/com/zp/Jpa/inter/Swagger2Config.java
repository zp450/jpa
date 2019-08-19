package com.zp.Jpa.inter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Swagger2Config {
	@Bean
	public Docket createRestApi() {
	//扫描控制器中Swagger2的注解
	return new Docket(DocumentationType.SWAGGER_2)
	.apiInfo(apiInfo())
	.select()
	.apis(RequestHandlerSelectors.basePackage("com.zp.Jpa.controller"))//指定包
	.paths(PathSelectors.any())
	.build();
	}
	//API描述信息
	private ApiInfo apiInfo() {
	return new ApiInfoBuilder()
	.title("爱邮吉游API接口文档")
	.description("java后台模块")
	.termsOfServiceUrl("http://blog.csdn.net/saytime")
	.version("1.0")
	.build();
	}
}
