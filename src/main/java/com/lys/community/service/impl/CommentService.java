package com.lys.community.service.impl;

import com.lys.community.dao.CommentDao;
import com.lys.community.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentDao.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    public int findCommentsCount(int entityType, int entityId) {
        return commentDao.selectCountByEntity(entityType, entityId);
    }
}
