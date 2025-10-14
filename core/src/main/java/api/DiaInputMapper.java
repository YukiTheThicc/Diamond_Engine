package api;
/**
 * DiaInputMapper
 * @author Santiago Barreiro
 */
public interface DiaInputMapper {

    public void keyCallback(long window, int key, int scancode, int action, int mods);

    public void mouseCallback();
}
