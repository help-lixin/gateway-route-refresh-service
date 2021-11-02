package help.lixin.gateway.refresh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@SpringBootConfiguration
@EnableAutoConfiguration
public class GatewayApplication {
	public static void main(String[] args) {
//		System.setProperty("spring.profiles.active","apollo");
		SpringApplication.run(GatewayApplication.class, args);
	}
}
