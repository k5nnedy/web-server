package http;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    //Space & CRLF characters in ASCII
    //space
    private static final int SP = 0x28; // 32
    //carriage return
    private static final int CR = 0x0D; // 13
    //line feed
    private static final int LF = 0x0A // 10
    
    //Creates HTTP Parser parses 3 section of request for each thread
    public HttpRequest parseHttpRequest(InputStream in) {
        InputStreamReader reader = new InputStreamReader(in, StandardCharsets.US_ASCII);
        
        HttpRequest req = new HttpRequest();

        parseRequestLine(reader, req);
        parseHeaders(reader, req);
        parseBody(reader, req);

        return req;

    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest req) {
         
        int _byte;
        while((_byte = reader.read()) >=0) {
            if (_byte == CR) {
                _byte = reader.read();
                if (_byte == LF) {
                    return;
                }
            }
        }
        
    }

    private void parseHeaders(InputStreamReader reader, HttpRequest req) {
        
    }

    private void parseBody(InputStreamReader reader, HttpRequest req) {
        
    }
}