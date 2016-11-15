package io.particle.cloudsdk.example_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.ParticleEvent;
import io.particle.android.sdk.cloud.ParticleEventHandler;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.Toaster;

public class ValueActivity extends Activity {

    private static final String ARG_VALUE = "ARG_VALUE";
    private static final String ARG_DEVICEID = "ARG_DEVICEID";

    private boolean ledStatus1;
    private boolean ledStatus2;
    private ParticleDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_value);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Xenotron.ttf");
        TextView temporary = (TextView)findViewById(R.id.temperature);
        temporary.setTypeface(myTypeface);
        temporary = (TextView)findViewById(R.id.humidity);
        temporary.setTypeface(myTypeface);

        //...
        // Do network work on background thread
        Async.executeAsync(ParticleCloud.get(ValueActivity.this), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(ParticleCloud ParticleCloud) throws ParticleCloudException, IOException {
                device = ParticleCloud.getDevice(getIntent().getStringExtra(ARG_DEVICEID));
                VariableList ValueRecieved = new VariableList();

                long subscriptionId;  // save this for later, for unsubscribing
                subscriptionId = device.subscribeToEvents(
                        "ledChange",  // the first argument, "eventNamePrefix", is optional
                        new ParticleEventHandler() {
                            public void onEvent(String eventName, ParticleEvent event) {

                                if(event.dataPayload.equals("ON0")) {
                                    ledStatus1 = true;
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            findViewById(R.id.button1).setBackgroundColor(0xFF00FF00);
                                        }
                                    });
                                }
                                else if(event.dataPayload.equals("OFF0")) {
                                    ledStatus1 = false;
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            findViewById(R.id.button1).setBackgroundColor(0xFFFF0000);
                                        }
                                    });
                                }
                                else if(event.dataPayload.equals("ON1")) {
                                    ledStatus2 = true;
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            findViewById(R.id.button2).setBackgroundColor(0xFF00FF00);
                                        }
                                    });
                                }
                                else if(event.dataPayload.equals("OFF1")) {
                                    ledStatus2 = false;
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            findViewById(R.id.button2).setBackgroundColor(0xFFFF0000);
                                        }
                                    });
                                }
                            }

                            public void onEventError(Exception e) {
                                Log.d("sometag", "Event error: ", e);
                            }
                        });

                /*
                try {
                    //ArrayList<String> temp = new ArrayList<String>();
                    //temp.add("1");
                    //int resultCode = device.callFunction("checkStatus", temp);
                    //Toaster.s(ValueActivity.this, "Result of calling checkStatus: " + resultCode);
                    //ValueRecieved.addInteger((int) device.getIntVariable("analogvalue"));
                    //ValueRecieved.addInteger((int) device.getIntVariable("analogvalue1"));
                    // } catch (ParticleDevice.FunctionDoesNotExistException e) {
                    //Toaster.l(ValueActivity.this, e.getMessage());
                    //variable = -1;
                } catch (ParticleDevice.VariableDoesNotExistException e) {
                    Toaster.l(ValueActivity.this, e.getMessage());
                    ValueRecieved = null;
                }

                try {
                    ValueRecieved.addDouble((double) device.getVariable("humi"));
                } catch (ParticleDevice.VariableDoesNotExistException e) {
                    Toaster.l(ValueActivity.this, e.getMessage());
                }*/

                try {
                    ValueRecieved.addInteger(device.getIntVariable("led0"));
                } catch (ParticleDevice.VariableDoesNotExistException e) {
                    Toaster.l(ValueActivity.this, e.getMessage());
                }

                try {
                    ValueRecieved.addInteger(device.getIntVariable("led1"));
                } catch (ParticleDevice.VariableDoesNotExistException e) {
                    Toaster.l(ValueActivity.this, e.getMessage());
                }


                if(ValueRecieved.getIntvariable(0) == 1)
                    ledStatus1 = true;
                else if(ValueRecieved.getIntvariable(0) == 0)
                    ledStatus1 = false;

                if(ValueRecieved.getIntvariable(1) == 1)
                    ledStatus2 = true;
                else if(ValueRecieved.getIntvariable(1) == 0)
                    ledStatus2 = false;

                return ValueRecieved;
            }

            @Override
            public void onSuccess(Object i) { // this goes on the main thread

                if(!((VariableList) i).isEmpty()) {

                    if (((VariableList) i).getIntvariable(0) == 1) {
                        findViewById(R.id.button1).setBackgroundColor(0xFF00FF00);
                    } else if (((VariableList) i).getIntvariable(0) == 0) {
                        findViewById(R.id.button1).setBackgroundColor(0xFFFF0000);
                    }

                    if (((VariableList) i).getIntvariable(1) == 1) {
                        findViewById(R.id.button2).setBackgroundColor(0xFF00FF00);
                    } else if (((VariableList) i).getIntvariable(1) == 0) {
                        findViewById(R.id.button2).setBackgroundColor(0xFFFF0000);
                    }

                    //((TextView) findViewById(R.id.tempvalue)).setText("" + String.format("%.2f",((VariableList) i).getDoublevariable(0)) + "\u2109");

                    //((TextView) findViewById(R.id.humivalue)).setText("" + String.format("%.2f",((VariableList) i).getDoublevariable(1)) + "%");
                }
            }

            @Override
            public void onFailure(ParticleCloudException e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Async.executeAsync(ParticleCloud.get(ValueActivity.this), new Async.ApiWork<ParticleCloud, Object>() {
                    @Override
                    public Object callApi(ParticleCloud ParticleCloud) throws ParticleCloudException, IOException {

                        try {
                            ArrayList<String> temp = new ArrayList<String>();
                            temp.add("0");
                            device.callFunction("chgLed", temp);
                        } catch (ParticleDevice.FunctionDoesNotExistException e) {
                            Toaster.l(ValueActivity.this, e.getMessage());
                        }

                        if(ledStatus1) {
                            ledStatus1 = false;
                            return true;
                        }
                        else {
                            ledStatus1 = true;
                            return false;
                        }
                    }

                    @Override
                    public void onSuccess(Object i) { // this goes on the main thread
                        if((boolean)i)
                        {
                            findViewById(R.id.button1).setBackgroundColor(0xFFFF0000);
                        }
                        else
                        {
                            findViewById(R.id.button1).setBackgroundColor(0xFF00FF00);
                        }
                    }

                    @Override
                    public void onFailure(ParticleCloudException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Async.executeAsync(ParticleCloud.get(ValueActivity.this), new Async.ApiWork<ParticleCloud, Object>() {
                    @Override
                    public Object callApi(ParticleCloud ParticleCloud) throws ParticleCloudException, IOException {

                        try {
                            ArrayList<String> temp = new ArrayList<String>();
                            temp.add("1");
                            device.callFunction("chgLed", temp);
                        } catch (ParticleDevice.FunctionDoesNotExistException e) {
                            Toaster.l(ValueActivity.this, e.getMessage());
                        }

                        if(ledStatus2) {
                            ledStatus2 = false;
                            return true;
                        }
                        else {
                            ledStatus2 = true;
                            return false;
                        }
                    }

                    @Override
                    public void onSuccess(Object i) { // this goes on the main thread
                        if((boolean)i)
                        {
                            findViewById(R.id.button2).setBackgroundColor(0xFFFF0000);
                        }
                        else
                        {
                            findViewById(R.id.button2).setBackgroundColor(0xFF00FF00);
                        }
                    }

                    @Override
                    public void onFailure(ParticleCloudException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        /*
        findViewById(R.id.refresh_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Async.executeAsync(ParticleCloud.get(ValueActivity.this), new Async.ApiWork<ParticleCloud, Object>() {
                    @Override
                    public Object callApi(ParticleCloud ParticleCloud) throws ParticleCloudException, IOException {
                        ParticleDevice device = ParticleCloud.getDevice(getIntent().getStringExtra(ARG_DEVICEID));
                        VariableList ValueRecieved = new VariableList();

                        try {
                            ValueRecieved.addDouble((double) device.getVariable("temp"));
                        } catch (ParticleDevice.VariableDoesNotExistException e) {
                            Toaster.l(ValueActivity.this, e.getMessage());
                        }

                        try {
                            ValueRecieved.addDouble((double) device.getVariable("humi"));
                        } catch (ParticleDevice.VariableDoesNotExistException e) {
                            Toaster.l(ValueActivity.this, e.getMessage());
                        }

                        return ValueRecieved;
                    }

                    @Override
                    public void onSuccess(Object i) { // this goes on the main thread
                        if(!((VariableList) i).isEmpty()) {
                            ((TextView) findViewById(R.id.tempvalue)).setText("" + String.format("%.2f",((VariableList) i).getDoublevariable(0)) + "\u2109");

                            ((TextView) findViewById(R.id.humivalue)).setText("" + String.format("%.2f",((VariableList) i).getDoublevariable(1)) + "%");
                        }
                    }

                    @Override
                    public void onFailure(ParticleCloudException e) {
                        e.printStackTrace();
                    }
                });

            }
        });*/


    }

    public static Intent buildIntent(Context ctx, Integer value, String deviceid) {
        Intent intent = new Intent(ctx, ValueActivity.class);
        intent.putExtra(ARG_VALUE, value);
        intent.putExtra(ARG_DEVICEID, deviceid);

        return intent;
    }


}
