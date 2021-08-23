package com.gzc.eshop.cache.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: KafkaConcusmer
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/12/6 22:16
 * Description:kafka消费者
 */

public class KafkaConcusmer implements Runnable {

    private ConsumerConnector consumerConnector;
    private String topic;

    public KafkaConcusmer(String topic){
        this.consumerConnector = Consumer.createJavaConsumerConnector(createConsumerConfig());
        this.topic = topic;
    }
    
    @Override
    public void run() {

        Map<String,Integer> topicCountMap = new HashMap<String,Integer>();
        //用几个线程去消费topic 这里为1
        topicCountMap.put(topic,1);
        //String代表的是topic，每个topic代表多个KafkaStream，传进去几个（topicCountMap的值）就有几个KafkaStream
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap
                = consumerConnector.createMessageStreams(topicCountMap);
        //取出topic对应的KafkaStream
        List<KafkaStream<byte[], byte[]>> steams = consumerMap.get(topic);

        //循环，创建线程处理KafkaStream里面的数据
        for (KafkaStream<byte[], byte[]> steam : steams) {
            new Thread(new KafkaMessageProcessor(steam)).start();
        }

    }

    private static ConsumerConfig createConsumerConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", "192.168.31.174:2181,192.168.31.100:2181,192.168.31.217:2181");
        props.put("group.id", "eshop-cache-group");
        props.put("zookeeper.session.timeout.ms", "40000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        return new ConsumerConfig(props);
    }
}
