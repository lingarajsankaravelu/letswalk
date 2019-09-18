package lingaraj.hourglass.in.letswalk.di.modules;

import android.app.Activity;
import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import lingaraj.hourglass.in.letswalk.letswalkscreen.LetsWalkActivity;
import lingaraj.hourglass.in.letswalk.letswalkscreen.di.LetsWalkActivityComponent;

@Module(subcomponents = {
    LetsWalkActivityComponent.class
})
public abstract class ActivityBindingModule {

    @Binds
    @IntoMap
    @ActivityKey(LetsWalkActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> provideMainActivityInjector(LetsWalkActivityComponent.Builder builder);


}

