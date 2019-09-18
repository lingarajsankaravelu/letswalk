package lingaraj.hourglass.in.letswalk.letswalkscreen.di;

import androidx.fragment.app.Fragment;
import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;
import lingaraj.hourglass.in.letswalk.letswalkscreen.FavouriteLocationsFragment;

@Module(subcomponents = {
    FavouriteLocationsComponent.class

})
public abstract class MainScreenBindingModule {

    @Binds
    @IntoMap
    @FragmentKey(FavouriteLocationsFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindSplashController(FavouriteLocationsComponent.Builder builder);



}

