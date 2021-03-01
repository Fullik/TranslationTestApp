package com.example.translation.presentation.view.favorites

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.translation.R
import com.example.translation.di.ComponentHolder
import com.example.translation.domain.model.FavoriteTranslationModel
import com.example.translation.presentation.presenter.favorites.FavoritesPresenter
import com.example.translation.presentation.showToast
import com.example.translation.presentation.view.common.BackButtonListener
import com.example.translation.presentation.view.common.SwipeToDeleteCallback
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

class FavoritesFragment : MvpAppCompatFragment(R.layout.fragment_favorites),
    FavoritesView,
    BackButtonListener {

    @Inject
    lateinit var presenterProvider: Provider<FavoritesPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }
    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        ComponentHolder.INSTANCE.getFavoritesFragmentComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler(view)
    }

    private fun setupRecycler(view: View) {
        adapter = FavoritesAdapter()
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                presenter.removeFavoriteTranslation(
                    (viewHolder as FavoritesViewHolder).model,
                    position
                )
                adapter.remoteItemAt(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        view.findViewById<RecyclerView>(R.id.recycler_view).run {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FavoritesFragment.adapter
            itemTouchHelper.attachToRecyclerView(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ComponentHolder.INSTANCE.clearFavoritesFragmentComponent()
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackPressed()
        return true
    }

    override fun showFavoriteTranslationsFetchError() {
        showToast(R.string.favorite_fetch_error)
    }

    override fun showFavorites(items: List<FavoriteTranslationModel>) {
        adapter.setItems(items)
    }

    override fun restoreItem(model: FavoriteTranslationModel, position: Int) {
        showToast(R.string.favorite_delete_error)
        adapter.addItem(model, position)
    }

    override fun showItemDeletedMessage() {
        showToast(R.string.favorite_word_deleted)
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}