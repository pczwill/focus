package com.focus.test.design.factorymethod;

//具体工厂（每个具体工厂负责一个具体产品）  
class BenzDriver extends Driver{  
  public Car createCar(String car) throws Exception {  
      return new Benz();  
  }  
} 