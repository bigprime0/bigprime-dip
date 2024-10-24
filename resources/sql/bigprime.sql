
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for data_database
-- ----------------------------
DROP TABLE IF EXISTS `data_database`;
CREATE TABLE `data_database`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '源名称',
  `product` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品',
  `summary` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品配置信息json格式',
  `active` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否有效',
  `deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
  `creator` bigint(0) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(0) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据集成-数据库管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_database
-- ----------------------------


-- ----------------------------
-- Table structure for di_config_category
-- ----------------------------
DROP TABLE IF EXISTS `di_config_category`;
CREATE TABLE `di_config_category`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '配置项标识',
  `pid` bigint(0) NULL DEFAULT NULL COMMENT '配置项父标识',
  `route` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '配置路径',
  `label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '配置项名称',
  `is_leaf` int(0) NULL DEFAULT NULL COMMENT '是否叶子节点',
  `is_add` int(0) NULL DEFAULT NULL COMMENT '可以添加子项',
  `category` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '配置项分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '数据集成-分类配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of di_config_category
-- ----------------------------
INSERT INTO `di_config_category` VALUES (1, 0, 'root', 'root', 0, 0, '');
INSERT INTO `di_config_category` VALUES (2, 1, 'root/seatunnel', 'seatunnel', 0, 0, 'product');
INSERT INTO `di_config_category` VALUES (3, 2, 'root/seatunnel/env', 'env', 1, 0, '');
INSERT INTO `di_config_category` VALUES (4, 2, 'root/seatunnel/source', 'source', 0, 1, '');
INSERT INTO `di_config_category` VALUES (5, 2, 'root/seatunnel/transform', 'transform', 0, 1, '');
INSERT INTO `di_config_category` VALUES (6, 2, 'root/seatunnel/sink', 'sink', 0, 1, '');
INSERT INTO `di_config_category` VALUES (7, 4, 'root/seatunnel/source/Jdbc', 'Jdbc', 1, 0, 'connector');
INSERT INTO `di_config_category` VALUES (8, 5, 'root/seatunnel/transform/Replace', 'Replace', 1, 0, 'transform');
INSERT INTO `di_config_category` VALUES (9, 6, 'root/seatunnel/sink/Jdbc', 'Jdbc', 1, 0, 'connector');
INSERT INTO `di_config_category` VALUES (10, 5, 'root/seatunnel/transform/Filter', 'Filter', 1, 0, NULL);
INSERT INTO `di_config_category` VALUES (12, 4, 'root/seatunnel/source/FakeSource', 'FakeSource', 1, 0, 'connector');
INSERT INTO `di_config_category` VALUES (13, 6, 'root/seatunnel/sink/Console', 'Console', 1, 0, 'connector');
INSERT INTO `di_config_category` VALUES (14, 1, 'root/addax', 'addax', 0, NULL, 'product');
INSERT INTO `di_config_category` VALUES (15, 14, 'root/addax/setting', 'setting', 1, NULL, 'setting');
INSERT INTO `di_config_category` VALUES (16, 14, 'root/addax/reader', 'reader', 0, NULL, '');
INSERT INTO `di_config_category` VALUES (17, 14, 'root/addax/writer', 'writer', 0, NULL, '');
INSERT INTO `di_config_category` VALUES (18, 16, 'root/addax/reader/rdbmsreader', 'rdbmsreader', 1, NULL, 'connector');
INSERT INTO `di_config_category` VALUES (19, 17, 'root/addax/writer/rdbmswriter', 'rdbmswriter', 1, 0, 'connector');
INSERT INTO `di_config_category` VALUES (20, 16, 'root/addax/reader/streamreader', 'streamreader', 1, 0, 'connector');
INSERT INTO `di_config_category` VALUES (22, 17, 'root/addax/writer/txtfilewriter', 'txtfilewriter', 1, 0, 'connector');
INSERT INTO `di_config_category` VALUES (23, 16, 'root/addax/reader/excelreader', 'excelreader', 1, 0, 'connector');

-- ----------------------------
-- Table structure for di_config_detail
-- ----------------------------
DROP TABLE IF EXISTS `di_config_detail`;
CREATE TABLE `di_config_detail`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '配置项标识',
  `product` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '产品',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '分类',
  `sub_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '子类',
  `config_group` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '配置项分组',
  `config_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '配置项',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '配置项',
  `config_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '配置项标题',
  `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '适用版本',
  `seq` int(0) NULL DEFAULT NULL COMMENT '排序字段',
  `cn_description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '中文描述',
  `en_description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '英文描述',
  `is_visible` tinyint(1) NULL DEFAULT NULL COMMENT '配置项是否可见',
  `is_property` tinyint(1) NULL DEFAULT NULL COMMENT '是否任务配置项',
  `is_enable` tinyint(1) NULL DEFAULT NULL COMMENT '是否开启配置',
  `comp_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '控件类型',
  `comp_config` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '控制配置',
  `default_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '默认值',
  `creator` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(0) NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `di_config_detail__uk`(`product`, `category`, `sub_category`, `config_group`, `config_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '数据集成-明细配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of di_config_detail
-- ----------------------------
INSERT INTO `di_config_detail` VALUES (2, 'seatunnel', 'env', NULL, '', 'job_name', 'job.name', '任务名称', 'universal', 20, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (3, 'seatunnel', 'env', NULL, '', 'job_mode', 'job.mode', '任务模式', 'universal', 30, '', '', 1, 1, 1, 'select', 'BATCH:批处理|STREAMING:流程处理', 'BATCH', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (4, 'seatunnel', 'env', NULL, '', 'parallelism', 'parallelism', '并行度', 'universal', 40, '', '', 0, 1, 1, 'numberInput', '', '1', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (5, 'seatunnel', 'source', 'Jdbc', '', 'datasource', 'datasource', '数据源', 'universal', 50, '', '', 1, 0, 1, 'datasource', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (6, 'seatunnel', 'source', 'Jdbc', '', 'url', 'url', '数据库连接串', 'universal', 60, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (7, 'seatunnel', 'source', 'Jdbc', '', 'driver', 'driver', '数据库连接驱动名', 'universal', 70, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (8, 'seatunnel', 'source', 'Jdbc', '', 'user', 'user', '数据库用户名', 'universal', 80, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (9, 'seatunnel', 'source', 'Jdbc', '', 'password', 'password', '数据库密码', 'universal', 90, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (10, 'seatunnel', 'source', 'Jdbc', '', 'table_path', 'table_path', '表路径', 'universal', 100, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (11, 'seatunnel', 'source', 'Jdbc', '', 'query', 'query', '查询语句', 'universal', 110, '', '', 1, 1, 1, 'textarea', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (12, 'seatunnel', 'transform', 'Replace', '', 'replace_field', 'replace_field', '需要替换的字段', 'universal', 120, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (13, 'seatunnel', 'transform', 'Replace', '', 'pattern', 'pattern', '被替换的旧字符串', 'universal', 130, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (14, 'seatunnel', 'transform', 'Replace', '', 'replacement', 'replacement', '替换的新字符串', 'universal', 140, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (15, 'seatunnel', 'transform', 'Replace', '', 'is_regex', 'is_regex', '正则表达式', 'universal', 150, '', '', 1, 1, 1, 'switch', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (16, 'seatunnel', 'transform', 'Replace', '', 'replace_first', 'replace_first', '是否替换第一个匹配字符串', 'universal', 160, '', '', 1, 1, 1, 'switch', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (17, 'seatunnel', 'transform', 'Filter', '', 'include_fields', 'include_fields', '保留的字段列表', 'universal', 170, '需要保留的字段列表。不在列表中的字段将被删除。注意，include_fields 和 exclude_fields 两个属性中，必须设置一个且只能设置一个\n例：include_fields = [name, card]', '', 1, 1, 1, 'array', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (18, 'seatunnel', 'transform', 'Filter', '', 'exclude_fields', 'exclude_fields', '删除的字段列表', 'universal', 180, '需要删除的字段列表。不在列表中的字段将被保留。注意，include_fields 和 exclude_fields 两个属性中，必须设置一个且只能设置一个\n例：exclude_fields = [age]', '', 1, 1, 1, 'array', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (19, 'seatunnel', 'transform', 'Filter', '', 'source_table_name', 'source_table_name', 'source_table_name', 'universal', 190, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (20, 'seatunnel', 'transform', 'Filter', '', 'result_table_name', 'result_table_name', 'result_table_name', 'universal', 200, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (22, 'seatunnel', 'transform', 'Replace', '', 'source_table_name', 'source_table_name', 'source_table_name', 'universal', 20, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (23, 'seatunnel', 'transform', 'Replace', '', 'result_table_name', 'result_table_name', 'result_table_name', 'universal', 30, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (24, 'seatunnel', 'sink', 'Jdbc', '', 'datasource', 'datasource', '数据源', 'universal', 10, '', '', 1, 0, 1, 'datasource', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (25, 'seatunnel', 'sink', 'Jdbc', '', 'url', 'url', '数据库连接串', 'universal', 20, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (26, 'seatunnel', 'sink', 'Jdbc', '', 'driver', 'driver', '数据库连接驱动名', 'universal', 30, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (27, 'seatunnel', 'sink', 'Jdbc', '', 'user', 'user', '数据库用户名', 'universal', 40, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (28, 'seatunnel', 'sink', 'Jdbc', '', 'password', 'password', '数据库密码', 'universal', 50, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (29, 'seatunnel', 'sink', 'Jdbc', '', 'query', 'query', '写入语句', 'universal', 60, '使用 sql 语句将上游输入数据写入到数据库，例：query = \"insert into test_table(name,age) values(?,?)\"', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (30, 'seatunnel', 'sink', 'Jdbc', '', 'database', 'database', '数据库名', 'universal', 70, '使用此 database 和 table-name 自动生成 SQL，并接收上游输入的数据写入数据库。\n此选项与 query 选项是互斥的，此选项具有更高的优先级。', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (31, 'seatunnel', 'sink', 'Jdbc', '', 'table ', 'table ', '表名', 'universal', 80, '使用 database 和此 table-name 自动生成 SQL，并接收上游输入的数据写入数据库。此选项与 query 选项是互斥的，此选项具有更高的优先级。\ntable参数可以填入一个任意的表名，这个名字最终会被用作创建表的表名，并且支持变量（${table_name}，${schema_name}）。 替换规则如下：${schema_name} 将替换传递给目标端的 SCHEMA 名称，${table_name} 将替换传递给目标端的表名。\nmysql 接收器示例:\ntest${schema_name}${table_name}_test\nsink_sinktable\nss_${table_name}\npgsql (Oracle Sqlserver ...) 接收器示例:\n${schema_name}.${table_name}_test\ndbo.tt_${table_name}_sink\npublic.sink_table\nTip: 如果目标数据库有 SCHEMA 的概念，则表参数必须写成 xxx.xxx', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (32, 'seatunnel', 'sink', 'Jdbc', '', 'source_table_name', 'source_table_name', 'source_table_name', 'universal', 90, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (33, 'seatunnel', 'sink', 'Jdbc', '', 'result_table_name', 'result_table_name', 'result_table_name', 'universal', 100, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (34, 'seatunnel', 'source', 'Jdbc', '', 'source_table_name', 'source_table_name', 'source_table_name ', 'universal', 110, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (35, 'seatunnel', 'source', 'Jdbc', '', 'result_table_name', 'result_table_name', 'result_table_name', 'universal', 120, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (36, 'seatunnel', 'env', NULL, '', 'rest_api', 'rest_api', 'Api地址', 'universal', 10, '', '', 1, 0, 1, 'textInput', '', 'http://localhost:5801/hazelcast/rest/maps', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (37, 'seatunnel', 'env', NULL, '', 'event_url', 'event_url', '事件上报地址', 'universal', 20, '', '', 1, 0, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (38, 'seatunnel', 'source', 'FakeSource', '', 'schema', 'schema', '模式(Schema)', 'universal', 10, '', '', 1, 1, 1, 'json', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (39, 'seatunnel', 'sink', 'Console', '', 'source_table_name', 'source_table_name', 'source_table_name', 'universal', 20, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (40, 'seatunnel', 'source', 'FakeSource', '', 'result_table_name', 'result_table_name', 'result_table_name', 'universal', 30, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (41, 'addax', 'setting', NULL, 'speed', 'byte', 'byte', '字节数(Bps)/秒', 'universal', 10, '设置每秒可获取的字节数(Bps)，一般是为了防止执行任务时将整个带宽跑满，从而影响到其他服务。如果不做限制，可设置为 -1', '', 1, 1, 1, 'numberInput', '', '-1', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (42, 'addax', 'setting', NULL, 'speed', 'record', 'record', '记录数/秒', 'universal', 20, '设置记录每秒可获取的最大记录条数，该参数需要和 speed.byte 配合使用', '', 1, 1, 1, 'numberInput', '', '1000', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (43, 'addax', 'setting', NULL, 'speed', 'channel', 'channel', '通道数', 'universal', 30, '设置通道数，该通道路确定了总的 Task 线程数，假定设定 speed.channel 为 13， 则一共有 13 个 Task. 然后根据 conf/core.json 配置中的 taskGroup.channel 配置来确定要创建的 taskGroup 数量。即 taskGroup = speed.channel / taskGroup.channel.', '', 1, 1, 1, 'numberInput', '', '1', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (44, 'addax', 'setting', NULL, 'errorLimit', 'record', 'record', '允许错误记录数', 'universal', 40, '允许错误的记录条数，如果超过这个数，则认为本次任务失败，否则认为成功', '', 0, 1, 0, 'numberInput', '', '0', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (45, 'addax', 'setting', NULL, 'errorLimit', 'percentage', 'percentage', '允许错误记录比率', 'universal', 50, '', '', 0, 1, 0, 'numberInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (46, 'addax', 'reader', 'rdbmsreader', '', 'name', 'name', '读插件名', 'universal', 60, '', '', 1, 1, 1, 'textInput', '', 'rdbmsreader', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (47, 'addax', 'reader', 'rdbmsreader', '', 'username', 'username', '数据源的用户名', 'universal', 70, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (48, 'addax', 'reader', 'rdbmsreader', '', 'password', 'password', '数据源密码', 'universal', 80, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (49, 'addax', 'reader', 'rdbmsreader', '', 'driver', 'driver', '驱动类名', 'universal', 69, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (50, 'addax', 'reader', 'rdbmsreader', 'connection', 'table', 'table', '同步的表名', 'universal', 100, '所选取的需要同步的表名,使用JSON数据格式，当配置为多张表时，用户自己需保证多张表是同一表结构', '', 1, 1, 1, 'array', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (51, 'addax', 'reader', 'rdbmsreader', 'connection', 'jdbcUrl', 'jdbcUrl', '数据库连接串', 'universal', 68, '', '', 1, 1, 1, 'array', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (52, 'addax', 'reader', 'rdbmsreader', '', 'column', 'column', '同步的列名', 'universal', 120, '', '', 1, 1, 1, 'array', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (53, 'addax', 'reader', 'rdbmsreader', '', 'querySql', 'querySql', '查询语句', 'universal', 130, '使用自定义的SQL而不是指定表来获取数据，当配置了这一项之后，忽略 table，column配置项忽略', '', 1, 1, 1, 'textarea', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (54, 'addax', 'reader', 'rdbmsreader', '', 'fetchSize', 'fetchSize', '批量获取条数', 'universal', 140, '定义了插件和数据库服务器端每次批量数据获取条数，调高该值可能导致 Addax 出现OOM', '', 0, 1, 0, 'numberInput', '', '1024', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (55, 'addax', 'reader', 'rdbmsreader', '', 'session', 'session', '写入数据的时间格式', 'universal', 150, '', '', 0, 1, 0, 'array', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (56, 'addax', 'reader', 'rdbmsreader', '', 'splitPk', 'splitPk', '按主键分片', 'universal', 160, '使用splitPk代表的字段进行数据分片，splitPk 仅支持整形、字符串型数据(ASCII类型) 切分，不支持浮点、日期等其他类型。 如果用户指定其他非支持类型，RDBMSReader 将报错！如果不填写，将视作用户不对单表进行切分，而使用单通道同步全量数据', '', 0, 1, 0, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (57, 'addax', 'reader', 'rdbmsreader', '', 'autoPk', 'autoPk', '自动猜测主键分片', 'universal', 170, '支持自动获取表主键或唯一索引，如果设置为 true ，将尝试通过查询数据库的元数据信息获取指定表的主键字段或唯一索引字段，如果获取可用于分隔的 字段不止一个，则默认取第一个。', '', 0, 1, 0, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (58, 'addax', 'writer', 'rdbmswriter', '', 'name', 'name', '写插件名', 'universal', 180, '', '', 1, 1, 1, 'textInput', '', 'rdbmswriter', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (59, 'addax', 'writer', 'rdbmswriter', 'connection', 'jdbcUrl', 'jdbcUrl', '数据库连接串', 'universal', 190, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (60, 'addax', 'writer', 'rdbmswriter', '', 'driver', 'driver', '驱动名', 'universal', 200, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (61, 'addax', 'writer', 'rdbmswriter', '', 'username', 'username', '数据库用户名', 'universal', 210, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (62, 'addax', 'writer', 'rdbmswriter', '', 'password', 'password', '数据库密码', 'universal', 220, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (63, 'addax', 'writer', 'rdbmswriter', 'connection', 'table', 'table', '写入表名', 'universal', 230, '', '', 1, 1, 1, 'array', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (64, 'addax', 'writer', 'rdbmswriter', '', 'column', 'column', '写入的列名集合', 'universal', 240, '', '', 0, 1, 0, 'array', '', '*', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (65, 'addax', 'writer', 'rdbmswriter', '', 'preSql', 'preSql', '同步前执行', 'universal', 250, '执行数据同步任务之前率先执行的sql语句，目前只允许执行一条SQL语句，例如清除旧数据,涉及到的表可用 @table表示', '', 0, 1, 0, 'textarea', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (66, 'addax', 'writer', 'rdbmswriter', '', 'postSql', 'postSql', '同步后执行', 'universal', 260, '执行数据同步任务之后执行的sql语句，目前只允许执行一条SQL语句，例如加上某一个时间戳', '', 0, 1, 0, 'textarea', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (67, 'addax', 'setting', NULL, '', 'addax_home', 'addax_home', 'Addax安装路径', 'universal', 270, '', '', 1, 0, 1, 'textInput', '', 'D:/Download/addax', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (68, 'addax', 'setting', NULL, '', 'event_url', 'event_url', '任务回调地址', 'universal', 280, '', '', 1, 0, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (69, 'addax', 'reader', 'rdbmsreader', '', 'datasource', 'datasource', '数据源', 'universal', 61, '', '', 1, 0, 1, 'datasource', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (70, 'addax', 'writer', 'rdbmswriter', '', 'datasource', 'datasource', '数据源', 'universal', 181, '', '', 1, 0, 1, 'datasource', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (71, 'addax', 'setting', NULL, '', 'job_name', 'job.name', '任务名称', 'universal', 1, '', '', 1, 0, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (72, 'addax', 'reader', 'streamreader', '', 'column', 'column', '字段列结构', 'universal', 10, '', '', 1, 1, 1, 'json', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (73, 'addax', 'reader', 'streamreader', '', 'sliceRecordCount', 'sliceRecordCount', '生成数据条数', 'universal', 20, '', '', 1, 1, 1, 'numberInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (74, 'addax', 'writer', 'txtfilewriter', '', 'path', 'path', '文件路径', 'universal', 30, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (75, 'addax', 'writer', 'txtfilewriter', '', 'fileName', 'fileName', '文件名', 'universal', 40, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (76, 'addax', 'writer', 'txtfilewriter', '', 'writeMode', 'writeMode', '写入模式', 'universal', 50, '', '', 1, 1, 1, 'select', 'truncate:写入前清理目录|append:合并写入|nonConflict:存在就报错', '', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (77, 'addax', 'writer', 'txtfilewriter', '', 'encoding', 'encoding', '文件编码', 'universal', 60, '', '', 1, 1, 1, 'textInput', '', 'UTF-8', 10000, NOW(), 10000, NOW(), 0);
INSERT INTO `di_config_detail` VALUES (78, 'addax', 'reader', 'excelreader', '', 'path', 'path', '文件路径', 'universal', 10, '', '', 1, 1, 1, 'textInput', '', '', 10000, NOW(), 10000, NOW(), 0);

-- ----------------------------
-- Table structure for di_job
-- ----------------------------
DROP TABLE IF EXISTS `di_job`;
CREATE TABLE `di_job`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '配置项标识',
  `engine` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '产品',
  `type` int(0) NULL DEFAULT 0 COMMENT '业务类型',
  `job_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '提交地址',
  `form_datas` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '配置内容',
  `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '提交内容',
  `write_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '写类型',
  `write_source_id` bigint(0) NULL DEFAULT NULL COMMENT '写数据源ID',
  `write_source_database` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '写数据源库',
  `write_source_table` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '写数据源表',
  `reader_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '读类型',
  `reader_source_id` bigint(0) NULL DEFAULT NULL COMMENT '读数据源ID',
  `reader_source_database` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '读数据源库',
  `reader_source_table` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '读数据源表',
  `analyze_sql` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '解析sql',
  `creator` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(0) NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `write_index`(`type`, `write_source_id`, `write_source_table`) USING BTREE,
  INDEX `reader_index`(`type`, `reader_source_id`, `reader_source_table`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '数据集成-集成任务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of di_job
-- ----------------------------

-- ----------------------------
-- Table structure for di_job_log
-- ----------------------------
DROP TABLE IF EXISTS `di_job_log`;
CREATE TABLE `di_job_log`  (
  `job_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务标识',
  `job_defined_id` bigint(0) NULL DEFAULT NULL COMMENT '配置项标识',
  `engine` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '产品',
  `type` int(0) NULL DEFAULT 0 COMMENT '业务类型',
  `job_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务名称',
  `form_datas` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '配置内容',
  `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '提交内容',
  `write_source_id` bigint(0) NULL DEFAULT NULL COMMENT '写数据源ID',
  `write_source_table` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '写数据源表',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务状态',
  `message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '返回消息',
  `logs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '日志内容',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`job_id`) USING BTREE,
  INDEX `type_index`(`type`, `write_source_id`, `write_source_table`, `job_defined_id`) USING BTREE,
  INDEX `status_index`(`status`, `type`) USING BTREE COMMENT '状态'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '数据集成-集成任务运行' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of di_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_integration_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_integration_config`;
CREATE TABLE `sys_integration_config`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '标识',
  `product` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '同步工具名',
  `db_type` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '数据库类型',
  `category` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '分类',
  `sub_category` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '子类',
  `applicable_version` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '适用版本',
  `label` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '配置项',
  `real_label` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '映射实际任务配置项',
  `title` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '配置项标题',
  `descr` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '配置项描述',
  `value_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '值类型',
  `default_value` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '默认值',
  `comp_type` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '控件类型',
  `comp_config` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '控件配置',
  `is_required` int(0) NULL DEFAULT 1 COMMENT '是否必须项（0是 1否）',
  `is_advanced` int(0) NULL DEFAULT 0 COMMENT '是否高级属性（0缓存 1不缓存）',
  `creator` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(0) NULL DEFAULT 0 COMMENT '逻辑删除',
  `seq` int(0) NULL DEFAULT 0 COMMENT '排序列',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 96 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '集成配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_integration_config
-- ----------------------------
INSERT INTO `sys_integration_config` VALUES (10, 'seatunnel', 'mysql', 'env', '', '1', '2', '2', '', '', 'text', '1', 'textInput', '', 1, 0, '10000', NOW(), '10000', NOW(), 1, 0);
INSERT INTO `sys_integration_config` VALUES (11, 'seatunnel', 'oracle', 'env', '', '1', '2', '2', '', '', 'text', '1', 'textInput', '', 1, 0, '10000', NOW(), '10000', NOW(), 1, 0);
INSERT INTO `sys_integration_config` VALUES (12, 'seatunnel', 'mysql', 'env', '', '1', '2', '2', '', '', 'text', '1', 'textInput', '', 1, 0, '10000', NOW(), '10000', NOW(), 1, 0);
INSERT INTO `sys_integration_config` VALUES (13, 'seatunnel', '', 'env', '', '', 'jobName', 'job.name', '任务名称', '参数配置任务名称', 'text', '', 'textInput', '', 1, 0, '10000', NOW(), '10000', NOW(), 0, 1);
INSERT INTO `sys_integration_config` VALUES (14, 'seatunnel', '', 'env', '', '', 'jobMode', 'job.mode', '任务模式', '通过job.mode你可以配置任务是在批处理模式还是流处理模式。例如：job.mode = \"BATCH\" 或者 job.mode = \"STREAMING\"', 'text', 'BATCH', 'select', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 2);
INSERT INTO `sys_integration_config` VALUES (15, 'seatunnel', '', 'env', '', '', 'parallelism', 'parallelism', '并行度', '配置source和sink的并行度', 'number', '1', 'numberInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 3);
INSERT INTO `sys_integration_config` VALUES (16, 'seatunnel', '', 'env', '', '', 'shadeIdentifier', 'shade.identifier', '加解密方式', '配置文件加密和解密,SeaTunnel 具备Base64编码和解码的功能，但不建议在生产环境中使用,Base64编码支持加密以下参数：username,password,auth', 'text', '', 'select', '[ { label: \'不加密\', value: \'\' }, { label: \'Base64\', value: \'base64\' }]', 0, 1, '10000', NOW(), '10000', NOW(), 0, 4);
INSERT INTO `sys_integration_config` VALUES (17, 'seatunnel', '', 'env', '', '', 'bytes_per_second', 'read_limit.bytes_per_second', '速度控制(行数)', '速度控制管理数据同步的速率(按行数）', 'number', '-1', 'numberInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 5);
INSERT INTO `sys_integration_config` VALUES (18, 'seatunnel', '', 'env', '', '', 'bytes_per_second', 'read_limit.bytes_per_second', '速度控制(字节)', '速度控制管理数据同步的速率(按字节数)', 'number', '-1', 'numberInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (19, 'seatunnel', '', 'env', '', '', 'checkpointInterval', 'checkpoint.interval', '检查点的时间间隔', '配置定时调度检查点的时间间隔；注意：在STREAMING模式下，检查点是必须的，如果不设置，将从应用程序配置文件seatunnel.yaml中获取。 在BATCH模式下，您可以通过不设置此参数来禁用检查点。', 'number', '', 'numberInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 7);
INSERT INTO `sys_integration_config` VALUES (20, 'seatunnel', 'jdbc', 'source', '', '', 'url', 'url', '连接URL', 'JDBC连接的URL', 'text', '', 'textInput', '', 1, 0, '10000', NOW(), '10000', NOW(), 0, 10);
INSERT INTO `sys_integration_config` VALUES (21, 'seatunnel', 'jdbc', 'source', '', '', 'driver', 'driver', '连接驱动名', '连接到远程数据源的jdbc类名', 'text', '', 'textInput', '', 1, 0, '10000', NOW(), '10000', NOW(), 0, 11);
INSERT INTO `sys_integration_config` VALUES (22, 'seatunnel', 'jdbc', 'source', '', '', 'user', 'user', '连接用户名', '连接实例用户名', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 12);
INSERT INTO `sys_integration_config` VALUES (23, 'seatunnel', 'mysql', 'source', '', '', 'password', 'password', '连接密码', '连接实例密码', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 1, 13);
INSERT INTO `sys_integration_config` VALUES (24, 'seatunnel', 'jdbc', 'source', '', '', 'password', 'password', '连接密码', '连接密码', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 13);
INSERT INTO `sys_integration_config` VALUES (25, 'seatunnel', 'jdbc', 'source', '', '', 'query', 'query', '查询语句', '源查询语句', 'text', '', 'textarea', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 14);
INSERT INTO `sys_integration_config` VALUES (26, 'seatunnel', 'jdbc', 'source', '', '', 'partition_num', 'partition_num', '并行读取器', '并行读取规则：拆分密钥规则:\n1、如果partition_column不为null，它将用于计算split。列必须在支持的拆分数据类型。\n2、如果partition_column为null，则seatunnel将从表中读取架构并获取主键和唯一索引。如果主键和唯一索引中有多个列，则支持的拆分数据类型将用于拆分数据。例如，表具有主键 (nn guid，名称varchar)，因为guidid不在支持的', 'number', '', 'numberInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 16);
INSERT INTO `sys_integration_config` VALUES (27, 'seatunnel', 'jdbc', 'source', '', '', 'partition_column', 'partition_column', '并行读取列', '并行度分区的列名，仅支持数值类型，仅支持数值类型主键，且只能配置一列。', 'text', '', 'textInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 15);
INSERT INTO `sys_integration_config` VALUES (28, 'seatunnel', 'jdbc', 'source', '', '', 'fetch_size', 'fetch_size', '取数大小', '对于返回大量对象的查询，您可以配置查询中用于提高性能的行提取大小减少满足选择标准所需的数据库命中数。零表示使用jdbc默认值。', 'number', '-1', 'numberInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 17);
INSERT INTO `sys_integration_config` VALUES (29, 'seatunnel', 'jdbc', 'source', '', '', 'properties', 'properties', '属性', '其他连接配置参数，当属性和URL具有相同的参数时，优先级由驱动程序的具体实现。例如，在MySQL中，属性优先于URL。示例：\n {\n         useSSL=false\n        }', 'text', '', 'textarea', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 18);
INSERT INTO `sys_integration_config` VALUES (30, 'seatunnel', 'jdbc', 'source', '', '', 'table_path', 'table_path', '表路径', '表的完整路径的路径，您可以使用此配置而不是query。\n示例:\nmysql: \"testdb.table1\"\noracle: \"test_schema.table1\"\nsqlserver: \"testdb.test_schema.table1\"\npostgresql: \"testdb.test_schema.table1\"', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 19);
INSERT INTO `sys_integration_config` VALUES (31, 'seatunnel', 'jdbc', 'source', '', '', 'table_list', 'table_list', '表路径集合', '要读取的表的列表，您可以使用此配置而不是table_path示例:\n[\n    { table_path = \"testdb.table1\"}, \n    {table_path = \"testdb.table2\", query = \"select * id, name from testdb.table2\"}\n]', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 20);
INSERT INTO `sys_integration_config` VALUES (32, 'seatunnel', 'jdbc', 'source', '', '', 'where_condition', 'where_condition', '通用条件', '所有表/查询的通用行筛选条件，必须以开头where。例如where id > 100', 'text', '', 'textInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 21);
INSERT INTO `sys_integration_config` VALUES (33, 'seatunnel', 'jdbc', 'source', '', '', 'split_size', 'split.size', '拆分大小', '表的拆分大小 (行数)，捕获的表在读取表时会拆分成多个拆分。', 'text', '', 'textInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 22);
INSERT INTO `sys_integration_config` VALUES (34, 'seatunnel', 'jdbc', 'source', '', '', 'result_table_name', 'result_table_name', '结果数据集名称', '当result_table_name未指定，则此插件处理的数据将不会注册为数据集(dataStream/dataset)可以由其他插件直接访问，或称为临时表(table);\n当result_table_name指定，则此插件处理的数据将注册为数据集(dataStream/dataset)可以由其他插件直接访问，或称为临时表(table)。数据集(dataStream/dataset)在此注册的其他插件可以通过指定直接访问source_table_name。', 'text', '', 'textInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 1);
INSERT INTO `sys_integration_config` VALUES (35, 'seatunnel', 'jdbc', 'sink', '', '', 'url', 'url', '目标连接url串', 'JDBC连接的URL', 'text', '', 'textInput', '', 1, 0, '10000', NOW(), '10000', NOW(), 0, 10);
INSERT INTO `sys_integration_config` VALUES (36, 'seatunnel', 'jdbc', 'sink', '', '', 'driver', 'driver', '连接驱动名', '连接到远程数据源的jdbc类名', 'text', '', 'textInput', '', 1, 0, '10000', NOW(), '10000', NOW(), 0, 11);
INSERT INTO `sys_integration_config` VALUES (37, 'seatunnel', 'jdbc', 'sink', '', '', 'user', 'user', '连接用户名', '连接实例用户名', 'text', '', 'textInput', '', 1, 0, '10000', NOW(), '10000', NOW(), 0, 12);
INSERT INTO `sys_integration_config` VALUES (38, 'seatunnel', 'jdbc', 'sink', '', '', 'password', 'password', '连接密码', '	连接实例密码', 'text', '', 'textInput', '', 1, 0, '10000', NOW(), '10000', NOW(), 0, 13);
INSERT INTO `sys_integration_config` VALUES (39, 'seatunnel', 'jdbc', 'sink', '', '', 'query', 'query', '写入语句', '使用此sql将上游输入数据写入数据库。示例：\"insert into test_table(name,age) values(?,?)\"', 'text', '', 'textarea', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 14);
INSERT INTO `sys_integration_config` VALUES (40, 'seatunnel', 'jdbc', 'sink', '', '', 'database', 'database', '目标数据库', '使用这个database和table-name自动生成sql并接收上游输入数据写入数据库。\n此选项与query具有更高的优先级', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 15);
INSERT INTO `sys_integration_config` VALUES (41, 'seatunnel', 'jdbc', 'sink', '', '', 'table', 'table', '目标表名', '使用数据库和这个表名自动生成sql并接收上游输入数据写入数据库。此选项与query具有更高的优先级。', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 15);
INSERT INTO `sys_integration_config` VALUES (42, 'seatunnel', 'jdbc', 'sink', '', '', 'primary_keys', 'primary_keys', '主键', '此选项用于支持诸如insert,delete,和update时自动生成sql。', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 16);
INSERT INTO `sys_integration_config` VALUES (43, 'seatunnel', 'jdbc', 'sink', '', '', 'max_retries', 'max_retries', '重试次数', '提交失败的重试次数 (executeBatch)', 'text', '-1', 'numberInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 21);
INSERT INTO `sys_integration_config` VALUES (44, 'seatunnel', 'jdbc', 'sink', '', '', 'is_exactly_once', 'is_exactly_once', '启用精确一次语义', '是否启用exactly-once语义，这将使用Xa事务。如果打开，则需要设置xa_data_source_class_name。', 'boolean', '', 'switch', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 22);
INSERT INTO `sys_integration_config` VALUES (45, 'seatunnel', 'jdbc', 'sink', '', '', 'generate_sink_sql', 'generate_sink_sql', '生成sql语句', '根据要写入的数据库表生成sql语句', 'boolean', '', 'switch', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 23);
INSERT INTO `sys_integration_config` VALUES (46, 'seatunnel', 'jdbc', 'sink', '', '', 'xa_data_source_class_name', 'xa_data_source_class_name', 'XA驱动名', '数据库驱动的xa数据源类名，例如mysql为com.mysql.cj.jdbc.MysqlXADataSource', 'text', '', 'textInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 23);
INSERT INTO `sys_integration_config` VALUES (47, 'seatunnel', 'jdbc', 'sink', '', '', 'batch_size', 'batch_size', '批量写入大小', '对于批量写入，当缓冲的记录数达到batch_size或时间到达checkpoint.interval,数据将被刷新到数据库中', 'number', '', 'numberInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 24);
INSERT INTO `sys_integration_config` VALUES (48, 'seatunnel', 'jdbc', 'sink', '', '', 'max_commit_attempts', 'max_commit_attempts', '提交重试次数', '事务提交失败的重试次数', 'number', '-1', 'numberInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 25);
INSERT INTO `sys_integration_config` VALUES (49, 'seatunnel', 'jdbc', 'sink', '', '', 'auto_commit', 'auto_commit', '自动提交', '默认情况下启用自动事务提交', 'boolean', '', 'switch', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 26);
INSERT INTO `sys_integration_config` VALUES (50, 'seatunnel', 'jdbc', 'sink', '', '', 'field_ide', 'field_ide', '是否转换字段', '标识从源同步到接收器时是否需要转换字段。ORIGINAL表示不需要转换；UPPERCASE表示转换为大写；LOWERCASE指示转换为小写。', 'text', 'ORIGINAL', 'select', '[ { label: \'不转换\', value: \'ORIGINAL\' }, { label: \'转大写\', value: \'UPPERCASE\' }, { label: \'转小写\', value: \'LOWERCASE\' }]', 0, 1, '10000', NOW(), '10000', NOW(), 0, 27);
INSERT INTO `sys_integration_config` VALUES (51, 'seatunnel', 'jdbc', 'sink', '', '', 'properties', 'properties', '属性', '其他连接配置参数，当属性和URL具有相同的参数时，优先级由\n驱动程序的具体实现。例如，在MySQL中，属性优先于URL。', 'text', '', 'textarea', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (52, 'seatunnel', 'jdbc', 'sink', '', '', 'source_table_name', 'source_table_name', '源数据集名称', '', 'text', '', 'textInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 2);
INSERT INTO `sys_integration_config` VALUES (53, 'seatunnel', 'jdbc', 'sink', '', '', 'schema_save_mode', 'schema_save_mode', 'Schema保存模式', '在启动同步任务之前，针对目标侧已有的表结构选择不同的处理方案', 'list', '', 'select', '[ { label: \'表不存在时会创建,存在删除重建\', value: \'RECREATE_SCHEMA\' }, { label: \'表不存在时会创建，当表已存在时则跳过创建\', value: \'CREATE_SCHEMA_WHEN_NOT_EXIST\' }, { label: \'表不存在时将抛出错误\', value: \'ERROR_WHEN_SCHEMA_NOT_EXIST\' }]', 0, 1, '10000', NOW(), '10000', NOW(), 0, 3);
INSERT INTO `sys_integration_config` VALUES (54, 'seatunnel', 'jdbc', 'sink', '', '', 'data_save_mode', 'data_save_mode', '数据保存模式', '在启动同步任务之前，针对目标侧已存在的数据选择不同的处理方案', 'list', '', 'select', '[ { label: \'保留数据库结构，删除数据\', value: \'DROP_DATA\' }, { label: \'保留数据库结构，保留数据，当表已存在时则跳过创建\', value: \'APPEND_DATA\' }, { label: \'允许用户自定义数据处理方式\', value: \'CUSTOM_PROCESSING\' }, { label: \'当有数据时抛出错误\', value: \'ERROR_WHEN_DATA_EXISTS\' }]', 0, 1, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (55, 'seatunnel', 'jdbc', 'sink', '', '', 'custom_sql', 'custom_sql', '同步前自定义sql', '当data_save_mode=CUSTOM_PROCESSING,用户在同步前执行自定义SQL', 'text', '', 'textarea', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (56, 'seatunnel', '', 'transform', '', '', 'Copy ', 'Copy ', '复制 ', '将字段复制到一个新字段。示例：  Copy {\n    source_table_name = \"fake\"\n    result_table_name = \"fake1\"\n    fields {\n      name1 = name\n      name2 = name\n      age1 = age\n    }\n  }', 'text', '', 'textarea', '', 0, 0, '10000', NOW(), '10000', NOW(), 1, 0);
INSERT INTO `sys_integration_config` VALUES (57, 'seatunnel', '', 'transform', '', '', 'result_table_name', 'result_table_name', '来源结果集表名', '当未指定result_table_name时，此插件处理的数据不会被注册为其他插件可以直接访问的数据集，也不会被称为临时表(table);当指定了result_table_name时，此插件处理的数据将被注册为其他插件可以直接访问的数据集(dataset),或者被称为临时表(table)。在这里注册的数据集可以通过指定source_table_name被其他插件直接访问。', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 1);
INSERT INTO `sys_integration_config` VALUES (58, 'seatunnel', '', 'transform', '', '', 'source_table_name', 'source_table_name', '目标结果集表名', '当未指定source_table_name时，当前插件在配置文件中处理由前一个插件输出的数据集(dataset);\n当指定了source_table_name时，当前插件正在处理与该参数对应的数据集', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 1);
INSERT INTO `sys_integration_config` VALUES (59, 'seatunnel', '', 'transform', '', '', 'FieldMapper', 'FieldMapper', '映射', '输入和输出之间的字段映射关系，示例： \n{\n	\"a\": \"a1\",\n	\"b\": \"b1 \"\n}', 'text', '', 'textarea', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (60, 'seatunnel', '', 'transform', '', '', 'Filter', 'Filter', '过滤', '需要保留的字段列表。不在列表中的字段将被删除，示例：\n   [”name“, ”card“]', 'text', '', 'textarea', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (61, 'seatunnel', '', 'transform', '', '', 'Replace', 'Replace', '替换', '检查给定字段中的字符串值，并用给定的替换项替换与给定字符串字面量或正则表达式匹配的字符串值的子字符串。示例：\n{\n	\"replace_field\": \"name\",\n	\"pattern\": \" \",\n	\"replacement\": \"_\",\n	\"is_regex\": true\n}', 'text', '', 'textarea', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (62, 'seatunnel', '', 'transform', '', '', 'Split', 'Split', '拆分', '拆分一个字段为多个字段。示例：\n{\n	\"separator\": \" \",\n	\"split_field\": \"name\",\n	\"output_fields\": [\n		\"first_name\", \"second_name\"\n	]\n}', 'text', '', 'textarea', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (63, 'seatunnel', '', 'transform', '', '', 'SQL', 'SQL', 'SQL转换', '使用SQL来转换给定的输入行,使用内存中的SQL引擎，我们可以通过SQL函数和SQL引擎的能力来实现转换任务。示例：\n{\n	\"query\": \"select id, concat(name, \'_\') as name, age+1 as age from fake where id>0\"\n}', 'text', '', 'textarea', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (64, 'seatunnel', '', 'transform', '', '', 'Copy', 'Copy', '复制', '将字段复制到一个新字段。示例： \n{\n	\"a\": \"a1\",\n	\"b\": \" b1\"\n}', 'text', '', 'textarea', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (65, 'datax', 'rdbms', 'setting', 'speed', '', 'byte', 'byte', '获取的字节数(Bps)', '设置每秒可获取的字节数(Bps)，一般是为了防止执行任务时将整个带宽跑满，从而影响到其他服务。如果不做限制，可设置为 -1', 'number', '-1', 'numberInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 10);
INSERT INTO `sys_integration_config` VALUES (66, 'datax', 'rdbms', 'setting', 'speed', '', 'record', 'record', '最大记录条数', '设置记录每秒可获取的最大记录条数，该参数需要和 speed.byte 配合使用', 'number', '-1', 'numberInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 11);
INSERT INTO `sys_integration_config` VALUES (67, 'datax', 'rdbms', 'setting', 'speed', '', 'channel', 'channel', '通道数', '设置通道数，该通道路确定了总的 Task 线程数，假定设定 speed.channel 为 13， 则一共有 13 个 Task. 然后根据 conf/core.json 配置中的 taskGroup.channel 配置来确定要创建的 taskGroup 数量。即 taskGroup = speed.channel / taskGroup.channel.', 'number', '1', 'numberInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 12);
INSERT INTO `sys_integration_config` VALUES (68, 'datax', 'rdbms', 'setting', 'errorLimit', '', 'record', 'record', '允许错误的记录条数', '允许错误的记录条数，如果超过这个数，则认为本次任务失败，否则认为成功', 'number', '-1', 'numberInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 15);
INSERT INTO `sys_integration_config` VALUES (69, 'datax', 'rdbms', 'setting', 'errorLimit', '', 'percentage', 'percentage', '允许错误记录的比率', '允许错误记录的比率，超过这个比率，则认为本次任务失败，否则认为成功', 'number', '-1', 'numberInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 16);
INSERT INTO `sys_integration_config` VALUES (70, 'datax', 'rdbms', 'reader', '', '', 'name', 'name', '读取器名称', '', 'text', '', 'textInput', '', 1, 0, '10000', NOW(), '10000', NOW(), 0, 20);
INSERT INTO `sys_integration_config` VALUES (71, 'datax', 'rdbms', 'reader', '', '', 'username', 'username', '用户名', '数据源的用户名', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 21);
INSERT INTO `sys_integration_config` VALUES (72, 'datax', 'rdbms', 'reader', '', '', 'password', 'password', '密码', '数据源指定用户名的密码', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 22);
INSERT INTO `sys_integration_config` VALUES (73, 'datax', 'rdbms', 'reader', '', '', 'column', 'column', '列名集合', '所配置的表中需要同步的列名集合，使用JSON的数组描述字段信息。用户使用 * 代表默认使用所有列配置，例如 [\"*\"]，也可以指定列名集合', 'array', ' [\"*\"]', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 23);
INSERT INTO `sys_integration_config` VALUES (74, 'datax', 'rdbms', 'reader', '', '', 'splitPk', 'splitPk', '指定分割键', '如果指定 splitPk，表示用户希望使用 splitPk 代表的字段进行数据分片，因此会启动并发任务进行数据同步，这样可以大大提供数据同步的效能。\n推荐 splitPk 用户使用表主键，因为表主键通常情况下比较均匀，因此切分出来的分片也不容易出现数据热点。\n目前 splitPk 仅支持整形、字符串型数据(ASCII类型) 切分，不支持浮点、日期等其他类型。 如果用户指定其他非支持类型，RDBMSReader 将报错！\nsplitPk 如果不填写，将视作用户不对单表进行切分，而使用单通道同步全量数据。', 'text', '', 'textInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 25);
INSERT INTO `sys_integration_config` VALUES (75, 'datax', 'rdbms', 'reader', '', '', 'autoPk', 'autoPk', '是否自动分片', '支持自动获取表主键或唯一索引，如果设置为 true ，将尝试通过查询数据库的元数据信息获取指定表的主键字段或唯一索引字段，如果获取可用于分隔的 字段不止一个，则默认取第一个。该特性目前支持的数据库有：ClickHouse、MySQL、Oracle、PostgreSQL、SQL Server', 'boolean', '', 'switch', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 26);
INSERT INTO `sys_integration_config` VALUES (76, 'datax', 'rdbms', 'reader', 'connection', '', 'table', 'table', '同步表名', '所选取的需要同步的表名,使用JSON数据格式，当配置为多张表时，用户自己需保证多张表是同一表结构', 'array', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (77, 'datax', 'rdbms', 'reader', 'connection', '', 'jdbcUrl', 'jdbcUrl', '数据源连接串', 'jdbcUrl 配置除了配置必要的信息外，我们还可以在增加每种特定驱动的特定配置属性，这里特别提到我们可以利用配置属性对代理的支持从而实现通过代理访问数据库的功能。 比如对于 PrestoSQL 数据库的 JDBC 驱动而言，支持 socksProxy 参数，于是上述配置的 jdbcUrl 可以修改为\njdbc:presto://127.0.0.1:8080/hive?socksProxy=192.168.1.101:1081\n大部分关系型数据库的 JDBC 驱动支持 socksProxyHost,socksProxyPort 参数来支持代理访问。也有一些特别的情况。', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 27);
INSERT INTO `sys_integration_config` VALUES (78, 'datax', 'rdbms', 'reader', '', '', 'driver', 'driver', '数据源驱动名', '', 'text', '', 'textInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 28);
INSERT INTO `sys_integration_config` VALUES (79, 'datax', 'rdbms', 'reader', '', '', 'session', 'session', '写入数据的时间格式', '控制写入数据的时间格式，时区等的配置，目前仅对 MySQL, Oracle, SQLServer 有效。下面是一个针对 Oracle 数据库配置 session 的例子。\n{\n  \"session\": [\n    \"alter session set NLS_DATE_FORMAT=\'yyyy-mm-dd hh24:mi:ss\'\",\n    \"alter session set NLS_TIMESTAMP_FORMAT=\'yyyy-mm-dd hh24:mi:ss\'\",\n    \"alter session set NLS_TIMESTAMP_TZ_FORMAT=\'yyyy-mm-dd hh', 'text', '', 'textarea', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (80, 'datax', 'rdbms', 'reader', '', '', 'where', 'where', '表筛选条件', '针对表的筛选条件', 'text', '', 'textInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (81, 'datax', 'rdbms', 'reader', '', '', 'fetchSize', 'fetchSize', '批量获取条数', '定义了插件和数据库服务器端每次批量数据获取条数，调高该值可能导致 Addax 出现OOM', 'number', '-1', 'numberInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (82, 'datax', 'rdbms', 'reader', 'connection', '', 'querySql', 'querySql', '查询语句', '使用自定义的SQL而不是指定表来获取数据，当配置了这一项之后，忽略 table，column配置项忽略', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 29);
INSERT INTO `sys_integration_config` VALUES (83, 'datax', 'rdbms', 'writer', '', '', 'name', 'name', '写入器名称', '', 'text', '', 'textInput', '', 1, 0, '10000', NOW(), '10000', NOW(), 0, 31);
INSERT INTO `sys_integration_config` VALUES (84, 'datax', 'rdbms', 'writer', 'connection', '', 'jdbcUrl', 'jdbcUrl', '写入数据源URL', 'jdbcUrl 配置除了配置必要的信息外，我们还可以在增加每种特定驱动的特定配置属性，这里特别提到我们可以利用配置属性对代理的支持从而实现通过代理访问数据库的功能。 比如对于 PrestoSQL 数据库的 JDBC 驱动而言，支持 socksProxy 参数，比如一个可能的 jdbcUrl 为\njdbc:presto://127.0.0.1:8080/hive?socksProxy=192.168.1.101:1081\n大部分关系型数据库的 JDBC 驱动支持 socksProxyHost,socksProxyPort 参数来支持代理访问。也有一些特别的情况。', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (85, 'datax', 'rdbms', 'writer', 'connection', '', 'table', 'table', '写入表名称', '', 'array', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 32);
INSERT INTO `sys_integration_config` VALUES (86, 'datax', 'rdbms', 'writer', '', '', 'username', 'username', '写入源用户名', '', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 33);
INSERT INTO `sys_integration_config` VALUES (87, 'datax', 'rdbms', 'writer', '', '', 'password', 'password', '写入源密码', '', 'text', '', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 34);
INSERT INTO `sys_integration_config` VALUES (88, 'datax', 'rdbms', 'writer', '', '', 'driver', 'driver', '写入源驱动', '一个数据库的JDBC驱动是固定的，但有些因为版本的不同，所建议的驱动类名不同，比如 MySQL。 新的 MySQL JDBC 驱动类型推荐使用 com.mysql.cj.jdbc.Driver 而不是以前的 com.mysql.jdbc.Drver。 如果想要使用就的驱动名称，则可以配置 driver 配置项。', 'text', '', 'textInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (89, 'datax', 'rdbms', 'writer', '', '', 'column', 'column', '写入源数据列', '', 'array', '[\"*\"]', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (90, 'datax', 'rdbms', 'writer', '', '', 'preSql', 'preSql', '前置执行语句', '执行数据同步任务之前率先执行的sql语句，目前只允许执行一条SQL语句，例如清除旧数据,涉及到的表可用 @table表示', 'array', '', 'textInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 35);
INSERT INTO `sys_integration_config` VALUES (91, 'datax', 'rdbms', 'writer', '', '', 'postSql', 'postSql', '后置语句', '执行数据同步任务之后执行的sql语句，目前只允许执行一条SQL语句，例如加上某一个时间戳', 'array', '', 'textInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (92, 'datax', 'rdbms', 'writer', '', '', 'batchSize', 'batchSize', '批量获取条数', '定义了插件和数据库服务器端每次批量数据获取条数，调高该值可能导致 Addax 出现OOM或者目标数据库事务提交失败导致挂起', 'number', '-1', 'numberInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (93, 'datax', 'rdbms', 'writer', '', '', 'session', 'session', '连接session', '描述：设置数据库连接时的session信息，比如针对 Oracle 数据库，可以设置如下：\n{\n  \"session\": [\n    \"alter session set nls_date_format = \'dd.mm.yyyy hh24:mi:ss\';\",\n    \"alter session set NLS_LANG = \'AMERICAN\';\"\n  ]\n}', 'array', '', 'textInput', '', 0, 1, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (94, 'seatunnel', '', 'global', '', '', 'home', '', '安装目录', '', 'text', '/usr/local/seatunnel', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 0);
INSERT INTO `sys_integration_config` VALUES (95, 'datax', '', 'global', '', '', 'home', 'home', '安装目录', '', 'text', '/usr/local/addax', 'textInput', '', 0, 0, '10000', NOW(), '10000', NOW(), 0, 0);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pid` bigint(0) NULL DEFAULT NULL COMMENT '上级ID，一级菜单为0',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `authority` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授权标识(多个用逗号分隔，如：sys:menu:list,sys:menu:save)',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
  `creator` bigint(0) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(0) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_pid`(`pid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 328 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (1, 0, 'Sys', NULL, NULL, 'IconSetting', 90, 0, 10000, NOW(), 10000, NOW());
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (2, 1, 'Menu', 'sys/menu/index', NULL, 'IconEditorMenuRight', 4, 0, 10000, NOW(), 10000, NOW());
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (224, 1, 'Org', 'sys/org/index', NULL, 'IconVersiontree', 1, 0, 10000, NOW(), 10000, NOW());
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (225, 1, 'User', 'sys/user/index', NULL, 'IconUser', 3, 0, 10000, NOW(), 10000, NOW());
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (227, 1, 'Role', 'sys/role/index', NULL, 'IconRichTextAlignCenter', 2, 0, 10000, NOW(), 10000, NOW());
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (242, 0, 'Integration', '', NULL, 'IconTaskCooperation', 3, 0, 10000, NOW(), 10000, NOW());
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (243, 242, 'Setting', 'integration/setting/index', NULL, 'IconSetting', 1, 0, 10000, NOW(), 10000, NOW());
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (244, 242, 'Define', 'integration/job/job-define-list', NULL, 'IconWriteProductioPlan', 2, 0, 10000, NOW(), 10000, NOW());
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (245, 242, 'Instance', 'integration/job/job-instance-list', NULL, 'IconSeparate', 3, 0, 10000, NOW(), 10000, NOW());
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (314, 0, 'Dsm', '', NULL, 'IconEditorAlignCenter', 1, 0, 10000, NOW(), 10000, NOW());
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (316, 314, 'Dsm-maintenance', 'data-source/components/dsm-maintenance', NULL, 'IconDataSource', 1, 0, 10000, NOW(), 10000, NOW());
INSERT INTO `sys_menu`(`id`, `pid`, `name`, `url`, `authority`, `icon`, `sort`, `deleted`, `creator`, `create_time`, `updater`, `update_time`) VALUES (317, 314, 'Dsm-query', 'data-source/components/dsm-query', NULL, 'IconExpressSearch', 2, 0, 10000, NOW(), 10000, NOW());
-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pid` bigint(0) NULL DEFAULT NULL COMMENT '上级ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '机构名称',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
  `creator` bigint(0) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(0) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_pid`(`pid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '机构管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_org
-- ----------------------------
INSERT INTO `sys_org` VALUES (8, 0, 'Bigprime', 1, 0, 10000, NOW(), 10000, NOW());
INSERT INTO `sys_org` VALUES (9, 8, '子公司', 1, 0, 10000, NOW(), 10000, NOW());
INSERT INTO `sys_org` VALUES (10, 0, '二级分公司', 2, 0, 10000, NOW(), 10000, NOW());
INSERT INTO `sys_org` VALUES (11, 10, '技术部门', 1, 0, 10000, NOW(), 10000, NOW());
INSERT INTO `sys_org` VALUES (12, 8, '二级子公司', 2, 0, 10000, NOW(), 10000, NOW());

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
  `creator` bigint(0) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(0) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` bigint(0) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(0) NULL DEFAULT NULL COMMENT '菜单ID',
  `deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
  `creator` bigint(0) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(0) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_menu_id`(`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` tinyint(0) NULL DEFAULT NULL COMMENT '性别   0：男   1：女   2：未知',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `org_id` bigint(0) NULL DEFAULT NULL COMMENT '机构ID',
  `super_admin` tinyint(0) NULL DEFAULT NULL COMMENT '超级管理员   0：否   1：是',
  `status` tinyint(0) NULL DEFAULT NULL COMMENT '状态  0：停用   1：正常',
  `deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
  `creator` bigint(0) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(0) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10001 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (10000, 'admin', '{bcrypt}$2a$10$mW/yJPHjyueQ1g26WNBz0uxVPa0GQdJO1fFZmqdkqgMTGnyszlXxu', '管理员', '', 1, '', '', 8, 1, 1, 0, 10000, NOW(), 10000, NOW());

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` bigint(0) NULL DEFAULT NULL COMMENT '角色ID',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户ID',
  `deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
  `creator` bigint(0) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(0) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
