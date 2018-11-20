package com.data.a3_collection

import scala.collection.immutable.Queue

/**
  * 特质和类
  *     Iterable
  *         Seq：
  *             List、ListBuffer
  *             Array、ArrayBuffer
  *             Queue、Stack
  *             RichString：Predef包含了String到RickString的隐式转换，RickString继承自 Seq[Char]
  *         Set、Map
  *             Map
  *         SortedMap
  *             TreeMap，相关的类型参数，必须能够混入，或者可以隐式转换为Ordered特质
  *
  * 集合互转：P228
  *     1. list -> Set、Map，使用 空的set ++ List （例如：TreeSet[String]() ++ List() ） P230
  *     2. toList、toArray
  *     3. 可变不可变转换（空的不可变 + 可变即可 -> 转换为了不可变集合）
  *
  * 原则：
  *   1. 优先使用不可变集合；或者发现不可变集合导致代码混乱时，使用不可变集合
  *   2. 元素个数不多时，使用不可变集合更加紧凑
  *
  */
class a0_common {
    val a = Array(1, 2, 3)

    val b = List(1, 2, 3)

    val c = Queue(1, 2, 3)
    c.enqueue()
}
