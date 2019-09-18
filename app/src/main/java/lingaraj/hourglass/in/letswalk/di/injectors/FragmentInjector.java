package lingaraj.hourglass.in.letswalk.di.injectors;

import android.app.Activity;
import android.util.Log;
import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjector;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Provider;
import lingaraj.hourglass.in.letswalk.di.ActivityScope;
import lingaraj.hourglass.in.letswalk.letswalkscreen.FavouriteLocationsFragment;
import lingaraj.hourglass.in.letswalk.letswalkscreen.LetsWalkActivity;

@ActivityScope
public class FragmentInjector {

    private final String TAG = "Screen Injector";
    private final Map<Class<? extends Fragment>, Provider<AndroidInjector.Factory<? extends Fragment>>> screenInjectors;
    private final Map<String, AndroidInjector<Fragment>> cache = new HashMap<>();
    private final String instanceId = "2";

    @Inject FragmentInjector(Map<Class<? extends Fragment>, Provider<AndroidInjector.Factory<? extends Fragment>>> screenInjectors) {
        this.screenInjectors = screenInjectors;
    }

    void inject(Fragment fragment) {
        if (cache.containsKey(instanceId)) {
            Objects.requireNonNull(cache.get(instanceId)).inject(fragment);
            return;
        }

        //noinspection unchecked
        Log.d(TAG,"Injecting Fragment:" + fragment.getClass());
        AndroidInjector.Factory<Fragment> injectorFactory =
                (AndroidInjector.Factory<Fragment>) Objects.requireNonNull(screenInjectors.get(fragment.getClass())).get();
        if(injectorFactory != null) {
            AndroidInjector<Fragment> injector = injectorFactory.create(fragment);
            cache.put(instanceId, injector);
            injector.inject(fragment);
        }
    }

    void clear(Fragment fragment) {
        cache.remove(instanceId);
    }

  static FragmentInjector get(Activity activity) {
    return ((LetsWalkActivity) activity).getFragmentInjector();
  }


}
