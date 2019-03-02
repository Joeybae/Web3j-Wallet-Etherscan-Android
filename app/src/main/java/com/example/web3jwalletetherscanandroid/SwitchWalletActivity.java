package com.example.web3jwalletetherscanandroid;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.web3jwalletetherscanandroid.web3j.utils.KeyStoreBean;
import com.example.web3jwalletetherscanandroid.web3j.utils.KeyStoreUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class SwitchWalletActivity extends BasecontractActivity {
    public static final int FROM_ADDRESS = 232;
    public static final int TO_ADDRESS = 255;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private BaseQuickAdapter<KeyStoreBean, BaseViewHolder> adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_switch_wallet;
    }


    @Override
    protected void initView() {

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<KeyStoreBean, BaseViewHolder>(R.layout.item_keystore) {

            @Override
            protected void convert(BaseViewHolder helper, KeyStoreBean item) {
                helper.setText(R.id.tv_address, item.getAddress());
            }
        };
        recyclerview.setAdapter(adapter);
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    

    }


    private void showPopuMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.item_keystore_menu, popupMenu.getMenu());
        popupMenu.setGravity(Gravity.RIGHT);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            KeyStoreBean item1 = adapter.getItem(position);
            new File(item1.getPath()).delete();
            adapter.remove(position);
            return true;
        });
    }

    @Override
    protected void initData() {
        boolean switchMode = getIntent().getBooleanExtra("SwitchMode", false);
        adapter.setOnItemLongClickListener((adapter, view, position) -> {
            showPopuMenu(view, position);
            return true;
        });

        if (switchMode)
            adapter.setOnItemClickListener((adapter, view, position) -> {
                KeyStoreBean item = this.adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("address", item.getAddress());
                intent.putExtra("path", item.getPath());
                setResult(RESULT_OK, intent);
                finish();
            });


        adapter.replaceData(gekeystoreList());

    }

    private List<KeyStoreBean> gekeystoreList() {


        List<KeyStoreBean> keyStoreBeans = new ArrayList<>();

        File[] keyStoreFiles = KeyStoreUtils.getKeyStorePathFile().listFiles();

        for (File file : keyStoreFiles) {

            String name = file.getName();
            String address;
            if (name.endsWith(".json")){
                 address = name.substring(name.lastIndexOf("--") + 2, name.lastIndexOf("."));
            }else {
                 address = name.substring(name.lastIndexOf("--") + 2, name.length()-1);
            }
            keyStoreBeans.add(new KeyStoreBean(address, file.getAbsolutePath()));
        }
        return keyStoreBeans;
    }
}
