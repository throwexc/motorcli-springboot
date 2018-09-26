package com.motorcli.springboot.mongodb.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PreDestroy;
import java.net.UnknownHostException;

@Configuration
@ConditionalOnClass({MongoClient.class})
@EnableConfigurationProperties({MotorCLIMongoProperties.class})
@ConditionalOnMissingBean(
        type = {"org.springframework.data.mongodb.MongoDbFactory"}
)
public class MotorCLIMongoDBAutoConfiguration {

    private final MotorCLIMongoProperties properties;

    private final MongoClientOptions options;

    private final Environment environment;
    private MongoClient mongo;

    public MotorCLIMongoDBAutoConfiguration(MotorCLIMongoProperties properties, ObjectProvider<MongoClientOptions> options, Environment environment) {
        this.properties = properties;
        this.options = options.getIfAvailable();
        this.environment = environment;
    }

    @PreDestroy
    public void close() {
        if(this.mongo != null) {
            this.mongo.close();
        }

    }

    @Bean
    @ConditionalOnMissingBean
    public MongoClient mongo() throws UnknownHostException {
        this.mongo = this.properties.createMongoClient(this.options, this.environment);
        return this.mongo;
    }
}
