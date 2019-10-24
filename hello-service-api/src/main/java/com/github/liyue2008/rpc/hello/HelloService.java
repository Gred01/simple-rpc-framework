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
package com.github.liyue2008.rpc.hello;

import com.github.liyue2008.rpc.hello.entity.HelloRequest;
import com.github.liyue2008.rpc.hello.entity.HelloResult;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public interface HelloService {

    String hello(String name);

    HelloResult helloMoreResult(String name,String value);

    HelloResult helloMoreResult(HelloRequest request);
}
