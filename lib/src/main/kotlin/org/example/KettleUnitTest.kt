package org.example

import io.grpc.BindableService
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.testing.GrpcCleanupRule
import org.junit.Rule
import kotlin.reflect.KClass

class KettleUnitTest {
    @Rule
    protected val grpcCleanupRule = GrpcCleanupRule()

    protected class KettleMockService {
        inline fun <reified T : AbstractCoroutineServerImpl> forService(): T {

        }
    }

    protected inline fun <reified T : BindableService> mockServer(): T {
        val serverName = InProcessServerBuilder.generateName()

        val serviceInstance = runCatching {
            T::class.java.getDeclaredConstructor().newInstance()
        }.getOrElse {
            throw IllegalArgumentException("Cannot create a service instance for type ${T::class.simpleName}. Ensure it has a constructor with no required arguments")
        }

        grpcCleanupRule.register(
            InProcessServerBuilder.forName(serverName).directExecutor().addService(serviceInstance).build()
        ).start()

        grpcCleanupRule.register(InProcessChannelBuilder.forName(serverName).directExecutor().build())


    }

    protected fun <T : AbstractCoroutineServerImpl> T.getClient(): AbstractCoroutineStub<T> {

    }

    private fun <T : BindableService> createServiceInstance(kClass: KClass<T>): T {
        return
    }
}