package cc.ixcc.noveltwo.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatButton;

import com.hjq.bar.TitleBar;
import cc.ixcc.noveltwo.Constants;
import cc.ixcc.noveltwo.R;
import cc.ixcc.noveltwo.aop.DebugLog;
import cc.ixcc.noveltwo.common.MyActivity;
import cc.ixcc.noveltwo.event.AddCountEvent;
import cc.ixcc.noveltwo.helper.InputTextHelper;
import cc.ixcc.noveltwo.other.KeyboardWatcher;
import cc.ixcc.noveltwo.ui.dialog.CustomSelectDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc   : 添加提现账户
 */
public final class AddAccountActivity extends MyActivity
        implements
        KeyboardWatcher.SoftKeyboardStateListener {

    @BindView(R.id.title)
    TitleBar title;
    @BindView(R.id.et_count)
    EditText etCount;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.btn_commit)
    AppCompatButton btnCommit;
    @BindView(R.id.et_type)
    EditText etType;
    int type;

    @DebugLog
    public static void start(Context context) {
        Intent intent = new Intent(context, AddAccountActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_account;
    }

    @Override
    protected void initView() {
        InputTextHelper.with(this)
                .addView(etType)
                .addView(etCount)
                .addView(etName)
                .setMain(btnCommit)
//                .setListener(helper ->
//                        (etPhone.getText().toString().length() == 11))
                .build();
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {

    }

    @Override
    public void onSoftKeyboardClosed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    private void changeFocus() {

    }

    private void showTypeDialog() {
        final CustomSelectDialog dialog = CustomSelectDialog.getMyDialog(this, "支付宝账户", "微信账户");
        //回调实现点击
        dialog.setDialogCallBack(new CustomSelectDialog.DialogCallBack() {
            @Override
            public void onTitleClick(String title) {
                etType.setText(title);
                type = Constants.TYPE_ZFB;
                dialog.dismiss();
            }

            @Override
            public void onTitle2Click(String title) {
                etType.setText(title);
                type = Constants.TYPE_WX;
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @OnClick({R.id.et_type, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_type:
                showTypeDialog();
                break;
            case R.id.btn_commit:
                EventBus.getDefault().post(new AddCountEvent(type, etName.getText().toString(), etCount.getText().toString()));
                finish();
                break;
        }
    }
}