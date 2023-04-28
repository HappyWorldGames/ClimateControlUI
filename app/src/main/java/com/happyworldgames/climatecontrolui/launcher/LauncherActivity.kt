package com.happyworldgames.climatecontrolui.launcher

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recyclerView = RecyclerView(this)
        setContentView(recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(this)
    }

    fun getActivityIcon(context: Context, packageName: String?, activityName: String?): Drawable? {
        val pm = context.packageManager
        val intent = Intent()
        intent.component = ComponentName(packageName!!, activityName!!)
        val resolveInfo = pm.resolveActivity(intent, 0)
        return resolveInfo!!.loadIcon(pm)
    }


    data class AppInfo(val label: CharSequence, val packageName: CharSequence, val icon: Drawable)

    class Views {
        companion object {
            const val ID_IMG = 1
            const val ID_TEXT = 2

            fun getAppView(context: Context): View {
                val linearLayout = LinearLayout(context)
                val imageView = ImageView(context)
                val textView = TextView(context)

                linearLayout.orientation = LinearLayout.HORIZONTAL
                linearLayout.addView(imageView)
                linearLayout.addView(textView)

                imageView.id = ID_IMG
                textView.id = ID_TEXT

                return linearLayout
            }
        }
    }

    class Adapter(context: Context) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        private var appsList: List<AppInfo>

        init {
            val pm: PackageManager = context.getPackageManager()
            appsList = ArrayList<AppInfo>()

            val i = Intent(Intent.ACTION_MAIN, null)
            i.addCategory(Intent.CATEGORY_LAUNCHER)

            val allApps = pm.queryIntentActivities(i, 0)
            for (ri in allApps) {
                val app = AppInfo(
                    label = ri.loadLabel(pm),
                    packageName = ri.activityInfo.packageName,
                    icon = ri.activityInfo.loadIcon(pm)
                )
                (appsList as ArrayList<AppInfo>).add(app)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(Views.getAppView(parent.context))
        }

        override fun getItemCount(): Int = appsList.size

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val appLabel = appsList[position].label.toString()
            val appPackage = appsList[position].packageName.toString()
            val appIcon = appsList[position].icon

            val textView: TextView = viewHolder.textView
            textView.text = appLabel
            val imageView: ImageView = viewHolder.img
            imageView.setImageDrawable(appIcon)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnClickListener {
            val textView = itemView.findViewById<TextView>(Views.ID_TEXT)
            val img = itemView.findViewById<ImageView>(Views.ID_IMG)

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View) {
                val pos = adapterPosition
                val context = v.context

                val launchIntent = context.packageManager.getLaunchIntentForPackage(appsList.get(pos).packageName.toString())
                context.startActivity(launchIntent)
            }
        }
    }
}