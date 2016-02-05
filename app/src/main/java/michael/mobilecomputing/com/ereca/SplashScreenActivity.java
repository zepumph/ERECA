package michael.mobilecomputing.com.ereca;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by bertolopez-cruz on 1/26/16.
 */
public class SplashScreenActivity extends Activity{
    Animation animationFadeOut;
    Animation animationFadeIn;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        image = (ImageView)findViewById(R.id.splashthis);
        image.startAnimation(animationFadeIn);
        image.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                image.startAnimation(animationFadeOut);

                /* start the next activity */
               // Intent transition = new Intent(SplashScreenActivity.this, MainActivity.class);
                Intent transition = new Intent(SplashScreenActivity.this, MainActivity.class);
                SplashScreenActivity.this.startActivity(transition);
                SplashScreenActivity.this.finish();
            }
        }, 100);


    }

}
