package apro.test.app;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


// run once to create a config file ormlite_config.txt
public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    private static final Class<?>[] classes = new Class[]{CarData.class};

    public static void main (String[] args) throws SQLException, IOException {
        writeConfigFile(new File("/home/leo/AndroidStudioProjects/CarsDB/app/src/main/res/raw/ormlite_config.txt"), classes);
    }
}
