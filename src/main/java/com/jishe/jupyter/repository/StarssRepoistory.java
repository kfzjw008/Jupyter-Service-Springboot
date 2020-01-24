package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.Questions;
import com.jishe.jupyter.entity.Starss;
import org.aspectj.lang.annotation.Before;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.transport.client.PreBuiltTransportClient;


import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.net.InetAddress;
import java.util.List;

/**
 * @program: jupyter
 * @description: 用于elasticSearch的检索
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-21 23:04
 **/
public  class StarssRepoistory  {
    TransportClient client;
//Page<Starss> findByName(String name, Pageable pageable);
    public  void init() throws Exception {

    //创建一个Settings对象
    Settings settings = Settings.builder()
            .put("cluster.name", "my-elasticsearch")
            .build();
    //创建一个TransPortClient对象
    client = new PreBuiltTransportClient(settings)
            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9301))
            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9302))
            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9303));
}

    private  Map search(QueryBuilder queryBuilder,int page) throws Exception {
        //执行查询
        Map<Object, Object> AllDataMap = new HashMap<Object, Object>();

        SearchResponse searchResponse = client.prepareSearch("stellar_data")
                .setTypes("Starss")
                .setQuery(queryBuilder)
                //设置分页信息
                .setFrom(page)
                //每页显示的行数
                .setSize(10)
                .get();
        //取查询结果
        SearchHits searchHits = searchResponse.getHits();
        //取查询结果的总记录数
        System.out.println("查询结果总记录数：" + searchHits.getTotalHits());
        AllDataMap.put("count", searchHits.getTotalHits());
        //查询结果列表
        int i=0;
        Iterator<SearchHit> iterator = searchHits.iterator();
        while(iterator.hasNext()) {
            i++;
            Map<Object, Object> BasicDataMap = new HashMap<Object, Object>();
            SearchHit searchHit = iterator.next();
            //打印文档对象，以json格式输出
            System.out.println(searchHit.getSourceAsString());
            //取文档的属性
            System.out.println("-----------文档的属性");
            Map<String, Object> document = searchHit.getSource();
            BasicDataMap.put("id", document.get("id"));
            BasicDataMap.put("name", document.get("name"));
            BasicDataMap.put("bayer", document.get("bayer"));
            BasicDataMap.put("fransted", document.get("fransted"));
            BasicDataMap.put("variable_star", document.get("variable_star"));
            BasicDataMap.put("hd", document.get("hd"));
            BasicDataMap.put("hip", document.get("hip"));
            BasicDataMap.put("right_ascension", document.get("right_ascension"));
            BasicDataMap.put("declination", document.get("declination"));
            BasicDataMap.put("apparent_magnitude", document.get("apparent_magnitude"));
            BasicDataMap.put("absolute_magnitude", document.get("absolute_magnitude"));
            BasicDataMap.put("distance", document.get("distance"));
            BasicDataMap.put("classification", document.get("classification"));
            BasicDataMap.put("notes", document.get("notes"));
            BasicDataMap.put("constellation", document.get("constellation"));
            BasicDataMap.put("ancient_chinese_name", document.get("ancient_chinese_name"));
            AllDataMap.put("Data"+i, BasicDataMap);
            System.out.println(document.get("id"));
            System.out.println(document.get("name"));
            System.out.println(document.get("bayer"));

        }
        //关闭client
       // client.close();
        return AllDataMap;
    }
    public Map testQueryStringQuery(String string,int page) throws Exception {
        //创建一个QueryBuilder对象
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(string, "name", "ancient_chinese_name", "absolute_magnitude");

        //执行查询
       return search(queryBuilder,page);
    }

}
