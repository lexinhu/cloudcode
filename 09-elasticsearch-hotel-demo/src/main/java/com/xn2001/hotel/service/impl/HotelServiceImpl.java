package com.xn2001.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xn2001.hotel.mapper.HotelMapper;
import com.xn2001.hotel.pojo.Hotel;
import com.xn2001.hotel.service.IHotelService;
import org.springframework.stereotype.Service;

@Service
public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {
}
