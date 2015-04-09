package function;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.microtecweb.css_mobile.R;

/**
 * Created by HieuHT on 04/03/2015.
 */
public class LoadFragment {
    FragmentManager fragmentManager;
    public static final String PACKAGE_ID = "PACKAGE_ID";

    public LoadFragment(FragmentManager fragmentManager)
    {
        this.fragmentManager = fragmentManager;
    }

    public void initializeFragment(Fragment fragment, int id) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(id != 0) {
            Bundle arguments = new Bundle();
            arguments.putInt(PACKAGE_ID, id);
            fragment.setArguments(arguments);
        }
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
