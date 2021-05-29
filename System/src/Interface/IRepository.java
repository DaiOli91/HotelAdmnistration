package Interface;

public interface IRepository<T, J> {

    public void add(T t);
    public void delete(J j);
    public T search(J j);
    public void edit(T t);
}
