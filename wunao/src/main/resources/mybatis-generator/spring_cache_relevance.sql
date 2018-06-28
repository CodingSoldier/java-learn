/*
 Navicat Premium Data Transfer

 Source Server         : 184.170.220.217我的
 Source Server Type    : MySQL
 Source Server Version : 50560
 Source Host           : 184.170.220.217:3306
 Source Schema         : cpq

 Target Server Type    : MySQL
 Target Server Version : 50560
 File Encoding         : 65001

 Date: 28/06/2018 13:07:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for spring_cache_relevance
-- ----------------------------
DROP TABLE IF EXISTS `spring_cache_relevance`;
CREATE TABLE `spring_cache_relevance`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `relevance_text` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of spring_cache_relevance
-- ----------------------------
INSERT INTO `spring_cache_relevance` VALUES ('11', '关联到spring_cache_relevance', 'relevance的text');

SET FOREIGN_KEY_CHECKS = 1;
