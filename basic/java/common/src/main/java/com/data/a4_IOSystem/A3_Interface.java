package com.data.a4_IOSystem;

/**
 * 1）通用接口
 *  AutoCloseable，定义了close方法
 *      可用于带资源的try语句，系统会在需要时调用其close方法
 *  Closeable 扩展了 AutoCloseable
 *  Flushable
 *
 * 2）支持多种数据类型
 *  DataInput、DataOutput
 *
 * 3）串行化
 *  Serializable
 *          没有定义任何成员，只是简单的指示类可以被串行化
 *          会递归进行串行化操作
 *
 *       https://blog.csdn.net/leixingbang1989/article/details/50556966
 *          The serialization runtime associates with each serializable class a version number, called a serialVersionUID,
 *          which is used during deserialization to verify that the sender and receiver of a serialized object have loaded
 *          classes for that object that are compatible with respect to serialization.
 *
 *  Externalizable
 *          用户可自定义此过程
 *
 * 4）过滤器
 *  FilenameFilter
 */
public class A3_Interface {
}
