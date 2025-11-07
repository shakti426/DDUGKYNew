package com.deendayalproject.fragments

import SharedViewModel
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
//import com.bumptech.glide.Glide
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.adapter.IndoorGameRFAdapter
import com.deendayalproject.adapter.LivingAreaInformationAdapter
import com.deendayalproject.adapter.RFToiletAdapter
import com.deendayalproject.databinding.RfQteamFormFagmentBinding
import com.deendayalproject.databinding.RoominformationPopdialogBinding
//import com.deendayalproject.databinding.RoominformationDialogboxLayoutBinding
import com.deendayalproject.databinding.TriPopdialogBinding
import com.deendayalproject.model.request.CompliancesRFQTReq
import com.deendayalproject.model.request.LivingRoomListViewRQ
import com.deendayalproject.model.request.RFGameRequest
import com.deendayalproject.model.request.RfLivingAreaInformationRQ
import com.deendayalproject.model.request.ToiletRoomInformationReq
import com.deendayalproject.model.request.TrainingCenter
import com.deendayalproject.model.request.TrainingCenterInfo
import com.deendayalproject.model.response.IndoorRFGameResponseDetails
import com.deendayalproject.util.AppUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class RFQTeamFormFragment : Fragment() {

    private var _binding: RfQteamFormFagmentBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: SharedViewModel

    private lateinit var adapter: LivingAreaInformationAdapter
    private lateinit var adapterToilet: RFToiletAdapter
    private lateinit var adapterIndoorGame: IndoorGameRFAdapter
    private val RFindoorGamesList = mutableListOf<IndoorRFGameResponseDetails>()
    private val approvalList = listOf("Approved", "Send for modification")
    private var selectedRFQTApproval = ""
    private var selectedRFLAIApproval = ""
    private var selectedRFToiletApproval = ""
    private var selectedIDCApproval = ""
    private var selectedIndoorGameApproval = ""
    private var selectedResidintislFacilityApproval = ""
    private var selectedNonAreaInfoApproval = ""
    private var RFQTresFacilityId = ""
    private var RFQInfraDetailsbuildingPlanFile = ""
    private var RFQInfraDetailprotectionStairsProofFile = ""
    private var RFQInfraDetailhostelNameBoardProofFile = ""
    private var RFQInfraDetailfoodSpecificationBoardFile = ""
    private var RFQInfraDetailbasicInformationBoardproofFile = ""
    private var RFQInfraDetailbasicsecuringWiresDoneProofFile = ""
    private var RFQInfraDetailcorridorProofFile = ""
    private var RFQInfraDetailcirculatingAreaProofFile = ""
    private var RFQInfraDetailbuildingPhotosFile = ""
    private var RFQInfraDetailleakagesProofFile = ""
    private var RFQInfraDetailconformanceDduProofFile= ""
    private var RFQInfraDetailswitchBoardsPanelBoardsProofFile= ""
    private var RFQInfraDetailcontactDetailImportantPeopleproofFile= ""
    private var RFQInfraDetailstudentEntitlementBoardProofFile= ""

//       Ajit Ranjan 03/11/2025 Non Room Information
    private var PreparedFoodFile= ""
    private var ReceptionAreaPdf= ""


    //       Ajit Ranjan 06/11/2025 Residential Facilities
    private var RFWardenCareFile= ""
    private var RFMaleDoctorFile= ""
    private var RFFemaleDoctorFile= ""
    private var RFWardenCaretakerMaleFile= ""
    private var RFHostelsSeparatedFile= ""
    private var RFSecurityGuardsFile= ""


    private var selectedRFQTRemarks = ""
    private var selectedRFLAIRemarks = ""
    private var selectedRFToiletRemarks = ""
    private var selectedIDCRemarks = ""
    private var selectedResidintislFacilityApprovalRemark = ""
    private var selectedIndoorGameApprovalRemark = ""
    private lateinit var tcElectricalAdapter: ArrayAdapter<String>
    private lateinit var RFIndoorGameAdapter: ArrayAdapter<String>
    private lateinit var tvLivingAreaInformationAdapter: ArrayAdapter<String>
    private lateinit var tvToiletAdapter: ArrayAdapter<String>
    private lateinit var tvNonLivingAreaAdapter: ArrayAdapter<String>

    private var centerId = ""
    private var sanctionOrder = ""
    private var centerName = ""
    private var RFQTBasicInfoPdf = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RfQteamFormFagmentBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//       GetRfBasicInformation AjitRanjan 17/10/2025
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        centerId = arguments?.getString("centerId").toString()
        centerName = arguments?.getString("centerName").toString()
        sanctionOrder = arguments?.getString("sanctionOrder").toString()
        val token = AppUtil.getSavedTokenPreference(requireContext())
//
//
        val TokeValue=token
//
//


//        ImageView Click View file all use in Infrastcture Details and Complains
        binding.infrastructureDetailsAndCompliancesLayout.BuildingPlanFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQInfraDetailsbuildingPlanFile, "RFQInfraDetailsbuildingPlanFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
        }


        binding.infrastructureDetailsAndCompliancesLayout.ProtectionOfStairsFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQInfraDetailprotectionStairsProofFile, "RFQInfraDetailprotectionStairsProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
        }

        binding.infrastructureDetailsAndCompliancesLayout.HostelNameBoardFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQInfraDetailhostelNameBoardProofFile, "RFQInfraDetailhostelNameBoardProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
        }
        binding.infrastructureDetailsAndCompliancesLayout.SpecificationBoardFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQInfraDetailfoodSpecificationBoardFile, "RFQInfraDetailfoodSpecificationBoardFileo")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
        }
        binding.infrastructureDetailsAndCompliancesLayout.BasicInformationBoardFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQInfraDetailbasicInformationBoardproofFile, "RFQInfraDetailbasicInformationBoardproofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
        }
        binding.infrastructureDetailsAndCompliancesLayout.SecuringWiresDoneFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQInfraDetailbasicsecuringWiresDoneProofFile, "RFQInfraDetailbasicsecuringWiresDoneProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
        }
        binding.infrastructureDetailsAndCompliancesLayout.CorridorFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQInfraDetailcorridorProofFile, "RFQInfraDetailcorridorProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
        }
        binding.infrastructureDetailsAndCompliancesLayout.circulatingAreaProofFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQInfraDetailcirculatingAreaProofFile, "RFQInfraDetailcirculatingAreaProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
        }
        binding.infrastructureDetailsAndCompliancesLayout.OnwershipOfBuldingFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQInfraDetailbuildingPhotosFile, "RFQInfraDetailbuildingPhotosFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
        }
        binding.infrastructureDetailsAndCompliancesLayout.VisibleSignsLeakagesFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQInfraDetailleakagesProofFile, "RFQInfraDetailleakagesProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
        }
        binding.infrastructureDetailsAndCompliancesLayout.ConformanceToDduGkyFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQInfraDetailconformanceDduProofFile, "RFQInfraDetailconformanceDduProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)


        }
        binding.infrastructureDetailsAndCompliancesLayout.SwitchBoardsAndPanelBoards.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQInfraDetailswitchBoardsPanelBoardsProofFile, "RFQInfraDetailswitchBoardsPanelBoardsProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)



        }
//                  Non Room Information ImageView Click 03/11/2025



        binding.RFNonLivingAreaLayout.ReceptionAreaFile.setOnClickListener {
            openBase64Pdf(requireContext(), ReceptionAreaPdf)

        }

        binding.RFNonLivingAreaLayout.PreparedFoodFile.setOnClickListener {
            openBase64Pdf(requireContext(), PreparedFoodFile)

        }




//       Ajit Ranjan 06/11/2025 Residential Facilities
        binding.RFResidentialFacilitiesAvailable.WardenCareFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFWardenCareFile,"Warden Care")

        }
        binding.RFResidentialFacilitiesAvailable.MaleDoctorFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFMaleDoctorFile,"Male Doctor File")

        }
        binding.RFResidentialFacilitiesAvailable.FemaleDoctorFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFFemaleDoctorFile,"Female Doctor")

        }
        binding.RFResidentialFacilitiesAvailable.WardenCaretakerMaleFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFWardenCaretakerMaleFile,"Warden Caretaker Male")

        }
        binding.RFResidentialFacilitiesAvailable.HostelsSeparatedFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFHostelsSeparatedFile,"Hostels Separated")

        }
        binding.RFResidentialFacilitiesAvailable.SecurityGuardsFile.setOnClickListener {
//            openBase64Pdf(requireContext(), RFSecurityGuardsFile)
            showBase64ImageDialog(requireContext(), RFSecurityGuardsFile,"Security Guards")

        }






        // TrainingCenterInfo API
        val requestTcInfo = TrainingCenterInfo(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext())
        )
        viewModel.getRfBasicInformationrInfo(requestTcInfo)
        collectTCInfoResponse()
        init()
        binding.backButton.setOnClickListener {

            findNavController().navigateUp()
        }

    }

    private fun init() {

        listener()
    }

    private fun listener() {


    }


    @SuppressLint("SetTextI18n")
    private fun collectTCInfoResponse() {
        viewModel.ResidentialFacilityQTeam.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {

                            binding.residentialfacilityqteamInfoLayout.tvSchemeName.text =
                                x.schemeName
                            binding.residentialfacilityqteamInfoLayout.tvtraningCentreName.text =
                                x.trainingCenterName
//                            binding.residentialfacilityqteamInfoLayout.tvProjectState.text = x.stateName
//                            binding.residentialfacilityqteamInfoLayout.tvblock.text = x.blockName
                            binding.residentialfacilityqteamInfoLayout.tvssanctionNumbere.text =
                                x.senctionOrder
                            binding.residentialfacilityqteamInfoLayout.tvresidentialFacilitytype.text =
                                x.residentialType
                            binding.residentialfacilityqteamInfoLayout.tvllocationOfRc.text =
                                x.residentialCenterLocation
                            binding.residentialfacilityqteamInfoLayout.tvhouseNo.text = x.houseNo
                            binding.residentialfacilityqteamInfoLayout.tvStreet.text = x.streetNo1
                            binding.residentialfacilityqteamInfoLayout.tvstateUi.text = x.stateName
                            binding.residentialfacilityqteamInfoLayout.tvblock.text = x.blockName
                            binding.residentialfacilityqteamInfoLayout.tvVillageWardNo.text =
                                x.villageName
                            binding.residentialfacilityqteamInfoLayout.tvpincode.text = x.pincode
//                            binding.residentialfacilityqteamInfoLayout.tvpincode.text = x.pincode
                            binding.residentialfacilityqteamInfoLayout.tvresidentialFacilityPhone.text =
                                x.residentialFacilitiesPhNo
                            binding.residentialfacilityqteamInfoLayout.tvEmail.text = x.email
                            binding.residentialfacilityqteamInfoLayout.tvTrainingCenterAddress.text =
                                x.geoAddress
                            binding.residentialfacilityqteamInfoLayout.tvLongitude.text =
                                x.longitude
                            binding.residentialfacilityqteamInfoLayout.tvGeoAddress.text =
                                x.geoAddress
                            binding.residentialfacilityqteamInfoLayout.tvCategoryofTClocation.text =
                                x.categoryOfTc
                            binding.residentialfacilityqteamInfoLayout.tvApproximateDistancefroma.text =
                                x.longitude
                            binding.residentialfacilityqteamInfoLayout.tvParliamentaryConstituency.text =
                                x.residentialFacilitiesPhNo
                            binding.residentialfacilityqteamInfoLayout.CentretoResidential.text =
                                x.residentialCenterLocation
                            binding.residentialfacilityqteamInfoLayout.tvEmployeeID.text = x.wardEmpId
                            binding.residentialfacilityqteamInfoLayout.tvresidentialFacilitytype.text =
                                x.resFacilityId

                            binding.residentialfacilityqteamInfoLayout.tvMobileNo.text = x.mobile

                            RFQTresFacilityId=x.resFacilityId.toString()




//                            binding.residentialfacilityqteamInfoLayout.tvPoliceVerificationStatus.text = x.policeVerfictnImage
//                            binding.residentialfacilityqteamInfoLayout.tvAppointmentLetter.text = x.empLetterImage
                            // ✅ Load image using Glide and ViewBinding
                            // Load image

                            RFQTBasicInfoPdf= x.policeVerfictnImage.toString()


                            binding.residentialfacilityqteamInfoLayout.valueRFQTInfoPhoto.setOnClickListener {


//                                showBase64ImageDialog(requireContext(), x.policeVerfictnImage, "RFQTeam Basic Info Appointment Letter Photo")
                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
                            }
                            binding.residentialfacilityqteamInfoLayout.tvStreet2.text =
                                x.streetNo2

                        }
                    }

                    202 -> Toast.makeText(
                        requireContext(),
                        "No data available.",
                        Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(),
                        "Please upgrade your app.",
                        Toast.LENGTH_SHORT
                    ).show()

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


        //Adapter Electrical
        tcElectricalAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.residentialfacilityqteamInfoLayout.SpinnerTcInfo.setAdapter(tcElectricalAdapter)

        binding.residentialfacilityqteamInfoLayout.SpinnerTcInfo.setOnItemClickListener { parent, view, position, id ->
            selectedRFQTApproval = parent.getItemAtPosition(position).toString()
            if (selectedRFQTApproval == "Send for modification") {
                binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks.visibility =
                    View.VISIBLE
                binding.residentialfacilityqteamInfoLayout.textViewRFQTInfoRemarks.visibility =
                    View.VISIBLE


            } else {

                binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks.visibility = View.GONE
                binding.residentialfacilityqteamInfoLayout.textViewRFQTInfoRemarks.visibility =
                    View.GONE

            }

        }

        binding.residentialfacilityqteamInfoLayout.btnRFQTInfoNext.setOnClickListener {


            if (selectedRFQTApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }
            binding.residentialfacilityqteamInfoLayout.viewRFQTInfo.visibility = View.GONE
            binding.residentialfacilityqteamInfoLayout.RFQTInfoExpand.visibility = View.GONE
            binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand.visibility = View.VISIBLE
            binding.tvinfrastructureDetailsAndCompliances.visibility = View.VISIBLE
            binding.infrastructureDetailsAndCompliancesLayout.viewIDC.visibility = View.VISIBLE

            binding.residentialfacilityqteamInfoLayout.tvTrainInfo.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }
            if (selectedRFQTApproval == "Send for modification") {
                selectedRFQTRemarks = binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks.text.toString()
                if (selectedRFQTRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
//                return@setOnClickListener
            } else selectedRFQTRemarks = ""

//            Ajit Ranjan create 21/October/2026  CompliancesRFQTReqRFQT
            val requestCompliancesRFQT = CompliancesRFQTReq(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                facilityId = RFQTresFacilityId,
                imeiNo = AppUtil.getAndroidId(requireContext())
            )

            viewModel.getCompliancesRFQTReqRFQT(requestCompliancesRFQT)
            collectInsfrastructureDetailsAndComplains()
        }





        //Adapter Living Area Information 27/10/2025
        tvLivingAreaInformationAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.livingareainformationLayout.SpinnerLivingAreaInformation.setAdapter(tvLivingAreaInformationAdapter)
        binding.livingareainformationLayout.SpinnerLivingAreaInformation.setOnItemClickListener { parent, view, position, id ->
            selectedRFLAIApproval = parent.getItemAtPosition(position).toString()
            if (selectedRFLAIApproval == "Send for modification") {
                binding.livingareainformationLayout.LivingAreaInformationRemarks.visibility =
                    View.VISIBLE
                binding.livingareainformationLayout.etLivingAreaInformationRemarks.visibility =
                    View.VISIBLE


            } else {

                binding.livingareainformationLayout.etLivingAreaInformationRemarks.visibility = View.GONE
                binding.livingareainformationLayout.LivingAreaInformationRemarks.visibility =
                    View.GONE

            }

        }
        binding.livingareainformationLayout.btnLivingAreaInformationNext.setOnClickListener {


            if (selectedRFLAIApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }
//            return@setOnClickListener
            binding.livingareainformationLayout.viewLAI.visibility = View.GONE
            binding.livingareainformationLayout.LivingAreaInformationExpand.visibility = View.GONE



            binding.RFTioletLayout.toiletsExpand.visibility = View.VISIBLE
            binding.tvRFTiolet.visibility = View.VISIBLE

//            binding.mainDescAcademia.visibility = View.GONE
//            binding.viewDescAcademia.visibility = View.GONE
//            viewIDC
//    IDetailsComplainExpand
//    llTopIDC
//    tvIDC


            binding.livingareainformationLayout.tvLAI.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }
            if (selectedRFLAIApproval == "Send for modification") {
                selectedRFLAIRemarks = binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks.text.toString()
                if (selectedRFLAIRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
//                return@setOnClickListener
            } else selectedRFLAIRemarks = ""

//            Ajit Ranjan create 27/October/2025  toiletRoomListView


            adapterToilet = RFToiletAdapter(emptyList()) { selectedItem ->
                val data = selectedItem.rfToiletId
                lifecycleScope.launch(Dispatchers.IO) {
                val requestToiletRoomInformationReq = ToiletRoomInformationReq(
                    appVersion = BuildConfig.VERSION_NAME,
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    rfToiletId = data.toString(),
                )
                viewModel.getRfToiletRoomInformation(requestToiletRoomInformationReq)
            }}
            ToiletRecyclerView()






        }

        binding.livingareainformationLayout.btnLivingAreaInformationPrevious.setOnClickListener {
            binding.tvlivingareainformation.visibility= View.GONE
            binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand.visibility= View.VISIBLE
            binding.livingareainformationLayout.LivingAreaInformationExpand.visibility= View.GONE

//            tvinfrastructure_details_and_compliances
        }
//


//        SpinnerToilet
//        LivingToiletRemarks
//        etToiletRemarks
//        btnToiletPrevious, btnToiletNext
        //Adapter   Ajit Ranjan Toilets 27/10/2025

        tvToiletAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.RFTioletLayout.SpinnerToilet.setAdapter(tvToiletAdapter)
        binding.RFTioletLayout.SpinnerToilet.setOnItemClickListener { parent, view, position, id ->
            selectedRFToiletApproval = parent.getItemAtPosition(position).toString()
            if (selectedRFToiletApproval == "Send for modification") {
                binding.RFTioletLayout.LivingToiletRemarks.visibility =
                    View.VISIBLE
                binding.RFTioletLayout.etToiletRemarks.visibility =
                    View.VISIBLE


            } else {

                binding.RFTioletLayout.etToiletRemarks.visibility = View.GONE
                binding.RFTioletLayout.LivingToiletRemarks.visibility =
                    View.GONE

            }

        }

        binding.RFTioletLayout.btnToiletNext.setOnClickListener {


            if (selectedRFToiletApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }
//            return@setOnClickListener
//            binding.RFTioletLayout.tvToilet.visibility = View.GONE
            binding.RFTioletLayout.toiletsExpand.visibility = View.GONE
            binding.RFTioletLayout.viewToilet.visibility = View.GONE
            binding.tvRFConstraintLayoutNonLivingArea.visibility = View.VISIBLE





//            tvRFNonLivingArea
//            RFNonLivingAreaLayout



            binding.RFTioletLayout.tvToilet.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }
            if (selectedRFToiletApproval == "Send for modification") {
                selectedRFToiletRemarks = binding.RFTioletLayout.etToiletRemarks.text.toString()
                if (selectedRFToiletRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
//                return@setOnClickListener
            } else selectedRFToiletRemarks = ""

//            Ajit Ranjan create 03/Novmber/2025  NonRoomInformstion


            val requestLRLVRQ = LivingRoomListViewRQ(
                appVersion = BuildConfig.VERSION_NAME,
                tcId = centerId.toInt(),
                sanctionOrder = sanctionOrder
            )
            viewModel.getRfNonLivingAreaInformation(requestLRLVRQ)

            NonAreaInformation()






        }

        binding.RFTioletLayout.btnToiletPrevious.setOnClickListener {
            binding.tvlivingareainformation.visibility= View.VISIBLE
            binding.livingareainformationLayout.LivingAreaInformationExpand.visibility= View.VISIBLE
            binding.tvRFTiolet.visibility= View.GONE

        }





    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")

    private fun NonAreaInformation() {
        viewModel.NonAreaInformationRoom.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {


                            binding.RFNonLivingAreaLayout.kitchenLength.text=
                            safeText(x.kitchenLength)
                            binding.RFNonLivingAreaLayout.kitchenWidth.text=
                                safeText(x.kitchenWidth)
                            binding.RFNonLivingAreaLayout.KitchenArea.text=
                                safeText(x.kitchenArea)
                            binding.RFNonLivingAreaLayout.NumberOfSeats.text=
                                safeText(x.noOfSeats)
                            binding.RFNonLivingAreaLayout.PreparedFood.text=
                                safeText(x.preparedFood)
                            binding.RFNonLivingAreaLayout.RecreationLength.text=
                                safeText(x.recreationLength)
                            binding.RFNonLivingAreaLayout.RecreationArea.text=
                                safeText(x.recreationArea)
                            binding.RFNonLivingAreaLayout.RecreationWidth.text=
                                safeText(x.recreationWidth)
                            binding.RFNonLivingAreaLayout.DiningArea.text=
                                safeText(x.diningArea)
                            binding.RFNonLivingAreaLayout.DiningLength.text=
                                safeText(x.diningLength)

                            binding.RFNonLivingAreaLayout.DiningWidth.text=
                                safeText(x.diningWidth)
                            binding.RFNonLivingAreaLayout.ReceptionArea.text=
                                safeText(x.recreationArea)

                            PreparedFoodFile=x.preprationFoodPdf
                            ReceptionAreaPdf= x.receptionAreaPdf.toString()
                        }
                    }

                    202 -> Toast.makeText(
                        requireContext(),
                        "No data available.",
                        Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(),
                        "Please upgrade your app.",
                        Toast.LENGTH_SHORT
                    ).show()

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

        //NonLiving Area Adapter
        tvNonLivingAreaAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.RFNonLivingAreaLayout.SpinnerNonLivingAreaInformation.setAdapter(tvNonLivingAreaAdapter)

        binding.RFNonLivingAreaLayout.SpinnerNonLivingAreaInformation.setOnItemClickListener { parent, view, position, id ->
            selectedNonAreaInfoApproval = parent.getItemAtPosition(position).toString()
            if (selectedNonAreaInfoApproval == "Send for modification") {
                binding.RFNonLivingAreaLayout.tvNonLivingAreaInformationRemarks.visibility =
                    View.VISIBLE
                binding.RFNonLivingAreaLayout.etNonLivingAreaInformationRemarks.visibility =
                    View.VISIBLE


            } else {

                binding.RFNonLivingAreaLayout.tvNonLivingAreaInformationRemarks.visibility = View.GONE
                binding.RFNonLivingAreaLayout.etNonLivingAreaInformationRemarks.visibility =
                    View.GONE

            }
//
        }
        binding.RFNonLivingAreaLayout.btnNonLivingAreaInformationPrevious.setOnClickListener {

            binding.tvRFConstraintLayoutNonLivingArea.visibility= View.GONE
            binding.RFTioletLayout.toiletsExpand.visibility = View.VISIBLE



        }
        binding.RFNonLivingAreaLayout.btnNonLivingAreaInformationNext.setOnClickListener {


            if (selectedNonAreaInfoApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }

            binding.RFNonLivingAreaLayout.viewNonLivingAreaInfor.visibility = View.GONE
            binding.RFNonLivingAreaLayout.NonLivingAreaInfoExpand.visibility = View.GONE
            binding.tvRFConstraintLayoutIndoorGame.visibility=View.VISIBLE
            binding.RFNonLivingAreaLayout.tvNonLivingAreaInfor.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )

//


//            Ajit Ranjan create 04/Novmber/2025  getRfIndoorGameDetails

//            IndoorGameRFAdapter



            adapterIndoorGame = IndoorGameRFAdapter(RFindoorGamesList) { game ->
                removeGame(game)
            }
            binding.RFIndoorGameLayout.recyclerViewInddorGame.adapter = adapterIndoorGame
            binding.RFIndoorGameLayout.recyclerViewInddorGame.layoutManager = LinearLayoutManager(requireContext())

            val rfGameRequest = RFGameRequest(
                appVersion = BuildConfig.VERSION_NAME,
                tcId = centerId.toInt(),
                sanctionOrder = sanctionOrder,
                imeiNo=AppUtil.getAndroidId(requireContext())
            )
            viewModel.getRfIndoorGameDetails(rfGameRequest)

            IndoorGameRecyclerView()

        }


    }


    @SuppressLint("SuspiciousIndentation")
    private fun collectInsfrastructureDetailsAndComplains() {
        viewModel.CompliancesRFQTReqRFQT.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {
                            binding.infrastructureDetailsAndCompliancesLayout.onwershipOfBulding.text=x.ownership
                            binding.infrastructureDetailsAndCompliancesLayout.areaOfTheBuilding.text = x.buildingArea
                            binding.infrastructureDetailsAndCompliancesLayout.HostelNameBoard.text = x.hostelNameBoard
                            binding.infrastructureDetailsAndCompliancesLayout.BasicInformationBoard.text = x.basicInformationBoard
                            binding.infrastructureDetailsAndCompliancesLayout.SecuringWiresDone.text = x.securingWiresDone
                            binding.infrastructureDetailsAndCompliancesLayout.roofBuildingLabel.text = x.roof
//                            binding.infrastructureDetailsAndCompliancesLayout.WhetherItIsStructurally.text = x.roof
                            binding.infrastructureDetailsAndCompliancesLayout.visibleSignsOfLeakages.text = x.leakage
                            binding.infrastructureDetailsAndCompliancesLayout.ConformanceToDduGky.text = x.conformanceDdu
                            binding.infrastructureDetailsAndCompliancesLayout.ProtectionOfStairs.text = x.protectionStairs
                            binding.infrastructureDetailsAndCompliancesLayout.CirculatingArea.text = x.circulatingArea
                            binding.infrastructureDetailsAndCompliancesLayout.Corridor.text = x.corridor
//                            binding.infrastructureDetailsAndCompliancesLayout.ElectricalWiringAndStandards.text = x.corridor
                            binding.infrastructureDetailsAndCompliancesLayout.SwitchBoardsAndPanelBoards.text = x.switchBoardsPanelBoards
                            binding.infrastructureDetailsAndCompliancesLayout.StudentEntitlementBoard.text = x.studentEntitlementBoard
                            binding.infrastructureDetailsAndCompliancesLayout.FoodSpecificationBoard.text = x.foodSpecificationBoard
                            binding.infrastructureDetailsAndCompliancesLayout.Area.text = x.openSpaceArea



                           RFQInfraDetailsbuildingPlanFile = x.buildingPlanFile.toString()
                            RFQInfraDetailprotectionStairsProofFile =x.protectionStairsProofFile.toString()
                            RFQInfraDetailhostelNameBoardProofFile = x.hostelNameBoardProofFile.toString()
                             RFQInfraDetailfoodSpecificationBoardFile = x.foodSpecificationBoardFile.toString()
                             RFQInfraDetailbasicInformationBoardproofFile = x.basicInformationBoardproofFile.toString()
                             RFQInfraDetailbasicsecuringWiresDoneProofFile = x.securingWiresDoneProofFile.toString()
                            RFQInfraDetailcorridorProofFile =x.corridorProofFile.toString()
                            RFQInfraDetailcirculatingAreaProofFile = x.circulatingAreaProofFile.toString()
                            RFQInfraDetailbuildingPhotosFile = x.buildingPhotosFile.toString()
                           RFQInfraDetailleakagesProofFile =x.leakagesProofFile.toString()
                            RFQInfraDetailconformanceDduProofFile= x.conformanceDduProofFile.toString()
                           RFQInfraDetailswitchBoardsPanelBoardsProofFile= x.switchBoardsPanelBoardsProofFile.toString()
                            RFQInfraDetailcontactDetailImportantPeopleproofFile= x.contactDetailImportantPeopleproofFile.toString()
                           RFQInfraDetailstudentEntitlementBoardProofFile= x.studentEntitlementBoardProofFile.toString()


                        }
                    }

                    202 -> Toast.makeText(
                        requireContext(),
                        "No data available.",
                        Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(),
                        "Please upgrade your app.",
                        Toast.LENGTH_SHORT
                    ).show()

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

        //Adapter Electrical
        tcElectricalAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.infrastructureDetailsAndCompliancesLayout.SpinnerIDC.setAdapter(tcElectricalAdapter)

        binding.infrastructureDetailsAndCompliancesLayout.SpinnerIDC.setOnItemClickListener { parent, view, position, id ->
            selectedIDCApproval = parent.getItemAtPosition(position).toString()
            if (selectedIDCApproval == "Send for modification") {
                binding.infrastructureDetailsAndCompliancesLayout.tvSelectApprovalIDC.visibility =
                    View.VISIBLE
                binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks.visibility =
                    View.VISIBLE


            } else {

                binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks.visibility = View.GONE
                binding.infrastructureDetailsAndCompliancesLayout.tvSelectApprovalIDC.visibility =
                    View.GONE

            }
//
        }

        binding.infrastructureDetailsAndCompliancesLayout.btnIDCPrevious.setOnClickListener {
            binding.tvinfrastructureDetailsAndCompliances.visibility= View.GONE
            binding.residentialfacilityqteamInfoLayout.RFQTInfoExpand.visibility= View.VISIBLE
        }
        binding.infrastructureDetailsAndCompliancesLayout.btnIDCNext.setOnClickListener {


            if (selectedIDCApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }
            binding.infrastructureDetailsAndCompliancesLayout.viewIDC.visibility = View.GONE
            binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand.visibility = View.GONE
            binding.tvlivingareainformation.visibility=View.VISIBLE
            binding.infrastructureDetailsAndCompliancesLayout.tvIDC.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }
            if (selectedIDCApproval == "Send for modification") {
                selectedIDCRemarks = binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks.text.toString()
                if (selectedIDCRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
//                return@setOnClickListener
            } else selectedIDCRemarks = ""

//            Ajit Ranjan create 27/October/2025  getRfLivingAreaInformation

            adapter = LivingAreaInformationAdapter(emptyList()) { center ->

                val requestTcRoomDetails = RfLivingAreaInformationRQ(
                    appVersion = BuildConfig.VERSION_NAME,
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    roomNo = center.roomNo.toInt(),
                )

                    viewModel.getRfLivingAreaInformation(requestTcRoomDetails)

                viewModel.fLivingAreaInformation.observe(viewLifecycleOwner) { result ->
                    result.onSuccess {
                        when (it.responseCode) {
                            200 -> {

                                val tcInfoData = it.wrappedList
                                for (x in tcInfoData) {
                                    // Wait for 2 seconds
                                    val binding =
                                        RoominformationPopdialogBinding.inflate(layoutInflater)
                                    val dialog = AlertDialog.Builder(requireContext())
                                        .setView(binding.root)
                                        .create()
                                    dialog.show()
                                    binding.progressBar.visibility = View.VISIBLE
//                                    delay(2000L)

                                    // Hide progress bar
                                    binding.progressBar.visibility = View.GONE


                                    // Now set data to TextViews


                                    binding.laiTypeOfRoof.text =
                                        safeText(x.roofType)
                                    binding.laiFalseCelling.text = safeText(x.falseCeiling)
                                    binding.laiHeightofCelling.text = safeText(x.ceilingHeight.toString())
//                                    binding.roofBuildingLabel.text = safeText(ventilationArea.toString())
                                    binding.laiLength.text = safeText(x.length.toString())
                                    binding.laiWidth.text = safeText(x.width.toString())
                                    binding.laiArea.text = safeText(x.area.toString())
                                    binding.laiwindowsArea.text = safeText(x.windowArea.toString())
                                    binding.laiCotInNo.text = safeText(x.cot.toString())
                                    binding.laiMattersInNo.text = safeText(x.mattress.toString())
                                    binding.laiBedSheetInNo.text = safeText(x.bedSheet.toString())
                                    binding.laiAirCondtion.text = safeText(x.airCondtion.toString())
                                    binding.laiLights.text = safeText(x.lights.toString())
                                    binding.laiStorage.text = safeText(x.storage.toString())
                                    binding.LiaBasicInformationBoard.text = safeText(x.infoBoard.toString())
                                    binding.LiaBasicInformationBoard.text = safeText(x.infoBoard.toString())
                                    binding.LiaBasicInformationBoard.text = safeText(x.infoBoard.toString())
                                    binding.LiaBasicInformationBoard.text = safeText(x.infoBoard.toString())

                                    //open Image

                                    binding.laiTypeOfRoofFile.setOnClickListener {
                                        x.roofTypePdf?.let { it1 ->
                                            openBase64Pdf(requireContext(),
                                                it1
                                            )
                                        }
                                    }
                                    binding.laiFalseCellingFile.setOnClickListener {
                                        x.falseCeilingPdf?.let { it1 ->
                                            openBase64Pdf(requireContext(),
                                                it1
                                            )
                                        }
                                    }


                                    binding.laiHeightofCellingFile.setOnClickListener {
                                        x.ceilingHeightPdf?.let { it1 ->
                                            openBase64Pdf(requireContext(),
                                                it1
                                            )
                                        }
                                    }
                                    binding.laiwindowsAreaFile.setOnClickListener {
                                        x.windowAreaPdf?.let { it1 ->
                                            openBase64Pdf(requireContext(),
                                                it1
                                            )
                                        }
                                    }
                                    binding.laiCotInNoFile.setOnClickListener {
                                        x.cotPdf?.let { it1 ->
                                            openBase64Pdf(requireContext(),
                                                it1
                                            )
                                        }
                                    }
                                    binding.laiMattersInNoFile.setOnClickListener {
                                        x.mattressPdf?.let { it1 ->
                                            openBase64Pdf(requireContext(),
                                                it1
                                            )
                                        }
                                    }
                                    binding.laiBedSheetInNoFile.setOnClickListener {
                                        x.bedSheetPdf?.let { it1 ->
                                            openBase64Pdf(requireContext(),
                                                it1
                                            )
                                        }
                                    }
                                    binding.laiAirCondtionFile.setOnClickListener {
                                        x.airConditionPdf?.let { it1 ->
                                            openBase64Pdf(requireContext(),
                                                it1
                                            )
                                        }
                                    }

                                    binding.laiLightsFile.setOnClickListener {
                                        x.lightPdf?.let { it1 ->
                                            openBase64Pdf(requireContext(),
                                                it1
                                            )
                                        }
                                    }

                                    binding.laiStorageFile.setOnClickListener {
                                        x.storagePdf?.let { it1 ->
                                            openBase64Pdf(requireContext(),
                                                it1
                                            )
                                        }
                                    }

                                    binding.backButton.setOnClickListener { dialog.dismiss() }
                                }

                            }






//


                            202 -> Toast.makeText(
                                requireContext(),
                                "No data available.",
                                Toast.LENGTH_SHORT
                            ).show()

                            301 -> Toast.makeText(
                                requireContext(),
                                "Please upgrade your app.",
                                Toast.LENGTH_SHORT
                            ).show()

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
            binding.livingareainformationLayout.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.livingareainformationLayout.recyclerView.adapter = adapter
            val livingRoomlistViewReq = LivingRoomListViewRQ(
                appVersion = BuildConfig.VERSION_NAME,
                tcId = centerId.toInt(),
                sanctionOrder = sanctionOrder,
            )

            viewModel.getlivingRoomListView(livingRoomlistViewReq)

            RoomRecyclerView()

        }


    }

    @SuppressLint("SuspiciousIndentation")
    private  fun IndoorGameRecyclerView(){
        viewModel.RfIndoorGameDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {

                when (it.responseCode) {
                    200 -> {





                        adapterIndoorGame.updateData(it.wrappedList ?: emptyList())
//                        binding.RFIndoorGameLayout.SpinnerIndoorGame.visibility =
//                            View.VISIBLE
//                        binding.RFIndoorGameLayout.IndoorGameRemarks.visibility =
//                            View.VISIBLE
//                        binding.RFIndoorGameLayout.etIndoorGameRemarks.visibility =
//                            View.VISIBLE
//
//                        binding.RFIndoorGameLayout.IndoorGameRemarks.visibility =
//                            View.VISIBLE
//                        binding.RFIndoorGameLayout.btnIndoorGameNext.visibility =
//                            View.VISIBLE

                    }

                    202 ->
                    {
//
//                        binding.RFIndoorGameLayout.SpinnerIndoorGame.visibility =
//                            View.GONE
//                    binding.RFIndoorGameLayout.IndoorGameRemarks.visibility =
//                        View.GONE
//                                binding.RFIndoorGameLayout.etIndoorGameRemarks.visibility =
//                                View.GONE
//
//                                binding.RFIndoorGameLayout.IndoorGameRemarks.visibility =
//                        View.GONE
//                                binding.RFIndoorGameLayout.btnIndoorGameNext.visibility =
//                                View.GONE

                        Toast.makeText(
                            requireContext(),
                            "No data available.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }




                    301 ->



                    {
//                        binding.RFIndoorGameLayout.SpinnerIndoorGame.visibility =
//                        View.GONE
////                        binding.RFIndoorGameLayout.IndoorGameRemarks.visibility =
////                            View.GONE
////                        binding.RFIndoorGameLayout.etIndoorGameRemarks.visibility =
////                            View.GONE
//
//                        binding.RFIndoorGameLayout.IndoorGameRemarks.visibility =
//                            View.GONE
//                        binding.RFIndoorGameLayout.btnIndoorGameNext.visibility =
//                            View.GONE

                        Toast.makeText(
                            requireContext(),
                            "Please upgrade your app.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }

                result.onFailure {


//                    binding.RFIndoorGameLayout.SpinnerIndoorGame.visibility =
//                        View.GONE
//                    binding.RFIndoorGameLayout.IndoorGameRemarks.visibility =
//                        View.GONE
//                    binding.RFIndoorGameLayout.etIndoorGameRemarks.visibility =
//                        View.GONE
//
//                    binding.RFIndoorGameLayout.IndoorGameRemarks.visibility =
//                        View.GONE
//                    binding.RFIndoorGameLayout.btnIndoorGameNext.visibility =
//                        View.GONE

                    Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        //Indoor Game Adapter
        RFIndoorGameAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.RFIndoorGameLayout.SpinnerIndoorGame.setAdapter(RFIndoorGameAdapter)

        binding.RFIndoorGameLayout.SpinnerIndoorGame.setOnItemClickListener { parent, view, position, id ->
            selectedIndoorGameApproval = parent.getItemAtPosition(position).toString()
            if (selectedIndoorGameApproval == "Send for modification") {
                binding.RFIndoorGameLayout.IndoorGameRemarks.visibility =
                    View.VISIBLE
                binding.RFIndoorGameLayout.etIndoorGameRemarks.visibility =
                    View.VISIBLE


            } else {

                binding.RFIndoorGameLayout.etIndoorGameRemarks.visibility = View.GONE
                binding.RFIndoorGameLayout.IndoorGameRemarks.visibility =
                    View.GONE

            }
//
        }

//        RFResidentialFacilitiesAvailable
//        RFResidentialConstraintLayoutFacilitiesAvailable
//
//tvRFResidentialFacality
//viewRFResidentialFacality
//SpinnerRFResidentialFacality
//tvRFResidentialFacalityRemarks
//etRFResidentialFacalityRemarks
//btnRFResidentialFacalityPrevious
//btnRFResidentialFacalityNext

        binding.RFIndoorGameLayout.btnIndoorGamePrevious.setOnClickListener {
            binding.tvRFConstraintLayoutIndoorGame.visibility= View.GONE
            binding.RFNonLivingAreaLayout.NonLivingAreaInfoExpand.visibility= View.VISIBLE
        }
        binding.RFIndoorGameLayout.btnIndoorGameNext.setOnClickListener {


            if (selectedIndoorGameApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }
            binding.RFIndoorGameLayout.viewIndoorGame.visibility = View.GONE
            binding.RFIndoorGameLayout.IndoorGameExpand.visibility = View.GONE



            binding.RFResidentialConstraintLayoutFacilitiesAvailable.visibility=View.VISIBLE
            binding.RFIndoorGameLayout.tvIndoorGame.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }
            if (selectedIndoorGameApproval == "Send for modification") {
                selectedIndoorGameApprovalRemark = binding.RFIndoorGameLayout.etIndoorGameRemarks.text.toString()
                if (selectedIndoorGameApprovalRemark.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
//                return@setOnClickListener
            } else selectedIndoorGameApprovalRemark = ""

//             Ajit Ranjan create 06/Novmber/2025     getResidentialFacilitiesAvailable


// "loginId" : "CHANCHAL",
//    "appVersion" : "1.0",
//	"imeiNo" : "c3070eef6cb45171",
//	"tcId":28,
//	"sanctionOrder":"SO25D050003"




            val requestTcInfo = TrainingCenterInfo(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                tcId =centerId.toInt(),
                sanctionOrder = sanctionOrder,
                imeiNo = AppUtil.getAndroidId(requireContext())
            )


                viewModel.getResidentialFacilitiesAvailable(requestTcInfo)

            ResidentialFacilitiesForm()

//            tcId = 30,
//                sanctionOrder = "SO25D160044",
//
//
        }








    }

    private  fun ResidentialFacilitiesForm(){
        viewModel.RFResidentialFacilitiesAvailable.observe(viewLifecycleOwner) { result ->
            result.onSuccess {

                when (it.responseCode) {
                    200 -> {

                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {

                            binding.RFResidentialFacilitiesAvailable.WardenCare.text=(x.wardenCaretakerFemale)
                            binding.RFResidentialFacilitiesAvailable.MaleDoctor.text=(x.maleDoctor)
                            binding.RFResidentialFacilitiesAvailable.WardenCaretakerMale.text=(x.wardenCaretakerMale)
                            binding.RFResidentialFacilitiesAvailable.HostelsSeparated.text=(x.hostelsSeparated)
                            binding.RFResidentialFacilitiesAvailable.FemaleDoctor.text=(x.femaleDoctor)
                            binding.RFResidentialFacilitiesAvailable.SecurityGuards.text=(x.securityGuards)



                            RFWardenCareFile=x.wardenCaretakerFemalePdf
                            RFMaleDoctorFile=x.maleDoctorPdf
                            RFFemaleDoctorFile=x.femaleDoctorPdf
                            RFWardenCaretakerMaleFile=x.wardenCaretakerMalePdf
                            RFHostelsSeparatedFile =x.hostelsSeparatedPdf
                            RFSecurityGuardsFile=x.securityGuardsPdf







                        }





                    }

                    202 ->
                    {


                        Toast.makeText(
                            requireContext(),
                            "No data available.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }




                    301 ->



                    {

                        Toast.makeText(
                            requireContext(),
                            "Please upgrade your app.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }

                result.onFailure {

                    Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        //Indoor Game Adapter
        RFIndoorGameAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.RFIndoorGameLayout.SpinnerIndoorGame.setAdapter(RFIndoorGameAdapter)

        binding.RFIndoorGameLayout.SpinnerIndoorGame.setOnItemClickListener { parent, view, position, id ->
            selectedIndoorGameApproval = parent.getItemAtPosition(position).toString()
            if (selectedIndoorGameApproval == "Send for modification") {
                binding.RFIndoorGameLayout.IndoorGameRemarks.visibility =
                    View.VISIBLE
                binding.RFIndoorGameLayout.etIndoorGameRemarks.visibility =
                    View.VISIBLE


            } else {

                binding.RFIndoorGameLayout.etIndoorGameRemarks.visibility = View.GONE
                binding.RFIndoorGameLayout.IndoorGameRemarks.visibility =
                    View.GONE

            }
//
        }

        binding.RFIndoorGameLayout.btnIndoorGamePrevious.setOnClickListener {
            binding.tvRFConstraintLayoutIndoorGame.visibility= View.GONE
            binding.RFNonLivingAreaLayout.NonLivingAreaInfoExpand.visibility= View.VISIBLE
        }
        binding.RFIndoorGameLayout.btnIndoorGameNext.setOnClickListener {


            if (selectedResidintislFacilityApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }
            binding.RFResidentialFacilitiesAvailable.viewRFResidentialFacality.visibility = View.GONE
            binding.RFResidentialFacilitiesAvailable.RFResidentialFacalityExpand.visibility = View.GONE



            binding.RFResidentialConstraintLayoutFacilitiesAvailable.visibility=View.VISIBLE
            binding.RFResidentialFacilitiesAvailable.tvRFResidentialFacality.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }
            if (selectedResidintislFacilityApproval == "Send for modification") {
                selectedResidintislFacilityApprovalRemark = binding.RFResidentialFacilitiesAvailable.etRFResidentialFacalityRemarks.text.toString()
                if (selectedResidintislFacilityApprovalRemark.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
//                return@setOnClickListener
            } else selectedResidintislFacilityApprovalRemark = ""





        }








    }



       private  fun RoomRecyclerView(){
           viewModel.livingRoomListView.observe(viewLifecycleOwner) { result ->
               result.onSuccess {
                   when (it.responseCode) {     200 -> adapter.updateData(it.wrappedList ?: emptyList())








                       202 -> Toast.makeText(
                           requireContext(),
                           "No data available.",
                           Toast.LENGTH_SHORT
                       ).show()

                       301 -> Toast.makeText(
                           requireContext(),
                           "Please upgrade your app.",
                           Toast.LENGTH_SHORT
                       ).show()

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
    private  fun ToiletRecyclerView() {




            viewModel.ToiletRoomInformationView.observe(viewLifecycleOwner) { result ->
                result.onSuccess {
                    when (it.responseCode) {
                        200 -> {

                            val tcInfoData = it.wrappedList
                            for (x in tcInfoData) {
                                // Wait for 2 seconds
                                val binding =
                                    TriPopdialogBinding.inflate(layoutInflater)
                                val dialog = AlertDialog.Builder(requireContext())
                                    .setView(binding.root)
                                    .create()
                                dialog.show()
                                binding.progressBar.visibility = View.VISIBLE
//                                    delay(2000L)

                                // Hide progress bar
                                binding.progressBar.visibility = View.GONE


                                // Now set data to TextViews
                                binding.triTypeOfFlooring.text =
                                    safeText(x.flooring)
                                binding.ConnectionToRunningWater.text = safeText(x.runningWater)
                                binding.ToiletType.text =
                                    safeText(x.type.toString())
                                binding.TriLights.text = safeText(x.lights.toString())
                                binding.FemaleUrinal.text = safeText(x.femaleUrinal.toString())
                                binding.FemaleWashbasin.text = safeText(x.femaleWashbasin.toString())
                                binding.OverheadTank.text = safeText(x.overheadTank.toString())



                                //open Image
                                binding.laiTypeOfFlooringFile.setOnClickListener {
                                    x.floorPdf?.let { it1 ->
                                        openBase64Pdf(
                                            requireContext(),
                                            it1
                                        )
                                    }
                                }
                                binding.TriLightsFile.setOnClickListener {
                                    x.lightPdf?.let { it1 ->
                                        openBase64Pdf(
                                            requireContext(),
                                            it1
                                        )
                                    }
                                }




                                binding.backButton.setOnClickListener { dialog.dismiss() }
                            }

                        }


//


                        202 -> Toast.makeText(
                            requireContext(),
                            "No data available.",
                            Toast.LENGTH_SHORT
                        ).show()

                        301 -> Toast.makeText(
                            requireContext(),
                            "Please upgrade your app.",
                            Toast.LENGTH_SHORT
                        ).show()

                        401 -> AppUtil.showSessionExpiredDialog(
                            findNavController(),
                            requireContext()
                        )
                    }
                }
                result.onFailure {
                    Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            viewModel.loading.observe(viewLifecycleOwner) { loading ->
                binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE


            }






        binding.RFTioletLayout.recyclerViewToilet.layoutManager = LinearLayoutManager(requireContext())
        binding.RFTioletLayout.recyclerViewToilet.adapter = adapterToilet

        val livingRoomlistViewReq = LivingRoomListViewRQ(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,)

        viewModel.getToiletRoomListView(livingRoomlistViewReq)






        viewModel.ToiletRoomListView.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {     200 -> adapterToilet.updateData(it.wrappedList ?: emptyList())













                    202 -> Toast.makeText(
                        requireContext(),
                        "No data available.",
                        Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(),
                        "Please upgrade your app.",
                        Toast.LENGTH_SHORT
                    ).show()

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

           fun safeText(value: String?): String {
               return if (value.isNullOrBlank() || value.equals("null", ignoreCase = true)) {
                   "N/A"
               } else value
           }
    private fun openBase64Pdf(context: Context, base64: String) {
        try {
            // 1. Clean Base64 (remove header if present)
            val cleanBase64 = base64
                .replace("data:application/pdf;base64,", "")
                .trim()

            // 2. Decode Base64
            val pdfBytes = Base64.decode(cleanBase64, Base64.DEFAULT)

            // 3. Verify PDF header
            if (pdfBytes.isEmpty() || !String(pdfBytes.copyOfRange(0, 4)).startsWith("%PDF")) {
                Toast.makeText(context, "Invalid PDF data", Toast.LENGTH_SHORT).show()
                return
            }

            // 4. Save temporarily in cache
            val pdfFile = File.createTempFile("temp_", ".pdf", context.cacheDir)
            pdfFile.outputStream().use { it.write(pdfBytes) }

            // 5. Get URI via FileProvider
            val uri: Uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",  // must match manifest authority
                pdfFile
            )

            // 6. Create intent
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            // 7. Check if any app can handle PDFs
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(Intent.createChooser(intent, "Open PDF with"))
            } else {
                Toast.makeText(context, "No PDF viewer installed", Toast.LENGTH_SHORT).show()
//                openPdfFile(base64)


            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to open PDF", Toast.LENGTH_SHORT).show()
        }
    }




    private fun showBase64ImageDialog(context: Context, base64ImageString: String?, title: String = "Image") {
        val imageView = ImageView(context)

        // Decode Base64 → Bitmap
        val bitmap: Bitmap? = if (!base64ImageString.isNullOrBlank()) {
            try {
                val cleanBase64 = base64ImageString
                    .replace("data:image/png;base64,", "")
                    .replace("data:image/jpg;base64,", "")
                    .replace("data:image/jpeg;base64,", "")
                    .replace("\\s".toRegex(), "")

                val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }

        // If bitmap is null → show default image
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
        } else {
            imageView.setImageResource(R.drawable.no_image) // your fallback drawable
        }

        imageView.adjustViewBounds = true
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.setPadding(20, 20, 20, 20)

        // Show in dialog
        AlertDialog.Builder(context)
            .setTitle(title)
            .setView(imageView)
            .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
            .show()
    }





    private fun removeGame(game: IndoorRFGameResponseDetails) {
//
    }


//    private fun openPdfFile(filePath: String) {
//        try {
//            val file = File(filePath)
//            val uri = FileProvider.getUriForFile(
//                requireContext(),
//                "${requireContext().packageName}.provider",
//                file
//            )
//
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.setDataAndType(uri, "application/pdf")
//            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
//
//            // Try to open with an installed PDF viewer
//            startActivity(intent)
//
//        } catch (e: ActivityNotFoundException) {
//            // No PDF viewer found, redirect to Play Store
//            redirectToPlayStore("com.adobe.reader") // or any PDF app package name
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Toast.makeText(requireContext(), "Error opening PDF", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun redirectToPlayStore(packageName: String) {
//        try {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
//            startActivity(intent)
//        } catch (e: Exception) {
//            val intent = Intent(
//                Intent.ACTION_VIEW,
//                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
//            )
//            startActivity(intent)
//        }
//    }




}






