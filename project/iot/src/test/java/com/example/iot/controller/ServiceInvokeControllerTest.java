package com.example.iot.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import com.example.iot.common.MsgIdGenerator;
import com.example.iot.IotApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * 服务调用接口的 MVC 集成测试。
 */
@SpringBootTest(classes = IotApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "iot.invoke.timeout=100ms",
        "iot.invoke.max-pending=2"
})
class ServiceInvokeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MsgIdGenerator msgIdGenerator;

    /**
     * 模拟 MQTT 回复到达后完成原始 HTTP 请求。
     *
     * @throws Exception MockMvc 执行失败时抛出
     */
    @Test
    void shouldReturnReplyDataWhenMockMqttReplyArrives() throws Exception {
        when(msgIdGenerator.nextId()).thenReturn(124545L);

        MvcResult invokeResult = mockMvc.perform(post("/service/invoke")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"data":"发的数据"}
                                """))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(post("/mock/mqtt/invoke-reply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"msgId":124545,"data":"返回的数据"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msgId").value(124545))
                .andExpect(jsonPath("$.matched").value(true));

        mockMvc.perform(asyncDispatch(invokeResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("返回的数据"));
    }

    /**
     * 未收到模拟 MQTT 回复时返回网关超时。
     *
     * @throws Exception MockMvc 执行失败时抛出
     */
    @Test
    void shouldReturnGatewayTimeoutWhenReplyDoesNotArrive() throws Exception {
        when(msgIdGenerator.nextId()).thenReturn(124546L);

        MvcResult invokeResult = mockMvc.perform(post("/service/invoke")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"data":"发的数据"}
                                """))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(invokeResult))
                .andExpect(status().isGatewayTimeout())
                .andExpect(jsonPath("$.code").value("TIMEOUT"))
                .andExpect(jsonPath("$.message").value("等待 MQTT 回复超时"));
    }

    /**
     * 消息 ID 不在待处理列表中时返回 matched=false。
     *
     * @throws Exception MockMvc 执行失败时抛出
     */
    @Test
    void shouldReturnMatchedFalseForUnknownMockReply() throws Exception {
        mockMvc.perform(post("/mock/mqtt/invoke-reply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"msgId":999999,"data":"返回的数据"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msgId").value(999999))
                .andExpect(jsonPath("$.matched").value(false));
    }
}
