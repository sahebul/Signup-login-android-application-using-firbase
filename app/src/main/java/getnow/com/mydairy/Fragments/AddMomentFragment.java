package getnow.com.mydairy.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import getnow.com.mydairy.R;

public class AddMomentFragment extends Fragment {
    public static AddMomentFragment newInstance() {
        AddMomentFragment addMomentFragment = new AddMomentFragment();
        return addMomentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_moment_fragment, container, false);
    }
}
