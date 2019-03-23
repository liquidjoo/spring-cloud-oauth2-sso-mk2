package io.bluemoon.queue;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QueueApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueueApplication.class, args);
    }

//    @Bean
//    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
//        return new QueueMessagingTemplate(amazonSQSAsync);
//    }

}
