package cc.ixcc.noveltwo.http;

public class OkhttpException extends Exception {
    private int code;
    public OkhttpException(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}
