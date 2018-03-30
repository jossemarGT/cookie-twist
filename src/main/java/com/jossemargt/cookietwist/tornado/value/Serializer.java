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
package com.jossemargt.cookietwist.tornado.value;

import com.jossemargt.cookietwist.tornado.CookieModel;

/**
 * A Tornado Serializer object handles the {@link CookieModel} transformation
 * process from a plain Java object into its String representation as a Tornado
 * secure cookie value.
 *
 * <p>
 * Based on the required version the Tornado Serializer should apply different
 * formats, as for example a Cookie with name <em>foo</em>, content <em>bar</em>
 * issued with timestamp <em>1521518443</em> could have the following Tornado
 * representations:
 * </p>
 *
 * <ul>
 *  <li><code>"bar|1521518443|the_signature"</code> for version 1</li>
 *  <li><code>"2|1:0|10:1521518443|3:foo|4:YmFy|the_signature"</code> for
 *  version 2</li>
 * </ul>
 *
 * <p>
 * <strong>Note:</strong> The cookie value signature should be calculated by a
 * tornado.Hasher
 * </p>
 */
public interface Serializer {

    /**
     * Transforms a {@link CookieModel} into a Tornado secure cookie value
     * String.
     *
     * @param model the Tornado Cookie object representation
     * @return the Tornado secure cookie value String
     */
    String serialize(CookieModel model);

}
