package com.skaz.eliot.Adapters

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.skaz.eliot.Model.Device
import com.skaz.eliot.Model.ElectricIndicationsRequest
import com.skaz.eliot.Model.MyDate
import com.skaz.eliot.Model.WaterIndicationsRequest
import com.skaz.eliot.R
import com.skaz.eliot.Services.AuthService
import com.skaz.eliot.Services.DataService
import com.skaz.eliot.Services.UserDataService

/*ыыы*/
class DevicesRecycleAdapter(
    val context: Context,
    private val devices: List<Device>
    //  private val deviceWater: List<WaterIndicationsResponse>
) : RecyclerView.Adapter<DevicesRecycleAdapter.Holder>() {
    private val deviceWrappers: List<DeviceWrapper> = devices.map { DeviceWrapper(it) }.toList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.content_main_devices, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        Log.d("SDASDAD", "${deviceWrappers.count()}")
        return deviceWrappers.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindProduct(
            deviceWrappers[position]
        )
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //   val productImage = itemView.findViewById<ImageView>(R.id.deviceImage)
        val deviceTitle = itemView.findViewById<TextView>(R.id.deviceLbl)
        val deviceId = itemView.findViewById<TextView>(R.id.idLbl)
        val deviceSerial = itemView.findViewById<TextView>(R.id.serialLbl)
        val deviceLastAct = itemView.findViewById<TextView>(R.id.lastActLbl)
        val dayUseLbl = itemView.findViewById<TextView>(R.id.dayUseLbl)
        val nightUseLbl = itemView.findViewById<TextView>(R.id.nightUseLbl)
        val allUseLbl = itemView.findViewById<TextView>(R.id.allUseLbl)
        val dateLbl = itemView.findViewById<TextView>(R.id.dateTblTxt)
        val pw_1 = itemView.findViewById<TextView>(R.id.mochNumTxt)
        val pw_2 = itemView.findViewById<TextView>(R.id.mochNumTxt2)
        val pw_3 = itemView.findViewById<TextView>(R.id.mochNumTxt3)
        val amper_1 = itemView.findViewById<TextView>(R.id.amperNumTxt)
        val amper_2 = itemView.findViewById<TextView>(R.id.amperNumTxt2)
        val amper_3 = itemView.findViewById<TextView>(R.id.amperNumTxt3)
        val volt_1 = itemView.findViewById<TextView>(R.id.naprNumTxt)
        val volt_2 = itemView.findViewById<TextView>(R.id.naprNumTxt2)
        val volt_3 = itemView.findViewById<TextView>(R.id.naprNumTxt3)
        val numPhase_1 = itemView.findViewById<TextView>(R.id.phaseNumTxt)
        val numPhase_2 = itemView.findViewById<TextView>(R.id.phaseNumTxt2)
        val numPhase_3 = itemView.findViewById<TextView>(R.id.phaseNumTxt3)
        val beginElectricDurationLbl = itemView.findViewById<TextView>(R.id.beginElectricDurationLbl)
        val endElectricDurationLbl = itemView.findViewById<TextView>(R.id.endElectricDurationLbl)
        val dateLabel = itemView.findViewById<TextView>(R.id.showUseLbl2)
        val phaseLabel = itemView.findViewById<TextView>(R.id.showUseLbl3)
        val tokLabel = itemView.findViewById<TextView>(R.id.showUseLbl4)
        val mochLabel = itemView.findViewById<TextView>(R.id.showUseLbl5)
        val naprLabel = itemView.findViewById<TextView>(R.id.showUseLbl6)
        val constraintLayoutElectrical =
            itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutElectrical)
        val constraintLayoutWater =
            itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutWater)

        val deviceTitleWater = itemView.findViewById<TextView>(R.id.deviceLblWater)
        val deviceIdWater = itemView.findViewById<TextView>(R.id.idLblWater)
        val deviceSerialWater = itemView.findViewById<TextView>(R.id.serialLblWater)
        val deviceLastActWater = itemView.findViewById<TextView>(R.id.lastActLblWater)
        val beginWaterDurationLbl = itemView.findViewById<TextView>(R.id.beginWaterDurationLbl)
        val endWaterDurationLbl = itemView.findViewById<TextView>(R.id.endWaterDurationLbl)
        val icWater = itemView.findViewById<ImageView>(R.id.deviceImageWater)
        val actLbWater = itemView.findViewById<TextView>(R.id.actLbWater)
        val resetElectricDurationBtn = itemView.findViewById<Button>(R.id.resetElectricDurationBtn)
        val showElectricDurationBtn = itemView.findViewById<Button>(R.id.showElectricDurationBtn)
        val resetWaterDurationBtn = itemView.findViewById<Button>(R.id.resetWaterDurationBtn)
        val showWaterDurationBtn = itemView.findViewById<Button>(R.id.showWaterDurationBtn)
        val electricDurationUseLbl = itemView.findViewById<TextView>(R.id.electricDurationUseLbl)
        val durationUseLblWater = itemView.findViewById<TextView>(R.id.durationUseLblWater)
        val cur = itemView.findViewById<TextView>(R.id.textView3Water)


        fun showElectricDurationStartBtnClicked(wrapper: DeviceWrapper) {
            val dpd =
                DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    beginElectricDurationLbl.text = UserDataService.dateToStringHuman(year, month, day)
                    wrapper.startDate = MyDate(year, month, day)
                }, wrapper.startDate!!.year, wrapper.startDate!!.month, wrapper.startDate!!.day)
            dpd.show()
        }

        fun showElectricDurationEndBtnClicked(wrapper: DeviceWrapper) {
            val dpd =
                DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    endElectricDurationLbl.text = UserDataService.dateToStringHuman(year, month, day)
                    wrapper.finishDate = MyDate(year, month, day)
                }, wrapper.finishDate!!.year, wrapper.finishDate!!.month, wrapper.finishDate!!.day)
            dpd.show()
        }

        fun showWaterDurationStartBtnClicked(wrapper: DeviceWrapper) {
            val dpd =
                DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    beginWaterDurationLbl.text = UserDataService.dateToStringHuman(year, month, day)
                    wrapper.startDate = MyDate(year, month, day)
                    if (wrapper.finishDate == null) {
                        wrapper.finishDate = UserDataService.defFinishDate
                        endWaterDurationLbl.text = UserDataService.dateToStringHuman(wrapper.finishDate)
                    }
                },
                    wrapper.startDate?.year ?: UserDataService.defStartDate.year,
                    wrapper.startDate?.month ?: UserDataService.defStartDate.month,
                    wrapper.startDate?.day ?: UserDataService.defStartDate.day)
            dpd.show()
        }

        fun showWaterDurationEndBtnClicked(wrapper: DeviceWrapper) {
            val dpd =
                DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    endWaterDurationLbl.text = UserDataService.dateToStringHuman(year, month, day)
                    wrapper.finishDate = MyDate(year, month, day)
                    if (wrapper.startDate == null) {
                        wrapper.startDate = UserDataService.defStartDate
                        beginWaterDurationLbl.text = UserDataService.dateToStringHuman(wrapper.startDate)
                    }
                },
                    wrapper.startDate?.year ?: UserDataService.defFinishDate.year,
                    wrapper.startDate?.month ?: UserDataService.defFinishDate.month,
                    wrapper.startDate?.day ?: UserDataService.defFinishDate.day)
            dpd.show()
        }

        fun electricRequest(wrapper: DeviceWrapper) {
            electricDurationUseLbl.text = "Потребление за все время"
            val request = ElectricIndicationsRequest(
                AuthService.authToken,
                wrapper.device.id.toString(),
                UserDataService.dateToStrinJson(wrapper.startDate),
                UserDataService.dateToStrinJson(wrapper.finishDate)
            )
            DataService.electricIndicationsRequest(request) { response ->
                if (response == null) {

                } else if (response.error != null) {
                    showAlter(response.error)
                } else if (response.notice != null) {
                    showAlter(response.notice)
                } else {
                    dayUseLbl.text = "${response.t1} кВт*ч"
                    nightUseLbl.text = "${response.t2} кВт*ч"
                    allUseLbl.text = "${response.t1 + response.t2} кВт*ч"
                }
            }
        }

        fun waterRequest(wrapper: DeviceWrapper) {
            durationUseLblWater.text = "Потребление за все время"
            val request = WaterIndicationsRequest(
                AuthService.authToken,
                wrapper.device.id.toString(),
                UserDataService.dateToStrinJson(wrapper.startDate),
                UserDataService.dateToStrinJson(wrapper.finishDate)
            )
            DataService.waterIndicationsRequest(request) { response ->
                if (response == null) {

                } else if (response.error != null) {
                    showAlter(response.error)
                } else if (response.notice != null) {
                    showAlter(response.notice)
                } else {
                    cur.text = "${response.value} М³"
                }
            }
        }

        fun bindProduct(
            wrapper: DeviceWrapper
        ) {

            if (wrapper.device.category == "ee") {
                constraintLayoutElectrical.visibility = View.VISIBLE
                constraintLayoutWater.visibility = View.GONE
                cur.text = ""
                showElectricDurationBtn.setOnClickListener {
                    electricRequest(wrapper)
                }
                resetElectricDurationBtn.setOnClickListener {
                    wrapper.startDate = UserDataService.defStartDate
                    wrapper.finishDate = UserDataService.defFinishDate
                    beginElectricDurationLbl.text = UserDataService.dateToStringHuman(wrapper.startDate)
                    endElectricDurationLbl.text = UserDataService.dateToStringHuman(wrapper.finishDate)
                    electricRequest(wrapper)
                }
                beginElectricDurationLbl.setOnClickListener{
                    showElectricDurationStartBtnClicked(wrapper)
                }
                endElectricDurationLbl.setOnClickListener{
                    showElectricDurationEndBtnClicked(wrapper)
                }
            } else if (wrapper.device.category == "water") {
                constraintLayoutElectrical.visibility = View.GONE
                constraintLayoutWater.visibility = View.VISIBLE

                if (wrapper.device.device_info.isNotEmpty()) {
                    when {
                        wrapper.device.device_info[0].type == "hot" -> icWater.setImageResource(R.drawable.red)
                        else -> icWater.setImageResource(R.drawable.drop)
                    }
                    deviceTitleWater.text = wrapper.device.type
                    deviceIdWater.text = "ID: ${wrapper.device.id} |"
                    deviceSerialWater.text = " Серийный номер: ${wrapper.device.device_info[0].serial}"
                    deviceLastActWater.text =
                        "Последняя активность: ${if (wrapper.device.device_info != null) wrapper.device.device_info[0].last_act else ""}"
                    actLbWater.text =
                        "Показания от: ${wrapper.device.last_data?.date ?: ""}" //TODO ne otobrazhaet
                }
                if (wrapper.device.last_data != null) {
                    cur.text = "${wrapper.device.last_data?.cur} М³"
                }

                showWaterDurationBtn.setOnClickListener {
                    waterRequest(wrapper)
                }
                resetWaterDurationBtn.setOnClickListener{
                    wrapper.startDate = null
                    wrapper.finishDate = null
                    beginWaterDurationLbl.text = UserDataService.dateToStringHuman(wrapper.startDate ?: UserDataService.defStartDate)
                    endWaterDurationLbl.text = UserDataService.dateToStringHuman(wrapper.finishDate ?: UserDataService.defFinishDate)
                    waterRequest(wrapper)
                }

                beginWaterDurationLbl.setOnClickListener{
                    showWaterDurationStartBtnClicked(wrapper)
                }
                endWaterDurationLbl.setOnClickListener{
                    showWaterDurationEndBtnClicked(wrapper)
                }

            }

            deviceTitle.text = wrapper.device.type
            deviceId.text = "ID: ${wrapper.device.id} |"
            deviceSerial.text =
                " Серийный номер: ${if (wrapper.device.device_info != null) wrapper.device.device_info[0].serial else ""}"
            deviceLastAct.text =
                "Последняя активность: ${if (wrapper.device.device_info != null) wrapper.device.device_info[0].last_act else ""}"
            dayUseLbl.text =
                "${if (wrapper.device.accumulated_en != null) wrapper.device.accumulated_en!!.t1 else ""} кВт*ч"
            nightUseLbl.text =
                "${if (wrapper.device.accumulated_en != null) wrapper.device.accumulated_en!!.t2 else ""} кВт*ч"
            allUseLbl.text =
                "${if (wrapper.device.accumulated_en != null) wrapper.device.accumulated_en!!.t1 + wrapper.device.accumulated_en!!.t2 else ""} кВт*ч" //Todo summa klvt
            dateLbl.text = wrapper.device.last_data?.date
            pw_1.text = wrapper.device.last_data?.pw_1?.toString()
            pw_2.text = wrapper.device.last_data?.pw_2?.toString()
            pw_3.text = wrapper.device.last_data?.pw_3?.toString()
            amper_1.text = wrapper.device.last_data?.amper_1?.toString() ?: "-"
            amper_2.text = wrapper.device.last_data?.amper_2?.toString() ?: "-"
            amper_3.text = wrapper.device.last_data?.amper_3?.toString() ?: "-"
            volt_1.text = wrapper.device.last_data?.volt_1?.toString() ?: "-"
            volt_2.text = wrapper.device.last_data?.volt_2?.toString() ?: "-"
            volt_3.text = wrapper.device.last_data?.volt_3?.toString() ?: "-"
            numPhase_1.text = "1"
            numPhase_2.text = "2"
            numPhase_3.text = "3"
            beginWaterDurationLbl.text = UserDataService.dateToStringHuman(UserDataService.defStartDate)
            endWaterDurationLbl.text = UserDataService.dateToStringHuman(UserDataService.defFinishDate)
            beginElectricDurationLbl.text = UserDataService.dateToStringHuman(UserDataService.defStartDate)
            endElectricDurationLbl.text = UserDataService.dateToStringHuman(UserDataService.defFinishDate)
        }
    }

    private fun showAlter(text: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Ошибка")
        builder.setMessage(text)
        builder.setPositiveButton("Хорошо") { _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}