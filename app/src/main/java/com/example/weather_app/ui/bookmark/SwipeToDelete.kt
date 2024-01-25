package com.example.weather_app.ui.bookmark

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import okhttp3.internal.notifyAll

class SwipeToDelete(var adapter: BookmarkListAdapter, val context : ViewModelStoreOwner): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val bookmarkViewModel by lazy {
        ViewModelProvider(context).get(BookmarkViewModel::class.java)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        var pos = viewHolder.adapterPosition
        val item = adapter.getItem(pos)
        bookmarkViewModel.deleteData(item.id, item.location, item.nx, item.ny, item.landArea, item.tempArea)

        val view = (viewHolder as BookmarkListAdapter.ViewHolder).swipeLayout
        view.animate().translationX(0f).setDuration(300).start()
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val view = (viewHolder as BookmarkListAdapter.ViewHolder).swipeLayout

            view.translationX = dX

            getDefaultUIUtil().onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive)
        }
    }

}