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

/**
 * Contains the interfaces and their implementations for Tornado secure cookie
 * value serialization and de-serialization.
 *
 * <p>
 * The serialization pattern to follow is based on the required Tornado secure
 * cookie value format version. As for example a Cookie with name <em>foo</em>,
 * content <em>bar</em> issued with timestamp <em>1521518443</em> could have the
 * following Tornado representations:
 *
 * <ul>
 *  <li><code>"bar|1521518443|the_signature"</code> for version 1
 *  <li><code>"2|1:0|10:1521518443|3:foo|4:YmFy|the_signature"</code> for
 *  version 2
 * </ul>
 *
 * <p>
 * The signature determination is done by the classes defined in the
 * {@code com.jossemargt.cookietwist.tornado.signature} package.
 */
package com.jossemargt.cookietwist.tornado.transform;
