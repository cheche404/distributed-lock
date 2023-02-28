-- 删除 t_lock 表
drop table t_lock;
-- 创建 t_lock 表
CREATE TABLE `t_lock`  (
`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增序号',
`name` varchar(255) NOT NULL COMMENT '锁名称',
`survival_time` int(11) NOT NULL COMMENT '存活时间，单位ms',
`create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
`thread_name` varchar(255) NOT NULL COMMENT '线程名称',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE INDEX `uk_name`(`name`) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Dynamic;