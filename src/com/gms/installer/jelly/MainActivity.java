package com.gms.installer.jelly;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        Button btnStep1 = (Button) findViewById(R.id.btn_step1);
        Button btnStep2 = (Button) findViewById(R.id.btn_step2);
        Button btnStep3 = (Button) findViewById(R.id.btn_step3);

        btnStep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installApkFromAssets("GoogleServicesFramework.apk");
            }
        });

        btnStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installApkFromAssets("GoogleLoginService.apk");
            }
        });

        btnStep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installApkFromAssets("Phonesky.apk");
            }
        });
    }

    private void installApkFromAssets(String apkName) {
        try {
            // Open the APK from your assets folder
            InputStream in = getAssets().open(apkName);
            
            // Save it temporarily to the app's internal cache folder
            File tempFile = new File(getCacheDir(), apkName);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            // Make the file readable so the Android system installer can access it
            tempFile.setReadable(true, false);

            // Fire the install intent for Android 4.2.2
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(tempFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
