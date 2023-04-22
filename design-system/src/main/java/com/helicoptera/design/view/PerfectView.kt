package com.helicoptera.design.view

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.helicoptera.design.R
import com.helicoptera.design.ext.toPx


class PerfectView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private var childContainer: LinearLayout? = null

    private var isHorizontal = false

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.PerfectView, defStyle, 0)

        val isClosable = typedArray.getBoolean(R.styleable.PerfectView_isClosable, false)

        isHorizontal = typedArray.getInt(R.styleable.PerfectView_orientation, 0) == 1

        val inflater: LayoutInflater = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val layout = inflater.inflate(R.layout.perfect_view, this@PerfectView, true) as LinearLayout

        val verticalChildContainer = layout.findViewById(R.id.vertical_child_container) as LinearLayout
        val horizontalChildContainer = layout.findViewById(R.id.horizontal_child_container) as LinearLayout
        val horizontalContainer = layout.findViewById(R.id.horizontal_container) as View
        if (isHorizontal) {
            childContainer = horizontalChildContainer
            verticalChildContainer.isVisible = false
        } else {
            childContainer = verticalChildContainer
            horizontalContainer.isVisible = false
        }

        val background = layout.findViewById(R.id.content) as LinearLayout

        //region cross
        val crossContainer = layout.findViewById(R.id.cross_container) as View
        crossContainer.isVisible = isClosable
        //endregion

        //region text
        val headerText = typedArray.getString(R.styleable.PerfectView_headerText)
        val subHeaderText = typedArray.getString(R.styleable.PerfectView_subHeaderText)
        val headerView = layout.findViewById(R.id.header) as TextView
        val subHeaderView = layout.findViewById(R.id.sub_header) as TextView

        headerView.setText(headerText)

        if (!subHeaderText.isNullOrBlank()) {
            subHeaderView.isVisible = true
            subHeaderView.setText(subHeaderText)
        } else {
            subHeaderView.isVisible = false
        }
        //endregion

        //region icon

        val iconPosition = if (typedArray.getInt(R.styleable.PerfectView_iconPosition, 0) == 1 || isClosable) {
            IconPosition.LEFT
        } else {
            IconPosition.RIGHT
        }

        val iconDrawable =
            typedArray.getDrawable(R.styleable.PerfectView_iconSrc) ?: context.getDrawable(R.drawable.default_icon)

        val icon = layout.findViewById(R.id.icon) as ImageView
        icon.setImageDrawable(iconDrawable)
        if (iconPosition == IconPosition.LEFT) {
            val iconTextContainer = layout.findViewById(R.id.icon_text_container) as LinearLayout
            iconTextContainer.removeView(icon)
            iconTextContainer.addView(icon, 0)
        }

        //endregion

        //region mainButton
        val buttonText = typedArray.getString(R.styleable.PerfectView_buttonText)
        val mainButton = layout.findViewById(R.id.main_button) as Button

        if (!buttonText.isNullOrBlank()) {
            mainButton.isVisible = true
            mainButton.setText(buttonText)
        } else {
            mainButton.isVisible = false
        }

        //end region

        //region additionalButton
        val additionalButtonText = typedArray.getString(R.styleable.PerfectView_additionalButtonText)
        val additionalButton = layout.findViewById(R.id.additional_button) as TextView

        if (!additionalButtonText.isNullOrBlank()) {
            subHeaderView.isVisible = false
            icon.isVisible = false
            crossContainer.isVisible = false
            additionalButton.isVisible = true
            additionalButton.setText(additionalButtonText)
        } else {
            additionalButton.isVisible = false
        }

        //region light
        val isLight = typedArray.getBoolean(R.styleable.PerfectView_isLight, true) || isClosable
        if (isLight) {
            background.background = context.getDrawable(R.drawable.light_bg)
            background.elevation = 8F
            subHeaderView.setTextColor(Color.parseColor("#9299A2"))
        } else {
            background.background = context.getDrawable(R.drawable.dark_bg)
            subHeaderView.setTextColor(Color.parseColor("#333333"))
        }
        //endregion

        //end region

        typedArray.recycle()
    }

    override fun addView(child: View?) {
        if (child is PerfectView) {
            childContainer?.addView(child)
        } else if (child != null) {
            super.addView(child)
        }
    }

    private fun processChild(perfectView: PerfectView) {
        if (isHorizontal) {
            processHorizontalChild(perfectView)
            requestLayout()
        } else {
            processVerticalChild(perfectView)
        }
    }

    private fun processHorizontalChild(perfectView: PerfectView) {
        val content = perfectView.findViewById<LinearLayout>(R.id.content)
        content.setPadding(10, 10, 12, 0)
        content.elevation = 0F
        val measure = 140.toPx(context)
        val padding = 12.toPx(context)
        content.layoutParams = LayoutParams(measure, measure)
        content.requestLayout()

        val iconTextContainer = perfectView.findViewById<LinearLayout>(R.id.icon_text_container)
        iconTextContainer.orientation = VERTICAL
        iconTextContainer.setPadding(padding, padding, padding, padding)

        val textContainer = perfectView.findViewById<LinearLayout>(R.id.text_container)
        textContainer.layoutParams = LayoutParams(measure, ViewGroup.LayoutParams.WRAP_CONTENT)

        //to reset margins
        val icon = perfectView.findViewById<ImageView>(R.id.icon)
        val iconLayoutParams = icon.layoutParams
        icon.layoutParams = LayoutParams(iconLayoutParams.width, iconLayoutParams.height)
    }

    private fun processVerticalChild(perfectView: PerfectView) {
        val content = perfectView.findViewById<LinearLayout>(R.id.content)
        val padding = 10.toPx(context)
        content.setPadding(padding, padding, 0, 0)
        content.background = null
        content.elevation = 0F
    }

    override fun addView(child: View?, index: Int) {
        if (child is PerfectView) {
            processChild(child)
            childContainer?.addView(child, index)
        } else if (child != null) {
            super.addView(child, index)
        }
    }

    override fun addView(child: View?, width: Int, height: Int) {
        if (child is PerfectView) {
            processChild(child)
            childContainer?.addView(child, width, height)
        } else if (child != null) {
            super.addView(child, width, height)
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (child is PerfectView) {
            processChild(child)
            childContainer?.addView(child, params)
        } else if (child != null) {
            super.addView(child, params)
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child is PerfectView) {
            processChild(child)
            childContainer?.addView(child, index, params)
        } else if (child != null) {
            super.addView(child, index, params)
        }
    }
}