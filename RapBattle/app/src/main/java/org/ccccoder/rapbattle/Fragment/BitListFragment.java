package org.ccccoder.rapbattle.Fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ccccoder.rapbattle.Adapter.ItemAdapter;
import org.ccccoder.rapbattle.Model.Bit;
import org.ccccoder.rapbattle.Model.Bit;
import org.ccccoder.rapbattle.R;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.ContentValues.TAG;

public class BitListFragment extends Fragment {
    ItemAdapter mAdapter;
    //List<ItemModel> models;
    RealmResults<Bit> models;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private Realm realm;  //DataBase
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_bitlist, container, false);
        realm = Realm.getDefaultInstance();
        RealmResults<Bit> temp = realm.where(Bit.class).findAll();
        if(temp.size()<6) preset();
        models = realm.where(Bit.class).findAll();
        Log.e(TAG,"모델크기는"+models.size());
        /*RecycleView 설정*/
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mAdapter = new ItemAdapter(getActivity().getApplicationContext(), null, null,models);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        return view;

    }

    private void preset(){
        long temp = System.currentTimeMillis();
        Bit new_item = new Bit();
        new_item.setBit_id(temp);
        new_item.setTitleName("angry");
        realm.beginTransaction();
        realm.copyToRealm(new_item);
        realm.commitTransaction();

        Bit new_item1 = new Bit();
        new_item1.setBit_id(temp + 10000);
        new_item1.setTitleName("chacha");
        realm.beginTransaction();
        realm.copyToRealm(new_item1);
        realm.commitTransaction();

        Bit new_item2 = new Bit();
        new_item2.setBit_id(temp+20000);
        new_item2.setTitleName("funky");
        realm.beginTransaction();
        realm.copyToRealm(new_item2);
        realm.commitTransaction();

        Bit new_item3 = new Bit();
        new_item3.setBit_id(temp+30000);
        new_item3.setTitleName("happy");
        realm.beginTransaction();
        realm.copyToRealm(new_item3);
        realm.commitTransaction();


        Bit new_item4 = new Bit();
        new_item4.setBit_id(temp + 40000);
        new_item4.setTitleName("lonely");
        realm.beginTransaction();
        realm.copyToRealm(new_item4);
        realm.commitTransaction();

        Bit new_item5 = new Bit();
        new_item5.setBit_id(temp+50000);
        new_item5.setTitleName("lovely");
        realm.beginTransaction();
        realm.copyToRealm(new_item5);
        realm.commitTransaction();

        Bit new_item6 = new Bit();
        new_item6.setBit_id(temp+60000);
        new_item6.setTitleName("thinking");
        realm.beginTransaction();
        realm.copyToRealm(new_item6);
        realm.commitTransaction();

    }

}
