package com.data.a1_common

import java.util

/**
  *
  */
class a2_type {


    /**
      * 两种定义方式一样
      */
    val x = new util.HashMap[Int, String]()
    val y: Map[Int, String] = new util.HashMap()
}
