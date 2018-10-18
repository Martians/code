package com.data.a3_Collection;

import com.data.a0_util.format.CollectionF;

import java.util.HashMap;
import java.util.Map;

import static com.data.a0_util.format.Display.out;

public class A5_Map {

    static Map<Integer, String> local = new HashMap<>();

    static {
        local.put(1, "aaa");
        local.put(2, "bbb");
        local.put(3, "ccc");
        local.put(4, "ddd");
        local.put(5, "eee");
    }

    static void hashMap() {
        /**
         * loop
         */
        HashMap<Integer, String> map = new HashMap<>(local);
        out(CollectionF.list(map));

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            out("next " + entry.getKey() + " -> " + entry.getValue());
        }

        /**
         * 获取值，或者是默认值
         */
        out(map.getOrDefault(10, "not find 10"));

        /**
         * 返回新值，就替换原有value；返回null，就删除原有key
         */
        map.compute(1, (a, b) -> a < 2 ? "new value" : null);
        out(CollectionF.list(map));

        ////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * 遍历map, 并改变value的值
         */
        HashMap<Integer, StringBuffer> map2 = new  HashMap() {{
            put(1, new StringBuffer("aaa"));
            put(2, new StringBuffer("bbb"));
        }};

        map2.forEach((x, y) -> {
            out("for each: " + x + "->" + y);
            ++x;
            y.append("+++");
        });
        out(CollectionF.list(map2));
    }

    static void treeMap() {

    }

    public static void main(String args[]) {
        hashMap();
    }
}
