package com.example.covid.questionnaire

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.covid.R
import com.example.covid.data.AgeData
import com.example.covid.data.PersonInfo
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "KotlinActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_next.setOnClickListener {
            if (isDetailFilled()) {
                val key = insertData();
                var intent = Intent(this, QuestionnaireActivity::class.java)
                val rGroup = findViewById(R.id.rg_ans_main) as RadioGroup
                val checkedRadioButton: RadioButton =
                    rGroup.findViewById<View>(rGroup.checkedRadioButtonId) as RadioButton
                intent.putExtra("Covid_affected", checkedRadioButton.text)
                intent.putExtra("UID", key)

                val ageData = AgeData.getInstance()
                ageData.age = Integer.parseInt(et_age!!.text.toString())
                PersonInfo.getInstance().addToDataList(ageData)

                startActivity(intent)
                this.finish()
            }
        }
    }

    fun insertData(): String? {
        val database = Firebase.database.reference.push()
        val patientName = database.child("Patient Name")
        patientName.setValue(et_patientName!!.text.toString())
        val mobileNo = database.child("Mobile No")
        mobileNo.setValue(et_mobileNo!!.text.toString())
        val gender = findViewById<RadioButton>(rg_gender.checkedRadioButtonId).text
        database.child("Gender").setValue(gender)
        database.child("Age").setValue(et_age!!.text.toString())
        database.child("Covid Patient").setValue(findViewById<RadioButton>(rg_ans_main.checkedRadioButtonId).getText())

        PersonInfo.getInstance().name = et_patientName!!.text.toString()
        PersonInfo.getInstance().mobileNumber = et_mobileNo!!.text.toString()
        PersonInfo.getInstance().male = gender == "Male"

        val key = database.key
        return key
    }

    fun isDetailFilled(): Boolean {

        if (et_patientName.text.toString().isBlank()) {
            et_patientName.error = "Please Enter Patient Name"
        } else if (et_mobileNo.text.toString().isBlank() || (et_mobileNo.text.toString().length != 10)) {
            et_mobileNo.error = "Please Enter Valid Mobile Number"
        } else if (rg_gender.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Please select Gender Type", Toast.LENGTH_SHORT).show()
        } else if (et_age.text.toString().isBlank() || !(et_age.text.toString().length in 1..3)) {
            et_age.error = "Please Enter Valid Age"
        } else if (rg_ans_main.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Please select Answer", Toast.LENGTH_SHORT).show()
        } else {
            return true
        }
        return false;
    }
}
