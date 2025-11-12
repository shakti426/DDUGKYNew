package com.deendayalproject.fragments

import CenterAdapter
import SharedViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.adapter.RfAdapterList
import com.deendayalproject.databinding.FragmentCenterBinding
import com.deendayalproject.model.request.AddNewRFReq
import com.deendayalproject.model.request.ModifyRfList
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.response.TrainingCenterItem
import com.deendayalproject.util.AppUtil

class RfCenterFragment : Fragment() {

    private var _binding: FragmentCenterBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: RfAdapterList
    var centerId =""
    var sanctionOrder =""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCenterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        adapter = RfAdapterList(emptyList()) { selectedItem ->

        centerId= selectedItem.trainingCenterId.toString()
        sanctionOrder= selectedItem.senctionOrder

          val facilityStatus = selectedItem.facilityStatus


            if (facilityStatus =="N"){

                val request = AddNewRFReq(
                    appVersion = BuildConfig.VERSION_NAME,
                    trainingCentre = selectedItem.trainingCenterId.toString(),
                    sanctionOrder = selectedItem.senctionOrder
                )
                viewModel.saveInitialResidentialFacility(request)
                observeViewAddNewRF()




            }

            else
            {


                val action =
                    RfCenterFragmentDirections.actionRfCenterFragmentToRfMultipleListFragment(centerId,sanctionOrder
                    )
                findNavController().navigate(action)

            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        observeViewModel()


        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        val request = TrainingCenterRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext())
        )
        viewModel.fetchRfList(request, AppUtil.getSavedTokenPreference(requireContext()))
    }

    private fun observeViewModel() {
        viewModel.rfTrainingCenters.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> adapter.updateData(it.wrappedList ?: emptyList())
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun observeViewAddNewRF() {
        viewModel.saveInitialResidentialFacility.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 ->  {

                        val facilityId = it.facilityId.toString()

                        Toast.makeText(requireContext(), "Rf Added.", Toast.LENGTH_SHORT).show()


                        val action =
                            RfCenterFragmentDirections.actionRfcenterFragmentToFragmentResidentialFacility(centerId,sanctionOrder,
                                facilityId
                            )
                        findNavController().navigate(action)


                    }
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

}
