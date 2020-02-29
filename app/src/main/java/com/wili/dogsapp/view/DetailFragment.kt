package com.wili.dogsapp.view


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.wili.dogsapp.R
import com.wili.dogsapp.databinding.FragmentDetailBinding
import com.wili.dogsapp.model.DogPalette
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
            dog?.let {
                it.imageUrl?.let {
                    setupBackgroundColor(it)
                }
            }
        })
    }

    private fun setupBackgroundColor(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate{palette->
                            val intColor = palette?.vibrantSwatch?.rgb ?:0
                            dataBinding.palette = DogPalette(intColor)

                        }
                }
            })
    }
}
