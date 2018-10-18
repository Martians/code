
##################################################################################################################
CREATE TABLE OrderItems
(
  order_num   INTEGER     NOT NULL,
  order_item  INTEGER     NOT NULL,
  prod_id     CHAR(10)    NOT NULL,
  quantity    INTEGER     NOT NULL DEFAULT 1,
  item_price  DECIMAL(8,2) NOT NULL
);

##################################################################################################################
# 修改表
ALTER TABLE Vendors
  ADD vend_phone CHAR(20);

ALTER TABLE Vendors
  DROP COLUMN vend_phone;
