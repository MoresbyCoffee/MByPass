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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.moresbycoffee.pass.ConverterContext.ConverterTypes;
import org.moresbycoffee.pass.api.Converter;
import org.moresbycoffee.pass.api.Converters;

/**
 * TODO javadoc.
 *
 * @author Barnabas Sudy (barnabas.sudy@gmail.com)
 * @since 2012
 */
public class ConverterContextTest {

    private ConverterContext converterContext;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        converterContext = new ConverterContext();
    }

    @Test
    public void addConverter() {
        converterContext.add(Converters.STRING_TO_INTEGER);
    }

    @Test
    public void removeAddedConverterShouldReturnTrue() {
        converterContext.add(Converters.STRING_TO_INTEGER);
        assertTrue(converterContext.remove(Converters.STRING_TO_INTEGER));
    }

    @Test
    public void removeNotAddedConverterShouldReturnFalse() {
        assertFalse(converterContext.remove(Converters.STRING_TO_INTEGER));
    }

    @Test
    public void getConverterTypesShouldReturnCorrectTypes() {

        final Converter<String, Integer> converter = Converters.STRING_TO_INTEGER;

        final ConverterTypes result = converterContext.getConverterTypes(converter);

        assertEquals(String.class,  result.fromType);
        assertEquals(Integer.class, result.toType);

    }

    @Test
    public void simpleConvertTest() {

        /* Preparation */
        final String  from     = "4";
        final Integer expected = 4;

        /* Test */
        converterContext.add(Converters.STRING_TO_INTEGER);

        /* Assertion / Verification */
        assertEquals(expected, converterContext.<String, Integer>convert(from, Integer.class));
    }

}
