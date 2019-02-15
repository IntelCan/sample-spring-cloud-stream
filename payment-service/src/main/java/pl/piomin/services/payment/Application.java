package pl.piomin.services.payment;

import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Header;
import pl.piomin.service.common.error.ErrorGenerator;
import pl.piomin.service.common.message.Order;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@SpringBootApplication
@EnableBinding(Sink.class)
public class Application {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ErrorGenerator errorGenerator;

    protected Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @StreamListener(Sink.INPUT)
    public void processOrder(Order order,
                             @Header(name = "x-death", required = false) Map<?, ?> deathHeaders) {
        logger.info("Processing order: " + order);
        checkDeathCount(deathHeaders);
        Order o = paymentService.processOrder(order);
        errorGenerator.sometimesGenerateErrorFor(order);
        if (o != null)
            logger.info("Final response: " + (o.getProduct().getPrice() + o.getShipment().getPrice()));
    }

    private void checkDeathCount(Map<?, ?> deathHeaders) {
        Optional.ofNullable(deathHeaders).ifPresent(deaths ->  {
            long counter = Long.valueOf(deaths.get("count").toString());
            if (counter > 5) {
                throw new ImmediateAcknowledgeAmqpException("Failed after 5 attempts");
            }
        });
    }
}
