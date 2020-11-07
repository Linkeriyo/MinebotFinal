package minebotfinal.exceptions;

public class ServerExclusiveCommandException extends RuntimeException {
    public ServerExclusiveCommandException() {
        super("Server-exclusive command used out of a server.");
    }
}
