package com.xn2001.hotel.mq;

import com.xn2001.hotel.constants.MqConstants;
import com.xn2001.hotel.service.HotelService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 乐心湖
 * @version 1.0
 * @date 2021/10/25 17:20
 */
@Component
public class HotelListener {

    @Autowired
    private HotelService hotelService;

    /**
     * 监听酒店新增或修改的业务
     * @param id 酒店id
     */
    @RabbitListener(queues = MqConstants.HOTEL_INSERT_QUEUE)
    public void listenHotelInsertOrUpdate(Long id) {
        hotelService.insertById(id);
    }

    /**
     * 监听酒店删除的业务
     * @param id 酒店id
     */
    @RabbitListener(queues = MqConstants.HOTEL_DELETE_QUEUE)
    public void listenHotelDelete(Long id) {
        hotelService.deleteById(id);
    }
}
