package org.bpunit.assertions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Random;
import java.util.logging.Logger;

/**
 * A utility class for boilerplate assertions
 */
public class AssertUtils {
    private static final Logger log = Logger.getLogger(AssertUtils.class.getName());

    private static final String SET_PREFIX = "set";
    private static final String GET_PREFIX = "get";
    private static final String RANDOM_PREFIX = "next";

    /** Should not be initialized. */
    private AssertUtils() {}

    public static void testProperties(Object o, Random random) {
        Class<?> objectClass = o.getClass();
        for (Method setMethod : objectClass.getMethods()) {
            String setMethodName = setMethod.getName();

            // Skip non setters
            if (!setMethodName.startsWith(SET_PREFIX)) {
                continue;
            }

            Class<?>[] paramTypes = setMethod.getParameterTypes();
            if (paramTypes.length != 1) {
                continue;
            }

            String propertyName = setMethodName.substring(SET_PREFIX.length());
            Method getMethod = getGetMethod(objectClass, propertyName);

            Class<?> type = paramTypes[0];
            if (!type.equals(getMethod.getReturnType())) {
                log.info("Getter and setter for " + propertyName + " do not match types.");
                continue;
            }
            
            Object randomValue = getRandomValue(random, type);
            if (randomValue == null) {
                continue;
            }


            try {
                setMethod.invoke(o, randomValue);
                Object returnedValue = getMethod.invoke(o);
                assertEquals("Wrong value for property " + propertyName, randomValue, returnedValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Can't test property " + propertyName + " due to exception: " + e);                
            }
        }
    }

    private static Method getGetMethod(Class<?> objectClass, String propertyName) {
        String getMethodName = GET_PREFIX + propertyName;
        Method getMethod;
        try {
            getMethod = objectClass.getMethod(getMethodName);
            if (!getMethod.isAccessible() && !Modifier.isPublic(getMethod.getModifiers())) {
                throw new NoSuchMethodException();
            }
        } catch (NoSuchMethodException e) {
            log.info("No appropriate getter for " + propertyName);
            return null;
        }
        return getMethod;
    }

    private static <T> T getRandomValue(Random random, Class<T> type) {
        String randomMethodName = RANDOM_PREFIX + type.getSimpleName();
        Class<? extends Random> randomClass = random.getClass();
        try {
            Method randomMethod = randomClass.getMethod(randomMethodName);
            if (!randomMethod.getReturnType().equals(type) || randomMethod.getParameterTypes().length != 0) {
                throw new NoSuchMethodException();
            }

            //noinspection unchecked
            return (T) randomMethod.invoke(random);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            log.info("Can't execute random method: " + randomClass.getSimpleName() + "." + randomMethodName);
            return null;
        }
    }
}
