package bintree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

class TreeNode<T> {
    private final List<TreeNode<T>> children;
    private final T treeData;

    public TreeNode(T treeData) {
        this.treeData = treeData;
        this.children = new ArrayList<>();
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public T getTreeData() {
        return treeData;
    }
}

interface NodeEvent<T> {
    void nodeParentAdded(TreeNode<T> treeNode);
}

public class BinaryTreeBuilder<T> {
    private final TreeNode<T> root;
    private final Map<String, TreeNode<T>> mapNode;
    private final List<NodeEvent<T>> nodeEvents;

    public BinaryTreeBuilder() {
        this.root = new TreeNode<>(null);
        this.mapNode = new HashMap<>();
        this.nodeEvents = new ArrayList<>();
    }

    public TreeNode<T> buildTree(List<T> data, Function<T, String> selfIdFunction, Function<T, String> parentIdFunction) {
        if (data == null || data.isEmpty()) {
            return root;
        }

        for (T item : data) {
            String id = selfIdFunction.apply(item);
            String parentId = parentIdFunction.apply(item);

            TreeNode<T> newNode = new TreeNode<>(item);
            mapNode.put(id, newNode);

            if ("0".equals(parentId)) {
                root.getChildren().add(newNode);
                notifyNodeAdded(newNode);
            } else {
                TreeNode<T> parentNode = mapNode.get(parentId);
                if (parentNode != null) {
                    parentNode.getChildren().add(newNode);
                    notifyNodeAdded(newNode);
                } else {
                    nodeEvents.add((treeNode) -> {
                        if (parentId.equals(selfIdFunction.apply(treeNode.getTreeData()))) {
                            treeNode.getChildren().add(newNode);
                            notifyNodeAdded(newNode);
                        }
                    });
                }
            }
        }

        if (!root.getChildren().isEmpty()) {
            return root.getChildren().get(0);
        }
        return null;
    }

    private void notifyNodeAdded(TreeNode<T> treeNode) {
        for (NodeEvent<T> event : nodeEvents) {
            event.nodeParentAdded(treeNode);
        }
    }
}
