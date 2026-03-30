package httpserver.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;

public class WebRootHandler {

    private File webRoot;

    public WebRootHandler(String webRootPath) throws WebRootNotFoundException {
        webRoot = new File(webRootPath);

        if (!webRoot.exists() || !webRoot.isDirectory()) {
            throw new WebRootNotFoundException("Webroot provided does not exist or is not a folder");
            
        }
    }

    private boolean containsSlashAtEnd(String relativePath) {
        return relativePath.endsWith("/");
    }

    /**
     * This method checks if the relative path is inside of the WebRoot
     * @param relativePath
     * @return true if the path exists inside the WebRoot, false if it does not.
     */
    private boolean doesRelPathExistsInWebRoot(String relativePath) {
        File file = new File(webRoot, relativePath);

        //comparing canonical path, which resolves path traversing issues
        if(!file.exists()) {
            return false;
        }
        try {
            if (file.getCanonicalPath().startsWith(webRoot.getCanonicalPath())) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;

    }

    //populates header: content-type: Section 4.3.3. of RFC 7231 - If a header is missing then a client may assume 
    //media type "application/octet-stream" as the defualt
    public String getFileMineType(String relativePath) throws FileNotFoundException {
        if (containsSlashAtEnd(relativePath)) {
            relativePath += "index.html"; //Serving index.html by default if it exists.
        }

        //if it does not exist/is not reachable throw error
        if (!doesRelPathExistsInWebRoot(relativePath)) {
            throw new FileNotFoundException("File not found" + relativePath);
        }
        File file = new File(webRoot, relativePath);

        //getting minetype using filenamemap
        //avoids from creating map of exentsions and corresponding mine types
        String mineType = URLConnection.getFileNameMap().getContentTypeFor(file.getName());

        //unresolved minetype return application/octet-stream
        //
        if(mineType == null) {
            return "application/octet-stream";
        }

        return mineType;
    }

    /**
     * Returns byte array of the contents of a file for a relative path
     * 
     * TODO - For large files a new implementation of the fileBytes array might be necessary
     * 
     * @param relativePath the path to the file in the specific webroot folder.
     * @return a byte array of the data.
     * @throws FileNotFoundException if the file cannot be found.
     * @throws ReadFileException if the file cannot be read.
     */
    public byte[] getFileByteArrayData(String relativePath) throws FileNotFoundException, ReadFileException {

        //same test for "/" and reachability of the file:

        if (containsSlashAtEnd(relativePath)) {
            relativePath += "index.html"; //Serving index.html by default if it exists.
        }

        //if it does not exist/is not reachable throw error
        if (!doesRelPathExistsInWebRoot(relativePath)) {
            throw new FileNotFoundException("File not found" + relativePath);
        }
        File file = new File(webRoot, relativePath);

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];

        try {
            fileInputStream.read(fileBytes);
            fileInputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new ReadFileException(e);
        } 
        return fileBytes;


    }

}
