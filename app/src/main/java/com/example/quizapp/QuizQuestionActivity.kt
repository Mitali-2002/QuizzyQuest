package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class QuizQuestionActivity : AppCompatActivity() , View.OnClickListener{

    private var mCurrentPosition:Int = 1
    private var mQuestionsList:ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUsername: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)

        mUsername = intent.getStringExtra(Constants.USER_NAME)

        mQuestionsList = Constants.getQuestions()

        setQuestion()

        val tv_option_one = findViewById<TextView>(R.id.tv_option_one)
        val tv_option_two = findViewById<TextView>(R.id.tv_option_two)
        val tv_option_three = findViewById<TextView>(R.id.tv_option_three)
        val tv_option_four = findViewById<TextView>(R.id.tv_option_four)
        val btn_submit = findViewById<Button>(R.id.btn_submit)

        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
        btn_submit.setOnClickListener(this)

    }

    private fun setQuestion(){

        val question = mQuestionsList!![mCurrentPosition-1]

        defaultOptionsView()

        if (mCurrentPosition == mQuestionsList!!.size){
            val btn_submit = findViewById<Button>(R.id.btn_submit)
            btn_submit.text = "FINISH"
        }else{
            val btn_submit = findViewById<Button>(R.id.btn_submit)
            btn_submit.text= "SUBMIT"
        }

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tv_progress = findViewById<TextView>(R.id.tv_progress)
        val tv_question = findViewById<TextView>(R.id.tv_question)
        val tv_option_one = findViewById<TextView>(R.id.tv_option_one)
        val tv_option_two = findViewById<TextView>(R.id.tv_option_two)
        val tv_option_three = findViewById<TextView>(R.id.tv_option_three)
        val tv_option_four = findViewById<TextView>(R.id.tv_option_four)
        val tv_image = findViewById<ImageView>(R.id.tv_image)
        progressBar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition" + "/" + progressBar.max

        tv_question.text = question!!.question
        tv_image.setImageResource(question.image)
        tv_option_one.text= question.optionOne
        tv_option_two.text= question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour
    }

    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        val tv_option_one = findViewById<TextView>(R.id.tv_option_one)
        val tv_option_two = findViewById<TextView>(R.id.tv_option_two)
        val tv_option_three = findViewById<TextView>(R.id.tv_option_three)
        val tv_option_four = findViewById<TextView>(R.id.tv_option_four)
        options.add(0,tv_option_one)
        options.add(1,tv_option_two)
        options.add(2,tv_option_three)
        options.add(3,tv_option_four)

        for(option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this,R.drawable.default_option_border_bg)
        }
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.tv_option_one->{
                val tv_option_one = findViewById<TextView>(R.id.tv_option_one)
                selectedOptionView(tv_option_one,1)
            }
            R.id.tv_option_two->{
                val tv_option_two = findViewById<TextView>(R.id.tv_option_two)
                selectedOptionView(tv_option_two,2)
            }
            R.id.tv_option_three->{
                val tv_option_three = findViewById<TextView>(R.id.tv_option_three)
                selectedOptionView(tv_option_three,3)
            }
            R.id.tv_option_four->{
                val tv_option_four = findViewById<TextView>(R.id.tv_option_four)
                selectedOptionView(tv_option_four,4)
            }
            R.id.btn_submit->{
                if (mSelectedOptionPosition == 0){
                    mCurrentPosition++

                    when{
                        mCurrentPosition <= mQuestionsList!!.size ->{
                            setQuestion()
                        }else ->{
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME,mUsername)
                            intent.putExtra(Constants.CORRECT_ANSWER,mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS,mQuestionsList!!.size)
                            startActivity(intent)
                        }
                    }
                }else{
                    val question = mQuestionsList?.get(mCurrentPosition-1)
                    if(question!!.correctAnswer != mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }else{
                        mCorrectAnswers++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if(mCurrentPosition == mQuestionsList!!.size){
                        val btn_submit = findViewById<Button>(R.id.btn_submit)
                        btn_submit.text = "FINISH"
                    }else{
                        val btn_submit = findViewById<Button>(R.id.btn_submit)
                        btn_submit.text = "Go TO THE NEXT QUESTION"
                    }
                    mSelectedOptionPosition = 0
                }
            }
        }

    }

    private fun answerView(answer:Int, drawableView: Int){
        when(answer){
            1 ->{
                val tv_option_one = findViewById<TextView>(R.id.tv_option_one)
                tv_option_one.background = ContextCompat.getDrawable(this,drawableView)
            }
            2 ->{
                val tv_option_two = findViewById<TextView>(R.id.tv_option_two)
                tv_option_two.background = ContextCompat.getDrawable(this,drawableView)
            }
            3 ->{
                val tv_option_three = findViewById<TextView>(R.id.tv_option_three)
                tv_option_three.background = ContextCompat.getDrawable(this,drawableView)
            }
            4 ->{
                val tv_option_four = findViewById<TextView>(R.id.tv_option_four)
                tv_option_four.background = ContextCompat.getDrawable(this,drawableView)
            }
        }
    }

    private fun selectedOptionView(tv:TextView,selectedOptionNum:Int){
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this,R.drawable.selected_option_border_bg)
    }
}