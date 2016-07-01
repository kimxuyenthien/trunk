package de.gimik.app.allpresanapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.database.MemberDbAdapter;
import de.gimik.app.allpresanapp.fragment.*;
import de.gimik.app.allpresanapp.gcm.RegistrationIntentService;
import de.gimik.app.allpresanapp.model.ItemMenu;
import de.gimik.app.allpresanapp.model.Member;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final long DOUBLE_PRESS_INTERVAL = 1000;/* some value in ns. */
    private long lastPressTime;
    private BaseFragment newFragment = null;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.navigation)
    NavigationView navigationView;

    @Bind(R.id.lv_menu_item)
    public ListView lvMenuItem;

    @Bind(R.id.btn_logout)
    public Button btnLogout;

    public ActionBarDrawerToggle drawerToggle;

    public MenuItemAdapter adapter;

    private BaseFragment oldFragment;
    List<ItemMenu> menuLoggedIn;

    boolean isOpenFromNotification = false;
    boolean isAcademyNews = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final MemberDbAdapter memberDbAdapter = new MemberDbAdapter(this);
        final Member member = memberDbAdapter.queryForFirst();

        Boolean isNotFirstTime = Utils.getBooleanForKey(
                this.getApplicationContext(),
                Constant.NOT_FRIST_TIME);
        boolean isLoggedIn = member != null && member.isActive();
        if (isNotFirstTime) {
            setupNavigationMenu(isLoggedIn);
            lvMenuItem.setOnItemClickListener(this);
            navigationView.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
        } else {
            navigationView.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);
        }

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        try {
            isOpenFromNotification = getIntent().getBooleanExtra(Constant.ExtraKey.IS_OPEN_FROM_NOTIFICATION, false);
            isAcademyNews = getIntent().getBooleanExtra(Constant.ExtraKey.IS_ACADEMY_NEWS, false);
        } catch (Exception e) {
        }
        //get data from scanner activity
        long productId = 0;
        try {
            productId = getIntent().getExtras().getLong(Constant.PRODUCT_ID, 0);
        } catch (Exception e) {
        }

        if (productId != 0) {
            Intent intent = new Intent(this, ProductDetailsActivity.class);
            intent.putExtra(Constant.PRODUCT_ID, productId);
            this.startActivity(intent);
//            replaceFragment(new ProductDetailsActivity(), ProductDetailsActivity.TAG, false, true, getIntent().getExtras());
            selectMenuItem(0);
        } else if (isNotFirstTime) {
            if (isOpenFromNotification && isLoggedIn) {
                if (!isAcademyNews) {
                    switchMenuItem(101);
                } else {
                    switchMenuItem(104);
                }
            } else {
                switchMenuItem(0);
            }
        } else
            replaceFragment(new MainFragment(), MainFragment.TAG, true, true, null);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogConfirmLogout();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switchMenuItem(position);
    }

    public void switchMenuItem(int position) {
        if (menuLoggedIn == null) {
            final MemberDbAdapter memberDbAdapter = new MemberDbAdapter(this);
            final Member member = memberDbAdapter.queryForFirst();

            boolean isLoggedIn = member != null && member.isActive();
            setupNavigationMenu(isLoggedIn);

            lvMenuItem.setOnItemClickListener(this);
            navigationView.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
        }
        String tag = "";
        BaseFragment fragment = null;
        Bundle bundle = null;
        ItemMenu menuItem = menuLoggedIn.get(position);
        switch (menuItem.code) {
            case 1:
                tag = ProductWorldsFragment.TAG;
                fragment = new ProductWorldsFragment();
                break;
            case 2:
                tag = TechnologieFragment.TAG;
                fragment = new TechnologieFragment();
                break;
            case 3:
                tag = AboutFragment.TAG;
                fragment = new AboutFragment();
                break;
            case 4:
                tag = ContactFragment.TAG;
                fragment = new ContactFragment();
                break;
            case 5:
                tag = ScannerIntroduceFragment.TAG;
                fragment = new ScannerIntroduceFragment();
                break;
            case 6:
                tag = LoginFragment.TAG;
                fragment = new LoginFragment();
                break;
            case 7:
                tag = DatenFragment.TAG;
                fragment = new DatenFragment();
                break;
            case 8:
                tag = ImpressumFragment.TAG;
                fragment = new ImpressumFragment();
                break;
            case 9:
                tag = FootProfessionalSearchFragment.TAG;
                fragment = new FootProfessionalSearchFragment();
                break;

            case 101:
                tag = EventFragment.TAG_NEWS;
                fragment = new EventFragment();
                bundle = new Bundle();
                bundle.putInt(Constant.EVENT_TYPE, 0);
                break;
            case 102:
                tag = AppAreasFragment.TAG;
                fragment = new AppAreasFragment();
                break;
            case 103:
                tag = TrainingToolFragment.TAG;
                fragment = new TrainingToolFragment();
                break;
            case 104:
                tag = EventFragment.TAG_AKADEMIE;
                fragment = new EventFragment();
                bundle = new Bundle();
                bundle.putInt(Constant.EVENT_TYPE, 1);
                break;
        }

        if (fragment != null) {
            replaceFragment(fragment, tag, true, true, bundle);
            selectMenuItem(position);
        }
        drawerLayout.closeDrawer(navigationView);
    }

    public void selectMenuItem(int position) {
        for (int i = 0; i < menuLoggedIn.size(); i++) {
            menuLoggedIn.get(i).selected = (i == position);
        }
        adapter.updateItems(menuLoggedIn);
    }


    public void setupNavigationMenu(boolean isShowLoggedInMenu) {
        menuLoggedIn = new ArrayList<ItemMenu>();
        menuLoggedIn.add(new ItemMenu(1, getString(R.string.product), R.drawable.produkte, R.drawable.produkte_active));
        menuLoggedIn.add(new ItemMenu(2, getString(R.string.technologie), R.drawable.technologien, R.drawable.technologien_active));
        menuLoggedIn.add(new ItemMenu(3, getString(R.string.about_us), R.drawable.uberun, R.drawable.uberun_active));
        menuLoggedIn.add(new ItemMenu(4, getString(R.string.contact), R.drawable.kontakt, R.drawable.kontakt_active));
        menuLoggedIn.add(new ItemMenu(5, getString(R.string.product_scanner), R.drawable.scanner, R.drawable.scanner_active));
        menuLoggedIn.add(new ItemMenu(9, getString(R.string.foot_prefessional_search), R.drawable.foot_professional_search, R.drawable.foot_professional_search_active));
        //isShowLoggedInMenu=true;
        if (isShowLoggedInMenu) {
            menuLoggedIn.add(new ItemMenu(101, getString(R.string.news), R.drawable.news, R.drawable.news_active));
            menuLoggedIn.add(new ItemMenu(102, getString(R.string.app_areas), R.drawable.anwendung, R.drawable.anwendung_active));
            menuLoggedIn.add(new ItemMenu(103, getString(R.string.training_tool), R.drawable.schulung, R.drawable.schulung_active));
            menuLoggedIn.add(new ItemMenu(104, getString(R.string.allpresan_academy), R.drawable.akademie, R.drawable.akademie_active));
        } else {
            menuLoggedIn.add(new ItemMenu(6, getString(R.string.login), R.drawable.bereich, R.drawable.bereich_active));
        }
        menuLoggedIn.add(new ItemMenu(7, getString(R.string.data_protection), R.drawable.daten, R.drawable.daten_active));
        menuLoggedIn.add(new ItemMenu(8, getString(R.string.impressum), R.drawable.impressum, R.drawable.impressum_active));


        adapter = new MenuItemAdapter(this, 0, menuLoggedIn);
        lvMenuItem.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btnLogout.setVisibility(isShowLoggedInMenu ? View.VISIBLE : View.GONE);
    }

    public <T extends BaseFragment> void replaceFragment(T fragment, String tag, boolean clearBackStack, boolean addBackStack, Bundle bundle) {
        // hide keyboard
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        new Handler().post(new RunFragment(fragment, tag, clearBackStack, addBackStack, bundle));
    }

    private class RunFragment implements Runnable {
        BaseFragment fragment;
        String tag;
        boolean addBackStack, cleanBackStack;

        private RunFragment(BaseFragment fragment, String tag, boolean cleanBackStack, boolean addBackStack, Bundle bundle) {
            this.fragment = fragment;
            this.tag = tag;
            this.addBackStack = addBackStack;
            this.cleanBackStack = cleanBackStack;
            //set new fragment to check
            newFragment = fragment;
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
        }

        @Override
        public void run() {
            try {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentManager == null)
                    return;
                BaseFragment topFragment = getTopFragment();

                if (cleanBackStack && (newFragment != topFragment))
                    fragmentManager.popBackStack(null, android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

                android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);


                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.add(R.id.content_layout, fragment, tag);
                if (topFragment != null && newFragment != topFragment) {
                    ft.hide(topFragment);
                }

                if (addBackStack && (newFragment != topFragment))
                    ft.addToBackStack(tag);

                setTitle(newFragment.getTitle());
                ft.commit();
                oldFragment = newFragment;
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        boolean isDoubleClick = false;
        long pressTime = System.currentTimeMillis();
        if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
            // this is a double click event
            isDoubleClick = true;
        }
        lastPressTime = pressTime;
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager == null) {
            super.onBackPressed();
        }
        int count = fragmentManager.getBackStackEntryCount();

        if (count > 0) {
            BaseFragment topFragment = getTopFragment();
            if (count > 1) {
                super.onBackPressed();
            } else {
                if (isDoubleClick) {
                    finish();
                    return;
                }
                if (!drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }

            BaseFragment currentTopFragment = getTopFragment();
            if (currentTopFragment != null && currentTopFragment.getOnDisplay() != null) {
                currentTopFragment.getOnDisplay().onDisplay();
            }
            if (currentTopFragment != null) {
                setTitle(currentTopFragment.getTitle());
                if (currentTopFragment instanceof LoginFragment) {
                    switchMenuItem(0);
                    return;
                }
            }
        } else {
            super.onBackPressed();
            if (isDoubleClick) {
                finish();
                return;
            }
        }
    }

    class MenuItemAdapter extends ArrayAdapter<ItemMenu> {
        private Context context;
        private List<ItemMenu> items;
        private LayoutInflater inflater;

        public MenuItemAdapter(Context context, int resource, List<ItemMenu> items) {
            super(context, resource, items);
            this.items = items;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        public void updateItems(List<ItemMenu> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.menu_item_layout, parent, false);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            }
            vh = (ViewHolder) convertView.getTag();
            vh.updateView(position);

            return convertView;
        }

        class ViewHolder {
            private ImageView ivIcon;
            private ImageView ivBackground;
            private TextView tvTitle;

            public ViewHolder(View view) {
                ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                ivBackground = (ImageView) view.findViewById(R.id.ivBackground);
            }

            public void updateView(int position) {
                ItemMenu item = items.get(position);
                tvTitle.setText(item.getTitle());
                final int version = Build.VERSION.SDK_INT;

                if (item.selected) {
                    ivIcon.setImageResource(item.activeResourceIcon);
                    ivBackground.setImageResource(R.drawable.leftmenubg_active);
                    if (version >= 23) {
                        tvTitle.setTextColor(getColor(android.R.color.white));
                    } else
                        tvTitle.setTextColor(getResources().getColor(android.R.color.white));
                } else {
                    ivIcon.setImageResource(item.getResourceIcon());
                    ivBackground.setImageResource(R.drawable.leftmenubg);
                    if (version >= 23) {
                        tvTitle.setTextColor(getColor(R.color.gray));
                    } else
                        tvTitle.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    /**
     */
    private void showDialogConfirmLogout() throws Resources.NotFoundException {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.logout_title))
                .setMessage(
                        getResources().getString(R.string.logout_confirm))
                .setPositiveButton(
                        getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                MemberDbAdapter memberDbAdapter = new MemberDbAdapter(MainActivity.this);
                                memberDbAdapter.deleteAll();
                                setupNavigationMenu(false);

                            }
                        })
                .setNegativeButton(
                        getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        }).show();
    }


}



