package http;
// RFC 7231, Request Methods  4-4.1
// Method = token, The method token is case-sensitive 
// because it might be used as a gateway to object-based systems with case-sensitive method names.
// All general-purpose servers MUST support the methods GET and HEAD.
// All other methods are OPTIONAL.
// When a request method is received
// that is unrecognized or not implemented by an origin server, the
// origin server SHOULD respond with the 501 (Not Implemented) status code.

import java.util.HashMap;
import java.util.Set;

// When a request method is received that is known by an origin
// server but not allowed for the target resource, the origin server
// SHOULD respond with the 405 (Method Not Allowed) status code.

// RFC 7230, Request Line 3.1.1.
// Recipients of an invalid request-line SHOULD respond with either a
// 400 (Bad Request) error
// HTTP does not place a predefined limit on the length of a
// request-line. A server that receives a method longer than any that it implements 
// SHOULD respond with a 501 (Not Implemented) status code.

// request-target longer than any URI it wishes to parse MUST respond
// with a 414 (URI Too Long) status code

// It is RECOMMENDED that all HTTP senders and recipients support, at a minimum, request-line lengths of 8000 octets.
// 1 Octet = 8 bits = 1 Byte, therefore 8000 bytes.


   
public class HttpRequest extends HttpMessage {

    private HttpMethod method;
    private String requestTarget;
    private String originalHttpVersion; // original literal we get from the request
    private HttpVersion bestCompatibleVersion;
    private HashMap<String, String> headers; // storing headers in hashmap, header name = key, corresponding value = values

    HttpRequest(){
        this.headers = new HashMap<>();
    }

    public HttpMethod getMethod() {
        return method;
    }

    
    public String getRequestTarget() {
        return requestTarget;
    }

    public String getOriginalHttpVersion() {
        return originalHttpVersion;
    }

    public HttpVersion getBestCompatibleVersion() {
        return bestCompatibleVersion;
    }
    
    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    public String getHeader(String headerName) {
        return headers.get(headerName.toLowerCase());
    }

    void setMethod(String methodName) throws HttpParsingException {
        for (HttpMethod method: HttpMethod.values()) {
            if (methodName.equals(method.name())) {
                this.method = method;
                return;
            }
        }
        throw new HttpParsingException(
            HttpStausCode.SERVER_ERROR_501_NOT_IMPLEMENTED
        );
    }

    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if (requestTarget == null || requestTarget.length() == 0) {
            throw new HttpParsingException(HttpStausCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.requestTarget = requestTarget;
    }

    void setHttpVersion(String originalHttpVersion) throws BadHttpVersionException, HttpParsingException {
        this.originalHttpVersion = originalHttpVersion;
        this.bestCompatibleVersion = HttpVersion.getBestCompatibleVersion(originalHttpVersion);
        if (this.bestCompatibleVersion == null) {
            throw new HttpParsingException(
                HttpStausCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED
            );
        }
    }

    void addHeader(String headerName, String headerField) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(headerName.toLowerCase(), headerField);
    }
}
