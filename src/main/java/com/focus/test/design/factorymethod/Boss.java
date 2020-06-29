package com.focus.test.design.factorymethod;

/*
 * 工厂方法模式
抽象工厂角色： 这是工厂方法模式的核心，它与应用程序无关。是具体工厂角色必须实现的接口或者必须继承的父类。在java中它由抽象类或者接口来实现。
具体工厂角色：它含有和具体业务逻辑有关的代码。由应用程序调用以创建对应的具体产品的对象。在java中它由具体的类来实现。
抽象产品角色：它是具体产品继承的父类或者是实现的接口。在java中一般有抽象类或者接口来实现。
具体产品角色：具体工厂角色所创建的对象就是此角色的实例。在java中由具体的类来实现。
来用类图来清晰的表示下的它们之间的关系：

 * 话说暴发户生意越做越大，自己的爱车也越来越多。这可苦了那位司机师傅了，
 * 什么车它都要记得，维护，都要经过他来使用！于是暴发户同情他说：我给你分配几个人手，
 * 你只管管好他们就行了！于是工厂方法模式的管理出现了。代码如下：
 * 
 * 
 *  使用开闭原则来分析下工厂方法模式。当有新的产品（即暴发户的汽车）产生时，
 *  只要按照抽象产品角色、抽象工厂角色提供的合同来生成，那么就可以被客户使用，
 *  而不必去修改任何已有的代码。（即当有新产品时，只要创建并基础抽象产品；
 *  新建具体工厂继承抽象工厂；而不用修改任何一个类）工厂方法模式是完全符合开闭原则的！
 * 
 * 
 * */
public class Boss {
	public static void main(String[] args) throws Exception {
		Driver d = new BenzDriver();
		Car c = d.createCar("benz");
		c.setName("benz");
		c.drive();
	}
}