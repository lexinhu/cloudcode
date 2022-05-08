package com.xn2001.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xn2001.hotel.pojo.Hotel;
import com.xn2001.hotel.pojo.PageResult;
import com.xn2001.hotel.pojo.RequestParams;

import java.util.List;
import java.util.Map;

public interface HotelService extends IService<Hotel> {
     PageResult search(RequestParams params);

     Map<String, List<String>> filters(RequestParams params);

     List<String> getSuggestions(String prefix);

     void insertById(Long id);

     void deleteById(Long id);
}
