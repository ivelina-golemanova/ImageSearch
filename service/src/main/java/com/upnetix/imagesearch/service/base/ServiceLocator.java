package com.upnetix.imagesearch.service.base;

import androidx.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {

    private static final Map<String, Object> serviceInstances = new HashMap<>();
    private static final Map<String, Class> serviceImplementationMapping = new HashMap<>();

    /**
     * Return instance of desired class or object that implement desired interface.
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T get(@NonNull Class<T> clazz) {
        return (T) getService(clazz.getName());
    }

    /**
     * This method allows to bind a custom service implementation to an interface.
     *
     * @param interfaceClass      interface
     * @param implementationClass class which implement interface specified in first param
     */
    public static void bindCustomServiceImplementation(@NonNull Class interfaceClass, @NonNull Class implementationClass) {
        serviceImplementationMapping.put(interfaceClass.getName(), implementationClass);
    }

    private static Object getService(@NonNull String name) {
        Object service = serviceInstances.get(name);

        if (service == null) {
            service = createService(name);
        }

        return service;
    }

    @NonNull
    private static Object createService(@NonNull String name) {
        try {
            Object serviceInstance;
            final Class<?> clazz;
            if (serviceImplementationMapping.containsKey(name)) {
                clazz = serviceImplementationMapping.get(name);
            } else {
                clazz = Class.forName(name);
            }

            Constructor constructor = clazz.getConstructor();
            serviceInstance = constructor.newInstance();

            if (!(serviceInstance instanceof IService)) {
                throw new IllegalArgumentException("Requested service must implement IService interface");
            }

            serviceInstances.put(name, serviceInstance);
            return serviceInstance;

        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Requested service class was not found: " + name, e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot initialize requested service: " + name, e);
        }
    }
}
