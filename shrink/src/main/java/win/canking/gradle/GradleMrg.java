package win.canking.gradle;

import win.canking.gradle.shrink.ApkUtils;
import win.canking.gradle.shrink.CompressInfo;
import win.canking.gradle.shrink.FileUtils;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.List;

/**
 * Created by changxing on 2017/11/21.
 */
public class GradleMrg {
    public static void do7zipCompressApk(String apkPath) {
        File srcFile = new File(apkPath);
        if (!srcFile.exists()) {
            Log.e("SHRINK", "No src apk file");
            return;
        }
        ProcessBuilder pb = new ProcessBuilder(new String[]{"which", "7za"});
        String sevenZipPath = "";
        try {
            Process process = pb.start();
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String tmp;
            while ((tmp = input.readLine()) != null) {
                if (tmp.endsWith(File.separator + "7za")) {
                    sevenZipPath = tmp;
                    Log.a("Shrink", "7zip path:" + sevenZipPath);
                }
            }
            process.waitFor();
            process.destroy();
        } catch (Exception e) {
            Log.e("Error", "no shrink" + e.getMessage());
            return;
        }

        String srcPath = srcFile.getAbsolutePath();
        String unzipPath = srcPath.replace(".apk", "unzip_FILES");
        String targetFile = srcPath.replace(".apk", "_shrink.apk");
        try {
            List<String> storedFiles = ApkUtils.unzipApk(srcPath, unzipPath);

            filterStoredFiles(storedFiles, unzipPath);

            ApkUtils.do7zApk(unzipPath, targetFile, sevenZipPath, storedFiles);

            FileUtils.deleteFile(new File(unzipPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void filterStoredFiles(List<String> storedFiles, String outPath) {
        CompressInfo info = CompressInfo.init();

        Iterator<String> it = storedFiles.iterator();
        String fileName;
        while (it.hasNext()) {
            fileName = it.next();

            File file = new File(outPath, fileName);
            if (file.exists()) {
                for (String p : info.compressFilesPattern) {
                    if (fileName.endsWith(p)) {
                        it.remove();
                    }
                }
            }
        }
    }
}
