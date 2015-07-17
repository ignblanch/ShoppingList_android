package com.ignblanch.shopping;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ProductListFragment extends Fragment {
	Context context;
	Button addButton;
	ToggleButton removeButton;
	ListView list;
	ArrayAdapter<String> adapterProducts;
	ArrayAdapter<String> adapterChoice;
	View rootView;
	SparseBooleanArray sp;

	// to display list in ListView
	private void setupList() {
        getData(rootView);
		list = (ListView) rootView.findViewById(R.id.listProducts);
		list.setFastScrollEnabled(true);
		adapterProducts = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, Lists.getProducts());
		Collections.sort(Lists.getProducts(), String.CASE_INSENSITIVE_ORDER); // Keep items sorted
		adapterProducts.notifyDataSetChanged();
		list.setAdapter(adapterProducts);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {

				if (!Lists.getItems().contains(
						Lists.getProducts().get(position))) {

					Lists.addItem(Lists.getProducts().get(position));

					final Toast toast = Toast.makeText(context, "Added!",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
					toast.show();
					// To make duration of toast shorter than default short
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							toast.cancel();
						}
					}, 250);

				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setMessage("Product Already Added");
					builder.setNegativeButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// User clicked OK button
								}
							});
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		});

	}
    //Saves data into SharedPreferences
    public void saveData(View rootView){
        Set<String> set = new HashSet<String>(Lists.getProducts());
        SharedPreferences sharedPrefs = context.getSharedPreferences("products", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putStringSet("products", set);
        editor.commit();
        Toast.makeText(context, "The data has been saved", Toast.LENGTH_LONG).show();
    }

    //get saved data
    public void getData(View rootView){
        final Set<String> DEFAULT = new HashSet<String>(Lists.getProducts());
        SharedPreferences sharedPrefs = context.getSharedPreferences("products", Context.MODE_PRIVATE);
        Set<String> set = sharedPrefs.getStringSet("products", DEFAULT);
        if("products".equals(DEFAULT)&&"shopping".equals(DEFAULT)){
            Toast.makeText(context, "Data could not be recovered", Toast.LENGTH_LONG);
        }else{
            Lists.getProducts().clear();       // clear previous data
            Lists.getProducts().addAll(set);   // load new data from prefs
            Toast.makeText(context, "Data recovered", Toast.LENGTH_LONG); // confirm
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.list_product, container, false);
		context = container.getContext();
		setupList();

		addButton = (Button) rootView.findViewById(R.id.button1);
		addButton.setOnClickListener(new View.OnClickListener() {
            TextView text;
            Button dialogButtonOk;
            Button dialogButtonCancel;

            @Override
            public void onClick(View v) {

                // Open dialog box to enter new products
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Add new product...");

                // Init elements in dialog box
                text = (TextView) dialog.findViewById(R.id.editText1);
                dialogButtonOk = (Button) dialog
                        .findViewById(R.id.dialogButtonOK);

                dialogButtonOk.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        do {
                            if (Lists.getProducts().contains(
                                    text.getText().toString())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        getActivity());
                                builder.setMessage("Product Already Exists");
                                builder.setNegativeButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int id) {
                                                // User clicked OK button
                                            }
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {
                                if (text.getText().length() > 0) {
                                    Lists.addProducts(text.getText().toString());
                                    text.setText("");
                                    removeButton.setEnabled(true);
                                    saveData(rootView);
                                    setupList();
                                } else {
                                    //do nothing
                                }
                            }
                        } while (text.getText() != null
                                && text.getText().length() < 0);
                    }

                });

                dialogButtonCancel = (Button) dialog
                        .findViewById(R.id.dialogButtonCancel);
                dialogButtonCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        saveData(rootView);
                    }
                });
                dialog.show();

            }
        });

		removeButton = (ToggleButton) rootView.findViewById(R.id.button2);
		if(Lists.getProducts().isEmpty()) {
			removeButton.setEnabled(false);
		}
		removeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    list.setAdapter(new ArrayAdapter<String>(
                            context, android.R.layout.simple_list_item_multiple_choice, Lists.getProducts()));
                    list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    list.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, final int position, long id) {
                            sp = list.getCheckedItemPositions();
                        }
                    });

                } else {
                    if (sp != null) {
                        AlertDialog.Builder build = new AlertDialog.Builder(
                                getActivity());
                        build.setMessage("" + sp.size() + " products will be deleted, are you sure?");
                        build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int id) {
                                // User clicked OK button
                                if (sp != null) {
                                    for (int i = sp.size() - 1; i >= 0; i--) {
                                        // Item position in adapter
                                        int position = sp.keyAt(i);

                                        if (sp.valueAt(i))
                                            Lists.removeProducts(Lists.getProducts().get(position));
                                    }
                                    if (Lists.getProducts().isEmpty()) {
                                        removeButton.setEnabled(false);
                                    }
                                    adapterProducts.notifyDataSetChanged();
                                    list.setAdapter(adapterProducts);
                                    saveData(rootView);
                                    setupList();
                                    sp.clear();

                                } else {
                                    saveData(rootView);
                                    setupList();
                                    sp.clear();
                                }

                            }
                        });
                        build.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int id) {
                                        // User clicked Cancel button
                                        dialog.dismiss();
                                        sp.clear();
                                        setupList();
                                    }
                                });
                        AlertDialog dialog = build.create();
                        dialog.show();
                    } else {
                        saveData(rootView);
                        setupList();
                    }
                }
            }
        });

		return rootView;
	}
}
