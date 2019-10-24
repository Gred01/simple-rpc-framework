package com.github.liyue2008.rpc.client;

import com.github.liyue2008.rpc.client.stubs.AbstractStub;
import com.github.liyue2008.rpc.client.stubs.RpcRequest;
import com.github.liyue2008.rpc.serialize.SerializeSupport;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLibDynamicProxy extends AbstractStub implements MethodInterceptor {

    private Class clz;

    public CGLibDynamicProxy(Class clz){
        this.clz = clz;
    }

    private Enhancer enhancer = new Enhancer();

    public Object getProxy(){
        //设置需要创建子类的类
        enhancer.setSuperclass(clz);
        enhancer.setCallback(this);
        //通过字节码技术动态创建子类实例
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return SerializeSupport.parse(invokeRemote(new RpcRequest(clz.getCanonicalName(),method.getName(),SerializeSupport.serialize(objects))));
    }
}
