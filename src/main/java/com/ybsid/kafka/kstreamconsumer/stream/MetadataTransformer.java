package com.ybsid.kafka.kstreamconsumer.stream;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.TransformerSupplier;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.ZoneId;

public class MetadataTransformer implements TransformerSupplier<String,String, KeyValue<String,String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataTransformer.class);

    @Override
    public Transformer<String , String , KeyValue<String,String>> get(){
       return new Transformer<>() {

           ProcessorContext processorContext;

           @Override
           public void init(ProcessorContext context) {
               this.processorContext = context;

           }

           @Override
           public KeyValue<String, String> transform(String key, String value) {
               if(key!=null) {
                   int partition = processorContext.partition();
                   long offset = processorContext.offset();
                   String topic = processorContext.topic();


                   String time = new Timestamp(processorContext.timestamp())
                           .toLocalDateTime()
                           .atZone(ZoneId.of("Asia/Kolkata")).toString();

                   LOGGER.info("Meta-Data of topic : Partition :{} , offset : {} ,  topic : {} and timestamp : "
                           , partition,offset , topic , time);

                   processorContext.commit();

               } else {
                   LOGGER.info("key is null");
               }

               return new KeyValue<>(key,value);
           }

           @Override
           public void close() {

               //TODO , one close of Metadata transformer close

           }
       };
    }
}
