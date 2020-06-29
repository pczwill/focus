package com.focus.test.design.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyService {
	
	/*https://www.cnblogs.com/akaneblog/p/6720513.html
	 * java中动态代理主要有JDK和CGLIB两种方式。
	 * 
	 * 区别主要是jdk是代理接口，而cglib是代理类。
	 * 
	 * jdk的动态代理调用了Proxy.newProxyInstance(ClassLoader loader,Class<?>[]
	 * interfaces,InvocationHandler h) 方法。
	 * 
	 * 通过该方法生成字节码，动态的创建了一个代理类，interfaces参数是该动态类所继承的所有接口，而继承InvocationHandler
	 * 接口的类则是实现在调用代理接口方法前后的具体逻辑，下边是具体的实现：
	 */
	

	static interface Subject {
		void sayHi();

		void sayHello();
	}

	static class SubjectImpl implements Subject {

		@Override
		public void sayHi() {
			System.out.println("hi");
		}

		@Override
		public void sayHello() {
			System.out.println("hello");
		}
	}

	static class ProxyInvocationHandler implements InvocationHandler {
		private Subject target;

		public ProxyInvocationHandler(Subject target) {
			this.target = target;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			System.out.print("say:");
			return method.invoke(target, args);
		}

	}

	public static void main(String[] args) {
		Subject subject = new SubjectImpl();
		
		Subject subjectProxy = (Subject) Proxy.newProxyInstance(subject.getClass().getClassLoader(),
				subject.getClass().getInterfaces(), new ProxyInvocationHandler(subject));
		subjectProxy.sayHi();
		subjectProxy.sayHello();

	}
}
