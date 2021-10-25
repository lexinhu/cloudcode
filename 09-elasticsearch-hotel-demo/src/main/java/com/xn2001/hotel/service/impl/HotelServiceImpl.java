package com.xn2001.hotel.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xn2001.hotel.mapper.HotelMapper;
import com.xn2001.hotel.pojo.Hotel;
import com.xn2001.hotel.pojo.HotelDoc;
import com.xn2001.hotel.pojo.PageResult;
import com.xn2001.hotel.pojo.RequestParams;
import com.xn2001.hotel.service.HotelService;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 乐心湖
 */
@Service
public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel> implements HotelService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public PageResult search(RequestParams params) {
        try {
            SearchRequest request = new SearchRequest("hotel");
            //构建复杂查询
            buildBasicQuery(params, request);
            //分页
            int page = params.getPage();
            int size = params.getSize();
            request.source().from((page - 1) * size).size(size);
            //排序
            String location = params.getLocation();
            if (!StringUtils.isEmpty(location)) {
                request.source().sort(SortBuilders
                        .geoDistanceSort("location", new GeoPoint(location))
                        .order(SortOrder.ASC)
                        .unit(DistanceUnit.KILOMETERS)
                );
            }
            //请求
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            return handleResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, List<String>> filters(RequestParams params) {
        SearchRequest request = new SearchRequest("hotel");
        try {
            //构建复杂查询
            buildBasicQuery(params, request);
            request.source().size(0);
            //构建聚合查询
            buildAggregation(request);
            //发送请求
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            //解析聚合结果
            List<String> brand = getAggByName(response.getAggregations(), "brandAgg");
            List<String> city = getAggByName(response.getAggregations(), "cityAgg");
            List<String> starName = getAggByName(response.getAggregations(), "starAgg");
            HashMap<String, List<String>> result = new HashMap<>();
            //返回
            result.put("brand", brand);
            result.put("city", city);
            result.put("starName", starName);
            return result;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public List<String> getSuggestions(String prefix) {

        try {
            SearchRequest request = new SearchRequest("hotel");
            //搜索建议
            request.source().suggest(new SuggestBuilder().addSuggestion("suggestions",
                    SuggestBuilders
                            .completionSuggestion("suggestion")
                            .prefix(prefix)
                            .skipDuplicates(true)
                            .size(10))
            );
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            Suggest suggest = response.getSuggest();
            //解析响应
            CompletionSuggestion suggestions = suggest.getSuggestion("suggestions");
            List<CompletionSuggestion.Entry.Option> options = suggestions.getOptions();
            List<String> list = new ArrayList<>(options.size());
            for (CompletionSuggestion.Entry.Option option : options) {
                String text = option.getText().toString();
                list.add(text);
            }
            return list;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void insertById(Long id) {
        try {
            // 根据id查询酒店数据
            Hotel hotel = getById(id);
            // 转换为文档类型
            HotelDoc hotelDoc = new HotelDoc(hotel);

            IndexRequest request = new IndexRequest("hotel").id(hotel.getId().toString());
            request.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            DeleteRequest request = new DeleteRequest("hotel", id.toString());
            client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void buildAggregation(SearchRequest request) {
        request.source()
                .aggregation(AggregationBuilders.terms("brandAgg").field("brand").size(100))
                .aggregation(AggregationBuilders.terms("cityAgg").field("city").size(100))
                .aggregation(AggregationBuilders.terms("starAgg").field("starName").size(100)
                );
    }

    private List<String> getAggByName(Aggregations aggregations, String aggName) {
        Terms terms = aggregations.get(aggName);
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        List<String> list = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            String key = bucket.getKeyAsString();
            list.add(key);
        }
        return list;
    }

    private void buildBasicQuery(RequestParams params, SearchRequest request) {
        //构建组合查询BooleanQuery
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //关键字搜索
        String key = params.getKey();
        if (StringUtils.isEmpty(key)) {
            boolQuery.must(QueryBuilders.matchAllQuery());
        } else {
            boolQuery.must(QueryBuilders.matchQuery("all", key));
        }
        //城市过滤
        if (!StringUtils.isEmpty(params.getCity()))
            boolQuery.filter(QueryBuilders.termQuery("city", params.getCity()));
        //品牌过滤
        if (!StringUtils.isEmpty(params.getBrand()))
            boolQuery.filter(QueryBuilders.termQuery("brand", params.getBrand()));
        //星级过滤
        if (!StringUtils.isEmpty(params.getStarName()))
            boolQuery.filter(QueryBuilders.termQuery("starName", params.getStarName()));
        //价格过滤
        if (params.getMaxPrice() != null && params.getMinPrice() != null)
            boolQuery.filter(QueryBuilders.rangeQuery("price").gte(params.getMinPrice()).lte(params.getMaxPrice()));

        //算分
        FunctionScoreQueryBuilder functionScoreQuery = QueryBuilders.functionScoreQuery(
                //原始查询
                boolQuery,
                //function score
                new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                                QueryBuilders.termQuery("isAD", true),
                                //算法函数 x10
                                ScoreFunctionBuilders.weightFactorFunction(10)
                        )
                });
        //丢回去查询条件
        request.source().query(functionScoreQuery);
    }

    private PageResult handleResponse(SearchResponse response) {
        SearchHits searchHits = response.getHits();
        // 获取总条数
        long total = searchHits.getTotalHits().value;
        SearchHit[] hits = searchHits.getHits();
        // 返回搜索的结果
        ArrayList<HotelDoc> hotelDocs = new ArrayList<>();
        // 遍历
        for (SearchHit hit : hits) {
            // 获取文档source
            String json = hit.getSourceAsString();
            // 反序列化
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            // 获取排序值（即为距离）
            Object[] sortValues = hit.getSortValues();
            if (sortValues.length > 0) {
                Object sortValue = sortValues[0];
                hotelDoc.setDistance(sortValue);
            }
            hotelDocs.add(hotelDoc);
        }
        return new PageResult(total, hotelDocs);
    }

}
