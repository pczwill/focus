package com.focus.test.design.simplefactory;

//具体产品  
class Benz extends Car{  
  public void drive(){  
      System.out.println(this.getName()+"----go-----------------------");  
  }  
} 