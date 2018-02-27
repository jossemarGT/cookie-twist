# Cookie Twist

> Did you hear about the secure cookie handling in Tornado web framework?
> There is a twist at the end! ＞▽＜

[Tornado web framework](https://github.com/tornadoweb/tornado) even from early
releases goes beyond setting cookies' [secure attribute flag](https://tools.ietf.org/html/rfc6265.html#section-5.2.5)
by [symmetric signing the cookie value](http://www.tornadoweb.org/en/stable/guide/security.html)
in order to prevent forgery. `cookie-twist` library aims to integrate this
behavior in Java using plain old [javax.servlet.http.Cookie](https://docs.oracle.com/javaee/6/api/javax/servlet/http/Cookie.html)
object.
