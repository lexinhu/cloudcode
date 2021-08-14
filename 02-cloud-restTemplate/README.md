# 远程调用

> 案例源码：https://gitee.com/xn2001/cloudcode/tree/master/02-cloud-restTemplate

正如上面的服务拆分要求中所提到，

> 订单服务如果需要查询用户信息，**只能调用用户服务的 Restful 接口**，不能查询用户数据库

因此我们需要知道 Java 如何去发送 http 请求，Spring 提供了一个 RestTemplate 工具，只需要把它创建出来即可。（即注入 Bean）

![](README.assets/image-20210814202949349.png)

发送请求，自动序列化为 Java 对象。

![](README.assets/image-20210814210007924.png)

启动完成后，访问：http://localhost:8080/order/101

![](README.assets/image-20210815023329690.png)

在上面代码的 url 中，我们可以发现调用服务的地址采用硬编码，这在后续的开发中肯定是不理想的，这就需要**服务注册中心**（Eureka）来帮我们解决这个事情。