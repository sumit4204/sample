package com.example.covid.questionnaire

/**
 * Created by  on 4/1/2020.
 */
data class QuestionnaireModelClass(var question:String = "",var ansList:ArrayList<AnswersModelClass>,var isCheckBox:Boolean = false  ) {
    data class AnswersModelClass(var answer:String = "",var rating:Int = 0,var isAnsSelected:Boolean = false)
}