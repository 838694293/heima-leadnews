package com.heima.search.listener;


import com.alibaba.fastjson.JSON;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.search.dtos.SearchArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class SyncArticleListener {

    @Autowired
    private RestHighLevelClient highLevelClient;

    @KafkaListener(topics = ArticleConstants.ARTICLE_ES_SYNC_TOPIC)
    public void sendOnMessage(String message){

        if (StringUtils.isNotBlank(message)) {
            log.info("SyncArticleListener,message={}", message);
            IndexRequest indexRequest = new IndexRequest("app_info_article");
            SearchArticleVo vo = JSON.parseObject(message, SearchArticleVo.class);
            indexRequest.id(vo.getId().toString());
            indexRequest.source(message, XContentType.JSON);
            try {
                highLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Sync es error={}",e);
            }

        }
    }
}
