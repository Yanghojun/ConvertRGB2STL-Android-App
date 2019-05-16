package org.andresoviedo.app.model3D.CreateSTL;

import android.graphics.Bitmap;

public class Global {
    public static Bitmap globalImg;

    //연산시간이 너무 오래 걸리니 static으로 model을 처리해보자
    public static Model globalModel = new Model();
}
