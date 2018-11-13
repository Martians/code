package com.data.a2_class;

/**
 * java 中实际都转换为了object进行处理，自动移除类型信息；类型转换都是隐式、自动进行的
 * 但是java可以自动确保类型安全
 *
 * 注意：不能实例化类型参数，即不能用new
 *
 *
 * Example
 *      1. Enum <E extends Enum<E>>, https://bbs.csdn.net/topics/390971992
 *      2. <E extends Enum<E>> E getEnum(String key, Class<E> clazz)
 *          https://stackoverflow.com/questions/4014117/enum-valueofclasst-enumtype-string-name-question
 */

import java.util.List;

import static com.data.a0_util.format.Display.out;

public class A4_Generic {

	/**
	 * 定义类型上界，必须是 Number 的子类
	 */
	static class Generic<T extends Number> {
		public T ob;

		public Generic(T o) {
			ob = o;
		}

		void showType() {
			out("type of t is " + ob.getClass().getName());
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 通过接口定义类型的上界
	 */
	interface MyIF {
		void handle();
	}

	/**
	 * 定义类型上界，必须是子类，并且定义了相应的接口
	 */
	static class Gen<T extends A3_Interface & MyIF> {
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * ？使用泛化的参数, 通配符参数，只能确定参数是 List<>中的，具体类型动态决定
	 *
	 * java中的模板类，并没有记录类型
	 */
	boolean check1(List<?> a, List<?> b) {
		return a.size() > b.size();
	}

	/**
	 * 泛化参数，上界
	 */
	boolean check2(List<? extends Number> a, List<? extends Number> b) {
		return a.size() > b.size();
	}

	/**
	 * 泛化参数，下界
	 */
	boolean check3(List<? super A2_Derive.Child> a) {
		return a.size() > 0;
	}

	/**
	 * 返回类型，T 必须是 Comparable<T> 类型的子类，即必须继承于 Comparable<T>，实现其接口
	 * 		Comparable本身也是个模板接口，因此必须给定其具体的类型
	 *
	 * 说明：
	 * 		https://blog.csdn.net/alwaystry/article/details/65448785
	 * 		<T extends Comparable<T>
	 *			类型 T 必须实现 Comparable 接口，并且这个接口的类型是 T
	 *			这样声明后，T 的类型之间就可以比较大小了
	 *
	 * 		<T extends Comparable<? super T>>
	 * 		 	类型 T 必须实现 Comparable 接口，并且这个接口的类型是 T 或 T 的任一父类
	 * 		 	这样声明后，T 的实例之间，T 的实例和它的父类的实例之间，可以相互比较大小
	 * 		 	这种声明，灵活性更高
	 * Todo：把文档看完
	 *
	 * 在函数中，对类型进行定义时，类型的定义是放在返回值之前的
	 */
	static <T extends Comparable<T>, V extends T> boolean isIn(T x, V[] y) {
		for (int i = 0; i < y.length; i++) {
			if (x.equals(y[i])) return true;
		}
		return false;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 泛型接口
	 */
	interface MinMax<T extends Comparable<T>> {
		T min();
		T max();
	}

	/**
	 * 注意：这里T的声明方式，MinMax中不再指定T的上界，而是放在MyClass中已经定义好了
	 */
	static class MyClass<T extends Comparable<T>, V> implements MinMax<T> {
		public T min() {
			return null;
		}
		public T max() {
			return null;
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////

//	static class MyClass2<T, V> {
//		MyClass2(T t, V v) {
//
//		}
//	}
//
//	public static boolean check5(MyClass2<T, V> o) {
//		return true;
//	}


	//////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		/**
		 * 菱形运算符，类型推断语法
		 */
		Generic<Integer> gen = new Generic<>(100);
		gen.showType();

		/**
		 * 不能声明，指向特定泛型类型的引用的数组
		 *
		 * 在数组中，类型已经给抹去了？
		 */
		//Generic<Integer> list[] = new Generic<Integer>[10];
		Generic<?> list[] = new Generic<?>[10];

		/**
		 * 运行时不能使用泛型类型信息，只能使用?
		 */
		//if (gen instanceof Generic<Integer>) {
		if (gen instanceof Generic<?>) {
			out("gen is instance of Generic<?>");
		}

		Integer nums[] = {1, 2, 3, 4};
		/**
		 * 使用时，不需要显示指定类型参数，会自动进行类型推断
		 */
		if (isIn(2, nums)) {
			out("in array");
		}

//		if (check5(new MyClass2<>(1, "test"))) {
////			out("good");
////		}
	}

}

