package ipca.example.photomap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(baseContext, "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show()
                return@OnCompleteListener
            }
            val token = task.result

            val msg = "My token is:${token} "
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            Preferences(this@SplashScreenActivity).fireBaseToken = token!!
        })

        Handler(Looper.getMainLooper()).postDelayed({
            val firebaseAuth = Firebase.auth
            if (firebaseAuth.currentUser!=null){
                val intent = Intent(
                    this@SplashScreenActivity,
                    MainActivity::class.java
                )
                startActivity(intent)
                finish()
            }else {
                val intent = Intent(
                    this@SplashScreenActivity,
                    LoginActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        },1000L)

    }
}