import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleHttpServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3000);
        System.out.println("Listening for connection on port 3000 ....");

        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                String line = reader.readLine();

                if (line != null) {
                    String[] requestTokens = line.split(" ");
                    String path = requestTokens[1]; // Assuming the request line is correctly formed

                    if (path.equals("/")) {
                        serveFile(clientSocket, "index.html");
                    } else if (path.equals("/digital-signature")) {
                        serveFile(clientSocket, "digital-signature.html");
                    } else if (path.equals("/memory-game")) {
                        serveFile(clientSocket, "memory-game.html");
                    } else if (path.equals("/calculator")) {
                        serveFile(clientSocket, "calculator.html");
                    } else {
                        // no custom page
                        // sendNotFound(clientSocket);

                        // custom page for error
                        serveFile(clientSocket, "error-page.html");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void serveFile(Socket clientSocket, String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            OutputStream clientOutput = clientSocket.getOutputStream();
            clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
            clientOutput.write("Content-Type: text/html\r\n".getBytes());
            clientOutput.write("\r\n".getBytes());
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            clientOutput.write(data);
            clientOutput.flush();
        } else {
            // sendNotFound(clientSocket);

            // custom error page
            serveFile(clientSocket, "error-page.html");

        }
        clientSocket.close();
    }

    // private static void sendNotFound(Socket clientSocket) throws IOException {
    // OutputStream clientOutput = clientSocket.getOutputStream();
    // clientOutput.write("HTTP/1.1 404 Not Found\r\n".getBytes());
    // clientOutput.write("Content-Type: text/html\r\n".getBytes());
    // clientOutput.write("\r\n".getBytes());
    // clientOutput.write("<h1>404 Not Found</h1><p>The requested resource was not
    // found on this server.</p>".getBytes());
    // clientOutput.flush();
    // clientSocket.close();
    // }
}
