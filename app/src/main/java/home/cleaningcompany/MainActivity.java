package home.cleaningcompany;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Context context = this;
    Uri photoURI = null;
    Communication communication;
    String token = "token";
    String imageName = "imageName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);

        communication = new Communication(this);
        communication.setupSSLCertificate();
        final Dialog dialog = new Dialog(context);

        SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        if (prefs.contains("token"))
        {
            token = prefs.getString("token","");
        } else{
                createUser();
        }
        //TODO setup your listeners for buttons etc....

        Button captureButton = (Button) findViewById(R.id.captureButton);
        captureButton.setOnClickListener(new View.OnClickListener(){
            static final int REQUEST_IMAGE_CAPTURE = 1;

            @Override
            public void onClick(View v){
                dispatchTakePictureIntent();
            }
        });

        Button submitImage = (Button) findViewById(R.id.uploadButton);

        submitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText caseId = (EditText) findViewById(R.id.caseId);
                String caseIdText = caseId.getText().toString();

                EditText description = (EditText) findViewById(R.id.description);
                String descriptionText = description.getText().toString();

                if(caseIdText.equals("") || caseIdText.isEmpty() || caseIdText.length() == 0 || descriptionText.isEmpty() || descriptionText.equals("") || descriptionText.length() == 0){
                    Toast.makeText(context,"Case ID and description must be provided",Toast.LENGTH_LONG).show();

                }else if(imageBitmap == null){
                    Toast.makeText(context, "You must first take a picture!", Toast.LENGTH_LONG).show();
                }else{
                    imageName += System.currentTimeMillis() + "";
                    communication.uploadPicture(imageBitmap, token, caseIdText, descriptionText, imageName);
                }

            }
        });
    }

    public void createUser() {
        OkCancelInputDialog dialog = new OkCancelInputDialog(this,"Create user","Choose a username")
        {
            @Override
            public void clickCancel() {
                super.clickCancel();
            }

            @Override
            public void clickOk() {
                Toast toast = Toast.makeText(context,"Creating user...please wait",Toast.LENGTH_LONG);
                toast.show();
                //Communication coms = new Communication(context);
                communication.CreateUser(getUserInput(),token+=System.currentTimeMillis());
                super.clickOk();
            }
        };
        dialog.show();


    }



    String mCurrentPhotoPath;
    private File createImageFile() throws IOException{

        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
          imageFileName, ".jpg", storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;

            try{
                photoFile = createImageFile();
            } catch (IOException ex){
                // TODO error handling
            }
            if(photoFile != null){
                photoURI = FileProvider.getUriForFile(this, "home.cleaningcompany.fileprovider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);


                List<ResolveInfo> resolvedIntentActivities = context.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                    String packageName = resolvedIntentInfo.activityInfo.packageName;

                    context.grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }

        }
    }
    Bitmap imageBitmap;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            if(mCurrentPhotoPath != null){
                imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                imageView.setImageBitmap(imageBitmap);
            }

        }

        if(photoURI != null) {
            context.revokeUriPermission(photoURI, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }

}
