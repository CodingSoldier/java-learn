package com.example.iot.mqtt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.iot.service.ServiceInvokeReplyService;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * {@link MqttReplyPayloadHandler} 的测试。
 */
@ExtendWith(MockitoExtension.class)
class MqttReplyPayloadHandlerTest {

    @Mock
    private ServiceInvokeReplyService serviceInvokeReplyService;

    /**
     * 收到合法 MQTT 回复时应完成对应服务调用。
     */
    @Test
    void shouldCompleteReplyWhenValidPayloadArrives() {
        MqttReplyPayloadHandler handler = new MqttReplyPayloadHandler(serviceInvokeReplyService);

        handler.handle("""
                {"msgId":124545,"data":"返回的数据"}
                """.getBytes(StandardCharsets.UTF_8));

        verify(serviceInvokeReplyService).completeReply(124545L, "返回的数据");
    }

    /**
     * 收到非法 JSON 时应忽略，不能把异常抛出到 MQTT 回调之外。
     */
    @Test
    void shouldIgnoreInvalidReplyPayload() {
        MqttReplyPayloadHandler handler = new MqttReplyPayloadHandler(serviceInvokeReplyService);

        handler.handle("不是 JSON".getBytes(StandardCharsets.UTF_8));

        verify(serviceInvokeReplyService, never()).completeReply(anyLong(), any());
    }

    /**
     * 收到缺少 msgId 的回复时应忽略。
     */
    @Test
    void shouldIgnoreReplyPayloadWithoutMsgId() {
        MqttReplyPayloadHandler handler = new MqttReplyPayloadHandler(serviceInvokeReplyService);

        handler.handle("""
                {"data":"返回的数据"}
                """.getBytes(StandardCharsets.UTF_8));

        verify(serviceInvokeReplyService, never()).completeReply(anyLong(), any());
    }
}
