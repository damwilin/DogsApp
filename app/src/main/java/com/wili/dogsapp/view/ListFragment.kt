package com.wili.dogsapp.view


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.wili.dogsapp.R
import com.wili.dogsapp.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {
    private lateinit var viewModel:ListViewModel
    private val dogsListAdapter= DogListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()
        dogsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogsListAdapter
        }
        observeViewModel()
        refreshLayout.setOnRefreshListener {
            dogsList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            viewModel.refreshBypassCache()
            refreshLayout.isRefreshing = false
        }
    }
    private fun observeViewModel(){
        viewModel.dogs.observe(this, Observer { dogs->
            dogsList.visibility = View.VISIBLE
            dogsListAdapter.updateDogList(dogs)
        })

        viewModel.dogsLoadError.observe(this, Observer {isError->
            isError?.let {
                listError.visibility =if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer {isLoading->
            isLoading?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settingsFragment -> {
                view?.let {
                    Navigation.findNavController(it).navigate(ListFragmentDirections.actionSettingsFragment()) }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
