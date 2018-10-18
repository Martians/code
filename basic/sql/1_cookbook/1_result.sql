
/** 
 * 按照字段中的部分内容，进行排序
 */
select ename,job
    from emp 
    order by substr(job, length(job) - 2);

/** 
 * 根据不同的条件，选择排序方式
 */
 select ename,sal,job,comm 
    from EMP 
    order by case when job= 'SALESMAN' then comm 
                  else sal end;