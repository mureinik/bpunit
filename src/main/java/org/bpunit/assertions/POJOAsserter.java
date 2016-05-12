package org.bpunit.assertions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * A utility class for asserting that boilerplate getters and setters work properly.
 */
public class POJOAsserter<T> {

    /* Constants */

    /** The logger to use. */
    private static final Logger log = LoggerFactory.getLogger(POJOAsserter.class);

    // Prefixes to make up method names
    private static final String SET_PREFIX = "set";
    private static final String GET_PREFIX = "get";
    private static final String BOOLEAN_GET_PREFIX = "is";
    private static final String RANDOM_PREFIX = "next";

    /** Hack to map between primitive types and boxing classes */
    private static final Map<Class<?>, Class<?>> primitiveToBoxing = new HashMap<>();
    static {
        primitiveToBoxing.put(Boolean.TYPE, Boolean.class);
        primitiveToBoxing.put(Byte.TYPE, Byte.class);
        primitiveToBoxing.put(Short.TYPE, Short.class);
        primitiveToBoxing.put(Integer.TYPE, Integer.class);
        primitiveToBoxing.put(Long.TYPE, Long.class);
        primitiveToBoxing.put(Float.TYPE, Float.class);
        primitiveToBoxing.put(Double.TYPE, Double.class);
    }


    /* Data Members */

    /** The POJO instance under test */
    private T pojo;
    private Random random;


    /* Constructors */

    POJOAsserter(T pojo, Random random) {
        this.pojo = pojo;
        this.random = random;
    }
    /**
     * Tests that the {@code getXYZ()} and {@code setXYZ(SomeType XYZ)} methods of {@link #pojo} are symmetric. I.e.,
     * if {@code setXYZ} is called with some randomly generated value, the subsequent {@code getXYZ} call will return
     * the same value.
     *
     * This behavior is tested by calling {@link org.junit.Assert}'s assertions, so a failure would behave just like any
     * other JUnit test failure.
     */
    public void assertProperties() {
        Class<?> pojoClass = pojo.getClass();
        for (Method setMethod : pojoClass.getMethods()) {
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
            Class<?> type = paramTypes[0];
            Method getMethod = getGetMethod(pojoClass, propertyName, type);
            if (getMethod == null) {
                log.info("Cannot find getter and setter pair for property " + propertyName);
                continue;
            }

            Object randomValue = getRandomValue(random, type);
            if (randomValue == null) {
                continue;
            }

            try {
                setMethod.invoke(pojo, randomValue);
                Object returnedValue = getMethod.invoke(pojo);
                assertEquals("Wrong value for property " + propertyName, randomValue, returnedValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Can't test property " + propertyName + " due to exception: " + e);
            }
        }
    }

    /**
     * @param pojoClass
     *            The class of POJO to get the method for.
     * @param propertyName
     *            The name of the property.
     * @param expectedType
     *            The expected return type.
     * @return A {@link Method} object representing the "get method" of the given property name.
     */
    private static Method getGetMethod(Class<?> pojoClass, String propertyName, Class<?> expectedType) {
        Method m = getGetMethod(pojoClass, propertyName, GET_PREFIX, expectedType);
        if (m == null && (expectedType.equals(Boolean.TYPE) || expectedType.equals(Boolean.class))) {
            log.info("Property " + propertyName + " is a boolean, trying a different prefix");
            m = getGetMethod(pojoClass, propertyName, BOOLEAN_GET_PREFIX, expectedType);
        }
        return m;
    }

    /**
     * @param pojoClass
     *            The class of POJO to get the method for.
     * @param propertyName
     *            The name of the property.
     * @param methodPrefix
     *            The prefix of the method to which the property name is appended to (e.g., "get", "is").
     * @param expectedType
     *            The expected return type.
     * @return A {@link Method} object representing the "get method" of the given property name.
     */
    private static Method getGetMethod(Class<?> pojoClass,
                                       String propertyName,
                                       String methodPrefix,
                                       Class<?> expectedType) {
        String getMethodName = methodPrefix + propertyName;
        Method getMethod;
        try {
            getMethod = pojoClass.getMethod(getMethodName);
            Class<?> retrunType = getMethod.getReturnType();
            if (!retrunType.equals(expectedType)) {
                throw new NoSuchMethodException();
            }
        } catch (NoSuchMethodException e) {
            log.info("No appropriate getter " + getMethodName + " for " + propertyName);
            return null;
        }
        return getMethod;
    }

    /**
     * @param random
     *            The random generator to use.
     * @param type
     *            The type to randomize.
     * @return A randomly generated value of type {@code type}.
     */
    private static <T> T getRandomValue(Random random, Class<T> type) {
        String typeName = type.getSimpleName();
        if (type.isPrimitive()) {
            typeName = capitalizeFirst(typeName);
        }
        String randomMethodName = RANDOM_PREFIX + typeName;
        Class<? extends Random> randomClass = random.getClass();
        try {
            Method randomMethod = randomClass.getMethod(randomMethodName);
            Class<?> returnType = randomMethod.getReturnType();
            if (randomMethod.getParameterTypes().length != 0 ||
                    (!returnType.equals(type) && !type.equals(primitiveToBoxing.get(returnType)))) {
                throw new NoSuchMethodException();
            }

            // noinspection unchecked
            return (T) randomMethod.invoke(random);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            log.info("Can't execute random method: " + randomClass.getSimpleName() + "." + randomMethodName);
            return null;
        }
    }

    /**
     * Transforms a string to title case.
     *
     * @param s
     *            The string to transform.
     * @return The transformed string.
     */
    private static String capitalizeFirst(String s) {
        return String.valueOf(Character.toTitleCase(s.charAt(0))) + s.substring(1);
    }
}
