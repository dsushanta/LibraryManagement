package com.bravo.johny.utils;

import com.bravo.johny.config.ProjectConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.net.URI;

public class CommonUtils {

    public static Logger getLoggerInstance(Class loggingClass) {
        return LoggerFactory.getLogger(loggingClass);
    }

    public static URI getURISelf(@Context UriInfo uriInfo, String id, Object object) {

        return uriInfo.getBaseUriBuilder()
                .path(object.getClass())
                .path(id)
                .build();
    }

    public static File getResourceAsFileObject(Object object, String resourceFilename) {
        File file = new File(
                object.getClass()
                        .getClassLoader()
                        .getResource(resourceFilename)
                        .getFile()
        );

        return file;
    }

    public static String getResourceFullPath(Object object, String resourceFilename) {
        return object.getClass()
                .getClassLoader()
                .getResource(resourceFilename)
                .getFile();
    }

    public static void throwBadRequestRuntimeException(String message) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                message
        );
    }

    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(ProjectConfig.BCRYPT_STRENGTH);
    }
}
