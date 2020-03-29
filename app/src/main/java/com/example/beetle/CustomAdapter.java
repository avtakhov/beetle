package com.example.beetle;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.beetle.album.Cell;
import com.example.beetle.album.Grid;

import java.util.Map;

class CustomAdapter extends BaseAdapter {

    private final Context context;
    private final Grid grid;
    private final Map<Cell, Integer> image;

    CustomAdapter(Context context, Grid grid, Map<Cell, Integer> image) {
        Log.d("TAG", "CREATED NEW ADAPTER");
        this.context = context;
        this.grid = grid;
        this.image = image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = (ImageView) convertView;
        if (imageView == null) {
            imageView = new ImageView(context);

            int size = getItemSize();
            imageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        Cell cell = grid.getCell(position / getM(), position % getM());
        Integer resource = image.get(cell);
        if (resource == null) {
            throw new IllegalArgumentException("Invalid grid");
        }
        imageView.setImageResource(resource);

        return imageView;
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public Integer getItem(int position) {
        Cell cell = grid.getCell(position / getM(), position % getM());
        return image.get(cell);
    }

    @Override
    public int getCount() {
        return getN() * getM();
    }

    private int getHeight() {
        return this.context.getResources().getDisplayMetrics().heightPixels;
    }

    private int getWidth() {
        return this.context.getResources().getDisplayMetrics().widthPixels;
    }

    private int getN() {
        return grid.getN();
    }

    private int getM() {
        return grid.getM();
    }

    public int getItemSize() {
        return getWidth() / getM();
    }
}