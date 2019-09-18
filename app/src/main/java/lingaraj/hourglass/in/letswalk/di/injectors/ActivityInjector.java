package lingaraj.hourglass.in.letswalk.di.injectors;

import android.app.Activity;
import android.content.Context;
import dagger.android.AndroidInjector;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;
import lingaraj.hourglass.in.letswalk.BaseApplication;

public class ActivityInjector {

    private final Map<Class<? extends Activity>, Provider<AndroidInjector.Factory<? extends Activity>>> activityInjectors;
    private final Map<String, AndroidInjector<? extends Activity>> cache = new HashMap<>();
    private final String instanceId = "1";

    @Inject
    ActivityInjector(Map<Class<? extends Activity>, Provider<AndroidInjector.Factory<? extends Activity>>> activityInjectors) {

        this.activityInjectors = activityInjectors;
    }

    void inject(Activity activity) {
        if(cache.containsKey(instanceId)) {
            ((AndroidInjector<Activity>) cache.get(instanceId)).inject(activity);
            return;
        }

        AndroidInjector.Factory<Activity> injectorFactory = (AndroidInjector.Factory<Activity>) activityInjectors.get(activity.getClass()).get();
        AndroidInjector<Activity> injector = injectorFactory.create(activity);
        cache.put(instanceId, injector);
        injector.inject(activity);
    }

    void clear(Activity activity) {
        cache.remove(instanceId);
    }

    static ActivityInjector get(Context context) {
        return ((BaseApplication) context.getApplicationContext()).getActivityInjector();
    }
}
