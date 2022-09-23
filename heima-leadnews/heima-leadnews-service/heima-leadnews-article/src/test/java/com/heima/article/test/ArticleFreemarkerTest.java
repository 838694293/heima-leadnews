package com.heima.article.test;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.ArticleApplication;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class ArticleFreemarkerTest {


    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private Configuration configuration;

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ApArticleMapper apArticleMapper;


    @Test
    public void createStaticUrlTest() throws Exception {
        //1.获取文章内容
        ApArticleContent apArticleContent = apArticleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, "1383827787629252610L"));
      if (apArticleContent!=null&&StringUtils.isNotBlank(apArticleContent.getContent())) {
          Template template = configuration.getTemplate("article.ftl");

          //数据模型
          Map<String, Object> content = new HashMap<>();
          content.put("content", JSONArray.parseArray(apArticleContent.getContent()));
          StringWriter out = new StringWriter();
          //合成
          template.process(content,out);

          //把html文件上传到minio中
          InputStream in=new ByteArrayInputStream(out.toString().getBytes());
          String path = fileStorageService.uploadHtmlFile("", apArticleContent.getArticleId() + ".html", in);

          //4.修改ap_article表，保存static_url字段
          ApArticle apArticle=new ApArticle();
          apArticle.setId(apArticleContent.getArticleId());
          apArticle.setStaticUrl(path);
          apArticleMapper.updateById(apArticle);
      }
    }
    @Test
    public void listTest(){
        List<Long> stringList = Arrays.asList(110L, 112L);
        List<Long> collect = stringList.stream().filter(s -> s != 112L).collect(Collectors.toList());
        for (Long s : collect) {
            System.out.println(s);
        }

    }



}