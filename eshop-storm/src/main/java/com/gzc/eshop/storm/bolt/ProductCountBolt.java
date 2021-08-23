package com.gzc.eshop.storm.bolt;

import com.alibaba.fastjson.JSONArray;
import com.gzc.eshop.storm.zk.ZookeeperSession;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.trident.util.LRUMap;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.utils.Utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: ProductCountBolt
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/12/24 20:43
 * Description:商品访问次数统计bolt
 */

public class ProductCountBolt extends BaseRichBolt {

    private LRUMap<Long,Long> productCountMap = new LRUMap<Long,Long>(1000);
    private ZookeeperSession zkSession;
    private int taskId;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.zkSession = ZookeeperSession.getInstance();
        this.taskId = context.getThisTaskId();
        new Thread(new ProductCountThread()).start();
        initTaskId(taskId);
    }

    private void initTaskId(int taskId){

        zkSession.acquireDistributedLock();
        String taskIdList = zkSession.getNodeData();
        if (!"".equals(taskIdList)){
            taskIdList += "," + taskId;
        }else {
            taskIdList += taskId;
        }
        zkSession.setNodeData("/taskid-list",taskIdList);
        zkSession.releaseDistributedLock();
    }

    private class ProductCountThread implements Runnable{


        @Override
        public void run() {

            ArrayList<Entry<Long, Long>> topnProductList = new ArrayList<Entry<Long, Long>>();

            while (true){
                topnProductList.clear();

                int topn = 3;

                for (Map.Entry<Long,Long> productCountEntry : productCountMap.entrySet()){

                    if (topnProductList.size() == 0){
                        topnProductList.add(productCountEntry);
                    } else {
                        boolean bigger = false;

                        for (int i = 0; i < topnProductList.size(); i++) {
                            Entry<Long, Long> topnProductCountEntry = topnProductList.get(i);

                            if (productCountEntry.getValue() > topnProductCountEntry.getValue()){

                                int lastIndex = topnProductList.size() < topn ?topnProductList.size() -1 : topn - 2;

                                for(int j = lastIndex ; j >= i ; j--){
                                    topnProductList.set(j + 1,topnProductList.get(j));
                                }
                                topnProductList.set(i,productCountEntry);
                                bigger = true;
                                break;
                            }
                        }

                        if (!bigger){
                            if (topnProductList.size() < topn){
                                topnProductList.add(productCountEntry);
                            }
                        }
                    }
                }
                String topnProductListJSON = JSONArray.toJSONString(topnProductList);
                zkSession.setNodeData("/task-hot-product-list-" + taskId,topnProductListJSON);
                Utils.sleep(60000);
            }
        }
    }

    @Override
    public void execute(Tuple tuple) {

        Long productId = tuple.getLongByField("productId");
        Long count = productCountMap.get(productId);

        if (count == null){
            count = 0L;
        }

        count++;

        productCountMap.put(productId,count);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
