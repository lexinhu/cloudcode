package com.xn2001.hotel;

import com.alibaba.fastjson.JSON;
import com.xn2001.hotel.pojo.Hotel;
import com.xn2001.hotel.pojo.HotelDoc;
import com.xn2001.hotel.service.IHotelService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * @author 乐心湖
 * @version 1.0
 * @date 2021/9/19 17:18
 */
@SpringBootTest
public class HotelDocumentTest {

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private IHotelService hotelService;

    @Test
    void testInit() {
        System.out.println(this.restHighLevelClient);
    }

    @Test
    void createHotelIndex() throws IOException {
        Hotel hotel = hotelService.getById(61083L);
        HotelDoc hotelDoc = new HotelDoc(hotel);
        // 1.准备Request对象
        IndexRequest hotelIndex = new IndexRequest("hotel").id(hotelDoc.getId().toString());
        // 2.准备Json文档
        hotelIndex.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
        // 3.发送请求
        restHighLevelClient.index(hotelIndex, RequestOptions.DEFAULT);
    }

    @Test
    void testGetDocumentById() throws IOException {
        // 1.准备Request
        GetRequest hotel = new GetRequest("hotel", "61083");
        // 2.发送请求，得到响应
        GetResponse hotelResponse = restHighLevelClient.get(hotel, RequestOptions.DEFAULT);
        // 3.解析响应结果
        String hotelDocSourceAsString = hotelResponse.getSourceAsString();
        // 4.json转实体类
        HotelDoc hotelDoc = JSON.parseObject(hotelDocSourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }

    @Test
    void testDeleteDocumentById() throws IOException {
        DeleteRequest hotel = new DeleteRequest("hotel", "61083");
        restHighLevelClient.delete(hotel, RequestOptions.DEFAULT);
    }

    @Test
    void testUpdateDocument() throws IOException {
        // 1.准备Request
        UpdateRequest request = new UpdateRequest("hotel", "61083");
        // 2.准备请求参数
        request.doc(
                "price", "952",
                "starName", "四钻"
        );
        // 3.发送请求
        restHighLevelClient.update(request, RequestOptions.DEFAULT);
    }

    @Test
    void testBulk() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        List<Hotel> hotelList = hotelService.list();
        hotelList.forEach(item -> {
            HotelDoc hotelDoc = new HotelDoc(item);
            bulkRequest.add(new IndexRequest("hotel")
                    .id(hotelDoc.getId().toString())
                    .source(JSON.toJSONString(hotelDoc), XContentType.JSON));
        });
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    @BeforeEach
    void init() {
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.211.128:9200")
        ));
    }

    @AfterEach
    void down() throws IOException {
        this.restHighLevelClient.close();
    }
}
