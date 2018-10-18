
# https://www.cnblogs.com/mqxs/p/6018766.html
# http://blog.csdn.net/liguo9860/article/details/50848216#t0
##########################################################################################################
## 方式1: 使用loop

/** 修改分隔符 */
DELIMITER $$

# 设置用户变量
set @total = 0;
DROP PROCEDURE  IF EXISTS show_custumer;
CREATE PROCEDURE show_custumer(OUT name VARCHAR(255), OUT address VARCHAR(255))
BEGIN
  /** 一定要在游标、句柄之前定义 */
  declare done int default 0;
  DECLARE id int DEFAULT 0;

  /** 定义游标 */
  DECLARE custCursor CURSOR FOR
  SELECT cust_id, cust_name, cust_address FROM Customers
  WHERE cust_email IS NULL;

  /** 声明句柄，指向一条sql语句，在循环结束时发生 */
  DECLARE CONTINUE HANDLER FOR NOT FOUND set done = 1;
  /** 另一种定义方式，效果相同，使用退出码定义 */
  # declare CONTINUE HANDLER FOR SQLSTATE '02000' set done = 1;

  OPEN custCursor;
  /** 方式1：使用loop */
  readloop: LOOP
    # FETCH [NEXT | PRIOR | FIRST | LAST] FROM <游标名> [ INTO 变量名1,变量名2,变量名3[,…] ]
    FETCH custCursor into id, name, address;

    if done THEN
      LEAVE readloop;
    END if;

    /** 如果循环执行多次，这里也只会显示一个相同的值，都是最后一个值 */
    select @name, @address;
    set @total = @total + id;
  END LOOP;

  CLOSE custCursor;
  select @total;
END;
$$
/** 恢复分隔符 */
DELIMITER ;

/**
 * 执行语句，传入的是刚定义的用户变量
 */
call show_custumer(@name, @address);
/**
 * 显示时，修改列名称
 */
select @name as name, @address as address, @total;

##########################################################################################################
# 方式2：使用while

DELIMITER $$
/** 用户变量，下次调用函数仍然有效，所有要先充值为空 */
set @name_list="";
DROP PROCEDURE  IF EXISTS show_custumer;
CREATE PROCEDURE show_custumer(OUT name VARCHAR(255), OUT address VARCHAR(255))
  BEGIN

    declare done int default 0;
    DECLARE id int DEFAULT 0;
    declare tmpName varchar(20) default '' ;

    DECLARE custCursor CURSOR FOR
      SELECT cust_id, cust_name, cust_address FROM Customers
      WHERE cust_email IS NULL;

    DECLARE CONTINUE HANDLER FOR NOT FOUND set done = 1;
    OPEN custCursor;

    while(not done) do
      FETCH custCursor into id, name, address;

      set tmpName = CONCAT(name,";") ;
      set @name_list = CONCAT(@name_list, tmpName);
      set @total = @total + id;
    end while;

    CLOSE custCursor;
  END;
$$
DELIMITER ;

call show_custumer(@name, @address);
/**
 * 本例中，用户变量 total 并没有首先定义，而是在 过程使用中定义的
 */
select @total, @name_list;

##########################################################################################################
DROP FUNCTION IF EXISTS combime;
DELIMITER $$
CREATE FUNCTION combime()
returns VARCHAR(255)
  BEGIN
    declare done int default 0;
    declare name_list varchar(255) default '' ;
    declare tmpName varchar(20) default '' ;
    declare name varchar(50) default '' ;

    DECLARE custCursor CURSOR FOR
      SELECT cust_name FROM Customers
      WHERE cust_email IS NULL;

    DECLARE CONTINUE HANDLER FOR NOT FOUND set done = 1;
    OPEN custCursor;

    while(not done) do
      FETCH custCursor into name;

      set tmpName = CONCAT(name,";") ;
      set name_list = CONCAT(@name_list, tmpName);
    end while;
    return name_list;
  END;
$$
DELIMITER ;

select combime();