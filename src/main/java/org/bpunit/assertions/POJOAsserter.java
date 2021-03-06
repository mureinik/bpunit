package org.bpunit.assertions;

import org.bpunit.assertions.behaviors.Behavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private static final Map<Class<?>, Class<?>> boxingToPrimitive = new HashMap<>();
    static {
        for (Map.Entry<Class<?>, Class<?>> entry : primitiveToBoxing.entrySet()) {
            boxingToPrimitive.put(entry.getValue(), entry.getKey());
        }
    }


    /* Data Members */

    /** The POJO instance under test */
    private T pojo;

    /* A randomizer for the POJO's properties */
    private Random random;


    /* Behaviors */

    /** The behavior for when a setter cannot be found for a property */
    private Behavior noGetterBehavior;

    /** The behavior for a random value generation failure */
    private Behavior randomFailureBehavior;

    /** The behavior for when a property can't be tested */
    private Behavior propertyTestFailureBehavior;


    /* Constructors */

    /**
     * @param pojo
     *            The POJO to be tested
     * @param random
     *            A random data source
     * @param noGetterBehavior
     *            The {@link Behavior} to perform when a property doesn't have a setter
     * @param randomFailureBehavior
     *            The {@link Behavior} to perform when a random value can't be generated
     * @param propertyTestFailureBehavior
     *            The {@link Behavior} to perform when a property can't be tested
     */
    POJOAsserter(T pojo, Random random, Behavior noGetterBehavior, Behavior randomFailureBehavior, Behavior propertyTestFailureBehavior) {
        this.pojo = pojo;
        this.random = random;
        this.noGetterBehavior = noGetterBehavior;
        this.randomFailureBehavior = randomFailureBehavior;
        this.propertyTestFailureBehavior = propertyTestFailureBehavior;
    }


    /* Public Methods */

    /**
     * Tests that the {@code getXYZ()} and {@code setXYZ(SomeType XYZ)} methods of {@link #pojo} are symmetric. I.e.,
     * if {@code setXYZ} is called with some randomly generated value, the subsequent {@code getXYZ} call will return
     * the same value.
     *
     * This behavior is tested by calling {@link org.junit.jupiter.api.Assertions}'s assertions, so a failure would
     * behave just like any other JUnit test failure.
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
            Class<T> type = (Class<T>)paramTypes[0];
            Method getMethod = getGetMethod(pojoClass, propertyName, type);
            if (getMethod == null) {
                noGetterBehavior.behave("Cannot find getter and setter pair for property " + propertyName, null);
                continue;
            }

            Object randomValue = getRandomValue(random, type);
            if (randomValue == null) {
                continue;
            }

            try {
                setMethod.invoke(pojo, randomValue);
                Object returnedValue = getMethod.invoke(pojo);
                assertEquals(randomValue, returnedValue, () -> "Wrong value for property " + propertyName);
            } catch (IllegalAccessException | InvocationTargetException e) {
                propertyTestFailureBehavior.behave("Can't test property " + propertyName, e);
            }
        }
    }


    /* Private helper methods */

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
    private T getRandomValue(Random random, Class<T> type) {
        Class typeToGenerate;
        typeToGenerate = boxingToPrimitive.getOrDefault(type, type);
        String typeName = typeToGenerate.getSimpleName();
        if (typeToGenerate.isPrimitive()) {
            typeName = capitalizeFirst(typeName);
        }
        String randomMethodName = RANDOM_PREFIX + typeName;
        Class<? extends Random> randomClass = random.getClass();
        try {
            Method randomMethod = randomClass.getMethod(randomMethodName);
            Class<?> returnType = randomMethod.getReturnType();
            if (randomMethod.getParameterTypes().length != 0 ||
                    (!returnType.equals(typeToGenerate) && !typeToGenerate.equals(primitiveToBoxing.get(returnType)))) {
                throw new NoSuchMethodException();
            }

            // noinspection unchecked
            return (T) randomMethod.invoke(random);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            randomFailureBehavior.behave
                    ("Can't execute random method: " + randomClass.getSimpleName() + "." + randomMethodName, e);
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
