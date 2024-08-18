package me.amlu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"me.amlu"})
public class OnlineFoodOrderingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineFoodOrderingApplication.class, args);

		// Log the available beans
//	ConfigurableApplicationContext context = SpringApplication.run(OnlineFoodOrderingApplication.class, args);
//        System.out.println("Available beans:");
//        for (String beanName : context.getBeanDefinitionNames()) {
//		System.out.println(beanName);
//	}

}
}

