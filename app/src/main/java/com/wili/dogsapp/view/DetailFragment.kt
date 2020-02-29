package com.wili.dogsapp.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.wili.dogsapp.R
import com.wili.dogsapp.databinding.FragmentDetailBinding
import com.wili.dogsapp.util.getProgressDrawable
import com.wili.dogsapp.util.loadImage
import com.wili.dogsapp.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {
    private var dogUuid = 0
    private lateinit var viewModel: DetailViewModel
    private lateinit var dataBinding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail,container,false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid
            viewModel.fetch(dogUuid)
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.dog.observe(this, Observer {dog->
            dataBinding.dog = dog
        })
    }
}
