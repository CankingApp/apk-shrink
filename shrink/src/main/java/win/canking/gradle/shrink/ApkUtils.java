package win.canking.gradle.shrink;

import win.canking.gradle.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by changxing on 2017/11/19.
 */

public class ApkUtils {
    private static final int BUFFER = 8192;

    public static List<String> unzipApk(String fileName, String filePath)
            throws IOException {
        FileUtils.checkDirectory(filePath);

        List<String> storedFiles = new ArrayList<>();
        ZipFile zipFile = new ZipFile(fileName);
        Enumeration em = zipFile.entries();
        while (em.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) em.nextElement();
            if (entry.isDirectory()) {
                new File(filePath, entry.getName()).mkdirs();
            } else {
                File file = new File(filePath + File.separator + entry.getName());
                File parent = file.getParentFile();
                if ((parent != null) && (!parent.exists())) {
                    parent.mkdirs();
                }
                if (entry.getMethod() == 0) {
                    storedFiles.add(entry.getName());
                }
                BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos, 8192);

                byte[] buf = new byte[BUFFER];
                int len;
                while ((len = bis.read(buf, 0, 8192)) != -1) {
                    fos.write(buf, 0, len);
                }
                bos.flush();
                bos.close();
                bis.close();
            }
        }
        zipFile.close();
        return storedFiles;
    }

    private static void genRaw7zip(String srcDirPath, String outZipPath, String sevenZipPath)
            throws IOException, InterruptedException {
        String srcFilesPath = new File(srcDirPath).getAbsolutePath() + File.separator + "*";
        outZipPath = new File(outZipPath).getAbsolutePath();

        ProcessBuilder pb = new ProcessBuilder(new String[]{sevenZipPath, "a", "-tzip", outZipPath, srcFilesPath, "-mx9"});
        Process process = pb.start();

        InputStreamReader ir = new InputStreamReader(process.getInputStream());
        LineNumberReader input = new LineNumberReader(ir);
        String line;
        while ((line = input.readLine()) != null) {
            Log.d(input.getLineNumber() + ":" + line);
        }
        process.waitFor();
        process.destroy();
    }

    public static void do7zApk(String srcDirPath, String outZipPath, String sevenZipPath, List<String> storedFiles)
            throws IOException, InterruptedException {
        File outFile = new File(outZipPath);
        if (outFile.exists()) {
            outFile.delete();
        }
        genRaw7zip(srcDirPath, outZipPath, sevenZipPath);

        String storedFilesPath = new File(srcDirPath).getParent() + File.separator + "storedFiles" + File.separator;
        for (String name : storedFiles) {
            FileUtils.copyFile(new File(srcDirPath + File.separator + name), new File(storedFilesPath + name));
        }
        storedFilesPath = storedFilesPath + File.separator + "*";
        String cmd = sevenZipPath;
        ProcessBuilder pb = new ProcessBuilder(new String[]{cmd, "a", "-tzip", outZipPath, storedFilesPath, "-mx0"});
        Process proc = pb.start();

        InputStreamReader ir = new InputStreamReader(proc.getInputStream());
        LineNumberReader input = new LineNumberReader(ir);
        String line;
        while ((line = input.readLine()) != null) {
            Log.d(input.getLineNumber() + " stored:" + line);
        }
        proc.waitFor();
        proc.destroy();

        FileUtils.deleteFile(new File(new File(srcDirPath).getParent() + File.separator + "storedFiles" ));
    }
}
