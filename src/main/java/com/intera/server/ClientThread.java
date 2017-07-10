package com.intera.server;

import com.intera.server.storage.Record;
import com.intera.server.storage.Storage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.TreeMap;

import static com.intera.server.ClientThreadState.*;

/**
 * This class is a thread that handles all workflow with clients
 * Created by pgordon on 23.06.2017.
 */
public class ClientThread extends Thread {
    private static final Logger logger = LogManager.getLogger();

    private TreeMap<String, String> commands = new TreeMap<>();
    private Socket socket;
    private BufferedReader inFromClient;
    private DataOutputStream outToClient;

    /**
     * Flag for checking if thread should be closed since "exit" command received
     */
    private boolean alive;
    /**
     * Temporary record that may be cancelled and therefore not commited to Storage
     */
    private Record record;

    /**
     * Current state of this thread
     * */
    private ClientThreadState currentState;

    /**
     * Handler for unregistering and deleting from the ThreadManager
     */
    private ThreadManager.UnregisterHandler unregisterHandler;

    ClientThread(Socket connectionSocket) throws IOException {
        logger.info("New client initialized");
        this.socket = connectionSocket;
        this.inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        this.outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        commands.put("help", "print all commands");
        commands.put("exit", "close the connection");
        commands.put("cancel", "interrupt current operation without closing connection");
        commands.put("new", "create new record");
        commands.put("search", "search through records");
        alive = true;
        currentState = WAITING_FOR_COMMAND;
    }


    /**
     * Lifecycle of thread
     * Waits for commands, logs and handles them.
     */
    @Override
    public void run() {
        logger.info("Started working");
        try {
            outToClient.write("Welcome to test server!  Enter help for list of available commands.\r\n".getBytes());
            outToClient.flush();
            String command;
            while ((command = inFromClient.readLine()) != null) {
                logger.info("Got command " + command);
                String response = handleCommand(command);
                outToClient.write((response + "\r\n").getBytes());
                outToClient.flush();
                if (!alive) {
                    socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        unregisterHandler.unregister();
        logger.info("Stopped working");
    }

    /**
     * Tries to determine command via comparing with commands list.
     * If fails, it goes to handleTextByState
     *
     * @param command command to interprete
     * @return a String response that will go to client
     */
    private String handleCommand(String command) {
        switch (command) {
            case "help":
                return buildHelp();
            case "new":
                record = new Record();
                currentState = WAITING_FOR_NAME;
                return "Enter name";
            case "cancel":
                currentState = WAITING_FOR_COMMAND;
                record = null;
                return "Cancelled current operation";
            case "list":
                return new Storage().prettyPrint();
            case "search":
                currentState = WAITING_FOR_SEARCH;
                return "Enter query";
            case "exit": {
                alive = false;
                return "Bye";
            }
            default:
                return handleTextByState(command);
        }
    }

    /**
     * Tries to determine text via comparing with states.
     * If fails, goes to handleTextByState
     *
     * @param command command to interprete
     * @return a String response that will go to client
     * @see ClientThreadState
     */
    private String handleTextByState(String command) {
        switch (currentState) {
            case WAITING_FOR_NAME:
                if (Record.isValidName(command)) {
                    record.setName(command);
                    currentState = WAITING_FOR_SURNAME;
                    return "Enter surname";
                } else {
                    return "Validation failed. Enter valid name";
                }
            case WAITING_FOR_SURNAME:
                if (Record.isValidSurname(command)) {
                    record.setSurname(command);
                    currentState = WAITING_FOR_PATRONYMIC;
                    return "Enter patronymic";
                } else {
                    return "Validation failed. Enter valid surname";
                }

            case WAITING_FOR_PATRONYMIC:
                if (Record.isValidPatronymic(command)) {
                    record.setPatronymic(command);
                    currentState = WAITING_FOR_POSITION;
                    return "Enter position";
                } else {
                    return "Validation failed. Enter valid patronymic";
                }
            case WAITING_FOR_POSITION:
                if (Record.isValidPosition(command)) {
                    record.setPosition(command);
                    record.commit();
                    currentState = WAITING_FOR_COMMAND;
                    return "Created successfully";
                } else {
                    return "Validation failed. Enter valid position";
                }
            case WAITING_FOR_SEARCH:
                currentState = WAITING_FOR_COMMAND;
                return new Storage().findAll(command);
            default:
                return "Wrong command";
        }
    }


    /**
     * Builds string for "help" command
     */
    private String buildHelp() {
        StringBuilder response = new StringBuilder();
        //TODO String.format
        commands.forEach((k, v) -> {
            response.append(k);
            response.append(" : ");
            response.append(v);
            response.append("\r\n");
        });
        return response.toString();
    }

    public void setUnregisterHandler(ThreadManager.UnregisterHandler unregisterHandler) {
        this.unregisterHandler = unregisterHandler;
    }
}
