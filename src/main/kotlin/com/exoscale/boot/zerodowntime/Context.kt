package com.exoscale.boot.zerodowntime

import org.springframework.beans.factory.annotation.Value
import java.net.InetAddress

class Context {

    @Value("\${info.app.version}")
    internal lateinit var version: String

    internal val hostname: String by lazy { InetAddress.getLocalHost().hostName }
}