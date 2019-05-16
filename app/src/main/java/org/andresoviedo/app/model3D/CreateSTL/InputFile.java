package org.andresoviedo.app.model3D.CreateSTL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

//import java.awt.Button;
//import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.image.BufferedImage;

//import javax.imageio.ImageIO;
//import javax.swing.JFileChooser;
//import javax.swing.JPanel;
//import javax.swing.filechooser.FileNameExtensionFilter;

public class InputFile //extends JPanel implements ActionListener
{

    private static final long serialVersionUID = 1L;

    //Bitmap 으로 변경! private BufferedImage myImage;
    //public static String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/dog.jpg";

    String path; //Path를 지역함수로 선언하자.. inputfile 2번째꺼 null 반환하게 하기 위해서
    //public static Bitmap myImage = BitmapFactory.decodeFile(path);
    private Bitmap myImage = null;
    /**
     * Precondition: myImage has been initialized
     *
     * @return width of myImage
     */

    InputFile(Bitmap mainImg){
        myImage = mainImg;
    }

    InputFile(String pt){
        path = pt;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 16;
        Bitmap src = BitmapFactory.decodeFile(path, options);
        myImage = src;
        //myImage = Bitmap.createScaledBitmap(src, 800, 800, true);
        //myImage = BitmapFactory.decodeFile(path);
    }

    InputFile(){
        path = null;
    }


    public int getImageWidth() {

        if (myImage != null)
            return myImage.getWidth();

        return 0;
    }

    /**
     * Precondition: myImage has been initialized
     *
     * @return height of myImage
     */
    public int getImageHeight() {
        if (myImage != null)
            return myImage.getHeight();

        return 0;
    }

    public void setImageNull() {
        myImage = null;
    }

    //마지막에 그림 그려주는 함수인듯 이거 일단 빼보자
    /*public void paintComponent(Graphics g) {
        if (myImage == null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        } else {
            g.drawImage(myImage, 0, 0, this.getWidth(), this.getHeight(), 0, 0,
                    myImage.getWidth(), myImage.getHeight(), null);
        }
    }*/

    /**
     * @return true if myImage has been initialized
     */
    public boolean hasImage() {
        return myImage != null;
    }

    /**
     * @param x
     *            x-coordinate into myImage
     * @param y
     *            y-coordinate into myImage
     * @return Vertex with x & y as passed in and z determined by the
     *         corresponding pixel of myImage z==0 whenever x or y are out of
     *         bounds
     */
    public Vertex getVertex(int x, int y) {
        if (myImage != null && x >= 0 && x < myImage.getWidth() && y >= 0
                && y < myImage.getHeight()) {



//            Color c = new Color(myImage.getRGB(x, y));
//            int val = c.getRed() + c.getGreen() + c.getBlue();   밑에 5줄로 변경

            int pixel = myImage.getPixel(x,y);
            int redval = Color.red(pixel);
            int blueval = Color.blue(pixel);
            int greenval = Color.green(pixel);
            int val = redval + blueval + greenval;

            // make the range of z roughly the same as the range of x and y
            return new Vertex(x, y, (myImage.getWidth() - 1
                    + myImage.getHeight() - 1)
                    / 2 * (val / (255.0 * 3.0)));
        } else {
            return new Vertex(x, y, 0.0);
        }
    }

    // 다이얼로그 여는 부분이라 필요 없을 듯
//    public void openLoadDialog() {
//        JFileChooser fd = new JFileChooser();
//        fd.setFileFilter(new FileNameExtensionFilter("Images", "jpg",
//                "gif", "png", "bmp", "jpeg"));
//        fd.showOpenDialog(this);
//        if (fd.getSelectedFile() != null) {
//            path = fd.getSelectedFile().getPath();
//            loadFile();
//        }else{
//            myImage = null;
//        }
//
//    }


    public void loadFile() {
        try {
            //myImage = ImageIO.read(new File(path));
            //myImage = BitmapFactory.decodeFile(path);
            Log.e("InputFile", "loadFile()의 path: "+path);
        } catch (Exception e) {
            Log.e("InputFile", "에러구문: "+ e);
            myImage = null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     * Open file dialog to load image files
     */


    //이게 뭐하는 걸까.
    /*public void actionPerformed(ActionEvent arg0) {
        Button b = (Button) arg0.getSource();
        if (b.getLabel().contains("Select")) {
            openLoadDialog();
        } else if (b.getLabel().contains("Update")) {
            loadFile();
        }
        this.getParent().repaint();
        Runner.update();
        // TODO add different actions for select and update
    }*/
}

