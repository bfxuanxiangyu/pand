/*
 Navicat Premium Data Transfer

 Source Server         : 61.129.57.240pand
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : 61.129.57.240:33036
 Source Schema         : pand

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 16/11/2018 15:00:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for area
-- ----------------------------
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area`  (
  `ID` int(22) NOT NULL COMMENT '区域编号',
  `AREA_CODE` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `AREA_NAME` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区域名称',
  `P_AREA_CODE` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '父级编号',
  `REMARK` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `SORT` int(2) DEFAULT NULL COMMENT '区域排序',
  `FLAG` int(2) DEFAULT 0 COMMENT '是否删除  0未删除  1已删除',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ask_for
-- ----------------------------
DROP TABLE IF EXISTS `ask_for`;
CREATE TABLE `ask_for`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `contact` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '其他联系方式',
  `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '索要内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `update_time` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '是否可用 0不可用  1可用   默认1',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片访问地址',
  `img_order` int(2) NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DICT_NAME` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字典名称',
  `DICT_ID` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典ID',
  `TYPE_ID` int(11) NOT NULL COMMENT '所属类型ID',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `DICT_TYPE_INDEX`(`TYPE_ID`, `DICT_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dict_type
-- ----------------------------
DROP TABLE IF EXISTS `dict_type`;
CREATE TABLE `dict_type`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE_NAME` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字典名称',
  `TYPE_CREATETIME` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `TYPE_ID` int(11) NOT NULL COMMENT '类型编号',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `TYPE_ID_UNIQUE`(`TYPE_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CREATER` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作用户名称',
  `REQUEST_URL` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'URL',
  `REQUEST_TIME` datetime(0) DEFAULT NULL COMMENT '日志生成时间',
  `REQUEST_METHOD` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '提交类型POST GET等',
  `REQUEST_PARAM` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请求参数',
  `EXECUTE_TIME` int(11) DEFAULT NULL COMMENT '执行时间',
  `HTTP_CODE` int(11) DEFAULT NULL COMMENT 'HTTP响应状态',
  `USER_AGENT` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '浏览器类型',
  `CLIENT_IP` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'IP地址',
  `DESCRIPTION` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '详细描述，如方法名称',
  `ERROR_MESSAGE` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '异常信息',
  `OPERATE_TYPE` int(11) DEFAULT NULL COMMENT '1:新增，2：删除，3：修改，4：查询，98:登录，99：其他',
  `MOUDLE_NAME` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作模块名称',
  `MENU_NAME` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作菜单名称',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `LOG_OPERATE_INDEX`(`OPERATE_TYPE`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 321 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `MENU_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单编号',
  `MENU_NAME` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名',
  `MENU_TITLE` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '显示标题',
  `PARENT_NAME` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '上级菜单',
  `ACTION` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请求地址',
  `ODER_BY` int(11) DEFAULT 0 COMMENT '排序',
  `STATUS` int(11) DEFAULT 1 COMMENT '状态1为可用，0为不可用',
  `AREA` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '区域',
  `ICON` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图标',
  `CREATE_TIME` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `CREATOR` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建者',
  `MENU_FUNCTION` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '点击菜单触发函数',
  `MENU_STYLE` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单样式',
  `MENU_STYLE_CLASS` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单样式名',
  PRIMARY KEY (`MENU_ID`) USING BTREE,
  UNIQUE INDEX `MENU_NAME_UNIQUE`(`MENU_NAME`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10038 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pand_audit_log
-- ----------------------------
DROP TABLE IF EXISTS `pand_audit_log`;
CREATE TABLE `pand_audit_log`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `update_time` datetime(0) DEFAULT NULL,
  `pand_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商家用户id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '原因',
  `audit_type` int(1) NOT NULL DEFAULT 2 COMMENT '审核状态 2审核通过  3不通过',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '服务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pand_images
-- ----------------------------
DROP TABLE IF EXISTS `pand_images`;
CREATE TABLE `pand_images`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `img_type` int(1) DEFAULT 0 COMMENT '图片类型  0通用型 1身份证正面，2反面  3持证  4港澳台通行证 5护照 ',
  `img_model` int(1) NOT NULL COMMENT '所属模块  1用户审核   2评论 3服务图片',
  `model_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '对应模块id号',
  `img_status` int(1) DEFAULT 0 COMMENT '图片是否可用 0可用 1不可用  默认0',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片访问路径',
  `img_base64` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT 'base64类型图片',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `imgIndexModelId`(`model_id`) USING BTREE,
  INDEX `imgIndexImgtype`(`img_type`) USING BTREE,
  INDEX `imgIndexImgModel`(`img_model`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pand_order
-- ----------------------------
DROP TABLE IF EXISTS `pand_order`;
CREATE TABLE `pand_order`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `update_time` datetime(0) DEFAULT NULL,
  `pand_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '下单用户id',
  `service_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务id',
  `shop_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '店铺id',
  `pand_shoper_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商铺用户id',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务地址',
  `time_period` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务时间段',
  `service_count` int(2) DEFAULT NULL COMMENT '服务数量',
  `message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '留言',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '订单状态  0下单  1取消订单  2删除  3接单  4订单完成',
  `service_money` double DEFAULT NULL COMMENT '订单金额',
  `order_num` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'pand订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pand_service
-- ----------------------------
DROP TABLE IF EXISTS `pand_service`;
CREATE TABLE `pand_service`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `update_time` datetime(0) DEFAULT NULL,
  `pand_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商家用户id',
  `service_type_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务类型id',
  `service_tilte` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务标题',
  `service_des` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务描述',
  `service_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '服务须知',
  `service_city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '服务城市',
  `service_area` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '服务区域',
  `service_price` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务价格',
  `service_scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '服务范围',
  `service_invoice` int(1) DEFAULT 0 COMMENT '是否开具发票 0不开具   1开具   默认0',
  `service_status` int(1) DEFAULT 1 COMMENT '服务状态 1发布  2发布审核通过  3审核不通过 4下架 5删除默认1',
  `service_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务地址',
  `service_lat` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务地址纬度',
  `service_lng` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务地址经度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '服务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pand_shop
-- ----------------------------
DROP TABLE IF EXISTS `pand_shop`;
CREATE TABLE `pand_shop`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `update_time` datetime(0) DEFAULT NULL,
  `pand_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户id',
  `shop_tel` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系方式',
  `shop_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺名称',
  `shop_des` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '店铺简介',
  `shop_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺营业时间',
  `shop_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺形象图',
  `shop_status` int(1) DEFAULT NULL COMMENT '店铺状态 0可用  1不可用  默认0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pand_user
-- ----------------------------
DROP TABLE IF EXISTS `pand_user`;
CREATE TABLE `pand_user`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `update_time` datetime(0) DEFAULT NULL,
  `user_type` int(1) NOT NULL DEFAULT 1 COMMENT '用户类型 1普通用户 2商家用户 默认1',
  `user_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `user_password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户密码',
  `user_phone` varchar(61) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `user_nickname` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
  `user_realname` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户真实姓名',
  `user_signature` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户签名',
  `user_weixin` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '绑定微信',
  `user_qq` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '绑定QQ',
  `user_email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '绑定邮箱',
  `user_cardtype` int(1) DEFAULT NULL COMMENT '证件类型 1身份证 2护照  3港澳台通行证 4驾照',
  `user_cardcode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '证件号码',
  `user_sex` int(1) DEFAULT 0 COMMENT '性别 0男  1女  默认0',
  `user_provinces` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所在省份',
  `user_city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所在城市',
  `user_area` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所在区域',
  `user_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '详细地址',
  `user_headpng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像链接',
  `user_status` int(1) DEFAULT NULL COMMENT '用户状态  0可用 1商家待审核 2商家审核通过 3商家审核不通过 4封号',
  `user_lat` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '服务所在纬度',
  `user_lng` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '服务所在经度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pand_user_address
-- ----------------------------
DROP TABLE IF EXISTS `pand_user_address`;
CREATE TABLE `pand_user_address`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新日期',
  `pand_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户id',
  `status` int(1) DEFAULT 0 COMMENT '是否可用 0可用 1不可用  默认0',
  `person_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人姓名',
  `person_phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系方式',
  `person_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务地址',
  `address_lat` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务地址纬度',
  `address_lng` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务地址经度',
  `house_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '门牌号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pand_user_collection
-- ----------------------------
DROP TABLE IF EXISTS `pand_user_collection`;
CREATE TABLE `pand_user_collection`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新日期',
  `pand_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户id',
  `service_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务id',
  `status` int(1) DEFAULT 0 COMMENT '是否可用 0可用 1不可用  默认0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pand_user_comment
-- ----------------------------
DROP TABLE IF EXISTS `pand_user_comment`;
CREATE TABLE `pand_user_comment`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新日期',
  `pand_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户id',
  `service_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务id',
  `status` int(1) DEFAULT 0 COMMENT '是否可用 0可用 1不可用  默认0',
  `skilled_score` double NOT NULL COMMENT '技术熟练分数',
  `attitude_score` double NOT NULL COMMENT '服务态度分数',
  `efficiency_score` double NOT NULL COMMENT '工作效率分数',
  `anonymous` int(1) NOT NULL DEFAULT 0 COMMENT '是否匿名  0是  1否',
  `comment` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for police
-- ----------------------------
DROP TABLE IF EXISTS `police`;
CREATE TABLE `police`  (
  `ID` int(22) NOT NULL,
  `POLICE_CODE` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '派出所编号',
  `POLICE_NAME` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '派出所名称',
  `POLICE_NAME_PINYIN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '派出所拼音',
  `P_POLICE_CODE` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '父级派出所编号',
  `REMARK` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `AREA_CODE` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '区域编号',
  `SORT` int(3) DEFAULT NULL COMMENT '派出所排序',
  `FLAG` int(2) DEFAULT 0 COMMENT '是否删除 0未删除  1已删除',
  `POINT_X` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '经度',
  `POINT_Y` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '纬度',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `ROLE_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `ROLE_NAME` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色名',
  `ROLE_TITLE` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `REMARK` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATOR` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ROLE_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role_menus
-- ----------------------------
DROP TABLE IF EXISTS `role_menus`;
CREATE TABLE `role_menus`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MENU_ID` int(11) NOT NULL COMMENT '菜单编号',
  `ROLE_ID` int(11) NOT NULL COMMENT '角色编号',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for script_sqllog
-- ----------------------------
DROP TABLE IF EXISTS `script_sqllog`;
CREATE TABLE `script_sqllog`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CREATE_TIME` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `SQL_TYPE` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类型schema、data',
  `SQL_VERSION` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '版本',
  `SQL_SCRIPTS` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '脚本',
  `EXECUTE_TIME` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for short_string
-- ----------------------------
DROP TABLE IF EXISTS `short_string`;
CREATE TABLE `short_string`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建日期',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '原文',
  `short_string` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '短文本',
  `app_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户id',
  `app_secret` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户密钥',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '短连接表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for short_url
-- ----------------------------
DROP TABLE IF EXISTS `short_url`;
CREATE TABLE `short_url`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建日期',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户id',
  `url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '原文地址',
  `short_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '短连接地址',
  `app_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户id',
  `app_secret` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户密钥',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '短连接表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sinnren_log
-- ----------------------------
DROP TABLE IF EXISTS `sinnren_log`;
CREATE TABLE `sinnren_log`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `version_s` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '版本',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `country` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家',
  `region` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省会',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
  `isp` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '运营商',
  `area` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '区域',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ip_index`(`ip`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for skills
-- ----------------------------
DROP TABLE IF EXISTS `skills`;
CREATE TABLE `skills`  (
  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `update_time` datetime(0) DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '是否可用 0不可用 1可用 默认1',
  `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图标路径',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '技能标题',
  `icon_order` int(2) DEFAULT NULL COMMENT '技能排序',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '技能描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_access_token
-- ----------------------------
DROP TABLE IF EXISTS `t_access_token`;
CREATE TABLE `t_access_token`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `access_token` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `expires_in` bigint(20) NOT NULL,
  `refresh_token` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_time` bigint(20) NOT NULL,
  `device_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `plat_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_roles
-- ----------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ROLE_ID` int(11) DEFAULT NULL COMMENT '角色编号',
  `USER_ID` int(11) DEFAULT NULL COMMENT '用户名',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `USER_PWD` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户密码',
  `PHONE` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户手机号',
  `EMAIL` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户邮箱',
  `NAME` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `POLICE_CODE` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '警号',
  `IDCARD` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '证件号码',
  `POLICE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '派出所编码',
  `AREA_CODE` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '区域编码',
  `ACTION` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '默认页面',
  `CREATE_TIME` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `LOGIN_LAST` datetime(0) DEFAULT NULL COMMENT '最后登录时间',
  `LOGIN_TOTAL` int(11) DEFAULT 0 COMMENT '总登陆次数',
  `USER_TYPE` int(11) DEFAULT 2 COMMENT '用户类型1：管理账号 2：普通用户',
  `STATUS` int(11) DEFAULT 1 COMMENT '用户状态 1为启用 0为禁用',
  `CREATOR` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建者',
  PRIMARY KEY (`USER_ID`) USING BTREE,
  UNIQUE INDEX `USER_NAME_UNIQUE`(`USER_NAME`) USING BTREE,
  UNIQUE INDEX `USER_ID_UNIQUE`(`USER_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10006 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
