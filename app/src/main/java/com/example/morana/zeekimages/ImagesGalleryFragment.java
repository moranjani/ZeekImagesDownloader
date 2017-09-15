package com.example.morana.zeekimages;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Morana on 9/15/2017.
 */
public class ImagesGalleryFragment extends Fragment {

    private static final int SPAN_COUNT = 2;
    private RecyclerView mRecyclerView;
    private ImagesAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.images_fragment, container, false);

        mRecyclerView = view.findViewById(R.id.images_recycler_view);
        mAdapter = new ImagesAdapter();
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }


}
