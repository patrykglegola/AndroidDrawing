package pg.androiddrawing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button redButton;
    private Button yellowButton;
    private Button blueButton;
    private Button greenButton;
    private Button clearButton;
    private DrawingSurface drawingSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findAllButtons();
        setupDrawingSurface();
        setupButtonsListeners();
    }

    private void setupDrawingSurface() {
        drawingSurface = (DrawingSurface) findViewById(R.id.activty_main_drawing_surface);
    }

    private void setupButtonsListeners() {
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingSurface.selectRedPaint();
            }
        });
        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingSurface.selectYellowPaint();
            }
        });
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingSurface.selectBluePaint();
            }
        });
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingSurface.selectGreenPaint();
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingSurface.clear();
            }
        });
    }

    private void findAllButtons() {
        redButton = (Button) findViewById(R.id.activity_main_red_button);
        yellowButton = (Button) findViewById(R.id.activity_main_yellow_button);
        blueButton = (Button) findViewById(R.id.activity_main_blue_button);
        greenButton = (Button) findViewById(R.id.activity_main_green_button);
        clearButton = (Button) findViewById(R.id.activity_main_clear_button);

    }
}
