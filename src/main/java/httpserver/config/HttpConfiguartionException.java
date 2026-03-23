package httpserver.config;

public class HttpConfiguartionException extends RuntimeException{

    public HttpConfiguartionException() {
    }

    public HttpConfiguartionException(String message) {
        super(message);
    }

    public HttpConfiguartionException(Throwable cause) {
        super(cause);
    }

    public HttpConfiguartionException(String message, Throwable cause) {
        super(message, cause);
    }

}
