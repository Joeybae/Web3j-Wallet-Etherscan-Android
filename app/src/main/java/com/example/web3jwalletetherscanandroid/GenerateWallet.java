package com.example.web3jwalletetherscanandroid;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.web3jwalletetherscanandroid.web3j.utils.KeyStoreUtils;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pc on 2018/1/24.
 */

public class GenerateWallet extends BasecontractActivity {
    @BindView(R.id.btn_generate)
    Button btnGenerate;
    @BindView(R.id.tv_mgs)
    TextView tvMgs;
    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_generate_wallet;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    public void importKeystore(View view) {
        startActivity(new Intent(this, ImportKeystore.class));

    }


    public void importPrivateKey(View view) {
        startActivity(new Intent(this, ImportPrivateKey.class));
    }


    @OnClick(R.id.btn_generate)
    public void onViewClicked() {

        genWallet();
    }

    private void genWallet() {
        if (etPassword.length() == 0) {
            etPassword.setError("입력해 주세요 password");
            return;
        }
        String password = etPassword.getText().toString();
        try {
            File fileDir = new File(Environment.getExternalStorageDirectory().getPath() + "/LightWallet");
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();

            //외장 카드 생성
            String filename = WalletUtils.generateWalletFile(password, ecKeyPair, fileDir, false);

            KeyStoreUtils.genKeyStore2Files(ecKeyPair);

            String msg = "fileName:\n" + filename
                    + "\nprivateKey:\n" + Numeric.encodeQuantity(ecKeyPair.getPrivateKey())
                    + "\nPublicKey:\n" + Numeric.encodeQuantity(ecKeyPair.getPublicKey());
            tvMgs.setText(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
