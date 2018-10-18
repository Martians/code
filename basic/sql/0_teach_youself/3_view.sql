
##################################################################################################################
# 创建视图，简化复杂的联接，隐藏复杂的语句
CREATE VIEW ProductCustomers AS
  SELECT cust_name, cust_contact, prod_id
  FROM Customers, Orders, OrderItems
  WHERE Customers.cust_id = Orders.cust_id
        AND OrderItems.order_num = Orders.order_num;

# 使用
SELECT cust_name, cust_contact
  FROM ProductCustomers
  WHERE prod_id = 'RGAN01';


##################################################################################################################
# 创建视图时，设置了选择条件
CREATE VIEW CustomerEMailList AS
  SELECT cust_id, cust_name, cust_email
  FROM Customers
  WHERE cust_email IS NOT NULL;

# 使用视图的时候，也可以再设置条件，与视图的条件where进行自动组合
SELECT *
  FROM CustomerEMailList
  where cust_name like 'F%';