package com.example.grocerylist;

import androidx.annotation.IntegerRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

public class RunActivity extends AppCompatActivity {


    DatabaseHandler db;

    ImageButton addData;
    ImageButton deleteData;
    EditText addItem;
    LinearLayout layout;

    ArrayList<String> listItem;

    int width = Resources.getSystem().getDisplayMetrics().widthPixels;
    int height = Resources.getSystem().getDisplayMetrics().heightPixels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noRotateNoStatusBar();
        setContentView(R.layout.activity_run);

        db = new DatabaseHandler(this);

        listItem = new ArrayList<>();

        addData = findViewById(R.id.imageButton);
        addItem = findViewById(R.id.editText);
        deleteData = findViewById(R.id.deleteButton);

        viewData();

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = addItem.getText().toString();

                if(!item.equals("") &&db.insertIntoDatabase(item)){

                    Toast.makeText(getApplicationContext(),"SUCCESS",Toast.LENGTH_SHORT).show();
                    addItem.getText().clear();
                    viewData();
                }
                else{
                    Toast.makeText(getApplicationContext(),"FAILED",Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteData();
                layout.removeAllViews();
                Toast.makeText(getApplicationContext(),"DELETED",Toast.LENGTH_SHORT).show();

            }
        });


        hideNavBar();
    }

    private void viewData() {
        final Cursor cursor = db.viewData();
        layout = findViewById(R.id.linearlayoutid);
        layout.removeAllViews();
        if(cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(),"NO DATA",Toast.LENGTH_SHORT).show();
        }
        else{

            while(cursor.moveToNext()) {
                final Button button = new Button(this);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                params.gravity = Gravity.LEFT;
                params.height = 120;
                params.width = width - 50;
                params.bottomMargin = 15;


                Typeface typeface = Typeface.createFromAsset(getAssets(), "font/text.ttf");

                button.setTypeface(typeface);
                button.setLayoutParams(params);
                button.setId(cursor.getInt(0));
                button.setText(cursor.getString(1));



                if(cursor.getInt(2) == 0) {
                    button.setBackgroundResource(R.drawable.buy_item);
                }
                else{
                    button.setBackgroundResource(R.drawable.got_item);
                }

                layout.addView(button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.addToBasket(button.getId());
                        viewData();
                        Toast.makeText(getApplicationContext(),"Marked",Toast.LENGTH_SHORT).show();
                    }
                });
                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Toast.makeText(getApplicationContext(),"Unmarked",Toast.LENGTH_SHORT).show();
                        db.removeFromBasket(button.getId());
                        viewData();
                        return true;
                    }
                });





            }
        }
    }


    private void noRotateNoStatusBar(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    private void hideNavBar(){
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}