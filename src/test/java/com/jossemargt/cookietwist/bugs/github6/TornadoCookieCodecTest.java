package com.jossemargt.cookietwist.bugs.github6;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.security.InvalidKeyException;
import java.time.Instant;

import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.jossemargt.cookietwist.tornado.transform.TornadoCookieCodec;
import com.jossemargt.cookietwist.tornado.transform.impl.V1TornadoCookieCodec;
import com.jossemargt.cookietwist.tornado.transform.impl.V2TornadoCookieCodec;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "javax.crypto.*" })
@PrepareForTest(TornadoCookieCodec.class)
public class TornadoCookieCodecTest {

    private static String secretKey = "not-so-secret";
    private static long frozenTimestamp = 1521518443;
    private static Instant frozenInstant;
    private static TornadoCookieCodec v1Subject;
    private static TornadoCookieCodec v2Subject;

    @BeforeClass
    public static void setUpAll() throws InvalidKeyException {
        frozenInstant = Instant.ofEpochSecond(frozenTimestamp);
        v1Subject = V1TornadoCookieCodec.builder().withSecretKey(secretKey).build();
        v2Subject = V2TornadoCookieCodec.builder().withSecretKey(secretKey).build();
    }

    @Before
    public void setUp() {
        mockStatic(Instant.class);
        when(Instant.now()).thenReturn(frozenInstant);
    }

    @Test
    public void testV1TornadoCookieCodecTimestamp() throws InvalidKeyException {
        String expectedV1TornadoCookieValue = "value|1521518443|8d2c562f3831063fbc70cd0b35da54aa4e4e730e";

        Cookie signedCookie = v1Subject.encodeCookie(new Cookie("name", "value"));

        assertEquals(expectedV1TornadoCookieValue, signedCookie.getValue());
    }

    @Test
    public void testV2TornadoCookieCodecTimestamp() throws InvalidKeyException {
        String expectedV2TornadoCookieValue = "2|1:0|10:1521518443|4:name|8:dmFsdWU=|80d021ad66d8ff3187d95a9203890d7c7bf7da0b995c54671203729b6f467961";

        Cookie signedCookie = v2Subject.encodeCookie(new Cookie("name", "value"));

        assertEquals(expectedV2TornadoCookieValue, signedCookie.getValue());
    }
}
