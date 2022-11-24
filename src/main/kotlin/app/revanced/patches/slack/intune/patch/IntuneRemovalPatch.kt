package app.revanced.patches.slack.intune.patch

import app.revanced.patcher.annotation.Compatibility
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Package
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.removeInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.ecmwf.misc.subscription.fingerprints.SubscriptionUnlockFingerprint

@Patch
@Name("slack-intune-removal")
@Description("Removes Intune requirement.")
@Compatibility([Package("com.Slack", arrayOf("22.09.40.0"))])
@Version("0.0.1")
class IntuneRemovalPatch : BytecodePatch(
    listOf(
        SubscriptionUnlockFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {
        val result = SubscriptionUnlockFingerprint.result!!
        val method = result.mutableMethod

        val index = method.implementation!!.instructions.size

        // remove R() at 10212
        method.removeInstruction(index - 3)
        // remove R() at 10206
        method.removeInstruction(index - 5)

        val insertIndex = index

        method.addInstructions(
            insertIndex - 1 - 2, 
            """
                const/4 p1, 0x1
            """
        )
        return PatchResultSuccess()
    }
}