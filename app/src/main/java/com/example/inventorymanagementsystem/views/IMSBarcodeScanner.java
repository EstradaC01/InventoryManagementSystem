package com.example.inventorymanagementsystem.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Size;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
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
    private ImageAnalysis imageAnalysis = null;
    private codeAnalyzer codeAnalyzerObject;
    private static final String[] REQUIRED_PERMISSIONS;

    static {
        List<String> requiredPermissions = new ArrayList<>(Arrays.asList(android.Manifest.permission.CAMERA));
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            requiredPermissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        REQUIRED_PERMISSIONS = requiredPermissions.toArray(new String[0]);
    }
    private PreviewView cameraPreview;
    private BarcodeScannerOptions barcodeOptions;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private BarcodeScanner scanner;
    private String rawValue = "";
    private String mWarehouse = "";
    private TextView tvBarcode;
    private FirebaseFirestore db;
    private Products p = null;
    private Boolean foundCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Barcode Scanner</font>"));

        barcodeOptions =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_UPC_A,
                                Barcode.FORMAT_UPC_E)
                        .build();

        scanner = BarcodeScanning.getClient(barcodeOptions);
        tvBarcode = findViewById(R.id.idTVBarcode);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraExecutor = Executors.newSingleThreadExecutor();

        imageCapture = new ImageCapture.Builder().build();
        imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280,720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        cameraPreview = findViewById(R.id.cameraView);
        codeAnalyzerObject = new codeAnalyzer();
        db = FirebaseFirestore.getInstance();
        Intent i = getIntent();
        mWarehouse = (String) i.getSerializableExtra("Warehouse");

    }
    @androidx.camera.core.ExperimentalGetImage

    private void scanBarcodes(ImageProxy image) {
        Log.d(TAG, "scanning");
        Image mediaImage = image.getImage();
        InputImage inputImage =
                                InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());
        // [START run_detector]
        Task<List<Barcode>> result = scanner.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
            @Override
            public void onSuccess(List<Barcode> barcodes) {
                if (barcodes.isEmpty()) {
                    Log.d(TAG, "does not have codes");

                } else {
                    for (Barcode barcode: barcodes) {
                        Log.d(TAG, "has codes");

                        rawValue = barcode.getRawValue();
                        Log.d(TAG, "Code: " + rawValue);
                        tvBarcode.setText(rawValue);
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Barcode>> task) {
                        image.close();
                        if (rawValue.isEmpty()) {
                            //Toast.makeText(getBaseContext(), "Code is empty.", Toast.LENGTH_SHORT).show();
                        } else {
                            CollectionReference itemsRef = db.collection("Warehouses/"+mWarehouse+"/Products");
                            itemsRef.whereEqualTo("productUpc", rawValue).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (!value.isEmpty()) {
                                        Log.d(TAG, "has upc: " + rawValue);
                                        List<DocumentSnapshot> list = value.getDocuments();
                                        for (DocumentSnapshot d : list) {
                                            p = d.toObject(Products.class);
                                            Log.d(TAG, "has product: " + p.getProductId());
                                            foundCode = true;
                                        }
                                    } else {
                                        Toast.makeText(IMSBarcodeScanner.this, "UPC code not found.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
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
                scanBarcodes(image);

            }
            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                super.onError(exception);
            }
        });
    }
    private void startCamera() {
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "CameraProviderFuture exception", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        imageAnalysis.setAnalyzer(cameraExecutor, codeAnalyzerObject);
        preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageCapture, imageAnalysis);
    }
    public class codeAnalyzer implements ImageAnalysis.Analyzer {
        @Override
        @androidx.camera.core.ExperimentalGetImage
        public void analyze(@NonNull ImageProxy image) {
                if (foundCode) {
                    Log.d(TAG, "has found code in db: " + foundCode);
                    Intent j = new Intent(IMSBarcodeScanner.this, ProductDetails.class);
                    j.putExtra("Object", p);
                    j.putExtra("Warehouse", mWarehouse);
                    finish();
                    startActivity(j);
                }
                else {
                    scanBarcodes(image);
                }
            }
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
    @Override
    public void onStart() {
        super.onStart();
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            finish();
        }
    }
}