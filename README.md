# Cookie Twist

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
    <dependency>
        <groupId>com.jossemargt</groupId>
        <artifactId>cookie-twist</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

Using [gradle](https://gradle.org/) build tool:

```groovy
// In the build.gradle file
dependencies {
    // ... Other dependencies
    compile 'com.jossemargt:cookie-twist:0.1.0'
}
```

## Usage

The simplified example:

```java
import javax.servlet.http.Cookie;
import com.jossemargt.cookietwist.CookiePot;

public static void main(String[] args){
    final String mySecretKey = 'like a ninja!';

    // Instantiate your Cookie Pot with your secret key
    CookiePot tornadoPot = new CookiePot(mySecretKey);

    // Generate a Tornado Signed Secure Cookie (with signing V2 by default);
    Cookie secureCookie = tornadoPot.encodeCookie(new Cookie('the_name', 'a value'));
    String hashedValue = secureCookie.getValue();

    // Generate a plain/flat cookie from a  hashed/signed one
    Cookie flatCookie = tornadoPot.decodeCookie(secureCookie);
    String flatValue = flatCookie.getValue();
}

```

## FAQ

**Why should I use this library?** I created `cookie-twist` with the sole goal
of integrating a Java application with a Python one (that happens to use Tornado
web framework) in the same distributed ecosystem without making use of some
heavy solution like [Jython](http://www.jython.org/) or so. If you stumble with
a similar scenario then go ahead, otherwise I suggest you to stick with the main
stream `¯\_(ツ)_/¯`

**How about using the word `jar` instead of `pot`?** I thought about it for
while, but something that I have learned is how important is to *not confuse the
user*, since the term `jar` is widely used to reference a Java artifact I
deliberately picked `pot` instead.
