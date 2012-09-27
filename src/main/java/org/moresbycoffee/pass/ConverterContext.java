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
package org.moresbycoffee.pass;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.moresbycoffee.pass.api.Converter;
import org.moresbycoffee.pass.api.ConverterException;

import com.google.common.reflect.TypeToken;

/**
 * TODO javadoc.
 *
 * <p><strong>Warning:</strong> The implementation is not thread safe. Do not modify the context until it is being used.</p>
 *
 * @author Barnabas Sudy (barnabas.sudy@gmail.com)
 * @since 2012
 */
public class ConverterContext {

    private final List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();

    /**
     * <p>This class is a small data structure storing a from and to type (of a converter)</p>
     * 
     * Package private for tests.
     */
    static class ConverterTypes {

        /* CHECKSTYLE:OFF because this is a Data Structure Object without any logic and it will never have any setter nor complex getter. */
        /** The input type of the converter. */
        public final Type fromType;
        /** The output type of the converter. */
        public final Type toType;
        /* CHECKSTYLE:ON */

        /**
         * @param fromType The input type of the converter.
         * @param toType The output type of the converter.
         */
        public ConverterTypes(final Type fromType, final Type toType) {
            this.fromType = fromType;
            this.toType   = toType;
        }

    }

    /**
     * Determines the input(from) and output(to) types of a converter in a {@link ConverterTypes} structure. 
     *
     * <p>Package private for tests.</p>
     *
     * @param converter The converter
     * @return The types of the converter wrapped in a {@link ConverterTypes} object.
     */
    ConverterTypes getConverterTypes(final Converter<?, ?> converter) {
        final Type[] interfaces = converter.getClass().getGenericInterfaces();
        for (final Type iface : interfaces) {
            if (TypeToken.of(Converter.class).isAssignableFrom(TypeToken.of(iface))) {
                /* The type of the Converter interface is ParameterizedType -> no need to check the cast*/
                final Type[] generics = ((ParameterizedType) iface).getActualTypeArguments();
                return new ConverterTypes(generics[0], generics[1]);
            }
        }
        /* This error should never occur. */
        throw new IllegalStateException("The converter doesn't have <F> from and <T> to generic types.");
    }

    /**
     * Converts a value (<tt>from</tt>) to a given type by the converters in the context. If the conversion is not possible the method will throw a {@link ConverterException}. 
     * 
     * @param <F> The type of the <em>input</em> object
     * @param <T> The type of the <em>output</em> object
     * 
     * @param from The input object to be converted. (NonNull)
     * @param toType The type of output object. (NonNull)
     * @return The result of the conversion. (NonNull)
     * 
     * @throws ClassCastException If the <tt>toType</tt> can't be assigned to <tt>&lt;T&gt;</tt>.
     * @throws ConverterException If an error occurs during the conversion or the conversion is not possible. 
     */
    <F, T> T convert(final F from, final Type toType) {
        
        for (@SuppressWarnings("rawtypes") final Converter converter : converters) {

            final ConverterTypes converterTypes = getConverterTypes(converter);

            if (TypeToken.of(from.getClass()).isAssignableFrom(converterTypes.fromType)) {
                if (TypeToken.of(toType).isAssignableFrom(converterTypes.toType)) {
                    /* 
                     * There is no need to check the cast because the converter will provide "toType" object.
                     */
                    @SuppressWarnings("unchecked")
                    final T result = (T) converter.convert(from);
                    return result;
                }
            }

        }
        throw new ConverterException("Not supported");


    }

    /**
     * <p>Adds (registers) a {@link Converter} into the {@link ConverterContext}. A registered converter will be used in the 
     * converter algorithm what tries to find conversion between arbitrary types.</p>
     * <p><strong>Warning:</strong> The implementation is not thread safe. Do not modify the context until it is being used.</p>
     *
     * @param converter The converter to be added to the context.
     */
    public <F, T> void add(final Converter<F, T> converter) {
        converters.add(converter);
    }

    /**
     * <p>Removes (unregisters) a {@link Converter} from the {@link ConverterContext}. The removed converter is no longer used in the
     * conversion algorithm.</p>
     * <p>To find the removable convert the {@link Object#equals(Object)} method is used.</p>
     * <p><strong>Warning:</strong> The implementation is not thread safe. Do not modify the context until it is being used.</p>
     *
     * @param converter The converter to be removed.
     */
    public <F, T> boolean remove(final Converter<F, T> converter) {
        return converters.remove(converter);
    }
    
}
