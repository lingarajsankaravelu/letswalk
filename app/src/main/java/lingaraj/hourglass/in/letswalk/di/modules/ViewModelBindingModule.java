package lingaraj.hourglass.in.letswalk.di.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import lingaraj.hourglass.in.letswalk.di.BaseViewModelFactory;
import lingaraj.hourglass.in.letswalk.di.ViewModelKey;
import lingaraj.hourglass.in.letswalk.letswalkscreen.LetsWalkViewModel;

@Module
public abstract class ViewModelBindingModule {

  @Binds
  @IntoMap
  @ViewModelKey(LetsWalkViewModel.class)
  abstract ViewModel providesLetsWalkViewModel(LetsWalkViewModel letsWalkViewModel);

  @Binds
  abstract ViewModelProvider.Factory bindsViewModelFactory(BaseViewModelFactory baseViewModelFactory);

}
