package com.catalinj.cryptosmart.presentationlayer.features.widgets.snackbar

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.res.Resources
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.catalinj.cryptosmart.R
import kotlinx.android.synthetic.main.snack_message_layout.view.*

class SnackBarWrapper {

    companion object {

        /**
         *
         */
        fun showSnackBar(view: View,
                         @StringRes infoMessageRes: Int) = showSnackBarInternal(view = view,
                infoMessageRes = infoMessageRes)


        /**
         *
         */
        fun showSnackBarWithHint(view: View,
                                 @StringRes infoMessageRes: Int,
                                 @StringRes hintRes: Int) = showSnackBarInternal(view = view,
                infoMessageRes = infoMessageRes,
                actionMessageRes = hintRes)

        /**
         *
         */
        fun showSnackBarWithAction(view: View,
                                   @StringRes infoMessageRes: Int,
                                   @StringRes actionMessageRes: Int,
                                   clickListener: View.OnClickListener) = showSnackBarInternal(view = view,
                infoMessageRes = infoMessageRes,
                actionMessageRes = actionMessageRes,
                clickListener = clickListener)

        /**
         *
         */
        private fun showSnackBarInternal(view: View,
                                         @StringRes infoMessageRes: Int,
                                         @StringRes actionMessageRes: Int? = null,
                                         clickListener: View.OnClickListener? = null) {

            val snackBar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)
            val snackBarContentLayout = (snackBar.view as Snackbar.SnackbarLayout).getChildAt(0) as LinearLayout
            //wrap click listener to first hide the snackBar, then invoke the user's listener
            val wrappedClickListener = View.OnClickListener {
                snackBar.dismiss()
                clickListener?.onClick(it)
            }
            //hide the views displayed normally by the snackBar
            hideBuildInViews(snackBarContentLayout = snackBarContentLayout)
            //customize the content to look as desired
            customizeSnackBarContainer(snackBar = snackBar, resources = view.context.resources)
            //inflate our custom layout
            val snackBarCustomContent = prepareCustomContent(context = view.context,
                    parentLayout = snackBarContentLayout,
                    infoMessage = infoMessageRes,
                    actionButtonMessage = actionMessageRes,
                    clickListener = wrappedClickListener)
            //add it as the top view to the snackBar
            snackBarContentLayout.addView(snackBarCustomContent, 0)
            //animate the countdown
            startCountDownAnimation(snackBarContent = snackBarCustomContent,
                    context = view.context,
                    animationEndHandler = snackBar::dismiss)
            //finally show the snackBar
            snackBar.show()
        }

        private fun prepareCustomContent(context: Context,
                                         parentLayout: LinearLayout,
                                         @StringRes infoMessage: Int,
                                         @StringRes actionButtonMessage: Int?,
                                         clickListener: View.OnClickListener? = null): View {
            val snackBarCustomContent = LayoutInflater.from(context)
                    .inflate(R.layout.snack_message_layout, parentLayout, false)

            snackBarCustomContent.text_message.setText(infoMessage)
            if (actionButtonMessage != null) {
                snackBarCustomContent.action_button.setText(actionButtonMessage)
                snackBarCustomContent.action_button.setOnClickListener(clickListener)
            } else {
                snackBarCustomContent.action_button.visibility = View.GONE
            }
            return snackBarCustomContent
        }

        private fun startCountDownAnimation(snackBarContent: View,
                                            context: Context,
                                            animationEndHandler: () -> Unit) {
            val countDownAnimation = AnimatorInflater.loadAnimator(context, R.animator.animator_retry_countdown)
            countDownAnimation.setTarget(snackBarContent.countdown_indicator)
            //hide the snackBar after the time elapses
            countDownAnimation.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    animationEndHandler.invoke()
                    countDownAnimation.removeAllListeners()
                }
            })
            //start the animation
            countDownAnimation.start()
        }

        private fun customizeSnackBarContainer(snackBar: Snackbar, resources: Resources) {
            //reset padding
            snackBar.view.setPadding(0, 0, 0, 0)
            //update height
            val currentLayoutParams = snackBar.view.layoutParams
            currentLayoutParams.height = resources.getDimensionPixelSize(R.dimen.snackbar_height)
            snackBar.view.layoutParams = currentLayoutParams
            //perform a layout
            snackBar.view.requestLayout()
        }

        private fun hideBuildInViews(snackBarContentLayout: LinearLayout) {
            //hide default views
            val text = snackBarContentLayout.findViewById(android.support.design.R.id.snackbar_text) as TextView
            text.visibility = View.INVISIBLE
            val action = snackBarContentLayout.findViewById(android.support.design.R.id.snackbar_action) as TextView
            action.visibility = View.INVISIBLE
        }
    }
}