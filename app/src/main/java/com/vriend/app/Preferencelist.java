package com.vriend.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vriend.app.databinding.ActivityPreferencelistBinding;

import java.util.ArrayList;

public class Preferencelist extends AppCompatActivity {
    String[] listActs;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    TextView mItemSelected;
    private ActivityPreferencelistBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreferencelistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_preferencelist);
        listActs = getResources().getStringArray(R.array.Favorite_Activities);
        checkedItems = new boolean[listActs.length];
        binding.btn0rder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Preferencelist.this);
                 mBuilder.setTitle(R.string.dialog_title);
                mBuilder.setMultiChoiceItems(listActs, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            if(! mUserItems.contains(position)){
                                mUserItems.add(position);
                            }
                        } else if(mUserItems.contains(position))
                        {
                            mUserItems.remove(position);
                        }

                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String item ="";
                        for( i = 0; i<mUserItems.size();i++)
                        {
                            item = item + listActs[mUserItems.get(i)];
                            if(i != mUserItems.size()-1)
                            {
                                item = item + ", ";
                            }
                        }
                        mItemSelected.setText(item);


                    }
                }); //17:32 android multiple choice list dialog tutorial
                mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton(R.string.clear_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for(which = 0 ; which < checkedItems.length;which++)
                        {
                            checkedItems[which] = false;
                            mUserItems.clear();;
                            mItemSelected.setText("");
                        }

                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

    }
}