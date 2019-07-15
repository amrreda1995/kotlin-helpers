package com.kotlin.helpers.builders

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu

/**
 * This class helps you to build a menu for a view using builder design pattern
 * */

class PopupMenuBuilder(context: Context, view: View) {

    private var menu: PopupMenu = PopupMenu(context, view)

    fun inflate(menuId: Int): PopupMenuBuilder {
        menu.inflate(menuId)
        return this
    }

    fun setOnClickListener(listener: (MenuItem) -> (Boolean)): PopupMenuBuilder {
        menu.setOnMenuItemClickListener(listener)
        return this
    }

    fun build(): PopupMenu {
        return menu
    }
}