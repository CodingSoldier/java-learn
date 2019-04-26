package com.coq.rabbitmq.springboot.producer;


import com.example.rabbitmqbean.MyOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * ip网段对应地址 前端控制器
 * </p>
 *
 * @author chenpiqian
 * @since 2019-04-24
 */
@RestController
@RequestMapping("/producer")
public class ProducerController {

    @Autowired
    RabbitSender rabbitSender;

    @GetMapping("/send")
    public String loginSend(@RequestParam("id") String id) throws Exception{
        MyOrder order = new MyOrder(id, "第"+id+"个订单");
        rabbitSender.sendOrder(order);
        return "OK";
    }

    @GetMapping("/sendDead")
    public String sendDead(@RequestParam("id") String id) throws Exception{
        MyOrder order = new MyOrder(id, "第"+id+"个订单");
        rabbitSender.sendDead(order);
        return "OK";
    }

}
