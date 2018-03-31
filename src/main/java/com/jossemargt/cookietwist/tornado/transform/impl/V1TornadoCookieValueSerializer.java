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

import com.jossemargt.cookietwist.tornado.TornadoCookieValue;
import com.jossemargt.cookietwist.tornado.transform.TornadoCookieValueSerializer;

/**
 * The Class V1TornadoCookieValueSerializer transforms a {@link TornadoCookieValue} object into its
 * Tornado secure cookie value string representation with the format version 1.
 */
public class V1TornadoCookieValueSerializer implements TornadoCookieValueSerializer {

    /* (non-Javadoc)
     * @see com.jossemargt.cookietwist.tornado.transform.TornadoCookieValueSerializer#serialize
     */
    @Override
    public String serialize(TornadoCookieValue model) {
        String value = "";

        if (model.getValue() != null) {
            value = model.getValue();
        }

        String result = String.format("%s|%d", value, model.getTimestamp());

        if (model.getSignature() != null && !model.getSignature().isEmpty()) {
            result = result + "|" + model.getSignature();
        }

        return result;
    }

}
