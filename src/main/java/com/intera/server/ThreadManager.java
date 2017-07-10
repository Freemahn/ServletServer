package com.intera.server;

import javax.naming.LimitExceededException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class manages all threads.
 * Used Singletone pattern though.
 * Created by pgordon on 05.07.2017.
 */
public class ThreadManager {
    private static ThreadManager instance;
    private static final int MAX_CLIENTS = 2;

    private final ConcurrentHashMap<Long, ClientThread> clients = new ConcurrentHashMap<>();
    private AtomicLong pid = new AtomicLong();

    /**
     * Hidden private constructor
     * */
    private ThreadManager() {
    }

    public static synchronized ThreadManager getInstance() {
        if (instance == null) {
            instance = new ThreadManager();
        }
        return instance;
    }

    /**
     * Tries to register thread.
     * @param thread thread to register
     * @throws LimitExceededException if there are more than MAX_CLIENTS
     * */

    public void registerThread(ClientThread thread) throws LimitExceededException {
        if (clients.size() >= MAX_CLIENTS) {
            throw new LimitExceededException();
        }
        long id = pid.incrementAndGet();
        clients.put(id, thread);
        thread.setUnregisterHandler(new UnregisterHandler(id));

    }


    /**
     * Inner class for thread to unregister itself.
     */
    class UnregisterHandler {

        /**
         * Id of process to identify in ThreadManager
         * */
        private long pid;

        public UnregisterHandler(long pid) {
            this.pid = pid;
        }

        public void unregister() {
            clients.remove(pid);
        }
    }


}
