package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setupMenu()

        binding.asteroidRecycler.adapter = AsteroidsAdapter(AsteroidsAdapter.OnClickListener {
            viewModel.displayAsteroidDetails(it)
        })

        binding.asteroidRecycler.adapter as AsteroidsAdapter

        viewModel.pictureDay.observe(viewLifecycleOwner) {
            if (null != it && it.url.isNotEmpty() && it.mediaType == NASAPictureOfDayMediaTypeImage) {
                Picasso.get().load(it.url).into(binding.activityMainImageOfTheDay)
            }
        }

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner) {
            if (null != it) {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailsComplete()
            }
        }

        val toolbar: Toolbar? = requireActivity().findViewById(R.id.toolbar)
        toolbar?.navigationIcon = null

        return binding.root
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_overflow_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.show_next_week_asteroids -> {
                        true
                    }
                    R.id.show_next_today_asteroids -> {
                        true
                    }
                    R.id.show_saved_asteroids -> {
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    companion object {
        const val NASAPictureOfDayMediaTypeImage = "image"
    }
}
