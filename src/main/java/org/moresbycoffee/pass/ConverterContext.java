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
 * @author Barnabas Sudy (barnabas.sudy@gmail.com)
 * @since 2012
 */
public class ConverterContext {

    private final List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();


    /**
     * TODO javadoc.
     *
     * Package private for tests.
     *
     * @param converter
     * @return
     */
    Type[] getConverterTypes(final Converter<?, ?> converter) {
        final Type[] interfaces = converter.getClass().getGenericInterfaces();
        for (final Type iface : interfaces) {
            if (TypeToken.of(Converter.class).isAssignableFrom(TypeToken.of(iface)) && iface instanceof ParameterizedType) {
                return ((ParameterizedType) iface).getActualTypeArguments();
            }
        }
        throw new IllegalStateException("TODO comment");
    }

    @SuppressWarnings("unchecked")
    <F, T> T convert(final F from, final Type toClass) {
        for (@SuppressWarnings("rawtypes") final Converter converter : converters) {

            final Type[] genericTypes = getConverterTypes(converter);

            if (TypeToken.of(from.getClass()).isAssignableFrom(genericTypes[0])) {
                if (TypeToken.of(toClass).isAssignableFrom(genericTypes[1])) {
                    return (T) converter.convert(from);
                }
            }

        }
        throw new ConverterException("Not supported");


    }

    /**
     * TODO javadoc.
     *
     * @param from
     * @return
     * @throws ConverterException
     */
    public <F, T> T convert(final F from) throws ConverterException {

        for (final Converter converter : converters) {

            final Type[] interfaces = converter.getClass().getGenericInterfaces();
            for (final Type iface : interfaces) {
                if (TypeToken.of(Converter.class).isAssignableFrom(TypeToken.of(iface))) {
                    if (iface instanceof ParameterizedType) {
                        final Type[] genericTypes = ((ParameterizedType) iface).getActualTypeArguments();
                        for (final Type genericType : genericTypes) {
                            System.out.println("genericsTypes: " + genericType);
                        }
                    }
                }
            }




        }

        throw new UnsupportedOperationException();
    }

    /**
     * TODO javadoc.
     *
     * @param converter
     */
    public <F, T> void add(final Converter<F, T> converter) {
//        final Type genericSuperclass = converter.getClass().getGenericSuperclass();
//        if (genericSuperclass instanceof ParameterizedType) {
//            System.out.println("-----------");
//        }
//        System.out.println("GenericSuperClass: " + genericSuperclass);
//        System.out.println("RawType: " + TypeToken.of(converter.getClass()).getRawType());
//
//        final Type[] interfaces = converter.getClass().getGenericInterfaces();
//        for (final Type iface : interfaces) {
//            System.out.println("Type: " + iface);
//            if (iface instanceof ParameterizedType) {
//                System.out.println("-----------");
//                final Type[] genericTypes = ((ParameterizedType) iface).getActualTypeArguments();
//                for (final Type genericType : genericTypes) {
//                    System.out.println("genericsTypes: " + genericType);
//                }
//            }
//        }





        converters.add(converter);
    }

    /**
     * TODO javadoc.
     *
     * @param converter
     */
    public <F, T> boolean remove(final Converter<F, T> converter) {
        return converters.remove(converter);
    }
}
