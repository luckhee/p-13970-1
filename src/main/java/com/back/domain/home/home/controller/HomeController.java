package com.back.domain.home.home.controller;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

import static java.net.InetAddress.getLocalHost;

@RestController
public class HomeController {
    @SneakyThrows // 이거를 달면 Try chatch를 하지 않아도 된다.
    @GetMapping
    public String main() {
        InetAddress localHost = getLocalHost(); // 로컬 호스트의 IP 주소와 호스트 이름을 가져옵니다.

        return """
                <h1>API 서버</h1>
                <p>Host Name: %s</p>
                <p>Host Address: %s</p>
                <div>
                    <a href="/swagger-ui/index.html">API 문서로 이동</a>
                </div>
                """.formatted(localHost.getHostName(), localHost.getHostAddress());
    }
}
