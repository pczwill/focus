package com.focus.test.design.simplefactory;

import java.io.IOException;

//简单工厂
/*
 * 
 * 这个模式本身很简单而且使用在业务较简单的情况下。一般用于小项目或者具体产品很少扩展的情况（这样工厂类才不用经常更改）。
它由三种角色组成：
工厂类角色：这是本模式的核心，含有一定的商业逻辑和判断逻辑，根据逻辑不同，产生具体的工厂产品。如例子中的Driver类。
抽象产品角色：它一般是具体产品继承的父类或者实现的接口。由接口或者抽象类来实现。如例中的Car接口。
具体产品角色：工厂类所创建的对象就是此角色的实例。在java中由一个具体类实现，如例子中的Benz、Bmw类。
 * 
 * */
public class SimpleFactory {
	public static void main(String[] args) throws IOException {  
        //老板告诉司机我今天坐奔驰  
        Car car = Driver.createCar("benz");  
        car.setName("benz");  
         //司机开着奔驰出发  
        car.drive();  
    }  
}
