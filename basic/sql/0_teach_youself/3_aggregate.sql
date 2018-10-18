
# 聚合函数

# 在多列进行的
SELECT SUM(item_price*quantity) AS total_price
FROM OrderItems
WHERE order_num = 20005;

# 计算包括空值
SELECT COUNT(*) AS num_cust
FROM Customers;

# 不包括这一列的空值
SELECT COUNT(cust_email) AS num_cust
FROM Customers;
