package com.ruffneck.cloudnote.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.activity.fragment.AllNoteBookFragment;
import com.ruffneck.cloudnote.activity.fragment.NoteBookFragment;
import com.ruffneck.cloudnote.db.DBConstants;
import com.ruffneck.cloudnote.models.note.NoteBook;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE_NEW_BOOK = 0x123;

    @InjectView(R.id.nv)
    NavigationView nv;
    @InjectView(R.id.drawer)
    DrawerLayout drawer;


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


        ButterKnife.inject(this);
        initDrawer();

        setDefaultFragment();
    }

    private void initDrawer() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, getToolbar(), R.string.open_drewer, R.string.close_drawer);
        toggle.syncState();
        drawer.setDrawerListener(toggle);

        initNoteBookMenu();

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
                startActivityForResult(new Intent(this, NewBookActivity.class), REQUEST_CODE_NEW_BOOK);
                break;
            //The notebook which is clicked.
            case R.id.action_all_note_book:
                setDefaultFragment();
                break;
            default:
                int notebookId = item.getItemId();
//                setMainFragment(new NoteBookFragment(noteBookDAO.queryById(notebookId)));
                setMainFragment(NoteBookFragment.newInstance(noteBookDAO.queryById(notebookId)));
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * set the drawer to the default selected state and the adapter.
     */
    public void setDefaultFragment(){
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

    private void setMainFragment(Fragment fragment) {
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
            }
        }else{
            setDefaultFragment();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
