package com.ruffneck.cloudnote.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.activity.fragment.AllNoteBookFragment;
import com.ruffneck.cloudnote.activity.fragment.MainFragment;
import com.ruffneck.cloudnote.activity.fragment.NoteBookFragment;
import com.ruffneck.cloudnote.activity.fragment.RecycleFragment;
import com.ruffneck.cloudnote.db.DBConstants;
import com.ruffneck.cloudnote.models.note.NoteBook;
import com.ruffneck.cloudnote.service.CloudIntentService;
import com.ruffneck.cloudnote.utils.CloudUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    public static final int REQUEST_CODE_NEW_BOOK = 0x123;
    public static final int REQUEST_CODE_EDIT_BOOK = 0x122;
    protected ImageLoader loader = ImageLoader.getInstance();
    protected DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.placeholder_avatar_320)
            .showImageOnFail(R.drawable.placeholder_avatar_320)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();
    private SharedPreferences mPref;

    @InjectView(R.id.nv)
    NavigationView nv;
    @InjectView(R.id.drawer)
    DrawerLayout drawer;
    @InjectView(R.id.fab_main)
    FloatingActionButton fabMain;

    MainFragment mCurrentFragment;

    private SubMenu subMenu;
    private MenuItem mCurrentItem;

    /**
     * The following field must below of the menu order in the resource menu_drawer (10000)
     */
    private static final int ORDER_DEFAULT = 1001;
    private static final int ORDER_NOTEBOOK = 1002;
    private static final int ORDER_ADD_NOTEBOOK = 1003;
    private static final int ORDER_RECYCLE_BIN = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        ButterKnife.inject(this);
        initDrawer();


        setDefaultFragment();
    }

    private void initUserInfo() {
        AVUser avUser = AVUser.getCurrentUser();
        if (avUser != null) {
            View headerView = nv.getHeaderView(0);
            TextView tvUserEmail = (TextView) headerView.findViewById(R.id.tv_user_email);
            TextView tvUserName = (TextView) headerView.findViewById(R.id.tv_user_name);
            CircleImageView circlePortrait = (CircleImageView) headerView.findViewById(R.id.iv_portrait);
            tvUserEmail.setText(avUser.getEmail());
            tvUserName.setText(mPref.getString("nickname", avUser.getUsername()));
            loader.displayImage(mPref.getString("figureUrl", "http://www.sdfsdfafdc.xcx"), circlePortrait);
        }
    }

    private void initDrawer() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, getToolbar(), R.string.open_drewer, R.string.close_drawer);
        toggle.syncState();
        drawer.setDrawerListener(toggle);

        initNoteBookMenu();

        initUserInfo();
//        setMainFragment(new NewBookFragment());
    }

    /**
     * Find from the database to set the drawer's menu
     */
    private void initNoteBookMenu() {
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //put it into the activity to handle.
                if (mCurrentItem != item)
                    onOptionsItemSelected(item);
                //Handle the current menuitem.
                if (mCurrentItem != null) mCurrentItem.setChecked(false);
                item.setChecked(true);
                mCurrentItem = item;
                drawer.closeDrawers();
                return false;
            }
        });
        Menu menu = nv.getMenu();

        //Initialize the notebook menuItem.
        if (subMenu == null) {
            refreshNotebookSubMenu();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        setIconEnable(menu, true);
        Drawable uploadDrawable = getDrawable(R.drawable.ic_cloud_upload_24dp);
        Drawable downloadDrawable = getDrawable(R.drawable.ic_cloud_download_24dp);
        if (mCurrentFragment != null && mCurrentFragment.optionsMenu() != 0) {
            getMenuInflater().inflate(mCurrentFragment.optionsMenu(), menu);

            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                Drawable icon = item.getIcon();
                icon.setTint(mCurrentFragment.optionsMenuItemColor());
                item.setIcon(icon);
                if (uploadDrawable != null) {
                    uploadDrawable.setTint(mCurrentFragment.optionsMenuItemColor());
                }
                if (downloadDrawable != null) {
                    downloadDrawable.setTint(mCurrentFragment.optionsMenuItemColor());
                }
            }
        } else
            getMenuInflater().inflate(R.menu.menu_null, menu);


        MenuItem cloudUpLoad = menu.add(0, R.id.action_cloud_upload, 10, "备份").setIcon(uploadDrawable);
        MenuItem cloudDownLoad = menu.add(0, R.id.action_cloud_restore, 20, "同步").setIcon(downloadDrawable);

        return super.onCreateOptionsMenu(menu);
    }

    private void setIconEnable(Menu menu, boolean enable) {
        try {
            Class<?> clazz = Class.forName("android.support.v7.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);

            //设置默认是
            Field field = clazz.getDeclaredField("mDefaultShowAsAction");
            field.setAccessible(true);
            field.set(menu, SupportMenuItem.SHOW_AS_ACTION_ALWAYS);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Initialize the submenu of all the notebooks.
     */
    public void refreshNotebookSubMenu() {

        Menu menu = nv.getMenu();

        if (subMenu != null)
            subMenu.clear();
        subMenu = menu.addSubMenu(0, 0, 1000, R.string.submenu_title_notebook);
        List<NoteBook> noteBookList = noteBookDAO.queryAll();
        for (NoteBook noteBook : noteBookList) {
            MenuItem noteBookItem = null;
            if (noteBook.getId() == DBConstants.NoteBook.ID_DEFAULT_NOTEBOOK) {
                //If the noteBook is default.
                noteBookItem = subMenu.add(0, (int) noteBook.getId(), ORDER_DEFAULT, noteBook.getName());
                noteBookItem.setIcon(R.drawable.ic_event_note_black_24dp);
            } else if (noteBook.getId() == DBConstants.NoteBook.ID_RECYCLE_BIN) {
                //If the item is recycle bin.
                noteBookItem = subMenu.add(0, (int) noteBook.getId(), ORDER_RECYCLE_BIN, noteBook.getName());
                noteBookItem.setIcon(R.drawable.ic_delete_black_24dp);
            } else {
                //If the item is the diy notebook.
                noteBookItem = subMenu.add(0, (int) noteBook.getId(), ORDER_NOTEBOOK, noteBook.getName());
                noteBookItem.setIcon(R.drawable.ic_event_note_black_24dp);
            }
            noteBookItem.setCheckable(true);

        }

        MenuItem addItem = subMenu.add(0, R.id.menu_add_new_notebook, ORDER_ADD_NOTEBOOK, "新建笔记本");
        addItem.setIcon(R.drawable.ic_library_add_black_24dp);
        addItem.setCheckable(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.menu_add_new_notebook:
                startActivityForResult(new Intent(this, EditNoteBookActivity.class), REQUEST_CODE_NEW_BOOK);
                break;
            //The notebook which is clicked.
            case R.id.action_cloud_upload:
                syncAllData();
                break;
            case R.id.action_cloud_restore:
                restoreAllData();
                break;
            case R.id.action_all_note_book:
                setDefaultFragment();
                break;
            case R.id.action_setting:

                break;
            case R.id.action_user_info:

                break;
            case R.id.action_exit:
                AVUser.logOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            default:
                int notebookId = item.getItemId();
//                setMainFragment(new NoteBookFragment(noteBookDAO.queryById(notebookId)));
                if (noteBookDAO.exist(notebookId)) {
                    startNoteBookFragment(notebookId);
                }
                break;
        }

        if (mCurrentFragment != null)
            mCurrentFragment.onOptionsItemSelected(item);

        return super.onOptionsItemSelected(item);
    }

    private void restoreAllData() {
        CloudIntentService.restoreNote(this);
        CloudIntentService.restoreNoteBook(this);
        CloudIntentService.restoreAttach(this);
        Toast.makeText(MainActivity.this, "开始同步数据...", Toast.LENGTH_SHORT).show();
    }

    private void syncAllData() {
        CloudUtils.syncAllUnSyncData(this);
    }

    public void startNoteBookFragment(long notebookId) {

        if (notebookId != DBConstants.NoteBook.ID_RECYCLE_BIN)
            setMainFragment(NoteBookFragment.newInstance(noteBookDAO.queryById(notebookId)));
        else
            setMainFragment(RecycleFragment.newInstance(noteBookDAO.queryById(notebookId)));

        if (mCurrentItem != null) {
            mCurrentItem.setChecked(false);
        }

        mCurrentItem = nv.getMenu().findItem((int) notebookId);

        mCurrentItem.setChecked(true);

    }


    /**
     * set the drawer to the default selected state and the adapter.
     */

    public void setDefaultFragment() {
        setMainFragment(new AllNoteBookFragment());

        if (mCurrentItem != null) {
            mCurrentItem.setChecked(false);
        }
        mCurrentItem = nv.getMenu().findItem(R.id.action_all_note_book);
        mCurrentItem.setChecked(true);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ActionBar actionBar = getNewActionBar();
        actionBar.setTitle("");
    }

    @Override
    protected int setContentResId() {
        return R.layout.activity_main;
    }

    private void setMainFragment(MainFragment fragment) {
        mCurrentFragment = fragment;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_fragment, fragment);
        ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_NEW_BOOK:
                    refreshNotebookSubMenu();
                    setDefaultFragment();
                    break;
                case REQUEST_CODE_EDIT_BOOK:
                    NoteBook noteBook = data.getParcelableExtra("notebook");
                    refreshNotebookSubMenu();
                    startNoteBookFragment(noteBook.getId());
                    break;
            }
        } else {
            setDefaultFragment();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public FloatingActionButton getFab() {
        return fabMain;
    }
}
