package kz.onko_zko.hospital;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kz.onko_zko.hospital.fragments.AccreditationFragment;
import kz.onko_zko.hospital.fragments.AdminFragment;
import kz.onko_zko.hospital.fragments.AntiCorruptionFragment;
import kz.onko_zko.hospital.fragments.AskQuestionFragment;
import kz.onko_zko.hospital.fragments.ContactFragment;
import kz.onko_zko.hospital.fragments.GalleryFragment;
import kz.onko_zko.hospital.fragments.GoalsTasksFragment;
import kz.onko_zko.hospital.fragments.MedicalListFragment;
import kz.onko_zko.hospital.fragments.NewsFragment;
import kz.onko_zko.hospital.fragments.ServicesFragment;
import kz.onko_zko.hospital.fragments.SettingsFragment;
import kz.onko_zko.hospital.fragments.StandardsFragment;
import kz.onko_zko.hospital.fragments.StructureFragment;
import kz.onko_zko.hospital.fragments.VacancyFragment;
import kz.onko_zko.hospital.fragments.WorkingHoursFragment;
import kz.onko_zko.hospital.hamburger.HamburgerMenuItem;
import kz.onko_zko.hospital.hamburger.HamburgerMenuModel;
import kz.onko_zko.hospital.hamburger.RecyclerAdapter;
import kz.onko_zko.hospital.hamburger.SubMenuItem;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.ItemClickChild,
        ContactFragment.OnFragmentInteractionListener,
        AccreditationFragment.OnFragmentInteractionListener,
        NewsFragment.OnFragmentInteractionListener,
        GalleryFragment.OnFragmentInteractionListener,
        VacancyFragment.OnFragmentInteractionListener,
        ShowDetailsFragment.OnFragmentInteractionListener,
        StandardsFragment.OnFragmentInteractionListener,
        StructureFragment.OnFragmentInteractionListener,
        GoalsTasksFragment.OnFragmentInteractionListener,
        MedicalListFragment.OnFragmentInteractionListener,
        WorkingHoursFragment.OnFragmentInteractionListener,
        AskQuestionFragment.OnFragmentInteractionListener,
        AntiCorruptionFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        AdminFragment.OnFragmentInteractionListener,
        ServicesFragment.OnFragmentInteractionListener{

    String names[];
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private RecyclerAdapter adapter;
    private ArrayList<HamburgerMenuModel> menu;
    private int bottomSelectedItemIndex = -1;
    String locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences("settings_onko",
                Context.MODE_PRIVATE);
        if(sp.getBoolean("disableNotifications", false) == false)
            FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.v("Notifications are", String.valueOf(sp.getBoolean("disableNotifications", false)));
        mDrawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menu);

        recyclerView = findViewById(R.id.recyclerView);
        locale = LocaleManager.getLocale(getResources()).toString();
        List list = getList(locale);

        adapter = new RecyclerAdapter(this, list, this);
        Intent intent = getIntent();

        int menuIndex = intent.getIntExtra("MenuIndex", -1);
        bottomSelectedItemIndex = intent.getIntExtra("BottomSelectedIndex", -1);
        if(menuIndex!=-1){
            String menu_name = (intent.getBooleanExtra("InnerMenu", false))?
                    menu.get(menuIndex).SubCategories.get(intent.getIntExtra("SubMenuIndex",0)).subMenuTitle:
                    menu.get(menuIndex).CategoryName;
            adapter.setItemParent(menu_name);
            actionbar.setTitle(menu_name);
            Fragment fr = (intent.getBooleanExtra("InnerMenu", false))?
                    menu.get(menuIndex).SubCategories.get(intent.getIntExtra("SubMenuIndex",0)).fragment:
                    menu.get(menuIndex).fragment;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fr)
                    .commit();
        } else if (bottomSelectedItemIndex!=-1) {
            adapter.setItemParent("");
            actionbar.setTitle((bottomSelectedItemIndex==1)?R.string.ask_question:R.string.settings_menu);
            RelativeLayout settings = findViewById(R.id.settings);
            RelativeLayout ask_question = findViewById(R.id.ask_question);
            settings.setBackgroundColor((bottomSelectedItemIndex==2)?0xFF197a9f:0xFF1c86ae);
            ask_question.setBackgroundColor((bottomSelectedItemIndex==1)?0xFF197a9f:0xFF1c86ae);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, (bottomSelectedItemIndex==1)?AskQuestionFragment.newInstance("",""):SettingsFragment.newInstance("",""))
                    .commit();
            bottomSelectedItemIndex = -1;
        } else {
            adapter.setItemParent(names[0]);
            actionbar.setTitle(names[0]);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, menu.get(0).fragment)
                    .commit();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        final RelativeLayout settings = findViewById(R.id.settings);
        final RelativeLayout ask_question = findViewById(R.id.ask_question);
        ((ImageView)ask_question.findViewById(R.id.menu_item_icon)).setImageResource(R.drawable.ic_doctor);
        ((TextView)ask_question.findViewById(R.id.menu_item_name)).setText(R.string.ask_question);
        ask_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setItemParent("");
                adapter.notifyDataSetChanged();
                v.setBackgroundColor(0xFF197a9f);
                settings.setBackgroundColor(0xFF1c86ae);
                mDrawerLayout.closeDrawers();
                toolbar.setTitle(R.string.ask_question);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, AskQuestionFragment.newInstance("",""))
                        .commit();
                bottomSelectedItemIndex = 1;
            }
        });

        ((ImageView)settings.findViewById(R.id.menu_item_icon)).setImageResource(R.drawable.ic_settings);
        ((TextView)settings.findViewById(R.id.menu_item_name)).setText(R.string.settings_menu);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setItemParent("");
                adapter.notifyDataSetChanged();
                v.setBackgroundColor(0xFF197a9f);
                ask_question.setBackgroundColor(0xFF1c86ae);
                mDrawerLayout.closeDrawers();
                toolbar.setTitle(R.string.settings_menu);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, SettingsFragment.newInstance("",""))
                        .commit();
                bottomSelectedItemIndex = 2;
            }
        });

    }
    @NonNull
    private Configuration getConfiguration(String language) {
        Configuration configuration = new Configuration(this.getResources().getConfiguration());
        configuration.setLocale(new Locale(language));
        return configuration;
    }

    public ArrayList<HamburgerMenuModel> setupList(String language){
        names = this.createConfigurationContext(getConfiguration(language)).getResources().getStringArray(R.array.menu_main);
        int icons[] = {
                R.drawable.ic_news,
                R.drawable.ic_services,
                R.drawable.ic_contacts,
                R.drawable.ic_vacancy,
                R.drawable.ic_businessmen,
                R.drawable.ic_gallery
        };
        Fragment fragments[] = {
                NewsFragment.newInstance("",""),
                ContactFragment.newInstance("",""),
                VacancyFragment.newInstance("",""),
                GalleryFragment.newInstance("","")
        };
        menu = new ArrayList<>();
        int fragment_index = 0;
        for(int i =0;i<names.length; i++){
            if(i==1 || i == 4){
                final int sub_index = i;
                menu.add(
                    new HamburgerMenuModel(names[i], icons[i],
                    new ArrayList<HamburgerMenuModel.SubMenuModel>() {{
                        if(sub_index == 1)
                        {
                            final Fragment subFragments[] = {
                                    MedicalListFragment.newInstance("",""),
                                    WorkingHoursFragment.newInstance("","")
                            };
                            String subMenus[] = getResources().getStringArray(R.array.services_submenu);
                            for(int i = 0; i < subMenus.length; i++)
                                add(new HamburgerMenuModel.SubMenuModel(subMenus[i], subFragments[i]));
                        } else {
                            final Fragment subFragments[] = {
                                    StandardsFragment.newInstance("",""),
                                    AntiCorruptionFragment.newInstance("",""),
                                    //StructureFragment.newInstance("",""),
                                    AccreditationFragment.newInstance("",""),
                                    GoalsTasksFragment.newInstance("","")
                            };
                            String subMenus[] = getResources().getStringArray(R.array.corporation_submenu);
                            for(int i = 0; i < subMenus.length; i++)
                                add(new HamburgerMenuModel.SubMenuModel(subMenus[i], subFragments[i]));
                        }
                    }}));
            }  else {
                menu.add(new HamburgerMenuModel(names[i], icons[i], fragments[fragment_index]));
                fragment_index++;
            }
        }
        return menu;
    }

    private List<HamburgerMenuItem> getList(String language) {

        List<HamburgerMenuItem> list = new ArrayList<>();
        menu = setupList(language);

        for(int z = 0; z< menu.size(); z++) {
            ArrayList<SubMenuItem> subMenu = new ArrayList<>();
            if (menu.get(z).SubCategories.size() > 0){
                for (int j = 0; j < menu.get(z).SubCategories.size(); j++) {
                    subMenu.add(new SubMenuItem(menu.get(z).SubCategories.get(j).subMenuTitle));
                }
            }
            list.add(new HamburgerMenuItem(menu.get(z).CategoryName, subMenu, menu.get(z).menuIconDrawable));
        }

        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hamburger_toolbar_contents, menu);
        if(locale.equals("ru")) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.rus_lang).setBackgroundColor(0xFF197a9f);
                    findViewById(R.id.kazak_lang).setBackgroundColor(0x00000000);
                }
            });
        } else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.kazak_lang).setBackgroundColor(0xFF197a9f);
                    findViewById(R.id.rus_lang).setBackgroundColor(0x00000000);
                }
            });
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.kazak_lang:
                changeLanguageAndRestart("kk-rKZ");
                return true;
            case R.id.rus_lang:
                changeLanguageAndRestart("ru");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeLanguageAndRestart(String language) {
        LocaleManager.setNewLocale(this, language);
        Intent intent = new Intent(this, MainActivity.class);
        for (int i = 0; i < menu.size(); i++) {
            if (toolbar.getTitle().equals(menu.get(i).CategoryName)) {
                intent.putExtra("MenuIndex", i);
                intent.putExtra("InnerMenu", false);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return;
            } else {
                for (int j = 0; j < menu.get(i).SubCategories.size(); j++) {
                    if (toolbar.getTitle().equals(menu.get(i).SubCategories.get(j).subMenuTitle)) {
                        intent.putExtra("MenuIndex", i);
                        intent.putExtra("SubMenuIndex", j);
                        intent.putExtra("InnerMenu", true);
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        return;
                    }
                }
            }
        }
        if(bottomSelectedItemIndex != -1) {
            intent.putExtra("BottomSelectedIndex", bottomSelectedItemIndex);
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }

    @Override
    public void onMenuItemClick(String name)
    {
        mDrawerLayout.closeDrawers();
        toolbar.setTitle(name);
        for (int i = 0; i < menu.size(); i++) {
            if (name.equals(menu.get(i).CategoryName)){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, menu.get(i).fragment)
                        .commit();
                break;
            } else {
                for (int j = 0; j < menu.get(i).SubCategories.size(); j++) {
                    if (name.equals(menu.get(i).SubCategories.get(j).subMenuTitle)){
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, menu.get(i).SubCategories.get(j).fragment)
                                .commit();
                        break;
                    }
                }
            }
        }
        findViewById(R.id.settings).setBackgroundColor(0xFF1c86ae);
        findViewById(R.id.ask_question).setBackgroundColor(0xFF1c86ae);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
