package com.example.fireinstgram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
ImageView iconImage;
LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    iconImage = findViewById(R.id.iconImage);
linearLayout = findViewById(R.id.layout);


linearLayout.animate().alpha(0f).setDuration(1);
        TranslateAnimation animation = new TranslateAnimation(0,0,0,-1000);
        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new myAnimationLisener());
        iconImage.setAnimation(animation);

    }

    public void registration(View view) {
        startActivity(new Intent(this,Registration.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }

    public void logIn(View view) {
        startActivity(new Intent(this,LogIn.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }

    private class  myAnimationLisener implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            iconImage.clearAnimation();
            iconImage.setVisibility(View.INVISIBLE);
            linearLayout.animate().alpha(1f).setDuration(1000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    if (FirebaseAuth.getInstance().getCurrentUser() != null){
        startActivity(new Intent(this,MainPage.class));
    finish();
    }
    }
}
