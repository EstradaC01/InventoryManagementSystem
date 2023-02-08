package com.example.inventorymanagementsystem.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IMSBarcodeScanner extends AppCompatActivity {

    private static final String FILENAME_FORMAT = "dd-M-yyyy hh:mm:ss";
    public static final int REQUEST_CODE_PERMISSIONS = 10;
    public static final String TAG = "CameraXApp";
    private ExecutorService cameraExecutor;
    private ImageCapture imageCapture = null;

    private static final String[] REQUIRED_PERMISSIONS;

    static {
        List<String> requiredPermissions = new ArrayList<>(Arrays.asList(android.Manifest.permission.CAMERA));
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            requiredPermissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        REQUIRED_PERMISSIONS = requiredPermissions.toArray(new String[0]);
    }
    private SurfaceView cameraPreview;
    private BarcodeScannerOptions barcodeOptions;
    private BarcodeScanner scanner;
    private Button scanBtn;
    private TextView tvBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        barcodeOptions =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_UPC_A,
                                Barcode.FORMAT_UPC_E)
                        .build();

        scanner = BarcodeScanning.getClient(barcodeOptions);
        scanBtn = findViewById(R.id.idBtnScanCode);
        tvBarcode = findViewById(R.id.idTVBarcode);

        cameraExecutor = Executors.newSingleThreadExecutor();

        imageCapture = new ImageCapture.Builder().build();

        cameraPreview = findViewById(R.id.cameraView);
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                startCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { }
        });
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
    }
    private void scanBarcodes(InputImage image) {

        // [START run_detector]
        Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        // Task completed successfully
                        // [START_EXCLUDE]
                        // [START get_barcodes]
                        for (Barcode barcode: barcodes) {
                            String rawValue = barcode.getRawValue();
                            tvBarcode.setText(rawValue);
                            barcodes.clear();
                        }
                        // [END get_barcodes]
                        // [END_EXCLUDE]
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                });
        // [END run_detector]
    }
    private void takePhoto() {
        ImageCapture imageCapture = this.imageCapture;
        if (imageCapture == null) return;
        imageCapture.takePicture(Executors.newSingleThreadExecutor(), new ImageCapture.OnImageCapturedCallback() {
            @Override
            @androidx.camera.core.ExperimentalGetImage
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                super.onCaptureSuccess(image);
                Image mediaImage = image.getImage();
                if (mediaImage != null) {
                    InputImage inputImage =
                            InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());
                    scanBarcodes(inputImage);
                }
            }
            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                super.onError(exception);
            }
        });
    }
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider((Preview.SurfaceProvider) cameraPreview);
                try {
                    cameraProvider.unbindAll();
                    cameraProvider.bindToLifecycle(IMSBarcodeScanner.this, cameraSelector, preview,imageCapture);
                } catch (Exception exception) {
                    Log.e(TAG, "Use case binding failed", exception);
                }
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "CameraProviderFuture exception", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}