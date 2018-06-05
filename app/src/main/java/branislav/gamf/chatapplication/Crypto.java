package branislav.gamf.chatapplication;

public class Crypto {
    public native String crypt(String message);

    static {
        System.loadLibrary("crypto");
    }
}