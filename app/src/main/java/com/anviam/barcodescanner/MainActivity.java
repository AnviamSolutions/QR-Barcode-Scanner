package com.anviam.barcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.anviam.scanner.AnviamScannerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


public class MainActivity extends AppCompatActivity implements AnviamScannerView.ResultHandler {

    private AnviamScannerView mScannerView;
    LinearLayoutCompat searchQRBottomSheet;
    BottomSheetBehavior searchQRBehaviour;
    AppCompatTextView TextsearchQRInfo;
    AppCompatTextView TxtComment,FlashSetting;
    SwitchCompat textSwitchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RunnableCameraPermission();
        InitializationView();
        onClickListener();
        ScannerSetup();
    }

    private void RunnableCameraPermission(){
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);

    }

    private void InitializationView() {
        TxtComment = findViewById(R.id.TxtComment);
        textSwitchCompat = findViewById(R.id.textSwitchCompat);
        FlashSetting = findViewById(R.id.FlashSetting);
        textSwitchCompat.setOnCheckedChangeListener((compoundButton, b) -> {
            if (mScannerView!=null){
                mScannerView.setFlash(b);
                if (b){
                    FlashSetting.setText("Flash ON");
                }else {
                    FlashSetting.setText("Flash OFF");
                }
            }
        });
        SearchQRBottomSheetInitialization();
    }

    private void onClickListener() {
        TxtComment.setOnClickListener(view -> {
            Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.anviam.com/"));
            startActivity(intent);
        });
    }

    private void ScannerSetup() {
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new AnviamScannerView(this);
        contentFrame.addView(mScannerView);
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to camera", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void handleResult(com.google.zxing.Result rawResult) {
        if (rawResult!=null && !TextUtils.isEmpty(rawResult.getText())){
            TextsearchQRInfo.setText(rawResult.getText());
            SearchQRBehaviorVisibility(true);
        }
    }

    private void SearchQRBottomSheetInitialization() {
        searchQRBottomSheet = findViewById(R.id.searchQRBottomSheet);
        searchQRBehaviour = BottomSheetBehavior.from(searchQRBottomSheet);
        TextsearchQRInfo = searchQRBottomSheet.findViewById(R.id.TextsearchQRInfo);
        searchQRBottomSheet.findViewById(R.id.searchQRBackArrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchQRBehaviorVisibility(false);
            }
        });
        searchQRBottomSheet.findViewById(R.id.BtnSearchQRInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url;
                if(Patterns.WEB_URL.matcher(TextsearchQRInfo.getText().toString()).matches()) {
                    url = TextsearchQRInfo.getText().toString();
                }else {
                    url = "http://www.google.com/#q=" + TextsearchQRInfo.getText().toString();
                }
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                SearchQRBehaviorVisibility(false);
            }
        });

        searchQRBottomSheet.findViewById(R.id.BtnCopyQRInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", TextsearchQRInfo.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                SearchQRBehaviorVisibility(false);
            }
        });

        searchQRBottomSheet.findViewById(R.id.BtnShareQRInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareAppInfo(TextsearchQRInfo.getText().toString());
                SearchQRBehaviorVisibility(false);
            }
        });

        searchQRBehaviour.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    searchQRBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    private void ShareAppInfo(String shareContent) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareContent);
        startActivity(Intent.createChooser(sharingIntent, "Share "));
    }
    boolean isShowing = false;
    private void SearchQRBehaviorVisibility(boolean b) {
        isShowing= b;
        if (b) {
            searchQRBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        else {
            searchQRBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mScannerView.resumeCameraPreview(MainActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        if (searchQRBehaviour!=null && isShowing){
            SearchQRBehaviorVisibility(false);
        }else
            super.onBackPressed();
    }
}