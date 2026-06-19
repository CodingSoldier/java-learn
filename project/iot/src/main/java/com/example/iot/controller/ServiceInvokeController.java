package com.example.iot.controller;

import com.example.iot.model.ServiceInvokeRequest;
import com.example.iot.service.ServiceInvokeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * IoT 服务调用 HTTP 接口。
 */
@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class ServiceInvokeController {

    private final ServiceInvokeService serviceInvokeService;

    /**
     * 调用 IoT 服务，并异步等待回复。
     *
     * @param request 调用请求
     * @return 由模拟 MQTT 回复完成的延迟响应
     */
    @PostMapping("/invoke")
    public DeferredResult<ResponseEntity<?>> invoke(@Valid @RequestBody ServiceInvokeRequest request) {
        return serviceInvokeService.invoke(request);
    }
}
