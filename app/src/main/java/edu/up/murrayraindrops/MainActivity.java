package edu.up.murrayraindrops;

//@author Ryan Murray

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private SeekBar seekBarDownUp, seekBarLeftRight;
    private Paint paint;
    private List<Raindrop> raindrops;
    private Raindrop mainRaindrop;
    private Random random = new Random();
    private int canvasSize = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.surfaceView);
        seekBarDownUp = findViewById(R.id.seekBarDownUp);
        seekBarLeftRight = findViewById(R.id.seekBarLeftRight);
        paint = new Paint();

        raindrops = generateRaindrops();
        mainRaindrop = raindrops.get(random.nextInt(raindrops.size()));

        drawCanvas();

        seekBarDownUp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mainRaindrop.y = progress;
                checkCollision();
                drawCanvas();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarLeftRight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mainRaindrop.x = progress;
                checkCollision();
                drawCanvas();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private List<Raindrop> generateRaindrops() {
        List<Raindrop> drops = new ArrayList<>();
        int numberOfDrops = random.nextInt(7) + 6; // Random number between 6 and 12 for amount of drops

        for (int i = 0; i < numberOfDrops; i++) {
            int x = random.nextInt(canvasSize - 60) + 30;
            int y = random.nextInt(canvasSize - 60) + 30;
            int color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            drops.add(new Raindrop(x, y, color));
        }

        return drops;
    }

    private void drawCanvas() {
        SurfaceHolder holder = surfaceView.getHolder();
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            canvas.drawColor(Color.WHITE); // Clear the canvas

            // Draw each raindrop
            for (Raindrop drop : raindrops) {
                paint.setColor(drop.color);
                canvas.drawCircle(drop.x, drop.y, 30, paint);
            }

            // Draw the main raindrop
            paint.setColor(mainRaindrop.color);
            canvas.drawCircle(mainRaindrop.x, mainRaindrop.y, 30, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void checkCollision() {
        for (int i = 0; i < raindrops.size(); i++) {
            Raindrop drop = raindrops.get(i);
            if (drop != mainRaindrop && isOverlapping(mainRaindrop, drop)) {
                // Merge colors
                mainRaindrop.color = averageColor(mainRaindrop.color, drop.color);
                raindrops.remove(i);
                break;
            }
        }
    }

    private boolean isOverlapping(Raindrop drop1, Raindrop drop2) {
        float distance = (float) Math.sqrt(Math.pow(drop1.x - drop2.x, 2) + Math.pow(drop1.y - drop2.y, 2));
        return distance < 60;
    }

    private int averageColor(int color1, int color2) {
        int r = (Color.red(color1) + Color.red(color2)) / 2;
        int g = (Color.green(color1) + Color.green(color2)) / 2;
        int b = (Color.blue(color1) + Color.blue(color2)) / 2;
        return Color.rgb(r, g, b);
    }
}