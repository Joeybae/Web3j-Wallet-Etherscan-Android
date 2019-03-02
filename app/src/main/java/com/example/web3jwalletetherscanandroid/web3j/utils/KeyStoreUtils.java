package com.example.web3jwalletetherscanandroid.web3j.utils;

import android.util.Log;

import com.example.web3jwalletetherscanandroid.App;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

public class KeyStoreUtils {
    public static final String DEFAULTKEY = "DEFAULT";
    public static final String KEYSTORE_PATH = App.getInstance().getFilesDir().getPath() + "/keystore";

    public static String genKeyStore2Files(ECKeyPair ecKeyPair) {

        try {
            File file =getKeyStorePathFile();
            String s = WalletUtils.generateWalletFile(DEFAULTKEY, ecKeyPair, file, false);
            Log.e("gen",s);
            return s;

        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Credentials getCredentials(String tagetAddress) throws FileNotFoundException {

        File keystorePath = new File(KEYSTORE_PATH);

        File[] files = keystorePath.listFiles();
        for (File file : files) {
            String name = file.getName();
            String address = name.substring(name.lastIndexOf("--") + 2, name.lastIndexOf("."));
            if (tagetAddress.equals(address)) {
                ObjectMapper mapper = new ObjectMapper();

                try {
                    WalletFile walletFile = mapper.readValue(file, WalletFile.class);
                    return Credentials.create(Wallet.decrypt(DEFAULTKEY, walletFile));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        throw new FileNotFoundException("not found keystore(keystore 못 찾겠어.)");
    }

    public static String signedTransactionData(String from, String to, String nonce, String gasPrice, String gasLimit, String value) throws FileNotFoundException {
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                new BigInteger(nonce),
                new BigInteger(gasPrice),
                new BigInteger(gasLimit),
                to,
                new BigInteger(value));

        Credentials credentials = KeyStoreUtils.getCredentials(from);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Numeric.toHexString(signedMessage);

    }

    public static File getKeyStorePathFile() {
        File file = new File(KEYSTORE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        Log.e("files", file.getAbsolutePath());
        return file;
    }
}
