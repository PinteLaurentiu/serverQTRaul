package com.raul.licenta.configuration

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.stereotype.Component


@Component
class BigFileConfiguration : WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    override fun customize(factory: TomcatServletWebServerFactory?) {
        factory?.addConnectorCustomizers(TomcatConnectorCustomizer { it.maxPostSize = 1024*1024*100 })
    }
}