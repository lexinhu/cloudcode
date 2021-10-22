package com.xn2001.hotel.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 乐心湖
 * @version 1.0
 * @date 2021/10/18 22:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult {
    private Long total;
    private List<HotelDoc> hotels;
}
