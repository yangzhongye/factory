/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/9/9 9:48:17                             */
/*==============================================================*/


drop table if exists goods;

drop table if exists goods_img;

drop table if exists goods_in_out_detail;

drop table if exists user;

/*==============================================================*/
/* Table: goods                                                 */
/*==============================================================*/
create table goods
(
   id                   varchar(64) not null comment 'id',
   seq_no               bigint auto_increment comment '序号',
   number               varchar(64) comment '编号',
   name                 varchar(128) comment '名称',
   quantity             decimal(18,2) comment '数量',
   note                 varchar(256) comment '备注',
   create_time          datetime comment '创建时间',
   create_user_id       varchar(64) comment '创建用户id',
   create_user_name     varchar(128) comment '创建用户名字',
   update_time          datetime comment '修改时间',
   update_user_id       varchar(64) comment '修改用户id',
   update_user_name     varchar(128) comment '修改用户名字',
   primary key (id),
   unique key AK_Key_2 (seq_no)
);

alter table goods comment '物品表';

/*==============================================================*/
/* Table: goods_img                                             */
/*==============================================================*/
create table goods_img
(
   id                   varchar(64) not null comment 'id',
   goods_id             varchar(64) comment '物品id',
   content              longblob comment '图片内容',
   original_name        varchar(256) comment '图片原名称',
   primary key (id)
);

alter table goods_img comment '物品图片表';

/*==============================================================*/
/* Table: goods_in_out_detail                                   */
/*==============================================================*/
create table goods_in_out_detail
(
   id                   varchar(64) not null comment 'id',
   goods_id             varchar(64) comment '物品id',
   type                 char(1) comment '类型 1-进 0-出',
   change_quantity      decimal(18,2) comment '变化数量',
   current_quantity     decimal(18,2) comment '当前数量',
   create_time          datetime comment '创建时间',
   oper_user_id         varchar(64) comment '操作用户id',
   oper_user_name       varchar(128) comment '操作用户名字',
   primary key (id)
);

alter table goods_in_out_detail comment '物品流水表';

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   id                   varchar(64) not null comment 'id',
   name                 varchar(128) comment '名字',
   login_name           varchar(128) comment '登录名',
   login_pwd            varchar(64) comment '登录密码',
   create_time          datetime comment '创建时间',
   status               char(1) comment '状态 1-启用 0-禁用',
   update_time          datetime comment '修改时间',
   mng_status           char(1) comment '是否管理 1-是 0-否',
   primary key (id)
);

alter table user comment '用户表';

