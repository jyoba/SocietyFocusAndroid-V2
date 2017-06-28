package com.zircon.app.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zircon.app.R;
import com.zircon.app.model.request.UploadImage;
import com.zircon.app.model.response.UploadImageResponse;
import com.zircon.app.utils.AuthCallbackImpl;
import com.zircon.app.utils.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by jyotishman on 28/06/17.
 */

public class ImageUploadWidget extends LinearLayout {

    private static final String APP_PICTURE_DIRECTORY = "sf";
    private static final int REQUEST_CODE_IMAGE = 47467;
    private static final String TAG = ImageUploadWidget.class.getSimpleName();

    private ImageButton addButton;
    private LinearLayout changeSyncLayout;
    private ImageButton changeButton;
    private ImageButton syncButton;
    private ImageView imageView;
    private LinearLayout syncProgressLayout;
    private boolean isImageSet;
    private STATE state;

    ;
    private boolean isActivityStarted;
    private UploadImageResponse imageUploadResponse;
    private Uri imageUri;
    private boolean isImageSynced;
    public ImageUploadWidget(Context context) {
        super(context);
        init();
    }

    public ImageUploadWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageUploadWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public boolean isImageSet() {
        return isImageSet;
    }

    public UploadImageResponse getImageUploadResponse() {
        return imageUploadResponse;
    }

    public boolean isImageSynced() {
        return isImageSynced;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!isActivityStarted)
            return;
        isActivityStarted = false;
        if (state != STATE.NEW)
            return;

        switch (requestCode) {
            case REQUEST_CODE_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        Log.d(TAG, "Data Uri from file : " + data.getData());
                        imageUri = data.getData();
                        setState(STATE.IMAGE_SET);
                    } else if (imageUri != null) {
                        setState(STATE.IMAGE_SET);
                        Log.d(TAG, "Data from custom file : " + imageUri);
                    } else {
                        Toast.makeText(getContext(), "Not able to access your storage", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void init() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_image_upload, this, false);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        addView(view);

        addButton = (ImageButton) view.findViewById(R.id.ib_plus);
        imageView = (ImageView) view.findViewById(R.id.iv_complaint_image);
        addButton = (ImageButton) view.findViewById(R.id.ib_plus);
        changeSyncLayout = (LinearLayout) view.findViewById(R.id.ll_change_sync);
        changeButton = (ImageButton) view.findViewById(R.id.ib_change);
        syncButton = (ImageButton) view.findViewById(R.id.ib_sync);
        syncProgressLayout = (LinearLayout) view.findViewById(R.id.ll_progress);

        setState(STATE.NEW);


        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setState(STATE.NEW);
                openImageSelectionScreen();
            }
        });

        changeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setState(STATE.NEW);
                openImageSelectionScreen();
            }
        });

        syncButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setState(STATE.IMAGE_SYNCING);
                UploadImage image = new UploadImage("data:image/jpeg;base64," + encodeImage(), "filename.jpg");

                Call<UploadImageResponse> call = HTTP.getAPI().uploadimage(image);

                call.enqueue(new AuthCallbackImpl<UploadImageResponse>(getContext()) {

//                    http://www.societyfocus.com/society/images/otA7WGHSep5341576141283219.png

                    @Override
                    public void apiSuccess(Response<UploadImageResponse> response) {
                        if (response.body().getBody() != null) {
                            imageUploadResponse = response.body();
                            setState(STATE.IMAGE_SYNCED);
                        } else {
                            apiFail(null);
                        }
                    }

                    @Override
                    public void apiFail(Throwable t) {
                        setState(STATE.IMAGE_SYNC_FAIL);
                        Toast.makeText(getContext(), "Image could not be loaded successfully!!\n Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void setState(STATE state) {
        this.state = state;
        addButton.setVisibility(GONE);
        changeSyncLayout.setVisibility(GONE);
        syncProgressLayout.setVisibility(GONE);
        switch (state) {
            case NEW:
                addButton.setVisibility(VISIBLE);
                isActivityStarted = false;
                isImageSynced = false;
                isImageSet = false;
                imageUri = null;
                imageView.setImageDrawable(null);
                imageUploadResponse = null;
                break;
            case IMAGE_SET:
            case IMAGE_SYNC_FAIL:
                isImageSet = true;
                imageView.setImageURI(imageUri);
                changeSyncLayout.setVisibility(VISIBLE);
                break;
            case IMAGE_SYNCING:
                syncProgressLayout.setVisibility(VISIBLE);
                break;
            case IMAGE_SYNCED:
                isImageSynced = true;
                break;
        }
    }

    private void openImageSelectionScreen() {
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            Toast toast = Toast.makeText(getContext(), "There was a problem saving the photo...", Toast.LENGTH_SHORT);
            toast.show();
        }
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Continue only if the File was successfully created
        if (photoFile != null) {
            imageUri = Uri.fromFile(photoFile);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }

        Intent i = new Intent();//ACTION_GET_CONTENT);
        i.addCategory(android.content.Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        i.setAction(android.content.Intent.ACTION_GET_CONTENT);
        Intent chooserIntent = android.content.Intent.createChooser(i, "Choose an action");

        // Set camera intent to file chooser
        chooserIntent.putExtra(android.content.Intent.EXTRA_INITIAL_INTENTS
                , new Parcelable[]{captureIntent});


        // On select image call onActivityResult method of activity
        isActivityStarted = true;
        ((Activity) getContext()).startActivityForResult(chooserIntent, REQUEST_CODE_IMAGE);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File publicDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File storageDir = new File(publicDir, APP_PICTURE_DIRECTORY);
        if (storageDir.exists() || storageDir.mkdirs())
            return File.createTempFile(imageFileName, ".jpg", storageDir);
        else
            return null;
    }

    private String encodeImage() {
        InputStream imageStream = null;
        try {
            imageStream = getContext().getContentResolver().openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        Bitmap bm = BitmapFactory.decodeStream(imageStream);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private enum STATE {NEW, IMAGE_SET, IMAGE_SYNCING, IMAGE_SYNCED, IMAGE_SYNC_FAIL}
}
