package Fragments;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.edward.dwarkawala.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Adapters.ConnectionDetector;
import Adapters.PostListAdapter;
import Adapters.ViewPagerAdapter;
import Models.Banners;
import Models.Response;
import Models.Thumbnail;
import Retrofit.DwarkawalaApi;
import retrofit2.Call;
import retrofit2.Callback;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FeedsFragment extends Fragment {

    private static final String TAG = FeedsFragment.class.getSimpleName();
    public static List<Response> postList;
    public static List<Thumbnail> thumbnailList;
    Context mContext;
    RecyclerView imageRecyclerView;
    ImageView noConnectionGif;
    RelativeLayout loadingLayout,connectionLost;
    PostListAdapter postListAdapter;
    boolean isLoading = false;
    static int page = 1;
    private NestedScrollView nestedScrollView;
    RecyclerView.LayoutManager mLayoutManager;
    private Parcelable mListState;
    Animation slide_down,slide_up;
    ConnectionDetector networkState;
    SwipeRefreshLayout swipeLayout;
    ViewPager viewPager;

    static List<Banners> bannersList;

    DatabaseReference banners;

    FirebaseLoadDone firebaseLoadDone;

    //ImageSlider Dots
    LinearLayout dotsSlider;
    private int dotsCount;
    private ImageView[] dotImages;

    Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feeds,container,false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getActivity();
        imageRecyclerView = (RecyclerView) view.findViewById(R.id.images_recycler_view);
        connectionLost = (RelativeLayout) view.findViewById(R.id.noConnectionLayoutID);
        loadingLayout = (RelativeLayout) view.findViewById(R.id.loadingRecyclerID);
        noConnectionGif = (ImageView) view.findViewById(R.id.no_connectionImageID);
        networkState = new ConnectionDetector(mContext);
        swipeLayout = view.findViewById(R.id.swipeRefreshID);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);


        //dots slider
        dotsSlider = (LinearLayout) view.findViewById(R.id.sliderDots);




        viewPager =  (ViewPager)view.findViewById(R.id.viewPager);

        viewPager.setPageTransformer(true,new DepthPageTransformer());
       // ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity());
        //viewPager.setAdapter(viewPagerAdapter);

        //Firebase for banners
        banners = FirebaseDatabase.getInstance().getReference("Banners");


        generateRecyclerView();


        loadBanner();

        // Adding Listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // refresh layouts
                //Toast.makeText(mContext, "Works!", Toast.LENGTH_LONG).show();
                connectionLost.setVisibility(View.INVISIBLE);
                postListAdapter.clearList();
                imageRecyclerView.setVisibility(View.VISIBLE);
                DownloadPosts();
                generateRecyclerView();
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);
                    }
                }, 4000); // Delay in millis
            }
        });

        // Scheme colors for animation
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        //Load animation
        slide_down = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);




        // Load images on app run
        DownloadPosts();


        //code to fetch more data for endless scrolling
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {

//                         Check if end of page has been reached
                        if( !isLoading && ((LinearLayoutManager)mLayoutManager).findLastVisibleItemPosition() == postListAdapter.getItemCount()-1 ){
                            Log.d(TAG , "End has reached, loading more images!");
                            isLoading = true;
                            loadingLayout.startAnimation(slide_up);
                            loadingLayout.setVisibility(View.VISIBLE);
                            page++;
                            DownloadPosts();
                        }


                    }
                }
            }
        });

        // Load more images onScroll end
//        imageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                // Check if end of page has been reached
//                if( !isLoading && ((LinearLayoutManager)mLayoutManager).findLastVisibleItemPosition() == postListAdapter.getItemCount()-1 ){
//                    isLoading = true;
//                    Log.d(TAG , "End has reached, loading more images!");
//                    loadingLayout.startAnimation(slide_up);
//                    loadingLayout.setVisibility(View.VISIBLE);
//                    page++;
//                    DownloadPosts();
//                }
//            }
//        });

        Timer myTimerTask = new Timer();
        myTimerTask.scheduleAtFixedRate(new MyTimerTask(),4000,4000);


    }

    public class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                        if (viewPager.getCurrentItem() == 0)
                        {
                            viewPager.setCurrentItem(1);
                        }
                        else if(viewPager.getCurrentItem()==1)
                        {
                            viewPager.setCurrentItem(2);
                        }
                        else if(viewPager.getCurrentItem()==2)
                        {
                            viewPager.setCurrentItem(0);
                        }


                }
            });
        }
    }


    public void generateRecyclerView(){
        // Set up RecyclerView
        postList = new ArrayList<Models.Response>();
        mLayoutManager = new GridLayoutManager(mContext, 1);
        postListAdapter = new PostListAdapter(mContext);
        imageRecyclerView.setLayoutManager(mLayoutManager);
        imageRecyclerView.setItemAnimator(new DefaultItemAnimator());
        imageRecyclerView.setAdapter(postListAdapter);
    }


    public void loadBanner()
    {
        banners.addListenerForSingleValueEvent(new ValueEventListener() {

            List<Banners> bannersList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot bannersnapshot:dataSnapshot.getChildren()) {
                    Banners bn = bannersnapshot.getValue(Banners.class);
                    bannersList.add(bn);

                    Log.d(TAG, "onDataChange: "+bn.getImage());
                }
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity(),bannersList);
                viewPager.setAdapter(viewPagerAdapter);
                dotsCount = viewPagerAdapter.getCount();
                dotImages = new ImageView[dotsCount];
                for (int i=0; i<dotsCount; i++)
                {
                    dotImages[i] = new ImageView(getActivity());
                    dotImages[i].setImageDrawable(getActivity().getDrawable(R.drawable.nonactive_dot));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                    ,LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8,8,8,8);
                    dotsSlider.addView(dotImages[i], params);
                }
                dotImages[0].setImageDrawable(getActivity().getDrawable(R.drawable.active_dot));

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                        for(int i = 0; i< dotsCount; i++){
                            dotImages[i].setImageDrawable(getActivity().getDrawable( R.drawable.nonactive_dot));
                        }

                        dotImages[position].setImageDrawable(getActivity().getDrawable( R.drawable.active_dot));

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });


                Log.d(TAG, "onDataChange: "+bannersList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    firebaseLoadDone.OnFirebaseFiled(databaseError.getMessage());
            }
        });
    }

    public void DownloadPosts() {

        DwarkawalaApi.Factory.getInstance().getPosts("post",page).enqueue(new Callback<List<Response>>() {
            @Override
            public void onResponse(Call<List<Response>> call, retrofit2.Response<List<Response>> response) {

                List<Response> newResponse = response.body();

                postListAdapter.addPosts(newResponse);
                isLoading = false;
                loadingLayout.setVisibility(View.INVISIBLE);
                loadingLayout.startAnimation(slide_down);

            }

            @Override
            public void onFailure(Call<List<Response>> call, Throwable t) {

                Log.e(TAG, "Failed " + t.getMessage());
                isLoading = false;
                // reduce page by 1 as page failed to load
                page--;
                imageRecyclerView.setVisibility(View.GONE);

                Glide.with(mContext)
                        .asGif()
                        .load(R.drawable.no_connection)
                        .into(noConnectionGif);
                connectionLost.setVisibility(View.VISIBLE);

            }
        });
}


}
