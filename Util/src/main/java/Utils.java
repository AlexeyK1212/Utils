
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;


public class Utils {


    public static <T extends Fractionable> T cache(T object) {
        return (T) Proxy.newProxyInstance(
                object.getClass().getClassLoader(),
                new Class<?>[]{Fractionable.class},
                new InvocationHandler() {
                    private final Map<Method, Object> cache = new HashMap<>();
                    private boolean stateChanged = false;

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


                        // Проверка на то что метод помечен как Mutator
                        if (method.isAnnotationPresent(Mutator.class)) {
                            stateChanged = true; // Модификация статуса что был вызван мутатор
                            return method.invoke(object, args);
                        }

                        //Проверка на то что метод помечен как Cache
                        if (method.isAnnotationPresent(Cache.class)) {
                            // Возвращаем сохраненный ранее результат Метод не запускаем
                            if (cache.containsKey(method) && !stateChanged) {
                                return cache.get(method);
                            } else {
                                // Вызов оригинального метода и сохранение результата
                                Object result = method.invoke(object, args);
                                cache.put(method, result);
                                stateChanged = false; //Меняем флаг статуса
                                return result;
                            }
                        }

                        // Если метод без аннотации
                        return method.invoke(object, args);
                    }
                }
        );
    }

}
