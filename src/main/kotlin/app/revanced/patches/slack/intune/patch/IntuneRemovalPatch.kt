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
import app.revanced.patches.slack.intune.fingerprints.IntuneRemovalEnterpriseTeamsSigninResponseFingerprint
import app.revanced.patches.slack.intune.fingerprints.IntuneRemovalTeamPrefsFingerprint
import app.revanced.patches.slack.intune.fingerprints.IntuneRemovalUserPermissionsImplIsGuestFingerprint
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.builder.instruction.BuilderInstruction11n
import org.jf.dexlib2.builder.instruction.BuilderInstruction11x
import org.jf.dexlib2.builder.instruction.BuilderInstruction21c
import org.jf.dexlib2.builder.instruction.BuilderInstruction22c
import org.jf.dexlib2.immutable.reference.ImmutableStringReference

@Patch
@Name("slack-intune-removal")
@Description("Removes Intune requirement.")
@Compatibility([Package("com.Slack")]) //TODO: Slack does not use semantic versioning :(
@Version("0.0.2")
class IntuneRemovalPatch : BytecodePatch(
    listOf(
        IntuneRemovalTeamPrefsFingerprint,
        IntuneRemovalEnterpriseTeamsSigninResponseFingerprint,
        IntuneRemovalUserPermissionsImplIsGuestFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        // Removes intune from signin response.
        with(IntuneRemovalEnterpriseTeamsSigninResponseFingerprint.result!!) {
            val implementation = mutableMethod.implementation!!
            val index = implementation.instructions.size
            val regP0 = implementation.registerCount - mutableMethod.parameters.size - 2
            val fieldList = mutableClass.fields.associateBy { field -> field.name }

            implementation.addInstructions(
                index - 1, listOf(
                    BuilderInstruction11n(Opcode.CONST_4, 0, 0),
                    BuilderInstruction22c(Opcode.IPUT_BOOLEAN, 0, regP0, fieldList["isIntuneEnabled"]!!),
                    BuilderInstruction22c(Opcode.IPUT_BOOLEAN, 0, regP0, fieldList["mdmRequired"]!!),
                    BuilderInstruction21c(Opcode.CONST_STRING, 0, ImmutableStringReference(
                        "tthorntone"
                    )),
                    BuilderInstruction22c(Opcode.IPUT_OBJECT, 0, regP0, fieldList["warning"]!!)
                )
            )
        }

        // TODO: Might not be necessary.
        // Sets User as NOT a guest.
        with(IntuneRemovalUserPermissionsImplIsGuestFingerprint.result!!) {
            val implementation = mutableMethod.implementation!!

            implementation.addInstructions(
                0, listOf(
                    BuilderInstruction11n(Opcode.CONST_4, 0, 0),
                    BuilderInstruction11x(Opcode.RETURN, 0)
                )
            )
        }
        
        // Removes intune from team prefs.
        with(IntuneRemovalTeamPrefsFingerprint.result!!) {
            val implementation = mutableMethod.implementation!!
            val index = implementation.instructions.size
            val fieldList = mutableClass.fields.associateBy { field -> field.name }

            implementation.addInstructions(
                index - 1, listOf(
                    BuilderInstruction11n(Opcode.CONST_4, 1, 0),
                    BuilderInstruction22c(Opcode.IPUT_BOOLEAN, 1, 0, fieldList["enterpriseIntuneEnabled"]!!),
                    BuilderInstruction22c(Opcode.IPUT_OBJECT, 1, 0, fieldList["enterpriseFlexibleAccessControl"]!!)
                )
            )
        }

        return PatchResultSuccess()
    }
}