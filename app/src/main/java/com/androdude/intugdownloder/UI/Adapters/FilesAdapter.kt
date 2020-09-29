package com.androdude.intugdownloder.UI.Adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.androdude.intugdownloder.R
import com.androdude.intugdownloder.UI.Model.FileClass
import com.androdude.intugdownloder.UI.Utils.Utils

class FilesAdapter : RecyclerView.Adapter<FilesAdapter.mImageViewHolder>() {

    private lateinit var fileList : List<FileClass>
    private lateinit var mListener: onListItemClick
    interface onListItemClick
    {
        fun getListItem(pos : Int,boolean: Boolean)
    }

    fun setOnListItemClick(mObjectListener: onListItemClick)
    {
        mListener=mObjectListener
    }

    fun setImageList(mList : List<FileClass>)
    {
        fileList=mList
        notifyDataSetChanged()
    }



    inner class mImageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val mImageView = itemView.findViewById<ImageView>(R.id.gallery_imageView)
        val mVideoView = itemView.findViewById<ConstraintLayout>(R.id.gallery_videoView)
        val mFileName = itemView.findViewById<TextView>(R.id.gallery_file_name)
        val mFileCreatedDate = itemView.findViewById<TextView>(R.id.gallery_created_date)
        val mFileDeleteButton = itemView.findViewById<ImageView>(R.id.file_delete)

        init {

            mFileDeleteButton.setOnClickListener {
                if(mListener != null && adapterPosition != RecyclerView.NO_POSITION )
                {
                    mListener.getListItem(adapterPosition,true)
                }
            }

            itemView.setOnClickListener {
                if(mListener != null && adapterPosition != RecyclerView.NO_POSITION )
                {
                    mListener.getListItem(adapterPosition,false)
                }
            }





        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mImageViewHolder {
        return mImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_image_view,parent,false))
    }

    override fun getItemCount(): Int {
                return fileList.size
    }

    override fun onBindViewHolder(holder: mImageViewHolder, position: Int) {
       if( Utils.checkFileExtention(fileList[position].file))
       {
           holder.mImageView.visibility = View.VISIBLE
           holder.mImageView.setImageBitmap(BitmapFactory.decodeFile(fileList[position].file.path))
       }
        else
       {
           holder.mVideoView.visibility = View.VISIBLE
       }
        holder.mFileName.text = fileList[position].file.name
        holder.mFileCreatedDate.text = Utils.setConvertedTime(fileList[position].file.lastModified())

    }



}