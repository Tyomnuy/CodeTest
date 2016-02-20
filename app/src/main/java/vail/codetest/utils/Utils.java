package vail.codetest.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by
 *
 * @author Evgen Marinin <imilin@yandex.ru>
 * @since 20.02.16.
 */
public class Utils {

    public static String loadStringFromAsset(Context context, String assetName) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            AssetManager assetManager = context.getAssets();
            is = new BufferedInputStream(assetManager.open(assetName));

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (Exception exc) {
            Log.e("FileUtils", "copyFromAssetsToData error", exc);
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ex) {
            }
        }

        return os.toString();
    }

}
