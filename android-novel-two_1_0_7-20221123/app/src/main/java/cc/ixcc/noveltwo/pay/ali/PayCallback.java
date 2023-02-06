package cc.ixcc.noveltwo.pay.ali;

/**
 * Created by cxf on 2018/10/23.
 */

public interface PayCallback {
    void onSuccess();

    void onFailed();
}
