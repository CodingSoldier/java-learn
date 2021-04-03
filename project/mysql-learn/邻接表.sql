
# https://ikutarian.github.io/2019/06/13/%E5%A6%82%E4%BD%95%E5%9C%A8%E6%95%B0%E6%8D%AE%E5%BA%93%E4%B8%AD%E5%AD%98%E5%82%A8%E4%B8%80%E6%A3%B5%E6%A0%91%EF%BC%8C%E5%AE%9E%E7%8E%B0%E6%97%A0%E9%99%90%E7%BA%A7%E5%88%86%E7%B1%BB/
闭包表

/* 组织机构 */
CREATE TABLE sys_org
(
  id         BIGINT,        /* 机构id */  
  name       VARCHAR(50)    /* 机构名称 */
);
INSERT INTO "public"."sys_org"("id", "name") VALUES (1, '华南分公司');
INSERT INTO "public"."sys_org"("id", "name") VALUES (2, '产品部');
INSERT INTO "public"."sys_org"("id", "name") VALUES (3, '研发部');
INSERT INTO "public"."sys_org"("id", "name") VALUES (4, '后台开发');
INSERT INTO "public"."sys_org"("id", "name") VALUES (5, 'JAVA开发');
INSERT INTO "public"."sys_org"("id", "name") VALUES (6, 'Python开发');
INSERT INTO "public"."sys_org"("id", "name") VALUES (7, '前端开发');
INSERT INTO "public"."sys_org"("id", "name") VALUES (8, '网页开发');
INSERT INTO "public"."sys_org"("id", "name") VALUES (9, 'Android开发');


/* 祖先与后代树 */
CREATE TABLE tree_path
(
  ancestor     BIGINT,   /* 祖先的id */  
  descendant   BIGINT    /* 后代的id */
);

INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (1, 1);
INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (1, 2);
INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (1, 3);
INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (1, 4);
INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (1, 5);
INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (1, 6);
INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (1, 7);
INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (1, 8);
INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (1, 9);
INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (3, 3);
INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (3, 4);
INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (3, 5);
INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (3, 6);
INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (3, 7);
INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (3, 8);
INSERT INTO "public"."tree_path"("ancestor", "descendant") VALUES (3, 9);



-- 查询组织机构id为3的子机构
SELECT org.* FROM sys_org org, tree_path tree
WHERE tree.ancestor=3 AND tree.descendant=org.id;

-- 查询组织机构id为2的父级机构
SELECT org.* FROM sys_org org, tree_path tree WHERE tree.descendant=2 AND tree.ancestor=org.id;

SELECT org.* FROM sys_org org, tree_path tree WHERE tree.descendant=3 AND tree.ancestor=org.id;

SELECT org.* FROM sys_org org, tree_path tree WHERE tree.descendant=9 AND tree.ancestor=org.id;

-- 插入一个新组织机构
INSERT INTO "public"."sys_org"("id", "name") VALUES (10, 'C++开发');

-- 更新tree表，此组织机构位于 4-后台开发部门 下
INSERT INTO tree_path (ancestor, descendant)
SELECT t.ancestor, 10
FROM tree_path AS t
WHERE t.descendant = 4
UNION ALL SELECT 10, 10

/*
解析

查询 4-后台开发部门 的所有父级机构，与当前组织机构的id
SELECT t.ancestor, 10
FROM tree_path AS t
WHERE t.descendant = 4

拼接上当前组织机构的id（闭包表的要求）
SELECT t.ancestor, 10
FROM tree_path AS t
WHERE t.descendant = 4
UNION ALL SELECT 10, 10

*/
