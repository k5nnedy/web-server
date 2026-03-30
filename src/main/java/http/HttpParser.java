package http;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    //Space & CRLF characters in Hex
    //space
    private static final int SP = 0x20; // 32
    //carriage return
    private static final int CR = 0x0D; // 13
    //line feed
    private static final int LF = 0x0A; // 10
    
    //Creates HTTP Parser parses 3 section of request for each thread
    public HttpRequest parseHttpRequest(InputStream in) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(in, StandardCharsets.US_ASCII);
        
        HttpRequest req = new HttpRequest();

        try {
            parseRequestLine(reader, req);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            parseHeaders(reader, req);
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseBody(reader, req);

        return req;

    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest req) throws IOException, HttpParsingException {

        //Using stringbuilder as a buffer
        StringBuilder processingDataBuffer = new StringBuilder();

        // Differentiating each line of this debugger to identify each item we are looking at
        boolean methodParsed = false;
        boolean requestTargetParsed = false;
        //Detecting CR & LF
        int _byte;
        while((_byte = reader.read()) >=0) {
            if (_byte == CR) {
                _byte = reader.read();
                if (_byte == LF) {
                    LOGGER.debug("Request Line VERSION to Process : {}", processingDataBuffer.toString());
                    if (!methodParsed || !requestTargetParsed) {
                        throw new HttpParsingException(HttpStausCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }

                    try {
                        req.setHttpVersion(processingDataBuffer.toString());
                    } catch (BadHttpVersionException e) {
                        throw new HttpParsingException(HttpStausCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    
                    return;
                } else {
                    // If no line feed throw exception:
                    throw new HttpParsingException(HttpStausCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }

            if (_byte == SP) {
                //Process prev data
                if (!methodParsed) {
                    LOGGER.debug("Request Line METHOD to Process : {}", processingDataBuffer.toString());
                    req.setMethod(processingDataBuffer.toString());
                    methodParsed = true;

                } else if (!requestTargetParsed) {
                    LOGGER.debug("Request Line REQ TARGET to Process : {}", processingDataBuffer.toString());
                    req.setRequestTarget(processingDataBuffer.toString());
                    requestTargetParsed = true;
                    //Error for detecting another space:
                } else {
                    throw new HttpParsingException(HttpStausCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                processingDataBuffer.delete(0, processingDataBuffer.length());

            } else {
                //store char 
                processingDataBuffer.append((char)_byte);
                if (!methodParsed) {
                    if(processingDataBuffer.length() > HttpMethod.MAX_LENGTH) {
                        throw new HttpParsingException(HttpStausCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }


        
    }

    private void parseHeaders(InputStreamReader reader, HttpRequest req) throws IOException, HttpParsingException{
        StringBuilder processingDataBuffer = new StringBuilder();
        boolean crlffound = false;

        int _byte;
        while((_byte = reader.read()) >=0) { 
            if (_byte == CR) {
                _byte = reader.read();
                if (_byte == LF) {
                    if (!crlffound) {
                        crlffound = true;

                        processSingleHeaderField(processingDataBuffer, req);
                        processingDataBuffer.delete(0, processingDataBuffer.length());
                    } else {
                        //Two CRLF received, end of Headers section
                        return;
                    }
                } else {
                    // If no line feed throw exception:
                    throw new HttpParsingException(HttpStausCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }   else {
                //Appends to buffer
                crlffound = false;
                processingDataBuffer.append((char)_byte);

            }
        } 
    }

    private void processSingleHeaderField(StringBuilder processingDataBuffer, HttpRequest req) throws HttpParsingException {
        String rawHeaderField = processingDataBuffer.toString();
        Pattern pattern = Pattern.compile("^(?<fieldName>[!#$%&'*+\\-./^_`|\\w]+):\\s*(?<fieldValue>.*?\\S)?\\s*$");
        
        Matcher matcher = pattern.matcher(rawHeaderField);
        if(matcher.matches()) {
            String fieldName = matcher.group("fieldName");
            String fieldValue = matcher.group("fieldValue");
            req.addHeader(fieldName, fieldValue);
        } else {
            throw new HttpParsingException(HttpStausCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    private void parseBody(InputStreamReader reader, HttpRequest req) {
    }
        
}