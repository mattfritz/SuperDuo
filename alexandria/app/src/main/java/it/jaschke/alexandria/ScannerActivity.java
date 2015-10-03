package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import it.jaschke.alexandria.services.BookService;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private String LOG_TAG = ScannerActivity.class.getSimpleName();
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.EAN_13);

        mScannerView = new ZXingScannerView(this);
        mScannerView.setFormats(formats);

        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        String ean = rawResult.getText();
        Log.v(LOG_TAG, ean);

        // Load book from ean using book service
        Intent bookIntent = new Intent(getApplicationContext(), BookService.class);
        bookIntent.putExtra(BookService.EAN, ean);
        bookIntent.putExtra(BookService.SCANNER, true);
        bookIntent.setAction(BookService.FETCH_BOOK);
        getApplicationContext().startService(bookIntent);

        // Notify user that book is being fetched
        Intent messageIntent = new Intent(MainActivity.MESSAGE_EVENT);
        messageIntent.putExtra(MainActivity.MESSAGE_KEY, getResources().getString(R.string.scan_successful));
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(messageIntent);

        finish();
    }
}
