
INSERT INTO Customers(cust_id,
                      cust_name,
                      cust_address,
                      cust_city,
                      cust_state,
                      cust_zip,
                      cust_country,
                      cust_contact,
                      cust_email)
            VALUES('1000000006',
                   'Toy Land',
                   '123 Any Street',
                   'New York',
                   'NY',
                   '11111',
                   'USA',
                   NULL,
                   NULL)


##################################################################################################################
# insert select, 插入的数据是刚查询出来的
INSERT INTO Customers(cust_id,
                      cust_contact,
                      cust_email,
                      cust_name,
                      cust_address,
                      cust_city,
                      cust_state,
                      cust_zip,
                      cust_country)
  SELECT cust_id,
    cust_contact,
    cust_email,
    cust_name,
    cust_address,
    cust_city,
    cust_state,
    cust_zip,
    cust_country
  FROM CustNew;

##################################################################################################################
# 表复制, SELECT INTO

# SELECT * INTO CustCopy FROM Customers;
CREATE TABLE CustCopy AS
  SELECT * FROM Customers;
select * from CustCopy;

##################################################################################################################
# 更新数据
UPDATE Customers
SET cust_contact = 'Sam Roberts',
  cust_email = 'sam@toyland.com'
WHERE cust_id = '1000000006';

# 删除某个列
UPDATE Customers
SET cust_email = NULL
WHERE cust_id = '1000000005';

# 删除行
DELETE FROM Customers
WHERE cust_id = '1000000006';
# TRUNCATE TABLE更快，因为不需要记录数据的变动