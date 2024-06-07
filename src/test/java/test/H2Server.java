package test;

import org.h2.tools.Server;

public class H2Server {

    static String host = "127.0.0.1";

    static String webPort = "3331";

    static String tcpPort = "3330";

    public static void main(String[] args) throws Exception {
        //String[] params = new String[]{"-tcpPort", port, "-tcp", "-tcpAllowOthers", "-web", "-webAllowOthers", "-webPort", "3331", "-browser"};
        //Server server = Server.createTcpServer(params);
        //server.start();
        start();
    }
    
    public static void start() throws Exception {
        String[] params = new String[]{"-web", "-webAllowOthers", "-webPort", webPort, "-browser", "-tcp", "-tcpAllowOthers", "-tcpPort", tcpPort};
        Server.main(params);
    }
}
