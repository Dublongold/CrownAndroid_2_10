package com.playourcrown.casi.noonli.negames.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FragmentViewBuilder {
    private var resourceId: Int? = null
    private var container: ViewGroup? = null
    private var containerMethodInvoked = false
    private var layoutInflater: LayoutInflater? = null
    private var attachToRoot: Boolean? = null

    fun inflater(layoutInflater: LayoutInflater): FragmentViewBuilder {
        this.layoutInflater = layoutInflater
        return this
    }

    fun container(container: ViewGroup?): FragmentViewBuilder {
        this.container = container
        containerMethodInvoked = true
        return this
    }

    fun resourceId(resourceId: Int): FragmentViewBuilder {
        this.resourceId = resourceId
        return this
    }

    fun attachToRoot(attachToRoot: Boolean): FragmentViewBuilder {
        this.attachToRoot = attachToRoot
        return this
    }

    fun build(): View? = when {
            layoutInflater == null -> throw IllegalStateException("You must set layoutInflater.")
            resourceId == null -> throw IllegalStateException("You must set resourceId.")
            !containerMethodInvoked -> throw IllegalStateException("You must set container " +
                    "(even if it's null)")
            attachToRoot == null -> throw IllegalStateException("You must set attachToRoot.")
            else -> layoutInflater?.inflate(resourceId!!, container, attachToRoot!!)
    }
}