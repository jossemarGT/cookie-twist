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
package com.jossemargt.cookietwist.tornado.transform.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jossemargt.cookietwist.tornado.TornadoCookieValue;
import com.jossemargt.cookietwist.tornado.transform.impl.V1TornadoCookieValueSerializer;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;


@RunWith(JUnitParamsRunner.class)
public class V1TornadoCookieValueSerializerTest {
    private V1TornadoCookieValueSerializer subject;

    @Before
    public void setUp() {
        subject = new V1TornadoCookieValueSerializer();
    }

    @Test
    @Parameters
    public void testSerialize(String value, String timestamp, String signature, String expectation) {
        TornadoCookieValue model = TornadoCookieValue.builder()
                .withValue(value)
                .withSignature(signature)
                .withTimestamp(Long.parseLong(timestamp))
                .build();
        assertEquals(expectation, subject.serialize(model));
    }

    @SuppressWarnings("unused")
    private Object parametersForTestSerialize() {
        return new Object[] {
                new Object[] {
                        "value","1521518443","the_signature","value|1521518443|the_signature"
                        },
                new Object[] {
                        null,"1521518443","the_signature","|1521518443|the_signature"
                        },
                new Object[] {
                        "value","1521518443", null,"value|1521518443"
                        },
                new Object[] {
                        "trick4|Str!n€","1521518443","the_signature","trick4|Str!n€|1521518443|the_signature"
                        }
        };
    }

}
