drop table if exists test_json;


create table test_json(
	`id` int primary key auto_increment comment 'id',
	`str_arr`  json comment '字符串arr',
	`json_obj` json comment 'json object',
	`json_obj_arr` json comment 'json object 数组'
)engine = innodb auto_increment = 10000 comment '测试json表';

insert into test_json(str_arr,json_obj,json_obj_arr)
values
(
JSON_ARRAY('hello','world','static'),
JSON_OBJECT("name","Lyanna","age",18),
JSON_ARRAY(
	JSON_OBJECT("name","zzj1","age",18),
	JSON_OBJECT("name","zzj2","age",19),
	JSON_OBJECT("name","zzj3","age",20),
	JSON_OBJECT("name","zzj3","age",21)
)
);

select * from test_json;