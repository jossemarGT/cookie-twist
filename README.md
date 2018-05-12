# Cookie Twist

[![Build Status](https://travis-ci.org/jossemarGT/cookie-twist.svg?branch=master)](https://travis-ci.org/jossemarGT/cookie-twist)

> Did you hear about the secure cookie handling in Tornado web framework?
> There is a twist at the end! ＞▽＜

The [Tornado web framework](https://github.com/tornadoweb/tornado) even from
early releases goes beyond setting the cookies' [secure attribute  flag](https://tools.ietf.org/html/rfc6265.html#section-5.2.5)
by [symmetric signing](http://www.tornadoweb.org/en/stable/guide/security.html#cookies-and-secure-cookies)
its value in order to prevent forgery. `cookie-twist` library aims to
integrate the same behavior in Java by composing a plain old [javax.servlet.http.Cookie](https://docs.oracle.com/javaee/6/api/javax/servlet/http/Cookie.html)
object.

## Install

Using [maven](https://maven.apache.org/) build tool:

```xml
<!-- in the pom.xml -->
<dependencies>
    <!-- ... Other dependencies -->
    <dependency>
        <groupId>com.jossemargt</groupId>
        <artifactId>cookie-twist</artifactId>
        <version>0.4.0</version>
    </dependency>
</dependencies>
```

Using [gradle](https://gradle.org/) build tool:

```groovy
// In the build.gradle file
dependencies {
    // ... Other dependencies
    compile 'com.jossemargt:cookie-twist:0.4.0'
}
```

## Usage

The simplified example:

```java
import javax.servlet.http.Cookie;
import com.jossemargt.cookietwist.CookiePot;
import com.jossemargt.cookietwist.CookieSignatureAlgorithm;
import com.jossemargt.cookietwist.exception.InvalidFormatException;

public static void main(String[] args){
    final String mySecretKey = 'like a ninja!';


    // Build the TornadoCookie encoder/decoder V1 with your secret key
    TornadoCookieCodec tcc1 = CookiePot.getBuilderFor(TORNADO_V1)
                                      .withSecretKey(mySecretKey)
                                      .build();

    // Build the TornadoCookie encoder/decoder V2 with your secret key
    TornadoCookieCodec tcc2 = CookiePot.getBuilderFor(TORNADO_V2)
                                      .withSecretKey(mySecretKey)
                                      .build();

    // Generate a Tornado Signed Secure Cookie V1
    Cookie secureCookie1 = tcc1.encodeCookie(new Cookie('name', 'value'));
    System.out.println(secureCookie1.getValue());
    // output: value|1525627053|a26554d64a0aff4dfbc0652d91a8193584d43caa

    // Generate a Tornado Signed Secure Cookie V2
    Cookie secureCookie2 = tcc2.encodeCookie(new Cookie('name', 'value'));
    System.out.println(secureCookie2.getValue());
    // output: 2|1:0|10:1525627053|4:name|8:dmFsdWU=|4ac4e05c3130c41d821ed97a5af9b065db5be09b8fcf071e70f3ba9a37fe2392

    // Generate a plain/flat cookie from a signed one
    Cookie flatCookie = tcc1.decodeCookie(secureCookie1);
    System.out.println(flatCookie.getValue());
    // output: value

    // What happens if you decode an tampered/invalid Cookie
    // or you are using the wrong signature algorithm?
    try {
        tcc2.decodeCookie(secureCookie1);
    } catch (InvalidFormatException e) {
        System.out.println("I just caught an unchecked exception: " + e.getMessage());
        // output: I just caught an unchecked exception: Invalid field quantity. Expected 6, got 3
        // Because TornadoV1 and TornadoV2 formats are incompatible
    }
}

```

## FAQ

**Why should I use this library?** I created `cookie-twist` with the sole
goal of integrating a Java application with a Python one (that happens to use
Tornado web framework) in the same distributed ecosystem without making use of
some heavy solution like [Jython](http://www.jython.org/) or so. If you stumble
with a similar scenario then go ahead, otherwise I suggest you to stick with the
main stream `¯\_(ツ)_/¯`

**How do I set the signed cookie as a `Set-Cookie` response header?** Well, the
answer is outside of this library's scope, but you could use any servlet
container (like [Tomcat](https://tomcat.apache.org/), [Jetty](https://www.eclipse.org/jetty/),
[Undertow](http://undertow.io/), etc) to add the `Cookie` object to the
response. As for example with Tomcat you could use the
[HttpServletResponse.html#addCookie](https://tomcat.apache.org/tomcat-8.0-doc/servletapi/javax/servlet/http/HttpServletResponse.html#addCookie(javax.servlet.http.Cookie))
method, which will generate the proper response header.

**How about using the word `jar` instead of `pot`?** I thought about it for
while, but something that I have learned is how important is to *not confuse the
user*, since the term `jar` is widely used as a reference to a Java artifact I
deliberately picked `pot` instead.
