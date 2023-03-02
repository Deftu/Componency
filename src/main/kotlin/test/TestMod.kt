//#if MC>=11900
package test

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.client.MinecraftClient
import java.util.Timer
import java.util.TimerTask

class TestMod : ClientModInitializer {
    override fun onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(ClientCommandManager.literal("componency_test").executes {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        MinecraftClient.getInstance().execute {
                            MinecraftClient.getInstance().setScreen(TestScreen())
                        }
                    }
                }, 10)

                1
            })
        }
    }
}
//#endif
