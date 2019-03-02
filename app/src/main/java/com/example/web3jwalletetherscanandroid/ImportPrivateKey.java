package com.example.web3jwalletetherscanandroid;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.web3jwalletetherscanandroid.web3j.utils.KeyStoreUtils;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.utils.Numeric;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pc on 2018/1/23.
 */

public class ImportPrivateKey extends BasecontractActivity {
    @BindView(R.id.et_private_key)
    EditText etPrivateKey;
    @BindView(R.id.btn_import)
    Button btnImport;
    @BindView(R.id.tv_mgs)
    TextView tvMgs;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_import_private_key;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.btn_import)
    public void onViewClicked() {
        importPrivateKey();

    }

    private void importPrivateKey() {
        checkInput();
        String privateKey = etPrivateKey.getText().toString();
        Credentials credentials = Credentials.create(privateKey);
        ECKeyPair ecKeyPair = credentials.getEcKeyPair();
        KeyStoreUtils.genKeyStore2Files(ecKeyPair);
        String msg = "address:\n" + credentials.getAddress()
                + "\nprivateKey:\n" + Numeric.encodeQuantity(ecKeyPair.getPrivateKey())
                + "\nPublicKey:\n" + Numeric.encodeQuantity(ecKeyPair.getPublicKey());

        tvMgs.setText(msg);


    }

    private boolean checkInput() {
        if (etPrivateKey.length()==0){
            etPrivateKey.setError("비밀 키를 입력해 주세요");
            return false;
        }
        return true;


    }
}
