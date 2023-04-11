package com.zref.experiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.zref.experiment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.anu)

            binding.anu.animateChilds {
                it.clear(binding.textTop.id, ConstraintSet.TOP)
                it.clear(binding.textBottom.id, ConstraintSet.TOP)
                it.connect(binding.textBottom.id, ConstraintSet.BOTTOM, binding.anu.id, ConstraintSet.BOTTOM)
                it.connect(binding.textTop.id, ConstraintSet.BOTTOM, binding.textBottom.id, ConstraintSet.TOP)
            }
        }
    }

    fun ConstraintLayout.animateChilds(
        iDuration: Long = 500L,
        iOrdering: Int = TransitionSet.ORDERING_TOGETHER,
        cSet: (cSet: ConstraintSet) -> Unit
    ) {
        val mCset = ConstraintSet().apply { clone(this@animateChilds) }
        cSet(mCset)
        animateChilds(iDuration, iOrdering, mCset)
    }

    fun ConstraintLayout.animateChilds(
        iDuration: Long = 500L,
        iOrdering: Int = TransitionSet.ORDERING_TOGETHER,
        cSet: ConstraintSet
    ) {
        TransitionManager.beginDelayedTransition(this,
            TransitionSet().apply {
                duration = iDuration
                ordering = iOrdering
                addTransition(ChangeBounds().setInterpolator(DecelerateInterpolator()))
                addTransition(Fade(Fade.OUT))
                addTransition(Fade(Fade.IN))
            }
        )
        cSet.applyTo(this)
    }
}