package com.data.a2_class

import java.util.RandomAccess

/**
  * 需要隐式转换场景：P287
  *     1. 给一个已有的类（没有其源码），添加新的特质，增加各种方法
  *
  *     2. 支持目标类型的转换，在需要目标类型的时候，可以隐式的将当前类型执行转换为目标类型
  *         1）某个操作 x + y 不能执行时，编译器可能会改为 convert(x) + y，convert把x转换为某种带了+方法的类
  *            这种方式修复程序以便让它通过类型检查；而且 convert 是一个非常简单的转换函数时，省略它可以简化代码
  *         2）系统默认存在的隐式转换：implicit int2double
  *
  *     3. 使用隐式参数：常用于提供显示标明的参数列表的类型信息，提供一种更好的组织方式 P294
  *        即将定义generic类型中的上界，可以将这个变成一个隐式参数，默认对参数提出要求；如果参数不满足，还相当于传入了一个隐式转换函数
  *
  *
  * 隐式转换的触发：
  *     1. 转换为期望类型： val x: Int = 3.5, 将3.5转换为Int
  *     2. 转换方法的接受者：1 + String，1没有此方法，就寻找是否有隐式转换，能将1转换为有 +(String) 这个方法的类型
  *     3. 产生新的语法， 1 -> "one" P291，能将Any类型转换为 ArrowAssoc，它定义了->操作符
  *
  * 隐式参数：P292，
  *     1. 使用curry定义的函数，最后一节参数里列表，用implicit定义；那么使用时可以省略整节参数段
  *        同时在某个object中也定义定义期望类型的变量，也用 implicit 修饰
  *
  *     2. 视界：更进一步
  *        这个implicit参数，还会被当做隐式转换传入到函数体重使用，进行某种转换来配合编译器 P296
  *
  * 使用原则
  *     1. 对隐式参数的类型，使用自定义命名的类型，而非简单类型（如：封装String，class PreferredType(val perference: String)） P296
  *     2. 过于频繁使用，会导致代码晦涩难懂
  *
  * 调试选项
  *     调试时，如果因为隐式转换出现错误，可以 增加 scalac -Xprint:typer 选项 P296
  */
object a6_implicit extends App {

    def operate_implicit() = {

        /**
          * 定义方式：
          *     1. 使用方式：implicit 前缀
          *     2. 引用方式：单一标识符的形式存在于作用域（即：不能是 com.data.convert 这种形式）
          *     3. object：编译器也会在源类型、期望类型的伴生对象中查找隐式定义
          *
          */
        implicit def Wrapper(s: String) = {
            new RandomAccess {
                def length = s.length
                def apply(i: Int) = s.charAt(i)
            }
        }

        /**
          * String 就 拥有了 RandomAccess 特质的方法
          */
        "abc" exists (_.isDigit)

        /**
          * 在源、目的类型的object中定义
          */
        class DollarA(val name: String)
        class Euro(val name: Int)
        object DollarA {
            implicit def convertTo(x: DollarA): Euro = { new Euro(x.name.toInt) }
        }
    }

    operate_implicit
}
