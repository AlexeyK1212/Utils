import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ObjectInvocationHandler  implements InvocationHandler {

    private final Object object1;
    private final Map<Method, Object> cache = new HashMap<>();
    private boolean stateChanged = false;


    public ObjectInvocationHandler(Object object) {
        this.object1 = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


        // Проверка на то что метод помечен как Mutator
        if (method.isAnnotationPresent(Mutator.class)) {
            stateChanged = true; // Модификация статуса что был вызван мутатор
            return method.invoke(object1, args);
        }

        //Проверка на то что метод помечен как Cache
        if (method.isAnnotationPresent(Cache.class)) {
            // Возвращаем сохраненный ранее результат Метод не запускаем
            if (cache.containsKey(method) && !stateChanged) {
                return cache.get(method);
            } else {
                // Вызов оригинального метода и сохранение результата
                Object result = method.invoke(object1, args);
                cache.put(method, result);
                stateChanged = false; //Меняем флаг статуса
                return result;
            }
        }

        // Если метод без аннотации
        return method.invoke(object1, args);
    }
}
