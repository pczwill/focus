--
-- 表的结构 `auth_user`
--

CREATE TABLE `auth_user` (
                           `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                           `company_id` int(10) UNSIGNED DEFAULT NULL COMMENT '公司',
                           `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '邮箱',
                           `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
                           `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '姓名',
                           `user_status` tinyint(3) UNSIGNED DEFAULT '0' COMMENT '用户状态 0--正常 1--禁用',
                           `department_id` int(3) UNSIGNED DEFAULT NULL,
                           `position_id` int(3) UNSIGNED DEFAULT NULL COMMENT '职位',
                           `role` varchar(255) DEFAULT '',
                           `avatar` varchar(255) DEFAULT '' COMMENT '头像',
                           `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                           `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           `kpi_score` tinyint(3) NOT NULL DEFAULT '0' COMMENT '最新kpi得分',
                           `del_status` tinyint(1) DEFAULT '0' COMMENT '软删除标志   1-是 0-否 ',
                           `created_by` int(11) DEFAULT '0' COMMENT '创建人',
                           `updated_by` int(11) DEFAULT '0' COMMENT '修改人',
                           PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- 转存表中的数据 `auth_user`
--

INSERT INTO `auth_user` (`id`, `company_id`, `email`, `password`, `name`, `user_status`, `department_id`, `position_id`, `role`, `avatar`, `created_at`, `updated_at`, `kpi_score`, `del_status`, `created_by`, `updated_by`) VALUES
(1, 1, 'futuremapadmin@futuremap.com.cn', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', '管理用户', 0, 1, 1, 'ADMIN', 'https://cdn1.iconfinder.com/data/icons/user-pictures/101/malecostume-512.png', '2018-12-30 04:02:58', '2019-04-22 16:40:15', 20, 0, 0, 0);