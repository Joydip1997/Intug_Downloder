package com.androdude.intugdownloder.db.model

data class NodeXXX(
    val created_at: Int,
    val did_report_as_spam: Boolean,
    val edge_liked_by: EdgeLikedByXX,
    val id: String,
    val is_restricted_pending: Boolean,
    val owner: OwnerX,
    val text: String,
    val viewer_has_liked: Boolean
)