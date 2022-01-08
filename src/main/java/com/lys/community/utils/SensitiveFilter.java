package com.lys.community.utils;

import org.springframework.stereotype.Component;

import java.security.cert.TrustAnchor;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感期过滤器
 */
@Component
public class SensitiveFilter {
//    前缀树内部类
    private class TrieNode{
    //    关键词结束标记
    private boolean isKeywordEnd = false;
    //    子节点（key是下级字符，value是下级节点
    private Map<Character, TrieNode> subNode = new HashMap<>();
    public boolean isKeywordEnd() {
        return isKeywordEnd;
    }

    public void setKeywordEnd(boolean isKeywordEnd) {
        this.isKeywordEnd = isKeywordEnd;
    }

    //    添加子节点
    public void addSubNode(Character c, TrieNode node) {
        subNode.put(c, node);
    }
//    获取子节点
    public TrieNode getSubNode(Character c) {
        return subNode.get(c);
    }
}

}
