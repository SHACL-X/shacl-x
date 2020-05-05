package org.topbraid.shacl.js;

import org.graalvm.polyglot.Value;

import java.util.ArrayList;
import java.util.List;

public class GraalUtil {

    public static List<String> asArray(Object obj) {
        if (((Value) obj).hasArrayElements()) {
            List<String> ret = new ArrayList();
            Value v = (Value) obj;
            for (int i = 0; i < v.getArraySize(); i++) {
                ret.add(String.valueOf(v.getArrayElement(i)));
            }
            return ret;
        } else {
            return null;
        }
    }

}
