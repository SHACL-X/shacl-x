package org.topbraid.shacl.js;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.graalvm.polyglot.Value;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScriptEngineUtil {

    public static List<Object> asArray(Object obj) throws Exception {
        if (obj instanceof Value) {
            return GraalUtil.asArray(obj);
        } else if (obj instanceof ScriptObjectMirror) {
            return Arrays.stream(NashornUtil.asArray(obj)).collect(Collectors.toList());
        } else {
            throw new Exception("Object not supported");
        }
    }

    public static boolean isArray(Object obj) throws Exception {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Value) {
            return GraalUtil.isArray(obj);
        } else if (obj instanceof ScriptObjectMirror) {
            return NashornUtil.isArray(obj);
        } else {
            return false;
        }
    }

}
