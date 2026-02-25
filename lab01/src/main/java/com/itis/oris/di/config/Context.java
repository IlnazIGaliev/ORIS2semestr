package com.itis.oris.di.config;

import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {

    private final String scanPath = "com.itis.oris.di";
    @Getter
    private final Map<Class<?>, Object> components = new HashMap<>();

    public Context() {
        scanComponent();
    }

    public Object getComponent(Class<?> clazz) {
        return components.get(clazz);
    }

    private void scanComponent() {

        List<Class<?>> classes = PathScan.find(scanPath);

        int remaining = classes.size();

        while (remaining > 0) {

            boolean createdSomething = false;

            for (Class<?> clazz : classes) {

                // Уже создан
                if (components.containsKey(clazz)) {
                    continue;
                }

                // Игнорируем неподходящие типы
                if (clazz.isInterface()
                        || clazz.isAnnotation()
                        || Modifier.isAbstract(clazz.getModifiers())) {
                    continue;
                }

                Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                if (constructors.length == 0) {
                    continue;
                }

                Constructor<?> constructor = constructors[0];
                constructor.setAccessible(true);

                Class<?>[] paramTypes = constructor.getParameterTypes();
                Object[] args = new Object[paramTypes.length];

                boolean canCreate = true;

                for (int i = 0; i < paramTypes.length; i++) {
                    Object dependency = components.get(paramTypes[i]);
                    if (dependency == null) {
                        canCreate = false;
                        break;
                    }
                    args[i] = dependency;
                }

                if (!canCreate) {
                    continue;
                }

                try {
                    Object instance = constructor.newInstance(args);
                    components.put(clazz, instance);
                    remaining--;
                    createdSomething = true;

                    System.out.println("Создан бин: " + clazz.getName());

                } catch (Exception e) {
                    throw new RuntimeException("Ошибка создания бина: "
                            + clazz.getName(), e);
                }
            }

            // Если ни один бин не создан за итерацию — зависимость неразрешима
            if (!createdSomething) {
                throw new RuntimeException(
                        "Невозможно разрешить зависимости. "
                                + "Проверьте циклические зависимости или отсутствующие компоненты.");
            }
        }
    }
}