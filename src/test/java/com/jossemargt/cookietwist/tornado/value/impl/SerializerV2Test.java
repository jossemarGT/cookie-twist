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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jossemargt.cookietwist.tornado.CookieModel;
import com.jossemargt.cookietwist.tornado.value.impl.SerializerV2;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class SerializerV2Test {
    private SerializerV2 subject;

    @Before
    public void setUp() {
        subject = new SerializerV2();
    }


    @Test
    @Parameters
    public void testSerialize(String name, String value, String timestamp, String signature, Integer keyVersion, String expectation) {
        CookieModel model = CookieModel.builder()
                .withName(name)
                .withValue(value)
                .withSignature(signature)
                .withSignatureKeyVersion(keyVersion)
                .withTimestamp(Long.parseLong(timestamp))
                .build();
        assertEquals(expectation, subject.serialize(model));
    }

    @SuppressWarnings("unused")
    private Object parametersForTestSerialize() {
        return new Object[] {
                new Object[] {
                        "one", "simple value","1521518443","the_signature", 0 ,"2|1:0|10:1521518443|3:one|16:c2ltcGxlIHZhbHVl|the_signature"
                        },
                new Object[] {
                        "two", "","1521518443","the_signature", 1, "2|1:1|10:1521518443|3:two|0:|the_signature"
                        },
                new Object[] {
                        "three", null, "1521518443", "the_signature", 2, "2|1:2|10:1521518443|5:three|0:|the_signature"
                        },
                new Object[] {
                        "four", "'|/unw@nt€d ch@r$?","1521518443","the_signature", 3 ,"2|1:3|10:1521518443|4:four|28:J3wvdW53QG504oKsZCBjaEByJD8=|the_signature"
                        },
                new Object[] {
                        "five", null, "1521518443", null, 4, "2|1:4|10:1521518443|4:five|0:"
                },
                new Object[] {
                        "six", "", "1521518443", "", 5, "2|1:5|10:1521518443|3:six|0:"
                }
        };
    }
}
