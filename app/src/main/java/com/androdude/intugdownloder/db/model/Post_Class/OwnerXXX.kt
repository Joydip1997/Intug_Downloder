package com.androdude.intugdownloder.db.model

data class OwnerXXX(
    val blocked_by_viewer: Boolean,
    val edge_followed_by: EdgeFollowedBy,
    val edge_owner_to_timeline_media: EdgeOwnerToTimelineMedia,
    val followed_by_viewer: Boolean,
    val full_name: String,
    val has_blocked_viewer: Boolean,
    val id: String,
    val is_private: Boolean,
    val is_unpublished: Boolean,
    val is_verified: Boolean,
    val pass_tiering_recommendation: Boolean,
    val profile_pic_url: String,
    val requested_by_viewer: Boolean,
    val restricted_by_viewer: Boolean,
    val username: String
)