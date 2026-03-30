package http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1",1,1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;
    
    HttpVersion(String LITERAL, int MAJOR, int MINOR) {
        this.LITERAL = LITERAL;
        this.MAJOR = MAJOR;
        this.MINOR = MINOR;
    }

    public static final Pattern httpVersionRegexPattern = Pattern.compile("^HTTP/(?<major>\\d+)\\.(?<minor>\\d+)$");
    
    public static HttpVersion getBestCompatibleVersion (String literalVersion) throws BadHttpVersionException{
        //Checking if literal string provided matches what we want from pattern
        Matcher matcher = httpVersionRegexPattern.matcher(literalVersion);
        if(!matcher.matches() || matcher.groupCount() != 2) {
            //Change to a 505 error if we find an incompatible version
            //But since we are checking if string does not represent the version, then we use the 500 error.
            throw new BadHttpVersionException();
        }
        //Extracting minor and major
        int major = Integer.parseInt(matcher.group("major"));
        int minor = Integer.parseInt(matcher.group("minor"));

        HttpVersion tempBestCompatible = null;
        //Finding best compatible version
        for (HttpVersion version : HttpVersion.values()) {
            if (version.LITERAL.equals(literalVersion)) {
                return version;
            } else {
                // if version major is = to version for literal:
                // check if minor version is smaller than minor from literal from the input:
                // if it is then it is assigned to best tempBestCompatible
                if (version.MAJOR == major) {
                    if (version.MINOR < minor) {
                        tempBestCompatible = version;
                    }
                }

            }
        }
        return tempBestCompatible;
    }
}
