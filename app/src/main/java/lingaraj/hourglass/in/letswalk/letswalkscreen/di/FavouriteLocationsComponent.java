package lingaraj.hourglass.in.letswalk.letswalkscreen.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import lingaraj.hourglass.in.letswalk.di.FragmentScope;
import lingaraj.hourglass.in.letswalk.di.components.ScreenComponent;
import lingaraj.hourglass.in.letswalk.di.modules.ScreenModule;
import lingaraj.hourglass.in.letswalk.letswalkscreen.FavouriteLocationsFragment;


@FragmentScope
@Subcomponent(modules ={ ScreenModule.class } )
public interface FavouriteLocationsComponent extends ScreenComponent<FavouriteLocationsFragment> {

  @Subcomponent.Builder
  abstract class Builder extends AndroidInjector.Builder<FavouriteLocationsFragment> {

    @Override
    public void seedInstance(FavouriteLocationsFragment instance) {

    }
  }
}
