package app.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import app.core.filters.LoginFilter;
import app.core.utils.JwtUtil;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling
@EnableSwagger2
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public FilterRegistrationBean<LoginFilter> filterRegistrationBean(JwtUtil jwtUtil) {
		FilterRegistrationBean<LoginFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new LoginFilter(jwtUtil));
		registrationBean.addUrlPatterns("/rest/*");
		return registrationBean;
	}

}
