package http;
// RFC 7231, Request Methods  4-4.1
// Method = token, The method token is case-sensitive 
// because it might be used as a gateway to object-based systems with case-sensitive method names.
// All general-purpose servers MUST support the methods GET and HEAD.
// All other methods are OPTIONAL.
// When a request method is received
// that is unrecognized or not implemented by an origin server, the
// origin server SHOULD respond with the 501 (Not Implemented) status code.

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
    private String requestLine;
    private String httpVersion;

    HttpRequest(){
    }

    public HttpMethod getMethod() {
        return method;
    }

    void setMethod(HttpMethod method) {
        this.method = method;
    }
}
