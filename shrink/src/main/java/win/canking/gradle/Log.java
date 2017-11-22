package win.canking.gradle;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Created by changxing on 2017/11/21.
 */
public class Log {
    private static boolean ERROR = false;
    private static String TAG = "Shrink";

    private static PrintStream sOut = System.out;

    public static void setOutput(String filename) {
        try {
            sOut = new PrintStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void d(String msg) {
        if (TAG == null) {
        } else  {
            sOut.println("(" + TAG + ") " + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (ERROR) sOut.println("(" + tag + ") " + msg);
    }

    public static void a(String tag, String msg) {
        sOut.println("(" + tag + ") " + msg);
    }
}
