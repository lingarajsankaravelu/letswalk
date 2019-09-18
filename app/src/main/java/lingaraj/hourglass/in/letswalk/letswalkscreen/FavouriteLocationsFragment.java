package lingaraj.hourglass.in.letswalk.letswalkscreen;

import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.gson.Gson;
import javax.inject.Inject;
import lingaraj.hourglass.in.letswalk.di.BaseViewModelFactory;
import lingaraj.hourglass.in.letswalk.di.injectors.Injector;
import lingaraj.hourglass.in.ridegmap.R;
import lingaraj.hourglass.in.ridegmap.databinding.FragmentFavouriteLocationsBinding;

public class FavouriteLocationsFragment extends Fragment {

  @Inject BaseViewModelFactory view_model_factory;
  FragmentFavouriteLocationsBinding binding;
  private LetsWalkViewModel view_model;
  private final String TAG = "FAVLocationFrags";
  private Context mContext;
  private AddressAdapter mAdapter;



  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
    Injector.inject(this);
    view_model = view_model_factory.create(LetsWalkViewModel.class);

  }

  private void setLiveDataObservers() {

    view_model.getDecodedAddress().observe(this, new Observer<Address>() {
      @Override
      public void onChanged(Address address) {
        Log.d(TAG,"Address Recieved:\n"+new Gson().toJson(address));
        mAdapter.addLocation(address);
      }
    });
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_locations,container,false);
    return binding.getRoot();
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    binding.locationList.setHasFixedSize(true);
    binding.locationList.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
    mAdapter = new AddressAdapter(mContext,new FavouriteClick());
    binding.locationList.setAdapter(mAdapter);
    setLiveDataObservers();
  }

  public class FavouriteClick implements View.OnClickListener{

    @Override public void onClick(View v) {
      int position = binding.locationList.getChildAdapterPosition(v);
      mAdapter.notifySelection(position);

    }
  }
}
