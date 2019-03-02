package com.example.web3jwalletetherscanandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;


public class TransactionlistActivity extends AppCompatActivity {

    private String htmlContentInStringFormat;
    private TextView textviewHtmlDocument1;
    private String htmlContentInStringFormat1;
    private TextView address;
    private String address1;
    private String etherscanapi;

    // add
    private TextView textviewHtmlDocument_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactionlist);

        address = findViewById(R.id.addressed);

        textviewHtmlDocument1 = (TextView)findViewById(R.id.balance_text);
        // add
        textviewHtmlDocument_2 = (TextView)findViewById(R.id.transaction_text2);




        final Button btnaddress = (Button) findViewById(R.id.btnaddress);
        btnaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(TransactionlistActivity.this, SwitchWalletActivity.class);
                Intent.putExtra("SwitchMode", true);
                startActivityForResult(Intent, SwitchWalletActivity.FROM_ADDRESS);
            }
        });

        Button htmlTitleButton = (Button)findViewById(R.id.transaction_chk);
        htmlTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionAsyncTask jsoupAsyncTask = new TransactionAsyncTask();
                jsoupAsyncTask.execute();
            }
        });

        Button htmlTitleButton1 = (Button)findViewById(R.id.balance_chk);
        htmlTitleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BalanceAsyncTask jsoupAsyncTask1 = new BalanceAsyncTask();
                jsoupAsyncTask1.execute();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        address.setText(data.getStringExtra("address"));
        address1 = "0x"+data.getStringExtra("address");
        etherscanapi="@string/api";
    }

    private class TransactionAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String htmlPageUrl = "https://api-ropsten.etherscan.io/api?module=account&action=txlist&address="+address1+"&startblock=0&endblock=99999999&sort=asc&apikey="+etherscanapi;
                Document doc = Jsoup.connect(htmlPageUrl).ignoreContentType(true).get();
                Elements links = doc.select("body");

                for (Element link : links) {
                    htmlContentInStringFormat += (link.attr("pre#text")
                            + "("+link.text().trim() + ")\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            textviewHtmlDocument_2.setText(htmlContentInStringFormat);
//
        }
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