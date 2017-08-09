package com.akafle.itunessearch;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("Search for tracks");
                    final EditText user_input = new EditText(MainActivity.this);
                    LinearLayout.LayoutParams mlayparam = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    user_input.setLayoutParams(mlayparam);
                    alertDialog.setView(user_input);
                    alertDialog.setIcon(R.drawable.delete);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            FragmentManager fm = getSupportFragmentManager();
                            Fragment songsListFragment = new SearchListFragment();
                            Bundle args = new Bundle();
                            args.putString("key", user_input.getText().toString());
                            songsListFragment.setArguments(args);
                            fm.beginTransaction().replace(R.id.container, songsListFragment).commit();

                        }
                    });


                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Oops,Cancelled", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });


                    alertDialog.show();

                    return true;
                }
            });
        return super.onCreateOptionsMenu(menu);
        }
    }







