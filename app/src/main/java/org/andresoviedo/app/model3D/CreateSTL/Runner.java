package org.andresoviedo.app.model3D.CreateSTL;

import android.util.Log;

//import java.awt.Button;
//import java.awt.Label;


//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JSlider;

public class Runner //extends JFrame
{

    private static final long serialVersionUID = 1L;
    public static final int WIDTH = 800, HEIGHT = 800;
    /* private static Button selectImage1, selectImage2, writeOutput;
     private static Button updateImage1, updateImage2, overwrite;
     private static Button resetOrbit, smooth;
     private static JSlider sliderX, sliderY, sliderZ;*/
    public static InputFile inputFile1, inputFile2;

    /**
     * static을 지우기 위해 Runner의 Model myModel을 지움
     *
     * 다시 static 선언했음. 지워도 별반 차이가 없어서
     */
    public static Model myModel = new Model();


    /*
    public static void main(String[] args) {
        Runner myRunner = new Runner();
        myRunner.setDefaultCloseOperation(EXIT_ON_CLOSE);
        myRunner.setVisible(true);*//* //ui 신경쓸것 따로 없으니 주석 처리하고  Runner는 oncreate에서 할당해주자
    }*/



    public Runner(String st) {
//        this.setSize(WIDTH, HEIGHT);
//        this.setResizable(false);
//        this.setLayout(null);
//        selectImage1 = new Button("Select");
//        selectImage2 = new Button("Select");
//        writeOutput = new Button("Write Output");
//        updateImage1 = new Button("Update");
//        updateImage2 = new Button("Update");
//        overwrite = new Button("Overwrite Output");
//        resetOrbit = new Button("Reset Orbit");
//        smooth = new Button("Smooth");
//
//        sliderX = new JSlider(JSlider.HORIZONTAL, 1, 120, 60);
//        sliderY = new JSlider(JSlider.HORIZONTAL, 1, 120, 60);
//        sliderZ = new JSlider(JSlider.HORIZONTAL, 1, 120, 60);
//        sliderX.setName("silderX");
//        sliderY.setName("silderY");
//        sliderZ.setName("silderZ");
//
//        Label temp = new Label("Scale X");
//        temp.setBounds(30,10,75,20);
//        this.add(temp);
//        temp = new Label("Scale Y");
//        temp.setBounds(30,35,75,20);
//        this.add(temp);
//        temp = new Label("Scale Z");
//        temp.setBounds(30,60,75,20);
//
//        this.add(temp);
//        sliderX.setBounds(100, 10, 600, 20);
//        this.add(sliderX);
//        sliderY.setBounds(100, 35, 600, 20);
//        this.add(sliderY);
//        sliderZ.setBounds(95, 60, 610, 45);
//        Hashtable<Integer,JLabel> labels = new Hashtable<Integer,JLabel>();
//        labels.put(1, new JLabel(".1"));
//        for(int i=1; i<13; i++){
//            labels.put(i*10, new JLabel(""+i));
//        }
//        sliderZ.setLabelTable(labels);
//        sliderZ.setPaintLabels(true);
//        sliderZ.setMajorTickSpacing(10);
//        sliderZ.setMinorTickSpacing(2);
//        sliderZ.setPaintTicks(true);
//        this.add(sliderZ);
//
//        temp = new Label("Top Image");
//        temp.setBounds(100, 100, 100, 20);
//        this.add(temp);
//        selectImage1.setBounds(10, 120, 120, 20);
//        updateImage1.setBounds(135, 120, 125, 20);
        /**
         * inputFile1 = new InputFile(img);
         */

        inputFile1 = new InputFile(st);

        //inputFile1.setBounds(10, 150, 250, 250);
//        this.add(inputFile1);
//        selectImage1.addActionListener(inputFile1);
//        updateImage1.addActionListener(inputFile1);
//
//
//        temp = new Label("Bottom Image");
//        temp.setBounds(100, 420, 100, 20);
//        this.add(temp);
//        selectImage2.setBounds(10, 440, 120, 20);
//        updateImage2.setBounds(135, 440, 125, 20);
        inputFile2 = new InputFile();

//        inputFile2.setBounds(10, 470, 250, 250);
//        this.add(inputFile2);
//        selectImage2.addActionListener(inputFile2);
//        updateImage2.addActionListener(inputFile2);
//
//
//        writeOutput.setBounds(270, 120, 125, 20);
//        overwrite.setBounds(395, 120, 125, 20);
//        smooth.setBounds(520,120,125,20);
//        resetOrbit.setBounds(645,120, 125, 20);
//        myModel = new Model();
//        myModel.setBounds(270, 150, 500, 500);
//        writeOutput.addActionListener(myModel);
//        overwrite.addActionListener(myModel);
//        resetOrbit.addActionListener(myModel);
//        smooth.addActionListener(myModel);
//        sliderX.addChangeListener(myModel);
//        sliderY.addChangeListener(myModel);
//        sliderZ.addChangeListener(myModel);
//        this.add(myModel);
//
//
//
//
//
//
//        selectImage2.setEnabled(false);
//        writeOutput.setEnabled(false);
//        updateImage1.setEnabled(false);
//        updateImage2.setEnabled(false);
//        overwrite.setEnabled(false);
//        smooth.setEnabled(false);
//        this.add(selectImage1);
//        this.add(selectImage2);
//        this.add(writeOutput);
//        this.add(updateImage1);
//        this.add(updateImage2);
//        this.add(overwrite);
//        this.add(resetOrbit);
//        this.add(smooth);






    }




    //아미지 바뀌었을때마다 호출되는 함수 지금은 필요없으니 주석하자

    public static void update() {
        boolean hasImageOne = inputFile1.hasImage();
        Log.e("Runner의 update()", "hasImageone 값: " + hasImageOne);
        /*if (!hasImageOne){
            inputFile2.setImageNull();
            myModel.clearRendering();
        }
        boolean hasImageTwo = inputFile2.hasImage();

        //버튼 안눌리게 처리하는 것인듯
selectImage2.setEnabled(hasImageOne);
        writeOutput.setEnabled(hasImageOne);
        updateImage1.setEnabled(hasImageOne);
        smooth.setEnabled(hasImageOne);

        updateImage2.setEnabled(hasImageTwo);
        overwrite.setEnabled(myModel.hasPath());


        if (hasImageOne && hasImageTwo
                && (  inputFile1.getImageHeight() != inputFile2.getImageHeight()
                || inputFile1.getImageWidth() != inputFile2.getImageWidth())) {
            Toast.makeText(this, "이미지는 다른 사이즈를 가졌어요. 최종출력물은 예측하기 어려워요.. 뭔 소리야??");

JOptionPane.showMessageDialog(null,
                    "Images have different sizes. Final output may be hard to predict.");


        }*/
        /*if (hasImageOne){
            myModel.recompute();
            Log.e("Runner의 update", "myModel.recompute()호출 직후");
        }*/
    }
}

