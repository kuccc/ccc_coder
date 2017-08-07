package org.ccccoder.rapbattle.Fragment;

import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import org.ccccoder.rapbattle.Adapter.ItemAdapter;
import org.ccccoder.rapbattle.Model.Record;
import org.ccccoder.rapbattle.Model.Title;
import org.ccccoder.rapbattle.R;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class TitleListFragment extends Fragment {
    ItemAdapter mAdapter;
    //List<ItemModel> models;
    RealmResults<Title> models;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private Realm realm;  //DataBase
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_titlelist, container, false);
        mFab = (FloatingActionButton)view.findViewById(R.id.fab);
        realm = Realm.getDefaultInstance();
        //preset();
        RealmResults<Title> temp = realm.where(Title.class).findAll();
        if(temp.size() <4) preset();
        models = realm.where(Title.class).findAll();
        /*RecycleView 설정*/
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mAdapter = new ItemAdapter(getActivity().getApplicationContext(), models, null, null);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return view;

    }

    private void preset(){
        Title new_item = new Title();
        new_item.setName("자작나무");
        realm.beginTransaction();
        realm.copyToRealm(new_item);
        realm.commitTransaction();

        Title new_item1 = new Title();
        new_item1.setName("더위");
        realm.beginTransaction();
        realm.copyToRealm(new_item1);
        realm.commitTransaction();

        Title new_item2 = new Title();
        new_item2.setName("고카톤");
        realm.beginTransaction();
        realm.copyToRealm(new_item2);
        realm.commitTransaction();

        Title new_item3 = new Title();
        new_item3.setName("탄산수");
        realm.beginTransaction();
        realm.copyToRealm(new_item3);
        realm.commitTransaction();

    }

}
