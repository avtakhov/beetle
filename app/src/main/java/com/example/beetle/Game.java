package com.example.beetle;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
            beetle.animate().translationX(0).translationY(getResources().getDimension(R.dimen.bar_height) - table.computeVerticalScrollOffset()).start();
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

        Button run = findViewById(R.id.run);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = true;
                List<Move> moves = runner.run();
                moves.add(new Move(0, 0));
                final Iterator<Move> it = moves.iterator();
                Runnable draw = new Runnable() {
                    @Override
                    public void run() {
                        if (!it.hasNext()) {
                            isRunning = false;
                            return;
                        }
                        Move move = it.next();
                        beetle
                                .animate()
                                .translationX(adapter.getItemSize() * move.getC())
                                .translationY(getResources().getDimension(R.dimen.bar_height) + adapter.getItemSize() * move.getR() - table.computeVerticalScrollOffset())
                                .setDuration(50)
                                .withEndAction(this)
                                .start();

                    }
                };
                draw.run();
            }
        });
    }

}
