package projetImage;

import org.opencv.core.Core;

public class SobelDemo {
    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new SobelDemoRun().run(args);
    }
}