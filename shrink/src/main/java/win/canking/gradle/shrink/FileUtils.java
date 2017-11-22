package win.canking.gradle.shrink;

import java.io.*;

/**
 * Created by changxing on 2017/11/19.
 */

public class FileUtils {
    public static boolean deleteFile(File file) {
        if ((file == null) || (!file.exists())) {
            return false;
        }
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i]);
            }
        }
        return file.delete();
    }

    public static boolean checkDirectory(String dir) {
        File file = new File(dir);

        deleteFile(file);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    public static void close(Closeable target) {
        if (target != null) {
            try {
                target.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyFile(File source, File dest)
            throws IOException {
        FileInputStream is = null;
        FileOutputStream os = null;
        File parent = dest.getParentFile();
        if ((parent != null) && (!parent.exists())) {
            parent.mkdirs();
        }
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest, false);

            byte[] buffer = new byte[8192];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            FileUtils.close(is);
            FileUtils.close(os);
        }
    }

}
