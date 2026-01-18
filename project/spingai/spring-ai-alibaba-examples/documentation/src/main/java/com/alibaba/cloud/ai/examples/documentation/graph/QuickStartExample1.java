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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}

