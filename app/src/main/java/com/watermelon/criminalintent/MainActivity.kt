package com.watermelon.criminalintent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.watermelon.criminalintent.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(),CrimeListFragment.Callbacks {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // se usa el fragment manager para manejar o controlar los fragmentos que se asignan a la main activity, el main activity se usa de host y el fragment manager controla que fragmen se mostrara en pantalla.
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null){

            val fragment = CrimeListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container,fragment)
                .commit()
        }
    }

    override fun onCrimeSelected(crimeId: UUID) {
        val fragment = CrimeFragment.newInstance(crimeId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .addToBackStack(null)
            .commit()

    }
}