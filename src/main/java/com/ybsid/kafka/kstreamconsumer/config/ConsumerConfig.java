package com.ybsid.kafka.kstreamconsumer.config;


import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Configuration
@ConfigurationProperties("stream.topicInfo")
@Data
public class ConsumerConfig extends KafkaStreamsConfig{

    @NotNull
    private String topicName;


    @Override
    protected Map<String,Object> moreConfigs(){
        // assigns default key , value serdes class
        return ImmutableMap.of(
                StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName(),
                StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass().getName()
        );
    }

}
