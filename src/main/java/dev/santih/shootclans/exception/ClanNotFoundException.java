package dev.santih.shootclans.exception;

public class ClanNotFoundException extends RuntimeException {
    public ClanNotFoundException(String message) {
        super(message);
    }
}