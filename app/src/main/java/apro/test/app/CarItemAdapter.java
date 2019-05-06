package apro.test.app;

import android.app.Activity;
import android.content.Context;
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
        ViewHolder viewHolder;

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
                Bitmap bitmap = BitmapFactory.decodeFile(key);
                viewHolder.carThumbImg.setImageBitmap(bitmap);
            } else {
                int id = resIdMap.get(key);
                viewHolder.carThumbImg.setImageResource(id);
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


}
