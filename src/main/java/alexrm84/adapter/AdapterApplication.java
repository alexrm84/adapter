package alexrm84.adapter;

import org.apache.camel.builder.DeadLetterChannelBuilder;
import org.apache.camel.processor.errorhandler.RedeliveryPolicy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@ComponentScan("alexrm84.adapter")
public class AdapterApplication {
	public static void main(String[] args) {
		SpringApplication.run(AdapterApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
