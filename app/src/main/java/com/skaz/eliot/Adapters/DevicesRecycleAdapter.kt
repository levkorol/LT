package com.skaz.eliot.Adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.skaz.eliot.Model.Device
import com.skaz.eliot.Model.DeviceAllData
import com.skaz.eliot.Model.DeviceInfo
import com.skaz.eliot.Model.DeviceTariff
import com.skaz.eliot.R
import com.skaz.eliot.Services.UserDataService

class DevicesRecycleAdapter(
    val context: Context,
    val devices: List<Device>,
    val devicesInfo: List<DeviceInfo>,
    val deviceTariffs: List<DeviceTariff>,
    val deviceAllData: List<DeviceAllData>
) : RecyclerView.Adapter<DevicesRecycleAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.content_main_devices, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        Log.d("SDASDAD", "${deviceAllData.count()}")
        return devices.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindProduct(
            context,
            devices[position],
            devicesInfo,
            deviceTariffs,
            deviceAllData,
            position
        )
        //     holder.bindProduct2(context, devicesInfo[position]

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
        val constraintLayoutElectrical = itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutElectrical)
        val constraintLayoutWater = itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutWater)

        fun bindProduct(
            context: Context,
            device: Device,
            deviceInfo: List<DeviceInfo>,
            deviceTariff: List<DeviceTariff>,
            deviceAllData: List<DeviceAllData>,
            position: Int
        ) {
            // val resourceId = context.resources.getIdentifier(product.image, "drawable", context.packageName)
            // productImage.setImageResource(resourceId)
            if (device.category == "ee") {
                constraintLayoutElectrical.visibility = View.VISIBLE
                constraintLayoutWater.visibility = View.GONE
            } else if (device.category == "water") {
                constraintLayoutElectrical.visibility = View.GONE
                constraintLayoutWater.visibility = View.VISIBLE
            }
            deviceTitle.text = device.type
            deviceId.text = "ID: ${device.id} |"
            deviceSerial.text =
                "  Серийный номер: ${if (deviceInfo.size > position) deviceInfo[position].serial else ""}"
            deviceLastAct.text =
                "Последняя активность: ${if (deviceInfo.size > position) deviceInfo[position].last_act else ""}"
            dayUseLbl.text =
                "${if (deviceTariff.size > position) deviceTariff[position].t1 else ""} кВт*ч"
            nightUseLbl.text =
                "${if (deviceTariff.size > position) deviceTariff[position].t2 else ""} кВт*ч"
//            allUseLbl.text =
//                "${if (deviceTariff.size > position) deviceTariff[position].t3 else ""} кВт*ч" //Todo summa klvt
            if (deviceAllData.size > position) {
                dateLbl.text = deviceAllData[position].deviceDate
                pw_1.text = deviceAllData[position].pw_1.toString()
                pw_2.text = deviceAllData[position].pw_2.toString()
                pw_3.text = deviceAllData[position].pw_3.toString()
                amper_1.text = deviceAllData[position].amper_1.toString()
                amper_2.text = deviceAllData[position].amper_2.toString()
                amper_3.text = deviceAllData[position].amper_3.toString()
                volt_1.text = deviceAllData[position].volt_1.toString()
                volt_2.text = deviceAllData[position].volt_2.toString()
                volt_3.text = deviceAllData[position].volt_3.toString()
                numPhase_1.text = "1"
                numPhase_2.text = "2"
                numPhase_3.text = "3"
            } else {
                dateLbl.text = ""
                pw_1.text = ""
                pw_2.text = ""
                pw_3.text = ""
                amper_1.text = ""
                amper_2.text = ""
                amper_3.text = ""
                volt_1.text = ""
                volt_2.text = ""
                volt_3.text = ""
                numPhase_1.text = ""
                numPhase_2.text = ""
                numPhase_3.text = ""
                dateLabel.text = ""
                phaseLabel.text = ""
                tokLabel.text = ""
                mochLabel.text = ""
                naprLabel.text = ""
            }

            defaultStartDate.text = UserDataService.defaultBeginDate
            defaultEndDate.text = UserDataService.defaultEndDate
        }
    }
}