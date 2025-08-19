package com.example.openidconnect;

import com.example.openidconnect.views.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
public class OpenIdConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenIdConnectApplication.class, args);
        start();
    }

    public static void start(){
        Menu.show();
    }

}
