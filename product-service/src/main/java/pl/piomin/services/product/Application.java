package pl.piomin.services.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.support.GenericMessage;
import pl.piomin.service.common.message.*;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@SpringBootApplication
@EnableBinding(Source.class)
public class Application {

    @Autowired
    private ProductService productService;

    private int index = 0;

    protected Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "10000", maxMessagesPerPoll = "1"))
    public MessageSource processOrder() {
        return () -> {
            Order order = new Order(index++, OrderType.PURCHASE, LocalDateTime.now(), OrderStatus.NEW, new Product("Example#2"), new Shipment(ShipmentType.SHIP));
            productService.processOrder(order);
            logger.info("Sending order: " + order);
            return new GenericMessage<>(order);
        };
    }

}
