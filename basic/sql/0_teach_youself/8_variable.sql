
# https://www.cnblogs.com/EasonJim/p/7966918.html
###############################################################################################
# 用户变量，用户登录过程有效

# 赋值方式1：设置用户变量
set @total = 0;

# 赋值方式2：使用select赋值时，必须使用 :=, 因为这里 =被当做比较运算符
select @name := cust_name from Customers where cust_id = 1000000001;

select @total as total, @name as name;

###############################################################################################
DELIMITER $$
drop procedure if exists math;
create procedure math(in a int, in b int)
begin
  set @var1 = 1;
  set @var2 = 2;

  select @sum:=(a + b) as sum, @dif:=(a - b) as dif;
end;$$
DELIMITER;

call math(10, 20);
select @var1, @var2;

###############################################################################################
# 局部变量，只能在函数中使用
DELIMITER $$
drop procedure if exists math;
create procedure math(in a int, in b int)
  begin
    declare sum int default 0;
    declare dif int default 0;
    declare name VARCHAR(255);

    set @var1 = 1;
    set @var2 = 2;

    /** 局部变量赋值方式1 */
    set sum = a * b;
    set dif = a / b;

    /** 局部变量赋值方式2 */
    select cust_name into name from Customers LIMIT 1;
    select sum, dif, name;
  end;$$
DELIMITER ;
call math(10, 20);

/** 赋值方式 */

###############################################################################################
# 会话变量
show session variables;
set session var_name = 111;
set @@session.var_name2 = 222;
set var_name = 333;

###############################################################################################
# 全局变量
show global variables;

set global var_name = "111";
select @@global.var_name;
show global variables like "%var%";
