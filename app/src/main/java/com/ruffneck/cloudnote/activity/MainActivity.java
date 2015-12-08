package com.ruffneck.cloudnote.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.db.DBConstants;
import com.ruffneck.cloudnote.db.NoteBookDAO;
import com.ruffneck.cloudnote.models.note.NoteBook;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.nv)
    NavigationView nv;
    @InjectView(R.id.drawer)
    DrawerLayout drawer;

    private NoteBookDAO noteBookDAO;


    private SubMenu subMenu;
    /**
     * The following field must below of the menu order in the resource menu_drawer (10000).
     */
    public static final int ORDER_DEFAULT = 1;
    public static final int ORDER_NOTEBOOK = 2;
    public static final int ORDER_ADD_NOTEBOOK = 3;
    public static final int ORDER_RECYCLE_BIN = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        noteBookDAO = NoteBookDAO.getInstance(this);

        ButterKnife.inject(this);
        initDrawer();
    }

    private void initDrawer() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, getToolbar(), R.string.open_drewer, R.string.close_drawer);
        toggle.syncState();
        drawer.setDrawerListener(toggle);

        initNoteBookMenu();

    }

    /**
     * Find from the database to set the drawer's menu
     */
    private void initNoteBookMenu() {
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            MenuItem mPreMenuItem;

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //put it into the activity to handle.
                onOptionsItemSelected(item);
                //Handle the current menuitem.
                if (mPreMenuItem != null) mPreMenuItem.setChecked(false);
                item.setChecked(true);
                mPreMenuItem = item;
                drawer.closeDrawers();
                return false;
            }
        });
        Menu menu = nv.getMenu();

        //Initialize the notebook menuItem.
        if (subMenu == null) {
            initNoteBookSubMenu(menu);
        }


    }


    /**
     * Initialize the submenu of all the notebooks.
     */
    private void initNoteBookSubMenu(Menu menu) {

        subMenu = menu.addSubMenu(R.string.submenu_title_notebook);
        List<NoteBook> noteBookList = noteBookDAO.queryAll();
        for (NoteBook noteBook : noteBookList) {
            MenuItem noteBookItem = null;
            if (noteBook.getId() == DBConstants.NoteBook.ID_DEFAULT_NOTEBOOK) {
                //If the noteBook is default.
                noteBookItem = subMenu.add(R.id.menu_add_new_notebook, (int) noteBook.getId(), ORDER_DEFAULT, noteBook.getName());
                noteBookItem.setIcon(R.drawable.ic_event_note_black_24dp);
                //The default choose item.
                noteBookItem.setChecked(true);
            }else if(noteBook.getId() == DBConstants.NoteBook.ID_RECYCLE_BIN){
                //If the item is recycle bin.
                noteBookItem = subMenu.add(R.id.menu_add_new_notebook, (int) noteBook.getId(), ORDER_RECYCLE_BIN, noteBook.getName());
                noteBookItem.setIcon(R.drawable.ic_delete_black_24dp);
            }else{
                //If the item is the diy notebook.
                noteBookItem = subMenu.add(R.id.menu_add_new_notebook, (int) noteBook.getId(), ORDER_NOTEBOOK, noteBook.getName());
                noteBookItem.setIcon(R.drawable.ic_event_note_black_24dp);
            }
            noteBookItem.setCheckable(true);

        }

        MenuItem addItem = subMenu.add(R.id.menu_add_new_notebook, R.id.menu_add_new_notebook, ORDER_ADD_NOTEBOOK, "新建笔记本");
        addItem.setIcon(R.drawable.ic_library_add_black_24dp);
        addItem.setCheckable(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_add_new_notebook:
                break;
            //The notebook which is clicked.
            default:
                Toast.makeText(MainActivity.this, item.getItemId() + "", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
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

}
