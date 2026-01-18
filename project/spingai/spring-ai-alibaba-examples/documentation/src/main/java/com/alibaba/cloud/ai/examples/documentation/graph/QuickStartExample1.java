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
package com.alibaba.cloud.ai.examples.documentation.graph;

import com.alibaba.cloud.ai.graph.KeyStrategy;
import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.alibaba.cloud.ai.graph.state.strategy.AppendStrategy;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Graph 工作流编排快速入门示例
 * 
 * 本示例演示如何通过将客服邮件处理流程分解为离散步骤来使用 Spring AI Alibaba Graph 构建智能工作流。
 * 
 * 示例包含：
 * 1. 状态定义（EmailClassification）
 * 2. 节点实现（读取邮件、分类意图、搜索文档、Bug跟踪、起草回复、人工审核、发送回复）
 * 3. Graph 组装和配置
 * 4. 测试执行
 */
public class QuickStartExample1 {

	private static final Logger log = LoggerFactory.getLogger(QuickStartExample1.class);

	/**
	 * 邮件分类
	 */
	public static class EmailClassification {
		private String intent;

		private String urgency;

		private String topic;

		private String summary;

		public EmailClassification() {
		}

		public EmailClassification(String intent, String urgency, String topic, String summary) {
			this.intent = intent;
			this.urgency = urgency;
			this.topic = topic;
			this.summary = summary;
		}

		public String getIntent() {
			return intent;
		}

		public void setIntent(String intent) {
			this.intent = intent;
		}

		public String getUrgency() {
			return urgency;
		}

		public void setUrgency(String urgency) {
			this.urgency = urgency;
		}

		public String getTopic() {
			return topic;
		}

		public void setTopic(String topic) {
			this.topic = topic;
		}

		public String getSummary() {
			return summary;
		}

		public void setSummary(String summary) {
			this.summary = summary;
		}

		@Override
		public String toString() {
			return String.format("EmailClassification{intent='%s', urgency='%s', topic='%s', summary='%s'}",
					intent, urgency, topic, summary);
		}
	}

	/**
	 * 配置key状态策略
	 */
	public static KeyStrategyFactory createKeyStrategyFactory() {
		return () -> {
			HashMap<String, KeyStrategy> strategies = new HashMap<>();
			strategies.put("email_content", new ReplaceStrategy());
			strategies.put("sender_email", new ReplaceStrategy());
			strategies.put("email_id", new ReplaceStrategy());
			strategies.put("classification", new ReplaceStrategy());
			strategies.put("search_results", new ReplaceStrategy());
			strategies.put("customer_history", new ReplaceStrategy());
			strategies.put("draft_response", new ReplaceStrategy());
			strategies.put("messages", new AppendStrategy());
			strategies.put("next_node", new ReplaceStrategy());
			strategies.put("status", new ReplaceStrategy());
			strategies.put("review_data", new ReplaceStrategy());
			return strategies;
		};
	}

	/**
	 * 读取邮件
	 */
	public static class ReadEmailNode implements NodeAction {

		@Override
		public Map<String, Object> apply(OverAllState state) throws Exception {
			String emailContent = state.value("email_content").map(v -> (String)v).orElse("");
			log.info("ReadEmailNode----Processing email: {}", emailContent);
			ArrayList<String> messages = new ArrayList<>();
			messages.add("Processing email: " + emailContent);
			return Map.of("messages", messages);
		}
	}

	/**
	 * 分类意图节点
	 */
	public static class ClassifyIntentNode implements NodeAction {

		private final ChatClient chatClient;

		public ClassifyIntentNode(ChatClient.Builder chatClientBuilder) {
			this.chatClient = chatClientBuilder.build();
		}

		@Override
		public Map<String, Object> apply(OverAllState state) throws Exception {
			String emailContent = state.value("email_content")
					.map(v -> (String) v)
					.orElseThrow(() -> new IllegalStateException("No email content"));
			String senderEmail = state.value("sender_email")
					.map(v -> (String) v)
					.orElse("unknown");

			// 按需格式化提示，不存储在状态中
			String classificationPrompt = String.format("""
					分析这封客户邮件并进行分类：

					邮件: %s
					发件人: %s

					提供分类，包括意图、紧急程度、主题和摘要。

					意图应该是以下之一: question, bug, billing, feature, complex
					紧急程度应该是以下之一: low, medium, high, critical

					以JSON格式返回: {"intent": "...", "urgency": "...", "topic": "...", "summary": "..."}
					""", emailContent, senderEmail);

			// 获取结构化响应
			String response = chatClient.prompt()
					.user(classificationPrompt)
					.call()
					.content();

			// 解析为 EmailClassification 对象
			QuickStartExample.EmailClassification classification = JSON.parseObject(response, QuickStartExample.EmailClassification.class);

			// 根据分类确定下一个节点
			String nextNode;
			if ("billing".equals(classification.getIntent()) ||
					"critical".equals(classification.getUrgency())) {
				nextNode = "human_review";
			} else if (List.of("question", "feature").contains(classification.getIntent())) {
				nextNode = "search_documentation";
			} else if ("bug".equals(classification.getIntent())) {
				nextNode = "bug_tracking";
			} else {
				nextNode = "draft_response";
			}

			// 将分类作为单个对象存储在状态中
			return Map.of(
					"classification", classification,
					"next_node", nextNode
			);
		}

		/**
		 * 简化的JSON解析（实际应用中使用Jackson或Gson）
		 */
		private QuickStartExample.EmailClassification parseClassification(String jsonResponse) {
			QuickStartExample.EmailClassification classification = new QuickStartExample.EmailClassification();

			// 简单的正则表达式解析
			Pattern intentPattern = Pattern.compile("\"intent\"\\s*:\\s*\"([^\"]+)\"");
			Pattern urgencyPattern = Pattern.compile("\"urgency\"\\s*:\\s*\"([^\"]+)\"");
			Pattern topicPattern = Pattern.compile("\"topic\"\\s*:\\s*\"([^\"]+)\"");
			Pattern summaryPattern = Pattern.compile("\"summary\"\\s*:\\s*\"([^\"]+)\"");

			Matcher matcher = intentPattern.matcher(jsonResponse);
			if (matcher.find()) {
				classification.setIntent(matcher.group(1));
			}

			matcher = urgencyPattern.matcher(jsonResponse);
			if (matcher.find()) {
				classification.setUrgency(matcher.group(1));
			}

			matcher = topicPattern.matcher(jsonResponse);
			if (matcher.find()) {
				classification.setTopic(matcher.group(1));
			}

			matcher = summaryPattern.matcher(jsonResponse);
			if (matcher.find()) {
				classification.setSummary(matcher.group(1));
			}

			// 如果解析失败，设置默认值
			if (classification.getIntent() == null) {
				classification.setIntent("question");
			}
			if (classification.getUrgency() == null) {
				classification.setUrgency("medium");
			}
			if (classification.getTopic() == null) {
				classification.setTopic("general");
			}
			if (classification.getSummary() == null) {
				classification.setSummary("需要处理的客户邮件");
			}

			return classification;
		}
	}


}

