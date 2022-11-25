package app.revanced.patches.slack.intune.fingerprints

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint

object IntuneRemovalUserPermissionsImplIsGuestFingerprint : MethodFingerprint(
    "Z",
    customFingerprint = { methodDef ->
        methodDef.definingClass == "Lslack/corelib/model/permissions/UserPermissionsImpl;" && methodDef.name == "isGuest"
    }
)