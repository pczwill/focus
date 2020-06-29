package com.focus.test.design.simplefactory;

//简单工厂  
class Driver {
	public static Car createCar(String car) {
		Car c = null;
		if ("Benz".equalsIgnoreCase(car))
			c = new Benz();
		else if ("Bmw".equalsIgnoreCase(car))
			c = new Bmw();
		return c;
	}
}