package app.revanced.patches.slack.intune.fingerprints

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import org.jf.dexlib2.util.MethodUtil

object IntuneRemovalEnterpriseTeamsSigninResponseFingerprint : MethodFingerprint(
    "V",
    customFingerprint = { methodDef ->
        methodDef.definingClass == "Lslack/services/api/enterprise/EnterpriseTeamsSigninResponse;" &&  MethodUtil.isConstructor(methodDef)
    }
)