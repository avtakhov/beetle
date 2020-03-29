package com.example.beetle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.beetle.album.*;
import com.example.beetle.base.Pair;
import com.example.beetle.runner.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@SuppressLint("Registered")
public class Game extends AppCompatActivity {

    private Grid grid;
    Runner runner;
    boolean isRunning = false;

    void moveBack(ImageView beetle, CustomGridView table, boolean isNeeded) {
        // ImageView beetle = findViewById(R.id.beetle);
        if (isNeeded) {
            beetle.animate().translationX(0).translationY(getResources().getDimension(R.dimen.bar_height) - table.computeVerticalScrollOffset()).rotation(0).start();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        if (this.getActionBar() != null) {
            this.getActionBar().hide();
        }

        grid = new Album(
                this.getResources().getInteger(R.integer.row_count),
                this.getResources().getInteger(R.integer.column_count)
        );

        runner = new BeetleRunner(grid);

        final TextView counter = findViewById(R.id.counter);
        final int[] count = {0};

        final CustomAdapter adapter = new CustomAdapter(
                this,
                grid,
                new HashMap<Cell, Integer>() {
                    {
                        put(Cell.EMPTY, R.drawable.empty_cell);
                        put(Cell.WALL, R.drawable.wall);
                    }
                }
        );
        final ImageView beetle = findViewById(R.id.beetle);
        beetle.setLayoutParams(new ConstraintLayout.LayoutParams(adapter.getItemSize(), adapter.getItemSize()));

        final Toast noWay = Toast.makeText(this, "No way", Toast.LENGTH_SHORT);

        final CustomGridView table = findViewById(R.id.table);
        table.setAdapter(adapter);
        moveBack(beetle, table, true);
        table.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean changed = grid.change(position / grid.getM(), position % grid.getM());
                if (changed) {
                    adapter.notifyDataSetChanged();
                } else {
                    noWay.show();
                }
            }
        });

        table.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                moveBack(beetle, table, !isRunning);
            }
        });

        ImageView clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grid.clear();
                adapter.notifyDataSetChanged();
            }
        });

        ImageView run = findViewById(R.id.run);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBack(beetle, table, true);
                isRunning = true;
                count[0] = 0;
                counter.setText(String.valueOf(count[0]));
                List<Pair<Move, Vector>> moves = runner.run();
                final Iterator<Pair<Move, Vector>> it = moves.iterator();
                Runnable draw = new Runnable() {
                    @Override
                    public void run() {
                        if (!it.hasNext()) {
                            isRunning = false;
                            moveBack(beetle, table, true);
                            return;
                        }
                        count[0]++;
                        counter.setText(String.valueOf(count[0]));
                        Pair<Move, Vector> move = it.next();

                        int rotation;

                        if (move.second.getDx() == 1) {
                            rotation = 180;
                        } else if (move.second.getDx() == -1) {
                            rotation = 0;
                        } else if (move.second.getDy() == 1) {
                            rotation = 90;
                        } else {
                            rotation = 270;
                        }

                        beetle.animate().rotation(rotation);

                        beetle
                                .animate()
                                .translationX(adapter.getItemSize() * move.first.getC())
                                .translationY(adapter.getItemSize() * move.first.getR() + getResources().getDimension(R.dimen.bar_height) - table.computeVerticalScrollOffset())
                                .setDuration(getResources().getInteger(R.integer.speed))
                                .withEndAction(this)
                                .start();

                    }
                };
                draw.run();
            }
        });
    }

    /*
    @Override
    public Object onRetainNonConfigurationInstance() {
        return grid;
    }
    */


}
