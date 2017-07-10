package com.intera.server;

/**
 * This enumeration contains all possible states for ClientThread on server side
 */
public enum ClientThreadState {
    WAITING_FOR_COMMAND,
    WAITING_FOR_NAME,
    WAITING_FOR_SURNAME,
    WAITING_FOR_PATRONYMIC,
    WAITING_FOR_POSITION,
    WAITING_FOR_SEARCH
}
