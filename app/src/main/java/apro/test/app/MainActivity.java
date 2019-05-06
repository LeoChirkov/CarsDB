package apro.test.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private List<CarData> carData;
    private HashMap<String, Integer> resIdMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        // get data from db
        RuntimeExceptionDao<CarData, Integer> carsDao = getHelper().getCarsRuntimeExceptionDao();
        carData = carsDao.queryForAll();

        // get map of values for images in /drawable folder for future use in CarItemAdapter
        resIdMap = getResIdMap(carData);
        Log.d("Generated map: ", resIdMap.toString());

        ListView listView = (ListView) this.findViewById(R.id.carsListView);
        CarItemAdapter itemAdapter = new CarItemAdapter(this, R.layout.list_item, carData, resIdMap);
        listView.setAdapter(itemAdapter);

        listView.setOnItemClickListener(onItemClickListener);

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, ViewCar.class);
            intent.putExtra("position", position);
            startActivity(intent);
        }
    };

    // get a HashMap for images in drawable folder, we pass the name of image and get Resource Id,
    // which will be used to set image to ImageView
    private static HashMap<String, Integer> getResIdMap(List<CarData> cars) {
        HashMap<String, Integer> map = new HashMap<>();
        int dbSize = cars.size();
        String imgName;
        Integer id;

        for (int i = 0; i < dbSize; i++) {
            imgName = cars.get(i).img;
            id = getResId(imgName, R.drawable.class);
            map.put(imgName, id);
        }

        return map;
    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // add menu to action bar and handle events
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_entry:
                Intent intent = new Intent(MainActivity.this, EditCar.class);
                startActivity(intent);

                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}
