package com.androdude.intugdownloder.db.model

data class NodeXX(
    val created_at: Int,
    val did_report_as_spam: Boolean,
    val edge_liked_by: EdgeLikedByX,
    val edge_threaded_comments: EdgeThreadedComments,
    val id: String,
    val is_restricted_pending: Boolean,
    val owner: OwnerXX,
    val text: String,
    val viewer_has_liked: Boolean
)