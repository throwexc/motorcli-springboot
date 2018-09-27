package com.motorcli.springboot.restful.dto.converter;

import com.motorcli.springboot.restful.dto.TreeEntityDataModel;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TreeDataChanger<T extends TreeEntityDataModel> {

    private Collection<T> data;

    private String topId = null;

    public TreeDataChanger(Collection<T> data) {
        this.data = data;
    }

    public TreeDataChanger(Collection<T> data, String topId) {
        this.data = data;
        this.topId = topId;
    }

    /**
     * 转化树形数据
     * @return 树形集合数据
     */
    public List<T> toTree() {
        return this.toTree(true);
    }

    /**
     * 转化树形数据
     * @param isTop 是否有顶级节点， 如果为 true 将从集合中找出 parentId 为空的数据做为顶级， 否则会从所有数据中找到没有父节点的数据做为顶级
     * @return 树形集合数据
     */
    public List<T> toTree(boolean isTop) {
        if(isTop == true) {
            List<T> topList = this.findTopList(this.data);
            return changeTree(topList, this.data);
        } else {
            List<T> topList = this.findTopListByAll(this.data);
            return changeTree(topList, this.data);
        }
    }

    /**
     * 从所有节点中找出顶级节点
     * @param list 节点集合
     * @return 顶级节点集合
     */
    public List<T> findTopListByAll(Collection<T> list) {
        List<T> topList = new ArrayList<>();

        for (T dto : list) {

            if (StringUtils.isEmpty(dto.getParentId())) {
                topList.add(dto);
            } else {
                boolean hasParent = false;

                for (T temp : list) {
                    if (dto.getParentId().equals(temp.getId())) {
                        hasParent = true;
                        break;
                    }
                }

                if (hasParent == true) {
                    topList.add(dto);
                }
            }
        }

        return topList;
    }

    /**
     * 数据转换
     * @param topList 顶级节点集合
     * @param list 数据集合
     * @return 树形数据
     */
    private List<T> changeTree(Collection<T> topList, Collection<T> list) {
        List<T> nodes = new ArrayList<>();
        for(T model : topList) {
            List<T> children = this.findChildrenList(model, this.data);
            if(children.size() > 0) {
                model.setChildren(this.childrenDataToTreeList(children, this.data));
            }
            nodes.add(model);
        }

        return nodes;
    }

    /**
     * 递归子节点
     */
    private List<T> childrenDataToTreeList(Collection<T> children, Collection<T> list) {
        List<T> nodes = new ArrayList<>();

        for(T model : children) {
            List<T> childrenList = this.findChildrenList(model, list);
            if(childrenList.size() > 0) {
                model.setChildren(this.childrenDataToTreeList(childrenList, list));
            }
            nodes.add(model);
        }

        return nodes;
    }

    /**
     * 查询顶级节点
     */
    private List<T> findTopList(Collection<T> list) {
        List<T> topList = new ArrayList<>();

        for(T model : list) {
            if(this.topId == null) {
                if(StringUtils.isEmpty(model.getParentId())) {
                    topList.add(model);
                }
            } else {
                if(this.topId.equals(model.getId())) {
                    topList.add(model);
                }
            }
        }
        return topList;
    }

    /**
     * 查询子节点
     */
    private List<T> findChildrenList(T parent, Collection<T> list) {
        List<T> children = new ArrayList<>();
        for(T model : list) {
            if(!StringUtils.isEmpty(model.getParentId())) {
                if(parent.getId().equals(model.getParentId())) {
                    children.add(model);
                }
            }
        }
        return children;
    }
}
