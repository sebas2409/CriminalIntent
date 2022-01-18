package com.watermelon.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.watermelon.criminalintent.databinding.CrimeFragmentBinding
import java.util.*

private const val ARG_CRIME_ID = "crime_id"

class CrimeFragment:Fragment() {

    private lateinit var crime:Crimen
    private lateinit var binding: CrimeFragmentBinding
    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crimen()
        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailViewModel.loadCrime(crimeId)
    }

    // con esta función es donde inflamos la vista para el fragmento y devuelve la vista inflada en la actividad de alojamiento.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // le estamos diciendo CrimeFragmentBinding que se infle con unas determinadas instrucciones.
        //el segundo parametro "container" es la vista padre que permite que se puedan usar los widgets que estan en el.
        //el tercer parametro "falso" le dice al inflador que no agrege inmediatamente la vista inflada al padre de la vista, por que la vista estara alojada en la vista container.
        binding = CrimeFragmentBinding.inflate(inflater,container,false)

        binding.btnCrimeDate.apply {
            text = crime.date.toString()
            isEnabled = false
        }


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner, { crime->
                crime?.let {
                    this.crime = crime
                    updateUI()
                }
            })
    }

    private fun updateUI() {
        binding.etName.setText(crime.title)
        binding.btnCrimeDate.text = crime.date.toString()
        binding.crimeSovled.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }


    override fun onStart() {
        super.onStart()

        val titleWatcher = object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { // s es el USERINPUT, que despues se convertira en el titulo del crimen.
                crime.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }

        binding.etName.addTextChangedListener(titleWatcher)

        binding.crimeSovled.apply {
            setOnCheckedChangeListener{_,isChecked->
                crime.isSolved = isChecked
            }
        }
    }

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }
    companion object{
        fun newInstance(crimeId: UUID):CrimeFragment{
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID,crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }
}