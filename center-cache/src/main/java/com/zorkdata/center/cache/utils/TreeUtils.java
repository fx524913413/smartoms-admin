package com.zorkdata.center.cache.utils;

import com.zorkdata.center.cache.vo.CacheTree;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/3/19 9:13
 */
public class TreeUtils {
    public static List<CacheTree> buildTree(List<CacheTree> trees) {
        List<CacheTree> list = new ArrayList<CacheTree>();
        for (CacheTree tree : trees) {
            if (tree.getParentId().equals("-1")) {
                list.add(tree);
            }
            for (CacheTree t : trees) {
                if (t.getParentId().equals(tree.getId())) {
                    if (tree.getNodes() == null) {
                        List<CacheTree> myChildrens = new ArrayList<CacheTree>();
                        myChildrens.add(t);
                        tree.setNodes(myChildrens);
                    } else {
                        tree.getNodes().add(t);
                    }
                }
            }
        }
        return list;
    }
}
