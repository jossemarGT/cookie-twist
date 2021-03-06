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

import com.jossemargt.cookietwist.tornado.transform.TornadoCookieCodec.Builder;
import com.jossemargt.cookietwist.tornado.transform.impl.V1TornadoCookieCodec;
import com.jossemargt.cookietwist.tornado.transform.impl.V2TornadoCookieCodec;

/**
 * The utility Class CookiePot facilitates any supported Cookie codec creation.
 *
 * @author Jonnatan Jossemar Cordero
 */
public final class CookiePot {

    /**
     * Instantiates a new cookie pot.
     */
    private CookiePot() {
        // Hides this utility class constructor
    }

    /**
     * Gets the CookieCodec builder for the given Signature Algorithm type.
     *
     * @param supportedSignature
     *            the enum that represents the desired secure cookie signature
     *            algorithm to use
     * @return the builder based on the given CookieSignatureAlgorithm
     */
    public static Builder<?> getBuilderFor(CookieSignatureAlgorithm supportedSignature) {
        switch (supportedSignature) {
        case TORNADO_V1:
            return V1TornadoCookieCodec.builder();
        default:
        case TORNADO_V2:
            return V2TornadoCookieCodec.builder();
        }
    }

}
