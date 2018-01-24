package com.stepcounter.stepcounter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.stepcounter.R;
import com.stepcounter.data.DatabaseHandler;
import com.stepcounter.data.GlobalClass;
import com.stepcounter.report.Step_Counter_Report;
import java.util.Timer;
import java.util.TimerTask;
import static java.lang.Math.sqrt;


public class Step_Counter_Activity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor senAccelerometer;
    public static final float ALPHA = (float) 0.7;

    float [] cachedAccelerometer = {0,0,0};
    float cachedAcceleration = 0;
    private int numSteps = 0;
    private long lastStepCountTime = 0;
    TextView tv_steps;
    Context context;
    LinearLayout ll_progress,ll_timer;
    TextView tv_timer,tv_steps_count,tv_steps_speed,tv_prepare,tv_heading;
    ImageView iv_play,iv_pause,iv_stop,iv_back,iv_setting,iv_report;
    ProgressBar progress;
    long temp_milis;
    Timer t;
    boolean isPlaying=false,isPaused=false;
    long total_seconds=System.currentTimeMillis();
    long temp_total=0;
    int glsteps;
    Vibrator v;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter_activity);

        context         = this;
        tv_steps        = (TextView) findViewById(R.id.tv_steps);
        tv_timer        = (TextView) findViewById(R.id.tv_timer);
        tv_steps_count  = (TextView) findViewById(R.id.tv_steps_count);
        tv_steps_speed  = (TextView) findViewById(R.id.tv_step_speed);
        tv_prepare      = (TextView) findViewById(R.id.tv_prepare);
        tv_heading      = (TextView) findViewById(R.id.tv_heading);

        iv_play         = (ImageView) findViewById(R.id.iv_start);
        iv_pause        = (ImageView) findViewById(R.id.iv_pause);
        iv_stop         = (ImageView) findViewById(R.id.iv_stop);
        iv_back         = (ImageView) findViewById(R.id.im_backbutton);
        iv_setting      = (ImageView) findViewById(R.id.iv_setting);
        iv_report       = (ImageView) findViewById(R.id.iv_report);

        ll_progress     = (LinearLayout) findViewById(R.id.ll_prepration);
        ll_timer        = (LinearLayout) findViewById(R.id.ll_timer);
        progress        = (ProgressBar) findViewById(R.id.circularProgressbar);

        glsteps         = GlobalClass.callSavedPreferences2("steps",glsteps,context);
        v               = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        db              = new DatabaseHandler(context);

        if (glsteps==0){
            glsteps=1000;
        }
        progress.setMax(glsteps);

        mSensorManager   = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (senAccelerometer!=null)
            mSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_FASTEST);

        iv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPaused && !isPlaying)
                startActivity(new Intent(context,Step_Counter_Report.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepsDialog();
            }
        });

        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              temp_milis=System.currentTimeMillis();
                iv_play.setVisibility(View.GONE);
                if (!isPaused) {
                    t = new Timer();
                    //Set the schedule function and rate
                    t.scheduleAtFixedRate(new TimerTask() {

                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    Animation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
                                            0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                                            Animation.RELATIVE_TO_SELF, 0.5f);
                                    Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                                    AnimationSet animationSet = new AnimationSet(false);
                                    animationSet.addAnimation(scaleAnimation);
                                    animationSet.addAnimation(alphaAnimation);
                                    animationSet.setDuration(1000);
                                    tv_prepare.setAnimation(animationSet);
                                    tv_prepare.setText(String.valueOf(3 - ((System.currentTimeMillis() - temp_milis) / 1000)));

                                    if ((System.currentTimeMillis() - temp_milis) / 1000 >= 3) {
                                        t.cancel();
                                        ll_timer.setVisibility(View.VISIBLE);
                                        ll_progress.setVisibility(View.GONE);

                                        startTimer();
                                    }
                                }

                            });
                        }

                    }, 0, 1000);
                }
                else
                    startTimer();
            }
        });

        iv_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying=false;
                isPaused=true;
                temp_total=total_seconds;
                t.cancel();
                iv_pause.setVisibility(View.GONE);
                iv_play.setVisibility(View.VISIBLE);
                iv_stop.setVisibility(View.VISIBLE);
            }
        });

        iv_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying=false;
                isPaused=false;
                t.cancel();
                if(numSteps>0) db.addSteps(numSteps,glsteps,(int)total_seconds);
                ll_progress.setVisibility(View.VISIBLE);
                ll_timer.setVisibility(View.GONE);
                iv_stop.setVisibility(View.GONE);
                iv_play.setVisibility(View.VISIBLE);
                iv_pause.setVisibility(View.GONE);
                numSteps=0;
                temp_total=0;
                tv_prepare.setText("Start");
                tv_timer.setText("00:00:00");
                progress.setProgress(0);
                tv_steps_count.setText("_");
                tv_steps_speed.setText("_S/Min");
                tv_steps.setText("");

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        long currTime = System.currentTimeMillis();

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            // Low-pass filter to remove noise
            cachedAcceleration = (float) ((1-ALPHA) * cachedAcceleration + ALPHA * sqrt(x*x + y*y + z*z));

            // Copy new values into the cachedAccelerometer array (We didn't use these values for step counter)
            System.arraycopy(sensorEvent.values, 0, cachedAccelerometer, 0, sensorEvent.values.length);

            // Peak is substantial enough to be correlated to a step
            if(cachedAcceleration > 11.5)
            {
                // There needs to be at least 300ms between two peaks, otherwise it isn't a step.
                if (currTime - lastStepCountTime > 300 && isPlaying)
                {
                    numSteps++;
                    lastStepCountTime = currTime;

                    Animation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
                            0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                    AnimationSet animationSet = new AnimationSet(false);
                    animationSet.addAnimation(scaleAnimation);
                    animationSet.addAnimation(alphaAnimation);
                    animationSet.setDuration(1000);
                    tv_steps.setAnimation(animationSet);
                    tv_steps.setText(String.valueOf(numSteps));
                    tv_steps_count.setText(String.valueOf(numSteps)+"/"+String.valueOf(glsteps));
                    progress.setProgress(numSteps);
                    if (total_seconds==0)
                        total_seconds=60;
                    tv_steps_speed.setText(String.valueOf((numSteps*60)/total_seconds)+" S/Min");
                    if (numSteps>=glsteps){
                        v.vibrate(500);
                        Toast.makeText(context, "You have Reached The Limit of Number of Steps You have Set Today!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void startTimer(){
        temp_milis=System.currentTimeMillis();
        if (!isPaused) {
            numSteps = 0;
            progress.setProgress(0);
        }
        iv_play.setVisibility(View.GONE);
        iv_pause.setVisibility(View.VISIBLE);
        iv_stop.setVisibility(View.GONE);
        isPaused=false;
        isPlaying=true;
        t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        tv_timer.setText(setCountDownTime(((System.currentTimeMillis()-temp_milis)/1000)+temp_total));
                        total_seconds=(((System.currentTimeMillis()-temp_milis)/1000)+temp_total);
                    }

                });
            }

        }, 0, 1000);
    }

    public String setCountDownTime(long tempsec) {
        long hr  = tempsec / 3600;
        long v   = tempsec % 3600;
        long min = v / 60;
        long sec = v % 60;
        String time = String.format(" %02d : %02d : %02d", hr, min, sec);

        return time;
    }

    public void stepsDialog(){
        final EditText input = new EditText(Step_Counter_Activity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(5);
        input.setFilters(FilterArray);
        final AlertDialog d = new AlertDialog.Builder(context)
                .setView(input)
                .setTitle(Html.fromHtml("<font color='#C2185B'>"+getResources().getString(R.string.Step)+"</font>"))
                .setMessage(Html.fromHtml("<font color='#FF7F27'>"+getResources().getString(R.string.Between_steps)+"</font>"))
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        d.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (input.getText().toString().trim().length()>0 && Integer.parseInt(input.getText().toString())>=5 && Integer.parseInt(input.getText().toString())<=10000){
                            //forgot_password(input.getText().toString());
                            GlobalClass.savePreferences2("steps",Integer.parseInt(input.getText().toString()),context);
                            glsteps = Integer.parseInt(input.getText().toString());
                            progress.setMax(glsteps);
                            d.dismiss();
                        }
                        else
                            input.setError("Enter valid number");
                    }
                });
            }
        });
        d.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}