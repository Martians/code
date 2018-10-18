//
//package com.data.native;
//
//public class Native {
//
//	int data;
//	public static void main(String[] args) {
//		Native object = new Native();
//		object.data = 10;
//
//		System.out.println("Object before native: " + object.data);
//		object.test();
//
//		System.out.println("Object after native: " + object.data);
//	}
//
//	public native void test();
//
//	static {
//		System.loadLibrary("NativeDemo");
//	};
//
//	//javah -classpath D:\Workspace\java\work\target\classes -jni com.data.work.Native
//}
