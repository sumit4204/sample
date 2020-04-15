package com.example.covid.questionnaire

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_question_ans.view.*


/**
 * Created by  on 4/1/2020.
 */
class QuestionAdapter(var datalist: ArrayList<QuestionnaireModelClass>) :
    RecyclerView.Adapter<QuestionAdapter.MyViewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                com.example.covid.R.layout.layout_question_ans,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    lateinit var lastCheckedRadioGroup: RadioGroup
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val questionModel = datalist[position]
        holder.itemView.tv_questions.text = questionModel.question
        var id = (position + 1) * 100
        for (price in questionModel.ansList) {
            if (!questionModel.isCheckBox) {
                holder.itemView.rg_adapter_ans.visibility = View.VISIBLE
                holder.itemView.ll_checkbox_container.visibility = View.GONE
                val rb = RadioButton(context)
                rb.textSize = 12f;
                rb.setTextColor(Color.rgb(105, 105, 105 ))
                rb.text = price.answer
                holder.itemView.rg_adapter_ans.addView(rb)
            } else {
                holder.itemView.rg_adapter_ans.visibility = View.GONE
                holder.itemView.ll_checkbox_container.visibility = View.VISIBLE
                val cb = CheckBox(context)
                cb.text = price.answer;
                cb.textSize = 12f;
                cb.setTextColor(Color.rgb(105, 105, 105 ))
                holder.itemView.ll_checkbox_container.addView(cb)
                cb.setOnCheckedChangeListener { compoundButton, b ->
                    questionModel.ansList.forEach {
                        if (it.answer==compoundButton.text) {
                            it.isAnsSelected = !it.isAnsSelected
                        }
                    }
                }
            }
        }
        holder.itemView.rg_adapter_ans.setOnCheckedChangeListener { radioGroup, i ->
            val checkedRadioButton: RadioButton =
                radioGroup.findViewById<View>(radioGroup.checkedRadioButtonId) as RadioButton
            Log.d("selection", position.toString())
            questionModel.ansList.forEach {
                it.isAnsSelected = it.answer==checkedRadioButton.text
            }
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}