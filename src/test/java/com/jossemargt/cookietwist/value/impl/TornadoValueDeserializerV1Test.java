/*
 * The MIT License
 * 
 * Copyright (C) 2018 Jonnatan Jossemar Cordero
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.jossemargt.cookietwist.value.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jossemargt.cookietwist.exception.InvalidFormatException;
import com.jossemargt.cookietwist.value.CookieValueModel;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class TornadoValueDeserializerV1Test {
    private TornadoValueDeserializerV1 subject;

    @Before
    public void setUp() {
        subject = new TornadoValueDeserializerV1();
    }

    @Test
    @Parameters
    public void testDeserialize(String serializedValue, String expectedValue, String expectedTimestamp,
            String expectedSignature) throws InvalidFormatException {
        CookieValueModel model = subject.deserialize(serializedValue);

        assertEquals(expectedValue, model.getValue());
        assertEquals(Long.parseLong(expectedTimestamp), model.getTimestamp());
        assertEquals(expectedSignature, model.getSignature());
    }

    @SuppressWarnings("unused")
    private Object parametersForTestDeserialize() {
        return new Object[] {
                new Object[] { "value|1521518443|the_signature", "value", "1521518443", "the_signature" },
                new Object[] { "|1521518443|the_signature", "", "1521518443", "the_signature" },
                new Object[] { "trick4|Str!n€|1521518443|the_signature", "trick4|Str!n€", "1521518443", "the_signature" },
                new Object[] { "tricky|String|the|revenge|1521518443|the_signature", "tricky|String|the|revenge", "1521518443", "the_signature" }
        };
    }

    @Test
    @Parameters
    public void testDeserializeInvalidFormatException(String serializedValue) {
        try {
            subject.deserialize(serializedValue);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertTrue("Expected InvalidFormatException", e instanceof InvalidFormatException);
        }
    }

    @SuppressWarnings("unused")
    private Object parametersForTestDeserializeInvalidFormatException() {
        return new Object[] {
                new Object[] { "value|1521518443" },
                new Object[] { "value|1521518443|" },
                new Object[] { "value||the_signature" },
                new Object[] { "value" }
        };
    }
}
