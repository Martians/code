/**
 * 需要; 结尾

 数据与方法分开
 方法与继承的traite分开

与其他语言交互
https://www.jianshu.com/p/95884d1d04aa

debug:
https://github.com/intellij-rust/intellij-rust/issues/535
https://github.com/intellij-rust/intellij-rust/issues/2462#issuecomment-381339051


http://wiki.jikexueyuan.com/project/rust-primer/cargo-projects-manager/cargo-projects-manager.html

Todo:
    如何调试
https://www.yiibai.com/rust/print_debug.html
https://users.rust-lang.org/t/debugging-rust-on-windows/7114/4
https://users.rust-lang.org/t/use-rust-gdb-and-rust-lldb-for-improved-debugging-you-already-have-them/756

https://internals.rust-lang.org/t/rust-debugger/2110

IDE:    https://areweideyet.com/

https://areweideyet.com/, Are we (I)DE yet?


 构建：
    cargo new guessing_game --bin
    cargo check：加速开发，不生成可执行文件
    cargo build：--release
    cargo run：编译 + 开发


获取依赖：
https://crates.io/

语言的主要特点是那些
不可变变量，类似于函数编程

静态类型语言

内存模型？

语句上的差异

类型都是写在后边

默认是函数是编程，不可变变量

vector + enumurate

string utf-8

 */

//use std::io;

#[macro_use]
extern crate log;
extern crate simple_logger;
//use simple_logger::*;


fn main() {
    println!("Guess the number!");
    println!("Please input your guess.");

//    let mut guess = String::new();
//
//    io::stdin().read_line(&mut guess)
//        .expect("Failed to read line");
//    println!("You guessed: {}", guess);

//    simple_logger::init().unwrap();

    simple_logger::init().unwrap();
    info!("starting up");git 
}
