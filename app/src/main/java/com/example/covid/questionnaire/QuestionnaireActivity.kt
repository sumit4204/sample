package com.example.covid.questionnaire

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covid.R
import com.example.covid.data.PersonInfo
import com.example.covid.data.TempData
import com.example.covid.hrmeasure.HRMeasureActivity
//import com.example.covid.hrmeasure.HRMeasureActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.questionnaire_activity.*


class QuestionnaireActivity : AppCompatActivity() {

    lateinit var mQuestionList:ArrayList<QuestionnaireModelClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.questionnaire_activity)
        var isCovidAffected = intent.getStringExtra("Covid_affected")

        if(isCovidAffected.contains("NO",true)){
            init()
        }else{
            tv_temp_questionnaire.visibility = View.VISIBLE
            rv_questionnier.visibility = View.INVISIBLE
        }
        bt_next_questionnaire.setOnClickListener {
            val key = intent.getStringExtra("UID")
            val database = Firebase.database.getReference(key)
            if(false) {
                for (item in mQuestionList) {
                    if (item.isCheckBox) {
                        database.child(item.question).setValue("Yes")
                    } else {
                        database.child(item.question).setValue("No")
                    }
                }
                val i = mQuestionList.size - 1
                val textView = rv_questionnier.getChildAt(i).findViewById<TextInputEditText>(
                    R.id.et_temp
                )
                if (textView != null) {
                    val text = textView.text.toString()
                    database.child(mQuestionList[i].question).setValue(text)

                    if (text != null && !text.isEmpty()) {
                        val tempData = TempData.getInstance()
                        tempData.temp = text.toFloat()
                        PersonInfo.getInstance().addToDataList(tempData)
                    }
                }
            }
            if(tv_temp_questionnaire.getVisibility() == View.VISIBLE){
                database.child("Present Body Temperature").setValue(et_tempe!!.text.toString())

                val tempData = TempData.getInstance()
                tempData.temp = et_tempe!!.text.toString().toFloat()
                PersonInfo.getInstance().addToDataList(tempData)
            }

            val hrIntent = Intent(this, HRMeasureActivity::class.java)
            startActivity(hrIntent)
        }

    }

    fun init(){
        initialiseListValue()
        val questionAdapter =
            QuestionAdapter(mQuestionList)
        rv_questionnier.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = questionAdapter
        }

    }
    fun initialiseListValue(){
        mQuestionList = ArrayList()
        val mAnsList_1 = ArrayList<QuestionnaireModelClass.AnswersModelClass>()
        mAnsList_1.add( QuestionnaireModelClass.AnswersModelClass("Less than 12 years",1))
        mAnsList_1.add(QuestionnaireModelClass.AnswersModelClass("12-50 years",5))
        mAnsList_1.add(QuestionnaireModelClass.AnswersModelClass("Above 60 years",10))
        mQuestionList.add(
            QuestionnaireModelClass(
                "How old are you?",mAnsList_1)
        )

        val mAnsList_2 = ArrayList<QuestionnaireModelClass.AnswersModelClass>()
        mAnsList_2.add( QuestionnaireModelClass.AnswersModelClass("Dry cough",5))
        mAnsList_2.add(QuestionnaireModelClass.AnswersModelClass("Loss of Diminished sense of smell",10))
        mAnsList_2.add(QuestionnaireModelClass.AnswersModelClass("Sore throat",5))
        mAnsList_2.add(QuestionnaireModelClass.AnswersModelClass("Weakness",6))
        mAnsList_2.add(QuestionnaireModelClass.AnswersModelClass("Change in Appetite",5))
        mQuestionList.add(
            QuestionnaireModelClass(
                "Are you experiencing any of the symptoms below (mark all those applicable)",
                mAnsList_2,true
            )
        )

        val mAnsList_3 = ArrayList<QuestionnaireModelClass.AnswersModelClass>()
        mAnsList_3.add( QuestionnaireModelClass.AnswersModelClass("<36",8))
        mAnsList_3.add(QuestionnaireModelClass.AnswersModelClass("In between 36 and 37",1))
        mAnsList_3.add(QuestionnaireModelClass.AnswersModelClass("in between 37 and 39",5))
        mAnsList_3.add(QuestionnaireModelClass.AnswersModelClass(">39",8))
        mQuestionList.add(
            QuestionnaireModelClass(
                "Please let us know your corrent body Temperature in degree Fahrenheit (Normal Body Temp is 98.6)"
                ,mAnsList_3)
        )
        val mAnsList_4 = ArrayList<QuestionnaireModelClass.AnswersModelClass>()
        mAnsList_4.add( QuestionnaireModelClass.AnswersModelClass("Moderate to severe cough",4))
        mAnsList_4.add(QuestionnaireModelClass.AnswersModelClass("Feeling Breathless",8))
        mAnsList_4.add(QuestionnaireModelClass.AnswersModelClass("Difficulty in Breathing",8))
        mAnsList_4.add(QuestionnaireModelClass.AnswersModelClass("Drowsiness",8))
        mAnsList_4.add(QuestionnaireModelClass.AnswersModelClass("Persistent pain and pressure in chest",8))
        mAnsList_4.add(QuestionnaireModelClass.AnswersModelClass("Severe Weakness",8))
        mAnsList_4.add(QuestionnaireModelClass.AnswersModelClass("Loss of taste and smell",10))
        mQuestionList.add(
            QuestionnaireModelClass(
                "Additionally, please verify if you are experiencing any of the symptoms below (mark all those applicable)"
                ,mAnsList_4,true)
        )

        val mAnsList_5 = ArrayList<QuestionnaireModelClass.AnswersModelClass>()
        mAnsList_5.add( QuestionnaireModelClass.AnswersModelClass("Yes",1))
        mAnsList_5.add(QuestionnaireModelClass.AnswersModelClass("No",5))
        QuestionnaireModelClass(
            "Chronic Disease?"
            ,mAnsList_5)

        val mAnsList_6 = ArrayList<QuestionnaireModelClass.AnswersModelClass>()
        mAnsList_6.add( QuestionnaireModelClass.AnswersModelClass("Diabetes",5))
        mAnsList_6.add(QuestionnaireModelClass.AnswersModelClass("High Blood Pressure",5))
        mAnsList_6.add(QuestionnaireModelClass.AnswersModelClass("Heart Disease",5))
        mAnsList_6.add(QuestionnaireModelClass.AnswersModelClass("Kidney Disease",5))
        mAnsList_6.add(QuestionnaireModelClass.AnswersModelClass("Lung Disease",8))
        mAnsList_6.add(QuestionnaireModelClass.AnswersModelClass("Stroke",5))
        mAnsList_6.add(QuestionnaireModelClass.AnswersModelClass("Reduced Immunity",8))
        mAnsList_6.add(QuestionnaireModelClass.AnswersModelClass("No"))
        mQuestionList.add(
            QuestionnaireModelClass(
                "Do you have history of any of these conditions (Mark all those applicable)"
                ,mAnsList_6,true)
        )
        val mAnsList_7 = ArrayList<QuestionnaireModelClass.AnswersModelClass>()
        mAnsList_7.add( QuestionnaireModelClass.AnswersModelClass("Yes",1))
        mAnsList_7.add(QuestionnaireModelClass.AnswersModelClass("No",5))
        mQuestionList.add(
            QuestionnaireModelClass(
                "Have you or someone in your family travelled within India in public transport and come in close contact with someone with cough, cold, fever and shortness of breath in last 14 days?"
                ,mAnsList_7)
        )
        val mAnsList_8 = ArrayList<QuestionnaireModelClass.AnswersModelClass>()
        mAnsList_8.add( QuestionnaireModelClass.AnswersModelClass("No Travel history",1))
        mAnsList_8.add(QuestionnaireModelClass.AnswersModelClass("No Contact with anyone with symptoms",1))
        mAnsList_8.add(QuestionnaireModelClass.AnswersModelClass("History of travel or meeting in affected geographical area in last 14 days",9))
        mAnsList_8.add(QuestionnaireModelClass.AnswersModelClass("Close contact with Confirmed COVID in last 14 days",10))
        mQuestionList.add(
            QuestionnaireModelClass(
                "Please select you Travel and exposure details."
                ,mAnsList_8,true)
        )
        val mAnsList_9 = ArrayList<QuestionnaireModelClass.AnswersModelClass>()
        mAnsList_9 .add( QuestionnaireModelClass.AnswersModelClass("Improved",1))
        mAnsList_9 .add(QuestionnaireModelClass.AnswersModelClass("No Changes",5))
        mAnsList_9 .add(QuestionnaireModelClass.AnswersModelClass("Worsen",7))
        mAnsList_9 .add(QuestionnaireModelClass.AnswersModelClass("Worsen Considerably",9))
        mQuestionList.add(
            QuestionnaireModelClass(
                "How have your symptoms progressed over the last 48hrs?"
                ,mAnsList_9)
        )
        val mAnsList_10 = ArrayList<QuestionnaireModelClass.AnswersModelClass>()
        mAnsList_10.add( QuestionnaireModelClass.AnswersModelClass(">=94%",1))
        mAnsList_10.add(QuestionnaireModelClass.AnswersModelClass("<94%",5))
        mQuestionList.add(
            QuestionnaireModelClass(
                "SpO2"
                ,mAnsList_10)
        )
        val mAnsList_11 = ArrayList<QuestionnaireModelClass.AnswersModelClass>()
        mAnsList_11.add( QuestionnaireModelClass.AnswersModelClass("<120",1))
        mAnsList_11.add(QuestionnaireModelClass.AnswersModelClass(">=120",5))
        mQuestionList.add(
            QuestionnaireModelClass(
                "Heart rate"
                ,mAnsList_11)
        )
        val mAnsList_12 = ArrayList<QuestionnaireModelClass.AnswersModelClass>()
        mAnsList_12.add( QuestionnaireModelClass.AnswersModelClass("<20",1))
        mAnsList_12.add(QuestionnaireModelClass.AnswersModelClass("In between 20 and 30",5))
        mAnsList_12.add(QuestionnaireModelClass.AnswersModelClass(">30",8))
        mQuestionList.add(
            QuestionnaireModelClass(
                "Respiration rate"
                ,mAnsList_12)
        )
    }

}