package vikivy.me.myfileproviderpro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	private static final int REQUEST_CODE_TAKE_PHOTO = 1;

	File mCurrentPhotoFile;
	Uri mCurrentPhotoUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dispatchTakePictureIntent();
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Continue only if the File was successfully created
			if (photoFile != null) {
				mCurrentPhotoUri = FileProvider.getUriForFile(this, "vikivy.me.myfileproviderpro", photoFile);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
				takePictureIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION); // grant write permission
				startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
			}
		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		@SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File imagesDir = new File(getExternalFilesDir(null), "images");
//		File imagesDir = new File(getFilesDir(), "images");
		if (!imagesDir.exists()) {
			boolean result = imagesDir.mkdir();
		}
		mCurrentPhotoFile = new File(imagesDir, imageFileName + ".jpg");
		return mCurrentPhotoFile;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);

		Log.d(TAG, "requestCode: " + requestCode + "resultCode: " + resultCode);

		if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
			if (resultCode == RESULT_OK) {
				Intent viewPictureIntent = new Intent(Intent.ACTION_VIEW);
				viewPictureIntent.setData(mCurrentPhotoUri);
				viewPictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // grant read permission
				startActivity(viewPictureIntent);
			}
		}
	}
}
