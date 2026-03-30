package http;

public class HttpParsingException extends Exception {

    private final HttpStausCode errorCode;

    public HttpParsingException(HttpStausCode errorCode) {
        super(errorCode.MESSAGE);
        this.errorCode = errorCode;
    }

    public HttpStausCode getErrorCode() {
        return errorCode;
    }
}