package com.watermelon.criminalintent


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.watermelon.criminalintent.databinding.ListItemCrimeBinding
import com.watermelon.criminalintent.databinding.FragmentCrimeListBinding
import java.util.*

const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {
    private var adapter:CrimeAdapter? = CrimeAdapter(emptyList())

    interface Callbacks{
        fun onCrimeSelected(crimeId: UUID)
    }
    private var callbacks:Callbacks? = null


    private lateinit var binding: FragmentCrimeListBinding

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    companion object{
        fun newInstance():CrimeListFragment{
            return CrimeListFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrimeListBinding.inflate(inflater,container,false)
        binding.crimeRecyclerView.adapter = adapter
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            { crimes ->
                crimes?.let {
                    Log.i(TAG,"Got crimes ${crimes.size}")
                    updateUI(crimes)
                }
            })
    }
    private fun updateUI(crimes:List<Crimen>){
        adapter = CrimeAdapter(crimes)
        binding.crimeRecyclerView.adapter = adapter
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                val crime = Crimen()
                crimeListViewModel.addCrime(crime)
                callbacks?.onCrimeSelected(crime.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    private inner class CrimeAdapter(var crimenes: List<Crimen>): RecyclerView.Adapter<CrimeAdapter.CrimeHolder>(){


        private inner class CrimeHolder(val binding: ListItemCrimeBinding): RecyclerView.ViewHolder(binding.root),View.OnClickListener{

            private lateinit var crime: Crimen

            init {
                itemView.setOnClickListener(this)
            }

            fun bind(crime: Crimen){
                this.crime = crime
                binding.crimeTitle.text = this.crime.title
                binding.crimeDate.text = this.crime.date.toString()
                binding.imgCrimeSolved.visibility = if (crime.isSolved) {
                        View.VISIBLE
                    }else{
                        View.GONE
                    }
                }

            override fun onClick(v: View?) {
                callbacks?.onCrimeSelected(crime.id)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {

            val binding = ListItemCrimeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return CrimeHolder(binding)
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimenes[position]
            holder.bind(crime)
        }

        override fun getItemCount(): Int {
            return crimenes.size
        }

    }


}