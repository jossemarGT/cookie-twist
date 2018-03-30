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
package com.jossemargt.cookietwist.tornado.value.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jossemargt.cookietwist.exception.InvalidFormatException;
import com.jossemargt.cookietwist.tornado.CookieModel;
import com.jossemargt.cookietwist.tornado.value.impl.DeserializerV2;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class DeserializerV2Test {
    private DeserializerV2 subject;

    @Before
    public void setUp() throws Exception {
        subject = new DeserializerV2();
    }

    @Test
    @Parameters
    public void testDeserialize(String serializedValue, String expectedName, String expectedValue,
            String expectedTimestamp, int expectedKeyVersion, String expectedSignature) throws InvalidFormatException {

        CookieModel model = subject.deserialize(serializedValue);

        assertEquals(expectedName, model.getName());
        assertEquals(expectedValue, model.getValue());
        assertEquals(Long.parseLong(expectedTimestamp), model.getTimestamp());
        assertTrue("Unexpected signature key version", expectedKeyVersion == model.getSignatureKeyVersion());
        assertEquals(expectedSignature, model.getSignature());
    }

    @SuppressWarnings("unused")
    private Object parametersForTestDeserialize() {
        return new Object[] {
                new Object[] {
                        "2|1:0|10:1521518443|3:one|16:c2ltcGxlIHZhbHVl|the_signature","one","simple value","1521518443", 0, "the_signature"
                },
                new Object[] {
                        "2|1:1|10:1521518443|3:two|0:|the_signature","two","","1521518443",1,"the_signature"
                },
                new Object[] {
                        "2|1:2|10:1521518443|5:three|28:J3wvdW53QG504oKsZCBjaEByJD8=|the_signature","three", "'|/unw@nt€d ch@r$?","1521518443",2,"the_signature"
                }
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
        return new Object[]{
                new Object[] {
                        "2|1:0|10:1521518443|4:zero|8:c2hvcnQ="
                },
                new Object[] {
                        "2|1:1|10:1521518443|3:one|0:|"
                },
                new Object[] {
                        "2|1:2|10:1521518443|3:two|3:c2hvcnQ=|the_signature"
                },
                new Object[]{
                        "2|1:3|10:1521518443|3:three|8:c2hvcnQ=|the_signature"
                },
                new Object[]{
                        "3|1:4|10:1521518443|4:four|8:c2hvcnQ=|the_signature"
                },
                new Object[]{
                        "2|1:5|10:abcdefghij|4:five|8:c2hvcnQ=|the_signature"
                },
                new Object[]{
                        "2|1:6|10:1521518443|a:six|8:c2hvcnQ=|the_signature"
                },
        };
    }
}
