package io.github.raieen.webredirect;

import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

public class WebRedirect {
    // Just redirecting one thing, so don't overcomplicate this.
    private static final String CONFIG_FILE = "web-redirect.txt";

    // Permanent Redirect (See https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)
    private final static int rCode = 308;

    public static void main(String[] args) throws IOException {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            System.out.println(String.format("%s does not exist. Creating it with default values.", CONFIG_FILE));
            file.createNewFile(); // Shouldn't be false, because !file.exists()

            FileWriter fileWriter = new FileWriter(CONFIG_FILE);
            // Default Values
            fileWriter.write("/\n");
            fileWriter.write("80\n");
            fileWriter.write("https://github.com/Raieen\n");
            fileWriter.close();
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String path = bufferedReader.readLine();
            int port = Integer.parseInt(bufferedReader.readLine());
            String redirect = bufferedReader.readLine();

            HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            httpServer.createContext(path, exchange -> {
                exchange.getResponseHeaders().set("Location", redirect);
                exchange.sendResponseHeaders(rCode, -1);
            });

            httpServer.start();
            System.out.println(String.format("Started web redirect from localhost:%d%s to %s", port, path, redirect));
        } catch (NumberFormatException e) {
            // Port MAX_VALUE is USHORT MAX_VALUE. In practice, 0 (and handful) are used/reserved
            System.out.println("Port is not a number, must be in [0, " + (Short.MAX_VALUE * 2 + 1) + "]");
        }
    }
}
