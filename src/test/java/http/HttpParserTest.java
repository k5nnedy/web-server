package http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HttpParserTest {

    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass() {
        httpParser = new HttpParser();
    }
    
    @Test
    void parseHttpRequest() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(
                generateValidGETTestCase()
            );

            
        } catch (HttpParsingException e) {
            
        }
        assertNotNull(request);
        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getRequestTarget(), "/");
        assertEquals(request.getBestCompatibleVersion(), HttpVersion.HTTP_1_1);
        assertEquals(request.getOriginalHttpVersion(), "HTTP/1.1");
    }

    @Test
    void parseHttpRequestBadMethod1() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                generateBadTestCaseMethodName1()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStausCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @Test
    void parseHttpRequestBadMethod2() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                generateBadTestCaseMethodName2()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStausCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @Test
    void parseHttpRequestInvNumOfItems1() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                generateBadTestCaseRequestLineInvNumOfItems1()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStausCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseHttpEmptyRequestLine() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                generateBadTestCaseRequestLineOnlyCRnoLF()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStausCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseHttpRequestLineCRnoLF() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                generateBadTestCaseRequestLineOnlyCRnoLF()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStausCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseHttpRequestBadHttpVersion() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                generateBadHttpVersionTestCase()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStausCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseHttpRequestUnsupportedHttpVersion() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                generateUnsupportedHttpVersionTestCase()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStausCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    @Test
    void parseHttpRequestSupportedHttpVersion() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                generateSupportedHttpVersionTestCase1()
            );
            assertNotNull(request);
            assertEquals(request.getBestCompatibleVersion(), HttpVersion.HTTP_1_1);
            assertEquals(request.getOriginalHttpVersion(), "HTTP/1.2");
            
        } catch (HttpParsingException e) {
            fail();
        }
    }
    
    //method for a test case
    private InputStream generateValidGETTestCase() {
        String rawData = "GET / HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Cache-Control: max-age=0\r\n" +
                        "sec-ch-ua: \"Chromium\";v=\"146\", \"Not-A.Brand\";v=\"24\", \"Google Chrome\";v=\"146\"\r\n" +
                        "sec-ch-ua-mobile: ?0\r\n" +
                        "sec-ch-ua-platform: \"macOS\"\r\n" +
                        "Upgrade-Insecure-Requests: 1\r\n" +
                        "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36\r\n" +
                        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                        "Sec-Fetch-Site: none\r\n" +
                        "Sec-Fetch-Mode: navigate\r\n" +
                        "Sec-Fetch-User: ?1\r\n" +
                        "Sec-Fetch-Dest: document\r\n" +
                        "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                        "Accept-Language: en-US,en;q=0.9\r\n" +
                        "\r\n";

        InputStream in = new ByteArrayInputStream(
            rawData.getBytes(
                StandardCharsets.US_ASCII
            )
        );
        
        return in;
    }

    //method for a test case
    private InputStream generateBadTestCaseMethodName1() {
        String rawData = "GeT / HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Accept-Language: en-US,en;q=0.9\r\n" +
                        "\r\n";

        InputStream in = new ByteArrayInputStream(
            rawData.getBytes(
                StandardCharsets.US_ASCII
            )
        );
        
        return in;
    }

    
    private InputStream generateBadTestCaseMethodName2() {
        String rawData = "GETTTT / HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Accept-Language: en-US,en;q=0.9\r\n" +
                        "\r\n";

        InputStream in = new ByteArrayInputStream(
            rawData.getBytes(
                StandardCharsets.US_ASCII
            )
        );
        
        return in;
    }

    //Test case for 3+ items in request line
    private InputStream generateBadTestCaseRequestLineInvNumOfItems1() {
        String rawData = "GET / AAAAA HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Accept-Language: en-US,en;q=0.9\r\n" +
                        "\r\n";

        InputStream in = new ByteArrayInputStream(
            rawData.getBytes(
                StandardCharsets.US_ASCII
            )
        );
        
        return in;
    }

    //Test case for an empty req line
    private InputStream generateBadTestCaseEmptyRequestLine() {
        String rawData = "\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Accept-Language: en-US,en;q=0.9\r\n" +
                        "\r\n";

        InputStream in = new ByteArrayInputStream(
            rawData.getBytes(
                StandardCharsets.US_ASCII
            )
        );
        
        return in;
    }

    private InputStream generateBadTestCaseRequestLineOnlyCRnoLF() {
        String rawData = "GET / HTTP/1.1\r" + // <-- no LF
                        "Host: localhost:8080\r\n" +
                        "Accept-Language: en-US,en;q=0.9\r\n" +
                        "\r\n";

        InputStream in = new ByteArrayInputStream(
            rawData.getBytes(
                StandardCharsets.US_ASCII
            )
        );
        
        return in;
    }

    private InputStream generateBadHttpVersionTestCase() {
        String rawData = "GET / HTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Cache-Control: max-age=0\r\n" +
                        "sec-ch-ua: \"Chromium\";v=\"146\", \"Not-A.Brand\";v=\"24\", \"Google Chrome\";v=\"146\"\r\n" +
                        "sec-ch-ua-mobile: ?0\r\n" +
                        "sec-ch-ua-platform: \"macOS\"\r\n" +
                        "Upgrade-Insecure-Requests: 1\r\n" +
                        "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36\r\n" +
                        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                        "Sec-Fetch-Site: none\r\n" +
                        "Sec-Fetch-Mode: navigate\r\n" +
                        "Sec-Fetch-User: ?1\r\n" +
                        "Sec-Fetch-Dest: document\r\n" +
                        "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                        "Accept-Language: en-US,en;q=0.9\r\n" +
                        "\r\n";

        InputStream in = new ByteArrayInputStream(
            rawData.getBytes(
                StandardCharsets.US_ASCII
            )
        );
        
        return in;
    }

    private InputStream generateUnsupportedHttpVersionTestCase() {
        String rawData = "GET / HTTP/2.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Cache-Control: max-age=0\r\n" +
                        "sec-ch-ua: \"Chromium\";v=\"146\", \"Not-A.Brand\";v=\"24\", \"Google Chrome\";v=\"146\"\r\n" +
                        "sec-ch-ua-mobile: ?0\r\n" +
                        "sec-ch-ua-platform: \"macOS\"\r\n" +
                        "Upgrade-Insecure-Requests: 1\r\n" +
                        "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36\r\n" +
                        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                        "Sec-Fetch-Site: none\r\n" +
                        "Sec-Fetch-Mode: navigate\r\n" +
                        "Sec-Fetch-User: ?1\r\n" +
                        "Sec-Fetch-Dest: document\r\n" +
                        "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                        "Accept-Language: en-US,en;q=0.9\r\n" +
                        "\r\n";

        InputStream in = new ByteArrayInputStream(
            rawData.getBytes(
                StandardCharsets.US_ASCII
            )
        );
        
        return in;
    }
    private InputStream generateSupportedHttpVersionTestCase1() {
        String rawData = "GET / HTTP/1.2\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Cache-Control: max-age=0\r\n" +
                        "sec-ch-ua: \"Chromium\";v=\"146\", \"Not-A.Brand\";v=\"24\", \"Google Chrome\";v=\"146\"\r\n" +
                        "sec-ch-ua-mobile: ?0\r\n" +
                        "sec-ch-ua-platform: \"macOS\"\r\n" +
                        "Upgrade-Insecure-Requests: 1\r\n" +
                        "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36\r\n" +
                        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                        "Sec-Fetch-Site: none\r\n" +
                        "Sec-Fetch-Mode: navigate\r\n" +
                        "Sec-Fetch-User: ?1\r\n" +
                        "Sec-Fetch-Dest: document\r\n" +
                        "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                        "Accept-Language: en-US,en;q=0.9\r\n" +
                        "\r\n";

        InputStream in = new ByteArrayInputStream(
            rawData.getBytes(
                StandardCharsets.US_ASCII
            )
        );
        
        return in;
    }

}