package org.andresoviedo.app.model3D;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.andresoviedo.app.model3D.CreateSTL.Global;
import org.andresoviedo.app.model3D.CreateSTL.Runner;
import org.andresoviedo.app.model3D.view.MenuActivity;
import org.andresoviedo.app.model3D.view.ModelActivity;
import org.andresoviedo.util.android.AndroidURLStreamHandlerFactory;
import org.andresoviedo.dddmodel2.R;
import org.andresoviedo.app.model3D.CreateSTL.Model;

import static org.andresoviedo.app.model3D.CreateSTL.Global.globalImg;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;


/**
 * This is the main android activity. From here we launch the whole stuff.
 * <p>
 * Basically, this activity may serve to show a Splash screen and copy the assets (obj models) from the jar to external
 * directory.
 *
 * @author andresoviedo
 */
public class Create_STL_Activity extends Activity {

    //final static Model myModel = new Model();
    static final int FROMGALLERY = 1;
    Handler mHandler = null;
    private boolean granted = true;
    ImageView ivImage;
    private String path; //Select 버튼을 누르면 가져온 이미지에 대한 경로를 이 path에다가 저장
    Model myModel = new Model();
    Runner run;
    public TextView tvStatus;

    //      /storage/0/ 이 사용가능한지 여부를 반환하는 함수
    public static boolean checkAvailable() {

        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set main layout controls.
        // Basically, this is a screen with the app name just in the middle of the scree
        setContentView(R.layout.activity_create_stl);
        check();

        while (!granted) ;

        mHandler = new Handler();
        tvStatus = (TextView)findViewById(R.id.tvStatus);
        Button btCreate = findViewById(R.id.btCreate);
        Button btSelect = findViewById(R.id.btSelect);
        ivImage = findViewById(R.id.ivImage);

        //여기있던 2줄 주석 처리하고 이벤트 부분에 넣을거야
        //Runner myRunner = new Runner();
        //가져온 사진 값 recompute()실행
        //myModel.recompute();

        //빠르게 실행하고 싶을 땐 이 코드 실행하자
        /*run = new Runner();
        Runner.myModel.recompute();
        Runner.myModel.writeOutput();
        System.out.println("Done!!");*/

        //갤러리 호출해서 이미지 가져오는 이벤트
        btSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.createSuccess = false;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, FROMGALLERY);
            }
        });

        btCreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Create_STL_Activity.this, PopUpActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Global.createSuccess)
            tvStatus.setText("파일추출에 성공하였습니다");
        else
            tvStatus.setText("");

        Log.e("onResume", "onResume");
    }

    public void check() {
        // 동의 0, 비동의 -1
        int check = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (check != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            //save();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                // grantResults.length 사용자의 동의결과 항목 수
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //동의한 경우
                    //save();
                    granted = true;
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //requestCode는 어떤 activityforresult로부터 왔는지 구분하기 위함. 어떤 activityforresult가 request(요청)하였는가!
        if (requestCode == FROMGALLERY) {
            if (resultCode == RESULT_OK)
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    globalImg = BitmapFactory.decodeStream(in);

                    Uri tempUri = getImageUri(getApplicationContext(), globalImg);
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    System.out.println("진짜??:" + getRealPathFromURI(tempUri));
                    path = getRealPathFromURI(tempUri);

                    // 이미지를 상황에 맞게 회전시킨다
                    if(globalImg.getHeight()<globalImg.getWidth()){
                        globalImg = imgRotate(globalImg);
                    }
                    ivImage.setImageBitmap(globalImg);
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }


        //갤러리 이미지 선택
        //new Runner(globalImg);

        //new Runner(Environment.getExternalStorageDirectory().getAbsolutePath()+"/dog.jpg");


        //그 이미지에 관련된 recoumpute 연산 시작
        run = new Runner(path);
        Runner.myModel.recompute();
    }



    private Bitmap imgRotate(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }
}

