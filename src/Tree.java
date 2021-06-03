import java.util.ArrayList;
import java.util.List;

public class Tree<T> {
    private T data;
    private List<Tree<T>> children;
    
    public Tree() {
        this.children = new ArrayList<Tree<T>>();
    }
    public Tree(T data) {
        this.data = data;
        this.children = new ArrayList<Tree<T>>();
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public List<Tree<T>> getChildren() {
        return children;
    }
    public void setChildren(List<Tree<T>> children) {
        this.children = children;
    }
    public Tree<T> getChild(int index) {
        return children.get(index);
    }
    public void setChild(int index, Tree<T> children) {
        this.children.set(index, children);
    }
    public void addChild(Tree<T> children) {
        this.children.add(children);
    }
}
