package com.xn2001.hotel;

import com.xn2001.hotel.constants.HotelConstants;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author 乐心湖
 * @version 1.0
 * @date 2021/9/19 17:18
 */
public class HotelIndexTest {

    private RestHighLevelClient restHighLevelClient;

    @Test
    void testInit(){
        System.out.println(this.restHighLevelClient);
    }

    @Test
    void createHotelIndex() throws IOException {
        //指定索引库名
        CreateIndexRequest hotel = new CreateIndexRequest("hotel");
        //写入JSON数据，这里是Mapping映射
        hotel.source(HotelConstants.MAPPING_TEMPLATE, XContentType.JSON);
        //创建索引库
        restHighLevelClient.indices().create(hotel, RequestOptions.DEFAULT);
    }

    @Test
    void deleteHotelIndex() throws IOException {
        DeleteIndexRequest hotel = new DeleteIndexRequest("hotel");
        restHighLevelClient.indices().delete(hotel,RequestOptions.DEFAULT);
    }

    @Test
    void existHotelIndex() throws IOException {
        GetIndexRequest hotel = new GetIndexRequest("hotel");
        boolean exists = restHighLevelClient.indices().exists(hotel, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    @BeforeEach
    void init(){
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.211.128:9200")
        ));
    }

    @AfterEach
    void down() throws IOException {
        this.restHighLevelClient.close();
    }
}
