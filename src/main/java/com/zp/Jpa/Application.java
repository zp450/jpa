package com.zp.Jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableJpaRepositories
@SpringBootApplication
@EnableTransactionManagement //启动事务
@EnableSwagger2 //启用Swagger2


//@EntityScan
//异步执行多线程
//@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)//因为有些service使用了CGLib这种动态代理而不是JDK原生的代理，所以导致问题的出现。
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println("项目启动成功");
	}
}