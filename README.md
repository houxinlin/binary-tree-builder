```java
    public static void main(String[] args) {

        User root = new User();

        root.setData("root");
        root.setId("root");
        root.setParentId("0");
        List<User> data = new ArrayList<>();

        {
            data.add(new User("1", "root-1", "root"));
            data.add(new User("1", "root-1-1-1", "root-1-1"));
            data.add(new User("1", "root-1-1", "root-1"));
            data.add(root);
        }

        TreeNode<User> userTreeNode = new BinaryTreeBuilder<User>().buildTree(data, User::getId, User::getParentId);
        System.out.println(userTreeNode);

    }
```
