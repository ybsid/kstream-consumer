package com.ybsid.kafka.kstreamconsumer.stream;


import com.ybsid.kafka.kstreamconsumer.config.ConsumerConfig;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class ConsumerStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerStream.class);

    @Autowired
    private ConsumerConfig consumerConfig;

    @Bean
    RetryTemplate retryTemplate(){
        RetryTemplate template = new RetryTemplate();

        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(10);
        template.setRetryPolicy(policy);
        template.setThrowLastExceptionOnExhausted(true);

        return template;
    }

    /**
     * KStream class , uses @Bean annotation to start up
     * consumer at startup
     */
    @Bean
    public KStream<String,String> streamConsumer(StreamsBuilder builder){
        KStream<String,String> stream = builder.stream(consumerConfig.getTopicName()
                , Consumed.with(Serdes.String(),Serdes.String()));

        stream.map((key,value)-> {
            final int[] retryCount = {0};

            try{
                // TODO : define and call a processor method

                return KeyValue.pair(key,value);
            } catch(Exception e ){
                LOGGER.error("Error in processing StackTrace  : {} ", ExceptionUtils.getStackTrace(e));
                LOGGER.info("Proceeding with Retry");

                try{
                    retryTemplate().execute(context -> {
                        retryCount[0]++;

                        LOGGER.info("***********Retry Count : {}",retryCount[0]);

                        // TODO : call same processor method here

                        return KeyValue.pair(key,value);
                    }) ;
                } catch (Exception ex ){
                    LOGGER.error("Exception caught after 10 retries {}",ExceptionUtils.getStackTrace(ex));

                    return KeyValue.pair(key,value);
                }
            }
            return KeyValue.pair(key,value);
        }).transform(new MetadataTransformer()) ;

        return stream;
    }

}
