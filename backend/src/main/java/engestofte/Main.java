package engestofte;

import engestofte.server.Server;

public class Main {

    // Attributes
    private static final Server server = new Server();

    // _________________________________________________________________________________________________________________

    public static void main(String[] args) {

        // Server start
        server.start();

    }

}