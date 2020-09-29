package com.androdude.intugdownloder.UI

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androdude.intugdownloder.R
import com.androdude.intugdownloder.UI.Adapters.FilesAdapter
import com.androdude.intugdownloder.UI.Model.FileClass
import com.androdude.intugdownloder.UI.Utils.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_gallery.view.*
import java.io.File


class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private lateinit var mAlertDialog: AlertDialog
    private lateinit var mList : ArrayList<FileClass>
    private lateinit var mAdapter: FilesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().toolbar_title.text="Downloads"

        //To access gallery in the phone
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()

        mList = ArrayList()
        mAdapter= FilesAdapter()
        mAdapter.setImageList(mList)
        getFiles(view)
        view.gallery_view.adapter=mAdapter
        view.gallery_view.layoutManager = LinearLayoutManager(requireContext())
        fileDelete(view)
    }

    //get the images and videos from the download folder
   fun getFiles(view: View)
    {
        val path: String =
            Environment.getExternalStorageDirectory().toString().toString() + "/intug"
        Log.d("Files", "Path: $path")
        val directory = File(path)
        val files: Array<File>? = directory.listFiles()
        Log.d("Files", "Size: " + files?.size)
        val x = files.isNullOrEmpty()

        if(files.isNullOrEmpty())
        {
            view.no_files_animation_view.visibility=View.VISIBLE
            view.gallery_view.visibility=View.INVISIBLE
        }
        else
        {
            for (i in files.indices) {
               mList.add(FileClass(files[i]))
          }
            view.no_files_animation_view.visibility=View.INVISIBLE
            view.gallery_view.visibility=View.VISIBLE

        }
    }

    //Delete the file function
    fun fileDelete(view: View)
    {
        mAdapter.setOnListItemClick(object : FilesAdapter.onListItemClick
        {


            override fun getListItem(pos: Int, boolean: Boolean) {
                val listFileItem = mList[pos]
                if(boolean)
                {
                    wantToDelete(pos,view)
                }
                else
                {
                    showFile(listFileItem.file)
                }
            }


        })
    }

    //show the images/videos in gallery
    private fun showFile(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        if(Utils.checkFileExtention(file))
        {
            intent.setDataAndType( Uri.fromFile(file)
                , "image/*")
        }
        else
        {
            intent.setDataAndType( Uri.fromFile(file)
                , "video/*")
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }


    //Want to delete dialog
    private fun wantToDelete(pos : Int,view: View)
    {
        val mAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        mAlertDialogBuilder.setTitle("Hey!")
        mAlertDialogBuilder.setMessage("Do you want to delete the file?")
        mAlertDialogBuilder.setPositiveButton("Delete",object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val selectedFile = mList[pos].file
                mList.removeAt(pos)
                mAdapter.setImageList(mList)
                selectedFile.delete()
                if(mList.isEmpty())
                {
                    view.no_files_animation_view.visibility=View.VISIBLE
                    view.gallery_view.visibility=View.INVISIBLE
                }
                else
                {
                    view.no_files_animation_view.visibility=View.INVISIBLE
                    view.gallery_view.visibility=View.VISIBLE
                }
            }

        })
        mAlertDialogBuilder.setNegativeButton("Cancel",object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                    mAlertDialog.dismiss()
            }

        })
        mAlertDialog = mAlertDialogBuilder.create()
        mAlertDialog.show()
    }

}