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

 Date: 28/06/2018 13:07:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for spring_cache
-- ----------------------------
DROP TABLE IF EXISTS `spring_cache`;
CREATE TABLE `spring_cache`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `num` int(11) NOT NULL,
  `date` datetime NULL DEFAULT NULL,
  `is_true` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of spring_cache
-- ----------------------------
INSERT INTO `spring_cache` VALUES ('1', '关联到spring_cache_relevance', 8, '2018-06-27 09:39:34', 1);
INSERT INTO `spring_cache` VALUES ('f3138477-30bf-4cdd-be6f-f32bd7fac249', '0000', 66, '2018-06-29 11:19:48', 0);
INSERT INTO `spring_cache` VALUES ('f3138477-30bs-4cdd-be6f-f32bd7fac249', '111', 616, '2018-06-30 11:19:48', 1);
INSERT INTO `spring_cache` VALUES ('f3138477-30bs-4cdd-be6f-f32bd7fass49', '111', 16, '2018-06-14 11:19:48', 1);

SET FOREIGN_KEY_CHECKS = 1;
