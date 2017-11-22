package win.canking.gradle.shrink;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by changxing on 2017/11/19.
 */

public class CompressInfo {
    public Set<String> compressFilesPattern;

    private CompressInfo() {
        this.compressFilesPattern = new HashSet<>();
    }

    public static CompressInfo init() {
        CompressInfo info = new CompressInfo();
        info.compressFilesPattern.add(".png");
        info.compressFilesPattern.add(".jpg");
        info.compressFilesPattern.add(".JPG");
        info.compressFilesPattern.add(".jpeg");
        info.compressFilesPattern.add(".gif");
        info.compressFilesPattern.add(".mp3");
        info.compressFilesPattern.add(".arsc");
        return info;
    }


}
