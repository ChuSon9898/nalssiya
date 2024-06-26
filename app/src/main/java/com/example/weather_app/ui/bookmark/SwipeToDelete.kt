package com.example.weather_app.ui.bookmark

import android.graphics.Canvas
import android.opengl.Visibility
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDelete(private val adapter: BookmarkListAdapter, private val context : ViewModelStoreOwner): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

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

    //스와이프 동작
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        val pos = viewHolder.adapterPosition
        val item = adapter.getItem(pos)

        //Room 데이터 삭제
        bookmarkViewModel.deleteData(item.id, item.Dong, item.nx, item.ny, item.landArea, item.tempArea)
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