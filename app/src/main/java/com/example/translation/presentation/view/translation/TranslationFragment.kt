package com.example.translation.presentation.view.translation

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.translation.R
import com.example.translation.di.ComponentHolder
import com.example.translation.domain.model.AvailableLanguagesModel
import com.example.translation.domain.model.RecentTranslationModel
import com.example.translation.presentation.presenter.translation.TranslationPresenter
import com.example.translation.presentation.showToast
import com.example.translation.presentation.view.common.BackButtonListener
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

class TranslationFragment : MvpAppCompatFragment(R.layout.fragment_translation),
    TranslationView,
    BackButtonListener {

    @Inject
    lateinit var presenterProvider: Provider<TranslationPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var translateInput: EditText
    private lateinit var searchInput: EditText
    private lateinit var adapter: TranslationAdapter
    private var selectedSpinnerItemFrom = 0
    private var selectedSpinnerItemTo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        ComponentHolder.INSTANCE.getTranslationScreenComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedSpinnerItemFrom = savedInstanceState?.getInt(STATE_SELECTED_SPINNER_FROM) ?: 0
        selectedSpinnerItemTo = savedInstanceState?.getInt(STATE_SELECTED_SPINNER_TO) ?: 0
        translateInput = view.findViewById(R.id.translation_input)
        searchInput = view.findViewById(R.id.search_input)
        setupRecycler(view)
        setupSpinners(view)
        setupButtonsClick(view)
    }

    private fun setupButtonsClick(view: View) {
        view.findViewById<Button>(R.id.translate_button).setOnClickListener {
            val translateFrom = spinnerFrom.selectedItem as AvailableLanguagesModel
            val translateTo = spinnerTo.selectedItem as AvailableLanguagesModel
            presenter.translate(translateInput.text.toString(), translateFrom, translateTo)
        }
        view.findViewById<Button>(R.id.search_button).setOnClickListener {
            presenter.search(searchInput.text.toString())
        }
    }

    private fun setupRecycler(view: View) {
        adapter = TranslationAdapter()
        view.findViewById<RecyclerView>(R.id.recent_translation_recycler).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@TranslationFragment.adapter
        }
    }

    private fun setupSpinners(view: View) {
        spinnerFrom = view.findViewById<Spinner>(R.id.translate_from_spinner).apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedSpinnerItemFrom = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
        spinnerTo = view.findViewById<Spinner>(R.id.translate_to_spinner).apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedSpinnerItemTo = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_SELECTED_SPINNER_FROM, selectedSpinnerItemFrom)
        outState.putInt(STATE_SELECTED_SPINNER_TO, selectedSpinnerItemTo)
    }

    override fun onDestroy() {
        super.onDestroy()
        ComponentHolder.INSTANCE.clearTranslationScreenComponent()
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackPressed()
        return true
    }

    override fun showTranslationError() {
        showToast(R.string.translation_error)
    }

    override fun showWordEmptyError() {
        showToast(R.string.word_empty_error)
    }

    override fun showSearchError() {
        showToast(R.string.search_error)
    }

    override fun setupSpinners(availableLanguages: List<AvailableLanguagesModel>) {
        spinnerFrom.setupAdapter(availableLanguages)
        spinnerTo.setupAdapter(availableLanguages)
        spinnerFrom.setSelection(selectedSpinnerItemFrom)
        spinnerTo.setSelection(selectedSpinnerItemTo)
    }

    override fun showRecentTranslations(items: List<RecentTranslationModel>) {
        adapter.submitList(items)
    }

    private fun Spinner.setupAdapter(availableLanguages: List<AvailableLanguagesModel>) {
        val createdAdapter = object : ArrayAdapter<AvailableLanguagesModel>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            availableLanguages.toTypedArray()
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                return setTextFromItem(view, position)
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                return setTextFromItem(view, position)
            }

            fun setTextFromItem(view: View, position: Int): View {
                val item = getItem(position)
                return item?.let {
                    (view as TextView).apply {
                        text = it.name
                    }
                } ?: view
            }
        }
        createdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter = createdAdapter
    }

    companion object {
        private const val STATE_SELECTED_SPINNER_FROM = "STATE_SELECTED_SPINNER_FROM"
        private const val STATE_SELECTED_SPINNER_TO = "STATE_SELECTED_SPINNER_TO"

        fun newInstance() = TranslationFragment()
    }
}