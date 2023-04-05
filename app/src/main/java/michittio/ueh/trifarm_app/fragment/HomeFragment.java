package michittio.ueh.trifarm_app.fragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import michittio.ueh.trifarm_app.ExpandableGridView;
import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.SliderAdapter;
import michittio.ueh.trifarm_app.SliderItem;
import michittio.ueh.trifarm_app.data.Product;
import michittio.ueh.trifarm_app.data.ProductAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
    private ViewPager2 viewPager2;
    private final Handler sliderHandler = new Handler();
    private LinearLayout linearLayoutFragmentSearch;
    private Context thiscontext;
    private GridView gridView;
    private ArrayList<Product> productArrayList;
    private ProductAdapter adapter;


    public HomeFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        viewPager2 = getView().findViewById(R.id.slider);
        List<SliderItem> sliderItems = getListSlider();
//        viewPager2.setCurrentItem(2);
        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });

        initui();
        renderData();
        nextFragmentSearch();

    }

    private void renderData() {
        ExpandableGridView productGrid = (ExpandableGridView) getView().findViewById(R.id.gridView);
        productGrid.setExpanded(true);
    }


    private void nextFragmentSearch() {
        //next fragment search
        linearLayoutFragmentSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
                // Hiển thị icon search trên BottomNavigationView
                bottomNavigationView.setSelectedItemId(R.id.search);
                //Chuyển fragment page
                replaceFragment(new SearchFragment());
            }
        });
    }


    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void initui() {
        gridView = getView().findViewById(R.id.gridView);
        productArrayList = new ArrayList<>();
        linearLayoutFragmentSearch = getView().findViewById(R.id.linearLayout);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        thiscontext = container.getContext();
        gridView = rootView.findViewById(R.id.gridView);
        productArrayList = new ArrayList<>();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    productArrayList.add(product);
                }
                adapter = new ProductAdapter(thiscontext, productArrayList);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);



    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000); // Slide duration 3 seconds
    }

    private List<SliderItem> getListSlider () {
        List<SliderItem> sliderItems = new ArrayList<>();

        sliderItems.add(new SliderItem(R.drawable.banner1));
        sliderItems.add(new SliderItem(R.drawable.banner2));
        sliderItems.add(new SliderItem(R.drawable.banner3));
        sliderItems.add(new SliderItem(R.drawable.banner4));

        return sliderItems;
    }
}
