package com.effectiveapps.app.ui.bottomnav

import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.effectiveapps.app.R


class BottomNavigationFragment : Fragment() {

    private lateinit var dot: View
    private val lambdas: SparseArray<(()->Unit)?> = SparseArray(4)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val createdView = inflater.inflate(R.layout.bottom_navigation_menu, container, false)
        dot = createdView.findViewById(R.id.bottom_nav_dot)
        return createdView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
    }

    private fun initClickListeners() {
        arrayOf(Explorer, Bag, Favorite, Profile).forEach { category: Int->
            view?.findViewById<View>(category)?.setOnClickListener {
                changeCategory(category)
                lambdas[category]?.invoke()
            }
        }
    }

    private fun changeCategory(@IdRes category: Int) {
        dot.updateLayoutParams<ConstraintLayout.LayoutParams> {
            endToStart = category
            topToTop = category
            bottomToBottom = category
        }
    }

    fun addCallback(@IdRes category: Int, onClick: ()->Unit) {
        lambdas[category] = onClick
    }

    companion object {
        @IdRes const val Explorer = R.id.bottom_nav_explorer
        @IdRes const val Bag = R.id.bottom_nav_bag
        @IdRes const val Favorite = R.id.bottom_nav_favorite
        @IdRes const val Profile = R.id.bottom_nav_profile


    }
}