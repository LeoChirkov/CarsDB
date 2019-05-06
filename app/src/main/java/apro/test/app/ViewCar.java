package apro.test.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.lang.reflect.Field;

public class ViewCar extends OrmLiteBaseActivity<DatabaseHelper> {

    TextView titleTV;
    ImageView imageView;
    TextView priceTV;
    TextView yearTV;
    TextView mileageTV;
    TextView engTypeTV;
    TextView engDispTV;
    TextView transTypeTV;
    TextView colorTV;
    TextView bodyTypeTV;
    Button buttonEdit;

    public static Integer pos = 0;
    public static Integer resId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.full_specs);

        //get data from db
        Bundle bundle = getIntent().getExtras();

        pos = bundle.getInt("position");
        RuntimeExceptionDao<CarData, Integer> carsDao = getHelper().getCarsRuntimeExceptionDao();
        CarData carData = carsDao.queryForId(pos);

        imageView = findViewById(R.id.car_img);

//        get id or uri of image for ImageView
        String name = carData.img;
        if(name.contains("/")) {
            Bitmap bm = BitmapFactory.decodeFile(name);
            imageView.setImageBitmap(bm);
        } else {
            resId = getResId(name, R.drawable.class);
            imageView.setImageResource(resId);
        }


        Log.d("carData: ", carData.toString());

        titleTV = findViewById(R.id.title_make_model);
        titleTV.setText(carData.make + " " +carData.model + ", " + carData.year.toString());

        priceTV = findViewById(R.id.car_price);
        priceTV.setText(carData.price.toString() + " BYN");

        yearTV = findViewById(R.id.view_year);
        yearTV.setText(carData.year.toString());

        mileageTV = findViewById(R.id.view_mileage);
        mileageTV.setText(carData.mileage.toString() + "km");

        engTypeTV = findViewById(R.id.view_eng_type);
        engTypeTV.setText(carData.engType);

        engDispTV = findViewById(R.id.view_eng_disp);
        engDispTV.setText(carData.engDisp.toString() + "cm3");

        transTypeTV = findViewById(R.id.view_trans_type);
        transTypeTV.setText(carData.transType);

        colorTV = findViewById(R.id.view_color);
        colorTV.setText(carData.color);

        bodyTypeTV = findViewById(R.id.view_body);
        bodyTypeTV.setText(carData.bodyType);

        buttonEdit = findViewById(R.id.edit_btn);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewCar.this, EditCar.class);

                Bundle extras = new Bundle();
                extras.putInt("position", pos);
                extras.putInt("resId", resId);
                intent.putExtras(extras);

                startActivity(intent);
            }
        });



    }

//    get id of the image in drawable folder by it's name
    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
