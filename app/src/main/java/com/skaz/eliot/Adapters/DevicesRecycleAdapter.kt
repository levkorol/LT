package com.skaz.eliot.Adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.skaz.eliot.Model.Device
import com.skaz.eliot.Model.DeviceAllData
import com.skaz.eliot.Model.DeviceTariff
import com.skaz.eliot.Model.DeviceWater
import com.skaz.eliot.R
import com.skaz.eliot.Services.UserDataService

class DevicesRecycleAdapter(
    val context: Context,
    private val devices: List<Device>
  //  private val deviceWater: List<DeviceWater>
) : RecyclerView.Adapter<DevicesRecycleAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.content_main_devices, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        Log.d("SDASDAD", "${devices.count()}")
        return devices.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindProduct(
            devices[position]
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
        val defaultStartDate = itemView.findViewById<TextView>(R.id.beginDurationLbl)
        val defaultEndDate = itemView.findViewById<TextView>(R.id.endDurationLbl)
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
        val defaultStartDateWater = itemView.findViewById<TextView>(R.id.beginDurationLblWater)
        val defaultEndDateWater = itemView.findViewById<TextView>(R.id.endDurationLblWater)
        val icWater = itemView.findViewById<ImageView>(R.id.deviceImageWater)
        val actLbWater = itemView.findViewById<TextView>(R.id.actLbWater)

        val cur = itemView.findViewById<TextView>(R.id.textView3Water)

        fun bindProduct(
            device: Device
        ) {

            if (device.category == "ee") {
                constraintLayoutElectrical.visibility = View.VISIBLE
                constraintLayoutWater.visibility = View.GONE
                cur.text = ""
            } else if (device.category == "water") {
                constraintLayoutElectrical.visibility = View.GONE
                constraintLayoutWater.visibility = View.VISIBLE

                if (device.device_info.isNotEmpty()) {
                    when {
                        device.device_info[0].type == "hot" -> icWater.setImageResource(R.drawable.red)
                        else -> icWater.setImageResource(R.drawable.drop)
                    }
                    deviceTitleWater.text = device.type
                    deviceIdWater.text = "ID: ${device.id} |"
                    deviceSerialWater.text = " Серийный номер: ${device.device_info[0].serial}"
                    deviceLastActWater.text =
                        "Последняя активность: ${if (device.device_info != null) device.device_info[0].last_act else ""}"
//                    actLbWater.text =
//                        "Показания от: ${ deviceWater.component2().date }" //TODO ne otobrazhaet
                }
                if (device.last_data != null) {
                    cur.text = "${device.last_data.cur} М³"
                }

            }

            deviceTitle.text = device.type
            deviceId.text = "ID: ${device.id} |"
            deviceSerial.text =
                " Серийный номер: ${if (device.device_info != null ) device.device_info[0].serial else ""}"
            deviceLastAct.text =
                "Последняя активность: ${if (device.device_info != null) device.device_info[0].last_act else ""}"
            dayUseLbl.text =
                "${if (device.accumulated_en != null) device.accumulated_en.t1 else ""} кВт*ч"
            nightUseLbl.text =
                "${if (device.accumulated_en != null) device.accumulated_en.t2 else ""} кВт*ч"
            allUseLbl.text =
                "${if (device.accumulated_en != null) device.accumulated_en.t1 + device.accumulated_en.t2 else ""} кВт*ч" //Todo summa klvt
            dateLbl.text = device.last_data?.date
            pw_1.text = device.last_data?.pw_1?.toString()
            pw_2.text = device.last_data?.pw_2?.toString()
            pw_3.text = device.last_data?.pw_3?.toString()
            amper_1.text = device.last_data?.amper_1?.toString() ?: "-"
            amper_2.text = device.last_data?.amper_2?.toString() ?: "-"
            amper_3.text = device.last_data?.amper_3?.toString() ?: "-"
            volt_1.text = device.last_data?.volt_1?.toString() ?: "-"
            volt_2.text = device.last_data?.volt_2?.toString() ?: "-"
            volt_3.text = device.last_data?.volt_3?.toString() ?: "-"
            numPhase_1.text = "1"
            numPhase_2.text = "2"
            numPhase_3.text = "3"
            defaultStartDateWater.text = UserDataService.defaultBeginDate
            defaultEndDateWater.text = UserDataService.defaultEndDate
            defaultStartDate.text = UserDataService.defaultBeginDate
            defaultEndDate.text = UserDataService.defaultEndDate
        }
    }
}