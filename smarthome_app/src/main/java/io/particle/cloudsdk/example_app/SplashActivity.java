package io.particle.cloudsdk.example_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.Toaster;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Toaster.l(SplashActivity.this, "Loading....");
        // DO!:
        Async.executeAsync(ParticleCloud.get(SplashActivity.this), new Async.ApiWork<ParticleCloud, Object>() {

            private ParticleDevice mDevice;

            @Override
            public Object callApi(ParticleCloud sparkCloud) throws ParticleCloudException, IOException {

                //Don't push you id or password to github
                sparkCloud.logIn("**UserID**", "**Password**");
                sparkCloud.getDevices();
                // mDevice = sparkCloud.getDevice("1f0034000747343232361234");
                mDevice = sparkCloud.getDevice("**deviceID**");
                //Object obj;
                return -1;

            }

            @Override
            public void onSuccess(Object value) {
                Intent intent = ValueActivity.buildIntent(SplashActivity.this, 123, mDevice.getID());
                startActivity(intent);
            }

            @Override
            public void onFailure(ParticleCloudException e) {
                Toaster.l(SplashActivity.this, e.getBestMessage());
                e.printStackTrace();
                Log.d("info", e.getBestMessage());
            }
        });


    }

    /*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    */
}
