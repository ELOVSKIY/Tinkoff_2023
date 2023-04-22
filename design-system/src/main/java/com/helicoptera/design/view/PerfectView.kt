package com.helicoptera.design.view

import android.R.layout
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.helicoptera.design.R


class PerfectView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {


    private var iconDrawable: Drawable? = null

//    private var isClosable: Boolean = false

//    private var headerView: TextView? = null
//    private var subHeaderView: TextView? = null

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.PerfectView, defStyle, 0)

        val isClosable = typedArray.getBoolean(R.styleable.PerfectView_isClosable, false)

        val inflater: LayoutInflater = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val layout = inflater.inflate(R.layout.perfect_view, this@PerfectView, true) as LinearLayout

        val background = layout.findViewById(R.id.content) as LinearLayout
        //region background
            val isLight = typedArray.getBoolean(R.styleable.PerfectView_isLight, true) || isClosable
            if (isLight) {
                background.background = context.getDrawable(R.drawable.light_bg)
                background.elevation = 8F
            } else {
                background.background = context.getDrawable(R.drawable.dark_bg)
            }
        //endregion

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
        val mainButton =layout.findViewById(R.id.main_button) as Button

        if (!buttonText.isNullOrBlank()) {
            mainButton.isVisible = true
            mainButton.setText(buttonText)
        } else {
            mainButton.isVisible = false
        }

        //end region

        typedArray.recycle()
    }

    companion object {

    }
}