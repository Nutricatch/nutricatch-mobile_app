package com.nutricatch.dev.views.navigation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.nutricatch.dev.R
import com.nutricatch.dev.data.ResultState
import com.nutricatch.dev.databinding.FragmentHomeBinding
import com.nutricatch.dev.views.factory.HomeViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvLatestPost.layoutManager = layoutManager

        /// nanti, ubah jadi observe ke viewmodel
        val user = "John Doe"
        binding.tvUserName.text = getString(R.string.home_greeting, user)

        binding.headerUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_navigation_setting))

        viewModel.foods.observe(viewLifecycleOwner){result->
            when(result){
                is ResultState.Success -> {
                    // showLoading(false)
                    val foods = result.data.foods
                    val adapter = FoodsAdapter()
                    adapter.submitList(foods)
                    binding.rvFoodRecomm.adapter = adapter
                }

                is ResultState.Loading -> {
                    // showLoading(true)
                }

                is ResultState.Error -> {
                    // showLoading(false)
                    // handle error
                }
            }
        }

        viewModel.recipes.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Success -> {
                    //showLoading(false)
                    val recipes = result.data.recipes
                    val adapter = RecipeAdapter()
                    adapter.submitList(recipes)
                    binding.rvLatestPost.adapter = adapter
                }

                is ResultState.Loading -> {
//                    showLoading(true)
                }

                is ResultState.Error -> {
//                    showLoading(false)

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}