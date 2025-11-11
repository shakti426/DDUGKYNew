package com.deendayalproject.fragments

import SharedViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.adapter.RfModifyListAdapter
import com.deendayalproject.databinding.FragmentRfMultipleListBinding
import com.deendayalproject.model.request.AddNewRFReq
import com.deendayalproject.model.request.ModifyRfList
import com.deendayalproject.model.response.TrainingCenterItem
import com.deendayalproject.util.AppUtil

class RfMultipleListFragment : Fragment() {

    private var _binding: FragmentRfMultipleListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: RfModifyListAdapter
    var centerId =""
    var sanctionOrder =""



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRfMultipleListBinding.inflate(inflater, container, false)

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        centerId =   AppUtil.getcenterIdRFPreference(requireContext())
        sanctionOrder = AppUtil.getsanctionOrderRFPreference(requireContext())



        adapter = RfModifyListAdapter(emptyList()) { selectedItem ->



            AppUtil.savesanctionOrderRFPreference(requireContext(),
                selectedItem.facilityId.toString()
            )



            findNavController().navigate(
                R.id.action_rfMultipleListFragment_to_fragment_residential_facility
            )


        }

        binding.btnAddResidentialFacility.setOnClickListener {


            val request = AddNewRFReq(
                appVersion = BuildConfig.VERSION_NAME,
                trainingCentre = centerId,
                sanctionOrder = sanctionOrder
            )
            viewModel.saveInitialResidentialFacility(request)

            observeViewAddNewRF()


        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        observeViewModel()


        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }


        val request = ModifyRfList(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder = sanctionOrder
        )
        viewModel.getResidentialList(request)


    }

    private fun observeViewModel() {
        viewModel.getResidentialList.observe(viewLifecycleOwner) { result ->
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

                        AppUtil.saveFacilityIdRFPreference(requireContext(), it.facilityId.toString())

                        Toast.makeText(requireContext(), "Rf Added.", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(
                            R.id.action_rfMultipleListFragment_to_fragment_residential_facility
                        )
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