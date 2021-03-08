package com.wikitude.samples;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.wikitude.architect.ArchitectView;
import com.wikitude.common.permission.PermissionManager;
import com.wikitude.samples.util.PermissionUtil;
import com.wikitude.samples.util.SampleCategory;
import com.wikitude.samples.util.SampleData;
import com.wikitude.samples.util.SampleJsonParser;
import com.wikitude.samples.util.TileItem;
import com.wikitude.samples.util.adapters.TilesAdapter;
import com.wikitude.sdksamples.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private static final String sampleDefinitionsPath = "samples/samples.json";
    BottomNavigationView bottomNavigationView;
    public static final String INTENT_EXTRAS_KEY_SAMPLE = "sampleData";
    private static final int EXPANDABLE_INDICATOR_START_OFFSET = 60;
    private static final int EXPANDABLE_INDICATOR_END_OFFSET = 30;
    private GridView gridView;
    private RecyclerView recyclerView;
    private TilesAdapter adapter;
    private ArrayList<TileItem> tileList;
    private final PermissionManager permissionManager = ArchitectView.getPermissionManager();
    private ExpandableListView listView;
    private List<SampleCategory> categories;


    private ArrayList<TileItem> getData() {
        int[] covers = new int[]{
                R.drawable.laatm1,
                R.drawable.lhome,
                R.drawable.lhospital,
                R.drawable.lhotel,
                R.drawable.llocation,
        };
        String [] colors = getResources().getStringArray(R.array.tile_colors);
        TileItem a = new TileItem("ATMs","ATMs",covers[0],colors[0]);
        tileList.add(a);

        TileItem b = new TileItem("Properties","Properties",covers[1],colors[1]);
        tileList.add(b);

        TileItem c = new TileItem("Hospitals", "Hospitals",covers[2],colors[2]);
        tileList.add(c);

        TileItem d = new TileItem("Restaurants" ,"Restaurants",covers[3],colors[3]);
        tileList.add(d);

        TileItem e = new TileItem("Places", "Places",covers[4],colors[4]);
        tileList.add(e);


        return tileList;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tileList = new ArrayList<>();
        adapter = new TilesAdapter(this, getData(), new TilesAdapter.TileAdapterListner() {
            @Override
            public void onAddToFavoriteSelected(int position) {

            }

            @Override
            public void onPlayNextSelected(int position) {

            }

            @Override
            public void onCardSelected(int position, ImageView thumbnail) {

                final SampleData sampleData = categories.get(0).getSamples().get(position);
                final String[] permissions = PermissionUtil.getPermissionsForArFeatures(sampleData.getArFeatures());

                permissionManager.checkPermissions(Main2Activity.this, permissions, PermissionManager.WIKITUDE_PERMISSION_REQUEST, new PermissionManager.PermissionManagerCallback() {
                    @Override
                    public void permissionsGranted(int requestCode) {
                        final Intent intent = new Intent(Main2Activity.this, sampleData.getActivityClass());
                        intent.putExtra(SimpleArActivity.INTENT_EXTRAS_KEY_SAMPLE, sampleData);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void permissionsDenied(@NonNull String[] deniedPermissions) {
                        Toast.makeText(Main2Activity.this, getString(R.string.permissions_denied) + Arrays.toString(deniedPermissions), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void showPermissionRationale(final int requestCode, @NonNull String[] strings) {
                        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Main2Activity.this);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle(R.string.permission_rationale_title);
                        alertBuilder.setMessage(getString(R.string.permission_rationale_text) + Arrays.toString(permissions));
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                permissionManager.positiveRationaleResult(requestCode, permissions);
                            }
                        });

                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    }
                });

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Main2Activity.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        final String json = SampleJsonParser.loadStringFromAssets(this, sampleDefinitionsPath);
        Log.i("json",json);
        categories = SampleJsonParser.getCategoriesFromJsonString(json);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private int dpToPx(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArchitectView.getPermissionManager().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }





}
