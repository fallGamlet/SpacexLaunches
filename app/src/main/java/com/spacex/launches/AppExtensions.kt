package com.spacex.launches

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment


fun Fragment.getRepositoryProvider(): RepositoryProvider {
    return context?.applicationContext as? RepositoryProvider
        ?: throw RuntimeException("Application class must implement RepositoryProvider interface")
}

fun Fragment.getNavigationProvider(): NavigationProvider {
    var fragment: Fragment? = this
    var provider: NavigationProvider? = null

    while (fragment != null && provider == null) {
        provider = fragment as? NavigationProvider
        fragment = fragment.parentFragment
    }

    return provider
        ?: activity as? NavigationProvider
        ?: throw RuntimeException("Fragment or Parent Fragment or Activity must implement NavigationProvider interface")
}

fun Context.openUrl(url: String) {
    try {
        if (url.isBlank()) return

        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "Not found application for open link $url", Toast.LENGTH_SHORT)
            .show()
    }
}
