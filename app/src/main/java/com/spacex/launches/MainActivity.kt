package com.spacex.launches

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.spacex.launches.ui.launches.details.LaunchesDetailsFragment
import com.spacex.launches.ui.launches.preview.LaunchesPreviewFragment

class MainActivity : AppCompatActivity(), NavigationProvider {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.commitNow {
                replace(R.id.container, LaunchesPreviewFragment.newInstance())
            }
        }
    }

    override fun openLaunchDetails(launchId: String) {
        supportFragmentManager.commit {
            val fragment = LaunchesDetailsFragment.newInstance(launchId)
            replace(R.id.container, fragment)
            addToBackStack("launch_details")
        }
    }
}
