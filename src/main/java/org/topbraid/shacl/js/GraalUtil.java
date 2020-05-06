package org.topbraid.shacl.js;

import org.graalvm.polyglot.Value;

import java.util.ArrayList;
import java.util.List;

public class GraalUtil {

    public static List<Object> asArray(Object obj) throws Exception {
        if (((Value) obj).hasArrayElements()) {
            List<Object> ret = new ArrayList();
            Value v = (Value) obj;
            for (int i = 0; i < v.getArraySize(); i++) {
                ret.add(v.getArrayElement(i).as(Object.class));
            }
            return ret;
        } else {
            throw new Exception("Object not supported");
        }
    }

    public static boolean isArray(Object obj) {
        if (obj instanceof Value) {
            Value v = (Value) obj;
            return v.hasArrayElements();
        } else {
            return false;
        }
    }

}
