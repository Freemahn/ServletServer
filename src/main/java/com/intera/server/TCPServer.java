package com.intera.server;

/**
 * Created by pgordon on 22.06.2017.
 */


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.LimitExceededException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class is an entry point of application.
 * Handles connection with clients and registers them in ThreadManager.
 */

class TCPServer implements Runnable {
    private static final Logger logger = LogManager.getLogger();
    private static final int PORT = 6789;


    public static void main(String argv[]) {
        new TCPServer().run();
    }

    @Override
    public void run() {
        try {
            ServerSocket welcomeSocket = new ServerSocket(PORT);
            logger.info("Server started");
            while (true) {
                Socket connectionSocket = welcomeSocket.accept();
                logger.info("Accepted connection from socket " + connectionSocket.getInetAddress() + ":" + connectionSocket.getPort());

                ClientThread clientThread = new ClientThread(connectionSocket);
                try {
                    ThreadManager.getInstance().registerThread(clientThread);
                    clientThread.start();
                } catch (LimitExceededException e) {
                    logger.info("Too many clients");
                    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                    outToClient.write("Exceeded user limit\r\n".getBytes());
                    outToClient.flush();
                    connectionSocket.close();
                }
            }
        } catch (IOException e) {
            logger.error("Connection problems", e);
        }

    }
}
