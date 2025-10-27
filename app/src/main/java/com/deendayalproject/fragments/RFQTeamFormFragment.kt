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
import com.deendayalproject.adapter.DescriptionAcademiaAdapter
import com.deendayalproject.databinding.CounsellingRoomBinding
import com.deendayalproject.databinding.DomainLabLayoutBinding
import com.deendayalproject.databinding.ItCumDomainLabLayoutBinding
import com.deendayalproject.databinding.ItLabLayoutBinding
import com.deendayalproject.databinding.OfficeCumCouncelingRoomLayoutBinding
import com.deendayalproject.databinding.OfficeRoomLayoutBinding
import com.deendayalproject.databinding.ReceptionAreaLayoutBinding
import com.deendayalproject.databinding.RfQteamFormFagmentBinding
import com.deendayalproject.databinding.TheoryClassRoomBinding
import com.deendayalproject.databinding.TheoryCumDomainLabLayoutBinding
import com.deendayalproject.databinding.TheoryCumItLabLayoutBinding
import com.deendayalproject.model.request.AllRoomDetaisReques
import com.deendayalproject.model.request.CompliancesRFQTReq
import com.deendayalproject.model.request.RfLivingAreaInformationResponseRQ
import com.deendayalproject.model.request.TrainingCenterInfo
import com.deendayalproject.model.response.RoomDetail
import com.deendayalproject.model.response.RoomItem
import com.deendayalproject.model.response.Trainer
import com.deendayalproject.util.AppUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class RFQTeamFormFragment : Fragment() {

    private var _binding: RfQteamFormFagmentBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: SharedViewModel
    var dataStaffList: MutableList<Trainer> = mutableListOf()
    var academiaList: MutableList<RoomItem> = mutableListOf()


    private val approvalList = listOf("Approved", "Send for modification")
    private lateinit var tcInfoAdapter: ArrayAdapter<String>
    private var selectedTcInfoApproval = ""
    private var selectedTcInfoRemarks = ""

    private lateinit var tcDescAcademiaAdapter: ArrayAdapter<String>
    private var selectedTcDescAcademiaApproval = ""
    private var selectedTcDescAcademiaRemarks = ""


    private lateinit var roomDetails: RoomDetail

    private lateinit var tcInfraAdapter: ArrayAdapter<String>
    private var selectedTcInfraApproval = ""
    private var selectedTcInfraRemarks = ""

    private lateinit var tcBasinAdapter: ArrayAdapter<String>
    private var selectedRFQTApproval = ""
    private var selectedIDCApproval = ""
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



    private var selectedRFQTRemarks = ""
    private var selectedIDCRemarks = ""


    private lateinit var tcDescOtherAreaAdapter: ArrayAdapter<String>
    private var selectedTcDescOtherAreaApproval = ""
    private var selectedTcDescOtherAreaRemarks = ""


    private lateinit var tcTeachingAdapter: ArrayAdapter<String>
    private var selectedTcTeachingApproval = ""
    private var selectedTcTeachingRemarks = ""


    private lateinit var tcGeneralAdapter: ArrayAdapter<String>
    private var selectedTcGeneralApproval = ""
    private var selectedTcGeneralRemarks = ""


    private lateinit var tcElectricalAdapter: ArrayAdapter<String>
    private var selectedTcElectricalApproval = ""
    private var selectedTcElectricalRemarks = ""


    private lateinit var tcSignageAdapter: ArrayAdapter<String>
    private var selectedTcSignageApproval = ""
    private var selectedTcSignageRemarks = ""


    private lateinit var tcIpEnableAdapter: ArrayAdapter<String>
    private var selectedTcIpEnableApproval = ""
    private var selectedTcIpEnableRemarks = ""


    private lateinit var tcCommonEquipmentAdapter: ArrayAdapter<String>
    private var selectedTcCommonEquipmentApproval = ""
    private var selectedTcCommonEquipmentRemarks = ""


    private lateinit var tcAvailSupportInfraAdapter: ArrayAdapter<String>
    private var selectedTcAvailSupportInfraApproval = ""
    private var selectedTcAvailSupportInfraRemarks = ""


    private lateinit var tcAvailOfStandardFormAdapter: ArrayAdapter<String>
    private var selectedTcAvailOfStandardFormApproval = ""
    private var selectedTcAvailOfStandardFormRemarks = ""

    //All Room Var

    private var fansRoomImage = ""
    private var writingBoard = ""
    private var internetConnectionImage = ""
    private var roomInfoBoardImage = ""
    private var digitalProjectorImage = ""
    private var officeComputer = ""
    private var printerScannerImage = ""
    private var centerSoundProof = ""
    private var falseCeiling = ""
    private var tablet = ""
    private var typingTuterCompImage = ""
    private var lanEnabledImage = ""
    private var internalSignageImage = ""
    private var airConditionRoom = ""
    private var roomsPhotographs = ""
    private var roomsPhotographsImage = ""
    private var audioCamera = ""
    private var lanEnabled = ""
    private var soundLevelImage = ""
    private var centerSoundProofImage = ""
    private var digitalCameraRoomImage = ""
    private var internetConnection = ""
    private var officeChair = ""
    private var officeTableImage = ""
    private var printerScanner = ""
    private var trainerChair = ""
    private var domainEquipmentImage = ""
    private var ecPowerBackup = ""
    private var tabletImage = ""
    private var soundLevel = ""
    private var trainerTable = ""
    private var falseCeilingImage = ""
    private var roomInfoBoard = ""
    private var roofTypeImage = ""
    private var digitalProjector = ""
    private var secureDocumentStorage = ""
    private var airConditionRoomImage = ""
    private var sounfLevelSpecific = ""
    private var ventilationArea = ""
    private var domainEquipment = ""
    private var officeTable = ""
    private var officeChairImage = ""
    private var typingTuterComp = ""
    private var ceilingHeightImage = ""
    private var candidateChair = ""
    private var candidateChairImage = ""
    private var ceilingHeight = ""
    private var lightsImage = ""
    private var secureDocumentStorageImage = ""
    private var writingBoardImage = ""
    private var lights = ""
    private var digitalCamera = ""
    private var audioCameraImage = ""
    private var internalSignage = ""
    private var trainerChairImage = ""
    private var ventilationAreaImage = ""
    private var roofType = ""
    private var trainerTableImage = ""
    private var fans = ""
    private var officeComputerImagePath = ""
    private var ecPowerBackupImage = ""

    //end all room var


    private var centerId = ""
    private var sanctionOrder = ""
    private var centerName = ""

    //    private var selfDeclarationPdf = ""
    private var RFQTBasicInfoPdf = ""
    private var buildingPdf = ""
    private var schematicPdf = ""
    private var internalExternalWallPdf = ""


    private var maleToiletImage = ""
    private var maleToiletSignageImage = ""
    private var maleToiletUrinalsImage = ""
    private var maleToiletWashbasinImage = ""


    private var femaleToiletImage = ""
    private var femaleToiletSignageImage = ""
    private var femaleToiletWashbasinImage = ""
    private var ovrHeadTankImage = ""
    private var typeOfFlooringImage = ""


    private var fansImage = ""
    private var circulationAreaImage = ""
    private var openSpaceImage = ""
    private var parkingSpaceImage = ""
    private var welcomeKitImage = ""
    private var signOfLeakageImage = ""
    private var protectionStairsBalImage = ""
    private var securingWiringImage = ""
    private var switchBoardImage = ""

    var roomData: RoomDetail? = null
    private var tcNameBoardImage = ""
    private var activitySummaryBoardImage = ""
    private var studentEntitlementBoardImage = ""
    private var contactDetailImpoPeopleImage = ""
    private var basicInfoBoardImage = ""
    private var codeOfConductImage = ""
    private var studentAttendanceImage = ""
    private var centralMonitorImage = ""
    private var conformationOfCCTVImage = ""
    private var storageOfCCtvImage = ""
    private var dvrImage = ""


    private var electricPowerImage = ""
    private var installBiometricImage = ""
    private var installationCCTVImage = ""
    private var storagePlaceSecuringDocImage = ""
    private var printerCumImage = ""
    private var digitalCameraImage = ""
    private var grievanceImage = ""
    private var minimumEquipmentImage = ""
    private var directionBoardsImage = ""
    private var safeDrinkingImage = ""
    private var fireFightingImage = ""
    private var firstAidImage = ""


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
//        val token = AppUtil.getSavedTokenPreference(requireContext())
//
//
//        val TokeValue=token
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
//            return@setOnClickListener
            binding.residentialfacilityqteamInfoLayout.viewRFQTInfo.visibility = View.GONE
            binding.residentialfacilityqteamInfoLayout.RFQTInfoExpand.visibility = View.GONE
            binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand.visibility = View.VISIBLE
            binding.tvinfrastructureDetailsAndCompliances.visibility = View.VISIBLE
            binding.infrastructureDetailsAndCompliancesLayout.viewIDC.visibility = View.VISIBLE
//            binding.mainDescAcademia.visibility = View.GONE
//            binding.viewDescAcademia.visibility = View.GONE
//            viewIDC
//    IDetailsComplainExpand
//    llTopIDC
//    tvIDC


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
                facilityId = "1",
                imeiNo = AppUtil.getAndroidId(requireContext())
            )

            viewModel.getCompliancesRFQTReqRFQT(requestCompliancesRFQT)
            collectInsfrastructureDetailsAndComplains()
        }


    }

    @SuppressLint("SetTextI18n")
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
//        binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand.visibility = View.GONE


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

//            cons- LivingAreaInformationExpand
//    lin llTopLAI
//    textView tvLAI


//            return@setOnClickListener
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

//            Ajit Ranjan create 24/October/2026  CompliancesRFQTReqRFQT
//            val rfLAreaInformationResponserq = RfLivingAreaInformationResponseRQ(
//                appVersion = BuildConfig.VERSION_NAME,
//                tcId = centerId.toInt(),
//                sanctionOrder = sanctionOrder,
//            )
//
//            viewModel.getRfLivingAreaInformation(rfLAreaInformationResponserq)

            RoomRecyclerView()

        }


    }
       private  fun RoomRecyclerView(){

           binding.livingareainformationLayout.recyclerView.layoutManager = LinearLayoutManager(requireContext())
           binding.livingareainformationLayout.recyclerView.adapter = DescriptionAcademiaAdapter(academiaList) { room ->


               when (room.roomType) {

                   "Theory Class Room" -> {
                       val binding = TheoryClassRoomBinding.inflate(layoutInflater)
                       val dialog = AlertDialog.Builder(requireContext())
                           .setView(binding.root)
                           .create()
                       dialog.show()

                       val requestTcRoomDetails = AllRoomDetaisReques(
                           loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                           imeiNo = AppUtil.getAndroidId(requireContext()),
                           appVersion = BuildConfig.VERSION_NAME,
                           tcId = centerId,
                           roomType = room.roomType,
                           roomNo = room.roomNo.toInt(),
                           sanctionOrder = sanctionOrder,
                       )

                       // Show progress bar
                       binding.progressBar.visibility = View.VISIBLE


                       viewLifecycleOwner.lifecycleScope.launch {
                           // Call API
                           viewModel.getAcademicRoomDetails(requestTcRoomDetails)

                           // Wait for 2 seconds
                           delay(2000L)

                           // Hide progress bar
                           binding.progressBar.visibility = View.GONE


                           // Now set data to TextViews
                           binding.yesNoTypeOfRoof.text =
                               safeText(roofType)
                           binding.yesNoFalseCeiling.text = safeText(falseCeiling)
                           binding.yesNoHeightCeiling.text = safeText(ceilingHeight.toString())
                           binding.yesNoVentilationArea.text = safeText(ventilationArea.toString())
                           binding.yesNoSoundLevel.text = safeText(soundLevel.toString())
                           binding.yesNoSoundProofAC.text = safeText(centerSoundProof)
                           binding.yesNoInfoBoard.text = safeText(roomInfoBoard)
                           binding.yesNoInternalSignage.text = safeText(internalSignage)
                           binding.yesNoCCTV.text = safeText(audioCamera)
                           binding.yesNoLCDComputers.text = safeText(digitalProjector)
                           binding.yesNoChairForCan.text = safeText(candidateChair)
                           binding.yesNoWritingBoard.text = safeText(writingBoard)
                           binding.yesNoTrainerChair.text = safeText(trainerChair)
                           binding.yesNoTrainerTable.text = safeText(trainerTable)
                           binding.yesNoLights.text = safeText(lights.toString())
                           binding.yesNoFans.text = safeText(fans.toString())
                           binding.yesNoPowerBackup.text = safeText(ecPowerBackup)
                           binding.yesNoLabPhoto.text = safeText(roomsPhotographs)
                           binding.yesNoAirConditioning.text = safeText(airConditionRoom)


                           //open Image

                           binding.valueTypeOfRoof.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roofTypeImage,
                                   "Roof Type Image"
                               )
                           }

                           binding.valueFalseCeiling.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   falseCeilingImage,
                                   "False Ceiling Image"
                               )
                           }

                           binding.valueHeightCeiling.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ceilingHeightImage,
                                   "Ceiling Height Image"
                               )
                           }

                           binding.valueVentilationArea.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ventilationAreaImage,
                                   "Ventilation Area Image"
                               )
                           }

                           binding.valueSoundLevel.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   soundLevelImage,
                                   "Sound Level Image"
                               )
                           }

                           binding.valueSoundProofAC.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   centerSoundProofImage,
                                   "Sound Proof & AC Image"
                               )
                           }

                           binding.valueInfoBoard.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomInfoBoardImage,
                                   "Information Board Image"
                               )
                           }

                           binding.valueInternalSignage.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   internalSignageImage,
                                   "Internal Signage Image"
                               )
                           }

                           binding.valueCCTV.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   audioCameraImage,
                                   "CCTV & Audio Image"
                               )
                           }

                           binding.valueLCDComputers.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   digitalProjectorImage,
                                   "Digital Projector / LCD Image"
                               )
                           }

                           binding.valueChairForCan.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   candidateChairImage,
                                   "Candidate Chair Image"
                               )
                           }

                           binding.valueWritingBoard.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   writingBoardImage,
                                   "Writing Board Image"
                               )
                           }

                           binding.valueTrainerChair.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   trainerChairImage,
                                   "Trainer Chair Image"
                               )
                           }

                           binding.valueTrainerTable.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   trainerTableImage,
                                   "Trainer Table Image"
                               )
                           }

                           binding.valueLights.setOnClickListener {
                               showBase64ImageDialog(requireContext(), lightsImage, "Lights Image")
                           }

                           binding.valueFans.setOnClickListener {
                               showBase64ImageDialog(requireContext(), fansRoomImage, "Fans Image")
                           }

                           binding.valuePowerBackup.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ecPowerBackupImage,
                                   "Power Backup Image"
                               )
                           }

                           binding.valueITLabPhoto.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomsPhotographsImage,
                                   "Room Photos"
                               )
                           }

                           binding.valueAirConditioning.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   airConditionRoomImage,
                                   "Air Conditioning Image"
                               )
                           }


                       }

                       binding.backButton.setOnClickListener { dialog.dismiss() }
                   }

                   "Office Cum Counselling Room" -> {
                       val binding = OfficeCumCouncelingRoomLayoutBinding.inflate(layoutInflater)
                       val dialog = AlertDialog.Builder(requireContext())
                           .setView(binding.root)
                           .create()
                       dialog.show()

                       val requestTcRoomDetails = AllRoomDetaisReques(
                           loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                           imeiNo = AppUtil.getAndroidId(requireContext()),
                           appVersion = BuildConfig.VERSION_NAME,
                           tcId = centerId,
                           roomType = room.roomType,
                           roomNo = room.roomNo.toInt(),
                           sanctionOrder = sanctionOrder,
                       )



                       viewLifecycleOwner.lifecycleScope.launch {
                           // Call API
                           viewModel.getAcademicRoomDetails(requestTcRoomDetails)

                           // Wait for 2 seconds
                           delay(2000L)


                           // Set data to TextViews (from variables set after API call)
                           binding.yesNoOfficeRoomPhoto.text = safeText(roomsPhotographs)
                           binding.yesNoRoofType.text = safeText(roofType)
                           binding.yesNoFalseCeiling.text = safeText(falseCeiling)
                           binding.yesNoCeilingHeight.text = safeText(ceilingHeight.toString())
                           binding.yesNoStorage.text = safeText(secureDocumentStorage)
                           binding.yesNoOfficeTable.text = safeText(officeTable)
                           binding.yesNoChairs.text = safeText(officeChair)
                           binding.yesNoComputerTable.text = safeText(officeComputer)
                           binding.yesNoPrinter.text = safeText(printerScanner)
                           binding.yesNoCamera.text = safeText(digitalCamera)
                           binding.yesNoPowerBackup.text = safeText(ecPowerBackup)

                           // Open images on click
                           binding.valueOfficeRoomPhoto.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomsPhotographsImage,
                                   "Room Photo"
                               )
                           }
                           binding.valueRoofType.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roofTypeImage,
                                   "Roof Type Image"
                               )
                           }
                           binding.valueFalseCeiling.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   falseCeilingImage,
                                   "False Ceiling Image"
                               )
                           }
                           binding.valueCeilingHeight.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ceilingHeightImage,
                                   "Ceiling Height Image"
                               )
                           }
                           binding.valueStorage.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   secureDocumentStorageImage,
                                   "Storage Image"
                               )
                           }
                           binding.valueOfficeTable.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   officeTableImage,
                                   "Office Table Image"
                               )
                           }
                           binding.valueChairs.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   officeChairImage,
                                   "Chairs Image"
                               )
                           }
                           binding.valueComputerTable.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   officeComputerImagePath,
                                   "Computer Table Image"
                               )
                           }
                           binding.valuePrinter.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   printerScannerImage,
                                   "Printer / Scanner Image"
                               )
                           }
                           binding.valueCamera.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   digitalCameraImage,
                                   "Digital Camera Image"
                               )
                           }
                           binding.valuePowerBackup.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ecPowerBackupImage,
                                   "Power Backup Image"
                               )
                           }
                       }

                       binding.backButton.setOnClickListener { dialog.dismiss() }
                   }

                   "Reception Area" -> {
                       val binding = ReceptionAreaLayoutBinding.inflate(layoutInflater)
                       val dialog = AlertDialog.Builder(requireContext())
                           .setView(binding.root)
                           .create()
                       dialog.show()

                       val requestTcRoomDetails = AllRoomDetaisReques(
                           loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                           imeiNo = AppUtil.getAndroidId(requireContext()),
                           appVersion = BuildConfig.VERSION_NAME,
                           tcId = centerId,
                           roomType = room.roomType,
                           roomNo = room.roomNo.toInt(),
                           sanctionOrder = sanctionOrder,
                       )



                       viewLifecycleOwner.lifecycleScope.launch {
                           // Call API
                           viewModel.getAcademicRoomDetails(requestTcRoomDetails)

                           // Wait for 2 seconds
                           delay(2000L)

                           // Hide progress bar

                           // Set data to TextViews (from variables set after API call)
                           binding.yesNoReceptionAreaPhoto.text = safeText(roomsPhotographs)

                           // Open image on click
                           binding.valueReceptionAreaPhoto.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomsPhotographsImage,
                                   "Reception Area Photo"
                               )
                           }
                       }

                       binding.backButton.setOnClickListener { dialog.dismiss() }
                   }

                   "Counselling Room" -> {
                       val binding = CounsellingRoomBinding.inflate(layoutInflater)
                       val dialog = AlertDialog.Builder(requireContext())
                           .setView(binding.root)
                           .create()
                       dialog.show()

                       val requestTcRoomDetails = AllRoomDetaisReques(
                           loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                           imeiNo = AppUtil.getAndroidId(requireContext()),
                           appVersion = BuildConfig.VERSION_NAME,
                           tcId = centerId,
                           roomType = room.roomType,
                           roomNo = room.roomNo.toInt(),
                           sanctionOrder = sanctionOrder,
                       )


                       viewLifecycleOwner.lifecycleScope.launch {
                           // Call API
                           viewModel.getAcademicRoomDetails(requestTcRoomDetails)

                           // Wait for 2 seconds (simulate API loading)
                           delay(2000L)

                           // Hide progress bar

                           // Set data to TextViews
                           binding.yesNoCounsellingAreaPhoto.text = safeText(roomsPhotographs)

                           // Open image on click
                           binding.valueCounsellingAreaPhoto.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomsPhotographsImage,
                                   "Counselling Area Photo"
                               )
                           }
                       }

                       binding.backButton.setOnClickListener { dialog.dismiss() }
                   }

                   "Office Room" -> {
                       val binding = OfficeRoomLayoutBinding.inflate(layoutInflater)
                       val dialog = AlertDialog.Builder(requireContext())
                           .setView(binding.root)
                           .create()
                       dialog.show()

                       val requestTcRoomDetails = AllRoomDetaisReques(
                           loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                           imeiNo = AppUtil.getAndroidId(requireContext()),
                           appVersion = BuildConfig.VERSION_NAME,
                           tcId = centerId,
                           roomType = room.roomType,
                           roomNo = room.roomNo.toInt(),
                           sanctionOrder = sanctionOrder,
                       )

                       // Show progress bar

                       viewLifecycleOwner.lifecycleScope.launch {
                           // Call API
                           viewModel.getAcademicRoomDetails(requestTcRoomDetails)

                           // Wait for 2 seconds to simulate loading
                           delay(2000L)


                           // Set data to TextViews
                           binding.yesNoOfficeRoomPhoto.text = safeText(roomsPhotographs)
                           binding.yesNoRoofType.text = safeText(roofType)
                           binding.yesNoFalseCeiling.text = safeText(falseCeiling)
                           binding.yesNoCeilingHeight.text = safeText(ceilingHeight)
                           binding.yesNoStorage.text = safeText(secureDocumentStorage)
                           binding.yesNoOfficeTable.text = safeText(officeTable)
                           binding.yesNoChairs.text = safeText(officeChair)
                           binding.yesNoComputerTable.text = safeText(officeComputer)
                           binding.yesNoPrinter.text = safeText(printerScanner)
                           binding.yesNoCamera.text = safeText(digitalCamera)
                           binding.yesNoPowerBackup.text = safeText(ecPowerBackup)

                           // Image click listeners
                           binding.valueOfficeRoomPhoto.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomsPhotographsImage,
                                   "Office Room Photo"
                               )
                           }
                           binding.valueRoofType.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roofTypeImage,
                                   "Roof Type Image"
                               )
                           }
                           binding.valueFalseCeiling.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   falseCeilingImage,
                                   "False Ceiling Image"
                               )
                           }
                           binding.valueCeilingHeight.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ceilingHeightImage,
                                   "Ceiling Height Image"
                               )
                           }
                           binding.valueStorage.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   secureDocumentStorageImage,
                                   "Storage Place Image"
                               )
                           }
                           binding.valueOfficeTable.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   officeTableImage,
                                   "Office Table Image"
                               )
                           }
                           binding.valueChairs.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   officeChairImage,
                                   "Chairs Image"
                               )
                           }
                           binding.valueComputerTable.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   officeComputerImagePath,
                                   "Computer Table Image"
                               )
                           }
                           binding.valuePrinter.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   printerScannerImage,
                                   "Printer / Scanner Image"
                               )
                           }
                           binding.valueCamera.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   digitalCameraRoomImage,
                                   "Digital Camera Image"
                               )
                           }
                           binding.valuePowerBackup.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ecPowerBackupImage,
                                   "Power Backup Image"
                               )
                           }
                       }

                       binding.backButton.setOnClickListener { dialog.dismiss() }
                   }

                   "IT cum Domain Lab" -> {
                       val binding = ItCumDomainLabLayoutBinding.inflate(layoutInflater)
                       val dialog = AlertDialog.Builder(requireContext())
                           .setView(binding.root)
                           .create()
                       dialog.show()

                       val requestTcRoomDetails = AllRoomDetaisReques(
                           loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                           imeiNo = AppUtil.getAndroidId(requireContext()),
                           appVersion = BuildConfig.VERSION_NAME,
                           tcId = centerId,
                           roomType = room.roomType,
                           roomNo = room.roomNo.toInt(),
                           sanctionOrder = sanctionOrder,
                       )

                       viewLifecycleOwner.lifecycleScope.launch {
                           viewModel.getAcademicRoomDetails(requestTcRoomDetails)
                           delay(2000L) // simulate API loading

                           // Set yes/no values
                           binding.yesNoTypeOfRoof.text = safeText(roofType)
                           binding.yesNoFalseCeiling.text = safeText(falseCeiling)
                           binding.yesNoHeightCeiling.text = safeText(ceilingHeight)
                           binding.yesNoVentilationArea.text = safeText(ventilationArea)
                           binding.yesNoSoundLevel.text = safeText(soundLevel)
                           binding.yesNoSoundProofAC.text = safeText(centerSoundProof)
                           binding.yesNoInfoBoard.text = safeText(roomInfoBoard)
                           binding.yesNoInternalSignage.text = safeText(internalSignage)
                           binding.yesNoCCTV.text = safeText(audioCamera)
                           binding.yesNoLANComputers.text = safeText(lanEnabled)
                           binding.yesNoInternet.text = safeText(internetConnection)
                           binding.yesNoTypingTutor.text = safeText(typingTuterComp)
                           binding.yesNoTablets.text = safeText(tablet)
                           binding.yesNoTrainerChair.text = safeText(trainerChair)
                           binding.yesNoTrainerTable.text = safeText(trainerTable)
                           binding.yesNoLights.text = safeText(lights)
                           binding.yesNoFans.text = safeText(fans)
                           binding.yesNoPowerBackup.text = safeText(ecPowerBackup)
                           binding.yesNoAirConditioning.text = safeText(airConditionRoom)
                           binding.yesNoLabPhoto.text = safeText(roomsPhotographs)
                           binding.yesNodomainrelatedequipPhoto.text = safeText(domainEquipment)
                           binding.yesNoStools.text = safeText(candidateChair)


                           // Open images on click
                           binding.valueTypeOfRoof.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roofTypeImage,
                                   "Roof Type"
                               )
                           }
                           binding.valueFalseCeiling.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   falseCeilingImage,
                                   "False Ceiling"
                               )
                           }
                           binding.valueHeightCeiling.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ceilingHeightImage,
                                   "Height of Ceiling"
                               )
                           }
                           binding.valueVentilationArea.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ventilationAreaImage,
                                   "Ventilation Area"
                               )
                           }
                           binding.valueSoundLevel.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   soundLevelImage,
                                   "Sound Level"
                               )
                           }
                           binding.valueSoundProofAC.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   centerSoundProofImage,
                                   "Sound Proof AC"
                               )
                           }
                           binding.valueInfoBoard.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomInfoBoardImage,
                                   "Information Board"
                               )
                           }
                           binding.valueInternalSignage.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   internalSignageImage,
                                   "Internal Signage"
                               )
                           }
                           binding.valueCCTV.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   audioCameraImage,
                                   "CCTV Camera"
                               )
                           }
                           binding.valueLANComputers.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   lanEnabledImage,
                                   "LAN Computers"
                               )
                           }
                           binding.valueInternet.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   internetConnectionImage,
                                   "Internet Connection"
                               )
                           }
                           binding.valueTypingTutor.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   typingTuterCompImage,
                                   "Typing Tutor"
                               )
                           }
                           binding.valueTablets.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   tabletImage,
                                   "Tablets"
                               )
                           }
                           binding.valueTrainerChair.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   trainerChairImage,
                                   "Trainer Chair"
                               )
                           }
                           binding.valueTrainerTable.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   trainerTableImage,
                                   "Trainer Table"
                               )
                           }
                           binding.valueLights.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   lightsImage,
                                   "Lights"
                               )
                           }
                           binding.valueFans.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   fansRoomImage,
                                   "Fans"
                               )
                           }
                           binding.valuePowerBackup.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ecPowerBackupImage,
                                   "Power Backup"
                               )
                           }
                           binding.valueAirConditioning.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   airConditionRoomImage,
                                   "Air Conditioning"
                               )
                           }
                           binding.valueITLabPhoto.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomsPhotographsImage,
                                   "IT cum Domain Lab Photo"
                               )
                           }
                           binding.valueStools.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   candidateChairImage,
                                   "Domain Related Equipment"
                               )
                           }

                       }

                       binding.backButton.setOnClickListener { dialog.dismiss() }
                   }

                   "Theory Cum IT Lab" -> {
                       val binding = TheoryCumItLabLayoutBinding.inflate(layoutInflater)
                       val dialog = AlertDialog.Builder(requireContext())
                           .setView(binding.root)
                           .create()

                       val requestTcRoomDetails = AllRoomDetaisReques(
                           loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                           imeiNo = AppUtil.getAndroidId(requireContext()),
                           appVersion = BuildConfig.VERSION_NAME,
                           tcId = centerId,
                           roomType = room.roomType,
                           roomNo = room.roomNo.toInt(),
                           sanctionOrder = sanctionOrder,
                       )

                       viewLifecycleOwner.lifecycleScope.launch {
                           viewModel.getAcademicRoomDetails(requestTcRoomDetails)
                           delay(2000L)


                           binding.yesNoTypeOfRoof.text = safeText(roofType)
                           binding.valueTypeOfRoof.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roofTypeImage,
                                   "Type of Roof"
                               )
                           }

                           binding.yesNoFalseCeiling.text = safeText(falseCeiling)
                           binding.valueFalseCeiling.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   falseCeilingImage,
                                   "False Ceiling"
                               )
                           }

                           binding.yesNoHeightCeiling.text = safeText(ceilingHeight)
                           binding.valueHeightCeiling.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ceilingHeightImage,
                                   "Height of Ceiling"
                               )
                           }

                           binding.yesNoVentilationArea.text = safeText(ventilationArea)
                           binding.valueVentilationArea.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ventilationAreaImage,
                                   "Ventilation Area"
                               )
                           }

                           binding.yesNoSoundLevel.text = safeText(soundLevel)
                           binding.valueSoundLevel.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   soundLevelImage,
                                   "Sound Level"
                               )
                           }

                           binding.yesNoSoundProofAC.text = safeText(centerSoundProof)
                           binding.valueSoundProofAC.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   centerSoundProofImage,
                                   "Sound Proof & AC"
                               )
                           }

                           binding.yesNoInfoBoard.text = safeText(roomInfoBoard)
                           binding.valueInfoBoard.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomInfoBoardImage,
                                   "Room Info Board"
                               )
                           }

                           binding.yesNoInternalSignage.text = safeText(internalSignage)
                           binding.valueInternalSignage.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   internalSignageImage,
                                   "Internal Signage"
                               )
                           }

                           binding.yesNoCCTV.text = safeText(audioCamera)
                           binding.valueCCTV.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   audioCameraImage,
                                   "CCTV Cameras"
                               )
                           }

                           binding.yesNoLANComputers.text = safeText(lanEnabled)
                           binding.valueLANComputers.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   lanEnabledImage,
                                   "LAN Enabled Computers"
                               )
                           }

                           binding.yesNoInternet.text = safeText(internetConnection)
                           binding.valueInternet.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   internetConnectionImage,
                                   "Internet Connection"
                               )
                           }

                           binding.yesNoTypingTutor.text = safeText(typingTuterComp)
                           binding.valueTypingTutor.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   typingTuterCompImage,
                                   "Typing Tutor Computers"
                               )
                           }

                           binding.yesNoTablets.text = safeText(tablet)
                           binding.valueTablets.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   tabletImage,
                                   "Tablets"
                               )
                           }

                           binding.yesNoStools.text = safeText(candidateChair)
                           binding.valueStools.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   candidateChairImage,
                                   "Candidate Chair"
                               )
                           }

                           binding.yesNoTrainerChair.text = safeText(trainerChair)
                           binding.valueTrainerChair.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   trainerChairImage,
                                   "Trainer Chair"
                               )
                           }

                           binding.yesNoTrainerTable.text = safeText(trainerTable)
                           binding.valueTrainerTable.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   trainerTableImage,
                                   "Trainer Table"
                               )
                           }

                           binding.yesNoLights.text = safeText(lights)
                           binding.valueLights.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   lightsImage,
                                   "Lights"
                               )
                           }

                           binding.yesNoFans.text = safeText(fans)
                           binding.valueFans.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   fansRoomImage,
                                   "Fans"
                               )
                           }

                           binding.yesNoPowerBackup.text = safeText(ecPowerBackup)
                           binding.valuePowerBackup.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ecPowerBackupImage,
                                   "Power Backup"
                               )
                           }

                           binding.yesNoLabPhoto.text = safeText(roomsPhotographs)
                           binding.valueITLabPhoto.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomsPhotographsImage,
                                   "IT Lab Photograph"
                               )
                           }

                           binding.yesNodomainrelatedequipPhoto.text = safeText(domainEquipment)
                           binding.valuedomainrelatedequipPhoto.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   domainEquipmentImage,
                                   "Domain Equipment"
                               )
                           }

                           binding.yesNoAirConditioning.text = safeText(airConditionRoom)
                           binding.valueAirConditioning.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   airConditionRoomImage,
                                   "Air Conditioning"
                               )
                           }


                           binding.backButton.setOnClickListener { dialog.dismiss() }
                           dialog.show()
                       }
                   }

                   "Theory Cum Domain Lab" -> {
                       val binding = TheoryCumDomainLabLayoutBinding.inflate(layoutInflater)
                       val dialog = AlertDialog.Builder(requireContext())
                           .setView(binding.root)
                           .create()

                       val requestTcRoomDetails = AllRoomDetaisReques(
                           loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                           imeiNo = AppUtil.getAndroidId(requireContext()),
                           appVersion = BuildConfig.VERSION_NAME,
                           tcId = centerId,
                           roomType = room.roomType,
                           roomNo = room.roomNo.toInt(),
                           sanctionOrder = sanctionOrder,
                       )

                       binding.backButton.setOnClickListener { dialog.dismiss() }
                       viewLifecycleOwner.lifecycleScope.launch {
                           viewModel.getAcademicRoomDetails(requestTcRoomDetails)
                           delay(2000L)
                           // ---------------------- Set all fields ----------------------
                           binding.yesNoTypeOfRoof.text = safeText(roofType)
                           binding.valueTypeOfRoof.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roofTypeImage,
                                   "Type of Roof"
                               )
                           }

                           binding.yesNoFalseCeiling.text = safeText(falseCeiling)
                           binding.valueFalseCeiling.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   falseCeilingImage,
                                   "False Ceiling"
                               )
                           }

                           binding.yesNoHeightCeiling.text = safeText(ceilingHeight)
                           binding.valueHeightCeiling.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ceilingHeightImage,
                                   "Height of Ceiling"
                               )
                           }

                           binding.yesNoVentilationArea.text = safeText(ventilationArea)
                           binding.valueVentilationArea.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ventilationAreaImage,
                                   "Ventilation Area"
                               )
                           }

                           binding.yesNoSoundLevel.text = safeText(soundLevel)
                           binding.valueSoundLevel.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   soundLevelImage,
                                   "Sound Level"
                               )
                           }

                           binding.yesNoSoundProofAC.text = safeText(centerSoundProof)
                           binding.valueSoundProofAC.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   centerSoundProofImage,
                                   "Sound Proof & AC"
                               )
                           }

                           binding.yesNoInfoBoard.text = safeText(roomInfoBoard)
                           binding.valueInfoBoard.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomInfoBoardImage,
                                   "Room Info Board"
                               )
                           }

                           binding.yesNoInternalSignage.text = safeText(internalSignage)
                           binding.valueInternalSignage.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   internalSignageImage,
                                   "Internal Signage"
                               )
                           }

                           binding.yesNoCCTV.text = safeText(audioCamera)
                           binding.valueCCTV.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   audioCameraImage,
                                   "CCTV Cameras"
                               )
                           }

                           binding.yesNoLANComputers.text = safeText(lanEnabled)
                           binding.valueLANComputers.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   lanEnabledImage,
                                   "LAN Enabled Computers"
                               )
                           }

                           binding.yesNoInternet.text = safeText(internetConnection)
                           binding.valueInternet.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   internetConnectionImage,
                                   "Internet Connection"
                               )
                           }

                           binding.yesNoTypingTutor.text = safeText(typingTuterComp)
                           binding.valueTypingTutor.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   typingTuterCompImage,
                                   "Typing Tutor Computers"
                               )
                           }

                           binding.yesNoTablets.text = safeText(tablet)
                           binding.valueTablets.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   tabletImage,
                                   "Tablets"
                               )
                           }

                           binding.yesNoStools.text = safeText(candidateChair)
                           binding.valueStools.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   candidateChairImage,
                                   "Candidate Chair"
                               )
                           }

                           binding.yesNoTrainerChair.text = safeText(trainerChair)
                           binding.valueTrainerChair.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   trainerChairImage,
                                   "Trainer Chair"
                               )
                           }

                           binding.yesNoTrainerTable.text = safeText(trainerTable)
                           binding.valueTrainerTable.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   trainerTableImage,
                                   "Trainer Table"
                               )
                           }

                           binding.yesNoLights.text = safeText(lights)
                           binding.valueLights.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   lightsImage,
                                   "Lights"
                               )
                           }

                           binding.yesNoFans.text = safeText(fans)
                           binding.valueFans.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   fansRoomImage,
                                   "Fans"
                               )
                           }

                           binding.yesNoPowerBackup.text = safeText(ecPowerBackup)
                           binding.valuePowerBackup.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ecPowerBackupImage,
                                   "Power Backup"
                               )
                           }

                           binding.yesNoLabPhoto.text = safeText(roomsPhotographs)
                           binding.valueITLabPhoto.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomsPhotographsImage,
                                   "IT Lab Photograph"
                               )
                           }

                           binding.yesNodomainrelatedequipPhoto.text = safeText(domainEquipment)
                           binding.valuedomainrelatedequipPhoto.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   domainEquipmentImage,
                                   "Domain Equipment"
                               )
                           }

                           binding.yesNoAirConditioning.text = safeText(airConditionRoom)
                           binding.valueAirConditioning.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   airConditionRoomImage,
                                   "Air Conditioning"
                               )
                           }

                           dialog.show()
                       }
                   }

                   "IT Lab" -> {
                       val binding = ItLabLayoutBinding.inflate(layoutInflater)
                       val dialog = AlertDialog.Builder(requireContext())
                           .setView(binding.root)
                           .create()


                       val requestTcRoomDetails = AllRoomDetaisReques(
                           loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                           imeiNo = AppUtil.getAndroidId(requireContext()),
                           appVersion = BuildConfig.VERSION_NAME,
                           tcId = centerId,
                           roomType = room.roomType,
                           roomNo = room.roomNo.toInt(),
                           sanctionOrder = sanctionOrder,
                       )

                       binding.backButton.setOnClickListener { dialog.dismiss() }
                       viewLifecycleOwner.lifecycleScope.launch {
                           viewModel.getAcademicRoomDetails(requestTcRoomDetails)
                           delay(2000L)
                           // Populate fields
                           binding.yesNoTypeOfRoof.text = safeText(roofType)
                           binding.valueTypeOfRoof.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roofTypeImage,
                                   "Type of Roof"
                               )
                           }

                           binding.yesNoFalseCeiling.text = safeText(falseCeiling)
                           binding.valueFalseCeiling.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   falseCeilingImage,
                                   "False Ceiling"
                               )
                           }

                           binding.yesNoHeightCeiling.text = safeText(ceilingHeight)
                           binding.valueHeightCeiling.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ceilingHeightImage,
                                   "Height of Ceiling"
                               )
                           }

                           binding.yesNoVentilationArea.text = safeText(ventilationArea)
                           binding.valueVentilationArea.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ventilationAreaImage,
                                   "Ventilation Area"
                               )
                           }

                           binding.yesNoSoundLevel.text = safeText(soundLevel)
                           binding.valueSoundLevel.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   soundLevelImage,
                                   "Sound Level"
                               )
                           }

                           binding.yesNoSoundProofAC.text = safeText(centerSoundProof)
                           binding.valueSoundProofAC.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   centerSoundProofImage,
                                   "Sound Proof & AC"
                               )
                           }

                           binding.yesNoInfoBoard.text = safeText(roomInfoBoard)
                           binding.valueInfoBoard.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomInfoBoardImage,
                                   "Room Info Board"
                               )
                           }

                           binding.yesNoInternalSignage.text = safeText(internalSignage)
                           binding.valueInternalSignage.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   internalSignageImage,
                                   "Internal Signage"
                               )
                           }

                           binding.yesNoCCTV.text = safeText(audioCamera)
                           binding.valueCCTV.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   audioCameraImage,
                                   "CCTV & Audio"
                               )
                           }

                           binding.yesNoLANComputers.text = safeText(lanEnabled)
                           binding.valueLANComputers.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   lanEnabledImage,
                                   "LAN Enabled Computers"
                               )
                           }

                           binding.yesNoInternet.text = safeText(internetConnection)
                           binding.valueInternet.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   internetConnectionImage,
                                   "Internet Connection"
                               )
                           }

                           binding.yesNoTypingTutor.text = safeText(typingTuterComp)
                           binding.valueTypingTutor.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   typingTuterCompImage,
                                   "Typing Tutor Computers"
                               )
                           }

                           binding.yesNoTablets.text = safeText(tablet)
                           binding.valueTablets.setOnClickListener {
                               showBase64ImageDialog(requireContext(), tabletImage, "Tablets")
                           }

                           binding.yesNoStools.text = safeText(candidateChair)
                           binding.valueStools.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   candidateChairImage,
                                   "Stools / Chairs"
                               )
                           }

                           binding.yesNoTrainerChair.text = safeText(trainerChair)
                           binding.valueTrainerChair.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   trainerChairImage,
                                   "Trainer Chair"
                               )
                           }

                           binding.yesNoTrainerTable.text = safeText(trainerTable)
                           binding.valueTrainerTable.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   trainerTableImage,
                                   "Trainer Table"
                               )
                           }

                           binding.yesNoLights.text = safeText(lights)
                           binding.valueLights.setOnClickListener {
                               showBase64ImageDialog(requireContext(), lightsImage, "Lights")
                           }

                           binding.yesNoFans.text = safeText(fans)
                           binding.valueFans.setOnClickListener {
                               showBase64ImageDialog(requireContext(), fansRoomImage, "Fans")
                           }

                           binding.yesNoPowerBackup.text = safeText(ecPowerBackup)
                           binding.valuePowerBackup.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ecPowerBackupImage,
                                   "Power Backup"
                               )
                           }

                           binding.yesNoLabPhoto.text = safeText(roomsPhotographs)
                           binding.valueITLabPhoto.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomsPhotographsImage,
                                   "IT Lab Photo"
                               )
                           }

                           binding.yesNoAirConditioning.text = safeText(airConditionRoom)
                           binding.valueAirConditioning.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   airConditionRoomImage,
                                   "Air Conditioning"
                               )
                           }

                           binding.backButton.setOnClickListener { dialog.dismiss() }
                           dialog.show()
                       }
                   }


                   "Domain Lab" -> {
                       val binding = DomainLabLayoutBinding.inflate(layoutInflater)
                       val dialog = AlertDialog.Builder(requireContext())
                           .setView(binding.root)
                           .create()

                       val requestTcRoomDetails = AllRoomDetaisReques(
                           loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                           imeiNo = AppUtil.getAndroidId(requireContext()),
                           appVersion = BuildConfig.VERSION_NAME,
                           tcId = centerId,
                           roomType = room.roomType,
                           roomNo = room.roomNo.toInt(),
                           sanctionOrder = sanctionOrder,
                       )

                       binding.backButton.setOnClickListener { dialog.dismiss() }
                       viewLifecycleOwner.lifecycleScope.launch {
                           viewModel.getAcademicRoomDetails(requestTcRoomDetails)
                           delay(2000L)
                           // 1. Type of Roof
                           binding.yesNoTypeOfRoof.text = safeText(roofType)
                           binding.valueTypeOfRoof.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roofTypeImage,
                                   "Type of Roof"
                               )
                           }

                           // 2. False Ceiling
                           binding.yesNoFalseCeiling.text = safeText(falseCeiling)
                           binding.valueFalseCeiling.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   falseCeilingImage,
                                   "False Ceiling"
                               )
                           }

                           // 3. Height of Ceiling
                           binding.yesNoHeightCeiling.text = safeText(ceilingHeight)
                           binding.valueHeightCeiling.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ceilingHeightImage,
                                   "Height of Ceiling"
                               )
                           }

                           // 4. Ventilation Area
                           binding.yesNoVentilationArea.text = safeText(ventilationArea)
                           binding.valueVentilationArea.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ventilationAreaImage,
                                   "Ventilation Area"
                               )
                           }

                           // 5. Sound Level
                           binding.yesNoSoundLevel.text = safeText(soundLevel)
                           binding.valueSoundLevel.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   soundLevelImage,
                                   "Sound Level"
                               )
                           }

                           // 6. Sound Proof & AC
                           binding.yesNoSoundProofAC.text = safeText(centerSoundProof)
                           binding.valueSoundProofAC.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   centerSoundProofImage,
                                   "Sound Proof & AC"
                               )
                           }

                           // 10. Academic Room Info Board
                           binding.yesNoInfoBoard.text = safeText(roomInfoBoard)
                           binding.valueInfoBoard.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomInfoBoardImage,
                                   "Room Info Board"
                               )
                           }

                           // 11. Internal Signage
                           binding.yesNoInternalSignage.text = safeText(internalSignage)
                           binding.valueInternalSignage.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   internalSignageImage,
                                   "Internal Signage"
                               )
                           }

                           // 12. CCTV with Audio
                           binding.yesNoCCTV.text = safeText(audioCamera)
                           binding.valueCCTV.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   audioCameraImage,
                                   "CCTV & Audio"
                               )
                           }

                           // 13. LAN Enabled Computers / LCD Digital Projector
                           binding.yesNoLCDComputers.text = safeText(lanEnabled)
                           binding.valueLCDComputers.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   lanEnabledImage,
                                   "LAN Enabled Computers"
                               )
                           }

                           // 14. Chair for Candidates
                           binding.yesNoChairForCan.text = safeText(candidateChair)
                           binding.valueChairForCan.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   candidateChairImage,
                                   "Chair for Candidates"
                               )
                           }

                           // 15. Writing Board
                           binding.yesNoWritingBoard.text = safeText(writingBoard)
                           binding.valueWritingBoard.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   writingBoardImage,
                                   "Writing Board"
                               )
                           }

                           // 18. Trainer Chair
                           binding.yesNoTrainerChair.text = safeText(trainerChair)
                           binding.valueTrainerChair.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   trainerChairImage,
                                   "Trainer Chair"
                               )
                           }

                           // 19. Trainer Table
                           binding.yesNoTrainerTable.text = safeText(trainerTable)
                           binding.valueTrainerTable.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   trainerTableImage,
                                   "Trainer Table"
                               )
                           }

                           // 20. Lights
                           binding.yesNoLights.text = safeText(lights)
                           binding.valueLights.setOnClickListener {
                               showBase64ImageDialog(requireContext(), lightsImage, "Lights")
                           }

                           // 21. Fans
                           binding.yesNoFans.text = safeText(fans)
                           binding.valueFans.setOnClickListener {
                               showBase64ImageDialog(requireContext(), fansRoomImage, "Fans")
                           }

                           // 22. Electrical Power Backup
                           binding.yesNoPowerBackup.text = safeText(ecPowerBackup)
                           binding.valuePowerBackup.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   ecPowerBackupImage,
                                   "Power Backup"
                               )
                           }

                           // 23. IT Lab Photo
                           binding.yesNoLabPhoto.text = safeText(roomsPhotographs)
                           binding.valueITLabPhoto.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   roomsPhotographsImage,
                                   "IT Lab Photo"
                               )
                           }

                           // Domain Related Equipment Photo
                           binding.yesNodomainrelatedequipPhoto.text = safeText(domainEquipment)
                           binding.valuedomainrelatedequipPhoto.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   domainEquipmentImage,
                                   "Domain Related Equipment"
                               )
                           }

                           // 24. Air Conditioning
                           binding.yesNoAirConditioning.text = safeText(airConditionRoom)
                           binding.valueAirConditioning.setOnClickListener {
                               showBase64ImageDialog(
                                   requireContext(),
                                   airConditionRoomImage,
                                   "Air Conditioning"
                               )
                           }

                           // Back button
                           binding.backButton.setOnClickListener { dialog.dismiss() }

                           dialog.show()
                       }

                   }

                   else -> {
                       Toast.makeText(
                           requireContext(),
                           "No layout found for ${room.roomType}",
                           Toast.LENGTH_SHORT
                       ).show()
                   }
               }


           }}

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
}