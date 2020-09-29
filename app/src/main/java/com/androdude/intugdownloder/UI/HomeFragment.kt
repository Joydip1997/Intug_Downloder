package com.androdude.intugdownloder.UI

import android.content.*
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.androdude.intugdownloder.R
import com.androdude.intugdownloder.UI.Utils.Utils
import com.androdude.intugdownloder.ViewModel.PostViewModel
import com.androdude.intugdownloder.db.model.PostRespose
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.image_view_layout.view.*
import kotlinx.android.synthetic.main.image_view_layout.view.close_dialog
import kotlinx.android.synthetic.main.video_view_layout.view.*
import retrofit2.Response


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var PostRespose: LiveData<Response<PostRespose>>
    private lateinit var postViewModel: PostViewModel


    //Permissions
    private val permissions = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().toolbar_title.text = "Intug Downloader"

        postViewModel = ViewModelProviders.of(requireActivity()).get(PostViewModel::class.java)

        view.paste_url.setOnClickListener {
            pasteCopiedUrl(view)
        }

        view.open_instagram.setOnClickListener {
            openInstagram()
        }



        view.open_url.setOnClickListener {
            if (isNetworkConnected()) {
                val postUrl = url_edittext.text.toString()
                if (checkPermissions()) {
                    if (Utils.checkLink(postUrl) && !postUrl.isEmpty()) {
                        view.main_screen_progress_bar.visibility = View.VISIBLE
                        view.home_screen_view.visibility = View.GONE
                        try {
                            PostRespose = postViewModel.getPosts(Utils.getThePostId(postUrl))
                            PostRespose.observe(requireActivity(), Observer {
                                val respose = it.body()
                                view.main_screen_progress_bar.visibility = View.GONE
                                view.home_screen_view.visibility = View.VISIBLE
                                if (respose != null) {
                                    if (respose.graphql.shortcode_media.is_video) {
                                        getPostVideoView(respose)
                                    } else {
                                        getPostImageView(respose)
                                    }
                                }
                                else
                                {
                                    Snackbar.make(view, "Error: Please Try Again", Snackbar.LENGTH_SHORT)
                                        .show()
                                }

                            })
                        } catch (e: Exception) {
                            view.main_screen_progress_bar.visibility = View.GONE
                            view.home_screen_view.visibility = View.VISIBLE
                            Snackbar.make(view, "Error: Please Try Again", Snackbar.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } else {
                Snackbar.make(view, "Error: No Internet Connection", Snackbar.LENGTH_SHORT).show()
            }


        }


    }

    //Paste the copied text
    private fun pasteCopiedUrl(view: View) {
        val clipboard =
            activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var pasteData = ""
        if (!clipboard.hasPrimaryClip()) {
        } else if (!clipboard.getPrimaryClipDescription()!!.hasMimeType(MIMETYPE_TEXT_PLAIN)
        ) {

        } else {
            val item: ClipData.Item = clipboard.getPrimaryClip()!!.getItemAt(0)
            pasteData = item.text.toString()
            url_edittext.setText(pasteData)
        }

    }

    //Checking Internet Connection
    private fun isNetworkConnected(): Boolean {
        val cm =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    private fun getPostVideoView(respose: PostRespose) {
        val mAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        mAlertDialogBuilder.setCancelable(false)
        val mView = layoutInflater.inflate(R.layout.video_view_layout, null, false)
        mView.post_video.setVideoPath(respose.graphql.shortcode_media.video_url)
        Glide.with(requireActivity()).load(respose.graphql.shortcode_media.owner.profile_pic_url)
            .into(mView.video_user_profile_image)
        mView.video_user_name.text = respose.graphql.shortcode_media.owner.username
        mView.video_likes_count.text =
            respose.graphql.shortcode_media.edge_media_preview_like.count.toString()
        mView.video_comments_count.text =
            respose.graphql.shortcode_media.edge_media_preview_comment.count.toString()
        val mAlertDialog = mAlertDialogBuilder.create()
        var is_Playing = false
        mView.post_video.setOnClickListener {
            if (is_Playing) {
                mView.post_video.stopPlayback()
                is_Playing = false
            } else {
                mView.post_video.start()
                is_Playing = true
            }
        }
        mView.close_dialog.setOnClickListener {
            mAlertDialog.dismiss()
        }
        mView.video_download_bttn.setOnClickListener {

            Utils.downloadFile(
                requireActivity(),
                respose.graphql.shortcode_media.video_url,
                "Intug",
                respose.graphql.shortcode_media.is_video
            )
        }
        mAlertDialog.setView(mView)
        mAlertDialog.show()
    }


    private fun getPostImageView(respose: PostRespose?) {
        val mAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        mAlertDialogBuilder.setCancelable(false)
        val mView = layoutInflater.inflate(R.layout.image_view_layout, null, false)
        Glide.with(requireActivity()).load(respose!!.graphql.shortcode_media.display_url)
            .into(mView.post_image)
        Glide.with(requireActivity()).load(respose.graphql.shortcode_media.owner.profile_pic_url)
            .into(mView.user_profile_image)
        mView.user_name.text = respose.graphql.shortcode_media.owner.username
        mView.likes_count.text =
            respose.graphql.shortcode_media.edge_media_preview_like.count.toString()
        mView.comments_count.text =
            respose.graphql.shortcode_media.edge_media_preview_comment.count.toString()
        val mAlertDialog = mAlertDialogBuilder.create()
        mView.close_dialog.setOnClickListener {
            mAlertDialog.dismiss()
        }
        mView.image_download_bttn.setOnClickListener {

            Utils.downloadFile(
                requireActivity(),
                respose.graphql.shortcode_media.display_url,
                "Intug",
                respose.graphql.shortcode_media.is_video
            )


        }
        mAlertDialog.setView(mView)
        mAlertDialog.show()

    }

    //Asking Runtime Permissions
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            ActivityCompat.requestPermissions(requireActivity(), permissions, 5000)

        }

        return false
    }

    fun openInstagram() {
        val uri: Uri = Uri.parse("http://instagram.com")
        val likeIng = Intent(Intent.ACTION_VIEW, uri)

        likeIng.setPackage("com.instagram.android")

        try {
            startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/xxx")
                )
            )
        }
    }


}