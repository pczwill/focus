package com.focus.test.design.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**   
 *    
 * JDK动态代理类   
 *    https://www.cnblogs.com/akaneblog/p/6720513.html
 *   
 */    
public class JDKProxy implements InvocationHandler {    
    
    private Object targetObject;//需要代理的目标对象    
    
    public Object newProxy(Object targetObject) {//将目标对象传入进行代理    
        this.targetObject = targetObject;     
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),    
                targetObject.getClass().getInterfaces(), this);//返回代理对象    
    }    
    
    public Object invoke(Object proxy, Method method, Object[] args)//invoke方法    
            throws Throwable {    
        before();
        Object ret = null;      // 设置方法的返回值    
        ret  = method.invoke(targetObject, args);       //invoke调用需要代理的方法
        after();
        return ret;    
    }    
    
    private void before() {//方法执行前   
        System.out.println("方法执行前 !");    
    }    
    private void after() {//方法执行后    
        System.out.println("方法执行后");    
    }    
}