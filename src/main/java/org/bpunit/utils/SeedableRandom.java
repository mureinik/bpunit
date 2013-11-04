package org.bpunit.utils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

/**
 * <code>SeedableRandom</code> is a random generator with more powerful random generating methods, based on work done on
 * the <code>org.ovirt.engine.core.utils.RandomUtils</code> class in the oVirt Engine project.
 * 
 * Several random string generation methods were inspired from <code>org.apache.commons.lang.RandomStringUtils</code>,
 * although greatly simplified
 * 
 * @see java.util.Random
 */
@SuppressWarnings("serial")
public class SeedableRandom extends Random {

    /* --- Class constants --- */

    /** Error message for a case when the min is larger than max, */
    private static final String MIN_MAX_ERROR = "min must be less than or equal to max";

    /** The first printable character. */
    private static final char FIRST_PRINTABLE_CHAR = ' ';

    /** The last printable character. */
    private static final char LAST_PRINTABLE_CHAR = '~';

    /** The first XML printable character. */
    private static final char FIRST_XML_PRINTABLE_CHAR = 'A';

    /** The last XML printable character. */
    private static final char LAST_XML_PRINTABLE_CHAR = 'Z';

    /** The legal characters for an entity name. */
    private static final char[] LEGAL_PROPERTY_CHARS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_".toCharArray();

    /* --- Class Fields --- */

    /** The seed that was last set. */
    private long seed;

    /* --- Constructor --- */

    /**
     * Constructor from the seed.
     */
    public SeedableRandom(long seed) {
        setSeed(seed);
    }

    /* --- Seed-related Methods --- */

    /**
     * The last seed is saved, so it is possible to {@link #getSeed()} it later. Since {@link Random}'s seed is private,
     * I am obliged to save my own copy.
     * 
     * See {@link Random#setSeed(long)}.
     */
    @Override
    public void setSeed(long seed) {
        super.setSeed(seed);
        this.seed = seed;
    }

    /**
     * Returns the seed that was set last.
     */
    public long getSeed() {
        return seed;
    }

    /* --- Byte-related Methods --- */

    /**
     * Randomizes a <tt>byte</tt> value.
     */
    public byte nextByte() {
        return (byte) super.nextInt();
    }

    /**
     * Randomize a <tt>byte</tt> value between 0 (inclusive) and the specified value (exclusive).
     */
    public byte nextByte(byte b) {
        return (byte) super.nextInt(b);
    }

    /**
     * Randomize a <tt>byte</tt> value in the given range [min, max].
     */
    public byte nextByte(byte min, byte max) {
        if (min > max) {
            throw new IllegalArgumentException(MIN_MAX_ERROR);
        }

        return (byte) (min + nextByte((byte) (max - min + 1)));
    }

    /* --- Short-related Methods --- */

    /**
     * Randomizes a <tt>short</tt> value.
     */
    public short nextShort() {
        return (short) super.nextInt();
    }

    /**
     * Randomize a <tt>short</tt> value between 0 (inclusive) and the specified value (exclusive).
     */
    public short nextShort(short s) {
        return (short) super.nextInt(s);
    }

    /**
     * Randomize a <tt>short</tt> value in the given range [min, max].
     */
    public short nextShort(short min, short max) {
        if (min > max) {
            throw new IllegalArgumentException(
                    MIN_MAX_ERROR);
        }

        return (short) (min + nextShort((short) (max - min + 1)));
    }

    /* --- Integer-related Methods --- */

    /**
     * Randomize an <tt>int</tt> and return it as an {@link Integer}
     */
    public Integer nextInteger() {
        return nextInt();
    }

    /**
     * Randomize an <tt>int</tt> value in the given range [min, max].
     */
    public int nextInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(
                    MIN_MAX_ERROR);
        }

        return (min + super.nextInt(max - min + 1));
    }

    /* --- Long-related Methods --- */

    /**
     * Randomize a <tt>long</tt> value between 0 (inclusive) and the specified value (exclusive).
     */
    public long nextLong(long l) {
        if (l <= 0) {
            throw new IllegalArgumentException("l must be greater than 0!");
        }

        long rand = super.nextLong();
        if (rand == Long.MIN_VALUE) {
            rand++;
        }
        return (Math.abs(rand) % l);
    }

    /**
     * Randomize a <tt>long</tt> value in the given range [min, max].
     */
    public long nextLong(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException(
                    MIN_MAX_ERROR);
        }

        return (min + nextLong(max - min + 1));
    }

    /* --- Float-related Methods --- */

    /**
     * Randomize a <tt>float</tt> value between 0.0 (inclusive) and the specified value (exclusive).
     */
    public float nextFloat(float f) {
        return nextFloat(f, false);
    }

    /**
     * Randomize a <tt>float</tt> value between 0.0 (inclusive) and the specified value (inclusive or exclusive as
     * required).
     * 
     * @param inclusive
     *            Whether or not, the returned value should include the given one.
     */
    public float nextFloat(float f, boolean inclusive) {
        if (f <= 0.0F) {
            throw new IllegalArgumentException("f must be greater than 0!");
        }

        // Randomize a float
        float rand = super.nextFloat();

        // If the returned value should not include the given one,
        // make sure that the randomized float is not exactly 1.0
        if (!inclusive) {
            while (rand == 1.0F) {
                rand = super.nextFloat();
            }
        }

        return (rand * f);
    }

    /**
     * Randomize a <tt>float</tt> value in the given range [min, max].
     */
    public float nextFloat(float min, float max) {
        if (min > max) {
            throw new IllegalArgumentException(
                    MIN_MAX_ERROR);
        }

        return (min + nextFloat(max - min, true));
    }

    /* --- Double-related Methods --- */

    /**
     * Randomize a <tt>double</tt> value between 0.0 (inclusive) and the specified value (exclusive).
     */
    public double nextDouble(double d) {
        return nextDouble(d, false);
    }

    /**
     * Randomize a <tt>double</tt> value between 0.0 (inclusive) and the specified value (inclusive or exclusive as
     * required).
     * 
     * @param inclusive
     *            Whether or not, the returned value should include the given one.
     */
    public double nextDouble(double d, boolean inclusive) {
        if (d <= 0.0D) {
            throw new IllegalArgumentException("d must be greater than 0!");
        }

        // Randomize a double
        double rand = super.nextDouble();

        // If the returned value should not include the given one,
        // make sure that the randomized float is not exactly 1.0
        if (!inclusive) {
            while (rand == 1.0D) {
                rand = super.nextDouble();
            }
        }

        return (rand * d);
    }

    /* --- Date Methods --- */

    public Date nextDate() {
        return new Date(nextLong());
    }

    /* --- Collections-related Methods --- */

    /**
     * Picks a random element from the given <code>Collection</code>.
     */
    public <T> T pickRandom(Collection<T> c) {
        int elementIndex = super.nextInt(c.size());

        Iterator<T> iter = c.iterator();
        for (int i = 0; i < elementIndex; ++i) {
            iter.next();
        }

        return iter.next();
    }

    /* --- Array-related Methods --- */

    /**
     * Picks a random element from the given array.
     */
    public <T> T pickRandom(T[] o) {
        return pickRandom(Arrays.asList(o));
    }

    /* --- String-related Methods --- */

    /**
     * Randomize a <code>String</code>.
     * 
     * @param length
     *            The requested length of the string.
     * @param printable
     *            Whether or not, the string should contain only printable characters.
     */
    public String nextString(int length, boolean printable) {
        if (printable) {
            byte[] data = new byte[length];

            for (int i = 0; i < length; ++i) {
                data[i] = (byte) nextInt(
                        FIRST_PRINTABLE_CHAR,
                        LAST_PRINTABLE_CHAR);
            }

            return new String(data);

        }
        return new String(nextBytes(length));
    }

    /**
     * Randomize a valid numeric string.
     * 
     * @param length
     *            The requested length of the string.
     */
    public String nextNumericString(int length) {
        return Long.toString(nextLong(
                (long) Math.pow(10, length - 1), (long) (Math.pow(10, length) - 1)));
    }

    /**
     * Randomize a valid XML Element name.
     * 
     * @param length
     *            The requested length of the string.
     */
    public String nextXmlString(int length) {
        byte[] data = new byte[length];
        for (int i = 0; i < length; ++i) {
            data[i] = (byte) nextInt(
                    FIRST_XML_PRINTABLE_CHAR,
                    LAST_XML_PRINTABLE_CHAR);
        }

        return new String(data);
    }

    /**
     * Randomize a valid entity name.
     * 
     * @param length
     *            The requested length of the string.
     */
    public String nextPropertyString(int length) {
        return nextString(length, LEGAL_PROPERTY_CHARS);
    }

    /**
     * Randomize a printable <code>String</code>.
     */
    public String nextString() {
        return nextString(10, true);
    }

    /**
     * Randomize a printable <code>String</code>.
     * 
     * @param length
     *            The requested length of the string.
     */
    public String nextString(int length) {
        return nextString(length, true);
    }

    /**
     * Randomize a <code>String</code> made up of the given characters
     * 
     * @param length
     *            The requested length of the string.
     */
    public String nextString(int length, char[] chars) {
        if (length == 0) {
            return "";
        } else if (length < 0) {
            throw new IllegalArgumentException("Requested random string length " + length + " is less than 0.");
        }

        if (chars == null) {
            throw new IllegalArgumentException("The chars array must not be null");
        }
        else if (chars.length == 0) {
            throw new IllegalArgumentException("The chars array must not be empty");
        }

        final char[] buffer = new char[length];

        for (int i = 0; i < length; ++i) {
            buffer[i] = chars[next(chars.length)];
        }
        return new String(buffer);
    }

    /**
     * Randomize a <code>String</code> of a length in the given range [min, max].
     * 
     * @param printable
     *            Whether or not, the string should contain only printable characters.
     */
    public String nextString(int min, int max, boolean printable) {
        return nextString(nextInt(min, max), printable);
    }

    /**
     * Randomize a printable <code>String</code> of a length in the given range [min, max].
     */
    public String nextString(int min, int max) {
        return nextString(nextInt(min, max), true);
    }

    /* --- General Utility Methods --- */

    /**
     * Creates a <tt>byte</tt> array of the specified size, initialized with random values.
     */
    public byte[] nextBytes(int size) {
        byte[] data = new byte[size];
        nextBytes(data);
        return data;
    }

    /* -- Big Integer related methods -- */

    /**
     * generates a new big integer with the desired number of bits the generated number will always be positive.
     * 
     * @param numOfBits
     *            the number of bits of the Big Integer
     * @return the randomized big integer.
     */
    public BigInteger nextBigInt(int numOfBits) {
        return new BigInteger(numOfBits, this);
    }

    /**
     * Returns a random value from an enum.
     * 
     * @param <T>
     *            The enum type.
     * @param enumClass
     *            The enum class to randomize.
     * 
     * @return A random enum from the given enum, or null if got null.
     */
    public <T extends Enum<?>> T nextEnum(Class<T> enumClass) {
        if (enumClass == null) {
            return null;
        }

        return pickRandom(enumClass.getEnumConstants());
    }
}
