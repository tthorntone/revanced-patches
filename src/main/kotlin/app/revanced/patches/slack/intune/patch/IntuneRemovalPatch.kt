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
import app.revanced.patches.slack.intune.fingerprints.IntuneRemovalFingerprint

@Patch
@Name("slack-intune-removal")
@Description("Removes Intune requirement.")
@Compatibility([Package("com.Slack")]) //TODO: Slack does not use semantic versioning :(
@Version("0.0.2")
class IntuneRemovalPatch : BytecodePatch(
    listOf(
        IntuneRemovalFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {
        IntuneRemovalFingerprint.result!!.mutableMethod.addInstructions(
            0, """
                return-void
                """
        )

        return PatchResultSuccess()
    }
}