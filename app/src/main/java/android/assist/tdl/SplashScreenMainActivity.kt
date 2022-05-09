package android.assist.tdl

import android.annotation.SuppressLint
import android.assist.tdl.ui.maintasks.MainTasks
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenMainActivity : AppCompatActivity() {

    private lateinit var splashImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        splashImage = findViewById(R.id.splash_screen_image)

        splashImage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.splash_in))
        Handler().postDelayed({
            splashImage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.splash_out))
            Handler().postDelayed({
                splashImage.visibility = View.GONE
                startActivity(Intent(this,MainTasks::class.java))
                finish()
            },500)
        },1500)
    }
}