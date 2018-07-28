package com.catalinj.cryptosmart.presentationlayer.features.settings.view


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.catalinj.cryptosmart.R
import com.catalinj.cryptosmart.datalayer.CurrencyRepresentation
import com.catalinj.cryptosmart.di.components.ActivityComponent
import com.catalinj.cryptosmart.di.components.SettingsComponent
import com.catalinj.cryptosmart.di.modules.settings.SettingsModule
import com.catalinj.cryptosmart.presentationlayer.MainActivity
import com.catalinj.cryptosmart.presentationlayer.features.settings.contract.SettingsContract
import com.catalinjurjiu.wheelbarrow.WheelbarrowFragment
import kotlinx.android.synthetic.main.layout_fragment_settings.view.*
import javax.inject.Inject

/**
 * A simple Settings Fragment, which allows changing between dark & light themes, and allows
 * changing the primary currency
 */
class SettingsFragment : WheelbarrowFragment<SettingsComponent>(), SettingsContract.SettingsView {
    override val name: String = TAG

    @Inject
    protected lateinit var settingsPresenter: SettingsContract.SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cargo.inject(settingsFragment = this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showBottomNavigation()
        settingsPresenter.viewAvailable(this)
    }

    override fun initialise() {
        val view = view!!
        val switch = view.switch_enable_dark_theme
        switch.isChecked = settingsPresenter.isDarkThemeEnabled()
        switch.setOnCheckedChangeListener { _, isChecked ->
            settingsPresenter.darkThemeSettingChanged(isChecked)
        }

        val currencyRepresentationSpinner = view.currency_representation_spinner
        val currenciesList = settingsPresenter.gerCurrenciesList()
        val selectedItem = settingsPresenter.getCurrentPrimaryCurrency()
        val selectedItemPosition = currenciesList.indexOf(selectedItem)

        val adapter = ArrayAdapter<CurrencyRepresentation>(context,
                R.layout.layout_simple_spinner_list_item, currenciesList)
        currencyRepresentationSpinner.adapter = adapter
        currencyRepresentationSpinner.setSelection(selectedItemPosition)

        currencyRepresentationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Cata", "Spinner on nothing selected.")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                settingsPresenter.primaryCurrencyChanged(currenciesList[position])
            }
        }
    }

    override fun onStart() {
        super.onStart()
        settingsPresenter.startPresenting()
    }

    override fun onStop() {
        super.onStop()
        settingsPresenter.stopPresenting()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        settingsPresenter.viewDestroyed()
    }

    override fun getPresenter(): SettingsContract.SettingsPresenter {
        return settingsPresenter
    }

    override fun applyNewTheme() {
        (activity as MainActivity).restart()
    }

    class SettingsFragmentFactory(private val activityComponent: ActivityComponent) :
            WheelbarrowFragment.Factory<SettingsComponent>() {
        override fun onCreateFragment(): WheelbarrowFragment<SettingsComponent> {
            return SettingsFragment()
        }

        override fun onCreateCargo(): SettingsComponent {
            return activityComponent.getSettingsComponent(settingsModule = SettingsModule())
        }
    }

    companion object {
        const val TAG = "SettingsFragment"
    }
}
