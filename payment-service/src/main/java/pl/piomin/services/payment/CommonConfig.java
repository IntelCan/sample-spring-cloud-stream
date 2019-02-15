package pl.piomin.services.payment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.piomin.service.common.error.ErrorGenerator;

import java.util.Random;

@Configuration
public class CommonConfig {

    @Bean
    public ErrorGenerator errorGenerator() {
        return new ErrorGenerator(new CustomRandom());
    }

}
