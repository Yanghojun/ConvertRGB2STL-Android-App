package org.andresoviedo.app.model3D;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import org.andresoviedo.app.model3D.CreateSTL.Global;
import org.andresoviedo.app.model3D.CreateSTL.Runner;
import org.andresoviedo.dddmodel2.R;

public class PopUpActivity extends Activity {

    Button btConfirm;
    Button btCancel;
    EditText etFileName;
    createSTL STL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        STL = new createSTL();
        btConfirm = (Button)findViewById(R.id.btConfirm);
        btCancel = (Button)findViewById(R.id.btCancel);
        etFileName = (EditText)findViewById(R.id.etFileName);

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STL.execute();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class createSTL extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(PopUpActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("추출중입니다..");
            asyncDialog.setCancelable(false);

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Runner.myModel.writeOutput(etFileName.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println("Done!!");
            asyncDialog.dismiss();
            Global.createSuccess = true;
            finish();
        }

    }


}

