

##################################################################################################################
# 过滤条件

# not 在比较复杂的语句中很有用
SELECT prod_name
FROM Products
WHERE NOT vend_id = 'DLL01'
ORDER BY prod_name;


##################################################################################################################
# 通配符
# 注意：通配符不要放在搜索模式开始的地方，速度会非常慢
SELECT prod_id, prod_name
FROM Products
WHERE prod_name LIKE 'Fish%';

SELECT cust_contact
FROM Customers
WHERE cust_contact LIKE '[^JM]%'
ORDER BY cust_contact;

##################################################################################################################
# 计算字段

# 拼接多个字段到一个列
SELECT Concat(vend_name, ' (', vend_country, ')')
FROM Vendors
ORDER BY vend_name;

# 数学计算
SELECT prod_id,
  quantity,
  item_price,
  quantity*item_price AS expanded_price
FROM OrderItems
WHERE order_num = 20008;

##################################################################################################################
# 使用函数
SELECT order_num
FROM Orders
WHERE YEAR(order_date) = 2012;

##################################################################################################################
# 子查询
#   作为子查询的 SELECT 语句只能查询单个列
#   尽量少使用，包含子查询难以阅读和调试

/** 订购物品 RGAN01 的所有顾客 */
# 进行查询过滤，子select只能查询单个列
SELECT cust_name, cust_contact
FROM Customers
WHERE cust_id IN (SELECT cust_id
                  FROM Orders
                  WHERE order_num IN (SELECT order_num
                                      FROM OrderItems
                                      WHERE prod_id = 'RGAN01'));
# 作为计算字段出现
#   子句中，通过 Orders.cust_id = Customers.cust_id，将外查询和内查询联系起来
SELECT cust_name,
  cust_state,
  (SELECT COUNT(*)
   FROM Orders
   WHERE Orders.cust_id = Customers.cust_id) AS orders
FROM Customers
ORDER BY cust_name;