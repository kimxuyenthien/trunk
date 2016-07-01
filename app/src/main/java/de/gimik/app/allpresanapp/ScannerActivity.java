package de.gimik.app.allpresanapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.zxing.Result;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.database.EanDbAdapter;
import de.gimik.app.allpresanapp.database.ProductDbAdapter;
import de.gimik.app.allpresanapp.database.ProductGroupDbAdapter;
import de.gimik.app.allpresanapp.model.Ean;
import de.gimik.app.allpresanapp.model.Product;
import de.gimik.app.allpresanapp.model.ProductGroup;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import java.util.List;

/**
 * Created by gimik on 5/12/16.
 */
public class ScannerActivity extends BaseActivity implements ZXingScannerView.ResultHandler {
    private static final int PERMISSIONS_CALL_PHONE = 100;

    private ZXingScannerView mScannerView;
    private AlertDialog dialog;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.




        mScannerView.startCamera();          // Start camera on resume

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("", rawResult.getText()); // Prints scan results
        String scanContent = rawResult.getText();
        Log.v("", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        Bundle bundle = new Bundle();

        if (scanContent != null) {
            //we have a result
            EanDbAdapter eanDbAdapter = new EanDbAdapter(this);
            List<Ean> eans = eanDbAdapter.queryByName(scanContent);
            if (eans != null && eans.size() > 0) {
                Ean ean = eans.get(0);
                ProductDbAdapter productDbAdapter = new ProductDbAdapter(this);
                Product product = productDbAdapter.queryForId(ean.getProductId());
                if (product != null) {
                    bundle.putLong(Constant.PRODUCT_GROUP_ID, product.getProductGroupId());
                    ProductGroupDbAdapter productGroupDbAdapter = new ProductGroupDbAdapter(this);
                    ProductGroup productGroup = productGroupDbAdapter.queryForId(product.getProductGroupId());
                    bundle.putString(Constant.PRODUCT_GROUP_NAME, productGroup.getName());
                    bundle.putLong(Constant.PRODUCT_ID, product.getId());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    getErrorDialog().show();
                }
            } else {
                getErrorDialog().show();
            }

        } else {
            Toast toast = Toast.makeText(this, R.string.error_scan, Toast.LENGTH_SHORT);
            toast.show();
        }
        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }

    private AlertDialog getErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScannerActivity.this);
        builder.setTitle(R.string.error_title_scan);
        builder.setMessage(R.string.error_content_scan);
        builder.setPositiveButton(R.string.close,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setNegativeButton(R.string.call_phone, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //finish();
                if (ActivityCompat.checkSelfPermission(ScannerActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ScannerActivity.this,
                            android.Manifest.permission.CALL_PHONE)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(ScannerActivity.this,
                                new String[]{android.Manifest.permission.CALL_PHONE},
                                PERMISSIONS_CALL_PHONE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
                else{
                    call();
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    call();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }


    }


    private void call(){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "01805255773"));
        startActivity(intent);
    }


}


