package com.lys.community.controller;

import com.lys.community.entity.User;
import com.lys.community.service.LikeService;
import com.lys.community.utils.CommunityUtils;
import com.lys.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType, int entityId) {
        User user = hostHolder.getUser();
    //    点赞
        likeService.like(user.getId(), entityType, entityId);
    //    数量
        long entityLikeCount = likeService.findEntityLikeCount(entityType, entityId);
    //    状态
        int entityLikeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

        HashMap<String, Object> map = new HashMap<>();
        map.put("likeCount", entityLikeCount);
        map.put("likeStatus", entityLikeStatus);
        return CommunityUtils.getJSONString(0, null, map);
    }
}
