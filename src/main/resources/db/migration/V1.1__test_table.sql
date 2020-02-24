--
-- 表的结构 `test`
--

CREATE TABLE `test` (
                           `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                           `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '姓名',
                           `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                           `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           `del_status` tinyint(1) DEFAULT '0' COMMENT '软删除标志   1-是 0-否 ',
                           `created_by` int(11) DEFAULT '0' COMMENT '创建人',
                           `updated_by` int(11) DEFAULT '0' COMMENT '修改人',
                           PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;