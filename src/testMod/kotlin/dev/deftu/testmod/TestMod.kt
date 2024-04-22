package dev.deftu.testmod

//#if FABRIC
import net.fabricmc.api.ClientModInitializer
//#else
//#if MC >= 1.15.2
//$$ import net.minecraftforge.fml.common.Mod
//$$ import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
//$$ import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
//#else
//$$ import net.minecraftforge.fml.common.Mod
//$$ import net.minecraftforge.fml.common.Mod.EventHandler
//$$ import net.minecraftforge.fml.common.event.FMLInitializationEvent
//#endif
//#endif

import dev.deftu.omnicore.client.OmniClient
import dev.deftu.omnicore.client.OmniScreen

//#if FABRIC
class TestMod : ClientModInitializer {
//#else
//#if MC >= 1.15.2
//$$ @Mod(value = "test-mod")
//#else
//$$ @Mod(modid = "test-mod")
//#endif
//$$ class TestMod {
//#endif
    private var openScreen = false

    //#if FORGE && MC >= 1.15.2
    //$$ init {
    //$$     FMLJavaModLoadingContext.get().modEventBus.register(this)
    //$$ }
    //#endif

    //#if FABRIC
    override
    //#endif
    fun onInitializeClient(
        //#if FORGE
        //#if MC >= 1.15.2
        //$$ event: FMLClientSetupEvent
        //#else
        //$$ event: FMLInitializationEvent
        //#endif
        //#endif
    ) {
        //#if FABRIC && MC >= 1.19.3
        net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT.register { dispatcher, registryAccess ->
            dispatcher.register(net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal("componency").executes { ctx ->
                openScreen = true
                1
            })
        }

        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.START_CLIENT_TICK.register {
            handleTick()
        }
        //#endif
    }

    private fun handleTick() {
        if (openScreen) {
            openScreen = false
            OmniClient.execute {
                OmniScreen.openScreen(TestScreen())
            }
        }
    }
}
