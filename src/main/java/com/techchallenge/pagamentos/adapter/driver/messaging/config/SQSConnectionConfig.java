package com.techchallenge.pagamentos.adapter.driver.messaging.config;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

@Configuration
@EnableJms
public class SQSConnectionConfig {

	@Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.queue.uri}")
    private String sqsUrl;
    
    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public ConnectionFactory connectionFactory() {
        return SQSConnectionFactory.builder()
                .withRegion(Region.getRegion(Regions.SA_EAST_1))
                .withEndpoint(sqsUrl)
                .withAWSCredentialsProvider(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey,secretKey)))
                .build();
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(this.connectionFactory);
        return factory;
    }
    
    @Bean
    public JmsTemplate defaultJmsTemplate(){
        return new JmsTemplate(this.connectionFactory);
    }
}
