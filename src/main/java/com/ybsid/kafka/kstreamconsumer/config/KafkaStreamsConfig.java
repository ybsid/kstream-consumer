package com.ybsid.kafka.kstreamconsumer.config;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class KafkaStreamsConfig {

    private static final Map<String,Object> DEFAULTS = ImmutableMap.of();
    private String bootstrapServers;
    private String schemaRegistryUrl;
    private String stateDir;
    private String applicationId;
    private String autoOffsetReset = "latest";
    private String commitInterval;
    private String replicationFactor="3";
    private String numOfStreamThreads="1";
    private boolean manualPartitioningEnable=false;
    private String manualPartitioningPodOrdinal;
    private String manualPartitioningTotalReplica;
    private String securityProtocol;
    private String sslTruststoreLocation;
    private String sslKeystoreLocation;
    private String sslTruststorePassword;
    private String sslKeystorePassword;
    private String clientId;
    private String metricsRecordLevel;
    private String deadLetterTopic;
    private int reconnectBackoffMs;
    private int reconnectBackoffMaxMs;
    private String lingerMS;
    private String acks="all";
    private String encryptedSerializerKeyPath;
    private String encryptedDeserializerKeyPath;
    private boolean useDeadLetterExceptionHandler = true;
    private String baseAvroModelPackage = "com.ybsid.kafka" ; //TODO : need to update path as per avro POJO models , currently not present
    private Map<String,Object> custom = new HashMap<>();


    public KafkaStreamsConfig(){}

    protected Map<String,Object> moreConfigs() {
        return ImmutableMap.of();
    }

    private boolean putIfNotNull(Map<String,Object> map , String key , Object value){
        if(key!=null && value!=null){
            map.put(key,value);
            return true;
        } else {
            return false;
        }
    }

    public Map<String,Object> allConfigs(){
        Map<String,Object> configs = new HashMap<>(DEFAULTS);
        this.putIfNotNull(configs,"bootstrap.servers",this.bootstrapServers);
        this.putIfNotNull(configs,"schema.registry.url",this.schemaRegistryUrl);
        this.putIfNotNull(configs,"topology.optimization","all");
        this.putIfNotNull(configs,"state.dir",this.stateDir);
        this.putIfNotNull(configs,"application.id",this.applicationId);
        this.putIfNotNull(configs,"auto.offset.reset",this.autoOffsetReset);
        this.putIfNotNull(configs,"replication.factor",this.replicationFactor);
        this.putIfNotNull(configs,"commit.interval.ms",this.commitInterval);
        this.putIfNotNull(configs,"ssl.truststore.location",this.sslTruststoreLocation);
        this.putIfNotNull(configs,"ssl.truststore.password",this.sslTruststorePassword);
        this.putIfNotNull(configs,"ssl.keystore.location",this.sslKeystoreLocation);
        this.putIfNotNull(configs,"ssl.keystore.password",this.sslKeystorePassword);
        this.putIfNotNull(configs,"client.id",this.clientId);
        this.putIfNotNull(configs,"metrics.recording.level",this.metricsRecordLevel);
        this.putIfNotNull(configs,"reconnect.backoff.ms",this.reconnectBackoffMs);
        this.putIfNotNull(configs,"reconnect.backoff.max.ms",this.reconnectBackoffMaxMs);
        this.putIfNotNull(configs,"num.stream.threads",this.numOfStreamThreads);
        this.putIfNotNull(configs,"linger.ms",this.lingerMS);
        this.putIfNotNull(configs,"acks",this.acks);
        this.putIfNotNull(configs,"encrypted.serializer.key.path",this.encryptedSerializerKeyPath);
        this.putIfNotNull(configs,"encrypted.deserializer.key.path",this.encryptedDeserializerKeyPath);
        this.putIfNotNull(configs,"specific.avro.base.package",this.baseAvroModelPackage);
        this.putIfNotNull(configs,"manualPartitioning.enabled",this.manualPartitioningEnable);
        this.putIfNotNull(configs,"manualPartitioning.podOrdinal",this.manualPartitioningPodOrdinal);
        this.putIfNotNull(configs,"manualPartitioning.totalReplicas",this.manualPartitioningTotalReplica);

        configs.putAll(this.moreConfigs());
        configs.putAll(this.custom);
        configs.values().removeIf(Objects::isNull);
        return configs;

    }
}
