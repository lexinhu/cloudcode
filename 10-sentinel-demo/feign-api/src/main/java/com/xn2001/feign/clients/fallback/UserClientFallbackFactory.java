package com.xn2001.feign.clients.fallback;

import com.xn2001.feign.clients.UserClient;
import com.xn2001.feign.pojo.User;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 乐心湖
 * @version 1.0
 * @date 2022/5/9 14:24
 */

@Slf4j
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable throwable) {
       return userClient -> {
           log.error("查询用户失败",throwable);
           return new User();
       };
    }
}
