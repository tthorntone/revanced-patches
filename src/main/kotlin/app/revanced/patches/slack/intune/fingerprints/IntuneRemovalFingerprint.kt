package app.revanced.patches.slack.intune.fingerprints

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint

object IntuneRemovalFingerprint : MethodFingerprint(
    "V",
    customFingerprint = { methodDef ->
        methodDef.definingClass == "Lslack.features.signin.ui.SignInActivity;") && methodDef.name == "showIntuneRequiredErrorTakeoverScreen"
    }
)