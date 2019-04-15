package com.noble.activity.justmovie.ui.features.about

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.noble.activity.justmovie.R
import com.noble.activity.justmovie.ui.utils.ViewUtils
import com.vansuita.materialabout.builder.AboutBuilder
import kotlinx.android.synthetic.main.activity_container.*

class AboutActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        val builder = AboutBuilder.with(this)
            .setAppIcon(R.mipmap.ic_launcher)
            .setAppName(R.string.app_name)
            .setPhoto(R.mipmap.profile_picture)
            .setCover(R.mipmap.profile_cover)
            .setLinksAnimated(true)
            .setDividerDashGap(13)
            .setName("Your Full Name")
            .setSubTitle("Mobile Developer")
            .setLinksColumnsCount(4)
            .setBrief("I'm warmed of mobile technologies. Ideas maker, curious and nature lover.")
            .addGooglePlayStoreLink("8002078663318221363")
            .addGitHubLink("jrvansuita")
            .addBitbucketLink("jrvansuita")
            .addFacebookLink("user")
            .addTwitterLink("user")
            .addInstagramLink("jnrvans")
            .addGooglePlusLink("+JuniorVansuita")
            .addYoutubeChannelLink("CaseyNeistat")
            .addDribbbleLink("user")
            .addLinkedInLink("arleu-cezar-vansuita-júnior-83769271")
            .addEmailLink("vansuita.jr@gmail.com")
            .addWhatsappLink("Jr", "+554799650629")
            .addSkypeLink("user")
            .addGoogleLink("user")
            .addAndroidLink("user")
            .addWebsiteLink("site")
            .addFiveStarsAction()
            .addMoreFromMeAction("Vansuita")
            .setVersionNameAsAppSubTitle()
            .addShareAction(R.string.app_name)
            .addUpdateAction()
            .setActionsColumnsCount(2)
            .addFeedbackAction("vansuita.jr@gmail.com")
            .addPrivacyPolicyAction("http://www.docracy.com/2d0kis6uc2")
            .addIntroduceAction(null as Intent?)
            .addHelpAction(null as Intent?)
            .addChangeLogAction(null as Intent?)
            .addRemoveAdsAction(null as Intent?)
            .addDonateAction(null as Intent?)
            .setWrapScrollView(true)
            .setShowAsCard(true)

        val view = builder.build()

        container.addView(view)

        setSupportActionBar(toolbar)
        toolbar.navigationIcon = ViewUtils.getIcon(this, R.drawable.ic_arrow_back, R.color.iconInactiveColor)
        toolbar.setNavigationOnClickListener { finish() }
        supportActionBar?.setTitle(R.string.about)

        appBarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent30))

    }
}