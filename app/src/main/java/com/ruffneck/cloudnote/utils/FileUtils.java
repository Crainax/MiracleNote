package com.ruffneck.cloudnote.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static void mkRootDir(String filePath) {
        try {
            File file = new File(filePath);

            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception ignored) {

        }
    }

}
