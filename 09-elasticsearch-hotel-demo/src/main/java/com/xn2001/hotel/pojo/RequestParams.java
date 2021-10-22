package com.xn2001.hotel.pojo;

import lombok.Data;

/**
 * @author 乐心湖
 * @version 1.0
 * @date 2021/10/18 22:42
 */
@Data
public class RequestParams {
    private String key;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String city;
    private String brand;
    private String starName;
    private Integer minPrice;
    private Integer maxPrice;
    private String location;
}
