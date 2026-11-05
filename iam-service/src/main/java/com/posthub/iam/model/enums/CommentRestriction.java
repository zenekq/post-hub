package com.posthub.iam.model.enums;

/**
 * Defines which users are allowed to write comments under a post.
 * Restrictions never hide already created comments, they only block new ones.
 */
public enum CommentRestriction {
    // Every authenticated user can comment - default value.
    ALL,
    // Only the author of the post can comment.
    ONLY_OWNER,
    // Nobody can comment, administrators are the only exception.
    DISABLED
}
