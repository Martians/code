
##################################################################################################################
# 笛卡尔积、叉联结（cross join）
SELECT vend_name, prod_name, prod_price
FROM Vendors, Products;

##################################################################################################################
# 内联接，等值联接
SELECT vend_name, prod_name, prod_price
FROM Vendors, Products
WHERE Vendors.vend_id = Products.vend_id;

# 内联接，这种写法更标准
SELECT vend_name, prod_name, prod_price
FROM Vendors INNER JOIN Products
    ON Vendors.vend_id = Products.vend_id;

# 自然联接，使每一列只返回一次
#   系统不完成这项工作，由你自己完成它，自己指定要输出的列
#   对表进行联结，应该至少有一列不止出现在一个表中，这样联接才有意义

##################################################################################################################
# 返回订购产品RGAN01 的顾客列表
# 多表联接

/** 返回顶戴20007中的商品 */
SELECT prod_name, vend_name, prod_price, quantity
FROM OrderItems, Products, Vendors
WHERE Products.vend_id = Vendors.vend_id
      AND OrderItems.prod_id = Products.prod_id
      AND order_num = 20007;

/** 订购产品RGAN01 的顾客列表 */
SELECT cust_name, cust_contact
FROM Customers, Orders, OrderItems
WHERE Customers.cust_id = Orders.cust_id
      AND OrderItems.order_num = Orders.order_num
      AND prod_id = 'RGAN01';

# 等同于
SELECT cust_name, cust_contact
FROM Customers
WHERE cust_id IN (SELECT cust_id
                  FROM Orders
                  WHERE order_num IN (SELECT order_num
                                      FROM OrderItems
                                      WHERE prod_id = 'RGAN01'));

##################################################################################################################
# 自连接
/** 与 Jim Jones 同一公司的所有顾客 */
SELECT cust_id, cust_name, cust_contact
FROM Customers
WHERE cust_name = (SELECT cust_name
                   FROM Customers
                   WHERE cust_contact = 'Jim Jones');

SELECT c1.cust_id, c1.cust_name, c1.cust_contact
FROM Customers AS c1, Customers AS c2
WHERE c1.cust_name = c2.cust_name
      AND c2.cust_contact = 'Jim Jones';

##################################################################################################################
# 左外链接
SELECT Customers.cust_id, Orders.order_num
FROM Customers LEFT OUTER JOIN Orders
    ON Customers.cust_id = Orders.cust_id;

##################################################################################################################
# UNION 将多个结果集合（行集合），合并在一起
SELECT cust_name, cust_contact, cust_email
FROM Customers
WHERE cust_state IN ('IL','IN','MI')
UNION
SELECT cust_name, cust_contact, cust_email
FROM Customers
WHERE cust_name = 'Fun4All';

# 等同于多条件查询
SELECT cust_name, cust_contact, cust_email
FROM Customers
WHERE cust_state IN ('IL','IN','MI')
      OR cust_name = 'Fun4All';
