/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50726 (5.7.26)
 Source Host           : localhost:3306
 Source Schema         : chaincharity

 Target Server Type    : MySQL
 Target Server Version : 50726 (5.7.26)
 File Encoding         : 65001

 Date: 01/07/2025 09:22:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activities
-- ----------------------------
DROP TABLE IF EXISTS `activities`;
CREATE TABLE `activities`  (
  `activity_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,
  `category_id` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `status` enum('planning','ongoing','completed') CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT 'planning',
  `created_by` int(11) NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`activity_id`) USING BTREE,
  INDEX `category_id`(`category_id`) USING BTREE,
  INDEX `created_by`(`created_by`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1001 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_categories
-- ----------------------------
DROP TABLE IF EXISTS `activity_categories`;
CREATE TABLE `activity_categories`  (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,
  `created_by` int(11) NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`category_id`) USING BTREE,
  UNIQUE INDEX `category_name`(`category_name`) USING BTREE,
  INDEX `created_by`(`created_by`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 180 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_participation
-- ----------------------------
DROP TABLE IF EXISTS `activity_participation`;
CREATE TABLE `activity_participation`  (
  `participation_id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `role` enum('donor','recipient') CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `participation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `approval_status` enum('pending','approved','rejected') CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT 'pending',
  `reviewed_by` int(11) NULL DEFAULT NULL,
  `reviewed_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`participation_id`) USING BTREE,
  UNIQUE INDEX `activity_id`(`activity_id`, `user_id`, `role`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `reviewed_by`(`reviewed_by`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Fixed;

-- ----------------------------
-- Table structure for distributions
-- ----------------------------
DROP TABLE IF EXISTS `distributions`;
CREATE TABLE `distributions`  (
  `distribution_id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_id` int(11) NOT NULL,
  `recipient_id` int(11) NOT NULL,
  `donation_type` enum('item','money') CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `item_donation_id` int(11) NULL DEFAULT NULL,
  `money_donation_id` int(11) NULL DEFAULT NULL,
  `distributed_quantity` int(11) NULL DEFAULT NULL,
  `distributed_amount` decimal(10, 2) NULL DEFAULT NULL,
  `distribution_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `approval_status` enum('pending','approved','rejected') CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT 'pending',
  `reviewed_by` int(11) NULL DEFAULT NULL,
  `reviewed_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`distribution_id`) USING BTREE,
  INDEX `activity_id`(`activity_id`) USING BTREE,
  INDEX `recipient_id`(`recipient_id`) USING BTREE,
  INDEX `item_donation_id`(`item_donation_id`) USING BTREE,
  INDEX `money_donation_id`(`money_donation_id`) USING BTREE,
  INDEX `reviewed_by`(`reviewed_by`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Fixed;

-- ----------------------------
-- Table structure for item_donations
-- ----------------------------
DROP TABLE IF EXISTS `item_donations`;
CREATE TABLE `item_donations`  (
  `donation_id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_id` int(11) NOT NULL,
  `donor_id` int(11) NOT NULL,
  `item_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `quantity` int(11) NOT NULL,
  `image_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `donation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `approval_status` enum('pending','approved','rejected') CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT 'pending',
  `reviewed_by` int(11) NULL DEFAULT NULL,
  `reviewed_at` timestamp NULL DEFAULT NULL,
  `status` enum('pending','received','distributed') CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT 'pending',
  PRIMARY KEY (`donation_id`) USING BTREE,
  INDEX `activity_id`(`activity_id`) USING BTREE,
  INDEX `donor_id`(`donor_id`) USING BTREE,
  INDEX `reviewed_by`(`reviewed_by`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for money_donations
-- ----------------------------
DROP TABLE IF EXISTS `money_donations`;
CREATE TABLE `money_donations`  (
  `donation_id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_id` int(11) NOT NULL,
  `donor_id` int(11) NOT NULL,
  `amount` decimal(10, 2) NOT NULL,
  `donation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `approval_status` enum('pending','approved','rejected') CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT 'pending',
  `reviewed_by` int(11) NULL DEFAULT NULL,
  `reviewed_at` timestamp NULL DEFAULT NULL,
  `status` enum('pending','received','distributed') CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT 'pending',
  PRIMARY KEY (`donation_id`) USING BTREE,
  INDEX `activity_id`(`activity_id`) USING BTREE,
  INDEX `donor_id`(`donor_id`) USING BTREE,
  INDEX `reviewed_by`(`reviewed_by`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Fixed;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `is_admin` tinyint(1) NULL DEFAULT 0,
  `user_status` enum('active','suspended','pending') CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT 'pending',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
