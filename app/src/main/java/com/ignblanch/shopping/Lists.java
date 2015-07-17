package com.ignblanch.shopping;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

// Both fragments interact with these lists
public class Lists {
	private static Context context;
	private static ArrayList<String> products = new ArrayList<String>();
	private static ArrayList<String> shoppingList = new ArrayList<String>();
	
	public static void addProducts(String product){
		products.add(product);
	}
	
	public static void removeProducts(String product){
		products.remove(product);
	}
	
	public static ArrayList<String> getProducts(){
		return products;
	}
	
	public static void addItem(String item){
		shoppingList.add(item);
	}
	
	public static ArrayList<String> getItems(){
		return shoppingList;
	}
	
	public static void removeItem(String item){
		shoppingList.remove(item);
	}


}
