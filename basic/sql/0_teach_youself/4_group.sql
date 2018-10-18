
##################################################################################################################
# WHERE过滤行，而 HAVING 过滤分组，表明一个分组是否需要显示出来
# WHERE 在数据分组前进行过滤， HAVING 在数据分组后进行过滤

/** 具有两个以上产品且其价格大于等于 4 的供应商 */
# 按照供应商分组
SELECT vend_id, COUNT(*) AS num_prods
FROM Products
WHERE prod_price >= 4
GROUP BY vend_id
HAVING COUNT(*) >= 2;