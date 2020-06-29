package com.focus.test.design.factorymethod;

class BmwDriver extends Driver{  
    public Car createCar(String car) throws Exception {  
        return new Bmw();  
    }  
} 