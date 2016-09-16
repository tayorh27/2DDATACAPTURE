package com.datacapture.a2ddatacapture;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    ImageView iv;
    Spinner spinner1, spinner2, spinner3;
    General general;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iv = (ImageView) findViewById(R.id.imageView);
        spinner1 = (Spinner) findViewById(R.id.age);
        spinner2 = (Spinner) findViewById(R.id.gender);
        spinner3 = (Spinner) findViewById(R.id.geo);
        general = new General(HomeActivity.this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = iv.getTag().toString();
                int stext1 = spinner1.getSelectedItemPosition();
                int stext2 = spinner2.getSelectedItemPosition();
                int stext3 = spinner3.getSelectedItemPosition();
                //Modifying Zones
                if (stext3 <= 6)
                    stext3 = 0;
                else if (stext3 > 6 && stext3 <= 12)
                    stext3 = 1;
                else if (stext3 > 12 && stext3 <= 19)
                    stext3 = 2;
                else if (stext3 > 19 && stext3 <= 25)
                    stext3 = 3;
                else if (stext3 > 25 && stext3 <= 31)
                    stext3 = 4;
                else if (stext3 > 31)
                    stext3 = 5;

                Date dt = Calendar.getInstance().getTime();
                String filename = dt.getDay() + "." + dt.getMonth() + "." + dt.getYear() + "_" + stext1 + "_" + stext2 + "_" + stext3 + ".jpg";

                if (!tag.contentEquals("default")) {
                    general.SaveImage(iv, filename);
                    spinner1.setSelection(0);
                    spinner2.setSelection(0);
                    spinner3.setSelection(0);
                } else {
                    Snackbar.make(view, "Please upload image", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void UploadImage(View view) {
        Crop.pickImage(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    private void handleCrop(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            iv.setImageURI(Crop.getOutput(data));
            iv.setTag("latest");
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(data).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void beginCrop(Uri data) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(data, destination).asSquare().start(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            startActivity(new Intent(HomeActivity.this, AboutActivity.class));
        }
        if (id == R.id.action_zip) {
            ZipAndExport();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ZipAndExport() {
        pd = new ProgressDialog(HomeActivity.this);
        pd.setMessage("Zipping all data...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/db_zipped_images/");
        File file = new File(path, "images.zip");
        ZipManager zipManager = new ZipManager();
        zipManager.zip(general.ListImages(), file.toString());
        pd.dismiss();
        Toast.makeText(HomeActivity.this,"All files zipped.",Toast.LENGTH_LONG).show();
        SendEmailAttachment();
    }

    private void SendEmailAttachment() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/db_zipped_images/");
        File file = new File(path, "images.zip");
    }
}
