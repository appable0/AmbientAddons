import com.ambientaddons.commands.AmbientCommand
import com.ambientaddons.config.Config
import com.ambientaddons.config.PersistentData
import com.ambientaddons.features.dungeon.AutoBuyChest
import com.ambientaddons.features.dungeon.CancelInteractions
import com.ambientaddons.features.dungeon.CloseChest
import com.ambientaddons.utils.LocationUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.ModMetadata
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.io.File

@Mod(
    modid = "ambientaddons",
    name = "AmbientAddons",
    version = "0.1",
    useMetadata = true,
    clientSideOnly = true
)
class AmbientAddons {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        metadata = event.modMetadata
        val directory = File(event.modConfigurationDirectory, "ambientaddons-forge")
        directory.mkdirs()
        configDirectory = directory
        persistentData = PersistentData.load()
        config = Config.apply { this.initialize() }
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        ClientCommandHandler.instance.registerCommand(AmbientCommand())
        listOf(
            this,
            LocationUtils,
            AutoBuyChest,
            CloseChest,
            CancelInteractions
        ).forEach(MinecraftForge.EVENT_BUS::register)
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || currentGui == null) return
        mc.displayGuiScreen(currentGui)
        currentGui = null
    }

    companion object {
        fun isInitialized(): Boolean {
            return ::config.isInitialized
        }

        val mc: Minecraft = Minecraft.getMinecraft()
        var currentGui: GuiScreen? = null

        lateinit var configDirectory: File
        lateinit var config: Config
        lateinit var persistentData: PersistentData

        lateinit var metadata: ModMetadata
    }
}