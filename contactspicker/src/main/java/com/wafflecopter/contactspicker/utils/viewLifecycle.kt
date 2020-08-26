package com.wafflecopter.contactspicker.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T> Fragment.viewLifecycleLazy(initialise: () -> T): ReadOnlyProperty<Fragment, T> =  object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {

    // A backing property to hold our value
    private var binding: T? = null

    private var viewLifecycleOwner: LifecycleOwner? = null

    init {
        // Observe the View Lifecycle of the Fragment
        this@viewLifecycleLazy
            .viewLifecycleOwnerLiveData
            .observe(
                this@viewLifecycleLazy, { newLifecycleOwner ->
                    viewLifecycleOwner
                        ?.lifecycle
                        ?.removeObserver(this)

                    viewLifecycleOwner = newLifecycleOwner.also {
                        it.lifecycle.addObserver(this)
                    }
                }
            )
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        binding = null
    }

    override fun getValue(
        thisRef: Fragment,
        property: KProperty<*>
    ): T {
        // Return the backing property if it's set, or initialise
        return this.binding ?: initialise().also {
            this.binding = it
        }
    }
}
