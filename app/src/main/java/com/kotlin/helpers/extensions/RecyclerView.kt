package com.kotlin.helpers.extensions

import androidx.recyclerview.widget.RecyclerView

/**
 * If you don't use RecyclerViewBuilder library; and you need to implement a pagination for your
 * recyclerView; of course you will need to trigger if your recyclerView reaches to its bottom or not
 * This extension will help you to implement this task
 * */

fun RecyclerView.addScrollListenerForPagination(
    orientation: Int? = null,
    reverseLayout: Boolean? = null,
    block: () -> Unit
) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            // 1 = down; -1 = up; 0 = up or down
            var direction = 1

            reverseLayout?.let {
                if (it) {
                    direction = -1
                }
            }

            orientation?.let {
                when (it) {
                    RecyclerView.VERTICAL -> {
                        if (!recyclerView.canScrollVertically(direction)) {
                            block()
                        }
                    }

                    RecyclerView.HORIZONTAL -> {
                        if (!recyclerView.canScrollHorizontally(direction)) {
                            block()
                        }
                    }
                }
            } ?: run {
                if (!recyclerView.canScrollVertically(direction)) {
                    block()
                }
            }
        }
    })
}