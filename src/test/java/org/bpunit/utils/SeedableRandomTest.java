package org.bpunit.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * A test class for the {@link SeedableRandom} class. It does not test the random distribution of the various methods,
 * just that the generated values fit the required parameters.
 *
 * The tests for {@code nextPrimitive()} will just fail if the method itself throws and exception or returns
 * {@code null} as it will cause the outboxing to fail. Other than that, there's nothing much to assert on a method
 * that can return any value.
 */
@SuppressWarnings("unused")
public class SeedableRandomTest {
    private SeedableRandom random;

    private enum TestEnum {
        ONE, TWO, THREE
    }

    @Before
    public void setUp() {
        random = new SeedableRandom();
    }

    @Test
    public void nextByte() {
        byte b = random.nextByte();
    }

    @Test
    public void nextByteMax() {
        byte b = random.nextByte((byte) 4);
        assertTrue(b < (byte) 4);
        assertTrue(b >= (byte) 0);
    }

    @Test
    public void nextByteMinMax() {
        byte b = random.nextByte((byte) 2, (byte) 4);
        assertTrue(b < (byte) 4);
        assertTrue(b >= (byte) 2);
    }

    @Test
    public void nextChar() {
        char c = random.nextChar();
    }

    @Test
    public void nextCharMax() {
        char c = random.nextChar('z');
        assertTrue(c < 'z');
        assertTrue(c >= (char) 0);
    }

    @Test
    public void nextCharMinMax() {
        char c = random.nextChar('a', 'z');
        assertTrue(c < 'z');
        assertTrue(c >= 'a');
    }

    @Test
    public void nextShort() {
        short s = random.nextShort();
    }

    @Test
    public void nextShortMax() {
        short s = random.nextShort((short) 200);
        assertTrue(s < (short) 200);
    }

    @Test
    public void nextShortMinMax() {
        short s = random.nextShort((short) 100, (short) 200);
        assertTrue(s < (short) 200);
        assertTrue(s >= (short) 100);
    }

    @Test
    public void nextInt() {
        int i = random.nextInt();
    }

    @Test
    public void nextIntMax() {
        int i = random.nextInt(1000);
        assertTrue(i < 1000);
    }

    @Test
    public void nextIntMinMax() {
        int i = random.nextInt(100, 1000);
        assertTrue(i < 1000);
        assertTrue(i >= 100);
    }

    @Test
    public void nextLong() {
        long l = random.nextLong();
    }

    @Test
    public void nextLongMax() {
        long l = random.nextLong(15000L);
        assertTrue(l < 15000L);
    }

    @Test
    public void nextLongMinMax() {
        long l = random.nextLong(12000L, 15000L);
        assertTrue(l < 15000L);
        assertTrue(l >= 12000L);
    }

    @Test
    public void nextFloat() {
        float f = random.nextFloat();
    }

    @Test
    public void nextFloatMax() {
        float f = random.nextFloat(5.0f);
        assertTrue(f < 5.0f);
    }

    @Test
    public void nextFloatMinMax() {
        float f = random.nextFloat(2.0f, 5.0f);
        assertTrue(f < 5.0f);
        assertTrue(f >= 2.0f);
    }

    @Test
    public void nextDouble() {
        double d = random.nextDouble();
    }

    @Test
    public void nextDoubleMax() {
        double d = random.nextDouble(5.0);
        assertTrue(d < 5.0);
    }

    @Test
    public void nextDoubleMinMax() {
        double d = random.nextDouble(2.0, 5.0);
        assertTrue(d < 5.0);
        assertTrue(d >= 2.0);
    }

    @Test
    public void pickRandomArray() {
        Integer[] arr = {random.nextInt(), random.nextInt(), random.nextInt()};
        Integer picked = random.pickRandom(arr);
        assertTrue(Arrays.asList(arr).contains(picked));
    }

    @Test
    public void pickRandomCollection() {
        List<Integer> arr = Arrays.asList(random.nextInt(), random.nextInt(), random.nextInt());
        Integer picked = random.pickRandom(arr);
    }

    @Test
    public void nextBytes() {
        byte[] bytes = random.nextBytes(10);
        assertEquals(10, bytes.length);
    }

    @Test
    public void nextBigInteger() {
        BigInteger bigInteger = random.nextBigInteger(100);
        assertTrue(bigInteger.bitLength() <= 100);
        assertTrue(bigInteger.compareTo(new BigInteger("2").pow(100).subtract(BigInteger.ONE)) <= 0);
        assertTrue(bigInteger.compareTo(BigInteger.ZERO) > 0);
    }

    @Test
    public void nextEnum() {
        TestEnum testEnum = random.nextEnum(TestEnum.class);
    }
}
