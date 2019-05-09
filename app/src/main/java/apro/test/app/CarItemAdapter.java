package apro.test.app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

// adapter class which configures and returns View for ListView
public class CarItemAdapter extends ArrayAdapter<CarData> {
    private Activity myContext;
    private List<CarData> cars;
    private HashMap<String, Integer> resIdMap;
    private Bitmap bitmap;


    public CarItemAdapter(Context context, int textViewResourceId, List<CarData> objects,
                          HashMap<String, Integer> map) {
        super(context, textViewResourceId, objects);
        myContext = (Activity) context;
        cars = objects;
        resIdMap = map;
    }

    static class ViewHolder {
        ImageView carThumbImg;
        TextView carMakeModel;
        TextView carDesc;
        TextView carPrice;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        //avoid inflating layout each time for each item (car)
        if (convertView == null) {
            LayoutInflater inflater = myContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.carThumbImg = (ImageView) convertView.findViewById(R.id.thumbnail);
            viewHolder.carMakeModel = (TextView) convertView.findViewById(R.id.firstLine);
            viewHolder.carDesc = (TextView) convertView.findViewById(R.id.secondLine);
            viewHolder.carPrice = (TextView) convertView.findViewById(R.id.thirdLine);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // if there's no image to show - set default icon

        if (cars.get(position).img == null) {
            viewHolder.carThumbImg.setImageResource(R.drawable.ic_directions_car_black_24dp);
        } else {

            String key = cars.get(position).img;

            if(key.contains("/")) {
                // create small scaled thumbnail from file instead of using full-sized image
                bitmap = decodeSampledBitmapFromFile(key, 100, 100);
                viewHolder.carThumbImg.setImageBitmap(bitmap);
            } else {
                final int id = resIdMap.get(key);
                // create small scaled thumbnail from drawable folder instead of using full-sized image
                bitmap = decodeSampledBitmapFromResource(myContext.getResources(), id,
                        100, 100);
                viewHolder.carThumbImg.setImageBitmap(bitmap);
            }
        }

        //generate short specs of car to show on second line in list_item layout
        String shortSpecs = cars.get(position).year.toString() + ", " +
                cars.get(position).transType + ", " +
                cars.get(position).engDisp.toString() + "cm3, " +
                cars.get(position).engType + ", " +
                cars.get(position).bodyType + ", " +
                cars.get(position).mileage.toString() + "km";

        //assign data for each line in list_item layout
        viewHolder.carMakeModel.setText(cars.get(position).make + " " + cars.get(position).model);
        viewHolder.carDesc.setText(shortSpecs);
        viewHolder.carPrice.setText(cars.get(position).price.toString() + " BYN");

        return convertView;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
}
