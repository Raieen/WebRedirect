# Web Redirect

A small web redirect server with a simple config that redirects paths to anything. A very easy way to redirect your domain to other things!

This makes use of things only in Java SE, mostly [HttpServer](https://docs.oracle.com/en/java/javase/11/docs/api/jdk.httpserver/com/sun/net/httpserver/HttpServer.html).

You can specify different ports for different paths.

**Note:** standard rules apply, you must have elevated permissions to run things on system ports (Like the first 1024 ports).  

## Usage

`java -jar webredirect.jar configFile`

### Config File

The config file specifies any number of redirects.

Here's an example config file.

```
/google/
80
https://google.ca
/bing/
80
https://bing.ca
/google/
81
https://google.ca
/businesscat
80
https://www.youtube.com/watch?v=qNhycX0XCJ0
/redirect/
80
https://github.com/Raieen/WebRedirect
```

The general format of a redirect:

```
path (ie. /)
port (ie. 80)
redirect url (ie. https://github.com/Raieen)
```