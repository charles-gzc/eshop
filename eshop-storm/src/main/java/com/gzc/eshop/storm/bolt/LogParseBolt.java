package com.gzc.eshop.storm.bolt;

import com.alibaba.fastjson.JSONObject;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: LogParseBolt
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/12/24 20:33
 * Description:日志解析的bolt
 */

public class LogParseBolt extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {

        String message = tuple.getStringByField("message");
        JSONObject messageJSON = JSONObject.parseObject(message);
        JSONObject uriArgsJSON = messageJSON.getJSONObject("uri_args");
        Long productId = uriArgsJSON.getLong("productId");

        if (productId != null){
            collector.emit(new Values(productId));
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("productId"));
    }
}
