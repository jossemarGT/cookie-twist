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
package com.jossemargt.cookietwist.tornado.transform;

import com.jossemargt.cookietwist.exception.InvalidFormatException;
import com.jossemargt.cookietwist.tornado.TornadoCookieValue;

/**
 * A Tornado TornadoCookieValueDeserializer object transforms the Tornado secure
 * cookie value String into a {@link TornadoCookieValue}.
 *
 * <p>
 * Due some Tornado secure value string format rules, the de-serialization
 * process could throw a {@link InvalidFormatException}. For example in the
 * <code>"2|1:0|10:1521518443|3:foo|4:YmFy|the_signature"</code> secure cookie
 * value String, each field length must match with the leading number.
 *
 * <p>
 * <strong>Note:</strong> The cookie value signature integrity should checked by
 * another object before transforming it into a {@link TornadoCookieValue}.
 */
public interface TornadoCookieValueDeserializer {

    /**
     * Transforms a Tornado secure cookie value String into a
     * {@link TornadoCookieValue}.
     *
     * @param value
     *            the Tornado secure cookie value String
     * @return the cookie value model
     * @throws InvalidFormatException
     *             if the secure cookie value String does not comply with a format
     *             rule.
     */
    TornadoCookieValue deserialize(String value);

}
