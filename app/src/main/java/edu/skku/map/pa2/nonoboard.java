package edu.skku.map.pa2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class nonoboard extends BaseAdapter {
    Context c;
    Bitmap items[];
    int check_board[];

    nonoboard(Context c,Bitmap arr[],int array_board[]){
        this.c=c;
        items=arr;
        check_board=array_board;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.grid_layout,null);
        }
        ImageView imageView= convertView.findViewById(R.id.imageView);

        items[position].eraseColor(Color.WHITE);
        imageView.setImageBitmap(items[position]);
        if(check_board[position]!=0){
            imageView.setColorFilter(Color.BLACK);
        }
        return convertView;
    }
}


