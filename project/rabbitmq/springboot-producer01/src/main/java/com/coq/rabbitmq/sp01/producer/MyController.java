package com.coq.rabbitmq.sp01.producer;


import com.coq.rabbitmq.sp01.bean.Order01;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
public class MyController {

    @Autowired
    SenderSpecific senderSpecific;
    @Autowired
    SenderAck senderAck;
    @Autowired
    SenderReturnQueueTimes senderReturnQueueTimes;

    @GetMapping("/send")
    public String loginSend(@RequestParam("id") Long id) throws Exception{
        Order01 order = new Order01(id, "第"+id+"个订单");
        senderSpecific.sendOrder(order);
        return "OK";
    }

    @GetMapping("/ack")
    public String senderRetry(@RequestParam("id") Long id) throws Exception{
        Order01 order = new Order01(id, "第"+id+"个订单");
        senderAck.sendOrder(order);
        return "OK";
    }

    @GetMapping("/returnQueueTimes")
    public String SenderReturnQueueTimes(@RequestParam("id") Long id) throws Exception{
        Order01 order = new Order01(id, "第"+id+"个订单");
        senderReturnQueueTimes.sendOrder(order);
        return "OK";
    }


    // @GetMapping("/ack/callback")
    // public String sendOrderCallback(@RequestParam("id") Long id) throws Exception{
    //     Order01 order = new Order01(id, "第"+id+"个订单");
    //     senderAck.sendOrderCallback(order);
    //     return "OK";
    // }



}
