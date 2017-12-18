package com.intervalintl.voltus.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;


public class ReflectionUtil {

    private ReflectionUtil() {}

    public static void invokeMethod(Object objInstance, String methodName
            , Class<?>[] parameterTypeArray, Object[] parameterInstanceArray) {

        try {

            Method method = objInstance.getClass().getMethod(methodName, parameterTypeArray);
            method.setAccessible(true);
            method.invoke(objInstance, parameterInstanceArray);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO(Pablo): Report these exceptions to some backlog to analysis and statistic
            e.printStackTrace();
        }
    }

    public static void injectFields(Object objInstance, Map<String, Object> fieldMap) {

        Class<?> objClazz = objInstance.getClass();

        for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
            try {

                Field field = objClazz.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(objInstance, entry.getValue());


            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // TODO(Pablo): Report these exceptions to some backlog to analysis and statistic
                e.printStackTrace();
            }
        }
    }

}
