package org.bpunit.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code SeedableRandom} is a random generator with more powerful random generating methods, based on work done on
 * the {@code org.ovirt.engine.core.utils.RandomUtils} class in the oVirt Engine project.
 * 
 * Several random string generation methods were inspired from {@code org.apache.commons.lang.RandomStringUtils},
 * although greatly simplified
 * 
 * @see java.util.Random
 */
@SuppressWarnings("serial")
public class SeedableRandom extends Random {

    /** The logger to use. */
    private static final Logger log = LoggerFactory.getLogger(SeedableRandom.class);

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

    /** The default seed to use with {@link #SeedableRandom()} */
    private static final long DEFAULT_SEED = 19811611;

    /* --- Class Fields --- */

    /** The seed that was last set. */
    private long seed;

    /* --- Constructors --- */

    /**
     * Default constructor.
     *
     * If the {@code BPUNIT.SEED} environment variable is set, it would be used as the seed.
     * If it is not, {@link #DEFAULT_SEED} will be used;
     *
     * @throws NumberFormatException if {@code BPUNIT.SEED} is set to a value that cannot be parsed to a {@code long}.
     */
    public SeedableRandom() {
        long seed = DEFAULT_SEED;
        String envSeed = System.getProperty("BPUNIT.SEED");
        if (envSeed != null) {
            seed = Long.parseLong(envSeed);
        }
        setSeed(seed);
    }

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
        log.info (getClass() + " using seed: " + seed);
    }

    /**
     * Returns the seed that was set last.
     */
    public long getSeed() {
        return seed;
    }

    /* --- Byte-related Methods --- */

    /**
     * Randomizes a {@code byte} value.
     */
    public byte nextByte() {
        return (byte) super.nextInt();
    }

    /**
     * Randomize a {@code byte} value between 0 (inclusive) and the specified value (exclusive).
     */
    public byte nextByte(byte b) {
        return (byte) super.nextInt(b);
    }

    /**
     * Randomize a {@code byte} value in the given range [min, max].
     */
    public byte nextByte(byte min, byte max) {
        if (min > max) {
            throw new IllegalArgumentException(MIN_MAX_ERROR);
        }

        return (byte) (min + nextByte((byte) (max - min + 1)));
    }

    /* --- Short-related Methods --- */

    /**
     * Randomizes a {@code short} value.
     */
    public short nextShort() {
        return (short) super.nextInt();
    }

    /**
     * Randomize a {@code short} value between 0 (inclusive) and the specified value (exclusive).
     */
    public short nextShort(short s) {
        return (short) super.nextInt(s);
    }

    /**
     * Randomize a {@code short} value in the given range [min, max].
     */
    public short nextShort(short min, short max) {
        if (min > max) {
            throw new IllegalArgumentException(MIN_MAX_ERROR);
        }

        return (short) (min + nextShort((short) (max - min + 1)));
    }

    /* --- Integer-related Methods --- */

    /**
     * Randomize an {@code int} value in the given range [min, max].
     */
    public int nextInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(MIN_MAX_ERROR);
        }

        return (min + super.nextInt(max - min + 1));
    }

    /* --- Long-related Methods --- */

    /**
     * Randomize a {@code long} value between 0 (inclusive) and the specified value (exclusive).
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
     * Randomize a {@code long} value in the given range [min, max].
     */
    public long nextLong(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException(MIN_MAX_ERROR);
        }

        return (min + nextLong(max - min + 1));
    }

    /* --- Float-related Methods --- */

    /**
     * Randomize a {@code float} value between 0.0 (inclusive) and the specified value (exclusive).
     */
    public float nextFloat(float f) {
        return nextFloat(f, false);
    }

    /**
     * Randomize a {@code float} value between 0.0 (inclusive) and the specified value (inclusive or exclusive as
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
     * Randomize a {@code float} value in the given range [min, max].
     */
    public float nextFloat(float min, float max) {
        if (min > max) {
            throw new IllegalArgumentException(MIN_MAX_ERROR);
        }

        return (min + nextFloat(max - min, false));
    }

    /* --- Double-related Methods --- */

    /**
     * Randomize a {@code double} value between 0.0 (inclusive) and the specified value (exclusive).
     */
    public double nextDouble(double d) {
        return nextDouble(d, false);
    }

    /**
     * Randomize a {@code double} value between 0.0 (inclusive) and the specified value (inclusive or exclusive as
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

    /**
     * Randomize a {@code double} value in the given range [min, max].
     */
    public double nextDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException(MIN_MAX_ERROR);
        }

        return (min + nextDouble(max - min, false));
    }

    /* --- Date Methods --- */

    public Date nextDate() {
        return new Date(nextLong());
    }

    /* --- Collections-related Methods --- */

    /**
     * Picks a random element from the given {@link Collection}.
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
     * Randomize a {@link String}.
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
                data[i] = (byte) nextInt(FIRST_PRINTABLE_CHAR, LAST_PRINTABLE_CHAR);
            }

            return new String(data, StandardCharsets.UTF_8);

        }
        return new String(nextBytes(length), StandardCharsets.UTF_8);
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
            data[i] = (byte) nextInt(FIRST_XML_PRINTABLE_CHAR, LAST_XML_PRINTABLE_CHAR);
        }

        return new String(data, StandardCharsets.UTF_8);
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
     * Randomize a printable {@link String}.
     */
    public String nextString() {
        return nextString(10, true);
    }

    /**
     * Randomize a printable {@link String}.
     * 
     * @param length
     *            The requested length of the string.
     */
    public String nextString(int length) {
        return nextString(length, true);
    }

    /**
     * Randomize a {@link String} made up of the given characters
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
     * Randomize a {@link String} of a length in the given range [min, max].
     * 
     * @param printable
     *            Whether or not, the string should contain only printable characters.
     */
    public String nextString(int min, int max, boolean printable) {
        return nextString(nextInt(min, max), printable);
    }

    /**
     * Randomize a printable {@link String} of a length in the given range [min, max].
     */
    public String nextString(int min, int max) {
        return nextString(nextInt(min, max), true);
    }

    /* --- General Utility Methods --- */

    /**
     * Creates a {@code byte} array of the specified size, initialized with random values.
     */
    public byte[] nextBytes(int size) {
        byte[] data = new byte[size];
        nextBytes(data);
        return data;
    }

    /* -- Big Integer related methods -- */

    /**
     * Randomize a positive {@link BigInteger} value represented by no more than {@code numBits} bits.
     *
     * @param numOfBits
     *            the maximum number of bits of the Big Integer
     * @return the randomized {@link BigInteger}.
     */
    public BigInteger nextBigInteger(int numOfBits) {
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
