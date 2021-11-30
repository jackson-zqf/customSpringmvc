package com.zqf.customframwork.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CustomResourceResolver {

    private Properties properties ;

    public CustomResourceResolver(Properties properties){
        this.properties = properties;
    }

    public  void  loadingConfig(String location) throws IOException {
        InputStream resourceAsStream = CustomResourceResolver.class.getClassLoader().getResourceAsStream(location);
        properties.load(resourceAsStream);
    }
}
