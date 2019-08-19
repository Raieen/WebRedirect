package io.github.raieen.webredirect;

import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Web Redirection Server
 * Simple web server using HttpServer.
 * Give it some urls and it will redirect someone, somewhere!
 */
public class WebRedirect {

    // Permanent Redirect (See https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)
    private final static int PERMANENT_REDIRECT = 308;

    public static void main(String[] args) {
        // Arg check
        if (args.length == 0) {
            System.err.println("Usage: java -jar webredirect.jar config.txt");
            return;
        }

        String configFile = args[0];

        File file = new File(configFile);
        try {
            if (!file.exists()) {
                System.err.println(String.format("%s does not exist, creating it with default values.", configFile));
                if (!file.createNewFile()) {
                    System.err.println(String.format("Error writing to %s", configFile));
                    return;
                }

                // Default Values
                FileWriter fileWriter = new FileWriter(configFile);
                fileWriter.write(String.format("%s\n%d\n%s\n", "/test", (short) 8080, "https://github.com/Raieen"));
                fileWriter.write(String.format("%s\n%d\n%s\n", "/test2", (short) 8080, "https://google.ca"));
                fileWriter.close();
            }
            if (!file.canRead()) {
                System.err.println(String.format("Cannot read file %s.", configFile));
                return;
            }

            // Map [PORT, Server]
            Map<Short, HttpServer> httpServerMap = new HashMap<>();

            // Read config file and create a context for each server with redirection
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Path
                String path = line;

                // Port
                short port;
                try {
                    port = Short.parseShort(bufferedReader.readLine());
                    if (port < 0) throw new NumberFormatException();
                } catch (NumberFormatException | IOException e) {
                    // Port MAX_VALUE is USHORT MAX_VALUE. In practice, 0 (and others) are used/reserved
                    System.err.println(String.format("Invalid port number. Port must be a number in [0, %d]", Short.MAX_VALUE * 2 + 1));
                    return;
                }

                // Redirect
                String redirect = bufferedReader.readLine();
                if (redirect == null) {
                    System.err.println("Missing a redirection URL!");
                    return;
                }

                HttpServer server;
                if (httpServerMap.containsKey(port)) {
                    server = httpServerMap.get(port);
                } else {
                    server = HttpServer.create(new InetSocketAddress(port), 0); // Use system default
                    httpServerMap.put(port, server);
                }

                // Redirect context
                server.createContext(path, exchange -> {
                    exchange.getResponseHeaders().set("Location", redirect);
                    exchange.sendResponseHeaders(PERMANENT_REDIRECT, -1);
                });

                System.out.println(String.format("Read web redirect from file, http://localhost:%d%s to %s", port, path, redirect));
            }
            for (HttpServer server : httpServerMap.values()) {
                server.start();
                System.out.println(String.format("Started server at http://localhost:%d", server.getAddress().getPort()));
            }
        } catch (IOException e) {
            System.err.println(String.format("Something went wrong: %s", e.getMessage()));
        }
    }
}
