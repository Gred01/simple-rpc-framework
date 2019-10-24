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
package com.github.liyue2008.rpc.serialize.impl;

import com.github.liyue2008.rpc.serialize.Serializer;

import java.io.*;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public class ObjectSerializer  implements Serializer<Object> {


    @Override
    public int size(Object entry) {
        return toByteArray(entry).length;
    }

    @Override
    public void serialize(Object entry, byte[] bytes, int offset, int length) {
        byte[] objBytes = toByteArray(entry);
        System.arraycopy(objBytes, 0, bytes, offset, objBytes.length);
    }

    @Override
    public Object parse(byte[] bytes, int offset, int length) {
        byte[] subBytes = new byte[length];
        System.arraycopy(bytes,offset,subBytes,0,length);
        return toObject(subBytes);
    }

    @Override
    public byte type() {
        return Types.TYPE_OBJECT;
    }

    @Override
    public Class getSerializeClass() {
        return Object.class;
    }

    /**
     * 对象转数组
     * @param obj
     * @return
     */
    public byte[] toByteArray (Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 数组转对象
     * @param bytes
     * @return
     */
    public Object toObject (byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
            ObjectInputStream ois = new ObjectInputStream (bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }
}
