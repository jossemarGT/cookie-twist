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
package com.jossemargt.cookietwist;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.InvalidKeyException;

import org.junit.Test;

import com.jossemargt.cookietwist.tornado.transform.TornadoCookieCodec;
import com.jossemargt.cookietwist.tornado.transform.impl.V1TornadoCookieCodec;
import com.jossemargt.cookietwist.tornado.transform.impl.V2TornadoCookieCodec;

public class CookiePotTest {

    private String secretKey = "not-so-secret";

    @Test
    public void getBuilderFor() throws InvalidKeyException {
        TornadoCookieCodec tcc;

        for (CookieSignatureAlgorithm algorithm : CookieSignatureAlgorithm.values()) {
            switch (algorithm) {
            case TORNADO_V1:
                tcc = CookiePot.getBuilderFor(algorithm).withSecretKey(secretKey).build();
                assertTrue(tcc instanceof V1TornadoCookieCodec);
                break;
            case TORNADO_V2:
                tcc = CookiePot.getBuilderFor(algorithm).withSecretKey(secretKey).build();
                assertTrue(tcc instanceof V2TornadoCookieCodec);
                break;
            default:
                fail(String.format("Unexpected signature algorithm: %s", algorithm));
            }
        }
    }
}
