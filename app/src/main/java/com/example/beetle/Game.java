package com.example.beetle;

import android.annotation.SuppressLint;
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
import com.example.beetle.runner.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@SuppressLint("Registered")
public class Game extends AppCompatActivity {

    private Grid grid;
    Runner runner;
    boolean isRunning = false;

    void moveBack(ImageView beetle, CustomGridView table) {
        // ImageView beetle = findViewById(R.id.beetle);
        if (!isRunning) {
            beetle.animate().translationX(0).translationY(getResources().getDimension(R.dimen.bar_height) - table.computeVerticalScrollOffset()).rotation(180).start();

        }
    }

    @SuppressLint("NewApi")
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
        moveBack(beetle, table);
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
                moveBack(beetle, table);
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
                count[0] = 0;
                counter.setText(String.valueOf(count[0]));
                isRunning = true;
                List<Vector> moves = runner.run();
                final Iterator<Vector> it = moves.iterator();
                Runnable draw = new Runnable() {
                    @Override
                    public void run() {
                        if (!it.hasNext()) {
                            isRunning = false;
                            moveBack(beetle, table);
                            return;
                        }
                        count[0]++;
                        counter.setText(String.valueOf(count[0]));
                        Vector vector = it.next();

                        int rotation;

                        if (vector.getDx() == 1) {
                            rotation = 180;
                        } else if (vector.getDx() == -1) {
                            rotation = 0;
                        } else if (vector.getDy() == 1) {
                            rotation = 90;
                        } else {
                            rotation = 270;
                        }

                        beetle.animate().rotation(rotation);

                        beetle
                                .animate()
                                .translationXBy(adapter.getItemSize() * vector.getDy())
                                .translationYBy(adapter.getItemSize() * vector.getDx())
                                .setDuration(getResources().getInteger(R.integer.speed))
                                .withEndAction(this)
                                .start();

                    }
                };
                draw.run();
            }
        });
    }

}
