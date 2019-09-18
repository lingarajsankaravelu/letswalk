package lingaraj.hourglass.in.letswalk.letswalkscreen.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import lingaraj.hourglass.in.letswalk.di.ActivityScope;
import lingaraj.hourglass.in.letswalk.letswalkscreen.LetsWalkActivity;

@ActivityScope
@Subcomponent(modules = {
        MainScreenBindingModule.class,

})
public interface LetsWalkActivityComponent extends AndroidInjector<LetsWalkActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<LetsWalkActivity> {

        @Override
        public void seedInstance(LetsWalkActivity instance) {

        }
    }
}
