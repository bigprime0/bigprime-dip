INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (315, 0, 'Dwm', '', NULL, 'IconLoading', 3, 0, 10000, '2024-09-12 21:12:35', 10000, '2024-10-16 17:13:07');
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (318, 315, 'Dwm-modeling', 'data-warehouse/components/dwm-modeling', NULL, 'IconEditorListDot', 1, 0, 10000, '2024-09-12 21:15:38', 10000, '2024-09-12 21:15:38');
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (319, 315, 'Dwm-query', 'data-warehouse/components/dwm-query', NULL, 'IconSearch', 2, 0, 10000, '2024-09-12 21:16:23', 10000, '2024-09-12 21:16:23');
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (320, 315, 'Dwm-structure', 'data-warehouse/components/dwm-structure', NULL, 'IconStreamSolid', 3, 0, 10000, '2024-09-12 21:17:04', 10000, '2024-09-12 21:17:04');
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (321, 315, 'Dwm-sql-execution', 'data-warehouse/components/dwm-sql-execution', NULL, 'IconAscending', 4, 0, 10000, '2024-09-12 21:18:17', 10000, '2024-09-12 21:18:17');



CREATE TABLE `data_house_layer` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                    `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分层英文名称',
                                    `cn_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分层中文名称',
                                    `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分层描述',
                                    `table_prefix` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表名前缀',
                                    `deleted` tinyint DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
                                    `creator` bigint DEFAULT NULL COMMENT '创建者',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `updater` bigint DEFAULT NULL COMMENT '更新者',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=30008 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='数据仓库-仓库分层';

CREATE TABLE `data_house_layer_detail` (
                                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                           `parent_id` bigint DEFAULT NULL COMMENT '父级分层ID',
                                           `product` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '绑定的产品',
                                           `database_id` bigint DEFAULT NULL COMMENT '绑定的数据库ID',
                                           `deleted` tinyint DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
                                           `creator` bigint DEFAULT NULL COMMENT '创建者',
                                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                           `updater` bigint DEFAULT NULL COMMENT '更新者',
                                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='数据仓库-绑定数据库';

INSERT INTO `data_house_layer`(`id`, `name`, `cn_name`, `note`, `table_prefix`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (30002, 'ODS', '数据引入层', '用于接收并处理需要存储至数据仓库系统的原始数据，其数据表的结构与原始数据所在的数据系统中的表结构一致，是数据仓库的数据准备区1', 'ods', 0, 10000, '2022-10-08 17:16:35', 10000, '2024-03-16 15:00:27');
INSERT INTO `data_house_layer`(`id`, `name`, `cn_name`, `note`, `table_prefix`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (30003, 'DIM', '维度层', '使用维度构建数据模型', 'dim', 0, 10000, '2022-10-08 17:17:40', 10000, '2022-10-08 17:17:42');
INSERT INTO `data_house_layer`(`id`, `name`, `cn_name`, `note`, `table_prefix`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (30004, 'DWD', '明细数据层', '通过企业的业务活动事件构建数据模型。基于具体业务事件的特点，构建最细粒度的明细数据事实表。', 'dwd', 0, 10000, '2022-10-08 17:18:13', 10000, '2022-10-08 17:18:18');
INSERT INTO `data_house_layer`(`id`, `name`, `cn_name`, `note`, `table_prefix`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (30006, 'DWS', '汇总数据层', '通过分析的主题对象构建数据模型。基于上层的应用和产品的指标需求，构建公共粒度的汇总指标表。', 'dws', 0, 10000, '2022-10-08 17:20:03', 10000, '2022-10-08 17:20:09');
INSERT INTO `data_house_layer`(`id`, `name`, `cn_name`, `note`, `table_prefix`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (30007, 'ADS', '应用数据层', '用于存放数据产品个性化的统计指标数据，输出各种报表', 'ads', 0, 10000, '2022-10-08 17:20:52', 10000, '2022-10-08 17:20:57');

