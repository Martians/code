
# SQL cookbook

/** 
 * 20，条件逻辑，新生成一列，对结果起别名
 */
select ename, sal,
    case when sal <= 2000 then 'underpaid'
         when sal >= 4000 then 'overpaid'
         else 'ok'
    end as status
from EMP;

/** 
 * 23 生成随机数，每次返回的都不一样
 */
select ename, job
    from EMP
    order by rand() limit 5;

 /** 
 * 41 在dept中查找，在表emp中不存在数据的所有部门；集合的差，或使用子查询
 注意空值问题
 */

/** 
 * 45 显示哪个部门没有员工；外链接，然后选择不匹配的记录
 */
select