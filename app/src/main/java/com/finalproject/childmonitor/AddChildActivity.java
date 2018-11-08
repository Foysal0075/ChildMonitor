package com.finalproject.childmonitor;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.finalproject.childmonitor.ObjectClass.ChildKey;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class AddChildActivity extends AppCompatActivity {

    private String childLinkKey;
    private EditText name, age;
    private Button qrGenerator,doneButton;
    private ImageView qrView;
    View alertView;

   private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        alertView = findViewById(R.id.qr_alert);
        alertView.setVisibility(View.INVISIBLE);
        name = findViewById(R.id.child_name_et);
        age = findViewById(R.id.age_et);
        qrGenerator = findViewById(R.id.qr_generator_btn);
        qrView = findViewById(R.id.qr_view);
        doneButton = findViewById(R.id.done_btn);

        user = FirebaseAuth.getInstance().getCurrentUser();

        childLinkKey = ChildKey.getToken()+ " "+ user.getUid();

        qrGenerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQrCode(childLinkKey);
                alertView.setVisibility(View.VISIBLE);

            }
        });






    }

    public void generateQrCode(String string){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(string,BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder encoder = new BarcodeEncoder();
           Bitmap bitmap =  encoder.createBitmap(bitMatrix);
            qrView.setImageBitmap(bitmap);

        }catch (WriterException e){
            e.printStackTrace();
        }

    }


}
