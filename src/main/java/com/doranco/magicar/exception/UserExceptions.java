package com.doranco.magicar.exception;



public class UserExceptions {

    public static class UtilisateurNotFoundException extends RuntimeException {
        public UtilisateurNotFoundException(String message) {
            super(message);
        }
    }

    public static class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException(String message) {
            super(message);
        }
    }
}