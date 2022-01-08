/*
 Navicat MySQL Data Transfer

 Source Server         : 1761
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : community

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 03/11/2021 13:55:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for discuss_post
-- ----------------------------
DROP TABLE IF EXISTS `discuss_post`;
CREATE TABLE `discuss_post`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(45) NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `type` int(11) NULL DEFAULT NULL COMMENT '0-普通；1-置顶',
  `status` int(255) NULL DEFAULT NULL COMMENT '0-正常；1-精华；2-拉黑',
  `create_time` timestamp NULL DEFAULT NULL,
  `comment_count` int(11) NULL DEFAULT NULL,
  `score` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `user_id`) USING BTREE,
  INDEX `userid`(`user_id`) USING BTREE,
  CONSTRAINT `userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
