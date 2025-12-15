/*
 * Copyright 2024-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cloud.ai.examples.chatbot;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.examples.chatbot.tool.UserLocationTool;
import com.alibaba.cloud.ai.examples.chatbot.tool.WeatherForLocationTool;
import com.alibaba.cloud.ai.examples.chatbot.tool.WeatherTool;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.hook.Hook;
import com.alibaba.cloud.ai.graph.agent.hook.hip.HumanInTheLoopHook;
import com.alibaba.cloud.ai.graph.agent.hook.hip.ToolConfig;
import com.alibaba.cloud.ai.graph.agent.hook.modelcalllimit.ModelCallLimitHook;
import com.alibaba.cloud.ai.graph.agent.hook.summarization.SummarizationHook;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ChatbotAgent {

	private final String QWEN_MODEL = "qwen-plus";

	String customSchema = """
    请按照以下JSON格式输出：
    {
        "title": "标题",
        "content": "内容",
        "style": "风格"
    }
    """;


	ToolCallback weatherTool = FunctionToolCallback.builder("get_weather", new WeatherTool())
			.description("Get weather for a given city")
			.inputType(String.class)
			.build();

	ToolCallback getWeatherTool = FunctionToolCallback
			.builder("getWeatherForLocation", new WeatherForLocationTool())
			.description("Get weather for a given city")
			.inputType(String.class)
			.build();

	ToolCallback getUserLocationTool = FunctionToolCallback
			.builder("getUserLocation", new UserLocationTool())
			.description("Retrieve user location based on user ID")
			.inputType(String.class)
			.build();

	Hook humanInTheLoopHook = HumanInTheLoopHook.builder()
			.approvalOn("getWeatherTool",
					ToolConfig.builder().description("Please confirm tool execution.").build())
			.build();

	@Bean("dashScopeApi")
	public DashScopeApi dashScopeApi() {
		return DashScopeApi.builder()
				.apiKey(System.getenv("ALI_AI_KEY"))
				.build();
	}

	/**
	 * 自定义的ChatModel必须填写.model(模型名称)参数
	 * @param dashScopeApi
	 * @return
	 */
	@Bean("chatModel")
	public ChatModel chatModel(@Qualifier("dashScopeApi") DashScopeApi dashScopeApi) {
		return DashScopeChatModel.builder()
				.dashScopeApi(dashScopeApi)
				.defaultOptions(DashScopeChatOptions.builder()
						.model(QWEN_MODEL)    // 控制随机性
						.withTemperature(0.7)    // 控制随机性
						.withMaxToken(2000)      // 最大输出长度
						.withTopP(0.9)           // 核采样参数
						.build())
				.build();
	}

	@Bean
	public ReactAgent agent(@Qualifier("chatModel") ChatModel chatModel) {
		SummarizationHook summarizationHook = SummarizationHook.builder()
				.model(chatModel)
				.maxTokensBeforeSummary(4000)
				.messagesToKeep(20)
				.build();
		ModelCallLimitHook modelCallLimitHook = ModelCallLimitHook.builder().runLimit(5).build();
		return  ReactAgent.builder()
				.name("schema_agent")
				.model(chatModel)
				.tools(getWeatherTool)
				.saver(new MemorySaver())
				.hooks(modelCallLimitHook, summarizationHook)
				.build();
	}

	@Bean
	public MemorySaver memorySaver() {
		return new MemorySaver();
	}


}

