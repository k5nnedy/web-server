package httpserver.core.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import httpserver.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebRootHandlerTest {

    private WebRootHandler webRootHandler;

    private Method containsSlashAtEndMethod;

    private Method doesRelPathExistsInWebRootMethod;


    @BeforeAll
    public void beforeClass() throws WebRootNotFoundException, NoSuchMethodException{
        webRootHandler = new WebRootHandler("Webroot");
        Class<WebRootHandler> cls = WebRootHandler.class;
        containsSlashAtEndMethod = cls.getDeclaredMethod("containsSlashAtEnd", String.class);
        containsSlashAtEndMethod.setAccessible(true);

        doesRelPathExistsInWebRootMethod = cls.getDeclaredMethod("doesRelPathExistsInWebRoot", String.class);
        doesRelPathExistsInWebRootMethod.setAccessible(true);

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

    @Test
    void containsSlashAtEndMethodFALSE() {
        try {
            boolean result = (Boolean) containsSlashAtEndMethod.invoke(webRootHandler,"index.html");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void containsSlashAtEndMethodFALSE2() {
        try {
            boolean result = (Boolean) containsSlashAtEndMethod.invoke(webRootHandler,"/index.html");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void containsSlashAtEndMethodFALSE3() {
        try {
            boolean result = (Boolean) containsSlashAtEndMethod.invoke(webRootHandler,"/private/index.html");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void containsSlashAtEndMethodTRUE1() {
        try {
            boolean result = (Boolean) containsSlashAtEndMethod.invoke(webRootHandler,"/");
            assertTrue(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void containsSlashAtEndMethodTRUE2() {
        try {
            boolean result = (boolean) containsSlashAtEndMethod.invoke(webRootHandler,"/private/");
            assertTrue(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootFilePathExists() {

        try {
            boolean result = (boolean) doesRelPathExistsInWebRootMethod.invoke(webRootHandler, "/index.html");
            assertTrue(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootFilePathExistsGoodRelative() {

        try {
            boolean result = (boolean) doesRelPathExistsInWebRootMethod.invoke(webRootHandler, "/./././index.html");
            assertTrue(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootFilePathDoesNotExist() {

        try {
            boolean result = (boolean) doesRelPathExistsInWebRootMethod.invoke(webRootHandler, "/indexNotindex.html");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootFilePathTraverseOut() {

        try {
            boolean result = (boolean) doesRelPathExistsInWebRootMethod.invoke(webRootHandler, "/../LICENSE");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void TestgetFileMineTypeText() {
        try {
            String mineType = webRootHandler.getFileMineType("/");
            assertEquals("text/html", mineType);
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void TestgetFileMineTypePng() {
        try {
            String mineType = webRootHandler.getFileMineType("/osamason.png");
            assertEquals("image/png", mineType);
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileByteArrayData() {
        try {
            assertTrue(webRootHandler.getFileByteArrayData("/").length > 0);
        } catch (FileNotFoundException e) {
            fail(e);
        } catch (ReadFileException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileByteArrayDataFileNotFound() {
        try {
        webRootHandler.getFileByteArrayData("/test.html");
        fail();
        } catch (FileNotFoundException e) {
            //pass
        } catch (ReadFileException e) {
            fail(e);
        }
    }
}
