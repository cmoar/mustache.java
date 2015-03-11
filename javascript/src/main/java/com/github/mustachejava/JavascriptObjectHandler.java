package com.github.mustachejava;

import com.github.mustachejava.reflect.ReflectionObjectHandler;
import jdk.nashorn.api.scripting.JSObject;

/**
 * Uses Nashorn to support Javascript scope objects
 */
public class JavascriptObjectHandler extends ReflectionObjectHandler {
  @Override
  public String stringify(Object object) {
    if (object instanceof JSObject) {
      final JSObject jso = (JSObject) object;
      if (jso.isFunction()) {
        Object call = jso.call(jso);
        return stringify(coerce(call));
      }
    }
    return super.stringify(object);
  }

  @Override
  public Object coerce(Object object) {
    if (object instanceof JSObject) {
      final JSObject jso = (JSObject) object;
      if (jso.isFunction()) {
        return new TemplateFunction() {
          @Override
          public String apply(String s) {
            Object call = jso.call(jso, s);
            return call == null ? null : call.toString();
          }
        };
      }
    }
    return super.coerce(object);
  }
}
