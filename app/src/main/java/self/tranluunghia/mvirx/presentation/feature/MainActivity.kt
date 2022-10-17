package self.tranluunghia.mvirx.presentation.feature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import self.tranluunghia.mvirx.R
import self.tranluunghia.mvirx.presentation.feature.randomnumber.RandomNumberFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView, RandomNumberFragment(), null).commit()
    }
}