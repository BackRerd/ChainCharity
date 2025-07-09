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
                          `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                          `is_admin` tinyint(1) NULL DEFAULT 0,
                          `user_status` enum('active','suspended','pending') CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT 'pending',
                          `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (`user_id`) USING BTREE,
                          UNIQUE INDEX `username`(`username`) USING BTREE,
                          UNIQUE INDEX `phone`(`phone`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;
ALTER TABLE `users`
    ADD COLUMN `user_type` enum('donor','recipient') NOT NULL COMMENT '捐赠者/受助者' AFTER `user_status`,
    ADD COLUMN `is_enterprise` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否是企业' AFTER `user_type`;
ALTER TABLE users ENGINE=InnoDB;
CREATE TABLE `user_profiles` (
                                 `profile_id` int(11) NOT NULL AUTO_INCREMENT,
                                 `user_id` int(11) NOT NULL,
                                 `user_type` enum('donor','recipient','enterprise') CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,

    -- 通用信息（所有用户类型共用）
                                 `real_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '个人实名/企业名称',
                                 `id_card_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '身份证号/统一社会信用代码',
                                 `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '头像/企业LOGO',

    -- 个人用户信息（捐赠者/受助者）
                                 `gender` enum('male','female','other') CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,
                                 `birth_date` date NULL,
                                 `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,

    -- 受助者专属字段
                                 `bank_account` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '受助者银行卡号',
                                 `bank_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,
                                 `needs_description_file` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '需求说明文件URL',
                                 `family_members` int(11) NULL COMMENT '家庭成员数',
                                 `monthly_income` decimal(10,2) NULL COMMENT '月收入',
                                 `poverty_level` enum('low','medium','high','extreme') CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,

    -- 企业专属字段
                                 `legal_representative` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '企业法人',
                                 `business_license` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '营业执照号',
                                 `enterprise_type` enum('private','state-owned','foreign','joint-venture') CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,
                                 `industry` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,
                                 `contact_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,
                                 `contact_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,

    -- 企业角色标识（可捐赠/可受助）
                                 `enterprise_role` set('can_donate','can_receive') CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '企业可扮演角色',

    -- 验证状态
                                 `id_verified` tinyint(1) DEFAULT 0 COMMENT '实名认证状态',
                                 `bank_verified` tinyint(1) DEFAULT 0 COMMENT '银行卡认证状态(受助者)',
                                 `enterprise_verified` tinyint(1) DEFAULT 0 COMMENT '企业认证状态',

                                 `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                                 PRIMARY KEY (`profile_id`) USING BTREE,
                                 UNIQUE INDEX `user_id`(`user_id`) USING BTREE,
                                 UNIQUE INDEX `id_card_number`(`id_card_number`) USING BTREE,
                                 CONSTRAINT `fk_profile_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci;

CREATE TABLE `announcement` (
                                `id` varchar(36) NOT NULL COMMENT '公告ID，UUID',
                                `title` varchar(100) NOT NULL COMMENT '公告标题',
                                `content` text NOT NULL COMMENT '公告内容(Markdown格式)',
                                `is_top` tinyint NOT NULL DEFAULT '0' COMMENT '是否置顶：0-否，1-是',
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人ID',
                                `category_id` varchar(36) DEFAULT NULL COMMENT '分类ID',
                                PRIMARY KEY (`id`),
                                KEY `idx_category` (`category_id`),
                                KEY `idx_top` (`is_top`),
                                KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告表';

CREATE TABLE `forum_categories` (
                                    `category_id` int(11) NOT NULL AUTO_INCREMENT,
                                    `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                    `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
                                    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    `is_active` tinyint(1) NOT NULL DEFAULT 1,
                                    `parent_id` int(11) NULL DEFAULT NULL COMMENT '用于子分类',
                                    `sort_order` int(11) NOT NULL DEFAULT 0 COMMENT '分类排序',
                                    PRIMARY KEY (`category_id`) USING BTREE,
                                    INDEX `parent_id`(`parent_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;
CREATE TABLE `forum_posts` (
                               `post_id` int(11) NOT NULL AUTO_INCREMENT,
                               `user_id` int(11) NOT NULL,
                               `category_id` int(11) NOT NULL,
                               `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                               `content` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                               `view_count` int(11) NOT NULL DEFAULT 0,
                               `reply_count` int(11) NOT NULL DEFAULT 0,
                               `last_reply_at` timestamp NULL DEFAULT NULL,
                               `is_sticky` tinyint(1) NOT NULL DEFAULT 0,
                               `is_essence` tinyint(1) NOT NULL DEFAULT 0 COMMENT '精华帖',
                               `is_closed` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否关闭回复',
                               `status` enum('published','pending','deleted') CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'published',
                               `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               PRIMARY KEY (`post_id`) USING BTREE,
                               INDEX `user_id`(`user_id`) USING BTREE,
                               INDEX `category_id`(`category_id`) USING BTREE,
                               INDEX `is_sticky`(`is_sticky`) USING BTREE,
                               FULLTEXT INDEX `title_content`(`title`, `content`)
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;
CREATE TABLE `forum_replies` (
                                 `reply_id` int(11) NOT NULL AUTO_INCREMENT,
                                 `post_id` int(11) NOT NULL,
                                 `user_id` int(11) NOT NULL,
                                 `content` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                 `like_count` int(11) NOT NULL DEFAULT 0,
                                 `is_first` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是首帖的回复',
                                 `status` enum('published','pending','deleted') CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'published',
                                 `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`reply_id`) USING BTREE,
                                 INDEX `post_id`(`post_id`) USING BTREE,
                                 INDEX `user_id`(`user_id`) USING BTREE,
                                 INDEX `is_first`(`is_first`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;
CREATE TABLE `forum_likes` (
                               `like_id` int(11) NOT NULL AUTO_INCREMENT,
                               `user_id` int(11) NOT NULL,
                               `post_id` int(11) NULL DEFAULT NULL,
                               `reply_id` int(11) NULL DEFAULT NULL,
                               `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (`like_id`) USING BTREE,
                               UNIQUE INDEX `user_content`(`user_id`, `post_id`, `reply_id`) USING BTREE,
                               INDEX `post_id`(`post_id`) USING BTREE,
                               INDEX `reply_id`(`reply_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;
CREATE TABLE `forum_attachments` (
                                     `attachment_id` int(11) NOT NULL AUTO_INCREMENT,
                                     `post_id` int(11) NULL DEFAULT NULL,
                                     `reply_id` int(11) NULL DEFAULT NULL,
                                     `user_id` int(11) NOT NULL,
                                     `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                     `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                     `file_size` int(11) NOT NULL COMMENT '文件大小(字节)',
                                     `file_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                     `download_count` int(11) NOT NULL DEFAULT 0,
                                     `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     PRIMARY KEY (`attachment_id`) USING BTREE,
                                     INDEX `post_id`(`post_id`) USING BTREE,
                                     INDEX `reply_id`(`reply_id`) USING BTREE,
                                     INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;
SET FOREIGN_KEY_CHECKS = 1;
