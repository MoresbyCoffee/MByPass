/*
 * Moresby Coffee Bean
 *
 * Copyright (c) 2012, Barnabas Sudy (barnabas.sudy@gmail.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.moresbycoffee.pass.api;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class which provides different implementations of {@link Converter}.
 */
public final class Converters {

    /**
     * <p>Implementation of {@link Converter} which is able to convert an integer to a byte[] containing the byte representation of the integer.</p>
     */
    public static final Converter<Integer, byte[]> INT_TO_BYTE = new Converter<Integer, byte[]>() {

        private static final int LENGTH = 4;

        @Override
        public byte[] convert(final Integer from) throws ConverterException {
            if (from == null) {
                return null;
            }
            int value = from.intValue();
            byte[] result = new byte[LENGTH];
            for (int i = 0; i < result.length; i++) {
                result[i] = (byte) (value >>> ((result.length - 1 - i) * 8));
            }
            return result;
        }

    };

    /**
     * <p>Implementation of {@link Converter} which is able to deconvert the converted integer (by {@link #INT_TO_BYTE}).</p>
     */
    public static final Converter<byte[], Integer> BYTE_TO_INT = new Converter<byte[], Integer>() {

        @Override
        public Integer convert(final byte[] from) throws ConverterException {
            if (from == null) {
                return null;
            }
            if (from.length > 4) {
                throw new ConverterException("The maximum length of a byte array containing a integer is 4. This array is " + from.length + " long.");
            }

            int result = 0;
            for (int i = 0; i < from.length; i++) {
                result <<= 8;
                result ^= from[i] & 0xFF;
            }

            return Integer.valueOf(result);

        }

    };

    /**
     * <p>Implementation of {@link Converter} which is able to convert an long to a byte[] containing the byte representation of the long.</p>
     */
    public static final Converter<Long, byte[]> LONG_TO_BYTE = new Converter<Long, byte[]>() {

        private static final int LENGTH = 8;

        @Override
        public byte[] convert(final Long from) throws ConverterException {
            if (from == null) {
                return null;
            }
            long value = from.longValue();
            byte[] result = new byte[LENGTH];
            for (int i = 0; i < result.length; i++) {
                result[i] = (byte) (value >>> ((result.length - 1 - i) * 8));
            }
            return result;
        }

    };

    /**
     * <p>Implementation of {@link Converter} which is able to deconvert the converted long (by {@link #LONG_TO_BYTE}).</p>
     */
    public static final Converter<byte[], Long> BYTE_TO_LONG = new Converter<byte[], Long>() {

        @Override
        public Long convert(final byte[] from) throws ConverterException {
            if (from == null) {
                return null;
            }
            if (from.length > 8) {
                throw new ConverterException("The maximum length of a byte array containing a long is 8. This array is " + from.length + " long.");
            }

            long result = 0;
            for (int i = 0; i < from.length; i++) {
                result <<= 8;
                result ^= (long) from[i] & 0xFF;
            }

            return Long.valueOf(result);

        }

    };


    /**
     * <p>Implementation of {@link Converter} which is able to convert an double to a byte[] containing the byte representation of the double.</p>
     */
    public static final Converter<Double, byte[]> DOUBLE_TO_BYTE = new Converter<Double, byte[]>() {

        @Override
        public byte[] convert(final Double from) throws ConverterException {
            if (from == null) {
                return null;
            }
            return LONG_TO_BYTE.convert(Long.valueOf(Double.doubleToRawLongBits(from.doubleValue())));
        }

    };

    /**
     * <p>Implementation of {@link Converter} which is able to convert an float to a byte[] containing the byte representation of the float.</p>
     */
    public static final Converter<Float, byte[]> FLOAT_TO_BYTE = new Converter<Float, byte[]>() {

        @Override
        public byte[] convert(final Float from) throws ConverterException {
            if (from == null) {
                return null;
            }
            return INT_TO_BYTE.convert(Integer.valueOf(Float.floatToRawIntBits(from.floatValue())));
        }

    };


    /**
     * <p>Implementation of {@link Converter} which is able to convert a String to its byte[] representation in which the content is utf8 encoded.</p>
     */
    public static final Converter<String, byte[]> STRING_TO_UTF8 = new Converter<String, byte[]>() {

        @Override
        public byte[] convert(final String from) throws ConverterException {
            if (from == null) {
                return null;
            }
            try {
                return from.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new ConverterException(e); //Should never happen.
            }
        }


    };

    /**
     * <p>Implementation of {@link Converter} which is able to convert a String to Long using the {@link Long#valueOf(String)} method.</p>
     * <p>If the input value is <tt>null</tt> than the converter returns a <tt>null</tt>
     */
    public static final Converter<String, Long> STRING_TO_LONG = new Converter<String, Long>() {

        @Override
        public Long convert(final String from) throws ConverterException {
            if (from == null) {
                return null;
            }
            try {
                return Long.valueOf(from);
            } catch (final NumberFormatException e) {
                throw new ConverterException(e);
            }
        }

    };

    /**
     * <p>Implementation of {@link Converter} which is able to convert a Long to a String.</p>
     * <p>If the input value is <tt>null</tt> than the converter returns a <tt>null</tt>
     */
    public static final Converter<Long, String> LONG_TO_STRING = new Converter<Long, String>() {

        @Override
        public String convert(final Long from) throws ConverterException {
            if (from == null) {
                return null;
            }
            return from.toString();
        }

    };

    /**
     * <p>Implementation of {@link Converter} which is able to convert a String to Integer using the {@link Integer#valueOf(String)} method.</p>
     * <p>If the input value is <tt>null</tt> than the converter returns a <tt>null</tt>
     */
    public static final Converter<String, Integer> STRING_TO_INTEGER = new Converter<String, Integer>() {

        @Override
        public Integer convert(final String from) throws ConverterException {
            if (from == null) {
                return null;
            }
            try {
                return Integer.valueOf(new BigDecimal(from).intValueExact());
            } catch (final Exception e) {
                throw new ConverterException(e);
            }
        }

    };

    /**
     * <p>Implementation of {@link Converter} which is able to convert a Long to a String.</p>
     * <p>If the input value is <tt>null</tt> than the converter returns a <tt>null</tt>
     */
    public static final Converter<Integer, String> INTEGER_TO_STRING = new Converter<Integer, String>() {

        @Override
        public String convert(final Integer from) throws ConverterException {
            if (from == null) {
                return null;
            }
            return from.toString();
        }

    };

    /**
     * <p>Implementation of {@link Converter} which is able to convert a String to Double using the {@link Double#valueOf(String)} method.</p>
     * <p>If the input value is <tt>null</tt> than the converter returns a <tt>null</tt>
     */
    public static final Converter<String, Double> STRING_TO_DOUBLE = new Converter<String, Double>() {

        @Override
        public Double convert(final String from) throws ConverterException {
            if (from == null) {
                return null;
            }
            try {
                return Double.valueOf(from);
            } catch (final NumberFormatException e) {
                throw new ConverterException(e);
            }
        }

    };

    /**
     * <p>Implementation of {@link Converter} which is able to convert a String to Float using the {@link Float#valueOf(String)} method.</p>
     * <p>If the input value is <tt>null</tt> than the converter returns a <tt>null</tt>
     */
    public static final Converter<String, Float> STRING_TO_FLOAT = new Converter<String, Float>() {

        @Override
        public Float convert(final String from) throws ConverterException {
            if (from == null) {
                return null;
            }
            try {
                return Float.valueOf(from);
            } catch (final NumberFormatException e) {
                throw new ConverterException(e);
            }
        }

    };

    /**
     * <p>Implementation of {@link Converter} which is able to convert a Long to a String.</p>
     * <p>If the input value is <tt>null</tt> than the converter returns a <tt>null</tt>
     */
    public static final Converter<Number, String> NUMBER_TO_STRING = new Converter<Number, String>() {

        @Override
        public String convert(final Number from) throws ConverterException {
            if (from == null) {
                return null;
            }
            return from.toString();
        }

    };

    /**
     * Converts a {@link Long} to {@link Integer} if the value of the long is int the integer range.
     * Otherwise it will throw a {@link ConverterException}.
     */
    public static final Converter<Long, Integer> LONG_TO_INT = new Converter<Long, Integer>() {

        /** {@inheritDoc} */
        @Override
        public Integer convert(final Long from) throws ConverterException {
            if (from == null) {
                return null;
            }
            if (from.longValue() > Integer.MAX_VALUE | from.longValue() < Integer.MIN_VALUE) {
                throw new ConverterException("The value of long is out of the integer range: " + from.longValue());
            }
            return Integer.valueOf(from.intValue());
        }
    };

    public static final Converter<String, Boolean> STRING_TO_BOOLEAN = new Converter<String, Boolean>() {

        @Override
        public Boolean convert(final String from) throws ConverterException {
            if (from == null) {
                return null;
            }
            return new Boolean(from);

        }

    };

    public static final Converter<String, BigDecimal> STRING_TO_BIGDECIMAL = new Converter<String, BigDecimal>() {

        @Override
        public BigDecimal convert(final String from) throws ConverterException {
            if (from == null) {
                return null;
            }
            try {
                return new BigDecimal(from);
            } catch (final NumberFormatException e) {
                throw new ConverterException(e);
            }
        }

    };

    /**
     * Converts a string to upper case string.
     */
    public static final Converter<String, String> TO_UPPERCASE = new Converter<String, String>() {

        @Override
        public String convert(final String from) throws ConverterException {
            if (from == null) {
                return null;
            }
            return from.toUpperCase();
        }

    };

    /**
     * Converts a string to lower case string.
     */
    public static final Converter<String, String> TO_LOWERCASE = new Converter<String, String>() {

        @Override
        public String convert(final String from) throws ConverterException {
            if (from == null) {
                return null;
            }
            return from.toLowerCase();
        }

    };

    public static final Converter<String, String> TRIM = new Converter<String, String>() {

        @Override
        public String convert(final String from) throws ConverterException {
            if (from == null) {
                return null;
            }
            return from.trim();
        }

    };

    public static Converter<String, String[]> splitter(final String regex) {

        return new Converter<String, String[]>() {

            @Override
            public String[] convert(final String from) throws ConverterException {
                if (from == null) {
                    return null;
                }
                return from.split(regex);
            }

        };

    }

    /**
     * Converts a UTC timestamp to Date object using the {@link Date#Date(long)} constructor.
     */
    public static final Converter<Long, Date> TIMESTAMP_TO_DATE = new Converter<Long, Date>() {

        @Override
        public Date convert(final Long from) throws ConverterException {
            if (from == null) {
                return null;
            }
            return new Date(from.longValue());
        }

    };

    /**
     * Converts a {@link Date} object to UTC timestamp.
     */
    public static final Converter<Date, Long> DATE_TO_TIMESTAMP = new Converter<Date, Long>() {

        @Override
        public Long convert(final Date from) throws ConverterException {
            if (from == null) {
                return null;
            }
            return Long.valueOf(from.getTime());
        }


    };

    public static final Converter<Object, String> TO_STRING = new Converter<Object, String>() {
        
        @Override
        public String convert(final Object from) throws ConverterException {
            if (from == null) {
                return null;
            }
            return String.valueOf(from);
        };
    };

    /**
     * Converts an array to a list using {@link Arrays#asList(Object...)}.
     *
     * @param <T> The type of the elements of the array.
     * @return The converter.
     */
    public static <T> Converter<T[], Collection<T>> toList() {

        return new Converter<T[], Collection<T>>() {

            @Override
            public Collection<T> convert(final T[] from) throws ConverterException {
                if (from == null) {
                    return null;
                }
                return Arrays.asList(from);
            }

        };

    }

    /**
     * Converter which converts a Collection to a sorted ArrayList.
     * @param <T> The type of the collection.
     */
    public static class Sorter<T extends Comparable<? super T>> implements Converter<Collection<T>, ArrayList<T>> {

        @Override
        public ArrayList<T> convert(final Collection<T> from) throws ConverterException {
            ArrayList<T> sortList = new ArrayList<T>(from);
            Collections.sort(sortList);
            return sortList;
        }

    }

    /**
     * Helper method which uses {@link Sorter} to sort a collection.
     * @param from The collection which should be sorted.
     * @return The sorted collection.
     */
    public static <T extends Comparable<? super T>> ArrayList<T> sort(final Collection<T> from) {
        return new Sorter<T>().convert(from);
    }

    /**
     * Sort converter that can be used in chains.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static final Converter<Collection<? extends Comparable>, ArrayList<? extends Comparable>> SORT = new Sorter();

    /**
     * <p>One implementation of {@link Converter} interface which is able to convert the elements of a {@link Collection}.
     * The result will be yield into an ArrayList.</p>
     *
     * <p>The implementation needs an other converter which is able to convert the elements.</p>
     *
     * @param <F> The type of the elements of the input {@link Collection}.
     * @param <T> The type of the elements of the output {@link ArrayList}
     */
    public static final class CollectionConverterToArrayList<F, T> implements Converter<Collection<? extends F>, ArrayList<T>> {

        private final Converter<F, T> converter;

        /**
         * @param converter  The converter which converts the elements of the Collection.
         */
        public CollectionConverterToArrayList(final Converter<F, T> converter) {
            this.converter = converter;
        }

        /** {@inheritDoc} */
        @Override
        public ArrayList<T> convert(final Collection<? extends F> from) throws ConverterException {
            if (from == null) {
                return null;
            }

            final ArrayList<T> result = new ArrayList<T>();
            for (F fromObject : from) {
                result.add(converter.convert(fromObject));
            }
            return result;
        }

    }


    /**
     * Convert the elements of a {@link Collection} and returns the result in a {@link List}.
     * 
     * @param converter The converter which converts the elements of the Collection.
     * @param from The collection.
     * @return The List of the converted objects.
     * @throws ConverterException
     */
    public static <F, T> List<T> convertList(final Converter<F, T> converter, final Collection<? extends F> from) throws ConverterException {
        CollectionConverterToArrayList<F, T> listConverter = new CollectionConverterToArrayList<F, T>(converter);
        return listConverter.convert(from);

    }

    /**
     * Returns a {@link Converter} what converts the elements of a {@link Collection} and returns the result in a {@link List}.
     * 
     * @param converter The converter to convert the element of the list.
     * @return The {@link Converter} what can convert elements of {@link Collection}.
     */
    public static <F, T> Converter<Collection<? extends F>, ArrayList<T>> listConverter(final Converter<F, T> converter) {
        return new CollectionConverterToArrayList<F, T>(converter);
    }

    /**
     * Generates a {@link HashMap} from the a collection generating the key with the keyExtractor converter.
     *
     * @param keyExtractor The converter to generate a key.
     * @param from The collection to be converted.
     * @return The extracted map.
     * @throws ConverterException If error occurs during the conversion.
     */
    public static <F, T> Map<T, F> extractHashMap(final Converter<F, T> keyExtractor, final Collection<? extends F> from) throws ConverterException {
        if (from == null) {
            return null;
        }
        final HashMap<T, F> results = new HashMap<T, F>();
        for (F item : from) {
            results.put(keyExtractor.convert(item), item);
        }
        return results;
    }

    /**
     * <p>Builds a map from a {@link Collection} by a <tt>keyExtractor</tt> converter. The keyExtractor's responsibility is to
     * extract a {@link Map.Entry#getKey() key} from the collection element.</p>
     * <p>This implementation uses the {@link #extractHashMap(Converter, Collection)} method what will build a {@link HashMap}. 
     * The order of the elements is not kept in the result.</p>
     * <p><strong>Warning:</strong> Use this carefully! If the key extractor returns the same key for different elements of the collection,
     * the map will contain only the last one of them.</p>
     * 
     * @param keyExtractor {@link Converter} to extract the key from the Collection element.
     * @return The map created from the collection elements.
     * @throws ConverterException If error occurs during the conversion.
     */
    public static <F, T> Converter<Collection<? extends F>, Map<T, F>> mapExtractor(final Converter<F, T> keyExtractor) throws ConverterException {
        
        return new Converter<Collection<? extends F>, Map<T, F>>() {

            @Override
            public Map<T, F> convert(final Collection<? extends F> from) throws ConverterException {
                return extractHashMap(keyExtractor, from);
            }
        };
    }
    
    /**
     * TODO javadoc.
     * 
     * @param valueConverter
     * @param from
     * @return
     * @throws ConverterException
     */
    public static <K, F, T> Map<K, T> convertMapValues(final Converter<F, T> valueConverter, final Map<K, ? extends F> from) throws ConverterException {
        if (from == null) {
            return null;
        }
        final HashMap<K, T> results = new HashMap<K, T>();
        for (Map.Entry<K, ? extends F> entry : from.entrySet()) {
            results.put(entry.getKey(), valueConverter.convert(entry.getValue()));
        }
        return results;
    }
    
    /**
     * TODO javadoc.
     * 
     * @param valueConverter
     * @return
     */
    public static <K, F, T> Converter<Map<K, ? extends F>, Map<K, T>> mapValueConverter(final Converter<F, T> valueConverter) {
        
        return new Converter<Map<K, ? extends F>, Map<K, T>>() {

            @Override
            public Map<K, T> convert(final Map<K, ? extends F> from) throws ConverterException {
                return convertMapValues(valueConverter, from);
            }
            
        };
        
    }
    
    /**
     * Returns the first element of a collection.
     *
     * @param collection The collection. (Nullable)
     * @return The first element of the collection if it exists. (Nullable)
     */
    public static <T> T first(final Collection<T> collection) {
        return new First<T>().convert(collection);
    }

    /**
     * Converts a collection into its first element.
     *
     * @param <T>
     *
     * @author bsudy
     * @since 2012
     */
    public static class First<T> implements Converter<Collection<T>, T> {

        /** {@inheritDoc} */
        @Override
        public T convert(final Collection<T> from) throws ConverterException {
            if (from == null || from.isEmpty()) {
                return null;
            }
            return from.iterator().next();
        }

    }

    /**
     * Special converter which can combine two converters and convert the from the input of the first converter
     * to the output of the second converter.
     *
     * @param <F> The type of the input.
     * @param <T1> The intermediate type.
     * @param <T> The type of the output.
     *
     * @author bsudy
     * @since 2012
     */
    public static class Chain<F, T1, T> implements Converter<F, T> {

        private final Converter<F, ? extends T1> c1;
        private final Converter<T1, ? extends T> c2;

        public Chain(final Converter<F, ? extends T1> c1, final Converter<T1, ? extends T> c2) {
            this.c1 = c1;
            this.c2 = c2;
        }

        /** {@inheritDoc} */
        @Override
        public T convert(final F from) throws ConverterException {
            return c2.convert(c1.convert(from));
        }

    }

    /**
     * TODO javadoc.
     * 
     * @param c1
     * @param c2
     * @return
     */
    public static <F, T1, T> Converter<F, T> chain(final Converter<F, ? extends T1> c1, final Converter<T1, ? extends T> c2) {
        return new Chain<F, T1, T>(c1, c2);
    }

    /**
     * TODO javadoc.
     * 
     * @param c1
     * @param c2
     * @param c3
     * @return
     */
    public static <F, T1, T2, T> Converter<F, T> chain(final Converter<F, ? extends T1> c1, final Converter<T1, ? extends T2> c2, final Converter<T2, ? extends T> c3) {
        return new Chain<F, T1, T>(c1, new Chain<T1, T2, T>(c2, c3));
    }

    /**
     * TODO javadoc.
     * 
     * @param c1
     * @param c2
     * @param c3
     * @param c4
     * @return
     */
    public static <F, T1, T2, T3, T> Converter<F, T> chain(final Converter<F, ? extends T1> c1, final Converter<T1, ? extends T2> c2, final Converter<T2, ? extends T3> c3, final Converter<T3, ? extends T> c4) {
        return new Chain<F, T1, T>(c1, new Chain<T1, T2, T>(c2, new Chain<T2, T3, T>(c3, c4)));
    }

    /** Hidden constructor of the utility class to avoid the instantiation. */
    private Converters() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

}
