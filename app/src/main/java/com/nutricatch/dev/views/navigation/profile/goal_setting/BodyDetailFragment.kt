package com.nutricatch.dev.views.navigation.profile.goal_setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nutricatch.dev.R
import com.nutricatch.dev.data.ResultState
import com.nutricatch.dev.data.api.response.Gender
import com.nutricatch.dev.data.prefs.Preferences
import com.nutricatch.dev.data.prefs.dataStore
import com.nutricatch.dev.databinding.FragmentBodyDetailBinding
import com.nutricatch.dev.views.factory.UserProfileViewModelFactory
import com.nutricatch.dev.views.navigation.profile.ProfileViewModel

class BodyDetailFragment : Fragment() {
    private var _binding: FragmentBodyDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: Preferences
    private val viewModel by viewModels<ProfileViewModel> {
        UserProfileViewModelFactory.getInstance(preferences, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBodyDetailBinding.inflate(inflater, container, false)

        preferences = Preferences.getInstance(requireContext().dataStore)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rbMale.isChecked = true
        viewModel.token.observe(viewLifecycleOwner) {
            if (it != null) {
                observeHealthData()
            } else {
                binding.btnUpdate.text = "Save"
            }
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnUpdate.setOnClickListener {

            val weight = binding.edtMyWeight.text.toString().trim().toIntOrNull() ?: 0
            val height = binding.edtMyHeight.text.toString().trim().toIntOrNull() ?: 0
            val age = binding.edtAge.text.toString().trim().toIntOrNull() ?: 0
            val genderId = binding.rgGender.checkedRadioButtonId
            val gender = if (genderId == binding.rbMale.id) {
                Gender.MALE
            } else {
                Gender.FEMALE
            }

            viewModel.setUserWeight(weight, height, age, gender.name)
            viewModel.updateData().observe(viewLifecycleOwner) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        findNavController().navigate(BodyDetailFragmentDirections.actionBodyDetailFragmentToNutritionTargeFragment())
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                        /// TODO Handle error here
                        if (result.errorCode == 401) {
                            //TODO navigate ke login page
//                        findNavController().navigate(BodyDetailFragmentDirections.actionBodyDetailFragmentToLoginFragment2())
                        } else {
                            /// TODO tampilkan error dengan toast
                            Toast.makeText(context, "${result.error}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    else -> {}
                }
            }
        }

        binding.edtMyHeight.addTextChangedListener {
            binding.tvBmi.text = calculateBmi()
        }
        binding.edtMyWeight.addTextChangedListener {
            binding.tvBmi.text = calculateBmi()
        }
    }

    private fun observeHealthData() {
        viewModel.userHealthData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Success -> {
                    showLoading(false)
                    val healthResponse = result.data
                    with(binding) {
                        if (healthResponse.age != null) {
                            edtAge.setText(healthResponse.age.toString())
                        }
                        if (healthResponse.height != null) {
                            edtMyHeight.setText(healthResponse.height.toString())
                        }
                        if (healthResponse.weight != null) {
                            edtMyWeight.setText(healthResponse.weight.toString())
                        }

                        if (healthResponse.gender == Gender.MALE.name) {
                            rbMale.isChecked = true
                        } else if (healthResponse.gender == Gender.FEMALE.name) {
                            rbFemale.isChecked = true
                        }
                    }
                }

                is ResultState.Error -> {
                    showLoading(false)
                    /// TODO Handle error here
                    if (result.errorCode == 401) {
                        //TODO navigate ke login page
//                        findNavController().navigate(BodyDetailFragmentDirections.actionBodyDetailFragmentToLoginFragment2())
                    } else {
                        /// TODO tampilkan error dengan toast
                        Toast.makeText(context, "${result.error}", Toast.LENGTH_SHORT).show()
                    }
                }

                else -> {}
            }
        }
    }

    private fun calculateBmi(): String {

        val weight = binding.edtMyWeight.text.toString().toDoubleOrNull()
        val height = binding.edtMyHeight.text.toString().toDoubleOrNull()

        if (weight != null && height != null) {
            val bmi = (weight / (height * height / 10000))
            binding.tvBmiName.text = when {
                bmi < 17 -> {
                    getString(R.string.underweight)
                }

                bmi < 18.5 -> {
                    getString(R.string.slightly_underweight)
                }

                bmi > 30 -> {
                    getString(R.string.extremely_overweight)
                }

                bmi > 27 -> {
                    getString(R.string.overweight)
                }

                else -> {
                    getString(R.string.slightly_overweight)
                }
            }

            return bmi.toString()
        }

        return "0"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}