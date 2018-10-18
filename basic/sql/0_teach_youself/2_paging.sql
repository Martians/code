
SELECT * FROM Orders
  WHERE order_num >= 20006
  LIMIT 2 OFFSET 0;

/**
 * 返回第二行开始的2行数据
 */
SELECT * FROM Orders
  WHERE order_num >= 20006
  LIMIT 2,2;
