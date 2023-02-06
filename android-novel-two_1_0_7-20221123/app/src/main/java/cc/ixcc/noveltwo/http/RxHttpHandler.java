package cc.ixcc.noveltwo.http;

public interface RxHttpHandler {
    void onStart();
    void onSuccess(String s);
    void onError(int code);
    void onFinish();
}