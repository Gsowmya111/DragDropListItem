package com.example.lenovo.dragdroplistitem;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.dragdroplistitem.model.QQ;
import com.example.lenovo.dragdroplistitem.sdlv.src.main.java.com.yydcdut.sdlv.Menu;
import com.example.lenovo.dragdroplistitem.sdlv.src.main.java.com.yydcdut.sdlv.MenuItem;
import com.example.lenovo.dragdroplistitem.sdlv.src.main.java.com.yydcdut.sdlv.SlideAndDragListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by yuyidong on 16/1/23.
 */
public class DifferentActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener,
        SlideAndDragListView.OnDragListener, SlideAndDragListView.OnSlideListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private static final String TAG = DifferentActivity.class.getSimpleName();
    DataBaseHandler1 db= null;
    private List<Menu> mMenuList;
    private List<QQ> mQQList;
    private SlideAndDragListView<QQ> mListView;
    private Toast mToast;

    String roomnamesdb[];
    String imgnamesdb[];
    int startpos,endos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdlv);
   //     db.insertData("Entrance","1","1");

      db=new DataBaseHandler1(DifferentActivity.this);
      //  db.opendb();
        db.getWritableDatabase();
        int rowcount=db.numberOfRows();
        if(rowcount<=0) {
            db.insertData("Entrance","1","2","entrance");
            db.insertData("Cafeteria", "2", "4","cafeteria");
            db.insertData("Reception", "3", "3","reception");
            db.insertData("Bedroom", "4", "0","bed_room");
            db.insertData("Balcony", "5", "1","balcony");

        }

        initData();
        initMenu();
        initUiAndListener();
    //    pullout();
        mToast = Toast.makeText(DifferentActivity.this, "", Toast.LENGTH_SHORT);
    }

    public void fetchposi(int size){
      int newpositionslist[]=db.getposvalues(size);

        for(int j=0;j<newpositionslist.length;j++){

            Log.d("newpositionslist" , ""+newpositionslist[j]);

        }

    }

    public void fetchrooms(){
       List<String> roomlist=db.getData1();
         roomnamesdb=roomlist.toArray(new String[roomlist.size()]);
    }

    public void fetchimgnames(){
        List<String> imglist=db.getimagenames();
        imgnamesdb=imglist.toArray(new String[imglist.size()]);
    }

    public void initData() {

        fetchrooms();
        fetchimgnames();

        fetchposi(roomnamesdb.length);

        for(int j=0;j<roomnamesdb.length;j++){

           Log.d("test","room"+j+"  "+roomnamesdb[j]);


        }


        for(int j=0;j<imgnamesdb.length;j++){

            Log.d("test","imgname"+j+"  "+imgnamesdb[j]);


        }


       // int imgname[] = new int[imgnamesdb.length];
        mQQList = new ArrayList<>();

        for(int k=0;k<imgnamesdb.length;k++){

            String mDrawableName = imgnamesdb[k];
            int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
           //  imgname[k] = Integer.parseInt("R.drawable."+imgnamesdb[k]);
            mQQList.add(new QQ("", roomnamesdb[k],"",resID));


        }



    }

   public void initMenu() {
        mMenuList = new ArrayList<>(2);
        Menu menu0 = new Menu(true, 0);
        menu0.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn2_width))
                .setBackground(new ColorDrawable(Color.RED))
                .setText("DELETE")
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.WHITE)
                .setTextSize(10)
                .build());

        mMenuList.add(menu0);
        // mMenuList.add(menu1);
    }

    public void initUiAndListener() {
        mListView = (SlideAndDragListView) findViewById(R.id.lv_edit);
        mListView.setMenu(mMenuList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemLongClickListener(this);
        mListView.setOnDragListener(this, mQQList);
        mListView.setOnItemClickListener(this);
        //   mListView.setOnSlideListener(this);
        //   mListView.setOnMenuItemClickListener(this);
        mListView.setOnItemDeleteListener(this);
        // mListView.setDivider(new ColorDrawable(Color.GRAY));
        //  mListView.setDividerHeight(1);
    }

    private BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mQQList.size();
        }

        @Override
        public Object getItem(int position) {
            return mQQList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mQQList.get(position).hashCode();
        }

        @Override
        public int getItemViewType(int position) {
            if (mQQList.get(position).isQun()) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CustomViewHolder cvh = null;
            if (convertView == null) {
                cvh = new CustomViewHolder();
                convertView = LayoutInflater.from(DifferentActivity.this).inflate(R.layout.item_qq, null);
                cvh.imgLogo = (ImageView) convertView.findViewById(R.id.img_item_qq);
                // cvh.txtName = (TextView) convertView.findViewById(R.id.txt_item_qq_title);
                 cvh.txtContent = (TextView) convertView.findViewById(R.id.txt_item_qq_title);
                convertView.setTag(cvh);
            } else {
                cvh = (CustomViewHolder) convertView.getTag();
            }
            QQ item = (QQ) this.getItem(position);
            cvh.imgLogo.setImageResource(item.getDrawableRes());
             cvh.txtContent.setText(item.getContent());
            if (getItemViewType(position) == 0) {
                 // cvh.txtName.setText(item.getName());
            } else {
                // cvh.txtName.setText("(QQ群) " + item.getName() + "(" + item.getQunNumber() + ")");
            }
            return convertView;
        }

        class CustomViewHolder {
            public ImageView imgLogo;
             //public TextView txtName;
              public TextView txtContent;
        }

    };

    @Override
    public void onDragViewStart(int position) {
        mToast.setText("onDragViewStart   position--->" + position);
        mToast.show();
        Log.i(TAG, "onDragViewStart   " + position);
        startpos=position;
    }

    @Override
    public void onDragViewMoving(int position) {
//        Toast.makeText(DifferentActivity.this, "onDragViewMoving   position--->" + position, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onDragViewMoving   " + position);
    }

    @Override
    public void onDragViewDown(int position) {
        mToast.setText("onDragViewDown   position--->" + position);
        mToast.show();
        Log.i(TAG, "onDragViewDown   " + position);
        endos=position;

      //  Log.i(TAG, "startpos   " + startpos +"end pos"+endos);

        mToast.setText("startpos   " + startpos +"end pos"+endos);
        mToast.show();
        int listsize=mQQList.size();
        if(startpos>endos){
        for(int l=endos;l<startpos;l++) {
           // boolean insfg= db.updateroompos_whr_nameis(roomnamesdb[endos],""+(endos+1));
            boolean insfg= db.updateroompos_whr_nameis(roomnamesdb[l],""+(l+1));
            // new_pos_array[y]=
        }
        db.updateroompos_whr_nameis(roomnamesdb[startpos],""+(endos));
        }else{

            for(int l=startpos+1;l<=endos;l++) {
                boolean insfg= db.updateroompos_whr_nameis(roomnamesdb[l],""+(l-1));
                // new_pos_array[y]=
            }
            db.updateroompos_whr_nameis(roomnamesdb[startpos],""+(endos));
        }


    }

    @Override
    public void onSlideOpen(View view, View parentView, int position, int direction) {
        mToast.setText("onSlideOpen   position--->" + position + "  direction--->" + direction);
        mToast.show();
        Log.i(TAG, "onSlideOpen   " + position);
    }

    @Override
    public void onSlideClose(View view, View parentView, int position, int direction) {
        mToast.setText("onSlideClose   position--->" + position + "  direction--->" + direction);
        mToast.show();
        Log.i(TAG, "onSlideClose   " + position);
    }

    @Override
    public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
        Log.i(TAG, "onMenuItemClick   " + itemPosition + "   " + buttonPosition + "   " + direction);
        int viewType = mAdapter.getItemViewType(itemPosition);
        switch (viewType) {
            case 0:
                return clickMenuBtn0(buttonPosition, direction);
            case 1:
                return clickMenuBtn1(buttonPosition, direction);
            default:
                return Menu.ITEM_NOTHING;
        }
    }

    private int clickMenuBtn0(int buttonPosition, int direction) {
        switch (direction) {
            case MenuItem.DIRECTION_LEFT:
                switch (buttonPosition) {
                    case 0:
                        return Menu.ITEM_SCROLL_BACK;
                }
                break;
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:
                        return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                    case 1:
                        return Menu.ITEM_NOTHING;
                    case 2:
                        return Menu.ITEM_SCROLL_BACK;
                }
        }
        return Menu.ITEM_NOTHING;
    }

    private int clickMenuBtn1(int buttonPosition, int direction) {
        switch (direction) {
            case MenuItem.DIRECTION_LEFT:
                switch (buttonPosition) {
                    case 0:
                        return Menu.ITEM_SCROLL_BACK;
                }
                break;
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:
                        return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                    case 1:
                        return Menu.ITEM_SCROLL_BACK;
                }
        }
        return Menu.ITEM_NOTHING;
    }

    @Override
    public void onItemDeleteAnimationFinished(View view, int position) {
        mQQList.remove(position - mListView.getHeaderViewsCount());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //mListView.getId()
        mToast.setText("onItemClick   position--->" + position);
        mToast.show();
        Log.i(TAG, "onItemClick   " + position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        mToast.setText("onItemLongClick   position--->" + position);
        mToast.show();
        Log.i(TAG, "onItemLongClick   " + position);


        return false;
    }









}
