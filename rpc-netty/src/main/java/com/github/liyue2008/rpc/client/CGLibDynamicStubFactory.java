/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.liyue2008.rpc.client;

import com.github.liyue2008.rpc.transport.Transport;
import com.itranswarp.compiler.JavaStringCompiler;

import java.util.Map;

/**
 * @author LiYue
 * Date: 2019/9/27
 */
public class CGLibDynamicStubFactory implements StubFactory{

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createStub(Transport transport, Class<T> serviceClass) {
        try {
            // jdk动态代理测试
            CGLibDynamicProxy proxy = new CGLibDynamicProxy(serviceClass);
            proxy.setTransport(transport);
            // 返回这个桩
            return (T) proxy.getProxy();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
