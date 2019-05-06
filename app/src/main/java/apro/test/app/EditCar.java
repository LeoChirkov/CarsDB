package apro.test.app;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static apro.test.app.ViewCar.resId;

public class EditCar extends OrmLiteBaseActivity<DatabaseHelper> {

    private int GALLERY = 1;
    private static final String IMAGE_DIRECTORY = "/data/data/apro.test.app/databases/img";
    private static String filePath = "";

    TextView makeTV;
    TextView modelTV;
    ImageView imageView;
    TextView priceTV;
    TextView yearTV;
    TextView mileageTV;
    TextView engTypeTV;
    TextView engDispTV;
    TextView transTypeTV;
    TextView colorTV;
    TextView bodyTypeTV;
    Button editSaveBtn;
    Button editCancelBtn;
    Button addImgBtn;

    CarData car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);

        makeTV = findViewById(R.id.edit_make);
        modelTV = findViewById(R.id.edit_model);
        priceTV = findViewById(R.id.edit_price);
        yearTV = findViewById(R.id.edit_year);
        mileageTV = findViewById(R.id.edit_mileage);
        engTypeTV = findViewById(R.id.edit_eng_type);
        engDispTV = findViewById(R.id.edit_eng_disp);
        transTypeTV = findViewById(R.id.edit_trans_type);
        colorTV = findViewById(R.id.edit_color);
        bodyTypeTV = findViewById(R.id.edit_body);
        imageView = findViewById(R.id.edit_img);

        addImgBtn = findViewById(R.id.btn_add_img);
        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhotoFromGallary();
            }
        });

        editCancelBtn = findViewById(R.id.btn_edit_cancel);
        editCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editSaveBtn = findViewById(R.id.btn_edit_save);

        Bundle bundle = getIntent().getExtras();


//        check whether we should edit existing car or create new

//        if bundle is not empty - fill all fields with existing data
        if (bundle != null) {
            final RuntimeExceptionDao<CarData, Integer> carData = getHelper().getCarsRuntimeExceptionDao();

            car = carData.queryForId(bundle.getInt("position"));
            resId = bundle.getInt("resId");

            makeTV.setText(car.make);
            modelTV.setText(car.model);
            yearTV.setText(car.year.toString());
            mileageTV.setText(car.mileage.toString());
            engTypeTV.setText(car.engType);
            engDispTV.setText(car.engDisp.toString());
            transTypeTV.setText(car.transType);
            bodyTypeTV.setText(car.bodyType);
            priceTV.setText(car.price.toString());
            colorTV.setText(car.color);

//            database holds filepath for new cars and short names for images in /drawable folder
//            so we should check what kind of data there is and use Bitmap or Id for imageView
            if(car.img.contains("/")) {
                Bitmap bm = BitmapFactory.decodeFile(car.img);
                imageView.setImageBitmap(bm);
            } else {
                imageView.setImageResource(resId);
            }


            editSaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        car.make = makeTV.getText().toString();
                        car.model = modelTV.getText().toString();
                        String yearToInt = yearTV.getText().toString();

//                    this field is set to num, so we won't get actual string instead of number
//                    however this operation may throw NumberFormatException, therefore we use try/catch
                        car.year = Integer.parseInt(yearToInt);

                        String mileageToInt = mileageTV.getText().toString();
                        car.mileage = Integer.parseInt(mileageToInt);

                        car.engType = engTypeTV.getText().toString();

                        String engDispToInt = engDispTV.getText().toString();
                        car.engDisp = Integer.parseInt(engDispToInt);

                        car.transType = transTypeTV.getText().toString();
                        car.bodyType = bodyTypeTV.getText().toString();

                        String priceToInt = priceTV.getText().toString();
                        car.price = Integer.parseInt(priceToInt);

                        car.color = colorTV.getText().toString();


//                  store new data to db and open first screen with full list of cars
                        carData.update(car);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Please, fill in all the fields",
                                Toast.LENGTH_LONG).show();
                    }

                Intent intent = new Intent(EditCar.this, MainActivity.class);
                startActivity(intent);


                    Toast.makeText(getApplicationContext(), "The entry has been saved",
                            Toast.LENGTH_LONG).show();
                }
            });
        } else {

            final RuntimeExceptionDao<CarData, Integer> carData = getHelper().getCarsRuntimeExceptionDao();

            editSaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String make = makeTV.getText().toString();
                        String model = modelTV.getText().toString();
                        String yearToInt = yearTV.getText().toString();
                        Integer year = Integer.parseInt(yearToInt);
                        String mileageToInt = mileageTV.getText().toString();
                        Integer mileage = Integer.parseInt(mileageToInt);
                        String engType = engTypeTV.getText().toString();
                        String engDispToInt = engDispTV.getText().toString();
                        Integer engDisp = Integer.parseInt(engDispToInt);
                        String transType = transTypeTV.getText().toString();
                        String bodyType = bodyTypeTV.getText().toString();
                        String priceToInt = priceTV.getText().toString();
                        Integer price = Integer.parseInt(priceToInt);
                        String color = colorTV.getText().toString();

                        carData.create(new CarData(make, model, year, mileage, engType, engDisp, transType,
                                bodyType, price, color, filePath));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Please, fill in all the fields",
                                Toast.LENGTH_LONG).show();
                    }

                    Intent intent = new Intent(EditCar.this, MainActivity.class);
                    startActivity(intent);
                }
            });

        }


    }



//    pick existing image from gallery
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }


//    when result is received - store image in imageView and save it in data folder for future use
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    filePath = saveImage(bitmap);
                    Toast.makeText(EditCar.this, "Image Saved!" , Toast.LENGTH_SHORT).show();
                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(EditCar.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File file = new File(IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            File f = new File(file, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
}
