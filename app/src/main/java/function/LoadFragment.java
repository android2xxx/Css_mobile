package function;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.microtecweb.css_mobile.R;

/**
 * Created by HieuHT on 04/03/2015.
 */
public class LoadFragment {
    FragmentManager fragmentManager;
    public LoadFragment(FragmentManager fragmentManager)
    {
        this.fragmentManager = fragmentManager;
    }

    public void initializeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
