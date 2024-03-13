package com.ninezero.mealtoday.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ninezero.mealtoday.R
import com.ninezero.mealtoday.databinding.FragmentAboutBinding
import com.ninezero.mealtoday.utils.openUrl
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class AboutFragment : Fragment(R.layout.fragment_about), View.OnClickListener {

    private lateinit var navController: NavController
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        enterTransition = com.google.android.material.transition.platform.MaterialSharedAxis(
            com.google.android.material.transition.platform.MaterialSharedAxis.Z,
            true
        ).addTarget(view)
        returnTransition = com.google.android.material.transition.platform.MaterialSharedAxis(
            com.google.android.material.transition.platform.MaterialSharedAxis.Z,
            false
        ).addTarget(view)
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAboutBinding.bind(view)
        navController = Navigation.findNavController(view)

        binding.back.setOnClickListener { navController.popBackStack() }

        binding.cvDev.setOnClickListener(this)
        binding.cvOther.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.cvDev -> openUrl("https://github.com/youuungh")
            R.id.cvOther -> {
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.oss_title))
                startActivity(Intent(requireActivity(), OssLicensesMenuActivity::class.java))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}