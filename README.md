# Web Redirect

A small web redirect server with a simple config file that redirects a path to anything.

This makes use of things only in Java SE 11, mostly [HttpServer](https://docs.oracle.com/en/java/javase/11/docs/api/jdk.httpserver/com/sun/net/httpserver/HttpServer.html).

## Usage

`java -jar webredirect.jar`

### Config File

The config file, `web-redirect.txt` is in this format.
```
path (ie. /)
port (ie. 80)
redirect url (ie. https://github.com/Raieen)
```