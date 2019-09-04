package com.skaz.eliot.Adapters

import android.content.Context
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
import org.w3c.dom.Text

class DevicesRecycleAdapter(val context: Context, val devices: List<Device>, val devicesInfo: List<DeviceInfo>, val deviceTariffs: List<DeviceTariff>, val  deviceAllData: List<DeviceAllData>) : RecyclerView.Adapter<DevicesRecycleAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.content_main_devices, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        Log.d("SDASDAD", "${deviceAllData.count()}")
        return devices.count()
    }

    override fun onBindViewHolder(holder: DevicesRecycleAdapter.Holder, position: Int) {
        holder.bindProduct(context, devices[position], devicesInfo[position], deviceTariffs[position], deviceAllData[position])
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

        fun bindProduct(
            context: Context,
            device: Device,
            deviceInfo: DeviceInfo,
            deviceTariff: DeviceTariff,
            deviceAllData: DeviceAllData
        ) {
           // val resourceId = context.resources.getIdentifier(product.image, "drawable", context.packageName)
           // productImage.setImageResource(resourceId)

            deviceTitle.text = device.type
            deviceId.text = "ID: ${device.type_id} |"
            deviceSerial.text = "  Серийный номер: ${deviceInfo.serial}"
            deviceLastAct.text = "Последняя активность: ${deviceInfo.last_act}"
            dayUseLbl.text = "${deviceTariff.t1} кВт*ч"
            nightUseLbl.text = "${deviceTariff.t2} кВт*ч"
            allUseLbl.text = "${deviceTariff.t3} кВт*ч"
            dateLbl.text = deviceAllData.deviceDate
            pw_1.text = deviceAllData.pw_1
            pw_2.text = deviceAllData.pw_2
            pw_3.text = deviceAllData.pw_3
            amper_1.text = deviceAllData.amper_1
            amper_2.text = deviceAllData.amper_2
            amper_3.text = deviceAllData.amper_3
            volt_1.text = deviceAllData.volt_1
            volt_2.text = deviceAllData.volt_2
            volt_3.text = deviceAllData.volt_3
            numPhase_1.text = "1"
            numPhase_2.text = "2"
            numPhase_3.text = "3"
            defaultStartDate.text = UserDataService.defaultBeginDate
            defaultEndDate.text = UserDataService.defaultEndDate



        }


    }

}