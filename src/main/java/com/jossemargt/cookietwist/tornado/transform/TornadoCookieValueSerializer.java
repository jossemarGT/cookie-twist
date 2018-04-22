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

import com.jossemargt.cookietwist.tornado.TornadoCookieValue;

/**
 * A Tornado TornadoCookieValueSerializer object transforms any {@link TornadoCookieValue} instance into
 * its Tornado secure cookie value string representation.
 *
 * <p>
 * <strong>Note:</strong> The cookie value signature should be calculated by
 * another object.
 */
public interface TornadoCookieValueSerializer {

    /**
     * Transforms a {@link TornadoCookieValue} into a Tornado secure cookie value
     * String.
     *
     * @param model the Tornado Cookie object representation
     * @return the Tornado secure cookie value String
     */
    String serialize(TornadoCookieValue model);

}
