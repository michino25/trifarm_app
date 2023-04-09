package michittio.ueh.trifarm_app.fragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
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
import michittio.ueh.trifarm_app.MainActivity;
import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.data.Category;
import michittio.ueh.trifarm_app.data.CategoryAdapter;
import michittio.ueh.trifarm_app.data.SliderAdapter;
import michittio.ueh.trifarm_app.data.SliderItem;
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
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
    private ViewPager2 viewPager2;
    private final Handler sliderHandler = new Handler();
    private LinearLayout searchBtn;
    private ImageView cartBtn;
    private Context context;
    private GridView gridView;
    private ArrayList<Product> productArrayList;
    private ProductAdapter adapter;
    private GridView gridViewCategory;


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
        nextFragmentCart();

        CategoryAdapter adapter = new CategoryAdapter(getListCategory(), getActivity().getApplicationContext());
        gridViewCategory.setAdapter(adapter);
        searchCategory();


    }


    private void renderData() {
        ExpandableGridView productGrid = (ExpandableGridView) getView().findViewById(R.id.gridView);
        productGrid.setExpanded(true);
    }


    private void nextFragmentSearch() {
        //next fragment search
        searchBtn = getView().findViewById(R.id.liner_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
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

    private void nextFragmentCart() {

        //next fragment cart
        cartBtn = getView().findViewById(R.id.cartBtn);
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
                // Hiển thị icon search trên BottomNavigationView
                bottomNavigationView.setSelectedItemId(R.id.cart);
                //Chuyển fragment page
                replaceFragment(new CartFragment());
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
        gridViewCategory = getView().findViewById(R.id.gridviewCategory);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ((MainActivity)getActivity()).updateStatusBarColor("#FFFFFF");

        context = container.getContext();
        gridView = rootView.findViewById(R.id.gridView);
        productArrayList = new ArrayList<>();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    productArrayList.add(product);
                }
                adapter = new ProductAdapter(context, productArrayList);
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

    private List<SliderItem> getListSlider() {
        List<SliderItem> sliderItems = new ArrayList<>();

        sliderItems.add(new SliderItem(R.drawable.banner1));
        sliderItems.add(new SliderItem(R.drawable.banner2));
        sliderItems.add(new SliderItem(R.drawable.banner3));
        sliderItems.add(new SliderItem(R.drawable.banner4));

        return sliderItems;
    }

    private  ArrayList<Category> getListCategory() {
        ArrayList<Category> categories = new ArrayList<>();

        categories.add(new Category(1, "Trái cây", "https://lh3.googleusercontent.com/fife/AMPSeme7HMbr_L4-RfgzvnRyxzTsCtn4S4ujrpsRGAGIMtXWBLrWmQCBErza8oxBXMYfPUqQaJ4K7e1KGMdipWTVRpR3iCv11wIbeOuQWRZkQUgXm9O0GsB45-xPXUhDQPAsfL-qErP7wT-a41rjN-NNPfgRcq9lNKLb8Dhbt21xivA9RHJyxFMde67JxWiltHqycP2wSfpmkk4X_rsNbOWryXYGVZ6AKzHh7urVwaIR6am_NbHPVIFONEpqrNVX3XbSMBq-ieCWq8CC1k1AOyzndurCLpoP3RvN9_Gb6ENgmb4pyDXPLb05I0IV5JcK4SVqqnzLWhhSO9iEsdjthI860KfrGqonGY005fvYiM1f43lwFtsAOIbMv8ahDAZWX0HMAgDRGnHgWKxnnCz7CMtCNYDDqdII3leKF7v9kiYk4Lua5JSIaqGLZFNlrBcc0vCJgolvriyEyxCurcZi9NXoJvdA7rdlB-o8u6IX89GCe1_ujcoabXthDI3dvnUtg68oBstPmKO0yPz8Qpx4lPkpUtwOdZ7KIJmQijXaGUx9alhy9WzMKZqpKNXYGhMt0LFFtCGTJmCPDtz8gvni8RNQhE9i1z3fR895MorRUlIoHxP0wndso6RyivPyasfbfzKn5ZQfp4FYKbFel9C0xvbS6_vN1xwPdhw2gBLlLV0rG0urG7hPH8NLixkW-_gttdXnacy9W-63fPpEWVpLTSl5ww19aXx35CuYzhCQX2TfDohnxNcPwK-ZBO7UHzjhOkLJt-OZ4aokaoFEBvqUglqeQDKE3Ns6cOQ475yzsIGNxdxg1y-41Q7v24ZMH9fnk5CQ3kXAIcVlyMfmS6GLKL515YP1K7szx7vWpyv4xh3SC9s7Giwaqs3fYteer7AXpC5qrbJOLF_HSg0aUaYUHR9YtmC-RmTL_Wis7IFc67CR3hzikNu1YXlkSW79P8AtiVcz7W3cOT8u6y0eLvzIjGIeNcD7GINz5ucxtRjU8AUZB1LIU4B_efaybY6APS7SmLowX5rDw-JJ0Sx1__4qhsfDynU9u2KYWqi-rnziG4AGxuJt9Eshivn6DoJmRgRjqEZU4aEviTK9UugRc2ZKMslTtN8E2X3lfLD4iC16F5CeY2g6HMRoHL4qkvZmKHeVvLNyLs6rNAJrH52CR5i0DZnlotjbzwS1ou2awGMwAV0ks9SbmN6O7i08J85PVGqqrapmid5qZpvtDlB17SJOSE0C12HBLCz-XV_W_50tm5pP0lKY2M2bu84-pQO51fwZyni2TZrBGwV-0QRhBZugXLivBYiGT6fpPXgeZSJXF2jNSD6G1hSJ24HsUsvS9U1kTms7y_Mgk-j478LUIYcc6hSuchXz88Y1e3SqCCMcoGm7gzGSEYEDBvjhlrsl_MPnyLZy7RzZssQ9PdoTXoz20QXgyJ-yPr4PWmpVZBQHD6n6ZyevnSlO7RLTFCAT_jto7jiydhF0CdhbWX8AHOQtSrPrWx5QlqrVVOzsmnpL6udwypKZhJwTtejUQ98ZSuKuSZs=w1088-h928"));
        categories.add(new Category(2, "Rau củ", "https://lh3.googleusercontent.com/fife/AMPSemcdem5OTxV7ZgcAqW0fIY_NFJCO288IzOxWERdZzdGKe-hV9cJS5dog5g5cSZ6dtZ8p-iOmTQ4O8kOzka8BATDm7E_Ld0NTyvrife-DmKFQueSZAJKv93oyelsLG-EaIEiMM2BhGBmMMNVFRf7gyBGuOhruu3gUYKxBZj8eCUaYwrINM0am0wYBxy1cx5__lbAw2udXizUmxiJGOPrprn-ORRXJrHBnnbK4_gZw68M6fQt7ANwrmkQgo7kLYlKxKgb_o6Lh4Hve6ePV4P6DjIa0g7I7yexmFoUj81ye2ItzoEt0HoRob6rTeyrRckTVvIiax8JTcCJNzu1ON8P7rcvGUz_h6XkDJX7bAEgeze-hh79PQO2h-FecpO0qj5QbY7-m_UnXYIjlyb9hIQaRrVrygWC5nAZjuLBS6ZwU-5b5NirADHn_WIRPnK2Sef4twq1715XGne-JFiu3UrHJSYSf4Tj6FONODgdVZdhZAcTCSHfoKGEk6OE8wnAVP5EOZFxcE73P5vsVNBoVoHg5GHPzUD7HYaYwHOalBse6mCG6YXe7yzARZCUWzeUcM5C8f70JPeiQN2XY-vE5wK2o2e_Qk73ZZgtw8v_3bvKmAulAMWSXUVjR_Lno1h3g--7C6-se7F7Xx8txiua4zZUe3pfjCG_TFAJRHpQLUVu3-aS39h8P2Ae2AzD6GTOl-9TcQKnaqK_0GSVCyL0GpoA-kbdeb5ZoSJLUXDv2xA1trM5Rsn_l7cmpDczlkZaswTGskdsUsXFl_ncKzFkdZJ8ArHk56E-0F2tXwnNNz0-DL35PyYKPGuCIR8ncmA0NstfGYiC7BU8mdQrWhj3oQTa7ANHtoqfxqh3QDsSqhn5Ra1_j8L3HsER8PCAiHEsAn4pOyysn6SH0rt2IEAjbHpXgF0j8iOlo7yy2DhzbY99Xg14-g5HUoZmAVCslUUDkr6qi3poG-rakshKscoVqR1T0rPt-iD1VOWlqg8HSNhEs1RcxD58RXHh6yFnGBLx-EoJkJYexFsqishOGBKEmFtgMBjDEloQNp-FNgTGfUj6DjW5jL7a530cnWw0yDuFGmiSedeGY4hm2NXzqX9berQ9tuRUb4_6JtQDLUBIle-xlVETKHjTOCr6DKqKxFcbU5kME46WTa4vGsiRUP_mHMx85t_5mk5yqmV-X6SrMEm3VaFYaYlnMpZxX10KKcQyV1SKTuW5iOGXq24olhrw5F9POlxMoAbqwp7B24iiIjuFx3VCl6DcVtoOJn3S5OIS_3wxNQvSruJpIdcIt_UpXd1vqAGXpZBlQmtVYLkhMqrNnkesR_40i5UBYoRcbwIh-0uUKr8xnNaw-VxNJzunLzdfYPR2MwwInxkCHbJ1WqLf6ktA5vCtuzJMWhr0w4QYUISrz7qJ23J9xdo7StJUTxTKXmqjgJYsyimoBSqzHyxBewoBa0tJK-1jKTyh3qQKVX5APowLtujeCWOLcu0kUTkcPq9lH-bja9AbDAVgVCukQ6_bHV4Tr0LthRqty4IHtRxM=w1088-h928"));
        categories.add(new Category(3, "Trà", "https://lh3.googleusercontent.com/fife/AMPSemesJGaJBNaiQYfI65mdxJ-7iSwiJ8ohYHWiA9MVRxL3hGesKIPtmB5wNYiS-zUB3IqKj_CCbCAtHTEB8XPo4r2f5VpX_2u7SaO-5oQNy9eCZBTzsgWp7y5qLBBGpi8lE72EptDDkBW2iI1bnLLWHlhcCyoAh5mL3KSzjB2SUgeDfv2YJeJXPPHIomgmzkN-E709Jbb6FEo7HazOcTIskg7ASB9C2aFY3zXICXhyc8PZxohqaornDGpjDIBUiQFz0x_A-oQTXltINQKrA-I71VJAx_swWW2BEcsLkWy7uRGlzEvlHVfKxBUGcXBg51rMMqKh9MpHeCGmzmCPo1Z5Z7AKRWblg1pWrUswbe9ZxxmWh9Y8SoDY6ndYHenDvVdufjGr8KZ2HQaChpO8xfjBwsmvVpuu2HQo14zi7g4vs2shao013yx9wtEk4yZ8ldeiicY5crIgNR52pqAF1aTU7szzaFWdxfKvFC-SenWFp_0XSe0novJ_tpeXH1IUfBy8N_IkHQc5EJlom3SNhFkQlHPs_hLppfd5Hukbsil838aMH_XR3i2Z5Erglm_qbfeI9e4u4a7EYMqjcn6I2kQEtLItmnSIHiauh5t3uwaNWMqTzspx2SHXpR4LB_owQbVYd7XlHCoSKB-5mu2xQLsx8LFZ7LJqxOGFLVr6yJYlcjB5TIwzuJBrT39QElTxF9VScmDeyfTQ9a89V_YUbfX3zncx0x8TZEmn_UuFgaQMxVZ87brDoPjv5WQbuSzAmbr1sbKa_WO0d5V9eIWRGla2NSb7RMYMDwRgLIUG8MeN_VvEJC4WEjwXBMnSNcwT0roonNM9rTNmbJtbaZ2C6AjsoVPXF7I9fYzYWfaCGOPiMnGGo-aSZ01hcGVCLk0uqL6F_T2CL2o_1kDrsx8EaqnYMpdgsy00V7CFvtSxMRpHfkRMDYJWZO7hGy9e3FWRxD0t2fU4pdTVHr6Gh7BlX7y3MqUXGV89QwzfY87GHvdQbCfRbfN665CP6KefxV6c26SLgUOD50g-ewYMWHH424yvX-_GyQ24q2u_9pxHUjckccJf8Z-yF9fdWPbFsQhxTgKAZx_G-ZUYo0tdUtBf4F9eOeJUr1l_0oiojwCYu3dPhNRCZbYhBLyRU2Q9AZ_P0Sk0FwAxHAYP7euWTr-oAGgDUl-HCCJSoBPKaFWmeBqIpu9dUfAA0xzQXiUYD5WGv3yRsENY7xzT04l8-Nz-Pr7rVgSf4mZqcliJl_DToo_jsXe94p_NZjKS0wcKsuAcW8GbvjuWi0_h0PA0dM1IKX-5QOTbqR8xgGbEz9BRKze13qHOPXmcON9tmcTSFdUAgUwUHBpmHxetyZyzBQNfi3BJwiBoQmmzoYQx4FvOQgrlGCblbFU1uWkMsbOn4u7ggv8_Rm58DBp0gPoTLk1z1kxsWaSvdsI_6t09i345z0Mem13-Jmi3moHYjdjvhl3KqaXPaHreVUcwIbQXgXfWR1bSDvnNLKBuxwtYKcaxTxo0kWOhNw-6Aep2opaeVmigK3c=w1919-h928"));
        categories.add(new Category(4, "Sữa", "https://lh3.googleusercontent.com/fife/AMPSemcGPg5LzBAsNiVLX1BNhuLw9WZPwQyt3HUkNa5TL5f4AunS2Wizfrak7tpWCJIPhF03WQnSi7HfsnX4qw6_OU7emhUC2zlJAHL0VfkA3DzZdUJgOb1t492d0OYD19HTxwbjbjLmNETu-XVvRNS1fB1nJ49GB5j4hAAFPZpXvHoppg3nG5wDUfpXBfAU0OyLVK_IUSWP06se6ps02qObayqfCicn3Vm7QTknXMn9XFl-MGjQFJ9h2jnciLHK0awKPmi1REbWzQtV5fzQF1NxvhXuI37haGScc2RcpqWKAHMIDwo8WDHF4IRa9YLb5FNbZQBFzBHUBwlgBTK_L0WIKCgUdiXjtK27C6pJ84foUrAZqy-x0iCzWe41MTviWt2gky7xDnheot0jGl87eifTxiK-ZgxDIrysxY7We1tgpaRfDyOiPk8OVAdY93Fp56q5dtM-xkIz5GUDSviuCMywrHDt-ucR5cePOOD-dbHtWMPnX38Yg0f7tD5j1XXvRDQWFbljug98WxOaXqD5yR8zDdZzDXfLPjdThFWyy4AHOEqcEiRr9X85ob3cfIi0Pww3tYLHCMwy18EJt0BOGO_d9JGBdA6RSddnahy8W6SlFZQ40B7RJSqPUIQeH36tRlmGUMF8O88UBEJCH2vHMveQbx49DCHvtyuEbSsPLHga8UcYImpKpgbCF02flKEyhU8bYi5DAw8FA2vHhspD8roJ3aHYMG6ICz8nojaj2ktZHxafH9IoS0vODtcIaKSkU6RD6P5cUFTHwSJI-JEYNeYRWXAo45Ba8uEdD2vXkmIfrqmxhDwIqVvEPRqzp3WyWJvdWQvfg1xsFavh-Xo6XApsmJl1i6VYTV6KuSq4eA0VhWfSGN7QSD0V3o8eqYr6Ps0JZEW4SWsKvrGotSakbt7xrSk6funahFu83a65LZoqDyEepJPTKLf5A-Jgq9MHL8N3UnwKwMYOPoTi2I8pdwQt03nNPD-a00yOy0NkLbSDNsDUizNO8NV0nUTVsydNiCgAGxLhsrWgqfBo8ReL-Xbe8rfZp-LRVnLek1rW_1aHFwgyhvnU5kJ7_fWrc2EeeAg3AWg2sUL-noQyI5TTztBwOR70rLqHPVSPCwOOuhGqRCRLVqvIMkru9l_rKFp_HDp-_w6hrQYF_LRRYDFoamigU4rOmbMOY4TfbfQh3HkB4my6e94vHTO1NJeaPZpYkVblpZpEfbnJ2_xWglGihxgJV33GASwpQ3veellbkVx_WS4PqAXIHCVe56txrk_a5CvylhzmKVYFRhLJK5m2J1NJ57qFUexo6rlFlXgzH2Nj_BJAM9czVpzcwVdspR-hviK_MtltmU9NvcfLoDii3VPeFBqkQ6UH2B8WCqX54dDxyjQFFo5DcYSSv5Si0e0H7NohjJ4pOv-S5LVlDUiChj7kBB8USD_P7hg-xdejiRa9YU__2LZClvOf5fFEmDvXgsY1_KmJqa8vok1cdyToPbwT4U4o-wDNH8i_YnbZqxDdO3GPrgGdcQCtL7p6jDFuzgM=w1088-h928"));
        categories.add(new Category(5, "Đồ chay", "https://lh3.googleusercontent.com/fife/AMPSemcwfswHlrloH0ZB3Yj9NEAUObz-BYaLcClXnrvCl8NG0bErC2uz6IlAI365sUU6L6WX2v8Zo1v3wWLsALL6roMEAA-hWU5HrsA7PVI7uJdpcD1xph6ntq58SYe3xE-Dj1gIozC9j-cXjD4is6qtWDziaxHyg_qPwZNVTpF87GqNd1Hv03OFawRTEZlzORh-QHGVL0_4vS4-YpaaZPhw9LC1-93hkaZJx1PqHSAkyv2gxnWuxFB7uzIVdz-eeBAn4qrK5MQxGKJp4smukAWTwrRvByqolrblWWYUTiaqvXrwZoef8dugv-jiElG5usGrakcYwt0TmCYMR2Ttjc-zAjf2Ve0puh_Qe6sayzQ7-NqMYva0FD7KI6h6-KkpX59AbHnV8ZvpHVr1UNm_XbMpbRMcUotKou7g-fHrM4dQF1yeKK-Fo7zyHKJ1gQz5rdUMElRhnYzIggPVDeDYGDEo8zuo5wqgj1nrt_5lAnPdm9ph-Akx0BGPRFL4O3ukCKUTUE7982wQq-ApHnpbn8uaLHfuM2-xVyWyNJOldhclnbvijwurrAK459DU3pEDlwwP482dDdSa1lVJugpR1tLhnohnRCsLLGoPMhal9KdgPVyXyxhKMhAtalUHeVWjgdWOGTXX4pzE9kaA6o-eny6kJ7flWDD_uhiv6I5ZxYd-hVehZg5xk5uuQoJdRUWLGiAH-fSFY1_4Gc82_Rybry4b7nRNYtr8mqQ8mveHAKcQl0Im1yCjIaOSqOM70u04OlTEDJycngkhIxHpWwQsG76ZX0QO7brFUHj9GeScq2E_Fxrz8mVJ6UXITU_ho3wHiDHdqXH8kTTyRu4Bj00B-ti_xqOu5ANTGW4cXxvd7VZjl-qO6pd1mxeM6uWzPQP_UNNn9vyk0S2Mh-R5AhPSIYUbGO2j-NK0o7h0yweohLmrUyBxiXryxDOo4F8Wqqj0O8nEtziSNjwm28zjiYJRFz3Ikg7oBn87NZAC5ra2EvrGol3cK8KpDKAXJSMQIDTAqX6_C0BIV4ogzP74BnEjM2FrFckT648vLmBt57Ct8zMt-6hfb1rwkm-UJsSgWL2x5ry80WbX3TEDAu71LBAywzl9E93-61faO1k2JJU04_5lb1-GkUuw3_pZAOo5fV7gVf2Iiy9B31NMr-oIaRS7v6qcp8-y_MpguxKaPW0WwzaiVjluDS_lPPoeXZg7yNv29OZrJnvCduCfFSEpaTWhH_V7KEc8dHxc4N1G6w935Uf6kiTBOxVnATfeHVB2hQtm1RciV66Mu0IKMgIIpMVWO-b0Vn1HGYMfEOVaETu44wS0PB0btIjWu0XjH4SJbD2fSC26dTZqcumbhVoN4tfRTgFQqLNTmt2E6FoTMhC6pGLF1-CnexJV2lGiUutr8uKwK2PeXbGFjNL7oVrvAnPl45TqlGipSyvqMoPCvMSP6Gn2ELzWOGlm_i7GZyGnWzIw0idhgk4RFz3o7q36yVyRnqZ8f9VutKkjKFbLafchwKbSP_qWeFsxZXA5Mn64wW3mTeg=w1088-h928"));
        categories.add(new Category(6, "Gạo", "https://lh3.googleusercontent.com/fife/AMPSemeYhubSmWKanGEKsNNeCMMyT9r7OgDIu5wuuq9heWuXcxXx9_EkEg6txkT0jcwEa9hYouosPP83J16bemWrCMceAfpeYWfshv_LPw6sHOdi_fGO1UuLT-N3vPsX-mtoaaYz6U-Hotxu4BGdDZuFFcNd5Zj7VO-zQtVY5MC9Ju8B-58HMTuB6hfdZop44erKAfk9Z5sAsaK50SkMfEnHW7I7991br6tUTVcgwe7pwp0S-gsIFEqFK6bdCuWdjyzhMVtvTE-tMh_DMt_NVCq466IeOlcr8fhrTk0yi-hWxe84FDwa8-W9zxplHeIyDXXnUiK3P4DOVjWrCmOaLwJiMNtEpltDQSsGcGwRwLca4nl0RFbsm_zcRZD3_RlScmsBRYijPhrO21nvl6VO4ThnWovxJPWV5cClsyKRS05PlehFOfP7USkti7VXrX77x2lwnPOtobwoq1n-nMZY5edgALt-SyG_WMg_fj3Bhp4sMUKG2DYYpJQtLoyRlPi7P3VLYdrAGwjbQmYtVB4PzriNGtIzrlvMct_czNviSr306weUzmp4RMN0XaK2gu8LUYFsO99xlCwGQZrScIHh8tkz6QNdnqXZjVWt3S3jNMwdL2WDE-a8JiESGPgUnYTZ2ScZrQGlqXoZcWQKxFS5YpoL7yS9gK8hhRWSiI_b9UQUa21yEjp1LeVUXZDFs75NTGgj7rQNSwl2FJr91sbUMklEr86WMEtWjCSNQBlaubxQ9m1ZRrn0YaWofcuSgg0yrvOR-V3QiEsYo-0wQwC84ewdZHlaXFJRI2rhQJswWoO3tY1A46op7sOn9ndAduwgUqXcoP0ZukV7m3fCes3bv2oMkv31N2F1TC5GJysgKTRjot0U7Hig2Mul0dqBZqJWOEPn_2pIONFcdJe6wIRUgFEK9BrpIoLxYQ0opUrFEQXqwKffcxC6y2pZMEcqpgWBK-BnU2Qj78Meg4WlDmj-CDcc7hfd7zxERFEQevjqTi4lWeyKaAyXKHAcPpM97pk2st2pU-AiGmiE8CG-REiST1WhJj-mqI-ftiXDSGArvbAZP2ZUXicMh6dLooeX8VGEkdR436Bp_nMxXF30TXlV9qfByNNxWqewT15asYF4K4_Imk0zVJ8dNUrg74KInCCYmr9HKTvSQDNJZTjoE3MJhmA72a3oKm8dRT3D-XUwon1cHj79Z0xrXA7_wPO3bwJVF3nKYTiHHqqcyk5rHbEekp8WOwYij6oj6hjbgi5_K8o-Bje9ptBKszKSQ0yjdmDOQbTDMOcafI3rvw51vWwK7tEqRv1e-BChN3MhRr81oHi9FuBOhQv-dTefpPWZ5BfiK7MP397-U-FqhT4FxSohTFWb03wxesnhQo7eUWpFVuLighh4mJ9CVFMN9Et8tziJo9_2nS2HEg845FAlS1CFqJHNcXIxc-NFDmaaUQJxXcv7HdmTeb7dfsHBFJm6DkB-Tg8gvEthX-UDcwq6Qn14j06Dfo7vKNBwv6SB2e6x9C2ONcAmdYl4qrwj104PWYJaCG8=w1919-h928"));
        categories.add(new Category(7, "Cà phê", "https://lh3.googleusercontent.com/fife/AMPSeme9jMlzkNBP0wA8N25p7hieSjUuMsZbpL4YDusacyRoIqrOBnhgNfEjCmSs4AD3pZShUSbBg_0VwylEx7tTWm7K91z0dS4BLXKUS4BGoj0DAHyYpPg3MMOyoiYEtsvOqY8VOGqN3siaxJPdnm3Hjy589-ePh07cOO_iWoWvVOvcteRtPS3cw5oW3IClVr15CwyONw9o5KGRdSs4r9umOj9uM5I9SxoTHC96rOAzsQ7yVyyrw8iRky3qhh0T0yXGDOloMMRA5a__bPhqdzReNfG1rgmApOosMIstnWnApI9gx6ChaJna8LU_7Q2bG_5PHJ2cvVwp1Mp4w5jX4a_-p-E5X86FxT7gWCGFdTy3TIZRMLJyOMN4o_Q5AAiDO45a7l6qMAQElwtpw-izgXu3uMROG6bNUP2rfDGZsmRABfaSlE9z6m7d35IhHeROEz34icPamus7i39E-4TyetEhxOhjh-6LZeLJ92MQTp9W3ZQY60xN13ujGsvurM60vF4qznHzuY0BhR840WZYSeXAQUMaZSgVM2gx_u4HhpkY2jx7MyQFvHFgDMRgbw90RWDbCOj_Pu8oH9luTXZCY_FEf0BYHYh6n0E8LF-FdBwlaBPYTgA0pYmx-1A24rI5U26h14F_6nB4QReZzOPSWKR5u-CwB7N7eWoM_cf7-3M7ncvE09173t64nQbWkxJeqC0NQ1MbJnHewL4NwpTnapVELgx1EIXC7RegxciD-v_uk4_CZ4mBx19RbZhPYdtoEqkNFW2IUt6qR8J5ql4r7291AJy8hI5Xr78twdqItpWzJ_vNHOkgpigtMbexP9e4FDuausYe01vjidLlI58Yruwq5D5Qelehl-mHyWv8OOkLA6YJLlsz75C6S6piXQ5qf74y5W2keoTUHeLYvGYHrnbxc6xuSdIJ4Q4MGZnXcNoED-jVB2l8G8n1XhMOr4A259K9X3uAbUFAOKabTcU1oKv8demQtqgMpgDW8mNwHUI5zCK5YSGoQNWnu_3mRDVpE4fK3l2IV7W-g-JNDyo5TZa_iCShIs78bH6F_yRSJcqgNcflx1JG0sEEl_1uQc-QD2hVGRYSH2uhEHUZTOImsIKeh_p3gO1S1x_jkRLz8mmV45GCsrZi5nKFz4Kz6qCFHaPLOuHcpCG9Dg8BdEuJiZMq2aurvq4JnV0L4GKx9GiUZD5iSpACzDvuiGexj25Ei9jpN7LRY1k8b4QAeThq6L0TaNwPtaQnX40PVpHzZS6IDwML0SaBIaat1G57-BLKDAoJJtaYakQ3Y6vOLbLHaPBh6ObVnTzp_-vh8MZ6vInMlDuZeYu3VGhG9Hp1bdsz2HkfjuXLoTW5VpA9-hr63OPDptesNP-kkCCItnsyY-H0tVGeubmV7gWCbDepTHitd95oXJ5W_grtDtY4qE5ttPfMEXZWjn_J6XVeBjVtZA6ZCG4KTQyQ7H6aWSvE-EkF-oqJ8eQdEyq7KiaEj99zlHQnZbbWu05g6mbJCt4SKHAkD1OecwI1PqCjAmMkiw4libE=w1088-h928"));

        return categories;
    }

    private  void searchCategory() {
        gridViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Tạo đối tượng Bundle để truyền dữ liệu qua Fragment mới
                Bundle bundle = new Bundle();
                bundle.putInt("idCategory", getListCategory().get(position).getId());

                // Tạo đối tượng Fragment mới
                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setArguments(bundle);

                // Lấy đối tượng FragmentManager
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                // Thay thế Fragment hiện tại bằng Fragment mới
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, searchFragment) // R.id.fragment_container là ID của ViewGroup chứa các Fragment
                        .addToBackStack(null) // (Tuỳ chọn) Thêm Fragment hiện tại vào Stack để có thể quay lại sau này
                        .commit();
            }
        });

    }

}
