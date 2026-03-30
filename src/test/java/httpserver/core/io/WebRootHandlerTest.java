package httpserver.core.io;

import static org.junit.jupiter.api.Assertions.fail;

import httpserver.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebRootHandlerTest {

    @BeforeAll
    public void beforeClass() {


    }

    @Test
    void constructorGoodPath() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("/Users/kennedy/web-server/WebRoot");
        } catch (WebRootNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void constructorBadPath() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("/Users/kennedy/web-server/WebRoot2");
            fail();
        } catch (WebRootNotFoundException e) {
        }
    }

    @Test
    void constructorGoodPath2() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("WebRoot");
        } catch (WebRootNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void constructorBadPath2() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("WebRoot2");
            fail();
        } catch (WebRootNotFoundException e) {
        }
    }
}
