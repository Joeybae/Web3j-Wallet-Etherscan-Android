package com.example.web3jwalletetherscanandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.web3jwalletetherscanandroid.web3j.Web3JService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private TextView mAddressField1;
    private TextView textviewHtmlDocument1;
    private String htmlContentInStringFormat1;
    private String address1;
    private String etherscanapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        String s = Environment.getDataDirectory().toString();

        textviewHtmlDocument1 = (TextView)findViewById(R.id.checkvalue);

        Log.e("dir", "main:" + WalletUtils.getMainnetKeyDirectory());
        Log.e("dir", "def:" + WalletUtils.getDefaultKeyDirectory());
        Log.e("dir", "data:" + s);
        Log.e("dir", "getfiles:" + getFilesDir().getAbsolutePath());

        final Button btnaddress1 = (Button) findViewById(R.id.btnaddress1);
        btnaddress1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(MainActivity.this, SwitchWalletActivity.class);
                Intent.putExtra("SwitchMode", true);
                startActivityForResult(Intent, SwitchWalletActivity.FROM_ADDRESS);
            }
        });

        Button htmlTitleButton1 = (Button)findViewById(R.id.checkvaluebtn);
        htmlTitleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BalanceAsyncTask jsoupAsyncTask1 = new BalanceAsyncTask();
                jsoupAsyncTask1.execute();

            }
        });

        mAddressField1 = findViewById(R.id.addressed1);
    }


    public void transation(View view) {
        startActivity(new Intent(this, SendActivity.class));
    }

    public void transactionlist(View view) {
        startActivity(new Intent(this, TransactionlistActivity.class));
    }

    public void generateWallet(View view) {

        startActivity(new Intent(this, GenerateWallet.class));

    }

    public void switchWallet(View view) {

        startActivity(new Intent(this, SwitchWalletActivity.class));
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.MOUNT_UNMOUNT_FILESYSTEMS"

    };


    public static void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testNetwork(View view) {
        Observable.create((ObservableOnSubscribe<Web3ClientVersion>) e -> {
            Web3ClientVersion send = Web3JService.getInstance().web3ClientVersion().send();
            e.onNext(send);
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(web3ClientVersion -> {
                    String version = web3ClientVersion.getWeb3ClientVersion();
                    Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
                    Log.i("web3j",version);
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAddressField1.setText(data.getStringExtra("address"));
        address1 = "0x"+data.getStringExtra("address");
        etherscanapi="@string/api";
    }

    private class BalanceAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String htmlPageUrl = "https://api-ropsten.etherscan.io/api?module=account&action=balance&address="+address1+"&tag=latest&apikey="+etherscanapi;
                Document doc = Jsoup.connect(htmlPageUrl).ignoreContentType(true).get();
                Elements links = doc.select("body");

                for (Element link : links) {
                    htmlContentInStringFormat1 += (link.attr("pre#text")
                            + "("+link.text().trim() + ")\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            String result_wei = htmlContentInStringFormat1.substring(44,htmlContentInStringFormat1.length()-4);
            textviewHtmlDocument1.setText(" ( Wei : "+result_wei+" )");
        }
    }
}
