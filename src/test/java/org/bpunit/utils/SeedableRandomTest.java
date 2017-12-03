package org.bpunit.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @BeforeEach
    public void setUp() {
        random = new SeedableRandom();
    }

    @Test
    public void nextBoolean() {
        boolean b = random.nextBoolean();
    }

    @Test
    public void setSeed() {
        long oldSeed = random.getSeed();
        long newSeed = oldSeed + 100;
        random.setSeed(newSeed);
        assertEquals(newSeed, random.getSeed());
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
    public void nextByteNegativeMax() {
        assertThrows(IllegalArgumentException.class, () -> random.nextByte((byte) -1));
    }

    @Test
    public void nextByteMinMax() {
        byte b = random.nextByte((byte) 2, (byte) 4);
        assertTrue(b < (byte) 4);
        assertTrue(b >= (byte) 2);
    }

    @Test
    public void nextByteMinMaxMinLargerThanMax() {
        assertThrows(IllegalArgumentException.class, () -> random.nextByte((byte) 4, (byte) 2));
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
    public void nextCharMinMaxMinLargerThanMax() {
        assertThrows(IllegalArgumentException.class, () -> random.nextChar('c', 'a'));
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
    public void nextShortNegativeMax() {
        assertThrows(IllegalArgumentException.class, () -> random.nextShort((short) -1));
    }

    @Test
    public void nextShortMinMax() {
        short s = random.nextShort((short) 100, (short) 200);
        assertTrue(s < (short) 200);
        assertTrue(s >= (short) 100);
    }


    @Test
    public void nextShortMinMaxMinLargerThanMax() {
        assertThrows(IllegalArgumentException.class, () -> random.nextShort((short) 200, (short) 100));
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
    public void nextIntNegativeMax() {
        assertThrows(IllegalArgumentException.class, () -> random.nextInt(-1));
    }

    @Test
    public void nextIntMinMax() {
        int i = random.nextInt(100, 1000);
        assertTrue(i < 1000);
        assertTrue(i >= 100);
    }

    @Test
    public void nextIntMinMaxMinLargerThanMax() {
        assertThrows(IllegalArgumentException.class, () -> random.nextInt(1000, 100));
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
    public void nextLongNegativeMax() {
        assertThrows(IllegalArgumentException.class, () -> random.nextLong(-1L));
    }

    @Test
    public void nextLongMinMax() {
        long l = random.nextLong(12000L, 15000L);
        assertTrue(l < 15000L);
        assertTrue(l >= 12000L);
    }

    @Test
    public void nextLongMinMaxMinLargerThanMax() {
        assertThrows(IllegalArgumentException.class, () -> random.nextLong(1500L, 1200L));
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
    public void nextFloatNegativeMax() {
        assertThrows(IllegalArgumentException.class, () -> random.nextFloat(-1.0F));
    }

    @Test
    public void nextFloatMinMax() {
        float f = random.nextFloat(2.0f, 5.0f);
        assertTrue(f < 5.0f);
        assertTrue(f >= 2.0f);
    }

    @Test
    public void nextFloatMinMaxMinLargerThanMax() {
        assertThrows(IllegalArgumentException.class, () -> random.nextFloat(5.0f, 2.0f));
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
    public void nextDoubleNegativeMax() {
        assertThrows(IllegalArgumentException.class, () -> random.nextDouble(-1.0));
    }

    @Test
    public void nextDoubleMinMax() {
        double d = random.nextDouble(2.0, 5.0);
        assertTrue(d < 5.0);
        assertTrue(d >= 2.0);
    }

    @Test
    public void nextDoubleMinMaxMinLargerThanMax() {
        assertThrows(IllegalArgumentException.class, () -> random.nextDouble(5.0, 2.0));
    }

    @Test
    public void nextDate() {
        Date d = random.nextDate();
    }

    @Test
    public void nextDateMax() {
        Date max = new Date(60335532000000L);
        Date d = random.nextDate(max);
        assertTrue(d.compareTo(max) < 0);
    }

    @Test
    public void nextDateNegativeMax() {
        assertThrows(IllegalArgumentException.class, () -> random.nextDate(new Date(-1L)));
    }

    @Test
    public void nextDateMinMax() {
        Date min = new Date(60335532000000L);
        Date max = new Date(61312543200000L);
        Date d = random.nextDate(min, max);
        assertTrue(d.compareTo(max) < 0);
        assertTrue(d.compareTo(min) >= 0);
    }

    @Test
    public void nextDateMinMaxMinLargerThanMax() {
        Date badMax = new Date(60335532000000L);
        Date badMin = new Date(61312543200000L);
        assertThrows(IllegalArgumentException.class, () -> random.nextDate(badMin, badMax));

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

    @Test
    public void nextString() {
        String s = random.nextString();
    }

    @Test
    public void nextStringLength() {
        String s = random.nextString(15);
        assertEquals(15, s.length());
    }

    @Test
    public void nextStringZeroLength() {
        String s = random.nextString(0);
        assertEquals(0, s.length());
    }

    @Test
    public void nextStringNegativeLength() {
        assertThrows(IllegalArgumentException.class, () -> random.nextString(-5));
    }

    @Test
    public void nextStringLengthChars() {
        char[] chars = {random.nextChar(), random.nextChar(), random.nextChar()};
        String s = random.nextString(15, chars);
        assertEquals(15, s.length());
        Arrays.sort(chars);
        for (char c : s.toCharArray()) {
            assertTrue(Arrays.binarySearch(chars, c) > -1);
        }
    }

    @Test
    public void nextStringZeroLengthChars() {
        char[] chars = {random.nextChar(), random.nextChar(), random.nextChar()};
        String s = random.nextString(0, chars);
        assertEquals(0, s.length());
    }

    @Test
    public void nextStringNegativeLengthChars() {
        char[] chars = {random.nextChar(), random.nextChar(), random.nextChar()};
        assertThrows(IllegalArgumentException.class, () -> random.nextString(-5, chars));
    }

    @Test
    public void nextStringNullChars() {
        assertThrows(IllegalArgumentException.class, () -> random.nextString(15, null));
    }

    @Test
    public void nextStringEmptyChars() {
        assertThrows(IllegalArgumentException.class, () -> random.nextString(15, new char[0]));
    }

    @Test
    public void nextStringMinMaxLength() {
        String s = random.nextString(2, 15);
        assertTrue(s.length() >= 2);
        assertTrue(s.length() < 15);
    }

    @Test
    public void nextStringLengthPrintable() {
        String s = random.nextString(15, true);
        assertEquals(15, s.length());
        assertAsciiPrintable(s);
    }

    @Test
    public void nextStringZeroLengthPrintable() {
        String s = random.nextString(0, true);
        assertEquals(0, s.length());
    }

    @Test
    public void nextStringNegativeLengthPrintable() {
        assertThrows(IllegalArgumentException.class, () -> random.nextString(-5, true));
    }

    @Test
    public void nextStringLengthNotPrintable() {
        String s = random.nextString(15, false);
        assertEquals(15, s.length());
    }

    @Test
    public void nextStringZeroLengthNotPrintable() {
        String s = random.nextString(0, false);
        assertEquals(0, s.length());
    }

    @Test
    public void nextStringNegativeLengthNotPrintable() {
        assertThrows(IllegalArgumentException.class, () -> random.nextString(-5, false));
    }

    @Test
    public void nextStringMinMaxLengthPrintable() {
        String s = random.nextString(2, 15, true);
        assertTrue(s.length() >= 2);
        assertTrue(s.length() < 15);
        assertAsciiPrintable(s);
    }

    @Test
    public void nextStringMinMaxLengthNotPrintable() {
        String s = random.nextString(2, 15, false);
        assertTrue(s.length() >= 2);
        assertTrue(s.length() < 15);
    }

    private static void assertAsciiPrintable(String s) {
        for (char c : s.toCharArray()) {
            assertTrue(c >= 32 && c < 127);
        }
    }

    @Test
    public void nextNumericString() {
        String s = random.nextNumericString(15);
        assertEquals(15, s.length());
        assertNumeric(s);
    }

    @Test
    public void nextNumericStringLong() {
        String s = random.nextNumericString(100);
        assertEquals(100, s.length());
        assertNumeric(s);
    }

    @Test
    public void nextNumericStringShort() {
        String s = random.nextNumericString(1);
        assertEquals(1, s.length());
        assertNumeric(s);
    }

    @Test
    public void nextNumericStringZeroLength() {
        assertThrows(IllegalArgumentException.class, () -> random.nextNumericString(0));
    }

    @Test
    public void nextNumericStringNegaticeLength() {
        assertThrows(IllegalArgumentException.class, () -> random.nextNumericString(-5));
    }

    private static void assertNumeric(String s) {
        assertNotEquals('0', s.charAt(0));
        for (char c : s.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void nextPropertyString() {
        String s = random.nextPropertyString(10);
        assertEquals(10, s.length());
        for (char c : s.toCharArray()) {
            assertTrue(Character.isJavaIdentifierPart(c));
        }
    }

    @Test
    public void nextPropertyStringZeroLength() {
        String s = random.nextPropertyString(0);
        assertEquals(0, s.length());
    }

    @Test
    public void nextPropertyStringNegativeLength() {
        assertThrows(IllegalArgumentException.class, () -> random.nextPropertyString(-5));
    }
}
