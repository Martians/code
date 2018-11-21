package com.data.a7_library

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.nio.ByteBuffer

import com.esotericsoftware.kryo.{Kryo, Serializer}
import com.esotericsoftware.kryo.io.{Input, Output}
import com.esotericsoftware.kryo.serializers.JavaSerializer

object a0_kryo extends App {

    /**
      * kyro：https://github.com/EsotericSoftware/kryo/blob/master/README.md#quickstart
      *         https://blog.csdn.net/paoma_1008/article/details/79827803
      *         https://www.zhihu.com/question/263956801
      */
    def kryo_simple() = {
        println("\nkryo_simple:")

        class Simple(val a: Int, val b: String) {
            def this() = this(0, "")
        }

        val origin = new Simple(1, "bbb")
        val kryo = new Kryo()
        kryo.register(classOf[Simple])

        val output = new Output(1024)
        kryo.writeObject(output, origin)

        val input = new Input(1000)
        input.setBuffer(output.getBuffer)

        val result: Simple = kryo.readObject(input, classOf[Simple])

        println(result.b)
        println(output.getBuffer + ", <" + output.position())

    }

    class Work(val a: Int, val b: String) extends Serializable {
        def this() = this(0, "")
        var y = Set("1", "2", "3", "4")
    }

    /**
      * 注册自己的Serializer
      */
    def kryo_new_serialize()= {
        println("\nkryo_serialize:")

        class WorkSerializer extends Serializer[Work] {
            override def write(kryo: Kryo, output: Output, work: Work): Unit = {
                output.writeInt(work.a)
                output.writeString(work.b)

                output.writeInt(work.y.size)
                work.y.foreach(output.writeString(_))
            }

            override def read(kryo: Kryo, input: Input, t: Class[Work]): Work = {
                val work = new Work(input.readInt(), input.readString())
                val count = input.readInt
                for (i <- 0 until count) {
                    work.y += input.readString()
                }
                work
            }
        }

        val origin = new Work(1, "bbb")
        val kryo = new Kryo()

        kryo.register(classOf[Work], new WorkSerializer)

        val output = new Output(1024)
        kryo.writeObject(output, origin)

        val input = new Input(1000)
        input.setBuffer(output.getBuffer)

        val result: Work = kryo.readObject(input, classOf[Work])

        println(result.y)
        println(output.getBuffer + ", <" + output.position())
    }

    /**
      * kryo 不支持 scala 类型，使用 java JavaSerializer
      *     但这样就使用的是默认的java方式，相当于直接使用java的序列化
      */
    def kryo_java_serialize()= {
        println("\nkryo_java_serialize:")

        val origin = new Work(1, "bbb")
        val kryo = new Kryo()

        kryo.register(classOf[Work], new JavaSerializer)

        val output = new Output(1024)
        kryo.writeObject(output, origin)

        val input = new Input(1000)
        input.setBuffer(output.getBuffer)

        val result: Work = kryo.readObject(input, classOf[Work])

        println(result.y)
        println(output.getBuffer + ", <" + output.position())
    }

    /**
      * 直接使用java的方式进行序列化，问题在于这将把类信息也存储进去，因此会很大
      *     http://www.cnblogs.com/huhx/p/serializable.html
      */
    def java_serialize() = {
        println("\noperate_serialize:")

        class Work(val a: Int, val b: String) extends Serializable {
            def this() = this(0, "")
            val x = List(1, 2, 3, 4)
            val y = Set("1", "2", "3", "4")
        }

        val origin = new Work(1, "bbb")

        val buffer = new ByteArrayOutputStream()
        val output = new ObjectOutputStream(buffer)
        output.writeObject(origin)
        output.close()

        val input = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray))
        val result = input.readObject()

        println(result)
        println(buffer.toByteArray + ", <" + buffer.size())
    }

    kryo_simple
    kryo_new_serialize

    kryo_java_serialize
    java_serialize
}
