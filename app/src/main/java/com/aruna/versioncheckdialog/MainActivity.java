package com.aruna.versioncheckdialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getBaseContext();
    }

    private void checkVersion() {
        VersionChecker versionChecker = new VersionChecker();
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
            String gradleVersion = pInfo.versionName;

            String playStoreVersion = versionChecker.execute().get();
            Log.e("Version_Code", playStoreVersion);

            if (!(gradleVersion.equalsIgnoreCase(playStoreVersion))) {
                showVersionCheckDialog();
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showVersionCheckDialog(){

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        // Add the buttons
        builder.setTitle("Update Check");
        builder.setMessage("A New version of Application is Available for your best experience");
        builder.setPositiveButton("Update Now", new android.content.DialogInterface.OnClickListener() {
            public void onClick(android.content.DialogInterface dialog, int id) {
                // User clicked OK button

                final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });
        builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // Set other dialog properties

        // Create the AlertDialog
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}
