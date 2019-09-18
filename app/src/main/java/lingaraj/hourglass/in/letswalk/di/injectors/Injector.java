package lingaraj.hourglass.in.letswalk.di.injectors;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import lingaraj.hourglass.in.letswalk.letswalkscreen.FavouriteLocationsFragment;
import lingaraj.hourglass.in.letswalk.letswalkscreen.LetsWalkActivity;

public class Injector {

    private Injector() {

    }

    public static void inject(Activity activity) {
        ActivityInjector.get(activity).inject(activity);
    }

    public static void clearComponent(Activity activity) {
        ActivityInjector.get(activity).clear(activity);
    }

    public static void inject(Fragment fragment) {
        FragmentInjector.get(fragment.getActivity()).inject(fragment);
    }

    public static void clearComponent(Fragment fragment) {
        FragmentInjector.get(fragment.getActivity()).clear(fragment);
    }
}
