package com.itis.oris.di;

import com.itis.oris.di.component.Application;
import com.itis.oris.di.config.Context;

public class Main {
    public static void main(String[] args) {
        Context context = new Context();

        Application application = (Application) context.getComponent(Application.class);
        application.run();
    }
}
