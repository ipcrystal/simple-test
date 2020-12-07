drop table if exists test_json;

create table test_json(
	id int primary key auto_increment comment 'id',
	strs json comment 'json 字段'
)engine = innodb auto_increment = 10000 comment '测试json表';

insert into test_json(strs)
values
(JSON_ARRAY('hello','world','static'));
