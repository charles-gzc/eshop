package com.gzc.eshop.storm.spout;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: AccessLogKafkaSpout
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/12/24 20:21
 * Description: kafka消费数据的spout
 */

public class AccessLogKafkaSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;

    private  ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(1000);

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        startKafkaConsumer();
    }

    private void startKafkaConsumer(){
        Properties props = new Properties();
        props.put("zookeeper.connect", "192.168.31.174:2181,192.168.31.100:2181,192.168.31.217:2181");
        props.put("group.id", "eshop-cache-group");
        props.put("zookeeper.session.timeout.ms", "40000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        ConsumerConfig config = new ConsumerConfig(props);

        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(config);
        String topic = "access-log";

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

    private class KafkaMessageProcessor implements Runnable{

        private KafkaStream kafkaStream;

        public KafkaMessageProcessor(KafkaStream kafkaStream){
            this.kafkaStream = kafkaStream;
        }

        @Override
        public void run() {
            ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
            while (it.hasNext()){
                String message = new String(it.next().message());
                try {
                    queue.put(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void nextTuple() {

        if (queue.size() > 0){
            try {
                String message = queue.take();
                collector.emit(new Values(message));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            Utils.sleep(100);
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("message"));
    }
}
