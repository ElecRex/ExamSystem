/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云ECS服务器
 Source Server Type    : MySQL
 Source Server Version : 50647
 Source Host           : 59.110.1.28:3306
 Source Schema         : exam

 Target Server Type    : MySQL
 Target Server Version : 50647
 File Encoding         : 65001

 Date: 07/06/2020 02:47:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stu_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `exam_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `file_size` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `times` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of answer
-- ----------------------------
INSERT INTO `answer` VALUES (43, '001', 'test', '1.jpg', '724759', '2020-06-06 22:18:19');
INSERT INTO `answer` VALUES (44, '002', 'test', '火女.jpg', '216991', '2020-06-06 22:18:50');
INSERT INTO `answer` VALUES (45, '001', 'test1', '1.jpg', '724759', '2020-06-06 22:34:38');
INSERT INTO `answer` VALUES (46, '003', 'test1', '影魔.jpg', '249263', '2020-06-06 22:35:19');
INSERT INTO `answer` VALUES (47, '002', 'test1', '火女.jpg', '216991', '2020-06-06 22:35:54');
INSERT INTO `answer` VALUES (48, '001', 'test2', 'applicationContext.xml', '3086', '2020-06-06 22:54:55');
INSERT INTO `answer` VALUES (49, '002', 'test2', 'springmvc.xml', '1216', '2020-06-06 22:55:27');
INSERT INTO `answer` VALUES (50, '001', 'test3', '答案1.txt', '11', '2020-06-06 23:40:21');
INSERT INTO `answer` VALUES (51, '002', 'test3', '答案2.txt', '12', '2020-06-06 23:41:08');
INSERT INTO `answer` VALUES (52, '005', 'test3', '答案5.txt', '7', '2020-06-06 23:41:40');
INSERT INTO `answer` VALUES (53, '003', '111', '答案3.txt', '15', '2020-06-07 00:37:59');
INSERT INTO `answer` VALUES (54, '001', '111', '答案1.txt', '11', '2020-06-07 00:39:13');
INSERT INTO `answer` VALUES (55, '001', '1', '答案1.txt', '11', '2020-06-07 00:59:57');
INSERT INTO `answer` VALUES (56, '001', '11', '答案1.txt', '11', '2020-06-07 02:35:16');
INSERT INTO `answer` VALUES (57, '003', '11', '答案3.txt', '15', '2020-06-07 02:36:35');
INSERT INTO `answer` VALUES (58, '001', '1111', '答案2.txt', '12', '2020-06-07 02:40:34');

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam`  (
  `e_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `e_starttime` datetime(0) NULL DEFAULT NULL,
  `e_teacher` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `e_autostart` tinyint(10) NULL DEFAULT NULL,
  `e_isend` tinyint(10) NULL DEFAULT NULL,
  `e_file` tinyint(10) NULL DEFAULT NULL,
  `e_clear` tinyint(10) NULL DEFAULT NULL,
  `e_isstart` tinyint(10) NULL DEFAULT NULL,
  `e_examination` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`e_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of exam
-- ----------------------------
INSERT INTO `exam` VALUES ('操作系统', '2020-03-14 17:08:32', 'teacher1', 1, 1, 0, 1, 1, 'Fast R-CNN.pdf');
INSERT INTO `exam` VALUES ('数据结构', '2020-04-05 12:00:43', 'teacher1', 0, 1, 0, 1, 1, '2019年数据结构期末考试');
INSERT INTO `exam` VALUES ('编译原理', '2020-03-23 08:00:48', 'teacher2', 0, 1, 0, 1, 1, '2020年编译原理考试题');
INSERT INTO `exam` VALUES ('计算机组成原理', '2020-03-14 17:29:35', 'admin', 0, 1, 0, 1, 1, 'GoogLeNet.pdf');
INSERT INTO `exam` VALUES ('计算机网络', '2020-02-18 12:00:29', 'teacher2', 0, 1, 0, 1, 1, NULL);

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `stu_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `stu_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `stu_class` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `stu_exam` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `stu_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `stu_submit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`stu_id`, `stu_exam`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `t_username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `t_pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `t_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `t_manager` tinyint(10) NULL DEFAULT NULL,
  PRIMARY KEY (`t_username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('admin', '21232f297a57a5a743894a0e4a801fc3', 'admin', 1);
INSERT INTO `teacher` VALUES ('teacher1', '202cb962ac59075b964b07152d234b70', 'teacher1', 0);
INSERT INTO `teacher` VALUES ('teacher2', '202cb962ac59075b964b07152d234b70', 'teacher2', 0);

SET FOREIGN_KEY_CHECKS = 1;
