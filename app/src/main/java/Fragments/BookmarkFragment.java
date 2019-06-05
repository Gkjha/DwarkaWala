package Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.edward.dwarkawala.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Adapters.BookmarksAdapter;
import Adapters.DealsAdapter;
import Models.MerchantData;

public class BookmarkFragment extends Fragment {


    private static final String TAG = BookmarkFragment.class.getSimpleName();
    private static final String FIREBASE_DATABASE_LOCATION = "Merchants";
    DatabaseReference recyclerDatabaseReference,dealsDatabase;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    BookmarksAdapter imageListAdaper;
    Context mContext;


    public static List<MerchantData> merchantDataList;
    MerchantData merchantData;

    RecyclerView imageRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<String> bookmarkList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmark,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mContext = getActivity();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recyclerDatabaseReference = FirebaseDatabase.getInstance().getReference("Bookmarks");
        dealsDatabase = FirebaseDatabase.getInstance().getReference("Merchants");

        imageRecyclerView = (RecyclerView) view.findViewById(R.id.mainRecyclerId);

        FetchIds();
    }


    public void FetchIds(){

        recyclerDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    if (postSnapshot.getKey().equals(mAuth.getCurrentUser().getUid())){

                        for (DataSnapshot marksSnapshot : postSnapshot.getChildren()){

                            Log.d("id",marksSnapshot.getKey());
                            bookmarkList.add(marksSnapshot.getKey());


                        }
                        FetchData();

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void FetchData(){

        Query query;
        query = FirebaseDatabase.getInstance().getReference()
                .child(FIREBASE_DATABASE_LOCATION)
                .orderByKey();
        query.keepSynced(true);



        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                merchantDataList = new ArrayList<>();
                merchantDataList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    merchantData = postSnapshot.getValue(MerchantData.class);

                    for (int i = 0; i<bookmarkList.size() ; i++){

                        if (Objects.requireNonNull(merchantData).getId().equals(bookmarkList.get(i))){
                            merchantDataList.add(merchantData);
                        }

                    }


                }
                initRecycler();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                //loadingLayout.setVisibility(View.INVISIBLE);
                //loadingLayout.startAnimation(slide_down);
                Log.d(TAG,databaseError.getDetails());

            }
        });




    }


    public void initRecycler(){

        // Set up RecyclerView
        imageListAdaper = new BookmarksAdapter(mContext,merchantDataList);
        mLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        imageRecyclerView.setLayoutManager(mLayoutManager);
        imageRecyclerView.setItemAnimator(new DefaultItemAnimator());
        imageRecyclerView.setAdapter(imageListAdaper);

    }
}
