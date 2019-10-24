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
package com.github.liyue2008.rpc.server;

import com.github.liyue2008.rpc.hello.HelloService;
import com.github.liyue2008.rpc.hello.entity.HelloRequest;
import com.github.liyue2008.rpc.hello.entity.HelloResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public class HelloServiceImpl implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(String name) {
        logger.info("HelloServiceImpl收到: {}.", name);
        String ret = "Hello, " + name;
        logger.info("HelloServiceImpl返回: {}.", ret);
        return ret;
    }

    @Override
    public HelloResult helloMoreResult(String name, String value) {

        logger.info("HelloServiceImpl收到: {}.", name,value);
        HelloResult result = new HelloResult();
        result.setSuccess(true);
        result.setResult("Success");
        result.setResultCode("1");
        logger.info("HelloServiceImpl返回: {}.", result.toString());

        return result;
    }

    @Override
    public HelloResult helloMoreResult(HelloRequest request) {

        logger.info("HelloServiceImpl收到: {}.", request.toString());
        HelloResult result = new HelloResult();
        result.setSuccess(true);
        result.setResult("Success");
        result.setResultCode("1");
        logger.info("HelloServiceImpl返回: {}.", result.toString());
        return result;
    }
}
